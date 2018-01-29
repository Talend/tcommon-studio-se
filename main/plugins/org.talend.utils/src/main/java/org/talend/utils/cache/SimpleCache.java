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
package org.talend.utils.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * DOC amaumont class global comment. Detailled comment
 * 
 * @param <K> key
 * @param <V> value
 */
public class SimpleCache<K, V> {

    /**
     * 
     * DOC amaumont SimpleCache class global comment. Detailled comment
     * 
     * @param <K> key
     */
    class HashKeyValue<K, V> implements Comparable<HashKeyValue<K, V>> {

        private K key;

        private V value;

        private long addTime;

        public HashKeyValue(K key) {
            this.key = key;
            this.addTime = System.currentTimeMillis();
        }

        public HashKeyValue(K key, V value) {
            this(key);
            this.value = value;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            HashKeyValue other = (HashKeyValue) obj;
            if (key == null) {
                if (other.key != null) {
                    return false;
                }
            } else if (!key.equals(other.key)) {
                return false;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(HashKeyValue<K, V> o) {
            if (this.equals(o)) {
                return 0;
            }
            return (getAddTime() < o.getAddTime()) ? -1 : 1;
        }

        /**
         * Getter for value.
         * 
         * @return the value
         */
        public V getValue() {
            return value;
        }

        /**
         * Getter for key.
         * 
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Getter for addTime.
         * 
         * @return the addTime
         */
        public long getAddTime() {
            return addTime;
        }

    }

    private Set<HashKeyValue<K, V>> keysOrderedByPutTime = new TreeSet<HashKeyValue<K, V>>();

    private Map<HashKeyValue<K, V>, HashKeyValue<K, V>> cache = new HashMap<HashKeyValue<K, V>, HashKeyValue<K, V>>();

    private Integer maxItems;

    private Long maxTime;

    /**
     * 
     * Constructor SimpleCache.
     * 
     * @param maxTime in ms, a value greater than <code>0</code> to enable the <code>maxTime</code>, a negative or
     * Integer.MAX_VALUE or Long.MAX_VALUE value means infinite.
     * @param maxItems max number of items to keep in the cache, a value greater than <code>0</code> to enable the
     * <code>maxItems</code>, a negative value or <code>Integer.MAX_VALUE</code> to mean infinite.
     * @throws IllegalArgumentException if you try to disable both <code>maxTime</code> and <code>maxItems</code>
     */
    public SimpleCache(long maxTime, int maxItems) {
        super();
        this.maxTime = maxTime;
        this.maxItems = maxItems;
        if ((this.maxTime == null || this.maxTime < 0 || this.maxTime == Long.MAX_VALUE || this.maxTime == Integer.MAX_VALUE)
                && (this.maxItems == null || this.maxItems < 0 || this.maxItems == Integer.MAX_VALUE)) {
            throw new IllegalArgumentException(
                    "At least one of maxTime or maxItems must be a value greater or equals 0, excepted the MAX_VALUE");
        }
    }

    public synchronized V get(K key) {
        HashKeyValue<K, V> internalKey = new HashKeyValue<K, V>(key);
        HashKeyValue<K, V> keyValue = cache.get(internalKey);
        if (keyValue != null) {
            return keyValue.getValue();
        } else {
            return null;
        }
    }

    public synchronized Long getAddTime(K key) {
        HashKeyValue<K, V> internalKey = new HashKeyValue<K, V>(key);
        HashKeyValue<K, V> keyValue = cache.get(internalKey);
        if (keyValue != null) {
            return keyValue.getAddTime();
        } else {
            return null;
        }
    }

    /**
     * 
     * Method "put".
     * 
     * The clean is done when putting new data.
     * 
     * @param key
     * @param value
     * @return previous value for the same key
     */
    public synchronized V put(K key, V value) {
        HashKeyValue<K, V> internalKeyValue = new HashKeyValue<K, V>(key, value);
        keysOrderedByPutTime.add(internalKeyValue);
        HashKeyValue<K, V> previousKeyValue = cache.put(internalKeyValue, internalKeyValue);
        int sizeItems = keysOrderedByPutTime.size();
        if (maxTime != null && maxTime >= 0 && maxTime != Long.MAX_VALUE && sizeItems > 0) {
            for (Iterator<HashKeyValue<K, V>> keysOrderedByPutTimeIterator = keysOrderedByPutTime.iterator(); keysOrderedByPutTimeIterator
                    .hasNext();) {
                long currentTimeMillis = System.currentTimeMillis();
                HashKeyValue<K, V> hashKey = keysOrderedByPutTimeIterator.next();
                // System.out.println("hashKey=" + hashKey.getKey() + ", hashKey.addTime=" + hashKey.addTime
                // + ", currentTimeMillis=" + currentTimeMillis + ", maxTime=" + maxTime);
                if (currentTimeMillis - hashKey.addTime >= maxTime) {
                    keysOrderedByPutTimeIterator.remove();
                    cache.remove(hashKey);
                } else {
                    break;
                }
            }
        }
        sizeItems = keysOrderedByPutTime.size();
        if (maxItems != null && maxItems >= 0 && maxItems != Integer.MAX_VALUE && sizeItems > maxItems && sizeItems > 0) {
            Iterator<HashKeyValue<K, V>> setIterator = keysOrderedByPutTime.iterator();
            if (setIterator.hasNext()) {
                HashKeyValue<K, V> toRemoveFromMap = setIterator.next();
                setIterator.remove();
                cache.remove(toRemoveFromMap);
            }
        }
        if (previousKeyValue != null) {
            return previousKeyValue.getValue();
        } else {
            return null;
        }
    }

    public synchronized int size() {
        return cache.size();
    }

    protected synchronized int internalTimeListSize() {
        return keysOrderedByPutTime.size();
    }

}
