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
package org.talend.commons.utils.data.list;

import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <B> type of beans
 */
public abstract class UniqueStringGenerator<B> {

    private String baseValue;

    private List<B> beans;

    /**
     * DOC amaumont NameGenerator constructor comment.
     */
    public UniqueStringGenerator(String baseValue, List<B> beans) {
        super();
        this.baseValue = baseValue;
        this.beans = beans;
    }

    public String getUniqueString() {

        int lstSize = beans.size();
        String[] labels = new String[lstSize];
        for (int i = 0; i < lstSize; i++) {
            B bean = (B) beans.get(i);
            labels[i] = getBeanString(bean);
        }

        boolean found = false;
        int indexNewColumn = 0;
        String newColumnName = null;
        boolean firstTime = true;
        while (!found) {
            newColumnName = baseValue + (firstTime ? "" : (++indexNewColumn)); //$NON-NLS-1$
            firstTime = false;
            boolean allAreDifferent = true;
            for (int j = 0; j < labels.length; j++) {
                String label = labels[j];
                if (label != null) {
                    if (label.equals(newColumnName)) {
                        allAreDifferent = false;
                        break;
                    }
                }
            }
            if (allAreDifferent) {
                found = true;
            }
        }

        return newColumnName;
    }

    protected abstract String getBeanString(B bean);

}
