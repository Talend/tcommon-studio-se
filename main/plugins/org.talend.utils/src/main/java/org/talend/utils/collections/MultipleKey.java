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


/**
 * @author scorreia
 *
 * MultipleKey class helps to build keys from an array of objects.
 */
public class MultipleKey implements Comparable<MultipleKey> {

    private Object[] internalKey;

    public MultipleKey(Object[] key, int nbElements) {
        this.internalKey = new Object[nbElements];
        for (int i = 0; i < nbElements; i++) {
            internalKey[i] = key[i];
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MultipleKey)) {
            return false;
        }
        MultipleKey other = (MultipleKey) obj;
        return this.compareTo(other) == 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (Object obj : internalKey) {
            if (obj == null) {
                obj = "null";
            }
            hash += 13 * obj.hashCode();
        }
        return hash;
    }

    public int compareTo(MultipleKey o) {
        if (o == null) {
            return -1;
        }
        int diff = this.internalKey.length - o.internalKey.length;
        if (diff != 0) {
            return diff;
        }
        for (int i = 0; i < internalKey.length; i++) {
            String internalObj = String.valueOf(internalKey[i]);
            String otherObj = String.valueOf(o.internalKey[i]);
            diff = internalObj.compareTo(otherObj);
            if (diff != 0) {
                return diff;
            }
        }

        return diff;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < internalKey.length; i++) {
            Object obj = internalKey[i];
            builder.append(obj);
            if (i < internalKey.length - 1) {
                builder.append(" | "); //$NON-NLS-1$
            }
        }
        return builder.toString();
    }

}
