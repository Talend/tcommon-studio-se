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
package org.talend.commons.ui.swt.proposal.xpath;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * Content proposal based on a IContextParameter. <br/>
 * 
 * $Id: EntryContentProposal.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class XPathContentProposal implements IContentProposal {

    private String content;

    private Node node;

    private boolean relative;

    private boolean firstRelativeNode;

    private String comment;

    /**
     * Constructs a new ContextParameterProposal.
     * 
     * @param node
     */
    public XPathContentProposal(Node node) {
        super();
        this.node = node;
    }

    /**
     * Constructs a new ContextParameterProposal.
     * 
     * @param node
     */
    public XPathContentProposal(String comment) {
        super();
        this.comment = comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    public String getContent() {
        if (comment != null) {
            content = ""; //$NON-NLS-1$
        } else {
            content = getLabel();
        }
        return content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
     */
    public int getCursorPosition() {
        return content.length() + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
     */
    public String getDescription() {
        // StringBuilder sb = new StringBuilder();
        return null;
    }

    public String format(Object object) {
        if (object == null) {
            return ""; //$NON-NLS-1$
        }
        return String.valueOf(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
     */
    public String getLabel() {
        if (comment != null) {
            return comment;
        }

        String label = ""; //$NON-NLS-1$
        if (relative && firstRelativeNode) {
            // nothing
        } else {
            label = "/"; //$NON-NLS-1$
        }

        if (this.node instanceof Attr) {
            label = label + "@" + this.node.getNodeName(); //$NON-NLS-1$
        } else {
            label = label + this.node.getNodeName();
        }
        return label;
    }

    /**
     * Getter for relative.
     * 
     * @return the relative
     */
    public boolean isRelative() {
        return this.relative;
    }

    /**
     * Sets the relative.
     * 
     * @param relative the relative to set
     */
    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    /**
     * Getter for startOfExpression.
     * 
     * @return the startOfExpression
     */
    public boolean isFirstRelativeNode() {
        return this.firstRelativeNode;
    }

    /**
     * Sets the startOfExpression.
     * 
     * @param startOfExpression the startOfExpression to set
     */
    public void setFirstRelativeNode(boolean startOfExpression) {
        this.firstRelativeNode = startOfExpression;
    }

}
