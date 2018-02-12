// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.localprovider.model;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.general.TalendNature;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.ProjectManager;

/**
 * created by nrousseau on Dec 12, 2013 Detailled comment
 * 
 */
public class BaseRepositoryTest {

    protected static LocalRepositoryFactory repositoryFactory;

    protected static Project originalProject;

    @BeforeClass
    public static void beforeAllTests() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        originalProject = repositoryContext.getProject();

        repositoryFactory = new LocalRepositoryFactory();
    }

    @AfterClass
    public static void afterAllTests() {
        repositoryFactory = null;

        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        repositoryContext.setProject(originalProject);
    }

    @Before
    public void beforeTest() throws PersistenceException, CoreException, LoginException {
        createTempProject();
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        repositoryContext.setProject(sampleProject);
    }

    @After
    public void afterTest() throws Exception {
        // make sure there is nothing in both jobs / routines after do anything
        try {
            final List<IRepositoryViewObject> allJobs = repositoryFactory.getAll(sampleProject, ERepositoryObjectType.PROCESS,
                    true, true);
            assertNotNull(allJobs);
            Set<String> jobs = new HashSet<String>();
            for (IRepositoryViewObject object : allJobs) {
                jobs.add(object.getLabel());
            }
            if (!jobs.isEmpty()) {
                throw new Exception("job:" + jobs + " should have been deleted before");
            }
            assertTrue(allJobs.isEmpty());

            final List<IRepositoryViewObject> allRoutines = repositoryFactory.getAll(ProjectManager.getInstance()
                    .getCurrentProject(), ERepositoryObjectType.ROUTINES, true, true);
            assertNotNull(allRoutines);
            for (IRepositoryViewObject o : allRoutines) {
                RoutineItem ri = (RoutineItem) o.getProperty().getItem();
                if (!ri.isBuiltIn()) {
                    System.out.println("routine:" + o.getLabel() + " should have been deleted before");
                }
                assertTrue(ri.isBuiltIn()); // should only have system routines, not others
            }
        } catch (Exception e) {
            throw e;
        } finally {
            removeTempProject();
            ProjectManager.getInstance().getFolders(sampleProject.getEmfProject()).clear();
            sampleProject = null;
        }
    }

    Project sampleProject;

    private void createTempProject() throws CoreException, PersistenceException, LoginException {
        Project projectInfor = new Project();
        projectInfor.setLabel("testauto");
        projectInfor.setDescription("no desc");
        projectInfor.setLanguage(ECodeLanguage.JAVA);
        User user = PropertiesFactory.eINSTANCE.createUser();
        user.setLogin("testauto@talend.com");
        projectInfor.setAuthor(user);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        String technicalLabel = Project.createTechnicalName(projectInfor.getLabel());
        IProject prj = root.getProject(technicalLabel);

        final IWorkspace workspace = ResourcesPlugin.getWorkspace();

        try {
            IProjectDescription desc = null;
            if (prj.exists()) {
                prj.delete(true, null); // always delete to avoid conflicts between 2 tests
            }
            desc = workspace.newProjectDescription(technicalLabel);
            desc.setNatureIds(new String[] { TalendNature.ID });
            desc.setComment(projectInfor.getDescription());

            prj.create(desc, null);
            prj.open(IResource.DEPTH_INFINITE, null);
            prj.setDefaultCharset("UTF-8", null);
        } catch (CoreException e) {
            throw new PersistenceException(e);
        }

        sampleProject = new Project();
        // Fill project object
        sampleProject.setLabel(projectInfor.getLabel());
        sampleProject.setDescription(projectInfor.getDescription());
        sampleProject.setLanguage(projectInfor.getLanguage());
        sampleProject.setAuthor(projectInfor.getAuthor());
        sampleProject.setLocal(true);
        sampleProject.setTechnicalLabel(technicalLabel);
        XmiResourceManager xmiResourceManager = new XmiResourceManager();
        Resource projectResource = xmiResourceManager.createProjectResource(prj);
        projectResource.getContents().add(sampleProject.getEmfProject());
        projectResource.getContents().add(sampleProject.getAuthor());
        xmiResourceManager.saveResource(projectResource);
    }

    protected void removeTempProject() throws PersistenceException, CoreException {
        repositoryFactory.logOffProject();

        // clear the folder, same as it should be in a real logoffProject.
        ProjectManager.getInstance().getFolders(sampleProject.getEmfProject()).clear();
        final IProject project = ResourceUtils.getProject(sampleProject);
        project.delete(true, null);
    }

    protected ProcessItem createTempProcessItem(LocalRepositoryFactory factory, String path) throws PersistenceException {
        ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setPath(path);
        processItem.setProperty(myProperty);
        processItem.setState(itemState);
        myProperty.setId(factory.getNextId());
        myProperty.setLabel("myJob");
        myProperty.setVersion("0.1");

        processItem.setProcess(TalendFileFactory.eINSTANCE.createProcessType());
        processItem.getProcess().getNode().add(TalendFileFactory.eINSTANCE.createNodeType());
        ((NodeType) processItem.getProcess().getNode().get(0)).setComponentName("test");
        ((NodeType) processItem.getProcess().getNode().get(0)).setComponentVersion("0.1");

        if (!"".equals(path)) {
            factory.createFolder(sampleProject, ERepositoryObjectType.PROCESS, new Path(""), path);
        }

        factory.create(sampleProject, processItem, new Path(path), false);

        return processItem;
    }

    protected RoutineItem createTempRoutineItem(LocalRepositoryFactory factory, String path) throws PersistenceException {
        RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
        Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setPath(path);
        routineItem.setState(itemState);
        routineItem.setProperty(myProperty);
        myProperty.setId(factory.getNextId());
        myProperty.setLabel("myRoutine");
        myProperty.setVersion("0.1");
        routineItem.setContent(PropertiesFactory.eINSTANCE.createByteArray());
        routineItem.getContent().setInnerContent("myRoutineContent".getBytes());

        if (!"".equals(path)) {
            factory.createFolder(sampleProject, ERepositoryObjectType.ROUTINES, new Path(""), path);
        }
        factory.create(sampleProject, routineItem, new Path(path), false);
        return routineItem;
    }

    protected void testGetSerializableRoutine(LocalRepositoryFactory repositoryFactory, String path) throws PersistenceException,
            CoreException {
        Object fullFolder;
        List<IRepositoryViewObject> serializableFromFolder;
        IRepositoryViewObject rvo;
        // ### START ### Test for routine
        // retrieve after create
        RoutineItem rItem = createTempRoutineItem(repositoryFactory, path);
        String routineId = rItem.getProperty().getId();
        fullFolder = getFullFolder(repositoryFactory, sampleProject, ERepositoryObjectType.ROUTINES, "");
        serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, routineId,
                ERepositoryObjectType.ROUTINES, true, true, true, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 1);
        rvo = serializableFromFolder.get(0);
        String content = new String(((RoutineItem) (rvo.getProperty().getItem())).getContent().getInnerContent());
        assertTrue(content.equals("myRoutineContent"));

        // test if logical delete (can retrieve or not with flag)
        repositoryFactory.deleteObjectLogical(sampleProject, rvo);
        serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, routineId,
                ERepositoryObjectType.ROUTINES, true, true, true, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 1);
        content = new String(((RoutineItem) (serializableFromFolder.get(0).getProperty().getItem())).getContent()
                .getInnerContent());
        assertTrue(content.equals("myRoutineContent"));

        serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, routineId,
                ERepositoryObjectType.ROUTINES, true, true, false, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 0);

        // test if physical delete
        repositoryFactory.deleteObjectPhysical(sampleProject, rvo, false);
        serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, routineId,
                ERepositoryObjectType.ROUTINES, true, true, true, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 0);

        // // test manual creation, check if can be retrieved.
        // RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
        // Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        // routineItem.setProperty(myProperty);
        // myProperty.setLabel("myTestRoutine");
        // myProperty.setVersion("0.1");
        // routineItem.setContent(PropertiesFactory.eINSTANCE.createByteArray());
        // routineItem.getContent().setInnerContent("myRoutineContent".getBytes());
        // routineItem.getProperty().setId("777");
        // routineItem.setState(PropertiesFactory.eINSTANCE.createItemState());
        // routineItem.getState().setPath(path);
        //
        // IProject project = ResourceModelUtils.getProject(sampleProject);
        // XmiResourceManager xrm = new XmiResourceManager();
        // Resource processItemResource = xrm.createItemResource(project, routineItem, new Path(path),
        // ERepositoryObjectType.ROUTINES, false);
        // Resource propertyResource = xrm.createPropertyResource(routineItem, processItemResource);
        //
        // propertyResource.getContents().add(routineItem.getProperty());
        // propertyResource.getContents().add(routineItem.getState());
        // propertyResource.getContents().add(routineItem);
        //
        // processItemResource.getContents().add(routineItem.getContent());
        // xrm.saveResource(processItemResource);
        // xrm.saveResource(propertyResource);
        // assertNull(routineItem.getParent());
        // // unload the resource to be really the same as created from navigator or even SVN project.
        // xrm.unloadResources(routineItem.getProperty());
        // serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, "777",
        // ERepositoryObjectType.ROUTINES, true, true, true, false);
        // assertNotNull(serializableFromFolder);
        // assertTrue(serializableFromFolder.size() == 1);
        // FolderItem folderItem = repositoryFactory.getFolderItem(sampleProject, ERepositoryObjectType.ROUTINES, new
        // Path(path));
        // routineItem = (RoutineItem) serializableFromFolder.get(0).getProperty().getItem();
        // assertNotNull(routineItem.getParent());
        // assertTrue(routineItem.getParent().equals(folderItem));
        //
        // // test manual deletion
        // IFile fileProperty;
        // IFile fileItem;
        // if ("".equals(path)) {
        // fileProperty = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.ROUTINES)
        // + "/myTestRoutine_0.1.properties"));
        // fileItem = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.ROUTINES)
        // + "/myTestRoutine_0.1.item"));
        // } else {
        // fileProperty = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.ROUTINES) +
        // "/"
        // + path + "/myTestRoutine_0.1.properties"));
        // fileItem = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.ROUTINES) + "/"
        // + path
        // + "/myTestRoutine_0.1.item"));
        // }
        // fileItem.delete(true, null);
        // fileProperty.delete(true, null);
        // serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, "777",
        // ERepositoryObjectType.ROUTINES, true, true, true, false);
        // assertNotNull(serializableFromFolder);
        // assertTrue(serializableFromFolder.size() == 0);
        //
        // // item has been deleted, so link to original folder should be null
        // assertNull(routineItem.getParent());

        // ### END ### Test for routine
    }

    protected void testGetSerializableProcess(LocalRepositoryFactory repositoryFactory, String path) throws PersistenceException,
            CoreException {
        // ### START ### Test for Job
        // retrieve after create
        ProcessItem pItem = createTempProcessItem(repositoryFactory, path);
        String jobId = pItem.getProperty().getId();
        Object fullFolder = getFullFolder(repositoryFactory, sampleProject, ERepositoryObjectType.PROCESS, path);
        List<IRepositoryViewObject> serializableFromFolder = repositoryFactory.getSerializableFromFolder(ProjectManager
                .getInstance().getCurrentProject(), fullFolder, jobId, ERepositoryObjectType.PROCESS, true, true, true, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 1);
        IRepositoryViewObject rvo = serializableFromFolder.get(0);
        assertTrue(((NodeType) ((ProcessItem) (serializableFromFolder.get(0).getProperty().getItem())).getProcess().getNode()
                .get(0)).getComponentVersion().equals("0.1"));

        // test if logical delete (can retrieve or not with flag)
        repositoryFactory.deleteObjectLogical(sampleProject, rvo);
        serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, jobId,
                ERepositoryObjectType.PROCESS, true, true, true, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 1);
        assertTrue(((NodeType) ((ProcessItem) (serializableFromFolder.get(0).getProperty().getItem())).getProcess().getNode()
                .get(0)).getComponentVersion().equals("0.1"));

        serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, jobId,
                ERepositoryObjectType.PROCESS, true, true, false, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 0);

        // test if physical delete
        repositoryFactory.deleteObjectPhysical(sampleProject, rvo, false);
        serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, jobId,
                ERepositoryObjectType.PROCESS, true, true, true, false);
        assertNotNull(serializableFromFolder);
        assertTrue(serializableFromFolder.size() == 0);

        // test manual creation, check if can be retrieved.
        // ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        // Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        // processItem.setProperty(myProperty);
        // myProperty.setLabel("myTestJob");
        // myProperty.setVersion("0.1");
        // processItem.getProperty().setId("666");
        // processItem.setState(PropertiesFactory.eINSTANCE.createItemState());
        // processItem.getState().setPath(path);
        //
        // processItem.setProcess(TalendFileFactory.eINSTANCE.createProcessType());
        // processItem.getProcess().getNode().add(TalendFileFactory.eINSTANCE.createNodeType());
        // ((NodeType) processItem.getProcess().getNode().get(0)).setComponentName("test");
        // ((NodeType) processItem.getProcess().getNode().get(0)).setComponentVersion("0.1");
        //
        // IProject project = ResourceModelUtils.getProject(sampleProject);
        // XmiResourceManager xrm = new XmiResourceManager();
        // Resource processItemResource = xrm.createItemResource(project, processItem, new Path(path),
        // ERepositoryObjectType.PROCESS, false);
        // Resource propertyResource = xrm.createPropertyResource(processItem, processItemResource);
        //
        // propertyResource.getContents().add(processItem.getProperty());
        // propertyResource.getContents().add(processItem.getState());
        // propertyResource.getContents().add(processItem);
        //
        // processItemResource.getContents().add(processItem.getProcess());
        // xrm.saveResource(processItemResource);
        // xrm.saveResource(propertyResource);
        // assertNull(processItem.getParent());
        // // unload the resource to be really the same as created from navigator or even SVN project.
        // xrm.unloadResources(processItem.getProperty());
        // serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, "666",
        // ERepositoryObjectType.PROCESS, true, true, true, false);
        // assertNotNull(serializableFromFolder);
        // assertTrue(serializableFromFolder.size() == 1);
        // FolderItem folderItem = repositoryFactory.getFolderItem(sampleProject, ERepositoryObjectType.PROCESS, new
        // Path(path));
        // processItem = (ProcessItem) serializableFromFolder.get(0).getProperty().getItem();
        // assertNotNull(processItem.getParent());
        // assertTrue(processItem.getParent().equals(folderItem));
        //
        // // test manual deletion
        // IFile fileProperty;
        // IFile fileItem;
        // if ("".equals(path)) {
        // fileProperty = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.PROCESS)
        // + "/myTestJob_0.1.properties"));
        // fileItem = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.PROCESS)
        // + "/myTestJob_0.1.item"));
        // } else {
        // fileProperty = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.PROCESS) +
        // "/"
        // + path + "/myTestJob_0.1.properties"));
        // fileItem = project.getFile(new Path(ERepositoryObjectType.getFolderName(ERepositoryObjectType.PROCESS) + "/"
        // + path
        // + "/myTestJob_0.1.item"));
        // }
        // fileItem.delete(true, null);
        // fileProperty.delete(true, null);
        // serializableFromFolder = repositoryFactory.getSerializableFromFolder(sampleProject, fullFolder, "666",
        // ERepositoryObjectType.PROCESS, true, true, true, false);
        // assertNotNull(serializableFromFolder);
        // assertTrue(serializableFromFolder.size() == 0);
        //
        // // item has been deleted, so link to original folder should be null
        // assertNull(processItem.getParent());

        // ### END ### Test for Job
    }

    private Object getFullFolder(LocalRepositoryFactory repositoryFactory, Project project, ERepositoryObjectType itemType,
            String path) throws PersistenceException {
        Object folder = repositoryFactory.getFolder(project, itemType);
        if (folder == null) {
            return null;
        }
        Object fullFolder;
        if (folder instanceof IFolder) {
            fullFolder = repositoryFactory.getFolder(project, itemType);
            if (path != null && !"".equals(path)) {
                fullFolder = ((IFolder) fullFolder).getFolder(new Path(path));
            }
        } else {
            if (path != null && !"".equals(path)) {
                fullFolder = repositoryFactory.getFolderHelper(project.getEmfProject()).getFolder(
                        ((FolderItem) folder).getProperty().getLabel() + "/" + path); //$NON-NLS-1$
            } else {
                fullFolder = folder;
            }
        }
        return fullFolder;
    }
}
