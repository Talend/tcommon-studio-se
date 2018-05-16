// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class TalendTypePrecisionLength {

    private String talendtype;

    private int lengthMax;

    private int lengthMin;

    private int precMax;

    private int precMin;

    public TalendTypePrecisionLength(String talendtype, int lengthMax, int lengthMin, int precMax, int precMin) {

        this.talendtype = talendtype;
        this.lengthMax = lengthMax;
        this.lengthMin = lengthMin;
        this.precMax = precMax;
        this.precMin = precMin;

    }

    /**
     * Getter for talendtype.
     * 
     * @return the talendtype
     */
    public String getTalendtype() {
        return this.talendtype;
    }

    /**
     * Sets the talendtype.
     * 
     * @param talendtype the talendtype to set
     */
    public void setTalendtype(String talendtype) {
        this.talendtype = talendtype;
    }

    /**
     * Getter for lengthMax.
     * 
     * @return the lengthMax
     */
    public int getLengthMax() {
        return this.lengthMax;
    }

    /**
     * Sets the lengthMax.
     * 
     * @param lengthMax the lengthMax to set
     */
    public void setLengthMax(int lengthMax) {
        this.lengthMax = lengthMax;
    }

    /**
     * Getter for lengthMin.
     * 
     * @return the lengthMin
     */
    public int getLengthMin() {
        return this.lengthMin;
    }

    /**
     * Sets the lengthMin.
     * 
     * @param lengthMin the lengthMin to set
     */
    public void setLengthMin(int lengthMin) {
        this.lengthMin = lengthMin;
    }

    /**
     * Getter for precMax.
     * 
     * @return the precMax
     */
    public int getPrecMax() {
        return this.precMax;
    }

    /**
     * Sets the precMax.
     * 
     * @param precMax the precMax to set
     */
    public void setPrecMax(int precMax) {
        this.precMax = precMax;
    }

    /**
     * Getter for precMin.
     * 
     * @return the precMin
     */
    public int getPrecMin() {
        return this.precMin;
    }

    /**
     * Sets the precMin.
     * 
     * @param precMin the precMin to set
     */
    public void setPrecMin(int precMin) {
        this.precMin = precMin;
    }

}
