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
package org.talend.core.ui.metadata.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IRuleConstant;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.ui.CoreUIPlugin;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class ChangeRuleMetadataCommand extends Command {

    private INode node;

    private String propName;

    private Object newPropValue;

    private IMetadataTable newOutputTable, oldOutputMetadata;

    private Integer index;

    public ChangeRuleMetadataCommand(INode node, String propName, Object newPropValue, IMetadataTable newOutputTable, int index) {
        this.node = node;
        this.propName = propName;
        this.newPropValue = newPropValue;
        this.newOutputTable = newOutputTable;
        this.index = index;
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

            }
            // else { // for dnd
            // boolean found = false;
            // if (paramValues.size() > 0) {
            // for (Map<String, Object> line : paramValues) {
            // String schemaName = (String) line.get(IEbcdicConstant.FIELD_SCHEMA);
            // if (schemaName != null && schemaName.equals(newPropValue)) {
            // found = true;
            // IMetadataTable table = MetadataTool.getMetadataTableFromNode(node, (String) newPropValue);
            // if (table != null) {
            // MetadataTool.copyTable(newOutputTable, table);
            // }
            //
            // }
            // }
            // }
            // if (!found) {
            // // create new line
            // setLineValue(paramValues, valueMap);
            // }
            //
            // }

        }
    }

    private void setLineValue(List<Map<String, Object>> paramValues, Map<String, Object> valueMap) {
        //
        // String uinqueTableName = node.getProcess().generateUniqueConnectionName(
        // MultiSchemasUtil.getConnectionBaseName((String) newPropValue));
        if (valueMap == null) {
            valueMap = new HashMap<String, Object>();
            paramValues.add(valueMap);
        }
        valueMap.put(IRuleConstant.SCHEMA, newPropValue);
        // if no value, set default field value
        // Object fieldValue = valueMap.get(IRuleConstant.FIELD_RULE);
        //        if (fieldValue == null || "".equals(fieldValue)) { //$NON-NLS-1$
        // valueMap.put(IRuleConstant.FIELD_RULE, newPropValue);
        // }

        if (oldOutputMetadata != null) {
            CoreUIPlugin.getDefault().getDesignerCoreService().removeConnection(node, oldOutputMetadata.getTableName());
            node.getMetadataList().remove(oldOutputMetadata);
        }

        newOutputTable.setLabel((String) newPropValue);
        newOutputTable.setTableName((String) newPropValue);

        node.getProcess().addUniqueConnectionName((String) newPropValue);
        node.getMetadataList().add(newOutputTable);

    }

    @Override
    public void undo() {
        // TODO
    }

}
