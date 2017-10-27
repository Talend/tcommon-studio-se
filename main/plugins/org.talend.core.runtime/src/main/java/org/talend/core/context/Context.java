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
package org.talend.core.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;

/**
 * Session context of execution. <br/>
 * 
 * $Id: Context.java 45911 2010-07-23 11:30:58Z hywang $
 * 
 */
public final class Context {

    Map<String, Object> properties = new Hashtable<String, Object>();

    public static final String REPOSITORY_CONTEXT_KEY = "repositoryContext"; //$NON-NLS-1$

    private static final String BREAKPOINTS = "BREAKPOINTS"; //$NON-NLS-1$

    /**
     * Constructs a new Context.
     */
    public Context() {
    }

    /**
     * @param arg0
     * @return
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object getProperty(String arg0) {
        return this.properties.get(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @return
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object putProperty(String arg0, Object arg1) {
        return this.properties.put(arg0, arg1);
    }

    /**
     * @param arg0
     * @return
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object removeProperty(Object arg0) {
        return this.properties.remove(arg0);
    }

    public List<INode> getBreakpointNodes(IProcess process) {
        Map<IProcess, List<INode>> nodesByProcess = (Map<IProcess, List<INode>>) properties.get(BREAKPOINTS);
        List<INode> nodes = nodesByProcess != null ? nodesByProcess.get(process) : null;
        return nodes != null ? nodes : new ArrayList<INode>();
    }

    public void addBreakpoint(IProcess process, INode node) {
        Map<IProcess, List<INode>> nodesByProcess = (Map<IProcess, List<INode>>) properties.get(BREAKPOINTS);
        if (nodesByProcess == null) {
            nodesByProcess = new HashMap<IProcess, List<INode>>();
            properties.put(BREAKPOINTS, nodesByProcess);
        }
        List<INode> nodes = nodesByProcess.get(process);
        if (nodes == null) {
            nodes = new ArrayList<INode>();
            nodesByProcess.put(process, nodes);
        }
        nodes.add(node);
        process.setNeedRegenerateCode(true);
    }

    public void removeBreakpoint(IProcess process, INode node) {
        Map<IProcess, List<INode>> nodesByProcess = (Map<IProcess, List<INode>>) properties.get(BREAKPOINTS);
        if (nodesByProcess != null) {
            List<INode> nodes = nodesByProcess.get(process);
            if (nodes != null) {
                nodes.remove(node);
                process.setNeedRegenerateCode(true);
                if (nodes.isEmpty()) {
                    nodesByProcess.remove(nodes);
                    if (nodesByProcess.isEmpty()) {
                        properties.remove(BREAKPOINTS);
                    }
                }
            }
        }
    }

}
