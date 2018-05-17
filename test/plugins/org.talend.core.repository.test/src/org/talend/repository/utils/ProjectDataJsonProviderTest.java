package org.talend.repository.utils;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.general.TalendNature;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.core.model.properties.ItemRelation;
import org.talend.core.model.properties.ItemRelations;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.StatAndLogsSettings;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.TalendFileFactoryImpl;
import org.talend.repository.ProjectManager;
import org.talend.repository.localprovider.model.LocalRepositoryFactory;

public class ProjectDataJsonProviderTest {

    private Project sampleProject;

    private int elementParameterTypeCount = 10;

    private int routinesParameterTypeCount = 10;

    private int elementValueTypeCount = 6;

    private int technicalStatusCount = 5;

    private int documentationStatusCount = 4;

    private int itemRelationsCount = 10;

    private int itemRelationCount = 8;

    @Before
    public void beforeTest() throws PersistenceException, CoreException {
        createTempProject();
        prepareProjectData();
    }

    @After
    public void afterTest() throws Exception {
        removeTempProject();
    }

    @Test
    public void testSaveProjectData() throws Exception {
        LocalRepositoryFactory localRepositoryFactory = new LocalRepositoryFactory();
        localRepositoryFactory.saveProject(sampleProject);

        checkResult(sampleProject);
    }

    @Test
    public void testLoadProjectData() throws Exception {
        LocalRepositoryFactory localRepositoryFactory = new LocalRepositoryFactory();
        localRepositoryFactory.saveProject(sampleProject);

        IProject physProject = ResourceUtils.getProject(sampleProject);
        XmiResourceManager xrm = new XmiResourceManager();
        org.talend.core.model.properties.Project emfProject = xrm.loadProject(physProject);
        sampleProject = new Project(emfProject);

        checkResult(sampleProject);
    }

    private void prepareProjectData() {
        ImplicitContextSettings implicitContextSettings = createImplicitContextSettingObject();
        sampleProject.getEmfProject().setImplicitContextSettings(implicitContextSettings);

        StatAndLogsSettings statAndLogsSettings = createStatAndLogsSettingObject();
        sampleProject.getEmfProject().setStatAndLogsSettings(statAndLogsSettings);

        fillTechnicalStatus(sampleProject);
        fillDocumentationStatus(sampleProject);
        fillItemRelations(sampleProject);
    }

    private void checkResult(Project project) {
        ImplicitContextSettings implicitContextSettings = sampleProject.getEmfProject().getImplicitContextSettings();
        assertNotNull(implicitContextSettings);
        checkParametersType(implicitContextSettings.getParameters());

        StatAndLogsSettings statAndLogsSettings = sampleProject.getEmfProject().getStatAndLogsSettings();
        assertNotNull(statAndLogsSettings);
        checkParametersType(statAndLogsSettings.getParameters());

        EList technicalStatusList = sampleProject.getEmfProject().getTechnicalStatus();
        assertEquals(technicalStatusCount, technicalStatusList.size());
        for (int i = 0; i < technicalStatusList.size(); i++) {
            Status status = (Status) technicalStatusList.get(i);
            assertEquals(status.getCode(), "code" + i);
            assertEquals(status.getLabel(), "label" + i);
        }

        EList documentationStatusList = sampleProject.getEmfProject().getDocumentationStatus();
        assertEquals(documentationStatusCount, documentationStatusList.size());
        for (int i = 0; i < documentationStatusList.size(); i++) {
            Status status = (Status) documentationStatusList.get(i);
            assertEquals(status.getCode(), "code" + i);
            assertEquals(status.getLabel(), "label" + i);
        }
        EList itemRelationsList = sampleProject.getEmfProject().getItemsRelations();
        assertEquals(itemRelationsCount, itemRelationsList.size());
        for (int i = 0; i < itemRelationsCount; i++) {
            ItemRelations itemRelations = (ItemRelations) itemRelationsList.get(i);
            ItemRelation baseItem = itemRelations.getBaseItem();
            assertEquals(baseItem.getId(), "base_id" + i);
            assertEquals(baseItem.getType(), "base_type" + i);
            assertEquals(baseItem.getVersion(), "base_version" + i);
            EList itemRelationList = itemRelations.getRelatedItems();
            assertEquals(itemRelationCount, itemRelationList.size());
            for (int j = 0; j < itemRelationList.size(); j++) {
                ItemRelation item = (ItemRelation) itemRelationList.get(j);
                assertEquals(item.getId(), "id" + j);
                assertEquals(item.getType(), "type" + j);
                assertEquals(item.getVersion(), "version" + j);
            }
        }
    }

