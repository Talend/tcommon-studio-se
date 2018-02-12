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
package org.talend.repository.items.importexport.handlers.imports;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.general.TalendNature;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.model.emf.CwmResource;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.handlers.model.ImportItem.State;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class ImportBasicHandlerTest {

    private IPath projPath;

    private IPath processPropPath1, processItemPath1, processItemPath2, processItemPath3, connPropPath, connItemPath;

    private Set<IPath> initPathes;

    Project originalProject;

    Project sampleProject;

    @BeforeClass
    @AfterClass
    public static void cleanup() {
        ImportCacheHelper.getInstance().beforePopulateItems(); // clearAll
    }

    @Before
    public void beforeTest() throws PersistenceException, CoreException {
        createTempProject();
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        originalProject = repositoryContext.getProject();
        repositoryContext.setProject(sampleProject);
    }

    @After
    public void afterTest() throws Exception {
        removeTempProject();
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        repositoryContext.setProject(originalProject);
        originalProject = null;
        sampleProject = null;
    }

    private void createTempProject() throws CoreException, PersistenceException {
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

    private void removeTempProject() throws PersistenceException, CoreException {
        // clear the folder, same as it should be in a real logoffProject.
        ProjectManager.getInstance().getFolders(sampleProject.getEmfProject()).clear();
        final IProject project = ResourceUtils.getProject(sampleProject);
        project.delete(true, null);
    }

    @Before
    public void initialize() {
        projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);

        processPropPath1 = new Path("TEST/process/test1_0.1.properties");
        processItemPath1 = new Path("TEST/process/test1_0.1.item");
        processItemPath2 = new Path("TEST/process/abc/xyz/test2_0.1.properties");
        processItemPath3 = new Path("TEST/process/abc/xyz/test2_0.1.item");
        connPropPath = new Path("TEST/metadata/connections/abc/xyz/test3_0.1.properties");
        connItemPath = new Path("TEST/metadata/connections/abc/xyz/test3_0.1.item");

        initPathes = new HashSet<IPath>();
        initPathes.add(projPath);
        initPathes.add(processPropPath1);
        initPathes.add(processItemPath1);
        initPathes.add(processItemPath2);
        initPathes.add(processItemPath3);
        initPathes.add(connPropPath);
        initPathes.add(connItemPath);
    }

    @Test
    public void testValid4NoBasePath() throws Exception {
        // process
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertFalse(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));

        Assert.assertFalse(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));

        Assert.assertFalse(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));

        Assert.assertFalse(basicHandler.valid(resManager, projPath));
    }

    @Test
    public void testValid4Process() throws Exception {
        // process
        ImportBasicHandler basicHandler = new ImportBasicHandler();
        Map<String, String> data = new HashMap<String, String>();
        data.put(IImportConstants.PARAM_PATH, "process,context");
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertTrue(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));

        Assert.assertTrue(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));

        Assert.assertFalse(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));

        Assert.assertFalse(basicHandler.valid(resManager, projPath));
    }

    @Test
    public void testValid4Context() throws Exception {
        // for context

        ImportBasicHandler basicHandler = new ImportBasicHandler();

        Map<String, String> data = new HashMap<String, String>();
        data.put(IImportConstants.PARAM_PATH, "context");
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertFalse(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));
        Assert.assertFalse(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));

        IPath contextPropPath = new Path("TEST/context/test_0.1.properties");
        IPath contextItemPath = new Path("TEST/context/test_0.1.item");
        pathes.add(contextPropPath);
        pathes.add(contextItemPath);
        Assert.assertTrue(basicHandler.valid(resManager, contextPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, contextItemPath));
    }

    @Test
    public void testValid4Connection() throws Exception {
        // DB connections
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        Map<String, String> data = new HashMap<String, String>();
        data.put(IImportConstants.PARAM_PATH, "context,metadata/connections");
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertFalse(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));

        Assert.assertFalse(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));

        Assert.assertTrue(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));
    }

    @Test
    public void testValid4Unknown() throws Exception {
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        Map<String, String> data = new HashMap<String, String>();
        data.put(IImportConstants.PARAM_PATH, "XXX");
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertFalse(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));
        Assert.assertFalse(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));
    }

    @Test
    public void testCheckAndSetProject4NoProject() {
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        ImportItem ImportItem = new ImportItem(processPropPath1);
        Property property = mock(Property.class);
        ImportItem.setProperty(property);

        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        when(property.getItem()).thenReturn(item);

        ResourcesManager resManager = mock(ResourcesManager.class);
        basicHandler.checkAndSetProject(resManager, ImportItem);

        Assert.assertFalse(ImportItem.getErrors().isEmpty());
        Assert.assertFalse(ImportItem.isValid());
        Assert.assertTrue(ImportItem.getErrors().size() == 1);
    }

    @Test
    public void testIsSame() {
        ImportItem ImportItem1 = mock(ImportItem.class);
        Property property1 = mock(Property.class);
        when(ImportItem1.getProperty()).thenReturn(property1);
        when(property1.getId()).thenReturn("123456789");
        when(property1.getLabel()).thenReturn("test");

        ImportItem ImportItem2 = mock(ImportItem.class);
        Property property2 = mock(Property.class);
        when(ImportItem2.getProperty()).thenReturn(property2);
        when(property2.getId()).thenReturn("123456789");
        when(property2.getLabel()).thenReturn("test");

        ImportBasicHandler basicHandler = new ImportBasicHandler();
        Assert.assertTrue(basicHandler.isSame(ImportItem1, ImportItem2));
    }

    @Test
    public void testIsSame4DiffId() {
        ImportItem ImportItem1 = mock(ImportItem.class);
        Property property1 = mock(Property.class);
        when(ImportItem1.getProperty()).thenReturn(property1);
        when(property1.getId()).thenReturn("987654321");
        when(property1.getVersion()).thenReturn("0.1");

        ImportItem ImportItem2 = mock(ImportItem.class);
        Property property2 = mock(Property.class);
        when(ImportItem2.getProperty()).thenReturn(property2);
        when(property2.getId()).thenReturn("123456789");
        when(property2.getVersion()).thenReturn("0.1");

        ImportBasicHandler basicHandler = new ImportBasicHandler();
        Assert.assertFalse(basicHandler.isSame(ImportItem1, ImportItem2));
    }

    @Test
    public void testIsSame4DiffVersion() {
        ImportItem ImportItem1 = mock(ImportItem.class);
        Property property1 = mock(Property.class);
        when(ImportItem1.getProperty()).thenReturn(property1);
        when(property1.getId()).thenReturn("123456789");
        when(property1.getVersion()).thenReturn("0.1");

        ImportItem ImportItem2 = mock(ImportItem.class);
        Property property2 = mock(Property.class);
        when(ImportItem2.getProperty()).thenReturn(property2);
        when(property2.getId()).thenReturn("123456789");
        when(property2.getVersion()).thenReturn("0.2");

        ImportBasicHandler basicHandler = new ImportBasicHandler();
        Assert.assertFalse(basicHandler.isSame(ImportItem1, ImportItem2));
    }

    @Test
    public void testIsSameName() {
        ImportItem ImportItem1 = mock(ImportItem.class);
        Property property1 = mock(Property.class);
        when(ImportItem1.getProperty()).thenReturn(property1);
        when(property1.getLabel()).thenReturn("test");
        // when(property1.getId()).thenReturn("123456789");

        IRepositoryViewObject repViewObj = mock(IRepositoryViewObject.class);
        when(repViewObj.getLabel()).thenReturn("test");
        // when(repViewObj.getId()).thenReturn("123456789");

        ImportBasicHandler basicHandler = new ImportBasicHandler();
        // now only check the label
        Assert.assertTrue(basicHandler.isSameName(ImportItem1, repViewObj));
    }

    @Test
    public void testIsSameName4DiffLabel() {
        ImportItem ImportItem1 = mock(ImportItem.class);
        Property property1 = mock(Property.class);
        when(ImportItem1.getProperty()).thenReturn(property1);
        when(property1.getLabel()).thenReturn("test1");
        // when(property1.getId()).thenReturn("123456789");

        IRepositoryViewObject repViewObj = mock(IRepositoryViewObject.class);
        when(repViewObj.getLabel()).thenReturn("test2");
        // when(repViewObj.getId()).thenReturn("123456789");

        ImportBasicHandler basicHandler = new ImportBasicHandler();
        // only when same label with diff id. don't care the same id with diff label.
        Assert.assertFalse(basicHandler.isSameName(ImportItem1, repViewObj));
    }

    // @Test
    // public void testIsSameName4DiffId() {
    // ImportItem ImportItem1 = mock(ImportItem.class);
    // Property property1 = mock(Property.class);
    // when(ImportItem1.getProperty()).thenReturn(property1);
    // when(property1.getLabel()).thenReturn("test");
    // when(property1.getId()).thenReturn("987654321");
    //
    // IRepositoryViewObject repViewObj = mock(IRepositoryViewObject.class);
    // when(repViewObj.getLabel()).thenReturn("test");
    // when(repViewObj.getId()).thenReturn("123456789");
    //
    // ImportBasicHandler basicHandler = new ImportBasicHandler();
    // // diff id, don't care same label.
    // Assert.assertTrue(basicHandler.isSameName(ImportItem1, repViewObj));
    // }

    @Test
    public void testCreateResource() throws Exception {
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        // create process item resource
        ImportItem processImportItem = new ImportItem(processItemPath1);
        Resource processItemResource = basicHandler.createResource(processImportItem, processItemPath1, false);
        Assert.assertTrue(processItemResource != null);
        Assert.assertTrue(processItemResource.getURI().fileExtension().equals("item"));

        // create property resource
        ImportItem propertyRecord = new ImportItem(processPropPath1);
        Resource propertyResource = basicHandler.createResource(propertyRecord, processPropPath1, false);
        Assert.assertTrue(propertyResource != null);
        Assert.assertTrue(propertyResource.getURI().fileExtension().equals("properties"));
    }

    @Test
    public void testCreateItemResource() {
        URI uri = URI.createURI(processPropPath1.toPortableString());
        ImportBasicHandler basicHandler = new ImportBasicHandler();
        Resource resource = basicHandler.createItemResource(uri);

        Assert.assertNotNull(resource);
        Assert.assertEquals(CwmResource.class, resource.getClass());
    }

    @Test
    public void testLoadItemResource() throws Exception {
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        ImportItem ImportItem = new ImportItem(processPropPath1);
        ImportItem.setItemName(processPropPath1.lastSegment());
        // Property
        Property property = mock(Property.class);
        ImportItem.setProperty(property);
        // Resources
        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>();
        IPath projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);
        pathes.add(projPath);
        pathes.add(processPropPath1);
        pathes.add(processItemPath1);
        when(resManager.getPaths()).thenReturn(pathes);
        // call
        Resource resource = basicHandler.loadResource(resManager, ImportItem);
        //
        Assert.assertNull(resource);
        //
    }

    @Test
    public void testCheckItem() throws Exception {
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        ImportItem ImportItem = new ImportItem(processPropPath1);
        ImportItem.setItemName(processPropPath1.lastSegment());
        // Property
        Property property = mock(Property.class);
        ImportItem.setProperty(property);
        // Process Item
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        when(property.getItem()).thenReturn(item);
        // Resources
        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>();
        IPath projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);
        pathes.add(projPath);
        pathes.add(processPropPath1);
        pathes.add(processItemPath1);
        // Case 1 : item no state
        Assert.assertFalse(basicHandler.checkItem(resManager, ImportItem, false));
        Assert.assertFalse(ImportItem.getErrors().isEmpty());
        // Case 2 : init item state
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        item.setState(itemState);
        Assert.assertTrue(basicHandler.checkItem(resManager, ImportItem, false));
        // Case 3 : item not been locked , then overwrite
        ImportItem.setState(State.NAME_AND_ID_EXISTED);
        Assert.assertTrue(basicHandler.checkItem(resManager, ImportItem, true));
        // Case 4 : item been locked , can not overwrite
        // when(ImportCacheHelper.getInstance().getRepObjectcache().getItemLockState(ImportItem)).thenReturn(true);
        Assert.assertTrue(basicHandler.checkItem(resManager, ImportItem, true));
        Assert.assertTrue(ImportItem.getErrors().size() == 1);
        // ...
    }

    @Test
    public void testComputeImportItem() throws Exception {
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        ImportItem ImportItem = new ImportItem(processPropPath1);
        ImportItem.setItemName(processPropPath1.lastSegment());
        // Property
        Property property = mock(Property.class);
        ImportItem.setProperty(property);
        // Resources
        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>();
        IPath projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);
        pathes.add(projPath);
        pathes.add(processPropPath1);
        pathes.add(processItemPath1);
        when(resManager.getPaths()).thenReturn(pathes);
        // call
        basicHandler.computeImportItem(resManager, processPropPath1);
        // Load Resource failed
        Assert.assertNull(basicHandler.loadResource(resManager, ImportItem));
        Assert.assertTrue(ImportCacheHelper.getInstance().getImportErrors().size() == 1);
        // ...
    }

    @Test
    public void testResolveItem4ValidItem() throws Exception {
        // 1) a.properties / a.item / href all normal
        // => normal item, can be import
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        ImportItem ImportItem = new ImportItem(processPropPath1);
        ImportItem.setItemName(processPropPath1.lastSegment());
        // Property
        Property property = mock(Property.class);
        ImportItem.setProperty(property);

        // Process Item
        ProcessItem processItem = createTempProcessItem();
        when(property.getItem()).thenReturn(processItem);
        // Resources
        ResourcesManager resManager = spy(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>();
        IPath projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);
        pathes.add(projPath);
        pathes.add(processPropPath1);
        pathes.add(processItemPath1);
        when(resManager.getPaths()).thenReturn(pathes);
        // call
        when(resManager.getStream(processItemPath1)).thenReturn(new InputStream() {

            @Override
            public int read() throws IOException {
                return 1;
            }
        });
        basicHandler.resolveItem(resManager, ImportItem);

        // only check this
        Assert.assertTrue(ImportItem.getErrors().isEmpty());
        Assert.assertTrue(ImportItem.isValid());
        Assert.assertTrue(ImportItem.getErrors().size() == 0);
    }

    @Test
    public void testResolveItem4MissItemFile() throws Exception {
        ImportBasicHandler basicHandler = new ImportBasicHandler();

        ImportItem ImportItem = new ImportItem(processPropPath1);
        ImportItem.setItemName(processPropPath1.lastSegment());
        // Property
        Property property = mock(Property.class);
        ImportItem.setProperty(property);

        // Process Item
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        when(property.getItem()).thenReturn(item);
        // Resources
        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>();
        IPath projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);
        pathes.add(projPath);
        pathes.add(processPropPath1);
        when(resManager.getPaths()).thenReturn(pathes);
        // call
        basicHandler.resolveItem(resManager, ImportItem);

        //
        Assert.assertFalse(ImportItem.getErrors().isEmpty());
        Assert.assertFalse(ImportItem.isValid());
        Assert.assertTrue(ImportItem.getErrors().size() == 1);
    }

    private ProcessItem createTempProcessItem() {
        ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        Property myProperty = PropertiesFactory.eINSTANCE.createProperty();
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(false);
        itemState.setPath("TEST/process/test1_0.1.item");
        processItem.setState(itemState);
        processItem.setProperty(myProperty);
        myProperty.setLabel("test1");
        myProperty.setVersion("0.1");

        processItem.setProcess(TalendFileFactory.eINSTANCE.createProcessType());
        processItem.getProcess().getNode().add(TalendFileFactory.eINSTANCE.createNodeType());
        ((NodeType) processItem.getProcess().getNode().get(0)).setComponentName("test");
        ((NodeType) processItem.getProcess().getNode().get(0)).setComponentVersion("0.1");

        return processItem;
    }
}
