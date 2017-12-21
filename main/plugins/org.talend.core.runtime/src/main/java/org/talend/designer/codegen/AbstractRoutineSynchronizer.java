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
package org.talend.designer.codegen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PigudfItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.core.runtime.util.ItemDateParser;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNodeUtilities;

/***/
public abstract class AbstractRoutineSynchronizer implements ITalendSynchronizer {

    private static Map<String, Date> id2date = new HashMap<String, Date>();

    protected Collection<RoutineItem> getRoutines() throws SystemException {
        return getAll(ERepositoryObjectType.ROUTINES);
    }

    protected Collection<RoutineItem> getAllPigudf() throws SystemException {
        return getAll(ERepositoryObjectType.PIG_UDF);
    }

    protected Collection<RoutineItem> getBeans() throws SystemException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
            ICamelDesignerCoreService service = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    ICamelDesignerCoreService.class);
            return getAll(service.getBeansType());
        }
        return Collections.emptyList();
    }

    private static Collection<RoutineItem> getAll(ERepositoryObjectType type) throws SystemException {
        // remove routine with same name in reference project
        final Map<String, RoutineItem> beansList = new HashMap<String, RoutineItem>();
        for (IRepositoryViewObject obj : getRepositoryService().getProxyRepositoryFactory().getAll(type)) {
            beansList.put(obj.getProperty().getLabel(), (RoutineItem) obj.getProperty().getItem());
        }

        for (Project project : ProjectManager.getInstance().getReferencedProjects()) {
            getReferencedProjectRoutine(beansList, project, type);
        }
        return beansList.values();
    }

    private static IRepositoryService getRepositoryService() {
        return (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
    }

    private static void getReferencedProjectRoutine(final Map<String, RoutineItem> beansList, final Project project,
            ERepositoryObjectType routineType) throws SystemException {
        for (IRepositoryViewObject obj : getRepositoryService().getProxyRepositoryFactory().getAll(project, routineType)) {
            final String key = obj.getProperty().getLabel();
            // it does not have a routine with same name
            if (!beansList.containsKey(key)) {
                beansList.put(key, (RoutineItem) obj.getProperty().getItem());
            }
        }
        for (ProjectReference projectReference : project.getProjectReferenceList()) {
            getReferencedProjectRoutine(beansList, new Project(projectReference.getReferencedProject()), routineType);
        }
    }

    private static IRunProcessService getRunProcessService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        return (IRunProcessService) service;
    }

    protected IFile getRoutineFile(RoutineItem routineItem) throws SystemException {
        ITalendProcessJavaProject talendProcessJavaProject = getRunProcessService().getTalendProcessJavaProject();
        if (talendProcessJavaProject == null) {
            return null;
        }
        IFolder routineFolder = talendProcessJavaProject.getSrcSubFolder(null, routineItem.getPackageType());
        IFile file = routineFolder.getFile(routineItem.getProperty().getLabel() + JavaUtils.JAVA_EXTENSION);
        return file;
    }

    private IFile getProcessFile(ProcessItem item) throws SystemException {
        final ITalendProcessJavaProject talendProcessJavaProject = getRunProcessService().getTalendProcessJavaProject();
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
        if (item instanceof RoutineItem) {
            final RoutineItem routineItem = (RoutineItem) item;
            final IProject project = ResourcesPlugin.getWorkspace().getRoot()
                    .getProject(ProjectManager.getInstance().getProject(routineItem).getTechnicalLabel());
            IFolder folder = project
                    .getFolder(ERepositoryObjectType.getFolderName(ERepositoryObjectType.getItemType(routineItem)));
            IPath ipath = RepositoryNodeUtilities.getPath(routineItem.getProperty().getId());
            if (ipath == null)
                return null;
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
        syncRoutine(routineItem, copyToTemp, false);
    }

    @Override
    public void syncRoutine(RoutineItem routineItem, boolean copyToTemp, boolean forceUpdate) throws SystemException {
        boolean needSync = false;
        if (routineItem != null) {
            if (forceUpdate || !isRoutineUptodate(routineItem)) {
                needSync = true;
            } else {
                IFile file = getFile(routineItem);
                if (file != null && !file.exists()) {
                    needSync = true;
                }
            }
        }
        if (needSync) {
            doSyncRoutine(routineItem, copyToTemp);
            setRoutineAsUptodate(routineItem);
        }
    }

    public void syncRoutine(RoutineItem routineItem) throws SystemException {
        if (routineItem != null) {
            doSyncRoutine(routineItem, true);
            setRoutineAsUptodate(routineItem);
        }
    }

    private void doSyncRoutine(RoutineItem routineItem, boolean copyToTemp) throws SystemException {
        try {
            IFile file = getRoutineFile(routineItem);
            if (file == null) {
                return;
            }
            if (routineItem.eResource() == null) {
                return;
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
                String uri = routineItem.eResource().getURI().trimFileExtension()
                        .appendFileExtension(FileExtensions.ITEM_EXTENSION).toPlatformString(false);
                File itemFile = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(uri).toFile();
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
        try {
            ITalendProcessJavaProject talendProcessJavaProject = getRunProcessService().getTalendProcessJavaProject();
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
    public void renamePigudfClass(PigudfItem routineItem, String oldLabel) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ITalendSynchronizer#syncAllPigudf()
     */
    @Override
    public void syncAllPigudf() throws SystemException {
        // TODO Auto-generated method stub

    }

    @Override
    public void syncAllRoutinesForLogOn() throws SystemException {
    }

    @Override
    public void syncAllBeansForLogOn() throws SystemException {
        for (RoutineItem beanItem : getBeans()) {
            syncRoutine(beanItem, true, true);
        }
    }

    @Override
    public void syncAllPigudfForLogOn() throws SystemException {
    }

    @Override
    public void syncAllBeans() throws SystemException {
        for (RoutineItem beanItem : getBeans()) {
            syncRoutine(beanItem, true);
        }
    }

}
