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
package org.talend.commons.runtime.model.emf.provider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class OptionProvider {

    public abstract String getName();

    public abstract Object getValue();

    public boolean checkSave(Object resource) {
        return true; // nothing to check by default
    }

    public boolean checkLoad(Object resource) {
        return true; // nothing to check by default
    }

}
