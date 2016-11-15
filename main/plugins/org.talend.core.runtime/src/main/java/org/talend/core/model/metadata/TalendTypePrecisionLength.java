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
package org.talend.core.model.metadata;

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

    public String getTalendtype() {
        return this.talendtype;
    }

    public void setTalendtype(String talendtype) {
        this.talendtype = talendtype;
    }

    public int getLengthMax() {
        return this.lengthMax;
    }

    public void setLengthMax(int lengthMax) {
        this.lengthMax = lengthMax;
    }

    public int getLengthMin() {
        return this.lengthMin;
    }

    public void setLengthMin(int lengthMin) {
        this.lengthMin = lengthMin;
    }

    public int getPrecMax() {
        return this.precMax;
    }

    public void setPrecMax(int precMax) {
        this.precMax = precMax;
    }

    public int getPrecMin() {
        return this.precMin;
    }

    public void setPrecMin(int precMin) {
        this.precMin = precMin;
    }

}
