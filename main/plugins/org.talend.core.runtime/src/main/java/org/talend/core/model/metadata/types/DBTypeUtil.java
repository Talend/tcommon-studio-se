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
 * DOC talend class global comment. Detailled comment
 */
public class DBTypeUtil {

    private String dbTypeName;

    private boolean isDefault;

    private boolean ignoreLength;

    private boolean ignorePrecision;

    public DBTypeUtil(String dbTypeName, boolean isDefault, boolean ignoreLen, boolean ignorePre) {
        this.dbTypeName = dbTypeName;
        this.isDefault = isDefault;
        this.ignoreLength = ignoreLen;
        this.ignorePrecision = ignorePre;
    }

    public String getDbTypeName() {
        return this.dbTypeName;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    /**
     * Getter for ignoreLength.
     * 
     * @return the ignoreLength
     */
    public boolean isIgnoreLength() {
        return this.ignoreLength;
    }

    /**
     * Getter for ignorePrecision.
     * 
     * @return the ignorePrecision
     */
    public boolean isIgnorePrecision() {
        return this.ignorePrecision;
    }

}
