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
package org.talend.core.repository.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.properties.ImplicitContextSettings;
import org.talend.core.model.properties.ItemRelation;
import org.talend.core.model.properties.ItemRelations;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.StatAndLogsSettings;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.recyclebin.RecycleBinManager;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.TalendFileFactoryImpl;
import org.talend.model.recyclebin.RecycleBin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectDataJsonProvider {

    public static int CONTENT_PROJECTSETTING = 1;

    public static int CONTENT_RELATIONSHIPS = 2;

    public static int CONTENT_RECYCLEBIN = 4;

    public static int CONTENT_ALL = 7;
    
    public static void saveProjectData(Project project) throws PersistenceException {
        saveProjectData(project, CONTENT_ALL);
    }

    public static void saveProjectData(Project project, int saveContent) throws PersistenceException {
        if ((saveContent & CONTENT_PROJECTSETTING) > 0) {
            saveProjectSettings(project);
        }
        if ((saveContent & CONTENT_RELATIONSHIPS) > 0) {
            saveRelationShips(project);
        }
        if ((saveContent & CONTENT_RECYCLEBIN) > 0) {
            RecycleBinManager.getInstance().saveRecycleBin(project);
        }
    }

    public static void loadProjectData(Project project, IContainer projectContainer, int loadContent)
            throws PersistenceException {
        if ((loadContent & CONTENT_PROJECTSETTING) > 0) {
            loadProjectSettings(project, projectContainer);
        }
        if ((loadContent & CONTENT_RELATIONSHIPS) > 0) {
            loadRelationShips(project, projectContainer);
        }
        if ((loadContent & CONTENT_RECYCLEBIN) > 0) {
            // Force reload from file
            RecycleBinManager.getInstance().clearCache(project);
            RecycleBin recycleBin = RecycleBinManager.getInstance().getRecycleBin(project);
            for (int i = 0; i < recycleBin.getDeletedFolders().size(); i++) {
                String folder = (String) recycleBin.getDeletedFolders().get(i);
                if (!project.getDeletedFolders().contains(folder)) {
                    project.getDeletedFolders().add(folder);
                }
            }
            // TODO --KK, After migration task finished, should open this
            // List<String> removedList = new ArrayList<String>();
            // for (int i = 0; i < project.getDeletedFolders().size(); i++) {
            // String folder = (String) project.getDeletedFolders().get(i);
            // if (!recycleBin.getDeletedFolders().contains(folder)) {
            // removedList.add(folder);
            // }
            // }
            // project.getDeletedFolders().removeAll(removedList);
        }
    }

    private static void saveProjectSettings(Project project) throws PersistenceException {
        ProjectSettings projectSetting = new ProjectSettings();
        projectSetting.setImplicitContextSettingJson(getImplicitContextSettingJson(project.getImplicitContextSettings()));
        projectSetting.setStatAndLogsSettingJson(getStatAndLogsSettingJson(project.getStatAndLogsSettings()));
        projectSetting.setTechnicalStatus(getTechnicalStatusJson(project.getTechnicalStatus()));
        projectSetting.setDocumentationStatus(getDocumentationJson(project.getDocumentationStatus()));
        File file = getSavingConfigurationFile(project.getTechnicalLabel(), FileConstants.PROJECTSETTING_FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, projectSetting);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    private static void loadProjectSettings(Project project, IContainer projectContainer) throws PersistenceException {
        try {
            File file = getLoadingConfigurationFile(projectContainer, FileConstants.PROJECTSETTING_FILE_NAME);
            ProjectSettings projectSetting = null;
            if (file != null && file.exists()) {
                projectSetting = new ObjectMapper().readValue(file, ProjectSettings.class);
            }
            if (projectSetting != null) {
                project.setImplicitContextSettings(getImplicitContextSettings(projectSetting.getImplicitContextSettingJson()));
                project.setStatAndLogsSettings(getStatAndLogsSettings(projectSetting.getStatAndLogsSettingJson()));
                loadTechnicalStatus(projectSetting.getTechnicalStatus(), project);
                loadDocumentationStatus(projectSetting.getDocumentationStatus(), project);
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    private static void saveRelationShips(Project project) throws PersistenceException {
        File file = getSavingConfigurationFile(project.getTechnicalLabel(), FileConstants.RELATIONSHIPS_FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, project.getItemsRelations());
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    private static void loadRelationShips(Project project, IContainer projectContainer) throws PersistenceException {
        TypeReference<List<ItemRelationsJson>> typeReference = new TypeReference<List<ItemRelationsJson>>() {
            // no need to overwrite
        };
        try {
            File file = getLoadingConfigurationFile(projectContainer, FileConstants.RELATIONSHIPS_FILE_NAME);
            List<ItemRelationsJson> itemRelationsJsons = null;
            if (file != null && file.exists()) {
                itemRelationsJsons = new ObjectMapper().readValue(file, typeReference);
            }
            if (itemRelationsJsons != null && itemRelationsJsons.size() > 0) {
                for (ItemRelationsJson json : itemRelationsJsons) {
                    project.getItemsRelations().add(json.toEmfObject());
                }
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    private static File getSavingConfigurationFile(String technicalLabel, String fileName) throws PersistenceException {
        IProject iProject = ResourceUtils.getProject(technicalLabel);
        IFolder folder = iProject.getFolder(FileConstants.CONFIGURATION_FOLDER_NAME);
        if (!folder.exists()) {
            ResourceUtils.createFolder(folder);
        }
        IFile file = folder.getFile(fileName);
        return new File(file.getLocationURI());
    }

    private static File getLoadingConfigurationFile(IContainer projectContainer, String fileName) throws PersistenceException {
        if (projectContainer != null) {
            if (FileConstants.PROJECTSETTING_FILE_NAME.equals(fileName)
                    || FileConstants.RELATIONSHIPS_FILE_NAME.equals(fileName)) {
                IFolder folder = projectContainer.getFolder(new Path(FileConstants.CONFIGURATION_FOLDER_NAME));
                if (folder != null) {
                    IFile file = folder.getFile(fileName);
                    if (file != null) {
                        return new File(file.getLocationURI());
                    }
                }
            }
        }
        return null;
    }

    protected static ImplicitContextSettingJson getImplicitContextSettingJson(ImplicitContextSettings implicitContextSettings) {
        if (implicitContextSettings != null) {
            ImplicitContextSettingJson implicitContextSettingJson = new ImplicitContextSettingJson(implicitContextSettings);
            return implicitContextSettingJson;
        }
        return null;
    }

    public static ImplicitContextSettings getImplicitContextSettings(ImplicitContextSettingJson implicitContextSettingJson) {
        if (implicitContextSettingJson != null) {
            return implicitContextSettingJson.toEmfObject();
        }
        return null;
    }

    protected static StatAndLogsSettingJson getStatAndLogsSettingJson(StatAndLogsSettings statAndLogsSettings) {
        if (statAndLogsSettings != null) {
            StatAndLogsSettingJson statAndLogsSettingJson = new StatAndLogsSettingJson(statAndLogsSettings);
            return statAndLogsSettingJson;
        }
        return null;
    }

    protected static StatAndLogsSettings getStatAndLogsSettings(StatAndLogsSettingJson statAndLogsSettingJson) {
        if (statAndLogsSettingJson != null) {
            return statAndLogsSettingJson.toEmfObject();
        }
        return null;
    }

    protected static List<StatusJson> getTechnicalStatusJson(EList technicalStatus) {
        if (technicalStatus != null && technicalStatus.size() > 0) {
            List<StatusJson> list = new ArrayList<StatusJson>(technicalStatus.size());
            for (int i = 0; i < technicalStatus.size(); i++) {
                StatusJson json = new StatusJson((Status) technicalStatus.get(i));
                list.add(json);
            }
            return list;
        }
        return null;
    }

    protected static void loadTechnicalStatus(List<StatusJson> statusList, Project project) {
        project.getTechnicalStatus().clear();
        if (statusList != null && statusList.size() > 0) {
            for (StatusJson json : statusList) {
                project.getTechnicalStatus().add(json.toEmfObject());
            }
        }
    }

    protected static List<StatusJson> getDocumentationJson(EList documentationStatus) {
        if (documentationStatus != null && documentationStatus.size() > 0) {
            List<StatusJson> list = new ArrayList<StatusJson>(documentationStatus.size());
            for (int i = 0; i < documentationStatus.size(); i++) {
                StatusJson json = new StatusJson((Status) documentationStatus.get(i));
                list.add(json);
            }
            return list;
        }
        return null;
    }

    protected static void loadDocumentationStatus(List<StatusJson> statusList, Project project) {
        project.getDocumentationStatus().clear();
        if (statusList != null && statusList.size() > 0) {
            for (StatusJson json : statusList) {
                project.getDocumentationStatus().add(json.toEmfObject());
            }
        }
    }

    protected static List<ItemRelationsJson> getItemRelationsJson(EList itemsRelations) {
        if (itemsRelations.size() > 0) {
            List<ItemRelationsJson> list = new ArrayList<ItemRelationsJson>(itemsRelations.size());
            for (int i = 0; i < itemsRelations.size(); i++) {
                ItemRelations relations = (ItemRelations) itemsRelations.get(i);
                ItemRelationsJson json = new ItemRelationsJson(relations);
                list.add(json);
            }
            return list;
        }
        return null;
    }

    protected static void loadItemRelations(List<ItemRelationsJson> itemRelationsList, Project project) {
        project.getItemsRelations().clear();
        if (itemRelationsList != null && itemRelationsList.size() > 0) {
            for (ItemRelationsJson json : itemRelationsList) {
                project.getItemsRelations().add(json.toEmfObject());
            }
        }
    }
}

@JsonInclude(Include.NON_NULL)
class ProjectSettings {

    @JsonProperty("technicalStatus")
    private List<StatusJson> technicalStatus;

    @JsonProperty("documentationStatus")
    private List<StatusJson> documentationStatus;

    @JsonProperty("statAndLogsSettings")
    private StatAndLogsSettingJson statAndLogsSettingJson;

    @JsonProperty("implicitContextSettings")
    private ImplicitContextSettingJson implicitContextSettingJson;

    public ImplicitContextSettingJson getImplicitContextSettingJson() {
        return implicitContextSettingJson;
    }

    public void setImplicitContextSettingJson(ImplicitContextSettingJson implicitContextSettingJson) {
        this.implicitContextSettingJson = implicitContextSettingJson;
    }

    public StatAndLogsSettingJson getStatAndLogsSettingJson() {
        return statAndLogsSettingJson;
    }

    public void setStatAndLogsSettingJson(StatAndLogsSettingJson statAndLogsSettingJson) {
        this.statAndLogsSettingJson = statAndLogsSettingJson;
    }

    public List<StatusJson> getTechnicalStatus() {
        return technicalStatus;
    }

    public void setTechnicalStatus(List<StatusJson> technicalStatus) {
        this.technicalStatus = technicalStatus;
    }

    public List<StatusJson> getDocumentationStatus() {
        return documentationStatus;
    }

    public void setDocumentationStatus(List<StatusJson> documentationStatus) {
        this.documentationStatus = documentationStatus;
    }
}

@JsonInclude(Include.NON_NULL)
class ImplicitContextSettingJson {

    @JsonProperty("parameters")
    private ParametersTypeJson parametersTypeJson;

    public ImplicitContextSettingJson() {
    }

    public ImplicitContextSettingJson(ImplicitContextSettings implicitContextSettings) {
        ParametersType parametersType = implicitContextSettings.getParameters();
        if (parametersType != null) {
            parametersTypeJson = new ParametersTypeJson(parametersType);
        }
    }

    public ParametersTypeJson getParametersTypeJson() {
        return parametersTypeJson;
    }

    public void setParametersTypeJson(ParametersTypeJson parametersTypeJson) {
        this.parametersTypeJson = parametersTypeJson;
    }

    public ImplicitContextSettings toEmfObject() {
        ImplicitContextSettings implicitContextSettings = PropertiesFactoryImpl.eINSTANCE.createImplicitContextSettings();
        if (parametersTypeJson != null) {
            ParametersType parameterType = parametersTypeJson.toEmfObject();
            implicitContextSettings.setParameters(parameterType);
        }
        return implicitContextSettings;
    }
}

@JsonInclude(Include.NON_NULL)
class ParametersTypeJson {

    @JsonProperty("elementParameter")
    private List<ElementParameterTypeJson> elementParameters;

    @JsonProperty("routinesParameters")
    private List<RoutinesParameterTypeJson> routinesParameters;

    public ParametersTypeJson() {
    }

    public ParametersTypeJson(ParametersType parametersType) {
        if (parametersType.getElementParameter().size() > 0) {
            elementParameters = new ArrayList<ElementParameterTypeJson>();
            for (int i = 0; i < parametersType.getElementParameter().size(); i++) {
                ElementParameterType type = (ElementParameterType) parametersType.getElementParameter().get(i);
                ElementParameterTypeJson typeJson = new ElementParameterTypeJson(type);
                elementParameters.add(typeJson);
            }
        }
        if (parametersType.getRoutinesParameter().size() > 0) {
            routinesParameters = new ArrayList<RoutinesParameterTypeJson>();
            for (int i = 0; i < parametersType.getRoutinesParameter().size(); i++) {
                RoutinesParameterType type = (RoutinesParameterType) parametersType.getRoutinesParameter().get(i);
                RoutinesParameterTypeJson typeJson = new RoutinesParameterTypeJson(type);
                routinesParameters.add(typeJson);
            }
        }
    }

    public List<ElementParameterTypeJson> getElementParameters() {
        return elementParameters;
    }

    public void setElementParameters(List<ElementParameterTypeJson> elementParameters) {
        this.elementParameters = elementParameters;
    }

    public List<RoutinesParameterTypeJson> getRoutinesParameters() {
        return routinesParameters;
    }

    public void setRoutinesParameters(List<RoutinesParameterTypeJson> routinesParameters) {
        this.routinesParameters = routinesParameters;
    }

    public ParametersType toEmfObject() {
        ParametersType parametersType = TalendFileFactoryImpl.eINSTANCE.createParametersType();
        if (elementParameters != null && elementParameters.size() > 0) {
            for (ElementParameterTypeJson json : elementParameters) {
                ElementParameterType type = json.toEmfObject();
                parametersType.getElementParameter().add(type);
            }
        }
        if (routinesParameters != null && routinesParameters.size() > 0) {
            for (RoutinesParameterTypeJson json : routinesParameters) {
                RoutinesParameterType type = json.toEmfObject();
                parametersType.getRoutinesParameter().add(type);
            }
        }
        return parametersType;
    }
}

@JsonInclude(Include.NON_NULL)
class ElementParameterTypeJson {

    @JsonProperty("field")
    private String field;

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    @JsonProperty("contextMode")
    private boolean contextMode;

    @JsonInclude(Include.NON_DEFAULT)
    @JsonProperty("show")
    private boolean isShow;

    @JsonProperty("elementValue")
    List<ElementValueTypeJson> elementValues;

    public ElementParameterTypeJson() {
    }

    public ElementParameterTypeJson(ElementParameterType type) {
        this.field = type.getField();
        this.name = type.getName();
        this.value = type.getValue();
        this.contextMode = type.isContextMode();
        this.isShow = type.isShow();
        if (type.getElementValue().size() > 0) {
            elementValues = new ArrayList<ElementValueTypeJson>();
            for (int i = 0; i < type.getElementValue().size(); i++) {
                ElementValueType value = (ElementValueType) type.getElementValue().get(i);
                ElementValueTypeJson valueJson = new ElementValueTypeJson(value);
                elementValues.add(valueJson);
            }
        }
    }

    public List<ElementValueTypeJson> getElementValues() {
        return elementValues;
    }

    public void setElementValues(List<ElementValueTypeJson> elementValues) {
        this.elementValues = elementValues;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isContextMode() {
        return contextMode;
    }

    public void setContextMode(boolean contextMode) {
        this.contextMode = contextMode;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public ElementParameterType toEmfObject() {
        ElementParameterType type = TalendFileFactoryImpl.eINSTANCE.createElementParameterType();
        type.setContextMode(this.isContextMode());
        type.setField(this.getField());
        type.setName(this.getName());
        type.setShow(this.isShow());
        type.setValue(this.getValue());
        if (elementValues != null && elementValues.size() > 0) {
            for (ElementValueTypeJson valueJson : elementValues) {
                ElementValueType value = valueJson.toEmfObject();
                type.getElementValue().add(value);
            }
        }
        return type;
    }
}

@JsonInclude(Include.NON_NULL)
class RoutinesParameterTypeJson {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    public RoutinesParameterTypeJson() {
    }

    public RoutinesParameterTypeJson(RoutinesParameterType type) {
        this.id = type.getId();
        this.name = type.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoutinesParameterType toEmfObject() {
        RoutinesParameterType type = TalendFileFactoryImpl.eINSTANCE.createRoutinesParameterType();
        type.setId(this.getId());
        type.setName(this.getName());
        return type;
    }
}

@JsonInclude(Include.NON_NULL)
class ElementValueTypeJson {

    @JsonProperty("elementRef")
    private String elementRef;

    @JsonProperty("value")
    private String value;

    @JsonProperty("type")
    private String type;

    @JsonProperty("isHexValue")
    private Boolean isHexValue;

    public ElementValueTypeJson() {
    }

    public ElementValueTypeJson(ElementValueType elementValueType) {
        this.setElementRef(elementValueType.getElementRef());
        this.setIsHexValue(elementValueType.isHexValue());
        this.setValue(elementValueType.getValue());
        this.setType(elementValueType.getType());
    }

    public String getElementRef() {
        return elementRef;
    }

    public void setElementRef(String elementRef) {
        this.elementRef = elementRef;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsHexValue() {
        return isHexValue;
    }

    public void setIsHexValue(Boolean isHexValue) {
        this.isHexValue = isHexValue;
    }

    public ElementValueType toEmfObject() {
        ElementValueType value = TalendFileFactoryImpl.eINSTANCE.createElementValueType();
        value.setElementRef(this.getElementRef());
        value.setHexValue(this.getIsHexValue());
        value.setType(this.getType());
        value.setValue(this.getValue());
        return value;
    }
}

@JsonInclude(Include.NON_NULL)
class StatAndLogsSettingJson {

    @JsonProperty("parameters")
    private ParametersTypeJson parametersTypeJosn;

    public StatAndLogsSettingJson() {
    }

    public StatAndLogsSettingJson(StatAndLogsSettings statAndLogsSettings) {
        if (statAndLogsSettings.getParameters() != null) {
            parametersTypeJosn = new ParametersTypeJson(statAndLogsSettings.getParameters());
        }
    }

    public ParametersTypeJson getParametersTypeJosn() {
        return parametersTypeJosn;
    }

    public void setParametersTypeJosn(ParametersTypeJson parametersTypeJosn) {
        this.parametersTypeJosn = parametersTypeJosn;
    }

    public StatAndLogsSettings toEmfObject() {
        StatAndLogsSettings statAndLogsSettings = PropertiesFactoryImpl.eINSTANCE.createStatAndLogsSettings();
        if (parametersTypeJosn != null) {
            statAndLogsSettings.setParameters(parametersTypeJosn.toEmfObject());
        }
        return statAndLogsSettings;
    }
}

@JsonInclude(Include.NON_NULL)
class ItemRelationsJson {

    @JsonProperty("baseItem")
    private ItemRelationJson baseItem;

    @JsonProperty("relatedItems")
    private List<ItemRelationJson> relatedItems;

    public ItemRelationsJson() {
    }

    public ItemRelationsJson(ItemRelations relations) {
        if (relations.getBaseItem() != null) {
            baseItem = new ItemRelationJson(relations.getBaseItem());
        }
        if (relations.getRelatedItems().size() > 0) {
            relatedItems = new ArrayList<ItemRelationJson>(relations.getRelatedItems().size());
            for (int i = 0; i < relations.getRelatedItems().size(); i++) {
                relatedItems.add(new ItemRelationJson((ItemRelation) relations.getRelatedItems().get(i)));
            }
        }
    }

    public ItemRelationJson getBaseItem() {
        return baseItem;
    }

    public void setBaseItem(ItemRelationJson baseItem) {
        this.baseItem = baseItem;
    }

    public List<ItemRelationJson> getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(List<ItemRelationJson> relatedItems) {
        this.relatedItems = relatedItems;
    }

    public ItemRelations toEmfObject() {
        ItemRelations itemRelations = PropertiesFactoryImpl.eINSTANCE.createItemRelations();
        if (baseItem != null) {
            itemRelations.setBaseItem(baseItem.toEmfObject());
        }
        if (relatedItems != null && relatedItems.size() > 0) {
            for (ItemRelationJson json : relatedItems) {
                itemRelations.getRelatedItems().add(json.toEmfObject());
            }
        }
        return itemRelations;
    }
}

@JsonInclude(Include.NON_NULL)
class ItemRelationJson {

    @JsonProperty("id")
    private String id;

    @JsonProperty("version")
    private String version;

    @JsonProperty("type")
    private String type;

    public ItemRelationJson() {
    }

    public ItemRelationJson(ItemRelation itemRelation) {
        this.id = itemRelation.getId();
        this.type = itemRelation.getType();
        this.version = itemRelation.getVersion();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ItemRelation toEmfObject() {
        ItemRelation itemRelation = PropertiesFactoryImpl.eINSTANCE.createItemRelation();
        itemRelation.setId(this.id);
        itemRelation.setType(this.type);
        itemRelation.setVersion(this.version);
        return itemRelation;
    }
}

@JsonInclude(Include.NON_NULL)
class StatusJson {

    @JsonProperty("label")
    private String label;

    @JsonProperty("code")
    private String code;

    public StatusJson() {
    }

    public StatusJson(Status status) {
        this.label = status.getLabel();
        this.code = status.getCode();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Status toEmfObject() {
        Status status = PropertiesFactoryImpl.eINSTANCE.createStatus();
        status.setLabel(this.label);
        status.setCode(this.code);
        return status;
    }
}
