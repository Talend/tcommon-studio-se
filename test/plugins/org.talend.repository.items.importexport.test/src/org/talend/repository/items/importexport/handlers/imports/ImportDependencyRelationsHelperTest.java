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
package org.talend.repository.items.importexport.handlers.imports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.general.TalendNature;
import org.talend.core.model.properties.ItemRelation;
import org.talend.core.model.properties.ItemRelations;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.wizard.models.ItemImportNode;
import org.talend.repository.items.importexport.wizard.models.ProjectImportNode;

/**
 * DOC jding  class global comment. Detailled comment
 */
public class ImportDependencyRelationsHelperTest {

    private ImportDependencyRelationsHelper helperInstance;

    private static final String fakePath = ResourcesPlugin.getWorkspace().getRoot().getLocation() + "/temp";

    private static final String technicalLabel = Project.createTechnicalName("testBranchImport");

    private static final Path fakeProjectPath = new Path(fakePath + "/" + technicalLabel);

    private Project project;

    private List<Property> propertyList;

    private List<ItemImportNode> allImportItemNodesList;

    @Before
    public void setUpBeforeClass() throws Exception {
        helperInstance = ImportDependencyRelationsHelper.getInstance();
        createTempProject();
        allImportItemNodesList = new ArrayList<ItemImportNode>();
        propertyList = new ArrayList<Property>();
        ProjectImportNode projectNode = new ProjectImportNode(project.getEmfProject());
        for (int i = 0; i < 3; i++) {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(ProxyRepositoryFactory.getInstance().getNextId());
            property.setLabel("test" + i);
            property.setVersion("0.1");
            ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
            property.setItem(item);
            ImportItem itemRecord = new ImportItem(new Path(
                    fakePath + "/" + technicalLabel + "/process/" + property.getLabel() + "_" + property.getVersion() + ".item"));
            itemRecord.setProperty(property);
            ItemImportNode importNode = new ItemImportNode(itemRecord);
            projectNode.addChild(importNode);
            allImportItemNodesList.add(importNode);
            propertyList.add(property);
        }
        createRelations(propertyList);
        Property property2 = propertyList.get(2);
        Property property3 = PropertiesFactory.eINSTANCE.createProperty();
        property3.setId(property2.getId());
        property3.setLabel(property2.getLabel());
        property3.setVersion("0.7");
        ProcessItem item3 = PropertiesFactory.eINSTANCE.createProcessItem();
        property3.setItem(item3);
        ImportItem itemRecord = new ImportItem(new Path(
                fakePath + "/" + technicalLabel + "/process/" + property3.getLabel() + "_" + property3.getVersion() + ".item"));
        itemRecord.setProperty(property3);
        ItemImportNode importNode = new ItemImportNode(itemRecord);
        projectNode.addChild(importNode);
        allImportItemNodesList.add(importNode);
        propertyList.add(property3);
        ImportCacheHelper.getInstance().getPathWithProjects().put(fakeProjectPath, project.getEmfProject());
    }

    @Test
    public void testLoadRelations() {
        helperInstance.clear();
        helperInstance.loadRelations(fakeProjectPath, project.getEmfProject().getItemsRelations());
        Map importItemsRelations = helperInstance.getImportItemsRelations(fakeProjectPath);
        Assert.assertTrue(importItemsRelations.values().size() == 2);
    }

    @Test
    public void checkImportRelationDependency() {
        helperInstance.clear();
        helperInstance.loadRelations(fakeProjectPath, project.getEmfProject().getItemsRelations());
        Set<ItemImportNode> toSelectSet = new HashSet<ItemImportNode>();
        List<ItemImportNode> checkedNodeList = new ArrayList<ItemImportNode>();

        checkedNodeList.add(allImportItemNodesList.get(0));
        toSelectSet.add(allImportItemNodesList.get(0));
        helperInstance.checkImportRelationDependency(checkedNodeList, toSelectSet, allImportItemNodesList);
        Assert.assertTrue(toSelectSet.size() == 3);

        // to test loop dependency
        Map<Relation, Set<Relation>> importItemsRelations = helperInstance.getImportItemsRelations(fakeProjectPath);
        Property property3 = propertyList.get(3);
        Relation baseRelation = new Relation();
        baseRelation.setId(property3.getId());
        baseRelation.setType(RelationshipItemBuilder.JOB_RELATION);
        baseRelation.setVersion(property3.getVersion());
        Relation relatedRelation = new Relation();
        relatedRelation.setId(propertyList.get(0).getId());
        relatedRelation.setType(RelationshipItemBuilder.JOB_RELATION);
        relatedRelation.setVersion(RelationshipItemBuilder.LATEST_VERSION);
        Set<Relation> relationSet = new HashSet<Relation>();
        relationSet.add(relatedRelation);
        importItemsRelations.put(baseRelation, relationSet);
        toSelectSet.clear();
        toSelectSet.add(allImportItemNodesList.get(0));
        helperInstance.checkImportRelationDependency(checkedNodeList, toSelectSet, allImportItemNodesList);
        Assert.assertTrue(toSelectSet.size() == 3);
    }

