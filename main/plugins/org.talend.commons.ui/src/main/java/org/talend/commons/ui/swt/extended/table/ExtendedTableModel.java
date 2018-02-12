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
package org.talend.commons.ui.swt.extended.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListenable;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableList;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <B> Type of beans
 */
public class ExtendedTableModel<B> extends AbstractExtendedControlModel {

    private TableViewer tableViewer;

    private ListenableList<B> beansList = new ListenableList<B>();

    private IModifiedBeanListenable<B> modifiedBeanListenable;

    public ExtendedTableModel(String name) {
        super(name);
        init();
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void init() {
        beansList.setUseEquals(false);
    }

    /**
     * Create a new empty list automatically.
     */
    public ExtendedTableModel() {
        super();
        init();
        registerDataList(new ArrayList<B>());
    }

    /**
     * Use the given list to load data.
     * 
     * @param name
     * @param beansList
     */
    public ExtendedTableModel(String name, List<B> dataList) {
        super(name);
        init();
        registerDataList(dataList);
    }

    /**
     * 
     * Insert bean.
     * 
     * @param bean
     * @param index can be null
     */
    public void add(B bean, Integer index) {

        if (index == null || index < 0 || index > this.beansList.size() - 1) {
            if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
                tableViewer.add(bean);
            }
            this.beansList.add(bean);
        } else {
            if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
                tableViewer.insert(bean, index);
            }
            this.beansList.add(index, bean);
        }
    }

    /**
     * 
     * Add bean at end of table.
     * 
     * @param bean
     */
    public void add(B bean) {
        if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
            tableViewer.add(bean);
        }
        this.beansList.add(bean);
    }

    /**
     * DOC amaumont Comment method "add".
     * 
     * @param beans
     * @param index can be null
     */
    public void addAll(final Integer index, List<B> beans) {
        addAll(index, beans, true, true);
    }

    public void addAll(final Integer index, List<B> beans, boolean fireBefore, boolean fireAfter) {
        if (index == null || index < 0 || index > this.beansList.size() - 1) {
            if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
                tableViewer.add(beans.toArray(new Object[beans.size()]));
            }
            this.beansList.addAll(beans, fireBefore, fireAfter);
        } else {
            if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
                int localIndex = index;
                int beansListSize = beans.size();
                for (int i = 0; i < beansListSize; i++) {
                    tableViewer.insert(beans.get(i), localIndex++);
                }
            }
            this.beansList.addAll(index, beans, fireBefore, fireAfter);
        }
    }

    /**
     * DOC amaumont Comment method "add".
     * 
     * @param beans
     * @param index can be null
     */
    public void addAll(List<Integer> indicesWhereAdd, List<B> beans) {
        this.beansList.addAll(indicesWhereAdd, beans);
    }

    /**
     * DOC amaumont Comment method "add".
     * 
     * @param beans
     * @param index can be null
     */
    public void addAll(List<B> beans) {
        addAll((Integer) null, beans);
    }

    public void addAll(List<B> beans, boolean fireBefore, boolean fireAfter) {
        addAll((Integer) null, beans, fireBefore, fireAfter);
    }

    public void registerDataList(List<B> list) {
        this.beansList.registerList(list);
    }

    public List<B> getBeansList() {
        return this.beansList;
    }

    public void addBeans(B b) {
        if (this.beansList.contains(b)) {
        } else {
            this.beansList.add(b);
        }
    }

    public void setUseEquals(boolean useEquals) {
        beansList.setUseEquals(useEquals);
    }

    /**
     * DOC amaumont Comment method "remove".
     * 
     * @param bean
     */
    public boolean remove(B bean) {
        if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
            tableViewer.remove(bean);
        }
        return this.beansList.remove(bean);
    }

    /**
     * DOC amaumont Comment method "remove".
     * 
     * @param i
     */
    public B remove(int index) {
        if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
            tableViewer.remove(this.beansList.get(index));
        }
        return this.beansList.remove(index);
    }

    /**
     * @param c
     * @return
     * @see org.talend.commons.utils.data.list.ListenableList#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<B> c) {
        return removeAll(c, true, true);
    }

    public boolean removeAll(Collection<B> c, boolean fireBefor, boolean fireAfter) {
        if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
            tableViewer.remove(c.toArray(new Object[c.size()]));
        }
        return this.beansList.removeAll(c, fireBefor, fireAfter);
    }

    public void removeAll() {
        if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
            tableViewer.remove(this.beansList.toArray(new Object[this.beansList.size()]));
        }
        this.beansList.clear();
    }

    /**
     * @param object1
     * @param object2
     * @see org.talend.commons.utils.data.list.ListenableList#swapElement(java.lang.Object, java.lang.Object)
     */
    public void swapElement(B object1, B object2) {
        internalSwapElement(object1, object2);
        this.beansList.swapElement(object1, object2);
    }

    /**
     * @param indicesOrigin
     * @param indicesTarget
     * @see org.talend.commons.utils.data.list.ListenableList#swapElements(java.util.List, java.util.List)
     */
    public void swapElements(List<Integer> indicesOrigin, List<Integer> indicesTarget) {
        internalSwapElementIndices(indicesOrigin, indicesTarget);
        this.beansList.swapElements(indicesOrigin, indicesTarget);
    }

    private void internalSwap(int index1, int index2) {

        if (index1 == index2) {
            return;
        }
        // some list implementations don't allow null elements / duplicate elements so we can't swap elements in one
        // statement :(
        Table table = tableViewer.getTable();
        TableItem[] items = table.getItems();
        B temp1 = (B) items[index1].getData();
        B temp2 = (B) items[index2].getData();

        if (index1 > index2) {
            items[index2].setData(temp1);
            items[index1].setData(temp2);
        } else {
            items[index1].setData(temp2);
            items[index2].setData(temp1);
        }
    }

    private void internalSwapElementIndices(List<Integer> indicesOrigin, List<Integer> indicesTarget) {
        if (indicesOrigin.size() != indicesTarget.size()) {
            throw new IllegalArgumentException();
        }
        int lstSize = indicesOrigin.size();
        for (int i = 0; i < lstSize; i++) {
            Integer idxOrigin = indicesOrigin.get(i);
            Integer idxDestination = indicesTarget.get(i);
            internalSwap(idxOrigin, idxDestination);
        }
    }

    private void internalSwapElement(Object object1, Object object2) {
        internalSwap(this.beansList.indexOf(object1), this.beansList.indexOf(object2));
    }

    /**
     * DOC amaumont Comment method "remove".
     * 
     * @param indexArray
     */
    public List<B> remove(int[] indexArray) {
        ArrayList<B> objectsToRemove = new ArrayList<B>(indexArray.length);
        for (int element : indexArray) {
            objectsToRemove.add(beansList.get(element));
        }
        removeAll(objectsToRemove);
        return objectsToRemove;
    }

    /**
     * @param listener
     * @see org.talend.commons.utils.data.list.ListenableList#addPostOperationListener(org.talend.commons.utils.data.list.IListenableListListener)
     */
    public void addAfterOperationListListener(IListenableListListener listener) {
        this.beansList.addPostOperationListener(listener);
    }

    /**
     * @param orderCall
     * @param listener
     * @see org.talend.commons.utils.data.list.ListenableList#addPostOperationListener(int,
     * org.talend.commons.utils.data.list.IListenableListListener)
     */
    public void addAfterOperationListListener(int orderCall, IListenableListListener listener) {
        this.beansList.addPostOperationListener(orderCall, listener);
    }

    /**
     * @param listener
     * @see org.talend.commons.utils.data.list.ListenableList#addBeforeOperationListener(org.talend.commons.utils.data.list.IListenableListListener)
     */
    public void addBeforeOperationListListener(IListenableListListener listener) {
        this.beansList.addBeforeOperationListener(listener);
    }

    /**
     * @param orderCall
     * @param listener
     * @see org.talend.commons.utils.data.list.ListenableList#addBeforeOperationListener(int,
     * org.talend.commons.utils.data.list.IListenableListListener)
     */
    public void addBeforeOperationListListener(int orderCall, IListenableListListener listener) {
        this.beansList.addBeforeOperationListener(orderCall, listener);
    }

    public void removeModifiedListListener(IListenableListListener listenableListListener) {
        this.beansList.removeListener(listenableListListener);
    }

    public void addModifiedBeanListener(IModifiedBeanListener<B> modifiedBeanListener) {
        if (this.modifiedBeanListenable != null) {
            this.modifiedBeanListenable.addModifiedBeanListener(modifiedBeanListener);
        } else {
            throw new IllegalStateException(Messages.getString("ExtendedTableModel.ModifiedObject.Error")); //$NON-NLS-1$
        }
    }

    public void removeModifiedBeanListener(IModifiedBeanListener<B> modifiedBeanListener) {
        if (this.modifiedBeanListenable != null) {
            this.modifiedBeanListenable.removeModifiedBeanListener(modifiedBeanListener);
        } else {
            throw new IllegalStateException(Messages.getString("ExtendedTableModel.ModifiedObject.Error")); //$NON-NLS-1$
        }
    }

    public void setModifiedBeanListenable(IModifiedBeanListenable<B> modifiedBeanListenable) {
        this.modifiedBeanListenable = modifiedBeanListenable;
    }

    @Override
    public boolean isDataRegistered() {
        return beansList.isListRegistered();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedControlModel#release()
     */
    @Override
    public void release() {
    }

    /**
     * DOC amaumont Comment method "getBeanCount".
     * 
     * @return
     */
    public int getBeanCount() {
        if (beansList.isListRegistered()) {
            return beansList.size();
        }
        return 0;
    }

    /**
     * DOC amaumont Comment method "createBeanInstance".
     */
    public B createBeanInstance() {
        return null;
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public void setTableViewer(TableViewer tableViewer) {
        this.tableViewer = tableViewer;
    }

}
