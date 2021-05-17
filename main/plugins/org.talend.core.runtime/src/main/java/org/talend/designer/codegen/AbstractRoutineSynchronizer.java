// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.core.runtime.services.IDesignerMavenService;
import org.talend.core.runtime.util.ItemDateParser;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNodeUtilities;

/***/
public abstract class AbstractRoutineSynchronizer implements ITalendSynchronizer {

    private static Map<String, Date> id2date = new HashMap<String, Date>();

    protected Collection<RoutineItem> getRoutines(boolean syncRef) throws SystemException {
        return getAll(ERepositoryObjectType.ROUTINES, syncRef);
    }

    protected Collection<RoutineItem> getBeans(boolean syncRef) throws SystemException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
            ICamelDesignerCoreService service = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    ICamelDesignerCoreService.class);
            return getAll(service.getBeansType(), syncRef);
        }
        return Collections.emptyList();
    }

    private Collection<RoutineItem> getAll(ERepositoryObjectType type, boolean syncRef) throws SystemException {
        // init code project
        getRunProcessService().getTalendCodeJavaProject(type);
        // remove routine with same name in reference project
        final Map<String, RoutineItem> beansList = new HashMap<String, RoutineItem>();
        for (IRepositoryViewObject obj : getRepositoryService().getProxyRepositoryFactory().getAll(type)) {
            beansList.put(obj.getProperty().getLabel(), (RoutineItem) obj.getProperty().getItem());
        }

        for (Project project : ProjectManager.getInstance().getReferencedProjects()) {
            getReferencedProjectRoutine(beansList, project, type, syncRef);
        }
        return beansList.values();
    }

    private static IRepositoryService getRepositoryService() {
        return (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
    }

    private Set<IRepositoryViewObject> getReferencedProjectRoutine(final Map<String, RoutineItem> beansList,
            final Project project, ERepositoryObjectType routineType, boolean syncRef) throws SystemException {
        // init ref code project
        if (syncRef) {
            getRunProcessService().getTalendCodeJavaProject(routineType, project.getTechnicalLabel());
        }
        Set<IRepositoryViewObject> routines = new HashSet<>();
        routines.addAll(getRepositoryService().getProxyRepositoryFactory().getAll(project, routineType));
        for (IRepositoryViewObject obj : routines) {
            final String key = obj.getProperty().getLabel();
            // it does not have a routine with same name
            if (!beansList.containsKey(key)) {
                beansList.put(key, (RoutineItem) obj.getProperty().getItem());
            }
        }
        for (ProjectReference projectReference : project.getProjectReferenceList()) {
            routines.addAll(getReferencedProjectRoutine(beansList, new Project(projectReference.getReferencedProject()),
                    routineType, syncRef));
        }
        if (syncRef) {
            routines.stream().forEach(obj -> {
                try {
                    syncRoutine((RoutineItem) obj.getProperty().getItem(), project.getTechnicalLabel(), true, true);
                } catch (SystemException e) {
                    ExceptionHandler.process(e);
                }
            });
            // sync system routine
            syncSystemRoutine(project);
        }
        return routines;
    }

    protected void syncSystemRoutine(Project project) throws SystemException {
        //
    }

    private static IRunProcessService getRunProcessService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        return (IRunProcessService) service;
    }

    protected IFile getRoutineFile(RoutineItem routineItem) throws SystemException {
        return getRoutineFile(routineItem, ProjectManager.getInstance().getCurrentProject().getTechnicalLabel());
    }

    protected IFile getRoutineFile(RoutineItem routineItem, String projectTechName) throws SystemException {
        if (RoutinesUtil.isInnerCodes(routineItem.getProperty())) {
            return getInnerCodeFile(routineItem);
        }
        ITalendProcessJavaProject talendProcessJavaProject = getRunProcessService()
                .getTalendCodeJavaProject(ERepositoryObjectType.getItemType(routineItem), projectTechName);
        if (talendProcessJavaProject == null) {
            return null;
        }
        IFolder routineFolder = talendProcessJavaProject.getSrcSubFolder(null, routineItem.getPackageType());
        return routineFolder.getFile(routineItem.getProperty().getLabel() + JavaUtils.JAVA_EXTENSION);
    }

    protected IFile getInnerCodeFile(RoutineItem routineItem) throws SystemException {
        CodesJarInfo info = CodesJarResourceCache.getCodesJarByInnerCode(routineItem);
        ITalendProcessJavaProject talendProcessJavaProject = getRunProcessService().getTalendCodesJarJavaProject(info);
        if (talendProcessJavaProject == null) {
            return null;
        }
        IFolder routineFolder = talendProcessJavaProject.getSrcSubFolder(null, getInnerCodePackage(routineItem));
        return routineFolder.getFile(routineItem.getProperty().getLabel() + JavaUtils.JAVA_EXTENSION);
    }

    private String getInnerCodePackage(RoutineItem innerCodeItem) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerMavenService.class)) {
            IDesignerMavenService service = GlobalServiceRegister.getDefault().getService(IDesignerMavenService.class);
            return service.getCodesJarPackageByInnerCode(innerCodeItem);
        }
        return null;
    }

    private IFile getProcessFile(ProcessItem item) throws SystemException {
        final ITalendProcessJavaProject talendProcessJavaProject = getRunProcessService().getTalendJobJavaProject(item.getProperty());
        if (talendProcessJavaProject == null) {
            return null;
        }
        final String jobName = item.getProperty().getLabel();
        final String folderName = JavaResourcesHelper.getJobFolderName(jobName, item.getProperty().getVersion());
        return talendProcessJavaProject.getSrcFolder().getFile(
                JavaResourcesHelper.getProjectFolderName(item) + '/' + folderName + '/' + jobName + JavaUtils.JAVA_EXTENSION);
    }

    @Override
    public IFile getFile(Item item) throws SystemException {
        if (item instanceof RoutineItem) {
            return getRoutineFile((RoutineItem) item);
        } else if (item instanceof ProcessItem) {
            return getProcessFile((ProcessItem) item);
        }
        return null;
    }

    @Override
    public IFile getRoutinesFile(Item item) throws SystemException {
        // from talend project
        if (item instanceof RoutineItem) {
            final RoutineItem routineItem = (RoutineItem) item;
            final IProject project = ResourcesPlugin.getWorkspace().getRoot()
                    .getProject(ProjectManager.getInstance().getProject(routineItem).getTechnicalLabel());
            ERepositoryObjectType type;
            ERepositoryObjectType innerCodeType = RoutinesUtil.getInnerCodeType(item.getProperty());
            if (innerCodeType != null) {
                type = innerCodeType;
            } else {
                type = ERepositoryObjectType.getItemType(routineItem);
            }
            IFolder folder = project.getFolder(ERepositoryObjectType.getFolderName(type));
            IPath ipath = RepositoryNodeUtilities.getPath(routineItem.getProperty().getId());
            if (ipath == null) {
                return null;
            }
            final String folderPath = ipath.toString();
            if (folderPath != null && !folderPath.trim().isEmpty()) {
                folder = folder.getFolder(folderPath);
            }
            final String fileName = routineItem.getProperty().getLabel() + '_' + routineItem.getProperty().getVersion()
                    + JavaUtils.ITEM_EXTENSION;
            return folder.getFile(fileName);
        }
        return null;
    }

    @Override
    public void syncRoutine(RoutineItem routineItem, boolean copyToTemp) throws SystemException {
        syncRoutine(routineItem, ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), copyToTemp, false);
    }

    @Override
    public void syncRoutine(RoutineItem routineItem, boolean copyToTemp, boolean forceUpdate) throws SystemException {
        syncRoutine(routineItem, ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), copyToTemp, forceUpdate);
    }

    @Override
    public void syncRoutine(RoutineItem routineItem, String projectTechName, boolean copyToTemp, boolean forceUpdate)
            throws SystemException {
        boolean needSync = false;
        if (routineItem != null) {
            if (forceUpdate || !isRoutineUptodate(routineItem)) {
                needSync = true;
            } else {
                IFile file = getRoutineFile(routineItem, projectTechName);
                if (file != null && !file.exists()) {
                    needSync = true;
                }
            }
        }
        if (needSync) {
            doSyncRoutine(routineItem, projectTechName, copyToTemp);
            if (ProjectManager.getInstance().getCurrentProject().getTechnicalLabel().equals(projectTechName)) {
                setRoutineAsUptodate(routineItem);
            }
        }
    }

    public void syncRoutine(RoutineItem routineItem) throws SystemException {
        if (routineItem != null) {
            doSyncRoutine(routineItem, ProjectManager.getInstance().getCurrentProject().getTechnicalLabel(), true);
            setRoutineAsUptodate(routineItem);
        }
    }

    protected void syncInnerCodeItems(boolean forceUpdate) throws SystemException {
        IProxyRepositoryFactory factory = getRepositoryService().getProxyRepositoryFactory();
        for (CodesJarInfo info : CodesJarResourceCache.getAllCodesJars()) {
            List<IRepositoryViewObject> innerCodesObjects = factory.getAllInnerCodes(info);
            for (IRepositoryViewObject codesObj : innerCodesObjects) {
                RoutineItem codeItem = (RoutineItem) codesObj.getProperty().getItem();
                syncRoutine(codeItem, info.getProjectTechName(), true, forceUpdate);
            }
        }
    }

    private void doSyncRoutine(RoutineItem routineItem, String projectTechName, boolean copyToTemp) throws SystemException {
        try {
            IFile file = getRoutineFile(routineItem, projectTechName);
            if (file == null) {
                return;
            }
            File itemFile = null;
            if (routineItem.eResource() == null) {
                if (!RoutinesUtil.isInnerCodes(routineItem.getProperty())) {
                    return;
                }
                CodesJarInfo info = CodesJarResourceCache.getCodesJarByInnerCode(routineItem);
                IFolder innerCodeFolder = ResourceUtils.getFolder(ResourceUtils.getProject(info.getProjectTechName()),
                        ERepositoryObjectType.getFolderName(info.getType()) + "/" + info.getLabel(), true);
                IFile innerCodeFile = innerCodeFolder
                        .getFile(routineItem.getProperty().getLabel() + "_" + routineItem.getProperty().getVersion() + ".item");
                if (innerCodeFile.exists()) {
                    itemFile = innerCodeFile.getLocation().toFile();
                }
                if (itemFile == null) {
                    return;
                }
            }
            Property property = routineItem.getProperty();
            Date modifiedDate = ItemDateParser.parseAdditionalDate(property, ItemProductKeys.DATE.getModifiedKey());
            if (modifiedDate != null) {
                long modificationItemDate = modifiedDate.getTime();
                long modificationFileDate = file.getModificationStamp();
                if (modificationItemDate <= modificationFileDate) {
                    return;
                }
            }

            if (copyToTemp) {
                if (itemFile == null) {
                    String uri = routineItem.eResource().getURI().trimFileExtension()
                            .appendFileExtension(FileExtensions.ITEM_EXTENSION).toPlatformString(false);
                    itemFile = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(uri).toFile();
                }
                byte[] buf = routineItem.getContent().getInnerContent();
                String charset = null;
                if (itemFile.exists()) {
                    charset = CharsetToolkit.getCharset(itemFile);
                } else {
                    charset = System.getProperty("file.encoding");
                }
                String routineContent = new String(buf, charset);
                // see 14713
                if (routineContent.contains("%GENERATED_LICENSE%")) { //$NON-NLS-1$
                    IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                            IBrandingService.class);
                    String routineHeader = service.getRoutineLicenseHeader(VersionUtils.getVersion());
                    routineContent = routineContent.replace("%GENERATED_LICENSE%", routineHeader); //$NON-NLS-1$
                }// end
                String label = property.getLabel();
                if (!label.equals(ITalendSynchronizer.TEMPLATE) && routineContent != null) {
                    routineContent = routineContent.replaceAll(ITalendSynchronizer.TEMPLATE, label);
                    // routineContent = renameRoutinePackage(routineItem,
                    // routineContent);
                    if (!file.exists()) {
                        file.create(new ByteArrayInputStream(routineContent.getBytes(System.getProperty("file.encoding"))), true,
                                null);
                    } else {
                        file.setContents(new ByteArrayInputStream(routineContent.getBytes(System.getProperty("file.encoding"))),
                                true, false, null);
                    }
                }
            }
        } catch (CoreException | IOException e) {
            throw new SystemException(e);
        }
    }

    @Override
    public void deleteRoutinefile(IRepositoryViewObject objToDelete) {
        Item item = objToDelete.getProperty().getItem();
        if (RoutinesUtil.isInnerCodes(item.getProperty())) {
            deleteInnerCodeFile(item);
            return;
        }
        try {
            ITalendProcessJavaProject talendProcessJavaProject = getRunProcessService()
                    .getTalendCodeJavaProject(ERepositoryObjectType.getItemType(item));
            if (talendProcessJavaProject == null) {
                return;
            }
            IFolder srcFolder = talendProcessJavaProject.getSrcFolder();
            IFile file = srcFolder.getFile(((RoutineItem) item).getPackageType() + '/' + objToDelete.getLabel()
                    + JavaUtils.JAVA_EXTENSION);
            file.delete(true, null);
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    private void deleteInnerCodeFile(Item item) {
        try {
            IFile file = getInnerCodeFile((RoutineItem) item);
            file.delete(true, null);
        } catch (CoreException | SystemException e) {
            ExceptionHandler.process(e);
        }
    }

    protected boolean isRoutineUptodate(RoutineItem routineItem) {
        Date refDate = getRefDate(routineItem);
        if (refDate == null) {
            return false;
        }
        Date date = id2date.get(routineItem.getProperty().getId());
        return refDate.equals(date);
    }

    protected void setRoutineAsUptodate(RoutineItem routineItem) {
        Date refDate = getRefDate(routineItem);
        if (refDate == null) {
            return;
        }
        id2date.put(routineItem.getProperty().getId(), refDate);
    }

    private Date getRefDate(RoutineItem routineItem) {
        Property property = routineItem.getProperty();
        if (routineItem.isBuiltIn()) {
            // FIXME mhelleboid for now, routines are deleted and recreated on
            // project logon
            // change this code, if one day routines are updated
            return ItemDateParser.parseAdditionalDate(property, ItemProductKeys.DATE.getCreatedKey());
        } else {
            return ItemDateParser.parseAdditionalDate(property, ItemProductKeys.DATE.getModifiedKey());
        }

    }

    @Override
    public void forceSyncRoutine(RoutineItem routineItem) {
        id2date.remove(routineItem.getProperty().getId());
        try {
            IFile file = getFile(routineItem);
            if (file == null) {
                return;
            }
            file.delete(true, new NullProgressMonitor());
        } catch (Exception e) {
            // ignore me
        }
    }

    // qli modified to fix the bug 5400 and 6185.
    @Override
    public abstract void renameRoutineClass(RoutineItem routineItem);

    @Override
    public void syncAllRoutinesForLogOn() throws SystemException {
    }

    @Override
    public void syncAllBeansForLogOn() throws SystemException {
        for (RoutineItem beanItem : getBeans(true)) {
            syncRoutine(beanItem, true, true);
        }
    }


    @Override
    public void syncAllBeans() throws SystemException {
        for (RoutineItem beanItem : getBeans(false)) {
            syncRoutine(beanItem, true);
        }
    }

}
