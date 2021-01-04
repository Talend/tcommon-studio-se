package org.talend.core.utils;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

public class CodesJarResourceCache {

    private static final Set<Property> CACHE = new HashSet<>();

    private static final Object LOCK = new Object();

    private static boolean isListenerAdded;

    private static PropertyChangeListener listener;

    public static void initCodesJarCache() {
        synchronized (LOCK) {
            CACHE.clear();
            List<Project> allProjects = new ArrayList<>();
            allProjects.addAll(ProjectManager.getInstance().getAllReferencedProjects(true));
            allProjects.add(ProjectManager.getInstance().getCurrentProject());
            try {
                for (Project project : allProjects) {
                    for (ERepositoryObjectType codesJarType : ERepositoryObjectType.getAllTypesOfCodesJar()) {
                        List<IRepositoryViewObject> objects = getProxyRepositoryFactory().getAllCodesJars(project, codesJarType);
                        for (IRepositoryViewObject obj : objects) {
                            CACHE.add(obj.getProperty());
                        }
                    }
                }
                addCodesJarChangeListener();
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    public static Set<Property> getAllCodesJars() {
        synchronized (LOCK) {
            return new HashSet<>(CACHE);
        }
    }

    public static Property getCodesJarById(String id) {
        synchronized (LOCK) {
            Optional<Property> optional = CACHE.stream().filter(p -> p.getId().equals(id)).findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }
            return null;
        }
    }

    public static Property getCodesJarByLabel(String label) {
        synchronized (LOCK) {
            Optional<Property> optional = CACHE.stream().filter(p -> p.getLabel().equals(label)).findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }
            return null;
        }
    }

    public static void addToCache(Property newProperty) {
        synchronized (LOCK) {
            Iterator<Property> iterator = CACHE.iterator();
            while (iterator.hasNext()) {
                Property oldProperty = iterator.next();
                if (newProperty.getId().equals(oldProperty.getLabel()) && newProperty.getLabel().equals(oldProperty.getLabel())
                        && newProperty.getVersion().equals(oldProperty.getVersion())) {
                    iterator.remove();
                }
            }
            CACHE.add(newProperty);
        }
    }

    public static void updateCache(String oldId, String oldLabel, String oldVersion, Property newProperty) {
        synchronized (LOCK) {
            Iterator<Property> iterator = CACHE.iterator();
            while (iterator.hasNext()) {
                Property oldProperty = iterator.next();
                if ((oldId == null || (oldId != null && oldId.equals(oldProperty.getId())))
                        && oldLabel.equals(oldProperty.getLabel())
                        && oldVersion.equals(oldProperty.getVersion())) {
                    iterator.remove();
                }
            }
            CACHE.add(newProperty);
        }
    }

    public static void removeCache(Property property) {
        synchronized (LOCK) {
            Iterator<Property> iterator = CACHE.iterator();
            while (iterator.hasNext()) {
                Property oldProperty = iterator.next();
                if (oldProperty.getId().equals(property.getId()) && oldProperty.getLabel().equals(property.getLabel())
                        && oldProperty.getVersion().equals(property.getVersion())) {
                    iterator.remove();
                }
            }
        }
    }

    private static IProxyRepositoryFactory getProxyRepositoryFactory() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            return GlobalServiceRegister.getDefault().getService(IProxyRepositoryService.class).getProxyRepositoryFactory();
        }
        return null;
    }

    public static void addCodesJarChangeListener() {
        if (!isListenerAdded) {
            synchronized (LOCK) {
                if (!isListenerAdded) {
                    if (listener == null) {
                        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                            IRunProcessService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
                            listener = service.addCodesJarChangeListener();
                            isListenerAdded = true;
                        }
                    }
                }
            }
        }
    }

}
