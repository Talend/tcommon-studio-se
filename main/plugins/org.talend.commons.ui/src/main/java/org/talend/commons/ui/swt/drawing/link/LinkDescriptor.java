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
 * @param <G1> the graphical item of extremety 1
 * @param <D1> the data item of extremety 1
 * @param <G2> the graphical item of extremety 2
 * @param <D2> the data item of extremety 2
 */
public class LinkDescriptor<G1, D1, G2, D2> {

    private IExtremityLink<G1, D1> extremity1;

    private IExtremityLink<G2, D2> extremity2;

    private IStyleLink styleLink;

    /**
     * DOC amaumont Link constructor comment.
     * 
     * @param extremity1
     * @param extremity2
     */
    public LinkDescriptor(IExtremityLink<G1, D1> extremity1, IExtremityLink<G2, D2> extremity2) {
        super();
        this.extremity1 = extremity1;
        this.extremity2 = extremity2;
    }

    /**
     * Getter for tip1.
     * 
     * @return the tip1
     */
    public IExtremityLink<G1, D1> getExtremity1() {
        return this.extremity1;
    }

    /**
     * Sets the tip1.
     * 
     * @param extremity1 the tip1 to set
     */
    public void setExtremity1(IExtremityLink<G1, D1> extremity1) {
        this.extremity1 = extremity1;
    }

    /**
     * Getter for tip2.
     * 
     * @return the tip2
     */
    public IExtremityLink<G2, D2> getExtremity2() {
        return this.extremity2;
    }

    /**
     * Sets the tip2.
     * 
     * @param extremity22 the tip2 to set
     */
    public void setExtremity2(IExtremityLink<G2, D2> extremity22) {
        this.extremity2 = extremity22;
    }

    /**
     * Getter for styleLink.
     * 
     * @return the styleLink
     */
    public IStyleLink getStyleLink() {
        return this.styleLink;
    }

    /**
     * Sets the styleLink.
     * 
     * @param drawableLink the styleLink to set
     */
    public void setStyleLink(IStyleLink styleLink) {
        this.styleLink = styleLink;
    }

}
