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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IMultipleComponentManager;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.utils.NodeUtil;
import org.talend.core.model.utils.ParameterValueUtil;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractNode.java 51166 2010-11-11 06:09:01Z wchen $
 * 
 */
public abstract class AbstractNode implements INode {

    private String componentName;

    private ComponentProperties componentProperties;

    List<? extends IElementParameter> elementParameters;

    private List<? extends IConnection> outgoingConnections = new ArrayList<IConnection>();

    private List<? extends IConnection> incomingConnections = new ArrayList<IConnection>();

    private List<IMetadataTable> metadataList;

    private String uniqueName;

    private boolean activate;

    private boolean start;

    private boolean subProcessStart;

    private IProcess process;

    private IComponent component;

    private boolean readOnly;

    private IExternalNode externalNode;

    private boolean hasConditionalOutputs = Boolean.FALSE;

    private boolean isMultiplyingOutputs = Boolean.FALSE;

    private List<BlockCode> blocksCodeToClose;

    private boolean isThereLinkWithHash;

    private boolean isThereLinkWithMerge;

    private Map<INode, Integer> mergeInfo;

    private String label;

    protected List<? extends INodeConnector> listConnector;

    private INode designSubjobStartNode;

    private boolean isVirtualGenerateNode;
    
    private INode realGraphicalNode;

    private EConnectionType virtualLinkTo;

    private String uniqueShortName;

    private boolean subProcessContainBreakpoint;

    private boolean isThereLinkWithRef = Boolean.FALSE;

    private List<INode> refNodes;

    private List<ModuleNeeded> modulesNeeded = new ArrayList<ModuleNeeded>();

    // for DI job, and indicate if the component after the iterator
    // connection which enable parallel, even this component after
    // onComponentOk
    private String parallelIterator = null;

    // as the talend job contains multiple mapreduce jobs, use this to indicate
    // which mapreduce job contains this
    // graphic node
    private Integer mrGroupId;

    // for the component which will generate multiple mapreduce jobs, count the
    // size of mapreduce jobs.
    private Integer mrJobInGroupCount;

    // for the component which will generate multiple mapreduce jobs
    private Integer mrJobIDInGroup;

    // indicate if this MR component will generate Reduce part
    private boolean mrContainsReduce;

    private boolean mapOnlyAfterReduce;

    // for MR, tag this component is the ref(lookup) start node
    private boolean isRefNode = false;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Override
    public List<? extends IElementParameter> getElementParameters() {
        return elementParameters;
    }

    @Override
    public void setElementParameters(List<? extends IElementParameter> elementParameters) {
        this.elementParameters = elementParameters;
    }

    public <T extends IElementParameter> void addElementParameter(T elementParameter) {
        ((List<T>) elementParameters).add(elementParameter);
    }

    @Override
    public List<? extends IConnection> getIncomingConnections() {
        return incomingConnections;
    }

    @Override
    public void setIncomingConnections(List<? extends IConnection> incomingConnections) {
        this.incomingConnections = incomingConnections;
    }

    @Override
    public List<? extends IConnection> getOutgoingConnections() {
        return outgoingConnections;
    }

    @Override
    public void setOutgoingConnections(List<? extends IConnection> outgoingConnections) {
        this.outgoingConnections = outgoingConnections;
    }

    @Override
    public List<IMetadataTable> getMetadataList() {
        return metadataList;
    }

    @Override
    public void setMetadataList(List<IMetadataTable> metadataList) {
        this.metadataList = metadataList;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        for (IElementParameter param : elementParameters) {
            if (param.getName().equals("UNIQUE_NAME")) { //$NON-NLS-1$
                param.setValue(uniqueName);
            }
        }
        this.uniqueName = uniqueName;
    }

    @Override
    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    @Override
    public boolean isStart() {
        return start;
    }

    @Override
    public void setStart(boolean start) {
        this.start = start;
    }

    @Override
    public boolean isSubProcessStart() {
        return subProcessStart;
    }

