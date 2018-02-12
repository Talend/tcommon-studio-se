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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public abstract class AbstractElementFilter<E> {

    protected String searchString;

    private List<E> elements;

    protected AbstractElementFilter() {
    }

    /**
     * 
     * DOC ycbai Comment method "filter".
     * 
     * Returns the passed elements from this filter.
     * 
     * @return
     */
    public List<E> filter() {
        List<E> out = new ArrayList<E>();
        if (elements == null) {
            return out;
        } else {
            for (E element : elements) {
                if (canAdd(element) && !out.contains(element)) {
                    out.add(element);
                }
            }
        }
        return out;
    }

    protected boolean canAdd(E element) {
        if (searchString == null || searchString.length() == 0) {
            return true;
        }

        if (element == null) {
            return false;
        }

        return select(element);
    }

    /**
     * 
     * DOC ycbai Comment method "select".
     * 
     * Returns whether the given element makes it through this filter.
     * 
     * @param element
     * @return
     */
    protected abstract boolean select(E element);

    /**
     * 
     * DOC ycbai Comment method "setElements".
     * 
     * Set the elements which will be filtered.
     * 
     * @param elements
     */
    public void setElements(List<E> elements) {
        this.elements = elements;
    }

    /**
     * 
     * DOC ycbai Comment method "setSearchText".
     * 
     * Set the search string.
     * 
     * @param searchText
     */
    public void setSearchText(String searchStr) {
        // Search must be a substring of the existing value
        this.searchString = ".*" + searchStr.toUpperCase() + ".*";
    }

}
