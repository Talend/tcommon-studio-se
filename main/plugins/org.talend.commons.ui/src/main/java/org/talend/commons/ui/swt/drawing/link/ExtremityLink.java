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
package org.talend.commons.ui.swt.drawing.link;


/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <G>
 * @param <D>
 * 
 */
public class ExtremityLink<G, D> implements IExtremityLink<G, D> {

    private D dataItem;

    private G graphicalObject;

    /**
     * DOC amaumont TableItemExtremityDescriptor constructor comment.
     * 
     * @param tableItem
     */
    public ExtremityLink(G graphicalObject, D dataItem) {
        super();
        this.dataItem = dataItem;
        this.graphicalObject = graphicalObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#getDataItem()
     */
    public D getDataItem() {
        return this.dataItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#setDataItem(java.lang.Object)
     */
    public void setDataItem(D dataItem) {
        this.dataItem = dataItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#getGraphicalItem()
     */
    public G getGraphicalObject() {
        return graphicalObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IExtremityLink#setGraphicalItem(java.lang.Object)
     */
    public void setGraphicalObject(G graphicalObject) {
        this.graphicalObject = graphicalObject;
    }

}
