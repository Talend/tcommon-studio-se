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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author scorreia
 *
 * This class helps to sort a map according to its values.
 */
public class MapValueSorter {

    /**
     * this comparator sorts values in ascending order.
     */
    private static class AscByValueComparator implements Comparator<Object> {

        private Map<Object, Long> baseMap;

        /**
         * AscByValueComparator constructor.
         *
         * @param map the map to be sorted according to its values
         */
        public AscByValueComparator(Map<Object, Long> map) {
            this.baseMap = map;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object arg0, Object arg1) {
            Long value0 = baseMap.get(arg0);
            if (value0 == null) {
                return -1;
            }
            // else
            Long value1 = baseMap.get(arg1);
            return value0.compareTo(value1);
        }
    }

    /**
     * this comparator sorts values in descending order.
     */
    private static class DescByValueComparator implements Comparator<Object> {

        private Map<Object, Long> baseMap;

        /**
         * DescByValueComparator constructor.
         *
         * @param map the map to be sorted according to its values
         */
        public DescByValueComparator(Map<Object, Long> map) {
            this.baseMap = map;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object arg0, Object arg1) {
            Long value1 = baseMap.get(arg1);
            if (value1 == null) {
                return -1;
            }
            Long value0 = baseMap.get(arg0);
            // else
            return value1.compareTo(value0);
        }
    }


    /**
     * Method "getMostFrequent".
     *
     * @param map the input map to be sorted
     * @param topN this parameter filters the most frequent elements
     * @return the list of most frequent keys according to the number values.
     */
    public List<Object> getMostFrequent(Map<Object, Long> map, int topN) {
        final List<Object> sortedMap = sortMap(map, false);
        return getFirstElements(sortedMap, topN);
    }

    /**
     * Method "getLessFrequent".
     *
     * @param map the input map to be sorted
     * @param bottomN this parameter filters the less frequent elements
     * @return the list of less frequent keys according to the number values.
     */
    public List<Object> getLessFrequent(Map<Object, Long> map, int bottomN) {
        final List<Object> sortedMap = sortMap(map, true);
        return getFirstElements(sortedMap, bottomN);
    }

    /**
     * DOC scorreia Comment method "getFirstElements".
     * @param bottomN
     * @param sortedMap
     * @return
     */
    private List<Object> getFirstElements(final List<Object> sortedMap, int bottomN) {
        List<Object> mostFrequent = new ArrayList<Object>();
        int i = 0;
        for (Iterator<Object> iterator = sortedMap.iterator(); iterator.hasNext();) {
            Object object = iterator.next();
            mostFrequent.add(object);
            i++;
            if (i == bottomN) {
                break;
            }
        }
        return mostFrequent;
    }

    /**
     * Method "sortMap".
     *
     * @param map the input map (the values being the elements on which the sort is done)
     * @param ascendingOrder the order of the sort
     * @return the list of sorted keys
     */
    public List<Object> sortMap(Map<Object, Long> map, boolean ascendingOrder) {
        Comparator<Object> cmp = ascendingOrder ? new AscByValueComparator(map) : new DescByValueComparator(map);
        List<Object> keySet = new ArrayList<Object>(map.keySet());
        Collections.sort(keySet, cmp);
        return keySet;
    }


}
