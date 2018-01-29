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

import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

/**
 * Interface used for the external proposals that can be filled in other plugins. <br>
 * Note that all proposals added here will be used in all viewers / jobs.
 */
public interface IExternalProposals {

    /**
     * Proposals used in the viewers.
     * 
     * @param prefix
     * @param offset
     * 
     * @return
     */
    public List<ICompletionProposal> getAdvancedProposals(int offset, String prefix);

    /**
     * Proposals used in the simple text box for example.
     * 
     * @return
     */
    public List<IContentProposal> getStandardProposals();

}