    @Test
    public void testGetLatestVersionItemImportNode() {
        ItemImportNode latestVersionNode = helperInstance.getLatestVersionItemImportNode(propertyList.get(2).getId(),
                null, allImportItemNodesList);
        Property latestVersionProperty = latestVersionNode.getItemRecord().getProperty();
        Property property3 = propertyList.get(3);
        Assert.assertEquals(latestVersionProperty.getId(), property3.getId());
        Assert.assertEquals(latestVersionProperty.getVersion(), property3.getVersion());
    }

    private void createRelations(List<Property> propertyList) {
        Property property = propertyList.get(0);
        ItemRelations itemRelations = PropertiesFactoryImpl.eINSTANCE.createItemRelations();
        ItemRelation baseRelation = PropertiesFactoryImpl.eINSTANCE.createItemRelation();
        baseRelation.setId(property.getId());
        baseRelation.setType(RelationshipItemBuilder.JOB_RELATION);
        baseRelation.setVersion(property.getVersion());
        itemRelations.setBaseItem(baseRelation);

        Property property1 = propertyList.get(1);
        ItemRelation relatedRelation = PropertiesFactoryImpl.eINSTANCE.createItemRelation();
        relatedRelation.setId(property1.getId());
        relatedRelation.setType(RelationshipItemBuilder.JOB_RELATION);
        relatedRelation.setVersion(RelationshipItemBuilder.LATEST_VERSION);
        itemRelations.getRelatedItems().add(relatedRelation);
        project.getEmfProject().getItemsRelations().add(itemRelations);

        ItemRelations itemRelations1 = PropertiesFactoryImpl.eINSTANCE.createItemRelations();
        ItemRelation baseRelation1 = PropertiesFactoryImpl.eINSTANCE.createItemRelation();
        baseRelation1.setId(property1.getId());
        baseRelation1.setType(RelationshipItemBuilder.JOB_RELATION);
        baseRelation1.setVersion(property1.getVersion());
        itemRelations1.setBaseItem(baseRelation1);

        Property property2 = propertyList.get(2);
        ItemRelation relatedRelation1 = PropertiesFactoryImpl.eINSTANCE.createItemRelation();
        relatedRelation1.setId(property2.getId());
        relatedRelation1.setType(RelationshipItemBuilder.JOB_RELATION);
        relatedRelation1.setVersion(RelationshipItemBuilder.LATEST_VERSION);
        itemRelations1.getRelatedItems().add(relatedRelation1);
        project.getEmfProject().getItemsRelations().add(itemRelations1);

    }

    private void createTempProject() throws Exception {
        project = new Project();
        project.setLabel("testBranchImport");
        project.setDescription("no desc");
        project.setLanguage(ECodeLanguage.JAVA);
        User user = PropertiesFactory.eINSTANCE.createUser();
        user.setLogin("testauto@talend.com");
        project.setAuthor(user);
        project.setLocal(true);
        project.setTechnicalLabel(technicalLabel);

        // prepare all node
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject prj = root.getProject(technicalLabel);
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();

        try {
            IProjectDescription desc = null;
            if (prj.exists()) {
                prj.delete(true, null); // always delete to avoid conflicts between 2 tests
            }
            desc = workspace.newProjectDescription(technicalLabel);
            desc.setNatureIds(new String[] { TalendNature.ID });
            desc.setComment(project.getDescription());

            prj.create(desc, null);
            prj.open(IResource.DEPTH_INFINITE, null);
            prj.setDefaultCharset("UTF-8", null);
        } catch (CoreException e) {
            throw new PersistenceException(e);
        }

        XmiResourceManager xmiResourceManager = new XmiResourceManager();
        Resource projectResource = xmiResourceManager.createProjectResource(prj);
        projectResource.getContents().add(project.getEmfProject());
        projectResource.getContents().add(project.getAuthor());
        xmiResourceManager.saveResource(projectResource);
    }

    @After
    public void tearDownAfterClass() throws PersistenceException, CoreException {
        ProjectManager.getInstance().getFolders(project.getEmfProject()).clear();
        final IProject prj = ResourceUtils.getProject(project);
        prj.delete(true, null);
        ImportCacheHelper.getInstance().getPathWithProjects().keySet().removeIf(key -> key.equals(fakeProjectPath));
    }

}
