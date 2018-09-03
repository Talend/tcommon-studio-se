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

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.talend.updates.runtime.i18n.Messages;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class Type {

    public static final Type ALL = new Type(Messages.getString("FeaturesManager.Type.all"), ""); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type P2 = new Type(Messages.getString("FeaturesManager.Type.p2"), "p2"); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type TCOMP = new Type(Messages.getString("FeaturesManager.Type.tcomp"), "tcomp"); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type TCOMP_V0 = new Type(Messages.getString("FeaturesManager.Type.tcompV0"), "tcompv0"); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type TCOMP_V1 = new Type(Messages.getString("FeaturesManager.Type.tcompV1"), "tcompv1"); //$NON-NLS-1$ //$NON-NLS-2$

    private static final Collection<Type> types = Arrays.asList(ALL, P2, TCOMP, TCOMP_V0, TCOMP_V1);

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

    public static Type valueOf(String type) {
        if (StringUtils.isBlank(type)) {
            return ALL;
        }
        for (Type t : types) {
            if (type.equalsIgnoreCase(t.getKeyWord())) {
                return t;
            }
        }
        return null;
    }

    public static Collection<Type> getAllTypes() {
        return types;
    }
}
