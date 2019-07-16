// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author S. Correia An helper for handling maps of type [key -> collection of values].
 */
public final class MultiMapHelper {

    private MultiMapHelper() {
    }

    /**
     * Adds the object "value" in the list got from "key". The object is not added if the list already contains it. The
     * collection is implemented as an ArrayList.
     *
     * @param key an entry key of the given map
     * @param value the object to add to the given map
     * @param map the map of the type [key -> List ]
     * @return true if object is added (or already present) in the correct collection
     * @see MultiMapHelper#addUniqueObjectToCollectionMap(Object, Object, Map) for a more efficient implementation.
     */
    public static <KeyT, ValT> boolean addUniqueObjectToListMap(KeyT key, ValT value, Map<KeyT, List<ValT>> map) {
        List<ValT> obj = map.get(key);
        if (obj == null) {
            List<ValT> list = new ArrayList<ValT>();
            list.add(value);
            return (map.put(key, list) == null);
        }

        // else
        List<ValT> coll = obj;
        // --- check whether it is already contained
        if (coll.contains(value)) {
            return true;
        }

        // --- else add it
        coll.add(value);
        return true;
    }

    /**
     * Adds the object "value" in the list got from "key". The object is not added if the list already contains it OR an
     * object that is identical to it. The collection is implemented as an ArrayList and the objects are added in an
     * ASCENDING order.
     *
     * @param key an entry key of the given map
     * @param value the object to add to the given map
     * @param map the map of the type [key -> List ]
     * @return true if object is added (or already present) in the correct collection
     * @see MultiMapHelper#addUniqueObjectToCollectionMap(Object, Object, Map) for a more efficient implementation.
     */
    public static boolean addUniqueComparableToListMap(Object key, Comparable<Object> value,
            Map<Object, List<Comparable<Object>>> map) {
        List<Comparable<Object>> obj = map.get(key);
        if (obj == null) {
            List<Comparable<Object>> list = new ArrayList<Comparable<Object>>();
            list.add(value);
            return (map.put(key, list) == null);
        }

        // else
        List<Comparable<Object>> coll = obj;
        // --- check whether it is already contained
        int idx = 0;
        for (Comparable<Object> element : coll) {
            if ((value == element) || (value.compareTo(element) == 0)) {
                return true;
            }
            if (value.compareTo(element) > 0) {
                continue;
            }
            // else value > element
            break;
        }

        coll.add(idx, value);
        return true;
    }

    /**
     * Adds the object "value" in the collection got from "key". The object is not added if the list already contains
     * it. The collection is implemented as a HashSet.
     *
     * @param key an entry key of the given map
     * @param value the object to add to the given map
     * @param map the map of the type [key -> Collection ]
     * @return true if object is added (or already present) in the correct collection
     */
    public static boolean addUniqueObjectToCollectionMap(Object key, Object value, Map<Object, Set<Object>> map) {
        Set<Object> obj = map.get(key);
        if (obj == null) {
            Set<Object> list = new HashSet<Object>();
            list.add(value);
            return (map.put(key, list) == null);
        }

        // else
        Set<Object> coll = obj;
        coll.add(value);
        return true;
    }

    /**
     * @param object the object to look for
     * @param map the map [key -> collection of objects]
     * @return true if object is contained in one of the lists values of map.
     */
    public static <ValT> boolean isContainedInListMap(ValT object, Map<Object, Collection<ValT>> map) {
        Collection<Collection<ValT>> values = map.values();
        // --- get the iterator on the list of lists
        for (Collection<ValT> list : values) {
            if (list == null) {
                continue;
            }
            if (list.contains(object)) {
                return true; // OK, object found in a list
            }
        }

        return false; // object not found
    }

    /**
     * Removes a value object from the collection obtained with the given key.
     *
     * @param key the key to access a collection of objects
     * @param toBeRemoved the object to remove from the collection
     * @param map the map [key -> collection of objects ]
     * @return true if map has changed as a result of this removing.
     * @see Collection#remove(java.lang.Object)
     */
    public static <KeyT, ValT> boolean removeObjectFromCollectionMap(KeyT key, ValT toBeRemoved,
            Map<KeyT, Collection<ValT>> map) {
        Collection<ValT> obj = map.get(key);
        if (obj == null) {
            return false;
        }

        // else
        Collection<ValT> coll = obj;
        return coll.remove(toBeRemoved);
    }

    /**
     * NOTE Same as removeObjectFromCollectionMap but remove key if Collection is empty.
     *
     * @param key point to the collection in which we must rmove an element
     * @param toBeRemoved the object to removed in collection refered by key
     * @param map the map of collection in which to do removal
     * @return true if after removal collection rezfered to is empty
     * @see MultiMapHelper#removeObjectFromCollectionMap(Object, Object, Map)
     */
    public static <KeyT, ValT> boolean removeAndCleanFromCollectionMap(KeyT key, ValT toBeRemoved,
            Map<KeyT, Collection<ValT>> map) {
        Collection<ValT> obj = map.get(key);
        if (obj == null) {
            return false;
        }
        // else
        Collection<ValT> coll = obj;
        final boolean ret = coll.remove(toBeRemoved);
        if (coll.isEmpty()) {
            map.remove(key);
        }
        return ret;
    }

} // EOC MultiMapHelper
