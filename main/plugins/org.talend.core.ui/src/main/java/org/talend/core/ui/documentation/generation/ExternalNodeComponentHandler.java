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
package org.talend.core.ui.documentation.generation;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.ParameterValueUtil;
import org.talend.designer.core.IDesignerCoreService;

/**
 * This class is external node component handler for generating HTML.
 * 
 * @author ftang
 * 
 */
public class ExternalNodeComponentHandler extends AbstractComponentHandler {

    private Map<String, List> targetConnectionMap = null;

    private Map<String, List> sourceConnectionMap = null;

    private Map<String, String> picFilePathMap;

    private List<Map> mapList;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, String> repositoryDBIdAndNameMap;

    private IDesignerCoreService designerCoreService;

    private Element externalNodeElement;

    private List<INode> componentsList;

    private Map<String, Object> externalNodeHTMLMap;

    // private String tempFolderPath;

    /**
     * Contructor.
     * 
     * @param picFilePathMap
     * @param externalNodeElement
     * @param allComponentsList
     * @param sourceConnectionMap
     * @param targetConnectionMap
     * @param designerCoreService
     * @param repositoryConnectionItemMap
     * @param repositoryDBIdAndNameMap
     * @param externalNodeHTMLList
     */
    public ExternalNodeComponentHandler(Map<String, String> picFilePathMap, Element externalNodeElement,
            List<INode> allComponentsList, Map<String, List> sourceConnectionMap, Map<String, List> targetConnectionMap,
            IDesignerCoreService designerCoreService, Map<String, ConnectionItem> repositoryConnectionItemMap,
            Map<String, String> repositoryDBIdAndNameMap, Map<String, Object> externalNodeHTMLMap/*
                                                                                                  * , String
                                                                                                  * tempFolderPath
                                                                                                  */) {
        this.picFilePathMap = picFilePathMap;
        this.externalNodeElement = externalNodeElement;
        this.componentsList = allComponentsList;
        this.sourceConnectionMap = sourceConnectionMap;
        this.targetConnectionMap = targetConnectionMap;
        this.designerCoreService = designerCoreService;
        this.repositoryConnectionItemMap = repositoryConnectionItemMap;
        this.repositoryDBIdAndNameMap = repositoryDBIdAndNameMap;
        this.externalNodeHTMLMap = externalNodeHTMLMap;
        // this.tempFolderPath = tempFolderPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.genHTMLDoc.IComponentHandler#generateComponentInfo(org.dom4j.Element,
     * java.util.List)
     */
    @Override
    public void generateComponentInfo() {

        for (INode externalNode : this.componentsList) {
            Element componentElement = generateComponentDetailsInfo(true, externalNode, this.externalNodeElement,
                    this.picFilePathMap, this.sourceConnectionMap, this.targetConnectionMap, this.repositoryDBIdAndNameMap);

            String componentName = externalNode.getUniqueName();
            IComponentDocumentation componentDocumentation = externalNode.getExternalNode().getComponentDocumentation(
                    componentName, HTMLDocUtils.getTmpFolder() /* checkExternalPathIsExists(tempFolderPath) */);

            // Checks if generating html file for external node failed, generating the same information as internal node
            // instead.
            if (componentDocumentation == null) {
                Element parametersElement = componentElement.addElement("parameters"); //$NON-NLS-1$
                List elementParameterList = externalNode.getElementParameters();
                // generateElementParamInfo(parametersElement, elementParameterList);
                // see 3328, document for scd is generated similar to internal node
                generateComponentSchemaInfo(externalNode, componentElement);
                generateComponentElementParamInfo(parametersElement, elementParameterList);
            } else {
                URL fileURL = componentDocumentation.getHTMLFile();
                if (fileURL != null) {
                    this.externalNodeHTMLMap.put(componentName, fileURL);
                }
            }
            componentElement.addComment(componentName);
        }
    }

