// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.ISAPConstant;
import org.talend.core.model.metadata.MetadataSchemaType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.MultiSchemasUtil;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParamData;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameter;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.utils.TalendQuoteUtils;

/**
 *
 * created by wchen on Sep 19, 2014 Detailled comment
 *
 */
public class RepositoryChangeMetadataForSAPBapi extends Command {

    private INode node;

    private IMetadataTable newMetadatTable, oldMetadataTable;

    private SAPFunctionUnit functionUnit;

    private final String ISINPUT = "isinput"; //$NON-NLS-1$

    private final String TRUE = "true"; //$NON-NLS-1$

    private Integer selectionIndex;

    public RepositoryChangeMetadataForSAPBapi(INode node, SAPFunctionUnit functionUnit, IMetadataTable newMetadatTable,
            IMetadataTable oldMetadataTable, Integer selectionIndex) {
        super();
        this.node = node;
        this.functionUnit = functionUnit;
        if (newMetadatTable != null) {
            this.newMetadatTable = newMetadatTable.clone();
            newMetadatTable.setTableType(newMetadatTable.getTableType());
        }
        this.oldMetadataTable = oldMetadataTable;
        this.selectionIndex = selectionIndex;

    }

    public RepositoryChangeMetadataForSAPBapi(INode node, SAPFunctionUnit functionUnit, IMetadataTable newMetadatTable,
            IMetadataTable oldMetadataTable) {
        this(node, functionUnit, newMetadatTable, oldMetadataTable, null);
    }

    @Override
    public void execute() {
        if (node == null) {
            return;
        }

        IElementParameter elementParameter = node.getElementParameter(ISAPConstant.SAP_FUNCTION);
        if (elementParameter != null && functionUnit != null) {
            elementParameter.setValue(TalendQuoteUtils.addQuotes(functionUnit.getName()));
        }

        // setup output schemas
        IElementParameter schemasTableParam = node.getElementParameter(ISAPConstant.TABLE_SCHEMAS);
        if (schemasTableParam != null) {
            List<Map<String, Object>> paramValues = (List<Map<String, Object>>) schemasTableParam.getValue();
            if (paramValues == null) {
                paramValues = new ArrayList<Map<String, Object>>();
                schemasTableParam.setValue(paramValues);
            }
            if (newMetadatTable != null) {
                // create new line
                createNewSchema(paramValues, newMetadatTable, MetadataSchemaType.OUTPUT.name());
            } else {
                if (functionUnit != null) {
                    EList<MetadataTable> tables = functionUnit.getTables();
                    for (MetadataTable table : tables) {
                        createNewSchema(paramValues, ConvertionHelper.convert(table), MetadataSchemaType.OUTPUT.name());
                    }
                }
            }
        }
        // setup output schemas
        IElementParameter inputTableParam = node.getElementParameter(ISAPConstant.MAPPING_INPUT);
        if (inputTableParam != null) {
            List<Map<String, Object>> paramValues = (List<Map<String, Object>>) inputTableParam.getValue();
            if (paramValues == null) {
                paramValues = new ArrayList<Map<String, Object>>();
                inputTableParam.setValue(paramValues);
            }
            if (newMetadatTable != null) {
                // create new line
                createNewSchema(paramValues, newMetadatTable, MetadataSchemaType.INPUT.name());
            } else {
                if (functionUnit != null) {
                    EList<MetadataTable> tables = functionUnit.getInputTables();
                    for (MetadataTable table : tables) {
                        EMap<String, String> properties = table.getAdditionalProperties();
                        if (properties != null) {
                            properties.put(ISINPUT, TRUE);
                        }
                        createNewSchema(paramValues, ConvertionHelper.convert(table), MetadataSchemaType.INPUT.name());
                    }
                }
            }
        }
    }

