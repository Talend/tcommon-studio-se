package org.talend.core.utils;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;
import org.talend.repository.model.IRepositoryService;

public class CodesJarResourceCache {

    private static final Set<CodesJarInfo> CACHE = new LinkedHashSet<>();

    private static final Object LOCK = new Object();

    private static AtomicBoolean isInitialized = new AtomicBoolean(false);

    private static AtomicBoolean isInitializing = new AtomicBoolean(true);

    private static PropertyChangeListener listener;

    public static void initCodesJarCache() {
        if (isInitialized.compareAndSet(false, true)) {
            CACHE.clear();
            List<Project> allProjects = new ArrayList<>();
            allProjects.addAll(ProjectManager.getInstance().getAllReferencedProjects(true));
            allProjects.add(ProjectManager.getInstance().getCurrentProject());
            IProxyRepositoryFactory factory = IProxyRepositoryService.get().getProxyRepositoryFactory();
            try {
                for (Project project : allProjects) {
                    for (ERepositoryObjectType codesJarType : ERepositoryObjectType.getAllTypesOfCodesJar()) {
                        List<IRepositoryViewObject> objects = factory.getAllCodesJars(project, codesJarType);
                        for (IRepositoryViewObject obj : objects) {
                            CACHE.add(CodesJarInfo.create(obj.getProperty()));
                        }
                    }
                }
                if (listener == null) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                        IRunProcessService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
                        listener = service.addCodesJarChangeListener();
                    }
                }
                isInitializing.set(false);
            } catch (PersistenceException e) {
                throw new RuntimeException("Failed to init resource cache for custom jars", e);
            }
        }
    }

    public static Set<CodesJarInfo> getAllCodesJars() {
        waitForInit();
        return new LinkedHashSet<>(CACHE);
    }

    public static CodesJarInfo getCodesJarById(String id) {
        waitForInit();
        Optional<CodesJarInfo> optional = CACHE.stream().filter(info -> info.getId().equals(id)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        ExceptionHandler.process(new Exception("Codes jar id [" + id + "] is not found!")); //$NON-NLS-1$ //$NON-NLS-2$
        return null;
    }

    public static CodesJarInfo getCodesJarByLabel(ERepositoryObjectType type, String projectTechName, String label) {
        waitForInit();
        Optional<CodesJarInfo> optional = CACHE.stream().filter(info -> info.getType() == type && info.getLabel().equals(label)
                && info.getProjectTechName().equals(projectTechName)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        ExceptionHandler.process(new Exception("Codes jar [" + label + "] is not found!")); //$NON-NLS-1$ //$NON-NLS-2$
        return null;
    }

    public static CodesJarInfo getCodesJarByInnerCode(RoutineItem routineItem) throws PersistenceException {
        String codesJarName = RoutinesUtil.getCodesJarLabelByInnerCode(routineItem);
        String projectTechName = ProjectManager.getInstance().getProject(routineItem).getTechnicalLabel();
        Optional<CodesJarInfo> optional = getAllCodesJars().stream()
                .filter(info -> info.getLabel().equals(codesJarName) && info.getProjectTechName().equals(projectTechName))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        ExceptionHandler.process(new Exception("Codes jar [" + routineItem.getProperty().getLabel() + "] is not found!")); //$NON-NLS-1$ //$NON-NLS-2$
        return null;
    }

    public static void addToCache(Property newProperty) {
        waitForInit();
        synchronized (LOCK) {
            Iterator<CodesJarInfo> iterator = CACHE.iterator();
            while (iterator.hasNext()) {
                CodesJarInfo oldInfo = iterator.next();
                if (newProperty.getId().equals(oldInfo.getId()) && newProperty.getLabel().equals(oldInfo.getLabel())
                        && newProperty.getVersion().equals(oldInfo.getVersion())) {
                    iterator.remove();
                }
            }
            CACHE.add(CodesJarInfo.create(newProperty));
        }
    }

    public static void updateCache(String oldId, String oldLabel, String oldVersion, Property newProperty) {
        waitForInit();
        synchronized (LOCK) {
            Iterator<CodesJarInfo> iterator = CACHE.iterator();
            while (iterator.hasNext()) {
                CodesJarInfo oldInfo = iterator.next();
                if ((oldId == null || (oldId != null && oldId.equals(oldInfo.getId()))) && oldLabel.equals(oldInfo.getLabel())
                        && oldVersion.equals(oldInfo.getVersion())) {
                    iterator.remove();
                }
            }
            CACHE.add(CodesJarInfo.create(newProperty));
        }
    }

    public static void removeCache(Property property) {
        waitForInit();
        synchronized (LOCK) {
            Iterator<CodesJarInfo> iterator = CACHE.iterator();
            while (iterator.hasNext()) {
                CodesJarInfo oldInfo = iterator.next();
                if (oldInfo.getId().equals(property.getId()) && oldInfo.getLabel().equals(property.getLabel())
                        && oldInfo.getVersion().equals(property.getVersion())) {
                    iterator.remove();
                }
            }
        }
    }

    public static void reset() {
        if (isInitialized.compareAndSet(true, false)) {
            if (listener != null) {
                IRepositoryService.get().getProxyRepositoryFactory().removePropertyChangeListener(listener);
                listener = null;
            }
            isInitializing.set(true);
        }
    }

    public static void waitForInit() {
        initCodesJarCache();
        int spent = 0;
        int time = 500;
        int timeout = 1000 * 60 * 10;
        while (isInitializing.get()) {
            try {
                Thread.sleep(time);
                spent += time;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (spent >= timeout) {
                // may be track in dead lock, throw exception to try to break dead lock
                throw new RuntimeException("Waiting for custom jar cache initialization timeout!"); //$NON-NLS-1$
            }
        }
    }

}
