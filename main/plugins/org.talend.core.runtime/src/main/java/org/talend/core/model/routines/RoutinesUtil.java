// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.routines;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ItemInforType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * ggu class global comment. Detailled comment
 */
public final class RoutinesUtil {

    private RoutinesUtil() {
    }

    public static boolean allowDeletedRoutine() {
        return false;
    }

    public static List<IRepositoryViewObject> getCurrentSystemRoutines() {
        List<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

        try {
            List<IRepositoryViewObject> all = factory.getAll(ProjectManager.getInstance().getCurrentProject(),
                    ERepositoryObjectType.ROUTINES);
            for (IRepositoryViewObject obj : all) {
                Item item = obj.getProperty().getItem();
                if (item instanceof RoutineItem && ((RoutineItem) item).isBuiltIn()) {
                    repositoryObjects.add(obj);
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        return repositoryObjects;

    }

    public static IRepositoryViewObject getUserRoutines(String id) {
        IRepositoryViewObject repositoryObjects = null;
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            repositoryObjects = factory.getLastVersion(id);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return repositoryObjects;
    }

    /**
     * DOC ycbai Comment method "getRoutineFromName".
     * 
     * @param name
     * @return
     */
    public static IRepositoryViewObject getRoutineFromName(String name) {
        if (name == null) {
            return null;
        }

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            List<IRepositoryViewObject> all = factory.getAll(ProjectManager.getInstance().getCurrentProject(),
                    ERepositoryObjectType.ROUTINES);
            for (IRepositoryViewObject obj : all) {
                if (obj != null && obj.getProperty() != null) {
                    Item item = obj.getProperty().getItem();
                    String label = obj.getProperty().getLabel();
                    if (item != null && item instanceof RoutineItem && name.equals(label)) {
                        return obj;
                    }
                }
            }
            for (Project project : ProjectManager.getInstance().getAllReferencedProjects()) {
                all = factory.getAll(project, ERepositoryObjectType.ROUTINES);
                for (IRepositoryViewObject obj : all) {
                    if (obj != null && obj.getProperty() != null) {
                        Item item = obj.getProperty().getItem();
                        String label = obj.getProperty().getLabel();
                        if (item != null && item instanceof RoutineItem && name.equals(label)) {
                            return obj;
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        return null;
    }

    /**
     * 
     * ggu Comment method "collectRelatedRoutines".
     * 
     * @param includeRoutineIdOrNames if null, will add all.
     * @param system
     * @return
     */
    public static List<IRepositoryViewObject> collectRelatedRoutines(Set<String> includeRoutineIdOrNames, boolean system,
            ERepositoryObjectType type) {
        List<IRepositoryViewObject> allRoutines = new ArrayList<IRepositoryViewObject>();
        if (system) {
            List<IRepositoryViewObject> systemRoutines = RoutinesUtil.getCurrentSystemRoutines();
            for (IRepositoryViewObject object : systemRoutines) {
                if (includeRoutineIdOrNames == null || includeRoutineIdOrNames.contains(object.getLabel())) {
                    allRoutines.add(object);
                }
            }
        } else {
            collectUserRoutines(allRoutines, ProjectManager.getInstance().getCurrentProject(), includeRoutineIdOrNames, type);
        }
        return allRoutines;
    }

    private static void collectUserRoutines(List<IRepositoryViewObject> allRoutines,
            org.talend.core.model.general.Project project, Set<String> includeRoutineIdOrNames, ERepositoryObjectType type) {
        try {
            List<IRepositoryViewObject> all = CoreRuntimePlugin.getInstance().getRepositoryService().getProxyRepositoryFactory()
                    .getAll(project, type, allowDeletedRoutine());
            for (IRepositoryViewObject obj : all) {
                if (includeRoutineIdOrNames == null || includeRoutineIdOrNames.contains(obj.getId())) {
                    allRoutines.add(obj);
                }
            }
            for (org.talend.core.model.general.Project p : ProjectManager.getInstance().getReferencedProjects(project)) {
                collectUserRoutines(allRoutines, p, includeRoutineIdOrNames, type);
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

    public static List<RoutinesParameterType> createJobRoutineDependencies(boolean system) throws PersistenceException {
        List<RoutinesParameterType> itemInfors = new ArrayList<RoutinesParameterType>();
        if (system) {
            List<IRepositoryViewObject> systemRoutines = RoutinesUtil.getCurrentSystemRoutines();
            for (IRepositoryViewObject object : systemRoutines) {
                RoutinesParameterType itemInfor = createItemInforType((RoutineItem) object.getProperty().getItem());
                itemInfors.add(itemInfor);
            }
        } else {
            Project p = ProjectManager.getInstance().getCurrentProject();
            createJobRoutineDependencies(itemInfors, p);
        }
        return itemInfors;
    }

    private static void createJobRoutineDependencies(List<RoutinesParameterType> itemInfors, Project project)
            throws PersistenceException {

        List<IRepositoryViewObject> all = CoreRuntimePlugin.getInstance().getRepositoryService().getProxyRepositoryFactory()
                .getAll(project, ERepositoryObjectType.ROUTINES, allowDeletedRoutine());
        for (IRepositoryViewObject object : all) {
            Property property = object.getProperty();
            RoutineItem item = (RoutineItem) property.getItem();
            if (!item.isBuiltIn()) {
                RoutinesParameterType itemInfor = createItemInforType(item);
                itemInfors.add(itemInfor);
            }
        }
        for (Project p : ProjectManager.getInstance().getReferencedProjects(project)) {
            createJobRoutineDependencies(itemInfors, p);
        }
    }

    private static RoutinesParameterType createItemInforType(RoutineItem routineItem) {
        Property property = routineItem.getProperty();

        RoutinesParameterType itemRecordType = TalendFileFactory.eINSTANCE.createRoutinesParameterType();

        // itemRecordType.setSystem(routineItem.isBuiltIn());
        // if (itemRecordType.isSystem()) {
        itemRecordType.setName(property.getLabel());
        // } else {
        itemRecordType.setId(property.getId());
        // }
        return itemRecordType;
    }

    public static List<RoutinesParameterType> createDependenciesInPreference() throws PersistenceException {
        List<RoutinesParameterType> itemInfors = new ArrayList<RoutinesParameterType>();

        final IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
        if (designerCoreService.getPreferenceStoreBooleanValue(ITalendCorePrefConstants.ADD_SYSTEM_ROUTINES)) {
            itemInfors.addAll(createJobRoutineDependencies(true));
        }
        if (designerCoreService.getPreferenceStoreBooleanValue(ITalendCorePrefConstants.ADD_USER_ROUTINES)) {
            itemInfors.addAll(createJobRoutineDependencies(false));
        }
        return itemInfors;
    }

    // need change
    public static List<ItemInforType> createOldJobRoutineDependencies(boolean system) throws PersistenceException {
        List<ItemInforType> itemInfors = new ArrayList<ItemInforType>();
        if (system) {
            List<IRepositoryViewObject> systemRoutines = RoutinesUtil.getCurrentSystemRoutines();
            for (IRepositoryViewObject object : systemRoutines) {
                ItemInforType itemInfor = createOldItemInforType((RoutineItem) object.getProperty().getItem());
                itemInfors.add(itemInfor);
            }
        } else {
            Project p = ProjectManager.getInstance().getCurrentProject();
            createOldJobRoutineDependencies(itemInfors, p);
        }
        return itemInfors;
    }

    private static void createOldJobRoutineDependencies(List<ItemInforType> itemInfors, Project project)
            throws PersistenceException {

        List<IRepositoryViewObject> all = CoreRuntimePlugin.getInstance().getRepositoryService().getProxyRepositoryFactory()
                .getAll(project, ERepositoryObjectType.ROUTINES, allowDeletedRoutine());
        for (IRepositoryViewObject object : all) {
            Property property = object.getProperty();
            RoutineItem item = (RoutineItem) property.getItem();
            if (!item.isBuiltIn()) {
                ItemInforType itemInfor = createOldItemInforType(item);
                itemInfors.add(itemInfor);
            }
        }
        for (Project p : ProjectManager.getInstance().getReferencedProjects(project)) {
            createOldJobRoutineDependencies(itemInfors, p);
        }
    }

    private static ItemInforType createOldItemInforType(RoutineItem routineItem) {
        Property property = routineItem.getProperty();

        ItemInforType itemRecordType = TalendFileFactory.eINSTANCE.createItemInforType();

        itemRecordType.setSystem(routineItem.isBuiltIn());
        if (itemRecordType.isSystem()) {
            itemRecordType.setIdOrName(property.getLabel());
        } else {
            itemRecordType.setIdOrName(property.getId());
        }
        return itemRecordType;
    }

    public static List<ItemInforType> createOldDependenciesInPreference() throws PersistenceException {
        List<ItemInforType> itemInfors = new ArrayList<ItemInforType>();

        final IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
        if (designerCoreService.getPreferenceStoreBooleanValue(ITalendCorePrefConstants.ADD_SYSTEM_ROUTINES)) {
            itemInfors.addAll(createOldJobRoutineDependencies(true));
        }
        if (designerCoreService.getPreferenceStoreBooleanValue(ITalendCorePrefConstants.ADD_USER_ROUTINES)) {
            itemInfors.addAll(createOldJobRoutineDependencies(false));
        }
        return itemInfors;
    }

    public static List<RoutineItem> getCompileErrorCodeItems(ERepositoryObjectType type) throws SystemException, CoreException {
        List<RoutineItem> errorItems = new ArrayList<RoutineItem>(10);
        if (type == null) {
            return errorItems;
        }
        ICodeGeneratorService codegenService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
            codegenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(ICodeGeneratorService.class);
        }
        if (codegenService == null) {
            return errorItems;
        }
        final ITalendSynchronizer synchronizer = codegenService.createRoutineSynchronizer();

        final IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        List<IRepositoryViewObject> routinesObjects = factory.getAll(ProjectManager.getInstance().getCurrentProject(), type);
        collectErrorCodesItems(errorItems, synchronizer, routinesObjects);
        // for ref-projects
        for (Project project : ProjectManager.getInstance().getAllReferencedProjects()) {
            routinesObjects = factory.getAll(project, type);
            collectErrorCodesItems(errorItems, synchronizer, routinesObjects);
        }
        return errorItems;
    }

    public static void collectErrorCodesItems(List<RoutineItem> errorItems, ITalendSynchronizer synchronizer,
            List<IRepositoryViewObject> routinesObjects) throws SystemException, CoreException {
        if (routinesObjects == null || errorItems == null) {
            return;
        }
        if (synchronizer == null && GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
            ICodeGeneratorService codegenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            synchronizer = codegenService.createRoutineSynchronizer();
        }
        if (synchronizer == null) {
            return;
        }

        for (IRepositoryViewObject obj : routinesObjects) {
            Property property = obj.getProperty();
            if (property == null) {
                continue;
            }
            boolean foundError = false;
            Item item = property.getItem();
            IFile file = synchronizer.getFile(item);
            IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);
            for (IMarker marker : markers) {
                Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
                if (severity != null && IMarker.SEVERITY_ERROR == severity.intValue()) {
                    foundError = true;
                    break;
                }
            }
            if (foundError) {
                if (item instanceof RoutineItem) {
                    errorItems.add((RoutineItem) item);
                }
            }
        }
    }
}
