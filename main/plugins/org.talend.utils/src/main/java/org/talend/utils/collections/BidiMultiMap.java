// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * created by zhao on Aug 15, 2014 A bidirectional map which provide the way of get multiple keys by value.
 * 
 */
public class BidiMultiMap<K, V> extends HashMap<K, V> {

    /**
     * 
     */
    private static final long serialVersionUID = -8587441823314548120L;

    private HashMap<V, List<K>> value2Keys = new HashMap<V, List<K>>();

    /*
     * (non-Javadoc)
     * 
     * @see java.util.TreeMap#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(K key, V value) {
        List<K> keys = value2Keys.get(value);
        if (keys == null) {
            keys = new ArrayList<K>();
            value2Keys.put(value, keys);
        }
        if (!keys.contains(key)) {
            keys.add(key);
        }
        return super.put(key, value);

    }

    public List<K> getKeys(V value) {
        return value2Keys.get(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.HashMap#clear()
     */
    @Override
    public void clear() {
        value2Keys.clear();
        super.clear();
    }

}
