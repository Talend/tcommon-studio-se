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
package org.talend.commons.utils.data.map;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public abstract class MultiLazyValuesMap implements Map {

    private Map map;

    /**
     * DOC amaumont CollectionsMap constructor comment.
     */
    public MultiLazyValuesMap(Map map) {
        super();
        this.map = map;
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set<java.util.Map.Entry> entrySet() {
        return map.entrySet();
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    public Object put(Object key, Object value) {
        Object v = map.get(key);
        if (v != null) {
            if (v instanceof List) {
                ((List) v).add(value);
            } else {
                Collection list = instanciateNewCollection();
                list.add(v);
                list.add(value);
                map.put(key, list);
            }
        } else {
            return map.put(key, value);
        }
        return v;
    }

    /**
     * DOC amaumont Comment method "instanciateNewList".
     * 
     * @return
     */
    public abstract Collection instanciateNewCollection();

    public void putAll(Map t) {
        map.putAll(t);
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }

    public Object removeValue(Object key, Object value) {
        Object v = map.get(key);
        if (v != null) {
            if (v instanceof List) {
                ((List) v).remove(value);
                return value;
            } else if (value.equals(v)) {
                remove(key);
                return value;
            }
            return null;
        }
        return null;
    }

    public Collection getCollection(Object key) {
        Object v = map.get(key);
        if (v != null) {
            if (v instanceof List) {
                return (Collection) v;
            } else {
                Collection list = instanciateNewCollection();
                list.add(v);
                map.put(key, list);
                return list;
            }
        } else {
            Collection list = instanciateNewCollection();
            map.put(key, list);
            return list;
        }
    }

}
