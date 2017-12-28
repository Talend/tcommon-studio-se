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
package org.talend.repository.items.importexport.ui.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.general.TalendNature;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.ImportExportHandlersManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.ui.managers.FileResourcesUnityManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class ImportIntegrationTest {

    private static int nbTests = 0;

    private static Project originalProject;

    private Project sampleProject;

    @BeforeClass
    public static void recordOriginalProject() throws PersistenceException, CoreException, LoginException {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        originalProject = repositoryContext.getProject();
    }

    @AfterClass
    public static void relogonOriginalProject() throws Exception {
        if (originalProject != null) {
            IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

                @Override
                public void run(IProgressMonitor monitor) throws CoreException {
                    try {
                        ProxyRepositoryFactory.getInstance().logOnProject(originalProject, new NullProgressMonitor());
                    } catch (Exception e) {
                        throw new CoreException(new org.eclipse.core.runtime.Status(IStatus.ERROR,
                                "org.talend.repository.svnprovider.test", e.getMessage(), e)); //$NON-NLS-1$
                    }
                }
            };

            ResourcesPlugin.getWorkspace().run(runnable, ResourcesPlugin.getWorkspace().getRoot(), IWorkspace.AVOID_UPDATE,
                    new NullProgressMonitor());
        }
        originalProject = null;
    }

    @Before
    public void beforeTest() throws PersistenceException, CoreException, LoginException {
        IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

            @Override
            public void run(IProgressMonitor monitor) throws CoreException {
                try {
                    ProxyRepositoryFactory.getInstance().logOffProject();
                    createTempProject();
                    ProxyRepositoryFactory.getInstance().logOnProject(sampleProject, new NullProgressMonitor());
                } catch (Exception e) {
                    throw new CoreException(new org.eclipse.core.runtime.Status(IStatus.ERROR,
                            "org.talend.repository.svnprovider.test", e.getMessage(), e)); //$NON-NLS-1$
                }
            }
        };

        ResourcesPlugin.getWorkspace().run(runnable, ResourcesPlugin.getWorkspace().getRoot(), IWorkspace.AVOID_UPDATE,
                new NullProgressMonitor());
    }

    @After
    public void afterTest() throws Exception {
        removeTempProject();
        ProxyRepositoryFactory.getInstance().logOffProject();
        sampleProject = null;
    }

    private void createTempProject() throws CoreException, PersistenceException, LoginException {
        Project projectInfor = new Project();
        projectInfor.setLabel("importTest" + nbTests);
        nbTests++;
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

    FileResourcesUnityManager resManager;

    ImportExportHandlersManager manager;

    private List<ImportItem> getImportItems1() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/importItems1.zip");
        manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("importItems1.zip not found");
        }
        resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();
        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());
        return importItems;
    }

    private void initialize() throws Exception {
        List<ImportItem> importItems = getImportItems1();
        manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));
    }

    @Test
    public void testItemSameNameSameId() throws Exception {
        initialize();

        List<ImportItem> importItems = getImportItems1();
        boolean found = false;
        for (ImportItem item : importItems) {
            if (item.getItem().getProperty().getId().equals("_A7WlgP9BEeOBFtZnw6gdMg")) {
                ProcessItem processItem = (ProcessItem) item.getItem();
                processItem.getProcess().getNode().add(TalendFileFactory.eINSTANCE.createNodeType());
                ((NodeType) processItem.getProcess().getNode().get(0)).setComponentName("test");
                ((NodeType) processItem.getProcess().getNode().get(0)).setComponentVersion("0.1");
                Assert.assertEquals(ImportItem.State.NAME_AND_ID_EXISTED, item.getState());
                found = true;
            }
        }
        if (!found) {
            throw new Exception("Items in the zip maybe changed, id not found: _A7WlgP9BEeOBFtZnw6gdMg");
        }
        boolean wrongImport = true;
        try {
            manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, false,
                    importItems.toArray(new ImportItem[0]), new Path(""));
        } catch (Exception e) {
            wrongImport = false;
        }
        if (wrongImport) {
            throw new Exception("Item should have been existing in repository but no exception thrown");
        }

        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

        // test result.
        // overwrite is not set, so we should NOT have the test component in the job
        IRepositoryViewObject object1 = repositoryFactory.getLastVersion(sampleProject, "_A7WlgP9BEeOBFtZnw6gdMg");
        Property property = object1.getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        found = false;
        for (Object node : pItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) node;
            if (nodeType.getComponentName().equals("test")) {
                found = true;
                break;
            }
        }
        if (found) {
            throw new Exception("Overwrite was not set, we should not have the item imported");
        }
        importItems = getImportItems1();
        for (ImportItem item : importItems) {
            if (item.getItem().getProperty().getId().equals("_A7WlgP9BEeOBFtZnw6gdMg")) {
                ProcessItem processItem = (ProcessItem) item.getItem();
                processItem.getProcess().getNode().add(TalendFileFactory.eINSTANCE.createNodeType());
                ((NodeType) processItem.getProcess().getNode().get(0)).setComponentName("test");
                ((NodeType) processItem.getProcess().getNode().get(0)).setComponentVersion("0.1");
                Assert.assertEquals(ImportItem.State.NAME_AND_ID_EXISTED, item.getState());
            }
        }
        manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));

        // test result.
        // we should have item we imported replace the old one (so the test component added should be present).
        object1 = repositoryFactory.getLastVersion(sampleProject, "_A7WlgP9BEeOBFtZnw6gdMg");
        property = object1.getProperty();
        pItem = (ProcessItem) property.getItem();
        found = false;
        for (Object node : pItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) node;
            if (nodeType.getComponentName().equals("test")) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Overwrite for same id / same name failed");
        }
    }

    private List<ImportItem> getImportItems2() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/importItems2.zip");
        manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("importItems2.zip not found");
        }
        resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();
        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());
        return importItems;
    }

    @SuppressWarnings("null")
    @Test
    public void testItemDifferentNameSameId() throws Exception {
        initialize();

        List<ImportItem> importItems = getImportItems2();
        boolean found = false;
        ImportItem testedItem = null;
        for (ImportItem item : importItems) {
            if (item.getItem().getProperty().getId().equals("_A7WlgP9BEeOBFtZnw6gdMg")) {
                Assert.assertEquals(ImportItem.State.ID_EXISTED, item.getState());
                testedItem = item;
                found = true;
            }
        }
        if (!found) {
            throw new Exception("Items in the zip maybe changed, id not found: _A7WlgP9BEeOBFtZnw6gdMg");
        }
        boolean wrongImport = false;
        try {
            manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, false,
                    importItems.toArray(new ImportItem[0]), new Path(""));
        } catch (Exception e) {
            wrongImport = true;
        }
        if (wrongImport) {
            throw new Exception("Import should not need overwrite here since name is different and id is the same");
        }
        String newItemId = testedItem.getItemId();

        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        IRepositoryViewObject object1 = repositoryFactory.getLastVersion(sampleProject, "_A7WlgP9BEeOBFtZnw6gdMg");
        Property property = object1.getProperty();
        Assert.assertEquals("simpleJob", property.getLabel());

        object1 = repositoryFactory.getLastVersion(sampleProject, newItemId);
        property = object1.getProperty();
        Assert.assertEquals("newJob", property.getLabel());
    }

    private List<ImportItem> getImportItems3() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/importItems3.zip");
        manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("importItems3.zip not found");
        }
        resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();
        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());
        return importItems;
    }

    @Test
    @Ignore
    public void testItemSameNameDifferentId() throws Exception {
        initialize();

        List<ImportItem> importItems = getImportItems3();
        boolean found = false;
        for (ImportItem item : importItems) {
            if (item.getItem().getProperty().getId().equals("_nuQhIAS1EeSC2uzKzBrHFA")) {
                Assert.assertEquals(ImportItem.State.NAME_EXISTED, item.getState());
                found = true;
            }
        }
        if (!found) {
            throw new Exception("Items in the zip maybe changed, id not found: _nuQhIAS1EeSC2uzKzBrHFA");
        }
        boolean wrongImport = true;
        try {
            manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, false,
                    importItems.toArray(new ImportItem[0]), new Path(""));
        } catch (Exception e) {
            wrongImport = false;
        }
        if (wrongImport) {
            throw new Exception("Item should have been existing in repository but no exception thrown");
        }

        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

        importItems = getImportItems3();
        for (ImportItem item : importItems) {
            if (item.getItem().getProperty().getId().equals("_nuQhIAS1EeSC2uzKzBrHFA")) {
                Assert.assertEquals(ImportItem.State.NAME_EXISTED, item.getState());
            }
        }
        manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));

        // test result.
        // we should have item we imported replace the old one (so the test component added should be present).
        IRepositoryViewObject object1 = repositoryFactory.getLastVersion(sampleProject, "_A7WlgP9BEeOBFtZnw6gdMg");
        Assert.assertNotNull(object1);

        object1 = repositoryFactory.getLastVersion(sampleProject, "_nuQhIAS1EeSC2uzKzBrHFA");
        Assert.assertNull(object1);
        if (!found) {
            throw new Exception("Overwrite for same id / same name failed");
        }
    }

    private List<ImportItem> getImportItems4() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/importItems4.zip");
        manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("importItems4.zip not found");
        }
        resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();
        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());
        return importItems;
    }

    /**
     * Import subjob first. <br>
     * Then import mainjob + subjob (different name/same id) <br>
     * After the 2nd import, the mainjob should be updated to use the new id regenerated of the subjob just imported.
     *
     * @throws Exception
     */
    @SuppressWarnings("null")
    @Test
    public void testItemDifferentNameSameIdWithDep() throws Exception {
        // this one is directly test case of bug TDI-29063
        List<ImportItem> importItems = getImportItems4();
        manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));

        importItems = getImportItems1();
        int nbFound = 0;
        ImportItem testedItem = null;
        List<ImportItem> checkedItems = new ArrayList<ImportItem>();
        for (ImportItem item : importItems) {
            // subjob
            if (item.getItem().getProperty().getId().equals("_Nh85oP9BEeOBFtZnw6gdMg")) {
                Assert.assertEquals(ImportItem.State.ID_EXISTED, item.getState());
                testedItem = item;
                checkedItems.add(item);
                nbFound++;
            }
            // main job
            if (item.getItem().getProperty().getId().equals("_FenVUP9BEeOBFtZnw6gdMg")) {
                checkedItems.add(item);
                nbFound++;
            }
        }
        if (nbFound != 2) {
            throw new Exception("Items in the zip maybe changed, ids not found");
        }
        manager.importItemRecords(new NullProgressMonitor(), resManager, checkedItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));
        String newItemId = testedItem.getItemId();

        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        IRepositoryViewObject object1 = repositoryFactory.getLastVersion(sampleProject, "_Nh85oP9BEeOBFtZnw6gdMg");
        Property property = object1.getProperty();
        Assert.assertEquals("myJob", property.getLabel());

        object1 = repositoryFactory.getLastVersion(sampleProject, newItemId);
        property = object1.getProperty();
        Assert.assertEquals("jobWithConnection", property.getLabel());

        // main job, check if it was updated
        object1 = repositoryFactory.getLastVersion(sampleProject, "_FenVUP9BEeOBFtZnw6gdMg");
        property = object1.getProperty();
        ProcessItem pItem = (ProcessItem) property.getItem();
        boolean updatedItem = false;
        for (Object node : pItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) node;
            if (nodeType.getComponentName().equals("tRunJob")) {
                for (Object elemParam : nodeType.getElementParameter()) {
                    ElementParameterType paramType = (ElementParameterType) elemParam;
                    if (paramType.getName().equals("PROCESS:PROCESS_TYPE_PROCESS")) {
                        if (paramType.getValue().equals(newItemId)) {
                            updatedItem = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!updatedItem) {
            throw new Exception("id of the subjob should have been updated in the main job to update relationship");
        }
    }

    private List<ImportItem> getImportItems5() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/importItems5.zip");
        manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("importItems5.zip not found");
        }
        resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();
        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());
        return importItems;
    }

    /**
     * Import first 1 db connection, 1 file item. <br>
     * Import after exactly the same items, with different names but with same id, and one job with dependencies on
     * them. <br>
     * File and db connection id must be regenerated. Job who use them should be updated for references with the new id.
     *
     * @throws Exception
     */
    @SuppressWarnings("null")
    @Test
    @Ignore
    public void testItemDifferentNameSameIdWithDep2() throws Exception {
        List<ImportItem> importItems = getImportItems5();
        manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));

        importItems = getImportItems1();
        int nbFound = 0;
        ImportItem fileItemId = null;
        ImportItem dbItemId = null;
        List<ImportItem> checkedItems = new ArrayList<ImportItem>();
        for (ImportItem item : importItems) {
            if (item.getItem().getProperty().getId().equals("_Nh85oP9BEeOBFtZnw6gdMg")) {
                checkedItems.add(item);
                nbFound++;
            }
            // file connection
            if (item.getItem().getProperty().getId().equals("_YR3uEP9DEeOBFtZnw6gdMg")) {
                Assert.assertEquals(ImportItem.State.ID_EXISTED, item.getState());
                fileItemId = item;
                checkedItems.add(item);
                nbFound++;
            }
            // db connection
            if (item.getItem().getProperty().getId().equals("_h5GaAKj_EeO1WOYXYCMHUw")) {
                Assert.assertEquals(ImportItem.State.ID_EXISTED, item.getState());
                dbItemId = item;
                checkedItems.add(item);
                nbFound++;
            }
        }
        if (nbFound != 3) {
            throw new Exception("Items in the zip maybe changed, ids not found");
        }
        boolean wrongImport = false;
        try {
            manager.importItemRecords(new NullProgressMonitor(), resManager, checkedItems, false,
                    importItems.toArray(new ImportItem[0]), new Path(""));
        } catch (Exception e) {
            wrongImport = true;
        }
        if (wrongImport) {
            throw new Exception("Import should not need overwrite here since name is different and id is the same");
        }
        String newItem1Id = fileItemId.getItemId();
        String newItem2Id = dbItemId.getItemId();

        // new db connection imported
        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        IRepositoryViewObject object1 = repositoryFactory.getLastVersion(sampleProject, newItem1Id);
        Property property = object1.getProperty();
        Assert.assertEquals("file", property.getLabel());

        object1 = repositoryFactory.getLastVersion(sampleProject, "_Nh85oP9BEeOBFtZnw6gdMg");
        property = object1.getProperty();
        Assert.assertEquals("jobWithConnection", property.getLabel());
        // job, check if connection was updated
        ProcessItem pItem = (ProcessItem) property.getItem();
        int nbUpdated = 0;
        for (Object node : pItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) node;
            if (nodeType.getComponentName().equals("tFileInputDelimited")) {
                for (Object elemParam : nodeType.getElementParameter()) {
                    ElementParameterType paramType = (ElementParameterType) elemParam;
                    if (paramType.getName().equals("PROPERTY:REPOSITORY_PROPERTY_TYPE")) {
                        if (!paramType.getValue().equals(newItem1Id)) {
                            throw new Exception("file id not updated in tFileInputDelimited");
                        }
                        nbUpdated++;
                    }
                    if (paramType.getName().equals("SCHEMA:REPOSITORY_SCHEMA_TYPE")) {
                        if (!paramType.getValue().startsWith(newItem1Id + " - ")) {
                            throw new Exception("file id not updated in tFileInputDelimited");
                        }
                        nbUpdated++;
                    }
                    if (paramType.getName().equals("SCHEMA_REJECT:REPOSITORY_SCHEMA_TYPE")) {
                        if (!paramType.getValue().startsWith(newItem1Id + " - ")) {
                            throw new Exception("file id not updated in tFileInputDelimited");
                        }
                        nbUpdated++;
                    }
                }
            }
        }
        if (nbUpdated != 3) {
            throw new Exception("All file delimited id were not checked");
        }

        nbUpdated = 0;
        for (Object elemParam : pItem.getProcess().getParameters().getElementParameter()) {
            ElementParameterType paramType = (ElementParameterType) elemParam;
            // check if implicit was updated: PROPERTY_TYPE_IMPLICIT_CONTEXT:REPOSITORY_PROPERTY_TYPE
            if (paramType.getName().equals("PROPERTY_TYPE_IMPLICIT_CONTEXT:REPOSITORY_PROPERTY_TYPE")) {
                nbUpdated++;
                if (!paramType.getValue().equals(newItem2Id)) {
                    throw new Exception("Implicit id should have been updated in the job when update relationship");
                }
            }
            // check if statandlogs was updated: PROPERTY_TYPE:REPOSITORY_PROPERTY_TYPE
            if (paramType.getName().equals("PROPERTY_TYPE:REPOSITORY_PROPERTY_TYPE")) {
                nbUpdated++;
                if (!paramType.getValue().equals(newItem2Id)) {
                    throw new Exception("StatAndLogs id should have been updated in the job when update relationship");
                }
            }
        }
        if (nbUpdated != 2) {
            throw new Exception("Both StatAndLogs And Implicit was not checked / updated");
        }

    }

    private List<ImportItem> getImportItems6() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/importItems6.zip");
        manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("importItems6.zip not found");
        }
        resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();
        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());
        return importItems;
    }

    /**
     * Import first 1 db connection, 1 file item, 1 job with dependencies to those items. <br>
     * Import after the 1 main job, with subjob, 1 db conn, 1 file item <br>
     * Subjob, db conn, file item, have all same name, but different id in the 2 imports.<br>
     * After import, we should get imported items, but those existing before should get the id of items existing before.
     * Main job should have also it's subjob dependency updated to use the old id of the item.
     *
     * @throws Exception
     */
    @SuppressWarnings("null")
    @Test
    @Ignore
    public void testItemSameNameDifferentIdWithDep() throws Exception {
        List<ImportItem> importItems = getImportItems6();
        manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));

        importItems = getImportItems1();
        int nbFound = 0;
        ImportItem fileItemId = null;
        ImportItem dbItemId = null;
        List<ImportItem> checkedItems = new ArrayList<ImportItem>();
        for (ImportItem item : importItems) {
            // main job
            if (item.getItem().getProperty().getId().equals("_FenVUP9BEeOBFtZnw6gdMg")) {
                checkedItems.add(item);
                nbFound++;
            }
            // subjob
            if (item.getItem().getProperty().getId().equals("_Nh85oP9BEeOBFtZnw6gdMg")) {
                Assert.assertEquals(ImportItem.State.NAME_EXISTED, item.getState());
                checkedItems.add(item);
                nbFound++;
                // add a test node in the job to make sure we really imported this one
                ProcessItem processItem = (ProcessItem) item.getItem();
                processItem.getProcess().getNode().add(TalendFileFactory.eINSTANCE.createNodeType());
                ((NodeType) processItem.getProcess().getNode().get(0)).setComponentName("test");
                ((NodeType) processItem.getProcess().getNode().get(0)).setComponentVersion("0.1");
            }
            // file connection
            if (item.getItem().getProperty().getId().equals("_YR3uEP9DEeOBFtZnw6gdMg")) {
                Assert.assertEquals(ImportItem.State.NAME_EXISTED, item.getState());
                fileItemId = item;
                checkedItems.add(item);
                nbFound++;
            }
            // db connection
            if (item.getItem().getProperty().getId().equals("_h5GaAKj_EeO1WOYXYCMHUw")) {
                Assert.assertEquals(ImportItem.State.NAME_EXISTED, item.getState());
                dbItemId = item;
                checkedItems.add(item);
                nbFound++;
            }
        }
        if (nbFound != 4) {
            throw new Exception("Items in the zip maybe changed, ids not found");
        }
        manager.importItemRecords(new NullProgressMonitor(), resManager, checkedItems, true,
                importItems.toArray(new ImportItem[0]), new Path(""));

        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

        // id of the item existing before
        IRepositoryViewObject object1 = repositoryFactory.getLastVersion(sampleProject, "_lVtQYATsEeSC2uzKzBrHFA");
        Property property = object1.getProperty();
        Assert.assertEquals("jobWithConnection", property.getLabel());
        ProcessItem pItem = (ProcessItem) property.getItem();

        boolean found = false;
        // check it's really the one we just imported but with old id.
        for (Object node : pItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) node;
            if (nodeType.getComponentName().equals("test")) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Overwrite for same id / same name failed");
        }

        // job, check if connection was updated with old id info
        int nbUpdated = 0;
        String oldFileId = "_nbX8YATsEeSC2uzKzBrHFA"; // id of the file existing before the 2nd import
        for (Object node : pItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) node;
            if (nodeType.getComponentName().equals("tFileInputDelimited")) {
                for (Object elemParam : nodeType.getElementParameter()) {
                    ElementParameterType paramType = (ElementParameterType) elemParam;
                    if (paramType.getName().equals("PROPERTY:REPOSITORY_PROPERTY_TYPE")) {
                        if (!paramType.getValue().equals(oldFileId)) {
                            throw new Exception("file id not updated in tFileInputDelimited");
                        }
                        nbUpdated++;
                    }
                    if (paramType.getName().equals("SCHEMA:REPOSITORY_SCHEMA_TYPE")) {
                        if (!paramType.getValue().startsWith(oldFileId + " - ")) {
                            throw new Exception("file id not updated in tFileInputDelimited");
                        }
                        nbUpdated++;
                    }
                    if (paramType.getName().equals("SCHEMA_REJECT:REPOSITORY_SCHEMA_TYPE")) {
                        if (!paramType.getValue().startsWith(oldFileId + " - ")) {
                            throw new Exception("file id not updated in tFileInputDelimited");
                        }
                        nbUpdated++;
                    }
                }
            }
        }
        if (nbUpdated != 3) {
            throw new Exception("All file delimited id were not checked");
        }

        nbUpdated = 0;
        String oldDbConnId = "_wBZ-EATsEeSC2uzKzBrHFA"; // id of the db connection existing before the 2nd import
        for (Object elemParam : pItem.getProcess().getParameters().getElementParameter()) {
            ElementParameterType paramType = (ElementParameterType) elemParam;
            // check if implicit was updated: PROPERTY_TYPE_IMPLICIT_CONTEXT:REPOSITORY_PROPERTY_TYPE
            if (paramType.getName().equals("PROPERTY_TYPE_IMPLICIT_CONTEXT:REPOSITORY_PROPERTY_TYPE")) {
                nbUpdated++;
                if (!paramType.getValue().equals(oldDbConnId)) {
                    throw new Exception("Implicit id should have been updated in the job when update relationship");
                }
            }
            // check if statandlogs was updated: PROPERTY_TYPE:REPOSITORY_PROPERTY_TYPE
            if (paramType.getName().equals("PROPERTY_TYPE:REPOSITORY_PROPERTY_TYPE")) {
                nbUpdated++;
                if (!paramType.getValue().equals(oldDbConnId)) {
                    throw new Exception("StatAndLogs id should have been updated in the job when update relationship");
                }
            }
        }
        if (nbUpdated != 2) {
            throw new Exception("Both StatAndLogs And Implicit was not checked / updated");
        }

        // main job, check if it was updated
        object1 = repositoryFactory.getLastVersion(sampleProject, "_FenVUP9BEeOBFtZnw6gdMg");
        property = object1.getProperty();
        pItem = (ProcessItem) property.getItem();
        boolean updatedItem = false;
        for (Object node : pItem.getProcess().getNode()) {
            NodeType nodeType = (NodeType) node;
            if (nodeType.getComponentName().equals("tRunJob")) {
                for (Object elemParam : nodeType.getElementParameter()) {
                    ElementParameterType paramType = (ElementParameterType) elemParam;
                    if (paramType.getName().equals("PROCESS:PROCESS_TYPE_PROCESS")) {
                        if (paramType.getValue().equals("_lVtQYATsEeSC2uzKzBrHFA")) {
                            updatedItem = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!updatedItem) {
            throw new Exception("id of the subjob should have been updated in the main job to update relationship");
        }

    }

    private List<ImportItem> getImportItems7() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/importItems7.zip");
        manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("importItems7.zip not found");
        }
        resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();
        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());
        return importItems;
    }

    @Test
    @Ignore
    public void testDuplicateIdInSameImport() throws Exception {
        List<ImportItem> importItems = getImportItems7();
        manager.importItemRecords(new NullProgressMonitor(), resManager, importItems, false,
                importItems.toArray(new ImportItem[0]), new Path(""));

        String originalId = "_A7WlgP9BEeOBFtZnw6gdMg";
        ImportItem itemWithOrigId = null;
        String newId = null;
        ImportItem itemWithNewId = null;
        for (ImportItem importItem : importItems) {
            if (importItem.getItemId().equals(originalId)) {
                itemWithOrigId = importItem;
            } else {
                newId = importItem.getItemId();
                itemWithNewId = importItem;
            }
        }

        Assert.assertNotNull(itemWithOrigId);
        Assert.assertNotNull(newId);
        Assert.assertNotNull(itemWithNewId);
    }
}
