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
 * A simple container of an array of objects.
 */
public class Tuple {

    private Object[] tuple;

    private int hashCode = 1;

    /**
     * Creates a tuple from the given array of objects. Beware that it does not create a new array, instead it keeps a
     * reference on the given array. If you need to create a new array, see {@link MultipleKey}.
     *
     * @param objects the object array that forms the tuple (must not be null, but may contains null values)
     */
    public Tuple(Object[] objects) {
        assert objects != null;
        this.tuple = objects;

        this.hashCode = computeHashCode();
    }

    /**
     * Getter for tuple.
     *
     * @return the tuple as given in the constructor
     */
    public Object[] getTuple() {
        return this.tuple;
    }

    private int computeHashCode() {
        return Arrays.hashCode(tuple);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Tuple) {
            Object[] otherTuple = ((Tuple) obj).tuple;
            return Arrays.equals(tuple, otherTuple);
        }
        return false;
    }

}
