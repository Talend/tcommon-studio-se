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
package org.talend.core.model.process;

import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataTable;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IConnection.java 44753 2010-07-01 06:42:03Z wchen $
 * 
 */
public interface IConnection extends IElement {

    /*
     * feature 6355, work for trace.
     */
    public static final String TRACE_SCHEMA_COLUMN = "TRACE_COLUMN"; //$NON-NLS-1$

    public static final String TRACE_SCHEMA_COLUMN_CHECKED = "TRACE_COLUMN_CHECKED"; //$NON-NLS-1$

    public static final String TRACE_SCHEMA_COLUMN_CONDITION = "TRACE_COLUMN_CONDITION"; //$NON-NLS-1$

    String getCondition();

    /**
     * Get the node target of the connection.
     * 
     * @return Node
     */
    public INode getTarget();

    /**
     * Get the node source of the connection.
     * 
     * @return Node
     */
    public INode getSource();

    /**
     * Get the name of the connection.
     * 
     * @return
     */
    public String getName();

    public String getUniqueName();

    /**
     * Return the given style of the connection.
     * 
     * @see org.talend.designer.core.ui.editor.connections.EConnectionType
     * @return int value of the style
     */
    public EConnectionType getLineStyle();

    public String getRouteConnectionType();

    // TESB-8043
    public String getEndChoice();

    public String getExceptionList();

    public IMetadataTable getMetadataTable();

    public boolean isActivate();

    public void setActivate(boolean activate);

    public void setTraceData(Map<String, TraceData> traceData);

    public Map<String, TraceData> getTraceData();

    public String getConnectorName();

    public int getInputId();

    public int getOutputId();

    public boolean isUseByMetter();

    /**
     * feature 6355. can trace for this connection.
     */
    public boolean isTraceConnection();

    public void setTraceConnection(boolean trace);

    /**
     * feature 6355. can trace for the columns of this connection.
     */
    public List<String> getEnabledTraceColumns();

    /**
     * feature 6355. get the trace condition this connection.
     */
    public String getTracesCondition();

    boolean isMonitorConnection();

    String getMetaName();

    INodeConnector getSourceNodeConnector();

    boolean isSubjobConnection();

    void updateAllId();

    void setMetaName(String uniqueName);

    void reconnect(INode newSource, INode oldTarget, EConnectionType newLineStyle);

    void updateName();

    void setName(String newName);

    void reconnect();

    INodeConnector getTargetNodeConnector();

    void disconnect();

    void setConnectorName(String name);

    void setInputId(int inputId);
}
