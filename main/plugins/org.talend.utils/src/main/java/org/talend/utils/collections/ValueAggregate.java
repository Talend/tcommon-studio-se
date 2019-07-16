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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author scorreia
 *
 * This class helps to aggregate values. It can be subclassed.
 * @param <T> a comparable class used as key
 * @param <N> the numeric type to aggregate
 */
public abstract class ValueAggregate<T, N> {

    /**
     * map [key -> array of values].
     */
    protected Map<T, N[]> keyToVal = new HashMap<T, N[]>();

    /**
     * The set of key having at least one null in their resulting values.
     */
    protected Set<T> nullResults = new HashSet<T>();

    /**
     * Method "addValue".
     *
     * @param key the key object to find the aggregated values
     * @param values the values to aggregate
     */
    public abstract void addValue(T key, N[] values);


    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (T key : keyToVal.keySet()) {
            builder.append(key.toString()).append(": "); //$NON-NLS-1$
            final N[] doubles = keyToVal.get(key);
            for (N d : doubles) {
                builder.append(d).append(" "); //$NON-NLS-1$
            }
            builder.append('\n'); //$NON-NLS-1$
        }
        return builder.toString();
    }


}
