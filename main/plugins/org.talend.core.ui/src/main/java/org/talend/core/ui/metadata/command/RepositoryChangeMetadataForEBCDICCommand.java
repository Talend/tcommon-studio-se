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
import org.talend.core.model.metadata.IEbcdicConstant;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.MultiSchemasUtil;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.ui.CoreUIPlugin;

/**
 * nrousseau class global comment. Detailled comment
 */
public class RepositoryChangeMetadataForEBCDICCommand extends Command {

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
    public RepositoryChangeMetadataForEBCDICCommand(INode node, String propName, Object propValue,
            IMetadataTable newOutputMetadata) {
        this(node, propName, propValue, null, newOutputMetadata, null, null);
    }

    public RepositoryChangeMetadataForEBCDICCommand(INode node, String propName, Object newPropValue,
            IMetadataTable newOutputMetadata, int index) {
        this(node, propName, newPropValue, null, newOutputMetadata, null, index);
    }

    public RepositoryChangeMetadataForEBCDICCommand(INode node, String propName, Object newPropValue, Object oldPropValue,
            IMetadataTable newOutputMetadata, IMetadataTable oldOutputMetadata, int index) {
        this(node, propName, newPropValue, oldPropValue, newOutputMetadata, oldOutputMetadata, new Integer(index));
    }

    private RepositoryChangeMetadataForEBCDICCommand(INode node, String propName, Object newPropValue, Object oldPropValue,
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
                        String schemaName = (String) line.get(IEbcdicConstant.FIELD_SCHEMA);
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
                setNoXc2jValue();
            }
        }
    }

    private void setNoXc2jValue() {
        // for EBCDIC ,there are schemaType and schemas exists at the same time ,so need to filter the extra
        // metadataTable
        List<IMetadataTable> tables = node.getMetadataList();
        List<IMetadataTable> reallytables = new ArrayList();
        for (IMetadataTable table : tables) {
            if (!node.getUniqueName().equals(table.getTableName())) {
                reallytables.add(table);
            }
        }
        List<IElementParameter> elementParameterList = (List<IElementParameter>) node.getElementParameters();
        for (IElementParameter param : elementParameterList) {
            if (param.getFieldType().equals(EParameterFieldType.CHECK)) {
                if (param.getName().equals("NO_X2CJ_FILE")) {
                    if (reallytables.size() == 1) {
                        // single schema do not display the table and xc2j file
                        param.setValue(new Boolean(true));
                    } else {
                        param.setValue(new Boolean(false));
                    }
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

        valueMap.put(IEbcdicConstant.FIELD_SCHEMA, newPropValue);
        // if no value, set default field value
        Object fieldValue = valueMap.get(IEbcdicConstant.FIELD_CODE);
        if (fieldValue == null || "".equals(fieldValue)) { //$NON-NLS-1$
            valueMap.put(IEbcdicConstant.FIELD_CODE, newOutputMetadata.getTableName());
        }
        valueMap.put(IEbcdicConstant.FIELD_SCHEMA + IEbcdicConstant.REF_TYPE, IEbcdicConstant.REF_ATTR_REPOSITORY);

        if (oldOutputMetadata != null) {
            CoreUIPlugin.getDefault().getDesignerCoreService().removeConnection(node, oldOutputMetadata.getTableName());
            node.getMetadataList().remove(oldOutputMetadata);
        }

        String uinqueTableName = node.getProcess().generateUniqueConnectionName(
                MultiSchemasUtil.getConnectionBaseName((String) newPropValue));
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
