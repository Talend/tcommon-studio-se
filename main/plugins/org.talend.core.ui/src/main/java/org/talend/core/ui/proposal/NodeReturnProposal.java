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

import java.text.MessageFormat;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.ui.i18n.Messages;

/**
 * Content proposal based on a INodeReturn. <br/>
 * 
 * $Id$
 * 
 */
public class NodeReturnProposal implements IContentProposal {

    private INode node;

    private INodeReturn nodeReturn;

    /**
     * Constructs a new NodeReturnProposal.
     */
    public NodeReturnProposal(INode node, INodeReturn nodeReturn) {
        super();

        this.node = node;
        this.nodeReturn = nodeReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    @Override
    public String getContent() {
        return ElementParameterParser.parse(node, nodeReturn.getVarName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
     */
    @Override
    public int getCursorPosition() {
        return getContent().length();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
     */
    @Override
    public String getDescription() {
        String message = Messages.getString("NodeReturnProposal.Description"); //$NON-NLS-1$
        message += Messages.getString("NodeReturnProposal.GlobalVariable"); //$NON-NLS-1$
        message += Messages.getString("NodeReturnProposal.Type"); //$NON-NLS-1$
        message += Messages.getString("NodeReturnProposal.Availability"); //$NON-NLS-1$
        message += Messages.getString("NodeReturnProposal.VariableName"); //$NON-NLS-1$

        MessageFormat format = new MessageFormat(message);
        // Object[] args = new Object[] { nodeReturn.getDisplayName(), node.getComponent().getName(), node.getLabel(),
        // nodeReturn.getDisplayType(), nodeReturn.getAvailability(), getContent() };
        Object[] args = new Object[] { nodeReturn.getDisplayName(), node.getComponent().getName(), node.getLabel(),
                nodeReturn.getDisplayType(), nodeReturn.getAvailability(), nodeReturn.getName() };
        return format.format(args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
     */
    @Override
    public String getLabel() {
        return node.getLabel() + "." + nodeReturn.getName(); //$NON-NLS-1$
    }

}