    /**
     * Generates parameter element information only for component external node component.
     * 
     * @param parametersElement
     * @param elementParameterList
     */
    private void generateElementParamInfo(Element parametersElement, List elementParameterList) {
        if (elementParameterList != null && elementParameterList.size() != 0) {
            Element parameterElement = null;
            for (int j = 0; j < elementParameterList.size(); j++) {
                IElementParameter type = (IElementParameter) elementParameterList.get(j);
                if (type.getName().equals(IHTMLDocConstants.REPOSITORY_COMMENT)) {
                    parameterElement = parametersElement.addElement("parameter"); //$NON-NLS-1$
                    Element columnElement = parameterElement.addElement("column"); //$NON-NLS-1$
                    columnElement.addAttribute("name", IHTMLDocConstants.DISPLAY_COMMENT); //$NON-NLS-1$
                    columnElement.setText(type.getValue().toString());
                    break;
                }
            }
        }
    }

    /**
     * Checks if external node component directory is existing.
     * 
     * @param resource
     * @return
     */
    private String checkExternalPathIsExists(String tempFolderPath) {
        String tempExternalPath = tempFolderPath + File.separator + IHTMLDocConstants.EXTERNAL_FOLDER_NAME;
        File file = new File(tempExternalPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return tempExternalPath;
    }

    /**
     * Generates the element parameters information of component.
     * 
     * @param parametersElement
     * @param elementParameterList
     */
    private void generateComponentElementParamInfo(Element parametersElement, List elementParameterList) {
        List<IElementParameter> copyElementParameterList = new ArrayList(elementParameterList);

        if (elementParameterList != null && elementParameterList.size() != 0) {
            for (int j = 0; j < elementParameterList.size(); j++) {
                IElementParameter elemparameter = (IElementParameter) elementParameterList.get(j);

                if ((!elemparameter.isShow(copyElementParameterList) && (!elemparameter.getName().equals(
                        EParameterFieldType.SCHEMA_TYPE.getName())))
                        || elemparameter.getCategory().equals(EComponentCategory.MAIN)
                        || elemparameter.getCategory().equals(EComponentCategory.VIEW)
                        || elemparameter.getName().equals(IHTMLDocConstants.REPOSITORY)
                        || elemparameter.getName().equals("SCHEMA") //$NON-NLS-1$
                        || elemparameter.getName().equals("QUERYSTORE") //$NON-NLS-1$
                        || elemparameter.getName().equals("PROPERTY") //$NON-NLS-1$
                        || elemparameter.getName().equals(EParameterFieldType.ENCODING_TYPE.getName())) {
                    continue;
                }
                // String value = HTMLDocUtils.checkString(elemparameter.getValue().toString());
                Object eleObj = elemparameter.getValue();
                String value = ""; //$NON-NLS-1$
                if (eleObj != null) {
                    value = eleObj.toString();
                }

                if (elemparameter.getName().equals(EParameterFieldType.PROPERTY_TYPE.getName())
                        && value.equals(IHTMLDocConstants.REPOSITORY)) {
                    String repositoryValueForPropertyType = getRepositoryValueForPropertyType(copyElementParameterList,
                            "REPOSITORY_PROPERTY_TYPE"); //$NON-NLS-1$
                    value = repositoryValueForPropertyType == null ? IHTMLDocConstants.REPOSITORY_BUILT_IN : value.toString()
                            .toLowerCase() + ": " + repositoryValueForPropertyType; //$NON-NLS-1$
                } else if (elemparameter.getName().equals(EParameterFieldType.SCHEMA_TYPE.getName())
                        && value.equals(IHTMLDocConstants.REPOSITORY)) {
                    String repositoryValueForSchemaType = getRepositoryValueForSchemaType(copyElementParameterList,
                            "REPOSITORY_SCHEMA_TYPE"); //$NON-NLS-1$
                    value = repositoryValueForSchemaType == null ? IHTMLDocConstants.REPOSITORY_BUILT_IN : value.toString()
                            .toLowerCase() + ": " + repositoryValueForSchemaType; //$NON-NLS-1$
                }

                else if (elemparameter.getName().equals(EParameterFieldType.QUERYSTORE_TYPE.getName())
                        && value.equals(IHTMLDocConstants.REPOSITORY)) {

                    String repositoryValueForQueryStoreType = getRepositoryValueForQueryStoreType(copyElementParameterList,
                            "REPOSITORY_QUERYSTORE_TYPE"); //$NON-NLS-1$
                    value = repositoryValueForQueryStoreType == null ? IHTMLDocConstants.REPOSITORY_BUILT_IN : value.toString()
                            .toLowerCase() + ": " + repositoryValueForQueryStoreType; //$NON-NLS-1$
                }
                // } else if (type.getName().equals("TYPE")) {
                // int index = type.getIndexOfItemFromList(type.getDisplayName());
                // value = checkString(type.getListItemsDisplayName()[index]);
                // }

                else if (elemparameter.getRepositoryValue() != null
                        && elemparameter.getRepositoryValue().toUpperCase().contains("PASSWORD") //$NON-NLS-1$
                        || elemparameter.getFieldType() == EParameterFieldType.PASSWORD) {
                    value = ParameterValueUtil.getValue4Doc(elemparameter).toString();
                }
                Element columnElement = parametersElement.addElement("column"); //$NON-NLS-1$
                columnElement.addAttribute("name", HTMLDocUtils.checkString(elemparameter.getDisplayName())); //$NON-NLS-1$

                if (value.equalsIgnoreCase(IHTMLDocConstants.REPOSITORY_BUILT_IN)) {
                    value = IHTMLDocConstants.DISPLAY_BUILT_IN;
                }
                columnElement.setText(value);
            }
        }
    }

    /**
     * Gets the repository value.
     * 
     * @param newList
     * @param repositoryName
     * @return
     */
    private String getRepositoryValueForPropertyType(List<IElementParameter> copyElementParameterList, String repositoryName) {
        String value = null;
        for (IElementParameter elemParameter : copyElementParameterList) {
            if (elemParameter.getName().equals(repositoryName)) {
                ConnectionItem connectionItem = repositoryConnectionItemMap.get(elemParameter.getValue());
                if (connectionItem != null) {
                    String aliasName = designerCoreService.getRepositoryAliasName(connectionItem);
                    value = aliasName + ":" + connectionItem.getProperty().getLabel(); //$NON-NLS-1$
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * Gets the repository value.
     * 
     * @param newList
     * @param repositoryName
     * @return
     */
    private String getRepositoryValueForSchemaType(List<IElementParameter> copyElementParameterList, String repositoryName) {
        String value = null;
        for (IElementParameter elemParameter : copyElementParameterList) {
            if (elemParameter.getName().equals(repositoryName)) {
                if (elemParameter.getValue() != null && elemParameter.getValue().toString().length() > 0) {
                    value = elemParameter.getValue().toString();
                    String newValue = value.substring(0, value.indexOf("-")).trim(); //$NON-NLS-1$
                    if (repositoryDBIdAndNameMap.containsKey(newValue)) {
                        value = value.replace(newValue, repositoryDBIdAndNameMap.get(newValue));
                        return value;
                    }
                }

            }
        }

        return null;
    }

    /**
     * Gets the repository value.
     * 
     * @param newList
     * @param repositoryName
     * @return
     */
    private String getRepositoryValueForQueryStoreType(List<IElementParameter> copyElementParameterList, String repositoryName) {
        String value = null;
        for (IElementParameter elemParameter : copyElementParameterList) {
            if (elemParameter.getName().equals(repositoryName)) {
                if (elemParameter.getValue() != null && elemParameter.getValue().toString().length() > 0) {
                    value = elemParameter.getValue().toString();
                    String newValue = value.substring(0, value.indexOf("-")).trim(); //$NON-NLS-1$
                    if (repositoryDBIdAndNameMap.containsKey(newValue)) {
                        value = value.replace(newValue, repositoryDBIdAndNameMap.get(newValue));
                        return value;
                    }
                }
            }
        }
        return null;
    }

}
