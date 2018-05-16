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
package org.talend.core.ui.metadata.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.ISAPConstant;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.MultiSchemasUtil;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParamData;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameter;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class RepositoryChangeMetadataForSAPCommand extends Command {

    private INode node;

    private final String propName;

    private final Object oldPropValue, newPropValue;

    private IMetadataTable newOutputMetadata, oldOutputMetadata;

    private Integer index;

    private SAPFunctionUnit functionUnit;

    /**
     * 
     * hwang RepositoryChangeMetadataForSAPCommand constructor comment.
     * 
     * only for DND
     */
    public RepositoryChangeMetadataForSAPCommand(INode node, String propName, Object propValue, IMetadataTable newOutputMetadata) {
        this(node, propName, propValue, null, newOutputMetadata, null, null);
    }

    public RepositoryChangeMetadataForSAPCommand(INode node, String propName, Object propValue, IMetadataTable newOutputMetadata,
            SAPFunctionUnit functionUnit) {
        this(node, propName, propValue, null, newOutputMetadata, null, null);
        this.functionUnit = functionUnit;
    }

    public RepositoryChangeMetadataForSAPCommand(INode node, String propName, Object newPropValue,
            IMetadataTable newOutputMetadata, int index) {
        this(node, propName, newPropValue, null, newOutputMetadata, null, index);
    }

    public RepositoryChangeMetadataForSAPCommand(INode node, String propName, Object newPropValue, Object oldPropValue,
            IMetadataTable newOutputMetadata, IMetadataTable oldOutputMetadata, int index) {
        this(node, propName, newPropValue, oldPropValue, newOutputMetadata, oldOutputMetadata, new Integer(index));
    }

    private RepositoryChangeMetadataForSAPCommand(INode node, String propName, Object newPropValue, Object oldPropValue,
            IMetadataTable newOutputMetadata, IMetadataTable oldOutputMetadata, Integer index) {
        super();
        this.node = node;
        this.propName = propName;
        this.newPropValue = newPropValue;// /////
        this.newOutputMetadata = newOutputMetadata.clone();
        this.index = index;
        this.oldPropValue = oldPropValue;
        this.oldOutputMetadata = oldOutputMetadata;
    }

    @Override
    public void execute() {
        if (node == null) {
            return;
        }
        IElementParameter schemasTableParam = node.getElementParameter(propName);
        if (schemasTableParam != null) {

            List<Map<String, Object>> paramValues = (List<Map<String, Object>>) schemasTableParam.getValue();
            if (paramValues == null) {
                paramValues = new ArrayList<Map<String, Object>>();
                schemasTableParam.setValue(paramValues);
            }
            //
            Map<String, Object> valueMap = null;
            if (index != null) {
                if (paramValues.size() > 0) {
                    valueMap = paramValues.get(index.intValue());
                }

                setLineValue(paramValues, valueMap);

            } else { // for dnd
                boolean found = false;
                if (paramValues.size() > 0) {
                    for (Map<String, Object> line : paramValues) {
                        String schemaName = (String) line.get(ISAPConstant.FIELD_SCHEMA);
                        if (schemaName != null && schemaName.equals(newPropValue)) {
                            found = true;
                            IMetadataTable table = MetadataToolHelper.getMetadataTableFromNode(node, (String) newPropValue);
                            if (table != null) {
                                MetadataToolHelper.copyTable(newOutputMetadata, table);
                            }

                        }
                    }
                }
                if (!found) {
                    // create new line
                    setLineValue(paramValues, valueMap);
                }

            }

        }
    }

    private void setLineValue(List<Map<String, Object>> paramValues, Map<String, Object> valueMap) {
        //
        if (valueMap == null) {
            valueMap = new HashMap<String, Object>();
            paramValues.add(valueMap);
        }
        String uinqueTableName = node.getProcess().generateUniqueConnectionName(
                MultiSchemasUtil.getConnectionBaseName((String) newPropValue));

        valueMap.put(ISAPConstant.FIELD_SCHEMA, uinqueTableName);
        // if no value, set default field value
        Object fieldValue = valueMap.get(ISAPConstant.FIELD_SAP_ITERATE_OUT_TYPE);
        if (fieldValue == null || "".equals(fieldValue)) { //$NON-NLS-1$
            if (newOutputMetadata.getLabel().equals("OUTPUT_SINGLE")) { //$NON-NLS-1$
                valueMap.put(ISAPConstant.FIELD_SAP_ITERATE_OUT_TYPE, "output_single"); //$NON-NLS-1$
            } else if (isStructure(newOutputMetadata.getLabel())) {
                valueMap.put(ISAPConstant.FIELD_SAP_ITERATE_OUT_TYPE, "output_structure"); //$NON-NLS-1$
            } else {
                valueMap.put(ISAPConstant.FIELD_SAP_ITERATE_OUT_TYPE, "table_output"); //$NON-NLS-1$
            }
        }
        valueMap.put(ISAPConstant.FIELD_SCHEMA + ISAPConstant.REF_TYPE, ISAPConstant.REF_ATTR_REPOSITORY);

        Object tableNameValue = valueMap.get(ISAPConstant.FIELD_SAP_TABLE_NAME);
        if (tableNameValue == null || "".equals(tableNameValue)) { //$NON-NLS-1$
            valueMap.put(ISAPConstant.FIELD_SAP_TABLE_NAME, TalendQuoteUtils.addQuotes(newOutputMetadata.getLabel()));
        }
        Object mapValue = valueMap.get(ISAPConstant.FIELD_MAPPING);
        List<IMetadataColumn> columnList = newOutputMetadata.getListColumns();
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < columnList.size(); i++) {
            IMetadataColumn metadataColumn = columnList.get(i);
            buff.append("\""); //$NON-NLS-1$
            buff.append(metadataColumn.getLabel());
            if (i == columnList.size() - 1) {
                buff.append("\""); //$NON-NLS-1$
            } else {
                buff.append("\","); //$NON-NLS-1$
            }
        }
        if (mapValue == null || "".equals(mapValue)) { //$NON-NLS-1$
            valueMap.put(ISAPConstant.FIELD_MAPPING, buff.toString());
        }

        if (oldOutputMetadata != null) {
            CoreUIPlugin.getDefault().getDesignerCoreService().removeConnection(node, oldOutputMetadata.getTableName());
            node.getMetadataList().remove(oldOutputMetadata);
        }

        // String uinqueTableName = node.getProcess().generateUniqueConnectionName(
        // MultiSchemasUtil.getConnectionBaseName((String) newPropValue));
        newOutputMetadata.setLabel((String) newPropValue);
        newOutputMetadata.setTableName(uinqueTableName);
        node.getProcess().addUniqueConnectionName(uinqueTableName);
        node.getMetadataList().add(newOutputMetadata);
    }

    private boolean isStructure(String label) {
        if (functionUnit == null) {
            return false;
        }
        SAPFunctionParamData paramData = functionUnit.getParamData();
        if (paramData == null) {
            return false;
        }

        // SCHEMAS is output param
        for (SAPFunctionParameter parameter : paramData.getOutputRoot().getChildren()) {
            if (parameter.getName().equals(label)) {
                if (parameter.getType().equals(ISAPConstant.PARAM_STRUCTURE)) {
                    return true;
                } else {
                    return false;
                }
            }

        }

        // OutputSAPFunctionParameterTable outputParameterTable = functionUnit.getOutputParameterTable();
        // for (Object obj : outputParameterTable.getColumns()) {
        // if (obj instanceof SAPFunctionParameterColumn) {
        // SAPFunctionParameterColumn column = (SAPFunctionParameterColumn) obj;
        // if (column == null || column.getStructureOrTableName() == null) {
        // continue;
        // }
        // if (column.getStructureOrTableName().equals(label)) {
        // String type = column.getParameterType();
        //                    if (type.startsWith("output") && type.endsWith("structure")) { //$NON-NLS-1$ //$NON-NLS-2$
        // return true;
        // }
        // }
        // }
        //
        // }
        return false;
    }
}
