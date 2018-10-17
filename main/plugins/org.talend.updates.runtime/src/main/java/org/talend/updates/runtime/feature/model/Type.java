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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.talend.updates.runtime.i18n.Messages;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class Type {

    public static final Type ALL = new Type(Messages.getString("FeaturesManager.Type.all"), "", true, false); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type P2 = new Type(Messages.getString("FeaturesManager.Type.p2"), "p2", false, false); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type TCOMP = new Type(Messages.getString("FeaturesManager.Type.tcomp"), "tcomp", true, false); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type PATCH = new Type(Messages.getString("FeaturesManager.Type.patch"), "patch", true, false); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type TACOKIT_SDK = new Type(Messages.getString("FeaturesManager.Type.p2.tacokitSDK"), "tckSDK", false, //$NON-NLS-1$ //$NON-NLS-2$
            true);

    public static final Type P2_PATCH = new Type(Messages.getString("FeaturesManager.Type.p2.studioPatch"), "p2Patch", false, //$NON-NLS-1$ //$NON-NLS-2$
            true);

    public static final Type PLAIN_ZIP = new Type(Messages.getString("FeaturesManager.Type.plainZip"), "plainZip", false, true); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type TCOMP_V0 = new Type(Messages.getString("FeaturesManager.Type.tcompV0"), "tcompv0", false, true); //$NON-NLS-1$ //$NON-NLS-2$

    public static final Type TCOMP_V1 = new Type(Messages.getString("FeaturesManager.Type.tcompV1"), "tcompv1", false, true); //$NON-NLS-1$ //$NON-NLS-2$

    private static final Collection<Type> types = Arrays.asList(ALL, P2, TCOMP, TCOMP_V0, TCOMP_V1, PATCH, TACOKIT_SDK, P2_PATCH,
            PLAIN_ZIP);

    private String keyword;

    private String label;

    private boolean showLabel;

    /**
     * unique means that one types group can only contains one unique type
     */
    private boolean unique;

    public Type(String label, String keyword, boolean showLabel, boolean unique) {
        this.label = label;
        this.keyword = keyword;
        this.showLabel = showLabel;
        this.unique = unique;
    }

    public String getLabel() {
        return this.label;
    }

    public String getKeyWord() {
        return this.keyword;
    }

    public boolean isShowLabel() {
        return this.showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public boolean isUnique() {
        return this.unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public Collection<Type> getCategories() {
        Collection<Type> categories = new LinkedList<>();
        if (this == Type.TCOMP_V0) {
            categories.addAll(Arrays.asList(Type.P2, Type.TCOMP, Type.TCOMP_V0));
        } else if (this == Type.TCOMP_V1) {
            categories.addAll(Arrays.asList(Type.TCOMP, Type.TCOMP_V1));
        } else if (this == Type.TACOKIT_SDK) {
            categories.addAll(Arrays.asList(Type.P2, Type.PATCH, Type.TACOKIT_SDK));
        } else if (this == Type.P2_PATCH) {
            categories.addAll(Arrays.asList(Type.P2, Type.PATCH, Type.P2_PATCH));
        } else if (this == Type.PLAIN_ZIP) {
            categories.addAll(Arrays.asList(Type.PATCH, Type.PLAIN_ZIP));
        }
        return categories;
    }

    @Override
    public String toString() {
        return keyword;
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

    public static Collection<Type> getAllTypes(boolean checkVisible) {
        Collection<Type> allTypes = new ArrayList<>();
        for (Type type : types) {
            if (checkVisible) {
                if (type.isShowLabel()) {
                    allTypes.add(type);
                }
            } else {
                allTypes.add(type);
            }
        }
        return allTypes;
    }

}
