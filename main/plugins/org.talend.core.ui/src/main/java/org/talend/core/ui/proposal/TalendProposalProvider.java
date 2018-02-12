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
package org.talend.core.ui.proposal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ContextItem;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.TalendType;

/**
 * ContentProposalProvider based on a Process. <br/>
 * 
 * $Id$
 * 
 */
public class TalendProposalProvider implements IContentProposalProvider {

    protected IProcess process;

    protected INode currentNode;

    /**
     * Constructs a new ProcessProposalProvider.
     */
    public TalendProposalProvider(IProcess process, final INode node) {
        super();
        this.currentNode = node;
        this.process = process;
    }

    public TalendProposalProvider(IProcess process) {
        super();
        this.process = process;
    }

    /**
     * yzhang ProcessProposalProvider constructor comment.
     */
    public TalendProposalProvider() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String, int)
     */
    @Override
    public IContentProposal[] getProposals(String contents, int position) {
        List<IContentProposal> proposals = new ArrayList<IContentProposal>();

        if (process != null) {
            // Proposals based on process context
            List<IContextParameter> ctxParams = process.getContextManager().getDefaultContext().getContextParameterList();
            for (IContextParameter ctxParam : ctxParams) {
                proposals.add(new ContextParameterProposal(ctxParam));
            }

            // Proposals based on global variables
            List<? extends INode> nodes = process.getGraphicalNodes();
            for (INode node : nodes) {
                List<? extends INodeReturn> nodeReturns = node.getReturns();
                for (INodeReturn nodeReturn : nodeReturns) {
                    proposals.add(new NodeReturnProposal(node, nodeReturn));
                }
            }

        } else {
            List<ContextItem> allContextItem = ContextUtils.getAllContextItem();
            List<IContextParameter> ctxParams = new ArrayList<IContextParameter>();
            if (allContextItem != null) {
                for (ContextItem item : allContextItem) {
                    List<IContextParameter> tmpParams = new JobContextManager(item.getContext(), item.getDefaultContext())
                            .getDefaultContext().getContextParameterList();
                    ctxParams.addAll(tmpParams);
                }
            }
            for (IContextParameter ctxParam : ctxParams) {
                proposals.add(new ContextParameterProposal(ctxParam));
            }
        }

        // Proposals based on global variables(only perl ).
        // add proposals on global variables in java (bugtracker 2554)
        // add variables in java
        IContentProposal[] javavars = JavaGlobalUtils.getProposals();
        for (IContentProposal javavar : javavars) {
            proposals.add(javavar);
        }

        // Proposals based on routines
        FunctionManager functionManager = new FunctionManager();

        List<TalendType> talendTypes = functionManager.getTalendTypes();
        for (TalendType type : talendTypes) {
            for (Object objectFunction : type.getFunctions()) {
                Function function = (Function) objectFunction;
                proposals.add(new RoutinesFunctionProposal(function));
            }
        }

        for (IExternalProposals externalProposals : ProposalFactory.getInstances()) {
            proposals.addAll(externalProposals.getStandardProposals());
        }

        // sort the list
        Collections.sort(proposals, new Comparator<IContentProposal>() {

            @Override
            public int compare(IContentProposal arg0, IContentProposal arg1) {
                return compareRowAndContextProposal(arg0.getLabel(), arg1.getLabel());
            }

        });

        IContentProposal[] res = new IContentProposal[proposals.size()];
        res = proposals.toArray(res);
        return res;
    }

    /**
     * Make sure the $row proposal follow the context proposal see feature 3725 DOC YeXiaowei Comment method
     * "compareRowAndContextProposal".
     * 
     * @param label0
     * @param label1
     * @return
     */
    protected int compareRowAndContextProposal(String label0, String label1) {
        if (label0.startsWith("$row[") && label1.startsWith("context")) { //$NON-NLS-1$ //$NON-NLS-2$
            return 1;
        } else if (label1.startsWith("$row[") && label0.startsWith("context")) { //$NON-NLS-1$ //$NON-NLS-2$
            return -1;
        } else {
            return label0.compareToIgnoreCase(label1);
        }
    }

}
