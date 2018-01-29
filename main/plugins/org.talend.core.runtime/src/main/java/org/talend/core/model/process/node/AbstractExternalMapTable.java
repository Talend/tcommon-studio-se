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
package org.talend.core.model.process.node;

/**
 * created by Talend on May 7, 2015 Detailled comment
 *
 */
public abstract class AbstractExternalMapTable implements IExternalMapTable {

    public String getExpressionFilter() {
        return null;
    }

    public boolean isActivateExpressionFilter() {
        return false;
    }

    public boolean isMinimized() {
        return false;
    }

    public boolean isActivateCondensedTool() {
        return false;
    }
}
