// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class ExtremityLink<G, D> implements IExtremityLink<G, D> {

    private D dataItem;

    private G graphicalObject;

    public ExtremityLink(G graphicalObject, D dataItem) {
        super();
        this.dataItem = dataItem;
        this.graphicalObject = graphicalObject;
    }

    public D getDataItem() {
        return this.dataItem;
    }

    public void setDataItem(D dataItem) {
        this.dataItem = dataItem;
    }

    public G getGraphicalObject() {
        return graphicalObject;
    }

    public void setGraphicalObject(G graphicalObject) {
        this.graphicalObject = graphicalObject;
    }

}
