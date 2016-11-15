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
 * @param <G1> the graphical item of extremety 1
 * @param <D1> the data item of extremety 1
 * @param <G2> the graphical item of extremety 2
 * @param <D2> the data item of extremety 2
 */
public class LinkDescriptor<G1, D1, G2, D2> {

    private IExtremityLink<G1, D1> extremity1;

    private IExtremityLink<G2, D2> extremity2;

    private IStyleLink styleLink;

    public LinkDescriptor(IExtremityLink<G1, D1> extremity1, IExtremityLink<G2, D2> extremity2) {
        super();
        this.extremity1 = extremity1;
        this.extremity2 = extremity2;
    }

    public IExtremityLink<G1, D1> getExtremity1() {
        return this.extremity1;
    }

    public void setExtremity1(IExtremityLink<G1, D1> extremity1) {
        this.extremity1 = extremity1;
    }

    public IExtremityLink<G2, D2> getExtremity2() {
        return this.extremity2;
    }

    public void setExtremity2(IExtremityLink<G2, D2> extremity22) {
        this.extremity2 = extremity22;
    }

    public IStyleLink getStyleLink() {
        return this.styleLink;
    }

    public void setStyleLink(IStyleLink styleLink) {
        this.styleLink = styleLink;
    }

}
