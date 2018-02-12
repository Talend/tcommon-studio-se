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
 * @param <E> $Id$
 * 
 */
public class ListenableListEvent<E> {

    /**
     * 
     * DOC amaumont ListenableListEvent class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    public enum TYPE {
        ADDED,
        REMOVED,
        CLEARED,
        SWAPED,
        LIST_REGISTERED,
        REPLACED;
    }

    public Integer index;

    public List<Integer> indicesOrigin;

    public List<Integer> indicesTarget;

    public Object[] swapedObjects;

    public IExtendedList<E> source;

    public Collection<E> addedObjects;

    public Collection<E> removedObjects;

    public TYPE type;

    /**
     * Value is true if event is fired before execution of operation, else false if event is fired after execution of
     * operation.
     */
    public boolean beforeOperation;

}
