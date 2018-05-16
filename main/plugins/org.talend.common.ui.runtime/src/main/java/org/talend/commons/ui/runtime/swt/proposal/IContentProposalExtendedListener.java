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
package org.talend.commons.ui.runtime.swt.proposal;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: IContentProposalExtendedListener.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public interface IContentProposalExtendedListener extends IContentProposalListener {

    /**
     * A proposal popup has been opened.
     */
    public void proposalOpened();

    /**
     * A proposal popup has been closed.
     */
    public void proposalClosed();

    /**
     * Before the control attached is modified by proposal.
     * 
     * @param proposal
     */
    public void proposalBeforeModifyControl(IContentProposal proposal);

}
