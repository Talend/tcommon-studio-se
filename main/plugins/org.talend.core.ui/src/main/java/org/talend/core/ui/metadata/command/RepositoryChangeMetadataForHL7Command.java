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
import org.talend.core.model.metadata.IHL7Constant;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.ISAPConstant;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.MultiSchemasUtil;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * nrousseau class global comment. Detailled comment
 */
public class RepositoryChangeMetadataForHL7Command extends Command {

    private INode node;

    private final String propName;

    private final Object oldPropValue, newPropValue;

    private IMetadataTable newOutputMetadata, oldOutputMetadata;

    private Integer index;

    /**
     * 
     * nrousseau RepositoryChangeMetadataForEBCDICCommand constructor comment.
     * 
     * only for DND
     */
    public RepositoryChangeMetadataForHL7Command(INode node, String propName, Object propValue, IMetadataTable newOutputMetadata) {
        this(node, propName, propValue, null, newOutputMetadata, null, null);
    }

    public RepositoryChangeMetadataForHL7Command(INode node, String propName, Object newPropValue,
            IMetadataTable newOutputMetadata, int index) {
        this(node, propName, newPropValue, null, newOutputMetadata, null, index);
    }

    public RepositoryChangeMetadataForHL7Command(INode node, String propName, Object newPropValue, Object oldPropValue,
            IMetadataTable newOutputMetadata, IMetadataTable oldOutputMetadata, int index) {
        this(node, propName, newPropValue, oldPropValue, newOutputMetadata, oldOutputMetadata, new Integer(index));
    }

    private RepositoryChangeMetadataForHL7Command(INode node, String propName, Object newPropValue, Object oldPropValue,
            IMetadataTable newOutputMetadata, IMetadataTable oldOutputMetadata, Integer index) {
        super();
        this.node = node;
        this.propName = propName;
        this.newPropValue = newPropValue;
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
        // avoid problem that add same column twice
        final List<IMetadataTable> metadataList = node.getMetadataList();
        if (!metadataList.isEmpty()) {
            final List<IMetadataColumn> newColumns = newOutputMetadata.getListColumns();
            for (IMetadataColumn newColumn : newColumns) {
                final String label = newColumn.getLabel();
                for (IMetadataTable table : metadataList) {
                    if (table.getLabel() != null && table.getLabel().equals(newOutputMetadata.getLabel())
                            && table.getColumn(label) != null) {
                        return;
                    }
                }
            }
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
                        String schemaName = (String) line.get(IHL7Constant.FIELD_SCHEMA);
                        String mappingName = (String) line.get(IHL7Constant.FIELD_SCHEMA);
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

        String displayName = "";
        // valueMap.put(IHL7Constant.FIELD_SCHEMA, newPropValue);
        List columnList = ConvertionHelper.convert(newOutputMetadata).getColumns();
        for (int i = 0; i < columnList.size(); i++) {
            MetadataColumn column = (MetadataColumn) columnList.get(i);
            String original = column.getOriginalField();
            if (original != null && !"".equals(original)) {
                if (original.indexOf(TalendQuoteUtils.LBRACKET) > 0) {
                    original = original.substring(0, original.indexOf(TalendQuoteUtils.LBRACKET));
                }
            }
            displayName += TalendQuoteUtils.addQuotes(original);
            if (i != columnList.size() - 1) {
                displayName += ',';
            }
        }
        valueMap.put(IHL7Constant.FIELD_MAPPING, displayName);

        if (oldOutputMetadata != null) {
            CoreUIPlugin.getDefault().getDesignerCoreService().removeConnection(node, oldOutputMetadata.getTableName());
            node.getMetadataList().remove(oldOutputMetadata);
        }

        newOutputMetadata.setLabel((String) newPropValue);
        newOutputMetadata.setTableName(uinqueTableName);

        node.getProcess().addUniqueConnectionName(uinqueTableName);
        node.getMetadataList().add(newOutputMetadata);
    }

    @Override
    public void undo() {
        // TODO
    }

}
