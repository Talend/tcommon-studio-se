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
package org.talend.commons.utils.data.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapList<K, V> extends HashMap<K, List<V>> implements Map<K, List<V>> {

    private static final long serialVersionUID = -6734316976728141405L;

    @Override
    public List<V> get(Object arg0) {
        List<V> list = super.get(arg0);
        if (list == null) {
            list = new ArrayList<V>();
            put((K) arg0, list);
        }
        return list;
    }

    public boolean add(K key, V value) {
        return get(key).add(value);
    }

    public void clear(K key) {
        get(key).clear();
    }

    public boolean removeAll(K key, Collection<V> c) {
        return get(key).removeAll(c);
    }
}
