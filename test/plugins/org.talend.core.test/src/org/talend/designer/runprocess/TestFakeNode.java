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
package org.talend.designer.runprocess;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.BlockCode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class TestFakeNode extends Element implements INode {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#isReadOnly()
     */
    @Override
    public boolean isReadOnly() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#setReadOnly(boolean)
     */
    @Override
    public void setReadOnly(boolean readOnly) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getLabel()
     */
    @Override
    public String getLabel() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getUniqueName()
     */
    @Override
    public String getUniqueName() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getUniqueShortName()
     */
    @Override
    public String getUniqueShortName() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isStart()
     */
    @Override
    public boolean isStart() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isActivate()
     */
    @Override
    public boolean isActivate() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isSubProcessStart()
     */
    @Override
    public boolean isSubProcessStart() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isSubProcessContainTraceBreakpoint()
     */
    @Override
    public boolean isSubProcessContainTraceBreakpoint() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getIncomingConnections()
     */
    @Override
    public List<? extends IConnection> getIncomingConnections() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingConnections()
     */
    @Override
    public List<? extends IConnection> getOutgoingConnections() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#hasConditionalOutputs()
     */
    @Override
    public boolean hasConditionalOutputs() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getBlocksCodeToClose()
     */
    @Override
    public List<BlockCode> getBlocksCodeToClose() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isMultiplyingOutputs()
     */
    @Override
    public boolean isMultiplyingOutputs() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setPerformanceData(java.lang.String)
     */
    @Override
    public void setPerformanceData(String perfData) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getMetadataList()
     */
    @Override
    public List<IMetadataTable> getMetadataList() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getReturns()
     */
    @Override
    public List<? extends INodeReturn> getReturns() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getProcess()
     */
    @Override
    public IProcess getProcess() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setProcess(org.talend.core.model.process.IProcess)
     */
    @Override
    public void setProcess(IProcess process) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getComponent()
     */
    @Override
    public IComponent getComponent() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setComponent(org.talend.core.model.components.IComponent)
     */
    @Override
    public void setComponent(IComponent component) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getExternalNode()
     */
    @Override
    public IExternalNode getExternalNode() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#metadataInputChanged(org.talend.core.model.components.IODataComponent,
     * java.lang.String)
     */
    @Override
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#metadataOutputChanged(org.talend.core.model.components.IODataComponent,
     * java.lang.String)
     */
    @Override
    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getSubProcessStartNode(boolean)
     */
    @Override
    public INode getSubProcessStartNode(boolean withConditions) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#useData(java.lang.String)
     */
    @Override
    public boolean useData(String name) {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameData(java.lang.String, java.lang.String)
     */
    @Override
    public void renameData(String oldName, String newName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isThereLinkWithHash()
     */
    @Override
    public boolean isThereLinkWithHash() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingSortedConnections()
     */
    @Override
    public List<? extends IConnection> getOutgoingSortedConnections() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingCamelSortedConnections()
     */
    @Override
    public List<? extends IConnection> getOutgoingCamelSortedConnections() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getMainOutgoingConnections()
     */
    @Override
    public List<? extends IConnection> getMainOutgoingConnections() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingConnections(org.talend.core.model.process.EConnectionType)
     */
    @Override
    public List<? extends IConnection> getOutgoingConnections(EConnectionType connectionType) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getIncomingConnections(org.talend.core.model.process.EConnectionType)
     */
    @Override
    public List<? extends IConnection> getIncomingConnections(EConnectionType connectionType) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingConnections(java.lang.String)
     */
    @Override
    public List<? extends IConnection> getOutgoingConnections(String connectorName) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getLinkedMergeInfo()
     */
    @Override
    public Map<INode, Integer> getLinkedMergeInfo() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isThereLinkWithMerge()
     */
    @Override
    public boolean isThereLinkWithMerge() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getMetadataFromConnector(java.lang.String)
     */
    @Override
    public IMetadataTable getMetadataFromConnector(String connector) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getConnectorFromName(java.lang.String)
     */
    @Override
    public INodeConnector getConnectorFromName(String connector) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#reloadComponent(org.talend.core.model.components.IComponent,
     * java.util.Map, boolean)
     */
    @Override
    public void reloadComponent(IComponent component, Map<String, Object> parameters, boolean isUpdate) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getDesignSubjobStartNode()
     */
    @Override
    public INode getDesignSubjobStartNode() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isDesignSubjobStartNode()
     */
    @Override
    public boolean isDesignSubjobStartNode() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isVirtualGenerateNode()
     */
    @Override
    public boolean isVirtualGenerateNode() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getVirtualLinkTo()
     */
    @Override
    public EConnectionType getVirtualLinkTo() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setVirtualLinkTo(org.talend.core.model.process.EConnectionType)
     */
    @Override
    public void setVirtualLinkTo(EConnectionType virtualLinkTo) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isGeneratedAsVirtualComponent()
     */
    @Override
    public boolean isGeneratedAsVirtualComponent() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isELTComponent()
     */
    @Override
    public boolean isELTComponent() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isUseLoopOnConditionalOutput(java.lang.String)
     */
    @Override
    public boolean isUseLoopOnConditionalOutput(String outputName) {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getExternalData()
     */
    @Override
    public IExternalData getExternalData() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getListConnector()
     */
    @Override
    public List<? extends INodeConnector> getListConnector() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isDummy()
     */
    @Override
    public boolean isDummy() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#fsComponentsInProgressBar()
     */
    @Override
    public Set<INode> fsComponentsInProgressBar() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isExternalNode()
     */
    @Override
    public boolean isExternalNode() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#addOutput(org.talend.core.model.process.IConnection)
     */
    @Override
    public void addOutput(IConnection connection) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#addInput(org.talend.core.model.process.IConnection)
     */
    @Override
    public void addInput(IConnection connection) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isTemplate()
     */
    @Override
    public boolean isTemplate() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isGeneratedByJobscriptBool()
     */
    @Override
    public boolean isGeneratedByJobscriptBool() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#removeOutput(org.talend.core.model.process.IConnection)
     */
    @Override
    public void removeOutput(IConnection connection) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#removeInput(org.talend.core.model.process.IConnection)
     */
    @Override
    public void removeInput(IConnection connection) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getMetadataTable(java.lang.String)
     */
    @Override
    public IMetadataTable getMetadataTable(String metaName) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getConnectorFromType(org.talend.core.model.process.EConnectionType)
     */
    @Override
    public INodeConnector getConnectorFromType(EConnectionType lineStyle) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#checkIfCanBeStart()
     */
    @Override
    public boolean checkIfCanBeStart() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setStart(boolean)
     */
    @Override
    public void setStart(boolean checkIfCanBeStart) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#checkNode()
     */
    @Override
    public void checkNode() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getSchemaParameterFromConnector(java.lang.String)
     */
    @Override
    public Object getSchemaParameterFromConnector(String name) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#hasRunIfLink()
     */
    @Override
    public boolean hasRunIfLink() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setMetadataList(java.util.List)
     */
    @Override
    public void setMetadataList(List<IMetadataTable> metadataList) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setOutgoingConnections(java.util.List)
     */
    @Override
    public void setOutgoingConnections(List<? extends IConnection> outgoingConnections) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setIncomingConnections(java.util.List)
     */
    @Override
    public void setIncomingConnections(List<? extends IConnection> incomingConnections) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getProcessStartNode(boolean)
     */
    @Override
    public INode getProcessStartNode(boolean processStartNode) {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isFileScaleComponent()
     */
    @Override
    public boolean isFileScaleComponent() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#sameProcessAs(org.talend.core.model.process.INode, boolean)
     */
    @Override
    public boolean sameProcessAs(INode target, boolean withConditions) {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setLabel(java.lang.String)
     */
    @Override
    public void setLabel(String label) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getPosX()
     */
    @Override
    public int getPosX() {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getPosY()
     */
    @Override
    public int getPosY() {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getJobletNode()
     */
    @Override
    public INode getJobletNode() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getJunitNode()
     */
    @Override
    public INode getJunitNode() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getModulesNeeded()
     */
    @Override
    public List<ModuleNeeded> getModulesNeeded() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isSubtreeStart()
     */
    @Override
    public boolean isSubtreeStart() {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.Element#getElementName()
     */
    @Override
    public String getElementName() {

        return null;
    }

    @Override
    public void setComponentProperties(ComponentProperties props) {

    }

    @Override
    public ComponentProperties getComponentProperties() {

        return null;
    }

    @Override
    public List<INodeConnector> getConnectorsFromType(final EConnectionType connType) {

        return null;
    }

}
