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

import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataColumn;

public interface IBigDataNode extends INode {

    /**
     * 
     * This method is used to get the OUTPUTTYPE of the previous component. If it doesn't exist, then recursively get
     * the OUTPUTTYPE of the previous components.
     * 
     * @return the OUTPUTTYPE of the previous component.
     */
    public String getIncomingType();

    /**
     * 
     * This method is used to get the INPUTTYPE of the next component. If it doesn't exist, then recursively get the
     * INPUTTYPE of the next components.
     * 
     * @return the INPUTTYPE of the next component.
     */
    public String getOutgoingType();

    /**
     * 
     * This method is used to get the INPUTTYPE of the current component.
     * 
     * @return the INPUTTYPE of the current component. Return null if it doesn't exist.
     */
    public String getRequiredInputType();

    /**
     * 
     * This method is used to get the OUTPUTTYPE of the current component.
     * 
     * @return the OUTPUTTYPE of the current component. Return null if it doesn't exist.
     */
    public String getRequiredOutputType();

    /**
     * 
     * Setter for the OUTPUTTYPE of the current component.
     * 
     * @param requiredOutputType
     */
    public void setRequiredOutputType(String requiredOutputType);

    /**
     * 
     * Setter for the INPUTTYPE of the current component.
     * 
     * @param requiredInputType
     */
    public void setRequiredInputType(String requiredInputType);

    /**
     * 
     * 
     * @return a boolean to define if the component is an IDENTITY component. A component is an IDENTITY component when
     * the INPUTTYPE and the OUTPUTTYPE are set to "IDENTITY". IDENTITY means that a component does't mind of the
     * incoming or outgoing type, but both must be the same. If the incoming type is a KEYVALUE, then the outgoing type
     * must be a KEYVALUE as well.
     */
    public boolean isIdentity();

    /**
     * Setter for the keyList parameter.
     * 
     * @param bigDataNode is the node in which we are going to lookup the PARTITIONING parameter in order to find the
     * parameter table that contains the list of columns which compose the key.
     * @param direction is a String parameter that takes 3 possible values (TODO: should be an Enum): "INPUT", "OUTPUT",
     * "BOTH".
     * <p>
     * <li>"INPUT" means that the bigDataNode is an upstream node for the current node.</li>
     * <li>"OUTPUT" means that the bigDataNode is a downstream node for the current node.
     * <li>"BOTH" means that the bigDataNode is the current node.
     * </p>
     */
    public void setKeyList(IBigDataNode bigDataNode, String direction);

    /**
     * Setter for the keyList parameter.
     * 
     * @param direction is a String parameter that takes 3 possible values (TODO: should be an Enum): "INPUT", "OUTPUT",
     * "BOTH".
     * <p>
     * <li>"INPUT" means that the bigDataNode is an upstream node for the current node.</li>
     * <li>"OUTPUT" means that the bigDataNode is a downstream node for the current node.
     * <li>"BOTH" means that the bigDataNode is the current node.
     * </p>
     * @param colList the list of columns which compose the key.
     */
    public void setKeyList(String direction, List<IMetadataColumn> colList);

    /**
     * Getter for the keyList parameter.
     * 
     * @return a Map where the key is the direction and the value is the column list.
     * @see setKeyList(IBigDataNode bigDataNode, String direction)
     */
    public Map<String, List<IMetadataColumn>> getKeyList();

    /**
     * Declares or not this node as a dummy. This is useful to distinguish 'real' nodes and the 'hidden' ones.
     * 
     * @param dummy true if this node has to be declared as a dummy, else false.
     */
    public void setDummy(boolean dummy);

}
