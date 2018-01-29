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
package org.talend.commons.utils.data.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public final class ListUtils {

    private ListUtils() {
        // do not use
    }

    /**
     * Swap object1 with object2 in the list.
     * 
     * @param list
     * @param object1
     * @param object2
     */
    @SuppressWarnings("unchecked")
    public static void swap(List list, Object object1, Object object2) {
        int indexObject1 = list.indexOf(object1);
        int indexObject2 = list.indexOf(object2);
        list.set(indexObject2, object1);
        list.set(indexObject1, object2);
    }

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for (Object o : c) {
            r.add(clazz.cast(o));
        }
        return r;
    }

}
