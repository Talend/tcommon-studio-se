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

import java.util.ArrayList;
import java.util.List;

/**
 * bqian XML Element structure. <br/>
 * 
 * $Id: Element.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 */
public class Element extends FOXTreeNode {

    private List<Attribute> attributes = new ArrayList<Attribute>();

    private List<NameSpaceNode> nameSpaces = new ArrayList<NameSpaceNode>();

    /**
     * Element constructor comment.
     */
    public Element() {
        super();
    }

    /**
     * Element constructor comment.
     */
    public Element(String label) {
        super(label);
    }

    public Element(String label, String defauleValue) {
        super(label, defauleValue);
    }

    @Override
    public void removeChild(FOXTreeNode child) {
        child.setParent(null);
        if (child instanceof Attribute) {
            Attribute element = (Attribute) child;
            attributes.remove(element);
            return;
        }
        if (child instanceof NameSpaceNode) {
            NameSpaceNode element = (NameSpaceNode) child;
            nameSpaces.remove(element);
            return;
        }
        super.removeChild(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        List<FOXTreeNode> childsWithoutNamespaces = new ArrayList<FOXTreeNode>(getChildren());
        childsWithoutNamespaces.removeAll(nameSpaces);
        return childsWithoutNamespaces.size() + attributes.size() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.fileoutputxml.data.FOXTreeNode#addChild(org.talend.designer.fileoutputxml.data.FOXTreeNode)
     */
    @Override
    public void addChild(FOXTreeNode child) {
        if (child instanceof Attribute) {
            Attribute element = (Attribute) child;
            attributes.add(element);
            child.setParent(this);
            return;
        }
        if (child instanceof NameSpaceNode) {
            NameSpaceNode element = (NameSpaceNode) child;
            nameSpaces.add(element);
            child.setParent(this);
            return;
        }
        super.addChild(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.fileoutputxml.data.FOXTreeNode#getChildren()
     */
    @Override
    public List<FOXTreeNode> getChildren() {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        list.addAll(attributes);
        list.addAll(nameSpaces);
        list.addAll(super.getChildren());

        return list;
    }

    public List<FOXTreeNode> getElementChildren() {
        return super.getChildren();
    }

    public List<? extends FOXTreeNode> getAttributeChildren() {
        // List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        // list.addAll(attributes);
        return this.attributes;
    }

    public List<? extends FOXTreeNode> getNameSpaceChildren() {
        // List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        // list.addAll(nameSpaces);
        return this.nameSpaces;
    }

}
