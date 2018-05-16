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
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.talend.commons.ui.runtime.image.ImageUtils;
import org.talend.commons.ui.runtime.image.ImageUtils.ICON_SIZE;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.ui.IJobletProviderService;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.repository.documentation.generation.IComponentHandler;

/**
 * This abstract class is defined some common methods for generation HTML. <br/>
 * 
 */
public abstract class AbstractComponentHandler implements IComponentHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.genHTMLDoc.IComponentHandler#generateComponentInfo(org.dom4j.Element,
     * java.util.List)
     */
    public abstract void generateComponentInfo();

    /**
     * Gets the file path of component icon base on node.
     * 
     * @return a string reprenenting component icon's path
     */
    protected String getComponentIconPath(INode node) {

        IComponent component = node.getComponent();
        if (PluginChecker.isJobLetPluginLoaded()) {
            IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                    IJobletProviderService.class);
            if (service != null && service.isJobletComponent(node)) {
                String filePath = getTmpFolder(node) + File.separator + node.getUniqueName() + ".png"; //$NON-NLS-1$
                ImageUtils.save(CoreImageProvider.getComponentIcon(component, ICON_SIZE.ICON_32), filePath, SWT.IMAGE_PNG);
                return filePath;

            }
        }
        // remove this part for bug:15579 by xtan
        // String string = component.getIcon32().toString();
        //        String path = string.substring("URLImageDescriptor(".length(), string.length() - 1); //$NON-NLS-1$
        // try {
        // return new URL(path).getPath();
        // } catch (MalformedURLException e) {
        // // do nothing, if component icon not found directly, recreate the image from icon image
        // }
        if (component.getIcon32() != null) {
            String filePath = getTmpFolder(node) + File.separator + node.getUniqueName() + ".png"; //$NON-NLS-1$
            ImageUtils.save(CoreImageProvider.getComponentIcon(component, ICON_SIZE.ICON_32), filePath, SWT.IMAGE_PNG);
            return filePath;
        }
        return null;
    }

    /**
     * Generates component details informaiton.
     * 
     * @param isExternalNodeComponent
     * @param nodeElement
     * @param picFilePathMap
     * @param targetConnectionMap
     * @param repositoryDBIdAndNameMap
     * @return
     */
    protected Element generateComponentDetailsInfo(boolean isExternalNodeComponent, INode node, Element nodeElement,
            Map picFilePathMap, Map<String, List> sourceConnectionMap, Map<String, List> targetConnectionMap,
            Map<String, String> repositoryDBIdAndNameMap) {

        Element componentElement = nodeElement.addElement("component"); //$NON-NLS-1$
        String relativePath = getComponentIconPath(node);
        String componentIconFileName = new File(relativePath).getName(); //$NON-NLS-1$

        String uniqueName = node.getUniqueName();
        componentElement.addAttribute("icon", IHTMLDocConstants.PICTUREFOLDERPATH + componentIconFileName); //$NON-NLS-1$
        componentElement.addAttribute("uniqueName", uniqueName); //$NON-NLS-1$

        // Stores the path of component icon.
        picFilePathMap.put(componentIconFileName, relativePath);
        List<? extends IElementParameter> elementParameters = node.getElementParameters();

        String componentLabel = getLabelFromElemParameters(elementParameters);

        componentElement.addAttribute("label", componentLabel == null ? "" : componentLabel); //$NON-NLS-1$ //$NON-NLS-2$

        String previewImagePath = ""; //$NON-NLS-1$

        // If component is external node component, gets its preview picture.
        if (isExternalNodeComponent) {
            previewImagePath = node.getUniqueName() + IHTMLDocConstants.JOB_PREVIEW_PIC_SUFFIX;
            Object obj = node.getProcess();
            boolean hasScreenshots = false;
            if (obj instanceof IProcess2) {
                IProcess2 process = (IProcess2) obj;
                Item item = process.getProperty().getItem();
                if (item instanceof ProcessItem) {
                    ProcessItem processItem = (ProcessItem) item;
                    if (processItem.getProcess().getScreenshots().get(uniqueName) != null) {
                        hasScreenshots = true;
                    }
                } else if (item instanceof JobletProcessItem) {
                    JobletProcessItem jobletItem = (JobletProcessItem) item;
                    if (jobletItem.getJobletProcess().getScreenshots().get(uniqueName) != null) {
                        hasScreenshots = true;
                    }
                }
            }

            if (!previewImagePath.equals("")) { //$NON-NLS-1$
                if (hasScreenshots) {
                    componentElement
                            .addAttribute(
                                    "preview", IHTMLDocConstants.PICTUREFOLDERPATH + IHTMLDocConstants.EXTERNAL_NODE_PREVIEW + previewImagePath); //$NON-NLS-1$
                }
            }
        }

        List<String> sourceList = sourceConnectionMap.get(uniqueName);

        // Gets the input of current component.
        if (sourceList != null && sourceList.size() > 0) {
            for (String string : sourceList) {
                Element inputElement = componentElement.addElement("input"); //$NON-NLS-1$
                inputElement.addAttribute("link", string); //$NON-NLS-1$
                inputElement.setText(HTMLDocUtils.checkString(string));
            }
        } else {// Sets the value of input to 'none'.
            Element inputElement = componentElement.addElement("input"); //$NON-NLS-1$
            inputElement.setText(IHTMLDocConstants.EMPTY_ELEMENT_VALUE);
        }

        List<String> targetList = targetConnectionMap.get(uniqueName);
        // Gets the output of current component.
        if (targetList != null && targetList.size() > 0) {
            for (String string : targetList) {
                Element outputElement = componentElement.addElement("output"); //$NON-NLS-1$
                outputElement.addAttribute("link", string); //$NON-NLS-1$
                outputElement.setText(string);
            }
        } else {// Sets the value of output to 'none'.
            Element inputElement = componentElement.addElement("output"); //$NON-NLS-1$
            inputElement.setText(IHTMLDocConstants.EMPTY_ELEMENT_VALUE);
        }

        Element componentTypeElement = componentElement.addElement("componentType"); //$NON-NLS-1$
        componentTypeElement.setText(HTMLDocUtils.checkString(node.getComponent().getName()));

        return componentElement;
    }

    /**
     * Gets the value of Label format.
     * 
     * @param elementParameters
     * @return
     */
    private String getLabelFromElemParameters(List<? extends IElementParameter> elementParameters) {

        for (IElementParameter parameter : elementParameters) {
            if (parameter.getCategory() == EComponentCategory.VIEW && parameter.getName().equals("LABEL")) { //$NON-NLS-1$
                return parameter.getValue().toString();
            }
        }

        return null;
    }

    /**
     * Gets the temporary folder.
     * 
     * @return a string representing temporary folder
     */
    protected String getTmpFolder(INode node) {
        String tmpFold = HTMLDocUtils.getTmpFolder() + File.separatorChar + node.getUniqueName();
        // String tmpFold = System.getProperty("osgi.instance.area") +
        File tempFile = new File(tmpFold);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        return tmpFold;
    }

    /**
     * This class is used for generating component schema information.
     * 
     * @param node
     * @param componentElement
     */
    protected void generateComponentSchemaInfo(INode node, Element componentElement) {
        List metaDataList = node.getMetadataList();
        Element schemasElement = null;
        if (metaDataList != null && metaDataList.size() != 0) {
            schemasElement = componentElement.addElement("schemas"); //$NON-NLS-1$
            boolean isBuiltIn = node.getConnectorFromName(EConnectionType.FLOW_MAIN.getName()).isMultiSchema()
                    || node.getConnectorFromName(EConnectionType.TABLE.getName()).isMultiSchema();

            for (int j = 0; j < metaDataList.size(); j++) {
                if ((!isBuiltIn) && (j > 0)) {
                    // if the component's main connector is not built-in
                    break;
                }
                IMetadataTable table = (IMetadataTable) metaDataList.get(j);
                List columnTypeList = table.getListColumns();
                Element schemaElement = schemasElement.addElement("schema"); //$NON-NLS-1$

                String metaName = table.getLabel();
                if (metaName == null) {
                    metaName = table.getTableName();
                }
                schemaElement.addAttribute("name", HTMLDocUtils.checkString(metaName)); //$NON-NLS-1$
                boolean dbComponent = false;
                if (node.getComponent().getOriginalFamilyName().startsWith("Database") || node.getComponent().getOriginalFamilyName().startsWith("ELT")) { //$NON-NLS-1$ //$NON-NLS-2$
                    dbComponent = true;
                }

                for (int k = 0; k < columnTypeList.size(); k++) {
                    IMetadataColumn columnType = (IMetadataColumn) columnTypeList.get(k);
                    Element columnElement = schemaElement.addElement("column"); //$NON-NLS-1$

                    columnElement.addAttribute("name", HTMLDocUtils.checkString(columnType.getLabel())); //$NON-NLS-1$
                    // if (dbComponent) {
                    // columnElement.addAttribute("dbColumn",
                    // HTMLDocUtils.checkString(columnType.getOriginalDbColumnName()));
                    // }

                    columnElement.addAttribute("key", HTMLDocUtils.checkString(columnType.isKey() + "")); //$NON-NLS-1$ //$NON-NLS-2$
                    String type = HTMLDocUtils.checkString(columnType.getTalendType());
                    if (node.getComponent().getOriginalFamilyName().startsWith("ELT")) { //$NON-NLS-1$
                        // if ELT then use the db type
                        type = HTMLDocUtils.checkString(columnType.getType());
                    } else if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
                        type = JavaTypesManager.getTypeToGenerate(columnType.getTalendType(), columnType.isNullable());
                    }
                    columnElement.addAttribute("type", type); //$NON-NLS-1$
                    // if (dbComponent) {
                    // columnElement.addAttribute("dbType", HTMLDocUtils.checkString(columnType.getType()));
                    // }
                    String length;
                    if ((columnType.getLength() == null) || (columnType.getLength() == 0)) {
                        length = ""; //$NON-NLS-1$
                    } else {
                        length = String.valueOf(columnType.getLength());
                    }
                    columnElement.addAttribute("length", length); //$NON-NLS-1$
                    String precision;
                    if ((columnType.getPrecision() == null) || (columnType.getPrecision() == 0)) {
                        precision = ""; //$NON-NLS-1$
                    } else {
                        precision = String.valueOf(columnType.getPrecision());
                    }
                    columnElement.addAttribute("precision", precision); //$NON-NLS-1$
                    columnElement.addAttribute("nullable", HTMLDocUtils.checkString(columnType.isNullable() + "")); //$NON-NLS-1$ //$NON-NLS-2$
                    columnElement.addAttribute("comment", HTMLDocUtils.checkString(ElementParameterParser.parse(node, columnType //$NON-NLS-1$
                            .getComment())));
                    if (PluginChecker.isDatacertPluginLoaded()) {
                        columnElement.addAttribute(
                                "relatedentity", HTMLDocUtils.checkString(ElementParameterParser.parse(node, columnType //$NON-NLS-1$
                                        .getRelatedEntity())));
                        columnElement.addAttribute(
                                "relationshiptype", HTMLDocUtils.checkString(ElementParameterParser.parse(node, columnType //$NON-NLS-1$
                                        .getRelationshipType())));
                    }
                }
            }
        }
        // boolean isExternalNode = true;
        // if (isExternalNode) {
        // schemasElement.addElement("componentName", node.getProcess().getLabel());
        // }
    }
}
