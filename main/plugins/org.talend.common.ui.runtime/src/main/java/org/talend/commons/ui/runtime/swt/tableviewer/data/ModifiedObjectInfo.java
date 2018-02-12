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
package org.talend.commons.ui.runtime.swt.tableviewer.data;

import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: ModifiedObjectInfo.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 * @param <O> The object type which is modified in your list
 */
public class ModifiedObjectInfo<O> {

    private O currentModifiedBean;

    private TableViewerCreatorColumnNotModifiable currentModifiedColumn;

    private O previousModifiedBean;

    private TableViewerCreatorColumnNotModifiable previousModifiedColumn;

    private Object originalPropertyBeanValue;

    private Object previousPropertyBeanValue;

    private int currentModifiedIndex;

    private int previousModifiedIndex;

    public O getCurrentModifiedBean() {
        return currentModifiedBean;
    }

    public void setCurrentModifiedBean(O object) {
        this.currentModifiedBean = object;
    }

    public Object getOriginalPropertyBeanValue() {
        return this.originalPropertyBeanValue;
    }

    public void setOriginalPropertyBeanValue(Object originalPropertyBeanValue) {
        this.originalPropertyBeanValue = originalPropertyBeanValue;
    }

    public Object getPreviousPropertyBeanValue() {
        return this.previousPropertyBeanValue;
    }

    public void setPreviousPropertyBeanValue(Object previousPropertyBeanValue) {
        this.previousPropertyBeanValue = previousPropertyBeanValue;
    }

    public O getPreviousModifiedBean() {
        return this.previousModifiedBean;
    }

    public void setPreviousModifiedBean(O previousModifiedBean) {
        this.previousModifiedBean = previousModifiedBean;
    }

    public TableViewerCreatorColumnNotModifiable getPreviousModifiedColumn() {
        return this.previousModifiedColumn;
    }

    public void setPreviousModifiedColumn(TableViewerCreatorColumnNotModifiable previousModifiedColumn) {
        this.previousModifiedColumn = previousModifiedColumn;
    }

    public boolean isCurrentCell(Object bean, TableViewerCreatorColumnNotModifiable column) {
        if (bean == currentModifiedBean && column == currentModifiedColumn) {
            return true;
        }
        return false;
    }

    public TableViewerCreatorColumnNotModifiable getCurrentModifiedColumn() {
        return this.currentModifiedColumn;
    }

    public void setCurrentModifiedColumn(TableViewerCreatorColumnNotModifiable currentModifiedColumn) {
        this.currentModifiedColumn = currentModifiedColumn;
    }

    /**
     * DOC amaumont Comment method "setCurrentModifiedIndex".
     * 
     * @param i
     */
    public void setCurrentModifiedIndex(int currentModifiedIndex) {
        this.currentModifiedIndex = currentModifiedIndex;
    }

    public int getCurrentModifiedIndex() {
        return this.currentModifiedIndex;
    }

    public int getPreviousModifiedIndex() {
        return this.previousModifiedIndex;
    }

    public void setPreviousModifiedIndex(int previousModifiedIndex) {
        this.previousModifiedIndex = previousModifiedIndex;
    }

}