    private void createNewSchema(List<Map<String, Object>> paramValues, IMetadataTable newMetadatTable, String tableType) {
        if (newMetadatTable != null && tableType.equals(newMetadatTable.getTableType())) {
            boolean found = false;
            if (paramValues.size() > 0) {
                List<IMetadataTable> metadataList = node.getMetadataList();
                for (IMetadataTable metadata : metadataList) {
                    if (tableType.equals(metadata.getTableType())) {
                        if (metadata.getLabel() != null && metadata.getLabel().equals(newMetadatTable.getLabel())) {
                            found = true;
                            MetadataToolHelper.copyTable(newMetadatTable, metadata);
                        }
                    }
                }
            }
            if (found) {
                return;
            }
            Boolean isInput = MetadataSchemaType.INPUT.name().equals(tableType);
            Map valueMap = null;

            String uinqueTableName = node.getProcess().generateUniqueConnectionName(
                    MultiSchemasUtil.getConnectionBaseName(newMetadatTable.getLabel()));
            String type = getType(newMetadatTable, isInput);
            if (type == null) {
                return;
            }
            if (selectionIndex != null && selectionIndex < paramValues.size()) {
                valueMap = paramValues.get(selectionIndex);
            }

            if (valueMap == null) {
                valueMap = new HashMap();
                paramValues.add(valueMap);
            }
            valueMap.put(ISAPConstant.NAME, TalendQuoteUtils.addQuotes(newMetadatTable.getLabel()));
            valueMap.put(ISAPConstant.TYPE, type);
            valueMap.put(ISAPConstant.FIELD_SCHEMA, uinqueTableName);
            if (isInput) {
                valueMap.put(ISAPConstant.PARENT_ROW, ""); //$NON-NLS-1$

                String inputParameterType = getInputParameterType(functionUnit, newMetadatTable);
                if (StringUtils.isBlank(inputParameterType)) {
                    inputParameterType = ISAPConstant.PARAMETER_TYPE_IMPORT;
                }
                valueMap.put(ISAPConstant.PARAMETER_TYPE, inputParameterType);

                valueMap.put(ISAPConstant.CHANGING, ISAPConstant.PARAMETER_TYPE_CHANGING.equals(inputParameterType));

                Map<String, String> properties = newMetadatTable.getAdditionalProperties();
                if (properties != null) {
                    properties.put(ISINPUT, TRUE);
                }
            }

            if (oldMetadataTable != null) {
                CoreUIPlugin.getDefault().getDesignerCoreService().removeConnection(node, oldMetadataTable.getTableName());
                node.getMetadataList().remove(oldMetadataTable);
            }

            newMetadatTable.setTableName(uinqueTableName);
            newMetadatTable.setLabel(uinqueTableName);
            node.getProcess().addUniqueConnectionName(uinqueTableName);
            node.getMetadataList().add(newMetadatTable);
        }
    }

    private String getInputParameterType(SAPFunctionUnit funcUnit, IMetadataTable newMetadatTable) {
        String parameterType = "";
        if (funcUnit != null && funcUnit.getParamData() != null && funcUnit.getParamData().getInputRoot() != null) {
            SAPFunctionParameter foundParam = null;
            String tableName = newMetadatTable.getTableName();
            for (SAPFunctionParameter param : funcUnit.getParamData().getInputRoot().getChildren()) {
                if (tableName.equals(param.getName())) {
                    foundParam = param;
                    break;
                }
            }
            if (foundParam != null) {
                if (foundParam.isChanging()) {
                    parameterType = ISAPConstant.PARAMETER_TYPE_CHANGING;
                } else {
                    if (ISAPConstant.PARAM_TABLE.equals(foundParam.getType())) {
                        if (foundParam.isTableResideInTables()) {
                            parameterType = ISAPConstant.PARAMETER_TYPE_TABLES;
                        } else {
                            parameterType = ISAPConstant.PARAMETER_TYPE_IMPORT;
                        }
                    } else {
                        parameterType = ISAPConstant.PARAMETER_TYPE_IMPORT;
                    }
                }
            }
        }
        return parameterType;
    }

    private String getType(IMetadataTable table, boolean isInput) {
        if (functionUnit == null) {
            return null;
        }
        SAPFunctionParamData paramData = functionUnit.getParamData();
        if (paramData == null) {
            return null;
        }

        EList<SAPFunctionParameter> parameterChildrenList = null;
        if (isInput) {
            parameterChildrenList = paramData.getInputRoot().getChildren();
        } else {
            parameterChildrenList = paramData.getOutputRoot().getChildren();
        }

        for (SAPFunctionParameter parameter : parameterChildrenList) {
            if (parameter.getName().equals(table.getTableName())) {
                if (parameter.getType().equals(ISAPConstant.PARAM_STRUCTURE)) {
                    return ISAPConstant.PARAM_STRUCTURE.toUpperCase();
                } else if (parameter.getType().equals(ISAPConstant.PARAM_TABLE)) {
                    return ISAPConstant.PARAM_TABLE.toUpperCase();
                } else {
                    return ISAPConstant.PARAM_SINGLE.toUpperCase();
                }
            }
        }

        if (!isInput && ISAPConstant.SINGLE_PARAM_TABLE_NAME.equals(table.getTableName())) {
            return ISAPConstant.PARAM_SINGLE.toUpperCase();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        // TODO Auto-generated method stub
        super.undo();
    }

}
