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
package org.talend.updates.runtime.feature.model;

import org.talend.updates.runtime.i18n.Messages;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class Type {

    public static final Type ALL = new Type(Messages.getString("FeaturesManager.Type.all"), ""); //$NON-NLS-1$ //$NON-NLS-2$

    private String keyword;

    private String label;

    public Type(String label, String keyword) {
        this.label = label;
        this.keyword = keyword;
    }

    public String getLabel() {
        return this.label;
    }

    public String getKeyWord() {
        return this.keyword;
    }
}
