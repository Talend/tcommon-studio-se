package org.talend.core.utils;

import java.beans.PropertyChangeEvent;
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
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.ProjectManager;
import org.talend.repository.documentation.ERepositoryActionName;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * !!!FIXME!!! Should maintain this cache for any case when A/D/M codesJars. check callers of
 * {@link ProxyRepositoryFactory} getAllCodeJars to replace
 */

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

    public static void addCodesJarCache(Property property) {
        synchronized (LOCK) {
            CACHE.add(property);
        }
    }

    public static Set<Property> getAllCodesJar() {
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
                        listener = new PropertyChangeListener() {

                            @Override
                            public void propertyChange(PropertyChangeEvent event) {
                                String propertyName = event.getPropertyName();
                                Object oldValue = event.getOldValue();
                                Object newValue = event.getNewValue();
                                if (propertyName.equals(ERepositoryActionName.PROPERTIES_CHANGE.getName())) {
                                    casePropertiesChange(oldValue, newValue);
                                } else if (propertyName.equals(ERepositoryActionName.DELETE_FOREVER.getName())
                                        || propertyName.equals(ERepositoryActionName.DELETE_TO_RECYCLE_BIN.getName())) {
                                    caseDelete(newValue);
                                } else if (propertyName.equals(ERepositoryActionName.SAVE.getName())
                                        || propertyName.equals(ERepositoryActionName.CREATE.getName())) {
                                    caseCreateOrSave(newValue);
                                } else if (propertyName.equals(ERepositoryActionName.IMPORT.getName())) {
                                    caseImport(propertyName, newValue);
                                } else if (propertyName.equals(ERepositoryActionName.RESTORE.getName())) {
                                    caseRestore(newValue);
                                }
                            }

                            private void casePropertiesChange(Object oldValue, Object newValue) {
                                if (oldValue instanceof String[] && newValue instanceof Property) {
                                    Property property = (Property) newValue;
                                    if (!needUpdate(property.getItem())) {
                                        return;
                                    }
                                    String[] oldFields = (String[]) oldValue;
                                    String oldName = oldFields[0];
                                    String oldVersion = oldFields[1];
                                    Iterator<Property> iterator = getAllCodesJar().iterator();
                                    while (iterator.hasNext()) {
                                        Property oldProperty = iterator.next();
                                        if (oldProperty.getLabel().equals(oldName)
                                                && oldProperty.getVersion().equals(oldVersion)) {
                                            iterator.remove();
                                        }
                                    }
                                    getAllCodesJar().add(property);
                                }
                            }

                            private void caseDelete(Object newValue) {
                                if (newValue instanceof IRepositoryViewObject) {
                                    Property property = ((IRepositoryViewObject) newValue).getProperty();
                                    if (needUpdate(property.getItem())) {
                                        Iterator<Property> iterator = getAllCodesJar().iterator();
                                        while (iterator.hasNext()) {
                                            Property oldProperty = iterator.next();
                                            if (oldProperty.getId().equals(property.getId())
                                                    && oldProperty.getLabel().equals(property.getLabel())
                                                    && oldProperty.getVersion().equals(property.getVersion())) {
                                                iterator.remove();
                                            }
                                        }
                                    }
                                }
                            }

                            private void caseCreateOrSave(Object newValue) {
                                if (newValue instanceof Item) {
                                    Item item = (Item) newValue;
                                    updateCache(item.getProperty());
                                }
                            }

                            private void caseImport(String propertyName, Object newValue) {
                                if (newValue instanceof Set) {
                                    Set<Item> importItems = (Set<Item>) newValue;
                                    importItems.forEach(item -> updateCache(item.getProperty()));
                                }
                            }

                            private void caseRestore(Object newValue) {
                                if (newValue instanceof IRepositoryViewObject) {
                                    IRepositoryViewObject object = (IRepositoryViewObject) newValue;
                                    updateCache(object.getProperty());
                                }
                            }

                            private void updateCache(Property property) {
                                if (needUpdate(property.getItem())) {
                                    Iterator<Property> iterator = getAllCodesJar().iterator();
                                    while (iterator.hasNext()) {
                                        Property oldProperty = iterator.next();
                                        if (oldProperty.getId().equals(property.getId())
                                                && oldProperty.getLabel().equals(property.getLabel())
                                                && oldProperty.getVersion().equals(property.getVersion())) {
                                            iterator.remove();
                                        }
                                    }
                                    getAllCodesJar().add(property);
                                }
                            }

                            private boolean needUpdate(Item item) {
                                ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);
                                if (type != null) {
                                    return ERepositoryObjectType.getAllTypesOfCodesJar().contains(type);
                                }
                                return false;
                            }
                        };
                    }
                    getProxyRepositoryFactory().addPropertyChangeListener(listener);
                    isListenerAdded = true;
                }
            }
        }
    }

}
