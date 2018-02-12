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
package org.talend.core.model.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;

/**
 * Concrete class to instanciate as an AbstractNode for the BigData code generators
 *
 */
public class BigDataNode extends AbstractNode implements IBigDataNode {

    private boolean dummy = false;

    private String outputType = null;

    private String inputType = null;

    private Map<String, List<IMetadataColumn>> keyList = new java.util.HashMap<String, List<IMetadataColumn>>();

    /**
     * Default constructor for the BigDataNode
     */
    public BigDataNode() {
    }

    /**
     * Constructor for the BigDataNode
     *
     * @param component
     * @param uniqueName
     */
    public BigDataNode(IComponent component, String uniqueName) {
        setComponentName(component.getName());
        List<IMetadataTable> metaList = new ArrayList<IMetadataTable>();
        IMetadataTable metaTable = new MetadataTable();
        metaTable.setTableName(uniqueName);
        metaList.add(metaTable);
        setMetadataList(metaList);
        setComponent(component);
        setElementParameters(component.createElementParameters(this));
        setListConnector(component.createConnectors(this));
        setUniqueName(uniqueName);
        setHasConditionalOutputs(component.hasConditionalOutputs());
        setIsMultiplyingOutputs(component.isMultiplyingOutputs());
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#getInputType()
     */
    @Override
    public String getIncomingType() {
        List<? extends IConnection> incomingConnections = getIncomingConnections();
        for (IConnection inConnection : incomingConnections) {
            if (inConnection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                if (incomingConnections.get(0).getSource() instanceof BigDataNode) {
                    IBigDataNode node = (IBigDataNode) incomingConnections.get(0).getSource();
                    String requiredOutputType = node.getRequiredOutputType();
                    return requiredOutputType != null ? requiredOutputType : node.getIncomingType();
                } else {
                    // We are on an external node => PairRDD
                    // TODO Maybe on the futur we need to handle RDD or DataFrame, but this required a big refactoring
                    // of
                    // the external nodes.
                    return "KEYVALUE"; //$NON-NLS-1$
                }
            }
        }
        return null;
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#getOutputType()
     */
    @Override
    public String getOutgoingType() {
        List<? extends IConnection> outgoingConnections = getOutgoingConnections();
        for (IConnection outConnection : outgoingConnections) {
            if (outConnection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                if (outConnection.getTarget() instanceof BigDataNode) {
                    IBigDataNode node = (IBigDataNode) outgoingConnections.get(0).getTarget();
                    String requiredInputType = node.getRequiredInputType();
                    return (requiredInputType != null && !node.isIdentity()) ? requiredInputType : node.getOutgoingType();
                } else {
                    // We are on an external node => PairRDD
                    // TODO Maybe on the futur we need to handle RDD or DataFrame, but this required a big refactoring
                    // of
                    // the external nodes.
                    return "KEYVALUE"; //$NON-NLS-1$
                }
            }
        }
        return null;
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#getRequiredInputType()
     */
    @Override
    public String getRequiredInputType() {
        return this.inputType == null ? getComponent().getInputType() : this.inputType;
    }

    @Override
    public void setRequiredInputType(String requiredInputType) {
        this.inputType = requiredInputType;
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#getRequiredOutputType()
     */
    @Override
    public String getRequiredOutputType() {
        return this.outputType == null ? getComponent().getOutputType() : this.outputType;
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#setRequiredOutputType(String requiredOutputType)
     */
    @Override
    public void setRequiredOutputType(String requiredOutputType) {
        this.outputType = requiredOutputType;
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#isIdentity()
     */
    @Override
    public boolean isIdentity() {
        return "IDENTITY".equals(getRequiredInputType()) && "IDENTITY".equals(getRequiredOutputType()); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private IElementParameter getNodeElemForList(IElementParameter parTableNode) {
        // Iterate over the table columns and make sure one of them is a COLUMN_LIST or a CHECKBOX.
        for (Object nodeItemList : parTableNode.getListItemsValue()) {
            if (((IElementParameter) nodeItemList).getFieldType().equals(EParameterFieldType.PREV_COLUMN_LIST)
                    || ((IElementParameter) nodeItemList).getFieldType().equals(EParameterFieldType.COLUMN_LIST)
                    || ((IElementParameter) nodeItemList).getFieldType().equals(EParameterFieldType.CHECK)) {
                return (IElementParameter) nodeItemList;
            }
        }
        return null;
    }

    private IElementParameter getPartitionTableNode(IBigDataNode bigDataNode, String rootPartitionKey) {
        // if the partition key is valid, get the first element of the key, which must be a table.
        IElementParameter partitionTableNode = bigDataNode.getElementParameter(rootPartitionKey);
        if (partitionTableNode == null) {
            throw new RuntimeException("The parameter " + rootPartitionKey + " does not exist in the component " //$NON-NLS-1$ //$NON-NLS-2$
                    + this.getComponentName());
        }
        if (!partitionTableNode.getFieldType().equals(EParameterFieldType.TABLE)) {
            throw new UnsupportedOperationException("The parameter " + rootPartitionKey + " type is wrong. It should be a " //$NON-NLS-1$ //$NON-NLS-2$
                    + EParameterFieldType.TABLE + " parameter"); //$NON-NLS-1$
        }
        return partitionTableNode;
    }

    public void setPositiveKeyList(IBigDataNode bigDataNode, String direction) {
        // Get the partition key and make sure it's valid. The PARTITIONING parameter must be composed of two elements
        // (ELEMENT1.ELEMENT2)
        String[] partitionKey = bigDataNode.getComponent().getPartitioning().split("\\."); //$NON-NLS-1$
        this.keyList = new HashMap<String, List<IMetadataColumn>>();
        List<IMetadataColumn> columnList = new ArrayList<IMetadataColumn>();

        if (partitionKey.length > 1) {
            // if the partition key is valid, get the first element of the key, which must be a table.
            IElementParameter parTableNode = bigDataNode.getElementParameter(partitionKey[0]);
            if (parTableNode != null) {
                if (parTableNode.getFieldType().equals(EParameterFieldType.TABLE)) {
                    String clumnNodeListName = partitionKey[1];
                    IElementParameter nodeElemForList = getNodeElemForList(parTableNode);

                    if (nodeElemForList != null) {
                        // Iterate over the table entries and get the second element of the partition key from that
                        // table.
                        for (Map nodeColumnListMap : (List<Map>) parTableNode.getValue()) {
                            Object value = nodeColumnListMap.get(clumnNodeListName);
                            String colName = ""; //$NON-NLS-1$
                            if (value != null) {
                                if (value instanceof String || value instanceof Boolean) {
                                    if (parTableNode.isBasedOnSchema()) {
                                        boolean isKey = false;
                                        if (value instanceof String) {
                                            isKey = "true".equals(value); //$NON-NLS-1$
                                        } else if (value instanceof Boolean) {
                                            isKey = (Boolean) value;
                                        }
                                        // if the table content is based on schema, then we suppose that the columns
                                        // which compose the key are defined by another parameter, which must be a
                                        // checkbox.
                                        if (isKey) {
                                            // SCHEMA_COLUMN is the name of the column in a "based on schema" context.
                                            colName = (String) nodeColumnListMap.get("SCHEMA_COLUMN"); //$NON-NLS-1$
                                        } else {
                                            continue;
                                        }
                                    } else {
                                        // else, the value itself contains the name of the column.
                                        colName = (String) value;
                                    }
                                } else if (value instanceof Integer) {
                                    Integer index = (Integer) value;
                                    if (nodeElemForList.getListItemsDisplayName().length > index) {
                                        colName = nodeElemForList.getListItemsDisplayName()[index];
                                    }
                                }
                                if (bigDataNode.getIncomingConnections().size() > 0) {
                                    columnList.add(bigDataNode.getIncomingConnections().get(0).getMetadataTable()
                                            .getColumn(colName));
                                }
                            } else {
                                throw new RuntimeException(
                                        "The parameter " + partitionKey[0] + "." + partitionKey[1] + " does not exist in the component " + this.getComponentName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            }
                        }
                        this.keyList.put(direction, columnList);
                    }
                } else {
                    throw new UnsupportedOperationException(
                            "The parameter " + partitionKey[0] + " type is wrong. It should be a " //$NON-NLS-1$ //$NON-NLS-2$
                                    + EParameterFieldType.TABLE + " parameter"); //$NON-NLS-1$
                }
            } else {
                throw new RuntimeException("The parameter " + partitionKey[0] + " does not exist in the component " //$NON-NLS-1$ //$NON-NLS-2$
                        + this.getComponentName());
            }
        }
    }

    public void setNegativeKeyList(IBigDataNode bigDataNode, String direction) {
        this.keyList = new HashMap<String, List<IMetadataColumn>>();

        // Get the partition key and make sure it's valid. The PARTITIONING parameter must be composed of two elements
        // (ELEMENT1.ELEMENT2)
        String[] partitionKey = bigDataNode.getComponent().getPartitioning().split("\\."); //$NON-NLS-1$

        if (partitionKey.length > 1) {
            // remove the ! on the first partition key
            partitionKey[0] = partitionKey[0].substring(1);

            IElementParameter parTableNode = getPartitionTableNode(bigDataNode, partitionKey[0]);
            String columnNodeListName = partitionKey[1];

            // get the full list of metadatacolumn into columnList
            List<String> nameColumnList = new ArrayList<String>();
            for (IMetadataColumn column : getMetadataList().get(0).getListColumns()) {
                nameColumnList.add(column.getLabel());
            }

            // Iterate over the table entries and get the second element of the partition key from that
            // table.
            for (Map nodeColumnListMap : (List<Map>) parTableNode.getValue()) {
                Object value = nodeColumnListMap.get(columnNodeListName);
                if (value != null) {
                    nameColumnList.remove(value);
                } else {
                    throw new RuntimeException(
                            "The parameter " + partitionKey[0] + "." + partitionKey[1] + " does not exist in the component " + this.getComponentName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            }

            // Sort data to be sure to get them on the same order in input and output
            Collections.sort(nameColumnList);

            // Convert column's name into column
            List<IMetadataColumn> columnList = new ArrayList<IMetadataColumn>();
            for (String colName : nameColumnList) {
                columnList.add(bigDataNode.getMetadataList().get(0).getColumn(colName));
            }

            this.keyList.put(direction, columnList);
        }
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#setKeyList(IBigDataNode bigDataNode, String direction)
     */
    @Override
    public void setKeyList(IBigDataNode bigDataNode, String direction) {
        // The partitionning field can describe the key elements. But if it's start with a "!",
        // key elements are elements wich are not present on the described field.
        if (bigDataNode.getComponent().getPartitioning().startsWith("!")) { //$NON-NLS-1$
            if (getMetadataList().size() <= 0) {
                throw new RuntimeException("Please define a schema for " + this.getComponentName());//$NON-NLS-1$
            }
            setNegativeKeyList(bigDataNode, direction);
        } else {
            setPositiveKeyList(bigDataNode, direction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IBigDataNode#setKeyList(java.lang.String, java.util.List)
     */
    @Override
    public void setKeyList(String direction, List<IMetadataColumn> colList) {
        this.keyList.put(direction, colList);
    }

    /*
     * @see org.talend.core.model.process.IBigDataNode#getKeyList()
     */
    @Override
    public Map<String, List<IMetadataColumn>> getKeyList() {
        return keyList;
    }

    @Override
    public boolean isDummy() {
        return this.dummy;
    }

    @Override
    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    @Override
    public void addInput(IConnection connection) {
        ((List<IConnection>) getIncomingConnections()).add(connection);
    }

    @Override
    public void addOutput(IConnection connection) {
        ((List<IConnection>) getOutgoingConnections()).add(connection);
    }
}
