// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class TestFakeNode extends Element implements INode {

    @Override
    public boolean isReadOnly() {

        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public String getLabel() {

        return null;
    }

    @Override
    public String getUniqueName() {

        return null;
    }

    @Override
    public String getUniqueShortName() {

        return null;
    }

    @Override
    public boolean isStart() {

        return false;
    }

    @Override
    public boolean isActivate() {

        return false;
    }

    @Override
    public boolean isSubProcessStart() {

        return false;
    }

    @Override
    public boolean isSubProcessContainTraceBreakpoint() {

        return false;
    }

    @Override
    public List<? extends IConnection> getIncomingConnections() {

        return null;
    }

    @Override
    public List<? extends IConnection> getOutgoingConnections() {

        return null;
    }

    @Override
    public boolean hasConditionalOutputs() {

        return false;
    }

    @Override
    public List<BlockCode> getBlocksCodeToClose() {

        return null;
    }

    @Override
    public boolean isMultiplyingOutputs() {

        return false;
    }

    @Override
    public void setPerformanceData(String perfData) {

    }

    @Override
    public List<IMetadataTable> getMetadataList() {
        return null;
    }

    @Override
    public List<? extends INodeReturn> getReturns() {
        return null;
    }

    @Override
    public IProcess getProcess() {
        return null;
    }

    @Override
    public void setProcess(IProcess process) {
    }

    @Override
    public IComponent getComponent() {
        return null;
    }

    @Override
    public void setComponent(IComponent component) {
    }

    @Override
    public IExternalNode getExternalNode() {
        return null;
    }

    @Override
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
    }

    @Override
    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
    }

    @Override
    public INode getSubProcessStartNode(boolean withConditions) {
        return null;
    }

    @Override
    public boolean useData(String name) {
        return false;
    }

    @Override
    public void renameData(String oldName, String newName) {
    }

    @Override
    public boolean isThereLinkWithHash() {
        return false;
    }

    @Override
    public List<? extends IConnection> getOutgoingSortedConnections() {
        return null;
    }

    @Override
    public List<? extends IConnection> getOutgoingCamelSortedConnections() {
        return null;
    }

    @Override
    public List<? extends IConnection> getMainOutgoingConnections() {
        return null;
    }

    @Override
    public List<? extends IConnection> getOutgoingConnections(EConnectionType connectionType) {
        return null;
    }

    @Override
    public List<? extends IConnection> getIncomingConnections(EConnectionType connectionType) {
        return null;
    }

    @Override
    public List<? extends IConnection> getOutgoingConnections(String connectorName) {
        return null;
    }

    @Override
    public Map<INode, Integer> getLinkedMergeInfo() {
        return null;
    }

    @Override
    public boolean isThereLinkWithMerge() {
        return false;
    }

    @Override
    public IMetadataTable getMetadataFromConnector(String connector) {
        return null;
    }

    @Override
    public INodeConnector getConnectorFromName(String connector) {
        return null;
    }

    @Override
    public void reloadComponent(IComponent component, Map<String, Object> parameters, boolean isUpdate) {
    }

    @Override
    public INode getDesignSubjobStartNode() {
        return null;
    }

    @Override
    public boolean isDesignSubjobStartNode() {
        return false;
    }

    @Override
    public boolean isVirtualGenerateNode() {
        return false;
    }

    @Override
    public EConnectionType getVirtualLinkTo() {
        return null;
    }

    @Override
    public void setVirtualLinkTo(EConnectionType virtualLinkTo) {
    }

    @Override
    public boolean isGeneratedAsVirtualComponent() {
        return false;
    }

    @Override
    public boolean isELTComponent() {
        return false;
    }

    @Override
    public boolean isUseLoopOnConditionalOutput(String outputName) {
        return false;
    }

    @Override
    public IExternalData getExternalData() {
        return null;
    }

    @Override
    public List<? extends INodeConnector> getListConnector() {
        return null;
    }

    @Override
    public boolean isDummy() {
        return false;
    }

    @Override
    public Set<INode> fsComponentsInProgressBar() {
        return null;
    }

    @Override
    public boolean isExternalNode() {
        return false;
    }

    @Override
    public void addOutput(IConnection connection) {
    }

    @Override
    public void addInput(IConnection connection) {
    }

    @Override
    public boolean isTemplate() {
        return false;
    }

    @Override
    public boolean isGeneratedByJobscriptBool() {
        return false;
    }

    @Override
    public void removeOutput(IConnection connection) {
    }

    @Override
    public void removeInput(IConnection connection) {
    }

    @Override
    public IMetadataTable getMetadataTable(String metaName) {
        return null;
    }

    @Override
    public INodeConnector getConnectorFromType(EConnectionType lineStyle) {
        return null;
    }

    @Override
    public boolean checkIfCanBeStart() {
        return false;
    }

    @Override
    public void setStart(boolean checkIfCanBeStart) {
    }

    @Override
    public void checkNode() {
    }

    @Override
    public Object getSchemaParameterFromConnector(String name) {
        return null;
    }

    @Override
    public boolean hasRunIfLink() {
        return false;
    }

    @Override
    public void setMetadataList(List<IMetadataTable> metadataList) {
    }

    @Override
    public void setOutgoingConnections(List<? extends IConnection> outgoingConnections) {
    }

    @Override
    public void setIncomingConnections(List<? extends IConnection> incomingConnections) {
    }

    @Override
    public INode getProcessStartNode(boolean processStartNode) {
        return null;
    }

    @Override
    public boolean isFileScaleComponent() {
        return false;
    }

    @Override
    public boolean sameProcessAs(INode target, boolean withConditions) {
        return false;
    }

    @Override
    public void setLabel(String label) {
    }

    @Override
    public int getPosX() {
        return 0;
    }

    @Override
    public int getPosY() {
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
        return null;
    }

    @Override
    public boolean isSubtreeStart() {
        return false;
    }

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

}
