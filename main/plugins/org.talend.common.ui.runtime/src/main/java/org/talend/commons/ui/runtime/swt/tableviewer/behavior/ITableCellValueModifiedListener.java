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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

/**
 * Listener used in <code>TableViewerCreator</code> to notify that a cell value has changed. <br/>
 * 
 * $Id: ITableCellValueModifiedListener.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public interface ITableCellValueModifiedListener {

    /**
     * This method is called after data is stored and <code>TableViewer</code> refreshed.
     * 
     * @param e
     */
    public void cellValueModified(TableCellValueModifiedEvent e);

}
