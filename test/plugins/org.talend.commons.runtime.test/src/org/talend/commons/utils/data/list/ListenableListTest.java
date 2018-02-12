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
package org.talend.commons.utils.data.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableList;
import org.talend.commons.utils.data.list.ListenableListEvent;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: ListenableListTest.java 38252 2010-03-10 05:39:44Z nrousseau $
 * 
 */
public class ListenableListTest {

    /**
     * 
     */
    private static final int A_3 = 3;

    private ListenableList<String> listenedList;

    private boolean added;

    private int count;

    private boolean cleared;

    private boolean removed;

    private boolean swaped;

    private boolean replaced;

    protected ListenableListEvent currentEvent;

    private IListenableListListener listener;

    /**
     * DOC amaumont Comment method "init".
     */
    @Before
    public void init() {

        ArrayList<String> list = new ArrayList<String>();
        listenedList = new ListenableList<String>(list);

    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#ListenableList(java.util.List)}.
     */
    @Test
    public void testListenableList() {

    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#add(java.lang.Object)}.
     */
    @Test
    public void testAddT() {
        assertFalse(added);
        listenedList.add("A"); //$NON-NLS-1$
        assertFalse(added);
        assertEquals("A", listenedList.get(0)); //$NON-NLS-1$
        initListener();
        assertFalse(added);
        listenedList.add("B"); //$NON-NLS-1$
        assertEquals(currentEvent.index, (Integer) 1);
        assertTrue(currentEvent.addedObjects.contains("B")); //$NON-NLS-1$
        assertEquals("B", listenedList.get(1)); //$NON-NLS-1$
        assertTrue(added);
        added = false;
        listenedList.add("C"); //$NON-NLS-1$
        assertEquals("C", listenedList.get(2)); //$NON-NLS-1$
        assertTrue(added);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#add(int, java.lang.Object)}.
     */
    @Test
    public void testAddIntT() {
        assertFalse(added);
        listenedList.add(0, "A"); //$NON-NLS-1$
        assertFalse(added);
        assertEquals("A", listenedList.get(0)); //$NON-NLS-1$
        initListener();
        assertFalse(added);
        listenedList.add(1, "B"); //$NON-NLS-1$
        assertEquals("B", listenedList.get(1)); //$NON-NLS-1$
        assertTrue(added);
        added = false;
        listenedList.add(2, "C"); //$NON-NLS-1$
        assertEquals("C", listenedList.get(2)); //$NON-NLS-1$
        assertTrue(added);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#addAll(java.util.Collection)}.
     */
    @Test
    public void testAddAllCollectionOfQextendsT() {
        listenedList.add(0, "A"); //$NON-NLS-1$
        assertFalse(added);
        ArrayList<String> listToAdd = new ArrayList<String>();
        listToAdd.add("B"); //$NON-NLS-1$
        listToAdd.add("C"); //$NON-NLS-1$

        initListener();
        assertFalse(added);
        listenedList.addAll(listToAdd);
        assertEquals("B", listenedList.get(1)); //$NON-NLS-1$
        assertTrue(added);
        added = false;
        listenedList.addAll(listToAdd);
        assertEquals("B", listenedList.get(A_3)); //$NON-NLS-1$
        assertTrue(added);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#addAll(int, java.util.Collection)}.
     */
    @Test
    public void testAddAllIntCollectionOfQextendsT() {
        listenedList.add(0, "A"); //$NON-NLS-1$
        assertFalse(added);
        ArrayList<String> listToAdd = new ArrayList<String>();
        listToAdd.add("B"); //$NON-NLS-1$
        listToAdd.add("C"); //$NON-NLS-1$

        initListener();
        assertFalse(added);
        listenedList.addAll(0, listToAdd);
        assertEquals("A", listenedList.get(2)); //$NON-NLS-1$
        assertTrue(added);
        added = false;
        listenedList.addAll(1, listToAdd);
        assertEquals("B", listenedList.get(1)); //$NON-NLS-1$
        assertTrue(added);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#clear()}.
     */
    @Test
    public void testClear() {
        listenedList.add(0, "A"); //$NON-NLS-1$
        assertFalse(added);
        ArrayList<String> listToAdd = new ArrayList<String>();
        listToAdd.add("B"); //$NON-NLS-1$
        listToAdd.add("C"); //$NON-NLS-1$
        listenedList.clear();
        assertFalse(cleared);

        initListener();
        assertFalse(cleared);
        listenedList.addAll(listToAdd);
        assertFalse(cleared);
        listenedList.clear();
        assertTrue(cleared);
        cleared = false;
        listenedList.addAll(listToAdd);
        listenedList.clear();
        assertTrue(cleared);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#contains(java.lang.Object)}.
     */
    @Test
    public void testContains() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        assertTrue(listenedList.contains("B")); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#containsAll(java.util.Collection)}.
     */
    @Test
    public void testContainsAll() {
        listenedList.add("A"); //$NON-NLS-1$
        ArrayList<String> listToAdd = new ArrayList<String>();
        listToAdd.add("B"); //$NON-NLS-1$
        listToAdd.add("C"); //$NON-NLS-1$
        listenedList.addAll(listToAdd);
        listenedList.add("D"); //$NON-NLS-1$
        assertTrue(listenedList.containsAll(listToAdd));
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#get(int)}.
     */
    @Test
    public void testGet() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        assertTrue("B".equals(listenedList.get(1))); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#indexOf(java.lang.Object)}.
     */
    @Test
    public void testIndexOf() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        assertTrue(listenedList.indexOf("B") == 1); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(listenedList.isEmpty());
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#iterator()}.
     */
    @Test
    public void testIterator() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        Iterator<String> iterator = listenedList.iterator();
        assertFalse(removed);
        for (int i = 0; iterator.hasNext(); i++) {
            assertEquals(listenedList.get(i), iterator.next());
            if (i == 1) {
                iterator.remove();
                i--;
                assertFalse(removed);
            }
        }
        initListener();
        for (int i = 0; iterator.hasNext(); i++) {
            iterator.next();
            if (i == 1) {
                iterator.remove();
                assertTrue(removed);
            }
        }
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#lastIndexOf(java.lang.Object)}.
     */
    @Test
    public void testLastIndexOf() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        assertEquals(1, listenedList.indexOf("B")); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#listIterator()}.
     */
    @Test
    public void testListIterator() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        ListIterator<String> listIterator = listenedList.listIterator();
        assertFalse(removed);
        assertTrue(listIterator.hasNext());
        assertEquals(listenedList.get(0), listIterator.next());
        assertEquals(listenedList.get(1), listIterator.next());
        listIterator.remove();
        assertFalse(removed);

        initListener();

        assertTrue(listIterator.hasPrevious());

        assertEquals(listIterator.next(), "C"); //$NON-NLS-1$

        assertFalse(added);
        listIterator.add("D"); //$NON-NLS-1$
        assertEquals(listIterator.previousIndex(), 2);
        assertEquals(listIterator.nextIndex(), A_3);
        assertTrue(currentEvent.addedObjects.contains("D")); //$NON-NLS-1$
        assertEquals(currentEvent.index, (Integer) 2);
        assertTrue(added);

        assertFalse(listIterator.hasNext());
        assertTrue(listIterator.hasPrevious());
        assertEquals(listIterator.previous(), "D"); //$NON-NLS-1$

        assertFalse(removed);
        listIterator.remove();
        assertTrue(currentEvent.removedObjects.contains("D")); //$NON-NLS-1$
        assertEquals(currentEvent.index, (Integer) 2);
        assertTrue(removed);

        assertFalse(listIterator.hasNext());
        assertTrue(listIterator.hasPrevious());
        assertEquals(listIterator.previous(), "C"); //$NON-NLS-1$

        assertFalse(replaced);
        listIterator.set("E"); //$NON-NLS-1$
        assertTrue(currentEvent.removedObjects.contains("C")); //$NON-NLS-1$
        assertTrue(currentEvent.addedObjects.contains("E")); //$NON-NLS-1$
        assertEquals(currentEvent.index, (Integer) 1);
        assertTrue(replaced);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#listIterator(int)}.
     */
    @Test
    public void testListIteratorInt() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        ListIterator<String> listIterator = listenedList.listIterator(1);
        assertEquals(listIterator.previousIndex(), 0);
        assertEquals(listIterator.nextIndex(), 1);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#remove(java.lang.Object)}.
     */
    @Test
    public void testRemoveObject() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$

        assertFalse(removed);
        listenedList.remove("B"); //$NON-NLS-1$
        assertFalse(removed);
        listenedList.add("D"); //$NON-NLS-1$

        initListener();

        assertFalse(removed);
        listenedList.remove("D"); //$NON-NLS-1$
        assertTrue(removed);

        assertTrue(currentEvent.removedObjects.contains("D")); //$NON-NLS-1$
        assertEquals(currentEvent.index, (Integer) 2);

    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#remove(int)}.
     */
    @Test
    public void testRemoveInt() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$

        assertFalse(removed);
        listenedList.remove(1);
        assertFalse(removed);
        listenedList.add("D"); //$NON-NLS-1$

        initListener();

        assertFalse(removed);
        listenedList.remove(2);
        assertTrue(removed);

        assertTrue(currentEvent.removedObjects.contains("D")); //$NON-NLS-1$
        assertEquals(currentEvent.index, (Integer) 2);

    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#removeAll(java.util.Collection)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveAll() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        listenedList.add("D"); //$NON-NLS-1$

        assertFalse(removed);
        List<String> objectsToRemove = new ArrayList<String>();
        objectsToRemove.add("B"); //$NON-NLS-1$
        objectsToRemove.add("D"); //$NON-NLS-1$

        listenedList.removeAll(objectsToRemove);
        assertFalse(removed);
        listenedList.add(1, "B"); //$NON-NLS-1$
        listenedList.add("D"); //$NON-NLS-1$

        initListener();

        assertFalse(removed);
        listenedList.removeAll(objectsToRemove);
        assertTrue(removed);

        Iterator<String> iterator = currentEvent.removedObjects.iterator();
        assertEquals(iterator.next(), "B"); //$NON-NLS-1$
        assertEquals(iterator.next(), "D"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#retainAll(java.util.Collection)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRetainAll() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        listenedList.add("D"); //$NON-NLS-1$

        assertFalse(removed);
        List<String> objectsToKeep = new ArrayList<String>();
        objectsToKeep.add("B"); //$NON-NLS-1$
        objectsToKeep.add("D"); //$NON-NLS-1$

        listenedList.retainAll(objectsToKeep);
        assertFalse(removed);
        listenedList.add(0, "A"); //$NON-NLS-1$
        listenedList.add(2, "C"); //$NON-NLS-1$

        initListener();

        assertFalse(removed);
        listenedList.retainAll(objectsToKeep);
        assertTrue(removed);

        Iterator<String> iterator = currentEvent.removedObjects.iterator();
        assertEquals(iterator.next(), "A"); //$NON-NLS-1$
        assertEquals(iterator.next(), "C"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#set(int, java.lang.Object)}.
     */
    @Test
    public void testSet() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$

        assertFalse(replaced);
        listenedList.set(1, "D"); //$NON-NLS-1$
        assertFalse(replaced);

        initListener();

        assertFalse(replaced);
        listenedList.set(1, "E"); //$NON-NLS-1$
        assertTrue(replaced);
        assertTrue(currentEvent.removedObjects.contains("D")); //$NON-NLS-1$
        assertTrue(currentEvent.addedObjects.contains("E")); //$NON-NLS-1$
        assertEquals(currentEvent.index, (Integer) 1);

    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#size()}.
     */
    @Test
    public void testSize() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        assertEquals(listenedList.size(), A_3);
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#subList(int, int)}.
     */
    @Test
    public void testSubList() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        listenedList.add("D"); //$NON-NLS-1$

        List<String> list = listenedList.subList(1, A_3);

        assertEquals(list.get(0), "B"); //$NON-NLS-1$
        assertEquals(list.get(1), "C"); //$NON-NLS-1$

    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#toArray()}.
     */
    @Test
    public void testToArray() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        Object[] array = (Object[]) listenedList.toArray();
        assertEquals(array[0], "A"); //$NON-NLS-1$
        assertEquals(array[1], "B"); //$NON-NLS-1$

    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#toArray(T[])}.
     */
    @Test
    public void testToArrayTArray() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        String[] array = (String[]) listenedList.toArray(new String[0]);
        assertEquals(array[0], "A"); //$NON-NLS-1$
        assertEquals(array[1], "B"); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.commons.utils.data.list.ListenableList#swap(int, int)}.
     */
    @Test
    public void testSwapIntInt() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        assertFalse(swaped);
        listenedList.swap(0, 2);
        assertFalse(swaped);
        assertEquals(listenedList.get(0), "C"); //$NON-NLS-1$
        assertEquals(listenedList.get(2), "A"); //$NON-NLS-1$

        initListener();

        assertFalse(swaped);
        listenedList.swap(0, 2);
        assertTrue(swaped);
        assertEquals(listenedList.get(0), "A"); //$NON-NLS-1$
        assertEquals(listenedList.get(2), "C"); //$NON-NLS-1$

    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.data.list.ListenableList#swapElement(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testSwapObjectObject() {
        listenedList.add("A"); //$NON-NLS-1$
        listenedList.add("B"); //$NON-NLS-1$
        listenedList.add("C"); //$NON-NLS-1$
        assertFalse(swaped);
        listenedList.swapElement("A", "C"); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(swaped);
        assertEquals(listenedList.get(0), "C"); //$NON-NLS-1$
        assertEquals(listenedList.get(2), "A"); //$NON-NLS-1$

        initListener();

        assertFalse(swaped);
        listenedList.swapElement("A", "C"); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue(swaped);
        assertEquals(listenedList.get(0), "A"); //$NON-NLS-1$
        assertEquals(listenedList.get(2), "C"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.data.list.ListenableList#addListener(org.talend.commons.utils.data.list.IListenableListListener)}
     * .
     */
    @Test
    public void testAddListener() {

        count = 0;

        assertFalse(added);
        listenedList.add("A"); //$NON-NLS-1$
        assertFalse(added);

        assertEquals(count, 0);

        initListener();

        assertFalse(added);
        listenedList.add("C"); //$NON-NLS-1$
        assertTrue(added);
        added = false;

        assertEquals(count, 1);

        assertFalse(added);
        listenedList.add("B"); //$NON-NLS-1$
        assertTrue(added);
        added = false;

        assertEquals(count, 2);

        assertFalse(added);
        listenedList.add("C"); //$NON-NLS-1$
        assertTrue(added);
        added = false;

        assertEquals(count, 3);

        listenedList.addPostOperationListener(listener);

        assertFalse(added);
        listenedList.add("B"); //$NON-NLS-1$
        assertTrue(added);
        added = false;

        assertEquals(count, 4);

        listenedList.removeListener(listener);

        assertFalse(added);
        listenedList.add("C"); //$NON-NLS-1$
        assertFalse(added);

        assertEquals(count, 4);

        // 0 listener registered

        listenedList.addPostOperationListener(listener);

        // 1 listener registered

        initListener();

        // 2 listeners registered

        assertFalse(added);
        listenedList.add("B"); //$NON-NLS-1$
        assertTrue(added);
        added = false;

        assertEquals(count, 6);

    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.data.list.ListenableList#removeListener(org.talend.commons.utils.data.list.IListenableListListener)}
     * .
     */
    @Test
    public void testRemoveListener() {
        assertFalse(added);
        listenedList.add("A"); //$NON-NLS-1$
        assertFalse(added);

        initListener();

        assertFalse(added);
        listenedList.add("B"); //$NON-NLS-1$
        assertTrue(added);
        added = false;

        listenedList.removeListener(listener);

        assertFalse(added);
        listenedList.add("C"); //$NON-NLS-1$
        assertFalse(added);
    }

    private void initListener() {
        listener = new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                count++;
                switch (event.type) {

                case ADDED:
                    added = true;
                    break;
                case CLEARED:
                    cleared = true;
                    break;
                case REMOVED:
                    removed = true;
                    break;
                case REPLACED:
                    replaced = true;
                    break;
                case SWAPED:
                    swaped = true;
                    break;
                default:
                    fail("event type unknown"); //$NON-NLS-1$
                }
                currentEvent = event;

            }

        };
        listenedList.addPostOperationListener(listener);

    }
}