    private void checkParametersType(ParametersType parametersType) {
        assertNotNull(parametersType);
        EList elementParameterList = parametersType.getElementParameter();
        assertEquals(elementParameterTypeCount, elementParameterList.size());
        for (int i = 0; i < elementParameterList.size(); i++) {
            ElementParameterType type = (ElementParameterType) elementParameterList.get(i);
            assertEquals(type.isContextMode(), i % 2 == 0);
            assertEquals(type.getField(), "field" + i);
            assertEquals(type.getName(), "name" + i);
            assertEquals(type.getValue(), "value" + i);
            assertEquals(type.isShow(), i % 2 != 0);

            EList elementValueList = type.getElementValue();
            assertEquals(elementValueTypeCount, elementValueList.size());
            for (int j = 0; j < elementValueList.size(); j++) {
                ElementValueType valueType = (ElementValueType) elementValueList.get(j);
                assertEquals(valueType.getElementRef(), "ref" + j);
                assertEquals(valueType.isHexValue(), j % 2 == 0);
                assertEquals(valueType.getType(), "type" + j);
                assertEquals(valueType.getValue(), "value" + j);
            }
        }
    }

    private ImplicitContextSettings createImplicitContextSettingObject() {
        ImplicitContextSettings implicitContextSettings = PropertiesFactoryImpl.eINSTANCE.createImplicitContextSettings();
        ParametersType parametersType = TalendFileFactoryImpl.eINSTANCE.createParametersType();
        implicitContextSettings.setParameters(parametersType);
        fillParametersType(parametersType);
        return implicitContextSettings;
    }

    private StatAndLogsSettings createStatAndLogsSettingObject() {
        StatAndLogsSettings settings = PropertiesFactoryImpl.eINSTANCE.createStatAndLogsSettings();
        ParametersType parametersType = TalendFileFactoryImpl.eINSTANCE.createParametersType();
        settings.setParameters(parametersType);
        fillParametersType(parametersType);
        return settings;
    }

    private void fillParametersType(ParametersType parametersType) {
        for (int i = 0; i < elementParameterTypeCount; i++) {
            ElementParameterType type = TalendFileFactoryImpl.eINSTANCE.createElementParameterType();
            type.setContextMode(i % 2 == 0);
            type.setField("field" + i);
            type.setName("name" + i);
            type.setValue("value" + i);
            type.setShow(i % 2 != 0);

            for (int j = 0; j < elementValueTypeCount; j++) {
                ElementValueType value = TalendFileFactoryImpl.eINSTANCE.createElementValueType();
                value.setElementRef("ref" + j);
                value.setHexValue(j % 2 == 0);
                value.setType("type" + j);
                value.setValue("value" + j);

                type.getElementValue().add(value);
            }
            parametersType.getElementParameter().add(type);
        }

        for (int i = 0; i < routinesParameterTypeCount; i++) {
            RoutinesParameterType type = TalendFileFactoryImpl.eINSTANCE.createRoutinesParameterType();
            type.setId("id" + i);
            type.setName("name" + i);
            parametersType.getRoutinesParameter().add(type);
        }
    }

    private void fillTechnicalStatus(Project project) {
        for (int i = 0; i < technicalStatusCount; i++) {
            Status status = PropertiesFactoryImpl.eINSTANCE.createStatus();
            status.setCode("code" + i);
            status.setLabel("label" + i);
            project.getEmfProject().getTechnicalStatus().add(status);
        }
    }

    private void fillDocumentationStatus(Project project) {
        for (int i = 0; i < documentationStatusCount; i++) {
            Status status = PropertiesFactoryImpl.eINSTANCE.createStatus();
            status.setCode("code" + i);
            status.setLabel("label" + i);
            project.getEmfProject().getDocumentationStatus().add(status);
        }
    }

    private void fillItemRelations(Project project) {
        for (int i = 0; i < itemRelationsCount; i++) {
            ItemRelations itemRelations = PropertiesFactoryImpl.eINSTANCE.createItemRelations();
            ItemRelation baseItem = PropertiesFactoryImpl.eINSTANCE.createItemRelation();
            baseItem.setId("base_id" + i);
            baseItem.setType("base_type" + i);
            baseItem.setVersion("base_version" + i);
            itemRelations.setBaseItem(baseItem);
            for (int j = 0; j < itemRelationCount; j++) {
                ItemRelation item = PropertiesFactoryImpl.eINSTANCE.createItemRelation();
                item.setId("id" + j);
                item.setType("type" + j);
                item.setVersion("version" + j);
                itemRelations.getRelatedItems().add(item);
            }
            project.getEmfProject().getItemsRelations().add(itemRelations);
        }
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
}
