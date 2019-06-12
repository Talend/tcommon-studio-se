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

import java.util.Arrays;

/**
 * @author scorreia
 *
 * This class aggregates doubles.
 * @param <T> the key class
 */
public class DoubleValueAggregate<T> extends ValueAggregate<T, Double> {

    /*
     * (non-Javadoc)
     *
     * @see org.talend.utils.collections.ValueAggregate#addValue(java.lang.Object, N[])
     */
    public void addValue(T key, Double[] values) {
        Double[] doubles = keyToVal.get(key);
        if (doubles == null) {
            doubles = new Double[values.length];
            Arrays.fill(doubles, 0.0);
        }

        for (int i = 0; i < values.length; i++) {
            Double d = values[i];
            if (d == null) {
                nullResults.add(key);
                return;
            }
            // else add values
            doubles[i] += d;
        }
        keyToVal.put(key, doubles);
    }

}
