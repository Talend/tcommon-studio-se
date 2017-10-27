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
package org.talend.core.ui.context;

import org.eclipse.nebula.widgets.nattable.sort.SortDirectionEnum;

/**
 * created by Talend on Jul 9, 2014 Detailled comment
 * 
 */
public class SortState {

    private SortDirectionEnum sortDirection = SortDirectionEnum.NONE;

    private int selectedColumnIndex = -1;

    private String selectedColumnName = null;

    // because the grp_size is String type, so need special operation before sorting
    private int grpSizeIndex = -1;

    // if the current column is the first time to click, should reset the sort direction
    // if the clicked column=current selected column, only need to get next direction
    private boolean isColumnChanged = Boolean.FALSE;

    private boolean isSortEnable = Boolean.FALSE;

    public void reset() {
        sortDirection = SortDirectionEnum.NONE;
        selectedColumnIndex = -1;
        isColumnChanged = Boolean.FALSE;
        grpSizeIndex = -1;
        isSortEnable = Boolean.FALSE;
        selectedColumnName = null;
    }

    public String getSelectedColumnName() {
        return this.selectedColumnName;
    }

    public void setSelectedColumnName(String selectedColumnName) {
        this.selectedColumnName = selectedColumnName;
    }

    public SortDirectionEnum getNextSortDirection() {
        if (isColumnChanged) {
            // get the first direction when column changed
            sortDirection = SortDirectionEnum.ASC;
        } else {
            sortDirection = sortDirection.getNextSortDirection();
        }
        // only when get direction, means that current is sort enabled.
        isSortEnable = Boolean.TRUE;
        return this.sortDirection;
    }

    public SortDirectionEnum getCurrentSortDirection() {
        return this.sortDirection;
    }

    public int getSelectedColumnIndex() {
        return this.selectedColumnIndex;
    }

    public void setSelectedColumnIndex(int clickedColumnIndex) {
        // if the selected column changed, set isColumnChanged= true
        if (clickedColumnIndex != selectedColumnIndex) {
            isColumnChanged = Boolean.TRUE;
        } else {
            isColumnChanged = Boolean.FALSE;
        }
        this.selectedColumnIndex = clickedColumnIndex;
        // every column header click will set the column index
        isSortEnable = Boolean.FALSE;
    }

    public void setGrpSizeIndex(int grpSizeIndex) {
        this.grpSizeIndex = grpSizeIndex;
    }

    public int getGrpSizeIndex() {
        return this.grpSizeIndex;
    }

    public boolean isGroupSizeColumn() {
        return grpSizeIndex == selectedColumnIndex;
    }

    public boolean isSortActive() {
        return this.isSortEnable;
    }

    /**
     * check if the given column is the current selected column
     * 
     * @param currentColumnName
     * @return
     */
    public boolean isSelectedColumn(String columnName) {
        if (this.selectedColumnName != null) {
            return this.selectedColumnName.equalsIgnoreCase(columnName);
        }
        return false;
    }

    public void resetSelectedColumn() {
        this.selectedColumnIndex = -1;
        this.selectedColumnName = null;
    }
}
