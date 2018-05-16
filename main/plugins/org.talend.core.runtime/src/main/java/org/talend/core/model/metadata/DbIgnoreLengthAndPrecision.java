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
 * @author informix
 * 
 */
public class DbIgnoreLengthAndPrecision {

    private String ignoreLength;

    private String ignorePrecision;

    private String dbType;

    public String getIgnoreLength() {
        return ignoreLength;
    }

    public void setIgnoreLength(String ignoreLength) {
        this.ignoreLength = ignoreLength;
    }

    public String getIgnorePrecision() {
        return ignorePrecision;
    }

    public void setIgnorePrecision(String ignorePrecision) {
        this.ignorePrecision = ignorePrecision;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

}
