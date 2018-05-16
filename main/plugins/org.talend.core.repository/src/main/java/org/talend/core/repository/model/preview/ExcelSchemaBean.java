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
package org.talend.core.repository.model.preview;

import java.util.ArrayList;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class ExcelSchemaBean {

    private String firstColumn = null;

    private String lastColumn = null;

    private boolean advancedSeparator = false;

    private String thousandSeparator = null;

    private String decimalSeparator = null;

    private boolean dieOnError = false;

    private String sheetName = null;

    private boolean selectAllSheets = false;

    private ArrayList sheetsList = null;

    private String generationMode = null;

    /**
     * Getter for advancedSeparator.
     * 
     * @return the advancedSeparator
     */
    public boolean isAdvancedSeparator() {
        return this.advancedSeparator;
    }

    /**
     * Sets the advancedSeparator.
     * 
     * @param advancedSeparator the advancedSeparator to set
     */
    public void setAdvancedSeparator(boolean advancedSeparator) {
        this.advancedSeparator = advancedSeparator;
    }

    /**
     * Getter for thousandSeparator.
     * 
     * @return the thousandSeparator
     */
    public String getThousandSeparator() {
        return this.thousandSeparator;
    }

    /**
     * Sets the thousandSeparator.
     * 
     * @param thousandSeparator the thousandSeparator to set
     */
    public void setThousandSeparator(String thousandSeparator) {
        this.thousandSeparator = thousandSeparator;
    }

    /**
     * Getter for decimalSeparator.
     * 
     * @return the decimalSeparator
     */
    public String getDecimalSeparator() {
        return this.decimalSeparator;
    }

    /**
     * Sets the decimalSeparator.
     * 
     * @param decimalSeparator the decimalSeparator to set
     */
    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    /**
     * Getter for firstColumn.
     * 
     * @return the firstColumn
     */
    public String getFirstColumn() {
        return this.firstColumn;
    }

    /**
     * Sets the firstColumn.
     * 
     * @param firstColumn the firstColumn to set
     */
    public void setFirstColumn(String firstColumn) {
        this.firstColumn = firstColumn;
    }

    /**
     * Getter for lastColumn.
     * 
     * @return the lastColumn
     */
    public String getLastColumn() {
        return this.lastColumn;
    }

    /**
     * Sets the lastColumn.
     * 
     * @param lastColumn the lastColumn to set
     */
    public void setLastColumn(String lastColumn) {
        this.lastColumn = lastColumn;
    }

    /**
     * Getter for dieOnError.
     * 
     * @return the dieOnError
     */
    public boolean isDieOnError() {
        return this.dieOnError;
    }

    /**
     * Sets the dieOnError.
     * 
     * @param dieOnError the dieOnError to set
     */
    public void setDieOnError(boolean dieOnError) {
        this.dieOnError = dieOnError;
    }

    /**
     * Getter for sheetName.
     * 
     * @return the sheetName
     */
    public String getSheetName() {
        return this.sheetName;
    }

    /**
     * Sets the sheetName.
     * 
     * @param sheetName the sheetName to set
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * Getter for selectAllSheets.
     * 
     * @return the selectAllSheets
     */
    public boolean isSelectAllSheets() {
        return this.selectAllSheets;
    }

    /**
     * Sets the selectAllSheets.
     * 
     * @param selectAllSheets the selectAllSheets to set
     */
    public void setSelectAllSheets(boolean selectAllSheets) {
        this.selectAllSheets = selectAllSheets;
    }

    /**
     * Getter for sheetsList.
     * 
     * @return the sheetsList
     */
    public ArrayList getSheetsList() {
        return this.sheetsList;
    }

    /**
     * Sets the sheetsList.
     * 
     * @param sheetsList the sheetsList to set
     */
    public void setSheetsList(ArrayList sheetsList) {
        this.sheetsList = sheetsList;
    }

    public String getGenerationMode() {
        return generationMode;
    }

    public void setGenerationMode(String generationMode) {
        this.generationMode = generationMode;
    }

}
