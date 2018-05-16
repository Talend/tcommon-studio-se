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
package org.talend.commons.utils.data.container;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * PTODO SML Delete this class.
 * 
 * @param <K> - DOC SML
 * @param <T> - type the container manages
 * 
 * $Id$
 * 
 */
public class ContentList<K, T> extends Hashtable<K, Content<K, T>> implements Map<K, Content<K, T>> {

    private static final long serialVersionUID = 1978422122352958954L;

    public List<T> objects() {
        List<T> toReturn = new ArrayList<T>();

        for (Content<K, T> current : values()) {
            toReturn.add(current.getContent());
        }

        return toReturn;
    }

    @SuppressWarnings("unchecked")
    public Content<K, T> getObject(Object object) {
        if (isKey(object)) {
            return getObjectByKey((K) object);
        } else {
            return getObjectByObject((T) object);
        }
    }

    private Content<K, T> getObjectByKey(K key) {
        Content<K, T> content = get(key);
        if (content == null) {
            return null;
        }
        return content;
    }

    private Content<K, T> getObjectByObject(T object) {
        for (Content<K, T> current : values()) {
            if (current.getContent().equals(object)) {
                return current;
            }
        }
        return null;
    }

    public T put(Container<K, T> parent, K key, T value) {
        Content<K, T> cValue = new Content<K, T>(parent, value, key);
        cValue = super.put(key, cValue);
        if (cValue == null) {
            return null;
        }
        return cValue.getContent();
    }

    @SuppressWarnings("unchecked")
    private boolean isKey(Object object) {
        try {
            K key = (K) object;
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
