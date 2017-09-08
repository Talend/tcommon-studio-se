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
package org.talend.commons.ui.runtime.swt.tableviewer.sort;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.data.AccessorUtils;

/**
 * @author amaumont <br/> $Id: MultipleColumnsBeanComparator.java 294 2006-11-02 08:27:05 +0000 (jeu., 02 nov. 2006)
 * smallet $
 */
public class MultipleColumnsBeanComparator implements Comparator {

    /**
     * 
     * @author amaumont
     */

    private static final short ASCENDANT_ORDER = 1;

    private static final short DESCENDANT_ORDER = -1;

    private TableViewerCreatorColumnNotModifiable[] columnsToOrder;

    private int ascendingInt;

    private boolean ignoreCase;

    public MultipleColumnsBeanComparator(boolean ascendingOrder, TableViewerCreatorColumnNotModifiable column) {
        this(ascendingOrder, new TableViewerCreatorColumnNotModifiable[] { column });
    }

    public MultipleColumnsBeanComparator(boolean ascendingOrder, TableViewerCreatorColumnNotModifiable... columns) {
        this.columnsToOrder = columns;
        this.ascendingInt = ascendingOrder ? ASCENDANT_ORDER : DESCENDANT_ORDER;
    }

    public MultipleColumnsBeanComparator(TableViewerCreatorColumnNotModifiable... columnsToOrder) {
        this(true, columnsToOrder);
    }

    public MultipleColumnsBeanComparator(TableViewerCreatorColumnNotModifiable columnToOrder) {
        this(new TableViewerCreatorColumnNotModifiable[] { columnToOrder });
    }

    public final int compare(Object object1, Object object2) {
        int returnValue = 0;
        if (columnsToOrder != null) {
            try {
                if (columnsToOrder.length == 1 && columnsToOrder[0] != null) {
                    returnValue = compare(columnsToOrder[0], object1, object2);
                } else {
                    for (int i = 0; i < this.columnsToOrder.length; i++) {
                        TableViewerCreatorColumnNotModifiable property = this.columnsToOrder[i];
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

    private int compare(TableViewerCreatorColumnNotModifiable column, Object object1, Object object2) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        Object value1 = null;
        Object value2 = null;
        if (column != null) {
            if (object1 != null) {
                value1 = AccessorUtils.get(object1, column);
            }
            if (object2 != null) {
                value2 = AccessorUtils.get(object2, column);
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
    public static void sort(boolean orderAscendant, List<? extends Object> collection,
            TableViewerCreatorColumnNotModifiable... propertiesByPriority) {
        Collections.sort(collection, new MultipleColumnsBeanComparator(orderAscendant, propertiesByPriority));
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public TableViewerCreatorColumnNotModifiable[] getColumnsToOrder() {
        return columnsToOrder;
    }

    public void setColumnsToOrder(TableViewerCreatorColumnNotModifiable[] columnsToOrder) {
        this.columnsToOrder = columnsToOrder;
    }

    public void setColumnToOrder(TableViewerCreatorColumnNotModifiable property) {
        this.columnsToOrder = new TableViewerCreatorColumnNotModifiable[] { property };
    }

    public void setAscendingOrder(boolean ascendingOrder) {
        ascendingInt = ascendingOrder ? ASCENDANT_ORDER : DESCENDANT_ORDER;
    }

}
