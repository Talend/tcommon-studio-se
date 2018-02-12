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

import java.util.Collection;
import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <T> type of beans in list
 */
public interface IExtendedList<T> extends List<T> {

    public void swap(int index1, int index2);

    public void swapElements(List<Integer> indicesOrigin, List<Integer> indicesTarget);

    public void swapElement(T object1, T object2);

    /**
     * Getter for useEquals.
     * 
     * @return the useEquals
     */
    public boolean isUseEquals();

    /**
     * Sets the useEquals.
     * 
     * @param useEquals the useEquals to set
     */
    public void setUseEquals(boolean useEquals);

    /**
     * 
     * 
     */
    @SuppressWarnings("unchecked")
    public void addAll(List<Integer> indices, Collection<? extends T> c);

}
