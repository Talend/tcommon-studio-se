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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class ProposalFactory {

    public static final String EXTENSION_ID = "org.talend.core.talend_external_proposals"; //$NON-NLS-1$

    public static final String ELEM_CLASS = "class"; //$NON-NLS-1$

    public static List<IExternalProposals> getInstances() {
        List<IExternalProposals> externalProposals = new ArrayList<IExternalProposals>();
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IConfigurationElement[] configurationElementsFor = extensionRegistry.getConfigurationElementsFor(EXTENSION_ID);
        for (IConfigurationElement configurationElement : configurationElementsFor) {
            try {
                IExternalProposals externalProposal = (IExternalProposals) configurationElement
                        .createExecutableExtension(ELEM_CLASS);
                externalProposals.add(externalProposal);
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }

        }
        return externalProposals;
    }
}
