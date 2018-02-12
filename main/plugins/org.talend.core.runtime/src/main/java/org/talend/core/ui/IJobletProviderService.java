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
package org.talend.core.ui;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryEditorInput;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * cli class global comment. Detailled comment
 */
public interface IJobletProviderService extends IService {

    public boolean isJobletComponent(INode node);

    public boolean isStandardJobletComponent(INode node);

    public boolean isJobletInOutComponent(INode node);

    public boolean isJobletInputComponent(INode node);

    public Property getJobletComponentItem(INode node);

    public Property getJobletComponentItem(IComponent component);

    public void reloadJobletProcess(INode node, boolean forceReload);

    public boolean isTriggerNode(INode node);

    public boolean isTriggerInputNode(INode node);

    public boolean canConnectTriggerNode(INode node, EConnectionType connType);

    public List<EConnectionType> getTriggerNodeSupportConnTypes(INode triggerNode);

    public List<INode> getTriggerNodes(INode jobletNode);

    public List<? extends INode> getGraphNodesForJoblet(INode jobletNode);

    public EConnectionType getTriggerNodeConnType(INode triggerNode);

    public boolean isBuiltTriggerConnector(INode jobletNode, INodeConnector connector);

    public List<INodeConnector> getTriggerBuiltConnectors(INode jobletNode, EConnectionType lineStyle, boolean input);

    public List<INodeConnector> getFreeTriggerBuiltConnectors(INode jobletNode, EConnectionType lineStyle, boolean input);

    public List<INode> getConnNodesForInputTrigger(INode jobletNode, IElementParameter param);

    public void upateJobletComonentList(INode jobletNode);

    public void loadComponentsFromProviders();

    public IComponent setPropertyForJobletComponent(String id, String version);

    public void updateParametersFromJoblet(INode node, IComponent newComponent);

    public ProcessType getJobletProcess(NodeType node);

    public IEditorPart openJobletItem(JobletProcessItem item);

    public boolean isJobletItem(Item item);

    public Action getMoveToJobletAction(IWorkbenchPart part, INode jobletNode, Map<INode, IConnection> nodeMap);

    public Action getMoveToJobAction(IWorkbenchPart part);

    public MultiPageEditorPart openJobletEditor(INode jobletNode, IWorkbenchPage page);

    public void saveJobletNode(JobletProcessItem jobletItem, IElement jobletContainer);

    public boolean isLock(INode in);

    public boolean isReadOnly(INode in);

    public void lockJoblet(INode node);

    public void updateJobletCom(INode node);

    public void unlockJoblet(INode node, boolean needAsk);

    public void updateRelationShip(final IProcess curJobletProcess, final IProcess2 currentProcess,
            final List<IMetadataTable> oldInputMetadata, final List<IMetadataTable> oldOutputMetadata, final boolean updateContext);

    public boolean jobletIsDirty(INode node);

    /**
     * The shadow data connections are only display in editor, but won't be used to generate in final job codes.
     */
    public IConnection[] getShadowDataConnections(IConnection conn);

    /**
     * Get the inner joblet data connections, those connections will be used to generate in final job codes. like
     * <Joblet Unique Name>_<Connection Unique Name>, for example, JobletTest_1_row1.
     */
    public IConnection[] getNonShadowDataConnections(INode node);

    public void clearJobletComponent();

    public IRepositoryEditorInput getJobletProcessEditorInput(JobletProcessItem processItem, Boolean load, Boolean lastVersion,
            Boolean readOnly, Boolean openedInJob) throws PersistenceException;

    public boolean isJobletEditor(IEditorPart activeEditor);

    public IComponent getUpdatedJobletComponent(IComponent component);

}
