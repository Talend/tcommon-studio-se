// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
 * @param <T> type of beans in list
 */
public interface IExtendedList<T> extends List<T> {

    public void swap(int index1, int index2);

    public void swapElements(List<Integer> indicesOrigin, List<Integer> indicesTarget);

    public void swapElement(T object1, T object2);

    public boolean isUseEquals();

    public void setUseEquals(boolean useEquals);

    public void addAll(List<Integer> indices, Collection<? extends T> c);

}