    public void setSubProcessStart(boolean subProcessStart) {
        this.subProcessStart = subProcessStart;
    }

    @Override
    public void setPerformanceData(String perfData) {
        // null
    }

    public void setTraceData(String traceData) {
        // null
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getReturns()
     */
    @Override
    public List<? extends INodeReturn> getReturns() {
        return new ArrayList<INodeReturn>();
    }

    @Override
    public IProcess getProcess() {
        return process;
    }

    @Override
    public void setProcess(IProcess process) {
        this.process = process;
    }

    @Override
    public void setComponent(IComponent component) {
        this.component = component;
    }

    @Override
    public IComponent getComponent() {
        return component;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isOnMainMergeBranch() {
        // return true even if got no branch.
        Map<INode, Integer> mapMerge = NodeUtil.getLinkedMergeInfo(this);
        if (mapMerge == null) {
            return true;
        } else {
            for (Integer i : mapMerge.values()) {
                if (i != 1) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Will return the first item of the subprocess. If "withCondition" is true, if there is links from type RunIf /
     * RunAfter / RunBefore, it will return the first element found. If "withCondition" is false, it will return the
     * first element with no active link from type Main/Ref/Iterate.<br>
     * <i><b>Note:</b></i> This function doesn't work if the node has several start points (will return a random start
     * node).
     * 
     * @param withCondition
     * @return Start Node found.
     */
    @Override
    public INode getSubProcessStartNode(boolean withConditions) {
        if (!withConditions) {
            Map<INode, Integer> mapMerge = NodeUtil.getLinkedMergeInfo(this);
            if (mapMerge == null) { // no merge after, so must be sub process
                                    // start.
                if ((getCurrentActiveLinksNbInput(EConnectionType.MAIN) == 0)) {
                    return this;
                }
            } else {
                for (Integer i : mapMerge.values()) {
                    if (i != 1) {
                        // not first merge, so will take the last merge from the
                        // tree, and retrieve the main sub
                        // process start.
                        return mapMerge.keySet().iterator().next().getSubProcessStartNode(withConditions);
                    }
                }
                if ((getCurrentActiveLinksNbInput(EConnectionType.MAIN) == 0)) {
                    return this; // main branch here, so we got the correct sub
                                 // process start.
                }
            }
        } else {
            int nb = 0;
            for (IConnection connection : getIncomingConnections()) {
                if (connection.isActivate()) {
                    nb++;
                }
            }
            if (nb == 0) {
                return this;
            }
        }

        for (IConnection connec : getIncomingConnections()) {
            if (((AbstractNode) connec.getSource()).isOnMainMergeBranch()) {
                if (!connec.getLineStyle().equals(EConnectionType.FLOW_REF)) {
                    return connec.getSource().getSubProcessStartNode(withConditions);
                }
            }
        }
        return null;
    }

    protected int getCurrentActiveLinksNbInput(int connectionCategory) {
        int nb = 0;
        for (IConnection connection : getIncomingConnections()) {
            if (connection.isActivate() && connection.getLineStyle().hasConnectionCategory(connectionCategory)) {
                nb++;
            }
        }
        return nb;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#hasConditionnalOutputs()
     */
    @Override
    public boolean hasConditionalOutputs() {
        return this.hasConditionalOutputs;
    }

    /**
     * Sets the hasConditionnalOutputs.
     * 
     * @param hasConditionalOutputs the hasConditionnalOutputs to set
     */
    public void setHasConditionalOutputs(boolean hasConditionalOutputs) {
        this.hasConditionalOutputs = hasConditionalOutputs;
    }

    /**
     * Getter for isMultiplyingOutputs.
     * 
     * @return the isMultiplyingOutputs
     */
    @Override
    public boolean isMultiplyingOutputs() {
        return isMultiplyingOutputs;
    }

    /**
     * Sets the isMultiplyingOutputs.
     * 
     * @param isMultiplyingOutputs the isMultiplyingOutputs to set
     */
    public void setIsMultiplyingOutputs(Boolean isMultiplyingOutputs) {
        this.isMultiplyingOutputs = isMultiplyingOutputs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(uniqueName + " - "); //$NON-NLS-1$
        buff.append("inputs:("); //$NON-NLS-1$
        for (int i = 0; i < incomingConnections.size(); i++) {
            buff.append(incomingConnections.get(i).getName());
            if (i < (incomingConnections.size() - 1)) {
                buff.append(","); //$NON-NLS-1$
            }
        }
        buff.append(") "); //$NON-NLS-1$
        buff.append("outputs:("); //$NON-NLS-1$
        for (int i = 0; i < outgoingConnections.size(); i++) {
            buff.append(outgoingConnections.get(i).getName());
            if (i < (outgoingConnections.size() - 1)) {
                buff.append(","); //$NON-NLS-1$
            }
        }
        buff.append(")"); //$NON-NLS-1$
        return buff.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getLocation()
     */
    public Object getLocation() {
        return null;
    }

    @Override
    public IExternalNode getExternalNode() {
        return externalNode;
    }

    public void setExternalNode(IExternalNode externalNode) {
        this.externalNode = externalNode;
    }

    /**
     * Getter for isThereLinkWithHash.
     * 
     * @return the isThereLinkWithHash
     */
    @Override
    public boolean isThereLinkWithHash() {
        return isThereLinkWithHash;
    }

    /**
     * Sets the isThereLinkWithHash.
     * 
     * @param isThereLinkWithHash the isThereLinkWithHash to set
     */
    public void setThereLinkWithHash(boolean isThereLinkWithHash) {
        this.isThereLinkWithHash = isThereLinkWithHash;
    }

    @Override
    public IElementParameter getElementParameter(String name) {
        if (name.contains(":")) { // look for the parent first, then will //$NON-NLS-1$
            // retrieve the children
            StringTokenizer token = new StringTokenizer(name, ":"); //$NON-NLS-1$
            String parentId = token.nextToken();
            String childId = token.nextToken();
            for (int i = 0; i < elementParameters.size(); i++) {
                if (elementParameters.get(i).getName().equals(parentId)) {
                    IElementParameter parent = elementParameters.get(i);
                    return parent.getChildParameters().get(childId);
                }
            }
        } else {
            for (IElementParameter elementParam : elementParameters) {
                if (elementParam.getName().equals(name)) {
                    return elementParam;
                }
            }
        }

        // if not found, look for the name if it's the name of a children
        // this code is added only for compatibility and will be executed only
        // one time
        // to initialize the child.
        // The parameters name are unique, so we just take the first one.
        for (IElementParameter elementParam : elementParameters) {
            Map<String, IElementParameter> childParameters = elementParam.getChildParameters();
            if (childParameters == null) {
                continue;
            }
            for (String key : childParameters.keySet()) {
                IElementParameter param = childParameters.get(key);
                if (param.getName().equals(name)) {
                    return param;
                }
            }
        }
        return null;
    }

    @Override
    public List<? extends IConnection> getOutgoingSortedConnections() {
        return org.talend.core.model.utils.NodeUtil.getOutgoingSortedConnections(this);
    }

    @Override
    public List<? extends IConnection> getOutgoingCamelSortedConnections() {
        return org.talend.core.model.utils.NodeUtil.getOutgoingCamelSortedConnections(this);
    }

    @Override
    public List<? extends IConnection> getMainOutgoingConnections() {
        return org.talend.core.model.utils.NodeUtil.getMainOutgoingConnections(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingConnections(org.talend .core.model.process.EConnectionType)
     */
    @Override
    public List<? extends IConnection> getOutgoingConnections(EConnectionType connectionType) {
        return org.talend.core.model.utils.NodeUtil.getOutgoingConnections(this, connectionType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingConnections(java.lang. String)
     */
    @Override
    public List<? extends IConnection> getOutgoingConnections(String connectorName) {
        return org.talend.core.model.utils.NodeUtil.getOutgoingConnections(this, connectorName);
    }

    @Override
    public List<BlockCode> getBlocksCodeToClose() {
        return this.blocksCodeToClose;
    }

    public void setBlocksCodeToClose(List<BlockCode> blockCodesToClose) {
        this.blocksCodeToClose = blockCodesToClose;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameData(java.lang.String, java.lang.String)
     */
    @Override
    public void renameData(String oldName, String newName) {
        if (oldName.equals(newName)) {
            return;
        }

        for (IElementParameter param : this.getElementParametersWithChildrens()) {
            if (param.getName().equals("UNIQUE_NAME") || isSQLQueryParameter(param) || isTDMParameter(param)) { //$NON-NLS-1$
                continue;
            }
            ParameterValueUtil.renameValues(param, oldName, newName);
        }
    }

    /**
     * see bug 4733
     * <p>
     * DOC YeXiaowei Comment method "isSQLQueryParameter".
     * 
     * @param parameter
     * @return
     */
    private boolean isSQLQueryParameter(final IElementParameter parameter) {
        return parameter.getFieldType().equals(EParameterFieldType.MEMO_SQL) && parameter.getName().equals("QUERY"); //$NON-NLS-1$
    }

    /**
     * bug TDM-409
     * <p>
     * DOC hwang Comment method "isTDMParameter".
     * 
     * @param parameter
     * @return
     */
    private boolean isTDMParameter(final IElementParameter parameter) {

        return parameter.getFieldType().equals(EParameterFieldType.HMAP_PATH)
                && parameter.getName().equals(EParameterFieldType.HMAP_PATH.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#useData(java.lang.String)
     */
    @Override
    public boolean useData(String name) {

        for (IElementParameter param : this.getElementParametersWithChildrens()) {
            if (param.getFieldType() == EParameterFieldType.IMAGE) {
                continue;
            }
            if (param.getName().equals("UNIQUE_NAME")) { //$NON-NLS-1$
                continue;
            }
            if (ParameterValueUtil.isUseData(param, name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<INode, Integer> getLinkedMergeInfo() {
        return mergeInfo;
    }

    public void setLinkedMergeInfo(Map<INode, Integer> mergeInfo) {
        this.mergeInfo = mergeInfo;
    }

    @Override
    public boolean isThereLinkWithMerge() {
        return isThereLinkWithMerge;
    }

    public void setThereLinkWithMerge(boolean isThereLinkWithHash) {
        this.isThereLinkWithMerge = isThereLinkWithHash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getIncomingConnections(org.talend .core.model.process.EConnectionType)
     */
    @Override
    public List<? extends IConnection> getIncomingConnections(EConnectionType connectionType) {
        return org.talend.core.model.utils.NodeUtil.getIncomingConnections(this, connectionType);
    }

    public List<? extends IConnection> getIncomingConnections(String connectorName) {
        return org.talend.core.model.utils.NodeUtil.getIncomingConnections(this, connectorName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getLabel()
     */
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public IMetadataTable getMetadataFromConnector(String connector) {
        if (metadataList == null) {
            return null;
        }
        for (IMetadataTable table : metadataList) {
            if (table.getAttachedConnector().equals(connector)) {
                return table;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getConnectorFromName(java.lang.String )
     */
    @Override
    public INodeConnector getConnectorFromName(String connName) {
        INodeConnector nodeConnector = null;
        int nbConn = 0;

        while ((nodeConnector == null) && (nbConn < listConnector.size())) {
            INodeConnector connector = listConnector.get(nbConn);
            if (connector.getName().equals(connName) && connector.isShow()) {
                nodeConnector = listConnector.get(nbConn);
            }
            nbConn++;
        }
        return nodeConnector;
    }

    @Override
    public List<INodeConnector> getConnectorsFromType(final EConnectionType connType) {
        INodeConnector nodeConnector = null;
        List<INodeConnector> listConnectors = new ArrayList<INodeConnector>();
        int nbConn = 0;

        EConnectionType testedType;

        if (connType.hasConnectionCategory(IConnectionCategory.FLOW)) {
            testedType = EConnectionType.FLOW_MAIN;
        } else {
            testedType = connType;
        }

        while (nbConn < listConnector.size()) {
            INodeConnector connector = listConnector.get(nbConn);
            if (connector.getDefaultConnectionType() == testedType && connector.isShow()) {
                nodeConnector = listConnector.get(nbConn);
                listConnectors.add(nodeConnector);
            }
            nbConn++;
        }

        return listConnectors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#reloadComponent(org.talend.core.model .components.IComponent,
     * java.util.Map)
     */
    @Override
    public void reloadComponent(IComponent component, Map<String, Object> parameters, boolean isUpdate) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getDesignSubjobStart()
     */
    @Override
    public INode getDesignSubjobStartNode() {
        if (designSubjobStartNode == null) {
            // used for process without design.
            // TODO: can we cache the data directly
            return getSubProcessStartNode(false);
        }
        return designSubjobStartNode;
    }

    /**
     * Sets the designSubjobStart.
     * 
     * @param designSubjobStart the designSubjobStart to set
     */
    public void setDesignSubjobStartNode(INode designSubjobStartNode) {
        this.designSubjobStartNode = designSubjobStartNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isDesignSubjobStartNode()
     */
    @Override
    public boolean isDesignSubjobStartNode() {
        if (designSubjobStartNode == null) {
            return this.equals(getSubProcessStartNode(false));
        }
        return this.equals(designSubjobStartNode);
    }

    @Override
    public boolean isVirtualGenerateNode() {
        return this.isVirtualGenerateNode;
    }

    public boolean setVirtualGenerateNode(boolean isVirtualGenerateNode) {
        return this.isVirtualGenerateNode = isVirtualGenerateNode;
    }

    /**
     * Sets the listConnector.
     * 
     * @param listConnector the listConnector to set
     */
    public void setListConnector(List<? extends INodeConnector> listConnector) {
        this.listConnector = listConnector;
    }

    @Override
    public List<? extends IElementParameter> getElementParametersWithChildrens() {
        if (this.elementParameters == null) {
            return new ArrayList<IElementParameter>();
        }
        List<IElementParameter> fullListParam = new ArrayList<IElementParameter>(this.elementParameters);

        for (IElementParameter curParam : elementParameters) {
            if (curParam.getChildParameters() != null) {
                for (String key : curParam.getChildParameters().keySet()) {
                    IElementParameter childParam = curParam.getChildParameters().get(key);
                    fullListParam.add(childParam);
                }
            }
        }
        return fullListParam;
    }

    @Override
    public boolean isGeneratedAsVirtualComponent() {
        IElementParameter param = getElementParameter("IS_VIRTUAL_COMPONENT"); //$NON-NLS-1$
        if (param != null) { // now only available for tUniqRow.
            return ((Boolean) param.getValue() && param.isRequired(elementParameters));
        }
        List<IMultipleComponentManager> multipleComponentManagers = getComponent().getMultipleComponentManagers();
        for (IMultipleComponentManager mcm : multipleComponentManagers) {
            if (!mcm.isLookupMode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EConnectionType getVirtualLinkTo() {
        return this.virtualLinkTo;
    }

    @Override
    public void setVirtualLinkTo(EConnectionType virtualLinkTo) {
        this.virtualLinkTo = virtualLinkTo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isELTComponent()
     */
    @Override
    public boolean isELTComponent() {
        if (getComponent() != null) {
            return getComponent().getOriginalFamilyName().startsWith("ELT"); //$NON-NLS-1$
        }
        return false;
    }

    @Override
    public boolean isFileScaleComponent() {
        return getComponent().getOriginalFamilyName().equals("FileScale"); //$NON-NLS-1$
    }

    @Override
    public boolean isUseLoopOnConditionalOutput(String outputName) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getUniqueShortName()
     */
    @Override
    public String getUniqueShortName() {
        return this.uniqueShortName;
    }

    /**
     * Sets the uniqueShortName.
     * 
     * @param uniqueShortName the uniqueShortName to set
     */
    public void setUniqueShortName(String uniqueShortName) {
        this.uniqueShortName = uniqueShortName;
    }

    public void setSubProcessContainTraceBreakpoint(boolean subProcessContainBreakpoint) {
        this.subProcessContainBreakpoint = subProcessContainBreakpoint;
    }

    @Override
    public boolean isSubProcessContainTraceBreakpoint() {
        return subProcessContainBreakpoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#getElementName()
     */
    @Override
    public String getElementName() {
        return this.getUniqueName();
    }

    @Override
    public void addInput(IConnection connection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addOutput(IConnection connection) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean checkIfCanBeStart() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void checkNode() {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<INode> fsComponentsInProgressBar() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public INodeConnector getConnectorFromType(EConnectionType lineStyle) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IExternalData getExternalData() {
        if (externalNode != null) {
            return externalNode.getExternalData();
        }
        return null;
    }

    @Override
    public List<? extends INodeConnector> getListConnector() {
        return listConnector;
    }

    @Override
    public IMetadataTable getMetadataTable(String metaName) {
        for (int i = 0; i < metadataList.size(); i++) {
            String tableName = metadataList.get(i).getTableName();
            if (tableName != null && tableName.equals(metaName)) {
                return metadataList.get(i);
            }
        }
        return null;
    }

    @Override
    public INode getProcessStartNode(boolean processStartNode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getSchemaParameterFromConnector(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getSize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasRunIfLink() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isDummy() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isExternalNode() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGeneratedByJobscriptBool() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTemplate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        // TODO Auto-generated method stub

    }

    @Override
    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeInput(IConnection connection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeOutput(IConnection connection) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean sameProcessAs(INode target, boolean withConditions) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setLocation(Object location) {
        // TODO Auto-generated method stub

    }

    public void setSize(Object size) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getPropertyValue(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPropertyValue(String name, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public IElementParameter getElementParameterFromField(EParameterFieldType propertyType, EComponentCategory category) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IElementParameter getElementParameterFromField(EParameterFieldType fieldType) {
        for (IElementParameter elementParam : getElementParameters()) {
            if (elementParam.getFieldType().equals(fieldType)) {
                return elementParam;
            }
        }
        return null;
    }

    @Override
    public Object getPropertyValue(String name, String paramName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLabel(String label) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getPosX() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getPosY() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public INode getJobletNode() {
        return null;
    }

    @Override
    public INode getJunitNode() {
        return null;
    }

    @Override
    public List<ModuleNeeded> getModulesNeeded() {
        if (modulesNeeded.isEmpty() && component != null && component.getModulesNeeded(this) != null) {
            // if the list is empty, initialize from the original component
            // this avoids complex refactor to initialize this list all the
            // time, and add the possibility to add more
            // modules needed to one original component

            modulesNeeded.addAll(component.getModulesNeeded(this));
        }
        return modulesNeeded;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isSubtreeStart()
     */
    @Override
    public boolean isSubtreeStart() {
        return isDesignSubjobStartNode() && !isThereLinkWithHash();
    }

    /**
     * Getter for mrGroupId.
     * 
     * @return the mrGroupId
     */
    public Integer getMRGroupId() {
        return mrGroupId;
    }

    /**
     * Sets the mrGroupId.
     * 
     * @param mrGroupId the mrGroupId to set
     */
    public void setMRGroupId(Integer mrGroupId) {
        this.mrGroupId = mrGroupId;
    }

    /**
     * Getter for mrJobInGroupCount.
     * 
     * @return the mrJobInGroupCount
     */
    public Integer getMRJobInGroupCount() {
        return mrJobInGroupCount;
    }

    /**
     * Sets the mrJobInGroupCount.
     * 
     * @param mrJobInGroupCount the mrJobInGroupCount to set
     */
    public void setMRJobInGroupCount(Integer mrJobInGroupCount) {
        this.mrJobInGroupCount = mrJobInGroupCount;
    }

    /**
     * Getter for mrJobIDInGroup.
     * 
     * @return the mrJobIDInGroup
     */
    public Integer getMrJobIDInGroup() {
        return mrJobIDInGroup;
    }

    /**
     * Sets the mrJobIDInGroup.
     * 
     * @param mrJobIDInGroup the mrJobIDInGroup to set
     */
    public void setMrJobIDInGroup(Integer mrJobIDInGroup) {
        this.mrJobIDInGroup = mrJobIDInGroup;
    }

    /**
     * Getter for mrContainsReduce.
     * 
     * @return the mrContainsReduce
     */
    public boolean isMrContainsReduce() {
        return this.mrContainsReduce;
    }

    /**
     * Sets the mrContainsReduce.
     * 
     * @param mrContainsReduce the mrContainsReduce to set
     */
    public void setMrContainsReduce(boolean mrContainsReduce) {
        this.mrContainsReduce = mrContainsReduce;
    }

    /**
     * Getter for isThereLinkWithRef.
     * 
     * @return the isThereLinkWithRef
     */
    public boolean isThereLinkWithRef() {
        return this.isThereLinkWithRef;
    }

    /**
     * Sets the isThereLinkWithRef.
     * 
     * @param isThereLinkWithRef the isThereLinkWithRef to set
     */
    public void setThereLinkWithRef(boolean isThereLinkWithRef) {
        this.isThereLinkWithRef = isThereLinkWithRef;
    }

    /**
     * Getter for refNodes.
     * 
     * @return the refNodes
     */
    public List<INode> getRefNodes() {
        return this.refNodes;
    }

    /**
     * Sets the refNodes.
     * 
     * @param refNodes the refNodes to set
     */
    public void setRefNodes(List<INode> refNodes) {
        this.refNodes = refNodes;
    }

    /**
     * Getter for mapOnlyAfterReduce.
     * 
     * @return the mapOnlyAfterReduce
     */
    public boolean isMapOnlyAfterReduce() {
        return this.mapOnlyAfterReduce;
    }

    /**
     * Sets the mapOnlyAfterReduce.
     * 
     * @param mapOnlyAfterReduce the mapOnlyAfterReduce to set
     */
    public void setMapOnlyAfterReduce(boolean mapOnlyAfterReduce) {
        this.mapOnlyAfterReduce = mapOnlyAfterReduce;
    }

    /**
     * Getter for isRefNode.
     * 
     * @return the isRefNode
     */
    public boolean isRefNode() {
        return this.isRefNode;
    }

    /**
     * Sets the isRefNode.
     * 
     * @param isRefNode the isRefNode to set
     */
    public void setRefNode(boolean isRefNode) {
        this.isRefNode = isRefNode;
    }

    /**
     * Getter for parallelIterator.
     * 
     * @return the parallelIterator
     */
    public String getParallelIterator() {
        return parallelIterator;
    }

    /**
     * Sets the parallelIterator.
     * 
     * @param parallelIterator the parallelIterator to set
     */
    public void setParallelIterator(String parallelIterator) {
        this.parallelIterator = parallelIterator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#isForceReadOnly()
     */
    @Override
    public boolean isForceReadOnly() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#setForceReadOnly(boolean)
     */
    @Override
    public void setForceReadOnly(boolean readOnly) {

    }

    @Override
    public void setComponentProperties(ComponentProperties props) {
        componentProperties = props;
    }

    @Override
    public ComponentProperties getComponentProperties() {
        return componentProperties;
    }

    public INode getRealGraphicalNode() {
        return realGraphicalNode;
    }
  
    public void setRealGraphicalNode(INode realGraphicalNode) {
        this.realGraphicalNode = realGraphicalNode;
    }
}
