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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.data.AccessorUtils;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: DefaultTableLabelProvider.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class DefaultTableLabelProvider implements ITableLabelProvider, ITableColorProvider {

    protected TableViewerCreatorNotModifiable tableViewerCreator;

    public DefaultTableLabelProvider(TableViewerCreatorNotModifiable tableViewerCreator) {
        super();
        this.tableViewerCreator = tableViewerCreator;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        TableViewerCreatorColumnNotModifiable column = (TableViewerCreatorColumnNotModifiable) this.tableViewerCreator
                .getColumns().get(columnIndex);
        if (column.getImageProvider() != null) {
            return column.getImageProvider().getImage(element);
        }
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        String returnValue = null;
        TableViewerCreatorColumnNotModifiable column = (TableViewerCreatorColumnNotModifiable) this.tableViewerCreator
                .getColumns().get(columnIndex);

        if (column.getLabelProvider() != null) {
            returnValue = column.getLabelProvider().getLabel(element);
        }

        if (returnValue == null) {
            if (column.getDisplayedValue() != null || column.getDefaultDisplayedValue() != null
                    || column.getBeanPropertyAccessors() == null) {
                String defaultValue = column.getDefaultDisplayedValue();
                String imposedDisplayedValue = column.getDisplayedValue();
                if (imposedDisplayedValue != null) {
                    returnValue = imposedDisplayedValue;
                } else if (defaultValue == null) {
                    returnValue = ""; //$NON-NLS-1$
                } else {
                    returnValue = defaultValue;
                }
            } else {
                Object value = AccessorUtils.get(element, column);
                CellEditor cellEditor = column.getCellEditor();
                CellEditorValueAdapter retrieverValue = column.getCellEditorValueAdapter();
                // add for bug TDI-21505
                if (value != null && column.getCellEditorValueAdapter() != null
                        && column.getCellEditorValueAdapter() instanceof ComboEditorValueAdapter) {
                    Object returnObject = column.getCellEditorValueAdapter().getCellEditorTypedValue(column.getCellEditor(),
                            value);
                    if ("-1".equals(returnObject + "")) {
                        return "";
                    }
                }
                if (cellEditor != null && retrieverValue != null && value != null) {
                    returnValue = retrieverValue.getColumnText(cellEditor, element, value);
                } else if (value != null) {
                    returnValue = String.valueOf(value);
                } else {
                    returnValue = ""; //$NON-NLS-1$
                }
            }
        }
        return returnValue;
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener lpl) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    public Color getBackground(Object element, int columnIndex) {
        TableViewerCreatorColumnNotModifiable column = (TableViewerCreatorColumnNotModifiable) this.tableViewerCreator
                .getColumns().get(columnIndex);
        if (column.getColorProvider() != null) {
            return column.getColorProvider().getBackgroundColor(element);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
     */
    public Color getForeground(Object element, int columnIndex) {
        TableViewerCreatorColumnNotModifiable column = (TableViewerCreatorColumnNotModifiable) this.tableViewerCreator
                .getColumns().get(columnIndex);
        if (column.getColorProvider() != null) {
            return column.getColorProvider().getForegroundColor(element);
        }
        return null;
    }
}
