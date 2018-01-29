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
package org.talend.core.model.metadata.types;

/**
 * created by hcyi on Aug 19, 2016 Detailled comment
 *
 */
public class AutoConversionType {

    private String sourceDataType;

    private String targetDataType;

    private String conversionFunction;

    public AutoConversionType() {
        //
    }

    public String getSourceDataType() {
        return this.sourceDataType;
    }

    public void setSourceDataType(String sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    public String getTargetDataType() {
        return this.targetDataType;
    }

    public void setTargetDataType(String targetDataType) {
        this.targetDataType = targetDataType;
    }

    public String getConversionFunction() {
        return this.conversionFunction;
    }

    public void setConversionFunction(String conversionFunction) {
        this.conversionFunction = conversionFunction;
    }
}
