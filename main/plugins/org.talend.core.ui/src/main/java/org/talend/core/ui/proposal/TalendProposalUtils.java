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
package org.talend.core.ui.proposal;

import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
import org.talend.commons.ui.swt.proposal.ProposalUtils;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TalendProposalUtils {

    /**
     * Installs a proposal provider on a text component based on a process.
     * 
     * @param control Text component on wich proposals are installed.
     * @param process Process from wich proposals are built.
     */
    public static ContentProposalAdapterExtended installOn(Control control, IProcess process, final INode node) {
        IContentProposalProvider proposalProvider = null;
        if (node != null && node.getComponent() != null && node.getComponent().getName() != null
                && node.getComponent().getName().startsWith("tPig")) {
            proposalProvider = new PigProposalProvider(process, node);
        } else if (process != null) {
            proposalProvider = new TalendProposalProvider(process, node);
        } else {
            proposalProvider = new TalendProposalProvider();
        }
        return ProposalUtils.getCommonProposal(control, proposalProvider);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "installOn".
     * 
     * @param control
     * @param process
     * @return
     */
    public static ContentProposalAdapterExtended installOn(Control control, IProcess process) {
        return installOn(control, process, null);
    }

}
