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

/**
 * @param <G> graphical item
 * @param <D> data item
 */
public interface IExtremityLink<G, D> {

    public G getGraphicalObject();

    public void setGraphicalObject(G graphicalItem);

    public D getDataItem();

    public void setDataItem(D dataItem);
}
