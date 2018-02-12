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
package org.talend.repository.view.sorter;

import static org.junit.Assert.assertEquals;

import org.eclipse.jface.viewers.Viewer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryCompareSorterTest {

    private RepositoryCompareSorter testSorter;

    private String obj1 = "Obj1", obj2 = "Obj2", obj3 = "Obj3", obj4 = "Obj4";

    private Object[] children;

    @Before
    public void before() {
        testSorter = new RepositoryCompareSorter() {

            @Override
            public void sort(Viewer viewer, Object parentPath, Object[] children) {
                //
            }

            @Override
            protected void swap(Object[] children, int source, int target) {
                super.swap(children, source, target);
            }

        };
        resetChildren();
    }

    private void resetChildren() {
        children = new Object[] { obj1, obj2, obj3, obj4 };
    }

    @After
    public void after() {
        testSorter = null;
        children = null;
    }

    @Test
    public void testSwap_empty() {
        Object[] c = new Object[0];
        testSorter.swap(c, 0, 0);
        assertEquals(c.length, 0);
    }

    private void checkChildren(Object data1, Object data2, Object data3, Object data4) {
        assertEquals(children[0], data1);
        assertEquals(children[1], data2);
        assertEquals(children[2], data3);
        assertEquals(children[3], data4);
    }

    @Test
    public void testSwap_SameIndex() {
        testSorter.swap(children, 0, 0);
        checkChildren(obj1, obj2, obj3, obj4); // keep original

        testSorter.swap(children, 1, 1);
        checkChildren(obj1, obj2, obj3, obj4); // keep original

        testSorter.swap(children, 2, 2);
        checkChildren(obj1, obj2, obj3, obj4); // keep original

        testSorter.swap(children, 3, 3);
        checkChildren(obj1, obj2, obj3, obj4); // keep original
    }

    @Test
    public void testSwap_WrongIndex() {
        // -1
        testSorter.swap(children, -1, 0);
        checkChildren(obj1, obj2, obj3, obj4); // keep original

        testSorter.swap(children, 0, -1);
        checkChildren(obj1, obj2, obj3, obj4); // keep original
    }

    @Test
    public void testSwap_OutOfBounds() {
        testSorter.swap(children, 4, 0);
        checkChildren(obj1, obj2, obj3, obj4); // keep original

        testSorter.swap(children, 0, 4);
        checkChildren(obj1, obj2, obj3, obj4); // keep original
    }

    @Test
    public void testSwap_Down() {
        testSorter.swap(children, 0, 1);
        checkChildren(obj2, obj1, obj3, obj4);

        resetChildren();
        testSorter.swap(children, 0, 2);
        checkChildren(obj2, obj3, obj1, obj4);

        resetChildren();
        testSorter.swap(children, 0, 3);
        checkChildren(obj2, obj3, obj4, obj1);

        resetChildren();
        testSorter.swap(children, 1, 2);
        checkChildren(obj1, obj3, obj2, obj4);

        resetChildren();
        testSorter.swap(children, 1, 3);
        checkChildren(obj1, obj3, obj4, obj2);

        resetChildren();
        testSorter.swap(children, 2, 3);
        checkChildren(obj1, obj2, obj4, obj3);
    }

    @Test
    public void testSwap_Up() {
        testSorter.swap(children, 3, 0);
        checkChildren(obj4, obj1, obj2, obj3);

        resetChildren();
        testSorter.swap(children, 3, 1);
        checkChildren(obj1, obj4, obj2, obj3);

        resetChildren();
        testSorter.swap(children, 3, 2);
        checkChildren(obj1, obj2, obj4, obj3);

        resetChildren();
        testSorter.swap(children, 2, 0);
        checkChildren(obj3, obj1, obj2, obj4);

        resetChildren();
        testSorter.swap(children, 2, 1);
        checkChildren(obj1, obj3, obj2, obj4);

        resetChildren();
        testSorter.swap(children, 1, 0);
        checkChildren(obj2, obj1, obj3, obj4);

    }

}
