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
package org.talend.commons.utils.data.sort;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author amaumont <br/> $Id: MultiplePropertiesBeanComparator.java 33 2006-10-02 15:04:29 +0000 (lun., 02 oct. 2006)
 * amaumont $
 */
public class MultiplePropertiesBeanComparator implements Comparator {

    /**
     * 
     * @author amaumont
     */

    private static final short ASCENDANT_ORDER = 1;

    private static final short DESCENDANT_ORDER = -1;

    private String[] properties;

    private int ascendingInt;

    private boolean ignoreCase;

    public MultiplePropertiesBeanComparator(boolean ascendingOrder, String property) {
        this(ascendingOrder, new String[] { property });
    }

    public MultiplePropertiesBeanComparator(boolean ascendingOrder, String... properties) {
        this.properties = properties;
        this.ascendingInt = ascendingOrder ? ASCENDANT_ORDER : DESCENDANT_ORDER;
    }

    public MultiplePropertiesBeanComparator(String... properties) {
        this(true, properties);
    }

    public MultiplePropertiesBeanComparator(String property) {
        this(new String[] { property });
    }

    public final int compare(Object object1, Object object2) {
        int returnValue = 0;
        if (properties != null) {
            try {
                if (properties.length == 1 && properties[0] != null) {
                    returnValue = compare(properties[0], object1, object2);
                } else {
                    for (int i = 0; i < this.properties.length; i++) {
                        String property = this.properties[i];
                        if (property == null) {
                            continue;
                        }

                        returnValue = compare(property, object1, object2);

                        if (returnValue != 0) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return returnValue;
    }

    private int compare(String property, Object object1, Object object2) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        Object value1 = null;
        Object value2 = null;
        if (property != null && !property.equals("")) { //$NON-NLS-1$
            if (object1 != null) {
                value1 = PropertyUtils.getSimpleProperty(object1, property);
            }
            if (object2 != null) {
                value2 = PropertyUtils.getSimpleProperty(object2, property);
            }
        }
        return checkNullsAndCompare(value1, value2);
    }

    @SuppressWarnings("unchecked")
    private int checkNullsAndCompare(Object object1, Object object2) {
        int returnValue = 0;
        if (object1 instanceof String && object2 instanceof String) {
            returnValue = compareStrings((String) object1, (String) object2);
        } else if (object1 instanceof Comparable && object2 instanceof Comparable) {
            returnValue = ((Comparable) object1).compareTo(object2);
        } else if (object1 != null && object2 != null) {
            // si les objets sont diff�rents de null on les compare avec le
            // toString
            returnValue = compareStrings(object1.toString(), object2.toString());
        } else if (object1 == null && object2 != null) {
            returnValue = 1; // un des deux objet est null, on inverse le tri
        } else if (object1 != null && object2 == null) {
            returnValue = -1; // un des deux objet est null
        } else {
            // les deux objet sont null
            returnValue = 0;
        }

        return ascendingInt * returnValue;
    }

    private int compareStrings(String string1, String string2) {
        if (this.ignoreCase) {
            return string1.compareToIgnoreCase(string2);
        } else {
            return string1.compareTo(string2);
        }
    }

    @SuppressWarnings("unchecked")
    public static void sort(boolean orderAscendant, List<? extends Object> collection, String... propertiesByPriority) {
        Collections.sort(collection, new MultiplePropertiesBeanComparator(orderAscendant, propertiesByPriority));
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public void setProperty(String property) {
        this.properties = new String[] { property };
    }

    public void setAscendingOrder(boolean ascendingOrder) {
        ascendingInt = ascendingOrder ? ASCENDANT_ORDER : DESCENDANT_ORDER;
    }

}
