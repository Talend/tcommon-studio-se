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
package org.talend.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.properties.Item;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.model.repository.ResourceModelUtils;
import org.talend.core.model.routines.RoutineLibraryMananger;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.model.utils.PerlResourcesHelper;
import org.talend.core.model.utils.ResourceModelHelper;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.PreferenceManipulator;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.services.IJobCheckService;
import org.talend.core.utils.KeywordsValidator;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.RepositoryConstants;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class CoreService implements ICoreService {

    private static Logger log = Logger.getLogger(CoreService.class);

    @Override
    public List<ColumnNameChanged> getColumnNameChanged(IMetadataTable oldTable, IMetadataTable newTable) {
        return MetadataToolHelper.getColumnNameChanged(oldTable, newTable);
    }

    @Override
    public List<ColumnNameChanged> getNewMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable) {
        return MetadataToolHelper.getNewMetadataColumns(oldTable, newTable);
    }

    @Override
    public List<ColumnNameChanged> getRemoveMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable) {
        return MetadataToolHelper.getRemoveMetadataColumns(oldTable, newTable);
    }

    @Override
    public void initializeForTalendStartupJob() {
        CorePlugin.getDefault().getRepositoryService().initializeForTalendStartupJob();
    }

    @Override
    public String getLanTypeString() {
        return getPreferenceStore().getString(CorePlugin.PROJECT_LANGUAGE_TYPE);
    }

    @Override
    public Image getImageWithDocExt(String extension) {
        return OverlayImageProvider.getImageWithDocExt(extension);
    }

    @Override
    public ImageDescriptor getImageWithSpecial(Image source) {
        return OverlayImageProvider.getImageWithSpecial(source);
    }

    @Override
    public boolean isContainContextParam(String code) {
        return ContextParameterUtils.isContainContextParam(code);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#setFlagForQueryUtils(boolean)
     */
    @Override
    public void setFlagForQueryUtils(boolean flag) {
        QueryUtil.isContextQuery = flag;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getFlagFromQueryUtils()
     */
    @Override
    public boolean getContextFlagFromQueryUtils() {
        return QueryUtil.isContextQuery;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getRoutineAndJars()
     */
    @Override
    public Map<String, List<LibraryInfo>> getRoutineAndJars() {
        return RoutineLibraryMananger.getInstance().getRoutineAndJars();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getTemplateString()
     */
    @Override
    public String getTemplateString() {
        return ITalendSynchronizer.TEMPLATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getParameterUNIQUENAME()
     */
    @Override
    public String getParameterUNIQUENAME(NodeType node) {
        return ElementParameterParser.getUNIQUENAME(node);
    }

    @Override
    public boolean isAlreadyBuilt(Project project) {
        return !project.getEmfProject().getItemsRelations().isEmpty();
    }

    @Override
    public void removeItemRelations(Item item) {
        RelationshipItemBuilder.getInstance().removeItemRelations(item);
    }

    @Override
    public String getJavaJobFolderName(String jobName, String version) {
        return JavaResourcesHelper.getJobFolderName(jobName, version);
    }

    @Override
    public String getJavaProjectFolderName(Item item) {
        return JavaResourcesHelper.getProjectFolderName(item);
    }

    @Override
    public IResource getSpecificResourceInJavaProject(IPath path) throws CoreException {
        return JavaResourcesHelper.getSpecificResourceInJavaProject(path);
    }

    @Override
    public String getContextFileNameForPerl(String projectName, String jobName, String version, String context) {
        return PerlResourcesHelper.getContextFileName(projectName, jobName, version, context);
    }

    @Override
    public String getRootProjectNameForPerl(Item item) {
        return PerlResourcesHelper.getRootProjectName(item);
    }

    @Override
    public IResource getSpecificResourceInPerlProject(IPath path) throws CoreException {
        return PerlResourcesHelper.getSpecificResourceInPerlProject(path);
    }

    @Override
    public void syncLibraries(IProgressMonitor... monitorWrap) {
        CorePlugin.getDefault().getLibrariesService().syncLibraries(monitorWrap);
        // if (!CommonsPlugin.isHeadless()) {
        // CorePlugin.getDefault().getRunProcessService().updateLibraries(new HashSet<String>(), null);
        // }

    }

    @Override
    public void removeJobLaunch(IRepositoryViewObject objToDelete) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    IDesignerCoreService.class);
            designerCoreService.removeJobLaunch(objToDelete);
        }
    }

    @Override
    public void deleteRoutinefile(IRepositoryViewObject objToDelete) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
            ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            codeGenService.createRoutineSynchronizer().deleteRoutinefile(objToDelete);
        }
    }

    @Override
    public void deleteBeanfile(IRepositoryViewObject objToDelete) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
            ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            codeGenService.createRoutineSynchronizer().deleteRoutinefile(objToDelete);
        }
    }

    @Override
    public boolean checkJob(String name) throws BusinessException {
        IJobCheckService jobCheckService = (IJobCheckService) GlobalServiceRegister.getDefault().getService(
                IJobCheckService.class);
        if (jobCheckService != null) {
            return jobCheckService.checkJob(name);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#syncAllRoutines()
     */
    @Override
    public void syncAllRoutines() throws SystemException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
            ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            codeGenService.createRoutineSynchronizer().syncAllRoutinesForLogOn();
            codeGenService.createRoutineSynchronizer().syncAllPigudfForLogOn();
        }

    }

    @Override
    public void syncAllBeans() throws SystemException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
            ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            ITalendSynchronizer talendSynchronizer = codeGenService.createRoutineSynchronizer();
            if (talendSynchronizer != null) {
                talendSynchronizer.syncAllBeansForLogOn();
            }
        }

    }

    @Override
    public Job initializeTemplates() {
        return CorePlugin.getDefault().getCodeGeneratorService().initializeTemplates();
    }

    @Override
    public void createStatsLogAndImplicitParamter(Project project) {
        IDesignerCoreService designerCoreService = CorePlugin.getDefault().getDesignerCoreService();
        if (designerCoreService != null) {
            designerCoreService.createStatsLogAndImplicitParamter(project);
        }
    }

    @Override
    public void deleteAllJobs(boolean fromPluginModel) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            runProcessService.deleteAllJobs(false);
        }
    }

    @Override
    public void addWorkspaceTaskDone(String task) {
        PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        prefManipulator.addWorkspaceTaskDone(task);
    }

    @Override
    public String filterSpecialChar(String input) {
        return TalendTextUtils.filterSpecialChar(input);
    }

    @Override
    public String getLastUser() {
        PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        return prefManipulator.getLastUser();
    }

    @Override
    public boolean isKeyword(String word) {
        return KeywordsValidator.isKeyword(word);
    }

    @Override
    public List<String> readWorkspaceTasksDone() {
        PreferenceManipulator prefManipulator = new PreferenceManipulator(CorePlugin.getDefault().getPreferenceStore());
        return prefManipulator.readWorkspaceTasksDone();
    }

    @Override
    public String validateValueForDBType(String columnName) {
        return MetadataToolHelper.validateValueForDBType(columnName);
    }

    @Override
    public void synchronizeMapptingXML() {
        try {
            URL url = MetadataTalendType.getProjectForderURLOfMappingsFile();
            if (url != null && GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                // set the project mappings url
                System.setProperty("talend.mappings.url", url.toString()); //$NON-NLS-1$
                IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                        IRunProcessService.class);
                if (runProcessService != null) {
                    ITalendProcessJavaProject talendProcessJavaProject = runProcessService.getTalendProcessJavaProject();
                    if (talendProcessJavaProject == null) {
                        return;
                    }
                    IFolder xmlMappingFolder = talendProcessJavaProject.getResourceSubFolder(null, JavaUtils.JAVA_XML_MAPPING);

                    File mappingSource = new File(url.getPath());
                    FilenameFilter filter = new FilenameFilter() {

                        @Override
                        public boolean accept(File dir, String name) {
                            if (XmlUtil.isXMLFile(name)) {
                                return true;
                            }
                            return false;
                        }
                    };

                    for (File file : mappingSource.listFiles(filter)) {
                        String targetName = getTargetName(file);
                        IFile targetFile = xmlMappingFolder.getFile(targetName);
                        copyFile(file, targetFile);
                    }

                }

            }
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } catch (SystemException e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    public void syncMappingsFileFromSystemToProject() {
        RepositoryWorkUnit workUnit = new RepositoryWorkUnit("Sync mapping files from system to project") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                try {
                    File sysMappingfolder = new File(MetadataTalendType.getSystemForderURLOfMappingsFile().getPath());
                    IFolder projectMappingFolder = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject())
                            .getFolder(MetadataTalendType.PROJECT_MAPPING_FOLDER);
                    if (!projectMappingFolder.exists()) {
                        projectMappingFolder.create(true, true, null);
                    }
                    for (File in : sysMappingfolder.listFiles()) {
                        IFile out = projectMappingFolder.getFile(in.getName());
                        copyFile(in, out);
                    }
                } catch (SystemException | CoreException | IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }

    public String getTargetName(File file) {
        String targetName = file.getName();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder analyser;
        try {
            analyser = documentBuilderFactory.newDocumentBuilder();
            Document document = analyser.parse(file);
            NodeList dbmsNodes = document.getElementsByTagName("dbms"); //$NON-NLS-1$
            String dbmsIdValue = "";
            for (int iDbms = 0; iDbms < dbmsNodes.getLength(); iDbms++) {
                Node dbmsNode = dbmsNodes.item(iDbms);
                NamedNodeMap dbmsAttributes = dbmsNode.getAttributes();
                dbmsIdValue = dbmsAttributes.getNamedItem("id").getNodeValue(); //$NON-NLS-1$

            }
            if (dbmsIdValue != null && !"".equals(dbmsIdValue)) {
                final String[] fileNameSplit = targetName.split("_");
                String idA = "_id";
                String idB = "id_";
                final int indexA = dbmsIdValue.indexOf(idA);
                final int indexB = dbmsIdValue.indexOf(idB);
                String secondeName = "";
                if (indexA > 0) {
                    secondeName = dbmsIdValue.substring(0, dbmsIdValue.length() - idA.length());
                } else if (indexB == 0) {
                    secondeName = dbmsIdValue.substring(idB.length(), dbmsIdValue.length());
                } else if (indexA == -1 && indexB == -1) {
                    secondeName = dbmsIdValue;
                }
                if (secondeName != null && !"".equals(secondeName)) {
                    targetName = fileNameSplit[0] + "_" + secondeName.toLowerCase() + XmlUtil.FILE_XML_SUFFIX;
                }

            }
        } catch (ParserConfigurationException e) {
            ExceptionHandler.process(e);
        } catch (SAXException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
        return targetName;
    }

    public void copyFile(File in, IFile out) throws CoreException, IOException {
        FileInputStream fis = new FileInputStream(in);
        if (out.exists()) {
            out.setContents(fis, true, false, null);
        } else {
            out.create(fis, true, null);
        }
        fis.close();
    }

    @Override
    public IPreferenceStore getPreferenceStore() {
        return CorePlugin.getDefault().getPreferenceStore();
    }

    @Override
    public boolean isOpenedItemInEditor(IRepositoryViewObject object) {
        return RepositoryManager.isOpenedItemInEditor(object);
    }

    @Override
    public IMetadataTable convert(MetadataTable originalTable) {
        return ConvertionHelper.convert(originalTable);
    }

    @Override
    public void syncLog4jSettings() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        String log = "Sync log4j settings"; //$NON-NLS-1$ 
        final RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(project, log) {

            @Override
            public void run() throws PersistenceException, LoginException {
                IRunProcessService service = null;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                    service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
                }
                if (service != null) {
                    try {
                        IFolder prefSettingFolder = ResourceUtils.getFolder(
                                ResourceModelHelper.getProject(ProjectManager.getInstance().getCurrentProject()),
                                RepositoryConstants.SETTING_DIRECTORY, false);
                        if (!prefSettingFolder.exists()) {
                            prefSettingFolder.create(true, true, null);
                        }
                        service.updateLogFiles(ResourceModelUtils.getProject(ProjectManager.getInstance().getCurrentProject()),
                                false);
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
    }
}
