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
package org.talend.core.model.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.DummyMetadataTalendTypeFilter;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTalendTypeFilter;
import org.talend.core.model.metadata.MrMetadataTalendTypeFilter;
import org.talend.core.model.metadata.SparkMetadataTalendTypeFilter;
import org.talend.core.model.metadata.StormMetadataTalendTypeFilter;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess;
import org.talend.core.runtime.IAdditionalInfo;
import org.talend.designer.core.ICamelDesignerCoreService;

/**
 * DOC xtan class global comment. Detailled comment <br/>
 *
 */
public class NodeUtil {

    /**
     * DOC sort the outgoingconnections to make sure the first connection is EConnectionType.FLOW_MAIN or
     * EConnectionType.FLOW_REF<br/>
     * <p>
     * bug:9363, if a component have 2 output links,
     * <li>"EConnectionType.FLOW_MAIN(FLOW), EConnectionType.FLOW_REF(REJECT)"</li>
     * <li>"EConnectionType.FLOW_MAIN(REJECT), EConnectionType.FLOW_REF(FLOW)"</li>, make FLOW before "REJECT"
     * </p>
     *
     * @param node
     * @return List<? extends IConnection>
     */

    public static List<? extends IConnection> getOutgoingCamelSortedConnections(INode node) {
        List<IConnection> conns = null;

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        if (outgoingConnections != null) {
            conns = new ArrayList<IConnection>(outgoingConnections);
            Collections.sort(conns, new Comparator<IConnection>() {

                private int getTypeWeighted(IConnection con) {
                    switch (con.getLineStyle()) {
                    case ROUTE_ENDBLOCK:
                        return 100;
                    case ROUTE:
                        return 90;
                    case ROUTE_OTHER:
                        return 80;
                    case ROUTE_WHEN:
                        return 70;
                    case ROUTE_FINALLY:
                        return 50;
                    case ROUTE_CATCH:
                        return 40;
                    case ROUTE_TRY:
                        return 30;
                    default:
                        return 0;
                    }
                }

                @Override
                public int compare(IConnection o1, IConnection o2) {
                    int weightedGap = getTypeWeighted(o1) - getTypeWeighted(o2);
                    if (weightedGap == 0) {
                        // same style, compare by inputId
                        if (o1.getOutputId() > o2.getOutputId()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    return weightedGap;
                }
            });
        }

        return conns;
    }

    public static List<? extends IConnection> getOutgoingSortedConnections(INode node) {

        List<IConnection> conns = null;

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        if (outgoingConnections != null) {
            conns = new ArrayList<IConnection>(outgoingConnections);
            Collections.sort(conns, new Comparator<IConnection>() {

                @Override
                public int compare(IConnection connection1, IConnection connection2) {

                    EConnectionType lineStyle = connection1.getLineStyle();
                    EConnectionType lineStyle2 = connection2.getLineStyle();
                    // "FLOW" only for the default Main connection, if user define his connection like: "FILTER",
                    // "REJECT", "FLOW", he should use this API in JET directly: List<? extends IConnection> connsFilter
                    // = node.getOutgoingConnections("FILTER");
                    // 1. FLOW first
                    if ("FLOW".equals(connection1.getConnectorName())) { //$NON-NLS-1$
                        return -1;
                    }

                    if ("FLOW".equals(connection2.getConnectorName())) { //$NON-NLS-1$
                        return 1;
                    }

                    // 2. FLOW_MAIN, FLOW_MERGE second
                    if (lineStyle == EConnectionType.FLOW_MAIN || lineStyle == EConnectionType.FLOW_MERGE) {
                        return -1;
                    }
                    if (lineStyle2 == EConnectionType.FLOW_MAIN || lineStyle2 == EConnectionType.FLOW_MERGE) {
                        return 1;
                    }

                    // 3. others cases, the last
                    return 0;

                }
            });
        }

        return conns;
    }

    /**
     * DOC get the EConnectionType.FLOW_MAIN or EConnectionType.FLOW_REF out goning Connections<br/>
     *
     * @param node
     * @return List<? extends IConnection>
     */
    public static List<? extends IConnection> getMainOutgoingConnections(INode node) {
        List<IConnection> conns = null;

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        if (outgoingConnections != null) {
            conns = new ArrayList<IConnection>();

            for (int i = 0; i < outgoingConnections.size(); i++) {

                IConnection connection = outgoingConnections.get(i);
                if (connection.getLineStyle() == EConnectionType.FLOW_MAIN
                        || connection.getLineStyle() == EConnectionType.FLOW_REF) {

                    conns.add(connection);

                }
            }
        }
        return conns;
    }

    public static List<? extends IConnection> getOutgoingConnections(INode node, EConnectionType connectionType) {
        List<IConnection> conns = null;

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        if (outgoingConnections != null) {
            conns = new ArrayList<IConnection>();

            for (int i = 0; i < outgoingConnections.size(); i++) {
                IConnection connection = outgoingConnections.get(i);
                if (connection.getLineStyle() == connectionType) {
                    conns.add(connection);
                }
            }
        }
        return conns;
    }

    /**
     * DOC get the EConnectionType.FLOW_MAIN in coming Connections<br/>
     *
     * @param node
     * @return INode
     */
    public static INode getMainIncomingConnections(INode node) {
        List<? extends IConnection> incomingConnections = node.getIncomingConnections();
        if (incomingConnections != null) {
            for (int i = 0; i < incomingConnections.size(); i++) {
                IConnection connection = incomingConnections.get(i);
                if (connection.getLineStyle() == EConnectionType.FLOW_MAIN) {
                    return connection.getSource();
                }
            }
        }
        return null;
    }

    public static List<? extends IConnection> getIncomingConnections(INode node, EConnectionType connectionType) {
        List<IConnection> conns = null;

        List<? extends IConnection> incomingConnections = node.getIncomingConnections();
        if (incomingConnections != null) {
            conns = new ArrayList<IConnection>();

            for (int i = 0; i < incomingConnections.size(); i++) {
                IConnection connection = incomingConnections.get(i);
                if (connection.getLineStyle() == connectionType) {
                    conns.add(connection);
                }
            }
        }
        return conns;
    }

    public static List<? extends IConnection> getOutgoingConnections(INode node, String connectorName) {
        List<IConnection> conns = null;

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        if (outgoingConnections != null) {
            conns = new ArrayList<IConnection>();

            for (int i = 0; i < outgoingConnections.size(); i++) {

                IConnection connection = outgoingConnections.get(i);
                if (connectorName.equals(connection.getConnectorName())) {
                    conns.add(connection);
                }
            }
        }
        return conns;
    }

    /**
     *
     * wzhang Comment method "getIncomingConnections".
     *
     * @param node
     * @param category
     * @return
     */
    public static List<? extends IConnection> getIncomingConnections(INode node, int category) {
        List<IConnection> conns = null;

        List<? extends IConnection> incomingConnections = node.getIncomingConnections();
        if (incomingConnections != null) {
            conns = new ArrayList<IConnection>();

            for (int i = 0; i < incomingConnections.size(); i++) {

                IConnection connection = incomingConnections.get(i);
                if (connection.getLineStyle().hasConnectionCategory(category)) {
                    conns.add(connection);
                }
            }
        }
        return conns;
    }

    public static List<? extends IConnection> getOutgoingConnections(INode node, int category) {
        List<IConnection> conns = null;

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        if (outgoingConnections != null) {
            conns = new ArrayList<IConnection>();

            for (int i = 0; i < outgoingConnections.size(); i++) {

                IConnection connection = outgoingConnections.get(i);
                if (connection.getLineStyle().hasConnectionCategory(category)) {
                    conns.add(connection);
                }
            }
        }
        return conns;
    }

    public static List<IMetadataTable> getIncomingMetadataTable(INode node, int category) {
        List<IMetadataTable> metadatas = new ArrayList<IMetadataTable>();

        List<? extends IConnection> incomingConnections = node.getIncomingConnections();
        if (incomingConnections != null) {
            for (int i = 0; i < incomingConnections.size(); i++) {

                IConnection connection = incomingConnections.get(i);
                if (connection.getLineStyle().hasConnectionCategory(category)) {
                    metadatas.add(connection.getMetadataTable());
                }
            }
        }
        return metadatas;
    }

    public static List<? extends IConnection> getIncomingConnections(INode node, String connectorName) {
        List<IConnection> conns = null;

        List<? extends IConnection> incomingConnections = node.getIncomingConnections();
        if (incomingConnections != null) {
            conns = new ArrayList<IConnection>();

            for (int i = 0; i < incomingConnections.size(); i++) {

                IConnection connection = incomingConnections.get(i);
                String inputName = null;
                if (IAdditionalInfo.class.isInstance(connection)) {
                    inputName = (String) IAdditionalInfo.class.cast(connection).getInfo("INPUT_NAME");
                }
                if (connectorName.equals(connection.getConnectorName()) || connectorName.equals(inputName)) {
                    conns.add(connection);
                }
            }
        }
        return conns;
    }

    public static boolean checkConnectionAfterNode(INode node, EConnectionType connectionType, List<INode> checkedNodes) {
        // fix bug 0004935: Error on job save
        if (checkedNodes.contains(node)) {
            return false;
        } else {
            checkedNodes.add(node);
        }

        boolean result = false;
        List<? extends IConnection> outConns = getOutgoingConnections(node, connectionType);
        if (outConns == null || outConns.size() == 0) {
            List<? extends IConnection> conns = getOutgoingSortedConnections(node);
            if (conns != null && conns.size() > 0) {
                for (IConnection conn : conns) {
                    result = checkConnectionAfterNode(conn.getTarget(), connectionType, checkedNodes);
                    if (result) {
                        break;
                    }
                }
            } else {
                result = false;
            }
        } else {
            result = true;
        }
        return result;
    }

    public static boolean checkComponentErrorConnectionAfterNode(INode node) {
        List<INode> checkedNodes = new ArrayList<INode>();
        return checkConnectionAfterNode(node, EConnectionType.ON_COMPONENT_ERROR, checkedNodes);
    }

    /**
     * DOC xtan
     * <p>
     * InLineJob means all nodes after a iterate link(The nodes will execute many times on every iterate).
     * </p>
     * Notice: The search method don't consider the second branch of the tUnite, but it is ok.
     *
     * @param node
     * @return
     */
    public static Set<? extends IConnection> getAllInLineJobConnections(INode node) {
        Set<String> uniqueNamesDone = new HashSet<String>();
        uniqueNamesDone.add(node.getUniqueName());
        return getAllInLineJobConnections(node, uniqueNamesDone);
    }

    private static Set<? extends IConnection> getAllInLineJobConnections(INode node, Set<String> uniqueNamesDone) {
        Set<IConnection> conns = new HashSet<IConnection>();

        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        if (outgoingConnections != null) {

            conns.addAll(outgoingConnections); // add all

            for (int i = 0; i < outgoingConnections.size(); i++) {

                IConnection connection = outgoingConnections.get(i);
                INode nextNode = connection.getTarget();

                if (!uniqueNamesDone.contains(nextNode.getUniqueName())) {
                    uniqueNamesDone.add(nextNode.getUniqueName());
                    conns.addAll(getAllInLineJobConnections(nextNode, uniqueNamesDone)); // follow this way
                }
            }
        }
        return conns;
    }

    public static INode getFirstMergeNode(INode node) {
        INode mergeNode = null;
        for (IConnection connection : node.getOutgoingConnections()) {
            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.MERGE)) {
                mergeNode = connection.getTarget();
            } else if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.FLOW)) {
                mergeNode = getFirstMergeNode(connection.getTarget());
            }
            if (mergeNode != null) {
                break;
            }
        }
        return mergeNode;
    }

    public static IConnection getNextMergeConnection(INode node) {

        List<? extends IConnection> outConns = getOutgoingConnections(node, IConnectionCategory.MERGE);
        if (outConns == null || outConns.size() == 0) {
            List<? extends IConnection> conns = node.getOutgoingConnections(EConnectionType.FLOW_MAIN);
            if (conns != null && conns.size() > 0) {
                for (IConnection conn : conns) {
                    node = conn.getTarget();
                    if (node.isActivate() || node.isDummy()) {
                        IConnection connection = getNextMergeConnection(node);
                        if (connection != null) {
                            return connection;
                        }
                    }
                }
            } else if (node.getVirtualLinkTo() != null) {
                return getNextMergeConnection(node.getOutgoingConnections().get(0).getTarget());
            }
            return null;
        } else {
            return outConns.get(0);
        }
    }

    /**
     * DOC bchen
     * <p>
     * This method works for the virtual component which the inner link is ON_COMPONENT_OK and the output connection
     * contains Merge type and the order is not 1.
     * </p>
     * <p>
     * notice: 1. the node is not in main branch, so there is not possible to have ON_SUBJOB_OK/ON_COMPONENT_OK in
     * start.
     *
     * 2. if there are two or more tUnite components, it will be tHash virtual component, no inner link in this virtual
     * component
     *
     * 3. if there have tIteratToFlow component, no inner link ,as this component also adapt to Merge connection.
     * </p>
     * <p>
     * return: 1. if there have another virtual component(ON_COMPONENT_OK) in the incoming connection, it will be
     * returned.
     *
     * 2. if there have not another virtual component(ON_COMPENT_OK), it will return the start node as it ignore other
     * type of virtual components
     * </p>
     *
     * @param node
     * @return node
     */
    public static INode getSpecificStartNode(INode node) {
        List<? extends IConnection> inConns = node.getIncomingConnections();
        if (inConns == null || inConns.size() == 0) {
            return node;
        } else {
            if (inConns.size() == 1 && inConns.get(0).getLineStyle().equals(EConnectionType.ON_COMPONENT_OK)) {
                INode sNode = inConns.get(0).getSource();
                if (sNode != null && sNode.getVirtualLinkTo() != null
                        && sNode.getVirtualLinkTo().equals(EConnectionType.ON_COMPONENT_OK)) {
                    return node;
                }
            }
            for (IConnection inConn : inConns) {
                INode sourceNode = inConn.getSource();
                if (inConn.getLineStyle().equals(EConnectionType.FLOW_MAIN) || sourceNode.getVirtualLinkTo() != null) {
                    INode activeNode = findActiveNode(sourceNode);
                    if (activeNode != null) {
                        INode findNode = getSpecificStartNode(activeNode);
                        if (findNode != null) {
                            return findNode;
                        }
                    }
                }

            }
        }
        return null;
    }

    private static INode findActiveNode(INode node) {
        if (node.isActivate()) {
            return node;
        } else if (node.isDummy()) {
            if (node.getIncomingConnections() != null && node.getIncomingConnections().size() == 1) {
                return findActiveNode(node.getIncomingConnections().get(0).getSource());
            }
        }
        return null;
    }

    public static boolean isLastFromMergeTree(INode node) {
        INode firstMergeNode = getFirstMergeNode(node);
        int totMerge = NodeUtil.getIncomingConnections(firstMergeNode, IConnectionCategory.MERGE).size();
        Integer posMerge = node.getLinkedMergeInfo().get(firstMergeNode);
        return totMerge == posMerge;
    }

    public static IConnection getRealConnectionTypeBased(IConnection connection) {
        IConnection realConnection = connection;

        boolean connectionAvailable = true;

        //        while (connectionAvailable && realConnection.getSource().getComponent().getName().equals("tReplace")) { //$NON-NLS-1$

        while (connectionAvailable && !realConnection.getSource().isSubProcessStart()
                && realConnection.getSource().getComponent().isDataAutoPropagated()) {

            List<IConnection> inConnections = (List<IConnection>) getIncomingConnections(realConnection.getSource(),
                    IConnectionCategory.FLOW);
            if (inConnections.size() > 0) {
                realConnection = inConnections.get(0);
            } else {
                connectionAvailable = false;
            }
        }

        return realConnection;
    }

    /**
     * DOC wliu
     * <p>
     * judge if the current connection is the last output connection of the component
     * </p>
     * Notice: It is used in subtree_end.javajet. And the aim is for feature5996
     *
     * @param connection
     * @return
     */
    public static boolean isLastMultiplyingOutputComponents(IConnection connection) {

        List<? extends IConnection> conns = connection.getSource().getOutgoingConnections();
        int last = -1;
        if (conns != null && conns.size() > 0) {
            for (int i = 0; i < conns.size(); i++) {
                if (conns.get(i).getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                    last = i;
                }
            }
        }

        if (last >= 0 && connection.getName().equals(conns.get(last).getName())) {
            return true;
        }

        return false;
    }

    /**
     *
     * judge if the current node is in the last branch Notice: It is only used in tPigStoreResult. And the aim is for
     * TDI-25120
     *
     * @param node
     * @return
     */
    public static boolean isSubTreeEnd(INode node) {
        List<? extends IConnection> incConnections = NodeUtil.getIncomingConnections(node, IConnectionCategory.DATA);
        if (incConnections.size() > 0) {
            IConnection connection = incConnections.get(0); // always take the first one, don't consider merge case.
            if (isLastMultiplyingOutputComponents(connection)) {
                return isSubTreeEnd(connection.getSource());
            } else {
                return false;
            }
        }
        return true;
    }

    public static Map<INode, Integer> getLinkedMergeInfo(final INode node) {
        Map<INode, Integer> map = new HashMap<INode, Integer>();

        getLinkedMergeInfo(node, map);

        if (map.isEmpty()) {
            // in case the component is not linked directly, it should take the status of previous component, since it
            // will be in the same branch.
            getLinkedMergeInfoFromMainLink(node, map);
        }

        return map;
    }

    private static void getLinkedMergeInfoFromMainLink(final INode node, final Map<INode, Integer> map) {
        if (node.getComponent().useMerge()) {
            return;
        }
        List<IConnection> inputConnections = (List<IConnection>) getIncomingConnections(node, IConnectionCategory.MAIN);
        if (inputConnections.size() > 0) {
            IConnection input = inputConnections.get(0);
            INode sourceNode = input.getSource();
            if (sourceNode.getJobletNode() != null) {
                sourceNode = sourceNode.getJobletNode();
            }
            getLinkedMergeInfo(sourceNode, map);
            if (map.isEmpty()) {
                getLinkedMergeInfoFromMainLink(sourceNode, map);
            }
        }

    }

    private static void getLinkedMergeInfo(final INode node, final Map<INode, Integer> map) {
        List<? extends IConnection> outgoingConnections = node.getOutgoingConnections();
        for (int i = 0; (i < outgoingConnections.size()); i++) {
            IConnection connec = outgoingConnections.get(i);
            if (connec.isActivate()) {
                if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MERGE)) {
                    INode jNode = connec.getTarget();
                    if (jNode.getJobletNode() != null) {
                        jNode = jNode.getJobletNode();
                    }
                    map.put(jNode, connec.getInputId());
                    getLinkedMergeInfo(jNode, map);
                } else if (connec.getLineStyle().hasConnectionCategory(EConnectionType.MAIN) && connec.getTarget() != null) {
                    INode jNode = connec.getTarget();
                    if (jNode.getJobletNode() != null) {
                        jNode = jNode.getJobletNode();
                    }
                    getLinkedMergeInfo(jNode, map);
                }
            }
        }
    }

    // this is only for bug:11754, and only be used in generating code.
    public static boolean isDataAutoPropagated(final INode node) {
        IComponent component = node.getComponent();
        // if it is tJavaFlex, use the property Version_V2_0 to instead the DATA_AUTO_PROPAGATE="false" config
        // in tJavaFlex_java.xml
        if (component.getName().compareTo("tJavaFlex") == 0) {
            boolean isVersionV20 = "true".equals(ElementParameterParser.getValue(node, "__Version_V2_0__"));
            return isVersionV20;
        } else {
            return component.isDataAutoPropagated();
        }
    }

    /**
     * DOC wliu
     * <p>
     * get the original connection instance className of the pamameter:conn.\n It is used to help optimize the code to
     * avoid 65535 bytes in a method
     * </p>
     * Notice: It is used in tFileOutputMSXML in TDI-21606
     *
     * @param connection
     * @return
     */
    public static String getPrivateConnClassName(final IConnection conn) {

        if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
            INode node = conn.getSource();
            if (node.isSubProcessStart() || !(NodeUtil.isDataAutoPropagated(node))) {
                return conn.getName();
            }
            List<? extends IConnection> listInConns = node.getIncomingConnections();
            if (listInConns != null && listInConns.size() > 0) {
                String retResult = getPrivateConnClassName(listInConns.get(0));
                if (retResult == null) {
                    return conn.getName();
                } else {
                    return retResult;
                }
            }
        }
        return null;

    }

    /**
     * DOC jyhu
     * <p>
     * function:get the node from generating nodes by unique name. aim:to get the property value from any node.
     * </p>
     * Notice: It is used to get property values from the pointed node.
     *
     * @param node:node from the job @param uniqueName:the unique name of the pointed node.
     * @return
     */
    public static INode getNodeByUniqueName(final IProcess process, String uniqueName) {

        return getNodeByUniqueName(process, uniqueName, false);
    }

    /**
     * DOC jzhao
     * <p>
     * function:get the node(maybe include virtual node) from generating nodes by unique name. aim:to get the property
     * value from any node.
     * </p>
     * Notice: It is used to get property values from the pointed node we can also get the virtual node.
     *
     * @param process: a job process
     * @param uniqueName:the unique name of the pointed node.
     * @param isReturnVirtualNode: specify whether return the virtual node.
     *
     * @return
     */
    public static INode getNodeByUniqueName(final IProcess process, String uniqueName, boolean isReturnVirtualNode) {

        List<INode> nodes = (List<INode>) process.getGeneratingNodes();
        for (INode current_node : nodes) {
            if (isReturnVirtualNode && current_node.isVirtualGenerateNode()) {
                current_node = getVirtualNode(current_node);
            }
            if (uniqueName.equals(current_node.getUniqueName())) {
                return current_node;
            }
        }
        return null;
    }

    /**
     *
     * DOC liuwu find all the tRecollectors which match to tPartitioner
     *
     * @param node : should be tPartitioner
     * @param recollectors
     */
    public static void getRecollectorsFromPartitioner(INode node, List<String> recollectors) {
        List<? extends INode> listRecollectors = node.getProcess().getNodesOfType("tRecollector"); //$NON-NLS-1$
        if (listRecollectors != null && listRecollectors.size() > 0) {
            for (INode recNode : listRecollectors) {
                String departitionerName = ElementParameterParser.getValue(recNode, "__DEPART_COMPONENT__"); //$NON-NLS-1$
                List<? extends INode> listDepartitioner = node.getProcess().getNodesOfType("tDepartitioner"); //$NON-NLS-1$
                if (listDepartitioner == null) {
                    return;
                }

                for (INode tnode : listDepartitioner) {
                    if (tnode.getUniqueName().equals(departitionerName)) { // find the tDepartitioner corresponding to
                                                                           // tRecollector
                        INode startNode = getSubProcessStartNode(tnode); // find the tCollector
                        List<? extends IConnection> inConns = startNode.getIncomingConnections(EConnectionType.STARTS);
                        if (inConns != null && inConns.size() > 0) {
                            if (inConns.get(0).getSource() == node) {
                                recollectors.add(recNode.getUniqueName());
                            }
                        }
                        break;
                    }
                }
            }
        }

    }

    /**
     * @author jyhu
     * @aim Get Whether the nodelist contain virtual component.
     * @param nodeList: Node list
     * @return nodelist contain virtual component or not. true:contain;false:not contain
     */
    public static boolean hasVirtualComponent(List<? extends INode> nodeList) {
        boolean hasVirtualComponent = false;
        for (INode node : nodeList) {
            if (node.isVirtualGenerateNode()) {
                hasVirtualComponent = true;
                break;
            }
        }
        return hasVirtualComponent;
    }

    /**
     * @author jyhu
     * @aim Get unique name of the graphica node from generating node.
     * @param node: Generated node
     * @return unique name of the graphica node.
     */
    public static String getVirtualUniqueName(INode node) {
        return getVirtualNode(node).getUniqueName();
    }

    /**
     * @author pbailly
     * @aim Get graphical node from generating node.
     * @param node: Generated node
     * @return If the component is a virtual node, return the graphical node, otherwise return initial node.
     */
    public static INode getVirtualNode(INode node) {
        if (node.isVirtualGenerateNode()) {
            String uniqueName = node.getUniqueName();
            for (INode graphicalNode : node.getProcess().getGraphicalNodes()) {
                if ((graphicalNode.isGeneratedAsVirtualComponent())
                        && (uniqueName.startsWith(graphicalNode.getUniqueName() + "_"))) { //$NON-NLS-1$
                    return graphicalNode;
                }
            }
        }
        return node;
    }

    public static void fillConnectionsForStat(List<String> connsName, INode currentNode) {
        for (IConnection conn : currentNode.getOutgoingConnections()) {
            if (conn.getLineStyle() == EConnectionType.FLOW_MAIN) {
                if (!(currentNode.isVirtualGenerateNode() && currentNode.getVirtualLinkTo() != null)) {
                    // if the conn between two virtual compnents, don't consider
                    connsName.add(conn.getUniqueName());
                }
                fillConnectionsForStat(connsName, conn.getTarget());
            } else if (conn.getLineStyle() == EConnectionType.FLOW_MERGE) {
                connsName.add(conn.getUniqueName());
                continue;
            } else if (conn.getLineStyle() == EConnectionType.ON_ROWS_END) {
                // on_rows_end only used for virtual component, so don't need to consider
                fillConnectionsForStat(connsName, conn.getTarget());
            }
        }
    }

    /*
     * return all displayed parameters
     */
    public static List<IElementParameter> getDisplayedParameters(INode currentNode) {
        List<? extends IElementParameter> eps = currentNode.getElementParameters();
        List<IElementParameter> reps = new ArrayList<IElementParameter>();
        // should ignore Parallelize?
        List<String> ignorePs = Arrays.asList("CONNECTION_FORMAT", "INFORMATION", "COMMENT", "VALIDATION_RULES", "LABEL", "HINT",
                "ACTIVATE", "TSTATCATCHER_STATS", "PARALLELIZE", "PROPERTY");
        // Exclude SQLPATTERN_VALUE.
        for (IElementParameter ep : eps) {
            if (ep.isShow(eps)) {
                if (!ignorePs.contains(ep.getName())) {
                    reps.add(ep);
                }
            }
        }
        return reps;
    }

    public static String getNormalizeParameterValue(INode node, String elementName) {
        List<? extends IElementParameter> eps = node.getElementParameters();
        for (IElementParameter ep : eps) {
            if (elementName.equals(ep.getName())) {
                return getNormalizeParameterValue(node, ep);
            }
        }
        throw new IllegalArgumentException();
    }

    public static String getNormalizeParameterValue(INode node, IElementParameter ep) {
        if (EParameterFieldType.TABLE.equals(ep.getFieldType())) {
            Map<String, IElementParameter> types = new HashMap<String, IElementParameter>();
            Object[] itemsValue = ep.getListItemsValue();
            if (itemsValue != null) {
                for (Object o : itemsValue) {
                    IElementParameter cep = (IElementParameter) o;
                    if (cep.isShow(node.getElementParameters())) {
                        types.put(cep.getName(), cep);
                    }
                }
            }
            List<Map<String, String>> lines = (List<Map<String, String>>) ElementParameterParser.getObjectValue(node,
                    "__" + ep.getName() + "__");
            StringBuilder value = new StringBuilder();
            // implement List & Map toString(), different is the value of Map
            Iterator<Map<String, String>> linesIter = lines.iterator();
            if (!linesIter.hasNext()) {
                return "\"[]\"";
            }
            value.append("\"[");
            for (;;) {
                Map<String, String> columns = linesIter.next();
                Iterator<Entry<String, String>> columnsIter = columns.entrySet().iterator();

                value.append("{");
                Entry<String, String> column = null;
                boolean printedColumnExist = false;
                while (columnsIter.hasNext()) {
                    column = columnsIter.next();
                    if (types.get(column.getKey()) == null) {
                        continue;
                    }
                    printedColumnExist = true;

                    value.append(column.getKey());
                    value.append("=\"+(");
                    value.append(getNormalizeParameterValue(column.getValue(), types.get(column.getKey()), true));
                    value.append(")+\"");

                    if (columnsIter.hasNext()) {
                        value.append(", ");
                    }
                }
                if (printedColumnExist && column != null && (types.get(column.getKey()) == null)) {
                    value.setLength(value.length() - 2);
                }
                value.append("}");

                if (!linesIter.hasNext()) {
                    return value.append("]\"").toString();
                }
                value.append(",").append(" ");
            }
        } else {
            String value = ElementParameterParser.getValue(node, "__" + ep.getName() + "__");
            if (EParameterFieldType.TABLE_BY_ROW.equals(ep.getFieldType())) {
                value = ep.getValue().toString();
            }
            return getNormalizeParameterValue(value, ep, false);
        }

    }

    private static String getNormalizeParameterValue(String value, IElementParameter ep, boolean itemFromTable) {
        if (value == null) {
            value = "";
        }
        value = value.replaceAll("[\r\n]", " ");// for multiple lines
        value = value.replaceAll("\\\\", "\\\\\\\\");// escape all \\
        value = value.replaceAll("\\\"", "\\\\\\\"");// escape all \"
        value = "\"" + value + "\"";// wrap double quote make it as string

        return value;
    }

    public static String replaceCRLFInMEMO_SQL(String original) {
        return replaceCRLFInMEMO_SQL(original, true);
    }
    
    /**
     *
     * DOC liuwu Comment method "replaceMEMO_SQL". aim: to resolve TDI-7487
     * 
     * if splitAlways is true, will split the string always to avoid the huge string constant
     * if splitAlways is false, split only when the string constant reach the limit : 60000 bytes
     * 
     * @param original
     * @return
     */
    public static String replaceCRLFInMEMO_SQL(String original, boolean splitAlways) {
        if (original == null || original.trim().length() == 0) {
            return original;
        }
        StringBuilder result = new StringBuilder();
        
        boolean needSplit = true;
        
        if(!splitAlways) {
            try {
                //there is 64k limit for string constant, we set the limit to 60000 for code format reason like auto new line for editor
                needSplit = original.getBytes("UTF-8").length > 60000;
            } catch (UnsupportedEncodingException e) {
            }
        }
        
        Pattern invalid_unicode_character_regex = Pattern.compile("^.*(\\\\u[0-9a-fA-F]{0,3})$", Pattern.DOTALL);
        //now don't process the octal case for code performance, can enable it when real need from customer
        //Pattern invalid_octal_character_regex = Pattern.compile("^.*(\\\\[0-9]{1,2})$", Pattern.DOTALL);
        
        int leftQuotes = original.indexOf("\"");
        int rightQuotes = original.indexOf("\"", leftQuotes + 1);
        int fakeRightQuotes = getFakeRightQuotes( original,leftQuotes);
        while (rightQuotes == fakeRightQuotes + 1) {
            rightQuotes = original.indexOf("\"", rightQuotes + 1);
            fakeRightQuotes = getFakeRightQuotes( original,fakeRightQuotes);
        }
        int leftPrev = 0;
    		while (leftQuotes >= 0 && rightQuotes > leftQuotes) {
      			if (leftQuotes > leftPrev) {//Outside of double quote
      				  result.append(original.substring(leftPrev, leftQuotes));
      			}
      			
      			if (leftQuotes < rightQuotes) {//Inside of double quote
        				//split string for better appearance and avoid compile error when string exceed 64k
        			  if(needSplit) {
            				int current = leftQuotes;
            				int Offset = 120;
            				int count = 0;
            				while (rightQuotes + 1 - current > 120) {
            				    //[\u0000] or [\000]
            				    Matcher matcher = invalid_unicode_character_regex.matcher(original.substring(current, current + Offset));
              				  if(matcher.matches()) {
              				      Offset = Offset - matcher.group(1).length();
              				  } 
              				  /*
              				  else {
                  				  matcher = invalid_octal_character_regex.matcher(original.substring(current, current + Offset));
                            if(matcher.matches()) {
                                Offset = Offset - matcher.group(1).length();
                            }
              				  }
              				  */
            				  
              					while (original.charAt(current + Offset - 1) == '\\') {//avoid split special character e.g. \"
              						  Offset--;
              					}
              					
              					if(count>500){//This is the code that really solve TDI-39968 others are only for good appearance.
                						result.append(original.substring(current, current + Offset).replace("\r", "").replace("\n", "\\n")).append("\" + new String()\n+\"");
                						count = 0;
              					}else{
              						  result.append(original.substring(current, current + Offset).replace("\r", "").replace("\n", "\\n")).append("\"\n+\"");
              					}
              					
              					current += Offset;
              					Offset = 120;
              					count++;
            				}
            				result.append(original.substring(current, rightQuotes + 1).replace("\r", "").replace("\n", "\\n"));
        			  } else {
            				result.append(original.substring(leftQuotes, rightQuotes + 1).replace("\r", "").replace("\n", "\\n"));
        			  }
      			}

            leftQuotes = original.indexOf("\"", rightQuotes + 1);
            leftPrev = rightQuotes + 1;
            rightQuotes = original.indexOf("\"", leftQuotes + 1);
            fakeRightQuotes = getFakeRightQuotes(original,leftQuotes);
            while (rightQuotes == fakeRightQuotes + 1) {
                rightQuotes = original.indexOf("\"", rightQuotes + 1);
                fakeRightQuotes = getFakeRightQuotes(original,fakeRightQuotes);
            }
        }
        result.append(original.substring(leftPrev));
        return result.toString();
    }
    
    /**
     * [] is the container for the string, not one part
     * ... mean other part of the string
     * 
     * This method would avoid get wrong fakeRithQuotes index, like:
     * ["\\"...] the right quote is not fake one.
     * ["\"...] the right quote is a fake one
     */
    private static int getFakeRightQuotes(String original, int fromIdex) {
        int fakeRightQuotes = original.indexOf("\\\"", fromIdex + 1);
        String quoteStr = "\\\"";
        int count = 0;
        while (fakeRightQuotes > 0) {
            count++;
            quoteStr = "\\" + quoteStr;
            int tmpIndex = original.indexOf(quoteStr, fromIdex + 1);
            if (tmpIndex > fakeRightQuotes || tmpIndex < 0) {
                // If add even times "\\", then the index is -1 or bigger than we get fakeRightQuotes
                // This means that this is really fake quote
                if (count % 2 == 1) {
                    break;
                } else {// Else it is really a right quote, then need get and check next fakeRightQuotes
                    fakeRightQuotes = original.indexOf("\\\"", fakeRightQuotes + 1);
                    quoteStr = "\\\"";
                    count = 0;
                }
            }

        }
        return fakeRightQuotes;
    }

    /**
     *
     * add it for TDI-28503
     *
     * @param departitioner node in collector subprocess
     * @return collector node as the start node
     */
    public static INode getSubProcessStartNode(INode currentNode) {
        int nb = 0;
        for (IConnection connection : currentNode.getIncomingConnections()) {
            if (connection.isActivate()) {
                nb++;
            }
        }
        if (nb == 0) {
            return currentNode;
        }
        IConnection connec;

        for (int j = 0; j < currentNode.getIncomingConnections().size(); j++) {
            connec = currentNode.getIncomingConnections().get(j);
            if (((AbstractNode) connec.getSource()).isOnMainMergeBranch()) {
                if (connec.getLineStyle() == EConnectionType.STARTS) {
                    return currentNode;
                } else if (connec.getLineStyle() != EConnectionType.FLOW_REF) {
                    return getSubProcessStartNode(connec.getSource());
                }
            }
        }
        return null;
    }

    public static boolean containsMultiThreadComponent(IProcess process) {
        List<? extends INode> multiThreadComponentList = process.getNodesOfType("tWriteXMLFieldOut");
        if (multiThreadComponentList != null && multiThreadComponentList.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean subBranchContainsParallelIterate(INode node) {
        for (IConnection connection : node.getIncomingConnections()) {
            if (connection == null || !connection.isActivate()) {
                continue;
            }

            if (!(connection.getLineStyle().hasConnectionCategory(IConnectionCategory.MAIN | IConnectionCategory.USE_ITERATE))) {
                continue;
            }

            if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.USE_ITERATE)) {
                if (Boolean.TRUE.toString().equals(ElementParameterParser.getValue(connection, "__ENABLE_PARALLEL__"))) {
                    return true;
                }
            }

            return subBranchContainsParallelIterate(connection.getSource());
        }
        return false;
    }

    /**
     *
     * DOC rdubois Comment method "isBigDataFrameworkNode".
     *
     * @return true if a Node is a BigData framework component.
     */
    public static boolean isBigDataFrameworkNode(INode node) {
        if (node != null && node.getComponent() != null && node.getComponent().getType() != null) {
            ComponentCategory cat = ComponentCategory.getComponentCategoryFromName(node.getComponent().getType());
            return (ComponentCategory.CATEGORY_4_MAPREDUCE == cat || ComponentCategory.CATEGORY_4_STORM == cat
                    || ComponentCategory.CATEGORY_4_SPARK == cat || ComponentCategory.CATEGORY_4_SPARKSTREAMING == cat);
        }

        return false;
    }

    /**
     * This static method is a factory for the MetadataTalendTypeFilter extensions. DOC rdubois Comment method
     * "createMetadataTalendTypeFilter".
     *
     * @param outputNode
     * @return
     */
    public static MetadataTalendTypeFilter createMetadataTalendTypeFilter(INode node) {
        if (node != null && node.getComponent() != null && node.getComponent().getType() != null) {
            ComponentCategory cat = ComponentCategory.getComponentCategoryFromName(node.getComponent().getType());
            if (ComponentCategory.CATEGORY_4_MAPREDUCE == cat) {
                return new MrMetadataTalendTypeFilter();
            }
            if (ComponentCategory.CATEGORY_4_SPARK == cat || ComponentCategory.CATEGORY_4_SPARKSTREAMING == cat) {
                return new SparkMetadataTalendTypeFilter(node.getComponent().getName());
            }
            if (ComponentCategory.CATEGORY_4_STORM == cat) {
                return new StormMetadataTalendTypeFilter(node.getComponent().getName());
            }
        }
        return new DummyMetadataTalendTypeFilter();
    }
    
    /**
     * Used for GUI normally, to know which connector is valid during the job design / link creation.
     * @param node
     * @return
     */
    public static INodeConnector getValidConnector(INode node) {
        INodeConnector mainConnector = null;
        if (node.isELTComponent()) {
            mainConnector = node.getConnectorFromType(EConnectionType.TABLE);
        } else if (ComponentCategory.CATEGORY_4_CAMEL.getName().equals(node.getComponent().getType())) {
            INodeConnector tmp = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                        .getService(ICamelDesignerCoreService.class);
                tmp = node.getConnectorFromType(camelService.getTargetConnectionType(node));
            } else {
                tmp = node.getConnectorFromType(EConnectionType.ROUTE);
            }
            mainConnector = tmp;
        } else {
            List<? extends INodeConnector> nodeConnList = node.getConnectorsFromType(EConnectionType.FLOW_MAIN);
            for (INodeConnector nodeConn : nodeConnList) {
                if (isConnectorValid(nodeConn)) {
                    return nodeConn;
                }
            }
        }

        if (!isConnectorValid(mainConnector)) {
            return null;
        }
        return mainConnector;
    }

    private static boolean isConnectorValid(INodeConnector mainConnector) {

        if (mainConnector == null) {
            return false;
        }

        if (!mainConnector.isShow()) {
            return false;
        }

        if (mainConnector.getMaxLinkOutput() != -1) {
            if (mainConnector.getCurLinkNbOutput() >= mainConnector.getMaxLinkOutput()) {
                return false;
            }
        }
        if (mainConnector.getMaxLinkOutput() == 0) {
            return false;
        }

        return true;
    }
}
