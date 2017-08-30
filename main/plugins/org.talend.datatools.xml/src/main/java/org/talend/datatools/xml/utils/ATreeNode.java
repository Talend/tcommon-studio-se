/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.talend.datatools.xml.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * The instance of this class is used as tree node of the xml schema tree that is passed to gui.
 */
public class ATreeNode {

    //
    public static final int ELEMENT_TYPE = 1;

    public static final int ATTRIBUTE_TYPE = 2;

    public static final int OTHER_TYPE = 0;

    public static final int NAMESPACE_TYPE = 3;

    // The value of certain tree node.
    private Object value;

    // The children list of certain tree node.
    private ArrayList children;

    // The parent of tree node.
    private ATreeNode parent;

    private String currentNamespace;

    // The type of the tree node, may either attribute or element.
    private int type;

    private List<String> uniqueNames = new ArrayList<String>();

    // The data type is the complex type that defined in an xsd file.
    private String dataType, originalDataType;

    private String label;

    private boolean isSubstitution = false;

    private boolean isChoice = false;

    private boolean isOptional = false;

    private long dataMaxLength = -1;

    private long precisionValue;

    private static HashMap xmlTypeToDataType = new HashMap();

    private Object foxTreeNode;

    static {
        try {
            xmlTypeToDataType.put("string", DataTypes.getTypeString(DataTypes.STRING));
            xmlTypeToDataType.put("byte", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("decimal", DataTypes.getTypeString(DataTypes.BIGDECIMAL));
            xmlTypeToDataType.put("double", DataTypes.getTypeString(DataTypes.DOUBLE));
            xmlTypeToDataType.put("float", DataTypes.getTypeString(DataTypes.DOUBLE));
            xmlTypeToDataType.put("int", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("integer", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("negativeInteger", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("nonNegativeInteger", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("nonPositiveInteger", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("positiveInteger", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("short", DataTypes.getTypeString(DataTypes.INT));
            xmlTypeToDataType.put("date", DataTypes.getTypeString(DataTypes.DATE));
            xmlTypeToDataType.put("dateTime", DataTypes.getTypeString(DataTypes.TIMESTAMP));
            xmlTypeToDataType.put("time", DataTypes.getTypeString(DataTypes.TIME));
        } catch (OdaException e) {
            // Should not arrive here
        }
    }

    private static String getDataType(String type) throws OdaException {
        Object result = xmlTypeToDataType.get(type);
        if (result == null) {
            return type;
        } else {
            return result.toString();
        }

    }

    /**
	 * 
	 *
	 */
    protected ATreeNode() {
        children = new ArrayList();
        if (NodeCreationObserver.isRunning()) {
            NodeCreationObserver.add(this);
        }
    }

    public ATreeNode copy() {
        ATreeNode copyNode = new ATreeNode();
        // don't copy parents and foxTreeNode, need to reset.
        copyNode.setChoice(isChoice);
        copyNode.setCurrentNamespace(currentNamespace);
        copyNode.setDataMaxLength(dataMaxLength);
        copyNode.dataType = dataType;
        copyNode.originalDataType = originalDataType;
        copyNode.setLabel(label);
        copyNode.setOptional(isOptional);
        copyNode.setPrecisionValue(precisionValue);
        copyNode.setSubstitution(isSubstitution);
        copyNode.setType(type);
        copyNode.setValue(value);

        return copyNode;
    }

    /**
     * Return the value of tree node.
     * 
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * Return the children of tree node.
     * 
     * @return
     */
    public Object[] getChildren() {
        // sort children with order namespace/attribute/element
        Collections.sort(children, new Comparator() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof ATreeNode && o2 instanceof ATreeNode) {
                    ATreeNode node1 = (ATreeNode) o1;
                    ATreeNode node2 = (ATreeNode) o2;
                    return node2.getType() - node1.getType();
                }
                return 0;
            }
        });

        return children.toArray();
    }

    /**
     * Return the parent of tree node.
     * 
     * @return
     */
    public ATreeNode getParent() {
        return parent;
    }

    /**
     * Set the value to be held by this tree node.
     * 
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Add a child to the tree node.
     * 
     * @param child
     */
    public void addChild(Object child) {
        this.children.add(child);
        ((ATreeNode) child).setNodeParent(this);
    }

    /**
     * Add a as first child to the tree node.
     * 
     * @param child
     */
    public void addAsFirstChild(Object child) {
        this.children.add(0, child);
        ((ATreeNode) child).setNodeParent(this);
    }

    /**
     * Add a group of child to the tree node.
     * 
     * @param children
     */
    public void addChild(Object[] children) {
        for (Object element : children) {
            addChild(element);
        }
    }

    public void removeChild(Object child) {
        this.children.remove(child);
    }

    public void removeChild(Object[] children) {
        this.children.remove(children);
    }

    public void removeAllChildren() {
        this.children.clear();
    }

    public void setNodeParent(ATreeNode parent) {
        this.parent = parent;
    }

    /**
     * Set the parent of the tree node.
     * 
     * @param parent
     */
    public void setParent(ATreeNode parent) {
        this.parent = parent;
        // parent.addChild(this);
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Return the type of tree node.
     * 
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * Set the type of tree node ( either attribute or element)
     * 
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Return the data type of tree node. The data type is the complex type that defined in an xsd file.
     * 
     * @return
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Set the data type of tree node ( either attribute or element)
     * 
     * @param type
     * @throws OdaException
     */
    public void setDataType(String type) throws OdaException {
        originalDataType = new String(type);
        this.dataType = getDataType(type);
    }

    /**
     * Return the data type of tree node. The data type is the complex type that defined in an xsd file.
     * 
     * @return
     */
    public String getOriginalDataType() {
        return originalDataType;
    }

    public List<String> getUniqueNames() {
        return this.uniqueNames;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.dataType == null) ? 0 : this.dataType.hashCode());
        result = prime * result + ((this.originalDataType == null) ? 0 : this.originalDataType.hashCode());
        result = prime * result + this.type;
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        // if (this.children != null) {
        // for (Object node : this.children) {
        // result = prime * result + this.type;
        // }
        // }
        // result = prime * result + ((this.children == null) ? 0 : this.children.);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return equals(obj, 0);
    }

    public boolean equals(Object obj, int depth) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ATreeNode other = (ATreeNode) obj;
        if (this.dataType == null) {
            if (other.dataType != null) {
                return false;
            }
        } else if (!this.dataType.equals(other.dataType)) {
            return false;
        }
        if (this.originalDataType == null) {
            if (other.originalDataType != null) {
                return false;
            }
        } else if (!this.originalDataType.equals(other.originalDataType)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        if (this.parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else {
            if (depth > 50) {
                return true;
            }
            if (!this.parent.equals(other.parent, depth + 1)) {
                return false;
            }
        }
        return true;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    public String getLabel() {
        if (this.label == null) {
            return this.getValue().toString();
        }
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSubstitution() {
        return this.isSubstitution;
    }

    public void setSubstitution(boolean isSubstitution) {
        this.isSubstitution = isSubstitution;
    }

    public boolean isChoice() {
        return this.isChoice;
    }

    public void setChoice(boolean isChoice) {
        this.isChoice = isChoice;
    }

    public boolean isOptional() {
        return this.isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public long getDataMaxLength() {
        return this.dataMaxLength;
    }

    public void setDataMaxLength(long dataMaxLength) {
        this.dataMaxLength = dataMaxLength;
    }

    public long getPrecisionValue() {
        return this.precisionValue;
    }

    public void setPrecisionValue(long precisionValue) {
        this.precisionValue = precisionValue;
    }

    public Object getFoxTreeNode() {
        return foxTreeNode;
    }

    public void setFoxTreeNode(Object foxTreeNode) {
        this.foxTreeNode = foxTreeNode;
    }

}
