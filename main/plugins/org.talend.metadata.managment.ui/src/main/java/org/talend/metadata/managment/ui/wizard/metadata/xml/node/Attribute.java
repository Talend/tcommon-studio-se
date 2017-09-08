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
package org.talend.metadata.managment.ui.wizard.metadata.xml.node;


/**
 * bqian XML attribute structure. <br/>
 * 
 * $Id: Attribute.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 */
public class Attribute extends FOXTreeNode {

    /**
     * Attribute constructor comment.
     */
    public Attribute() {
    }

    /**
     * Attribute constructor comment.
     */
    public Attribute(String label) {
        setLabel(label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#getValue()
     */
    @Override
    public String getLabelForViewer() {
        return "@" + super.getLabel(); //$NON-NLS-1$
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isLoop() {
        return false;
    }

    @Override
    public boolean isMain() {
        return false;
    }
}
