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
public class Category {

    public static final Category ALL = new Category(Messages.getString("FeaturesManager.Category.all"), ""); //$NON-NLS-1$

    private static final Collection<Category> categories = Arrays.asList(ALL);

    private String label;

    private String keyword;

    public Category(String label, String keyword) {
        this.label = label;
        this.keyword = keyword;
    }

    public String getLabel() {
        return this.label;
    }

    public String getKeyWord() {
        return this.keyword;
    }

    public static Category valueOf(String str) {
        if (StringUtils.isBlank(str)) {
            return ALL;
        }
        for (Category c : categories) {
            if (str.equalsIgnoreCase(c.getKeyWord())) {
                return c;
            }
        }
        return null;
    }

    public static Collection<Category> getAllCategories() {
        return categories;
    }
}
