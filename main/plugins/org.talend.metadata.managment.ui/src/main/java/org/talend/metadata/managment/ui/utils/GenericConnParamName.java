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
package org.talend.metadata.managment.ui.utils;

import org.talend.metadata.managment.ui.model.IConnParamName;

/**
 * created by ycbai on 2015年11月20日 Detailled comment
 *
 */
public class GenericConnParamName implements IConnParamName {

    private String name;

    private String contextVar;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContextVar() {
        return this.contextVar;
    }

    public void setContextVar(String contextVar) {
        this.contextVar = contextVar;
    }

}
