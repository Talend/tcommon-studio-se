// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.documentation.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.ProjectManager;

/**
 * This class is internal node component handler for generating HTML. <br/>
 * 
 */
public class InternalNodeComponentHandler extends AbstractComponentHandler {

    private Map<String, List> targetConnectionMap = null;

    private Map<String, List> sourceConnectionMap = null;

    private Map<String, String> picFilePathMap;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, String> repositoryDBIdAndNameMap;

    private IDesignerCoreService designerCoreService;

    private Element internalNodeElement;

    private List<INode> componentsList;

    private Map<String, Object> internalNodeHTMLMap;

    /**
     * DOC Administrator InternalNodeComponentHandler constructor comment.
     * 
     * @param picFilePathMap
     * @param internalNodeElement
     * @param allComponentsList
     * @param sourceConnectionMap
     * @param targetConnectionMap
     * @param designerCoreService
     * @param name2
     * @param name
     */
    public InternalNodeComponentHandler(Map<String, String> picFilePathMap, Element internalNodeElement,
            List<INode> allComponentsList, Map<String, List> sourceConnectionMap, Map<String, List> targetConnectionMap,
            IDesignerCoreService designerCoreService, Map<String, ConnectionItem> repositoryConnectionItemMap,
            Map<String, String> repositoryDBIdAndNameMap, Map<String, Object> internalNodeHTMLMap) {

        this.picFilePathMap = picFilePathMap;
        this.internalNodeElement = internalNodeElement;
        this.componentsList = allComponentsList;
        this.sourceConnectionMap = sourceConnectionMap;
        this.targetConnectionMap = targetConnectionMap;
        this.designerCoreService = designerCoreService;
        this.repositoryConnectionItemMap = repositoryConnectionItemMap;
        this.repositoryDBIdAndNameMap = repositoryDBIdAndNameMap;
        this.internalNodeHTMLMap = internalNodeHTMLMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.genHTMLDoc.IComponentHandler#generateComponentInfo(org.dom4j.Element,
     * java.util.List)
     */
    @Override
    public void generateComponentInfo() {
        for (INode node : this.componentsList) {
            Element componentElement = generateComponentDetailsInfo(false, node, this.internalNodeElement, this.picFilePathMap,
                    this.sourceConnectionMap, this.targetConnectionMap, this.repositoryDBIdAndNameMap);

            CoreRuntimePlugin.getInstance().getRepositoryService().setInternalNodeHTMLMap(node, internalNodeHTMLMap);
            generateComponentElemParamters(node, componentElement);
        }
    }

    /**
     * DOC Administrator Comment method "generateComponentElemParamters".
     * 
     * @param node
     * @param componentElement
     */
    protected void generateComponentElemParamters(INode node, Element componentElement) {
        Element parametersElement = componentElement.addElement("parameters"); //$NON-NLS-1$

        List elementParameterList = node.getElementParameters();

        boolean istRunJob = node.getComponent().getName().equals("tRunJob"); //$NON-NLS-1$
        generateComponentElementParamInfo(istRunJob, parametersElement, elementParameterList, node);

        generateComponentSchemaInfo(node, componentElement);
    }

    /**
     * Generates the element parameters information of component.
     * 
     * @param istMap
     * @param istRunJob
     * @param parametersElement
     * @param elementParameterList
     * @param node
     */
    private void generateComponentElementParamInfo(boolean istRunJob, Element parametersElement, List elementParameterList,
            INode node) {
        List<IElementParameter> copyElementParameterList = new ArrayList(elementParameterList);

        if (elementParameterList != null && elementParameterList.size() != 0) {
            for (int j = 0; j < elementParameterList.size(); j++) {
                IElementParameter elemparameter = (IElementParameter) elementParameterList.get(j);

                //                if ((istRunJob && elemparameter.getName().equals("PROCESS")) //$NON-NLS-1$
                // || (!elemparameter.isShow(copyElementParameterList) && (!elemparameter.getName().equals(
                // EParameterFieldType.SCHEMA_TYPE.getName())))
                // || elemparameter.getCategory().equals(EComponentCategory.MAIN)
                // || elemparameter.getCategory().equals(EComponentCategory.VIEW)
                // || elemparameter.getName().equals(IHTMLDocConstants.REPOSITORY)
                //                        || elemparameter.getName().equals("SCHEMA") || elemparameter.getName().equals("QUERYSTORE") //$NON-NLS-1$ //$NON-NLS-2$
                //                        || elemparameter.getName().equals("PROPERTY") //$NON-NLS-1$
                // || elemparameter.getName().equals(EParameterFieldType.ENCODING_TYPE.getName())) {
                // continue;
                // }
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
                else if ((elemparameter.getRepositoryValue() != null
                        && elemparameter.getRepositoryValue().toUpperCase().contains("PASSWORD") //$NON-NLS-1$
                || elemparameter.getFieldType() == EParameterFieldType.PASSWORD)
                        && ProjectManager.getInstance().getCurrentProject().getEmfProject().isHidePassword()
                        && !ContextParameterUtils.containContextVariables((String) elemparameter.getValue())) {
                    value = "******"; //$NON-NLS-1$
                }
                Element columnElement = parametersElement.addElement("column"); //$NON-NLS-1$
                columnElement.addAttribute("name", HTMLDocUtils.checkString(elemparameter.getDisplayName())); //$NON-NLS-1$

                if (value.equalsIgnoreCase(IHTMLDocConstants.REPOSITORY_BUILT_IN)) {
                    value = IHTMLDocConstants.DISPLAY_BUILT_IN;
                }
                // fix for TDI-17768
                // columnElement.setText(value);
                columnElement.addAttribute("value", HTMLDocUtils.checkString(value));
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
