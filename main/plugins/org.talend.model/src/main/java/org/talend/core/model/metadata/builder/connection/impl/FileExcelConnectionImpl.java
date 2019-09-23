/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File Excel Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getSheetName <em>Sheet Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getSheetColumns <em>Sheet Columns</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getFirstColumn <em>First Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getLastColumn <em>Last Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getThousandSeparator <em>Thousand Separator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getDecimalSeparator <em>Decimal Separator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#isAdvancedSpearator <em>Advanced Spearator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#isSelectAllSheets <em>Select All Sheets</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getSheetList <em>Sheet List</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.FileExcelConnectionImpl#getGenerationMode <em>Generation Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FileExcelConnectionImpl extends FileConnectionImpl implements FileExcelConnection {

    /**
     * The default value of the '{@link #getSheetName() <em>Sheet Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSheetName()
     * @generated
     * @ordered
     */
    protected static final String SHEET_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSheetName() <em>Sheet Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSheetName()
     * @generated
     * @ordered
     */
    protected String sheetName = SHEET_NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getSheetColumns() <em>Sheet Columns</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSheetColumns()
     * @generated
     * @ordered
     */
    protected EList<String> sheetColumns;

    /**
     * The default value of the '{@link #getFirstColumn() <em>First Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFirstColumn()
     * @generated
     * @ordered
     */
    protected static final String FIRST_COLUMN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFirstColumn() <em>First Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFirstColumn()
     * @generated
     * @ordered
     */
    protected String firstColumn = FIRST_COLUMN_EDEFAULT;

    /**
     * The default value of the '{@link #getLastColumn() <em>Last Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLastColumn()
     * @generated
     * @ordered
     */
    protected static final String LAST_COLUMN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLastColumn() <em>Last Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLastColumn()
     * @generated
     * @ordered
     */
    protected String lastColumn = LAST_COLUMN_EDEFAULT;

    /**
     * The default value of the '{@link #getThousandSeparator() <em>Thousand Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getThousandSeparator()
     * @generated
     * @ordered
     */
    protected static final String THOUSAND_SEPARATOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getThousandSeparator() <em>Thousand Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getThousandSeparator()
     * @generated
     * @ordered
     */
    protected String thousandSeparator = THOUSAND_SEPARATOR_EDEFAULT;

    /**
     * The default value of the '{@link #getDecimalSeparator() <em>Decimal Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDecimalSeparator()
     * @generated
     * @ordered
     */
    protected static final String DECIMAL_SEPARATOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDecimalSeparator() <em>Decimal Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDecimalSeparator()
     * @generated
     * @ordered
     */
    protected String decimalSeparator = DECIMAL_SEPARATOR_EDEFAULT;

    /**
     * The default value of the '{@link #isAdvancedSpearator() <em>Advanced Spearator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAdvancedSpearator()
     * @generated
     * @ordered
     */
    protected static final boolean ADVANCED_SPEARATOR_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAdvancedSpearator() <em>Advanced Spearator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAdvancedSpearator()
     * @generated
     * @ordered
     */
    protected boolean advancedSpearator = ADVANCED_SPEARATOR_EDEFAULT;

    /**
     * The default value of the '{@link #isSelectAllSheets() <em>Select All Sheets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSelectAllSheets()
     * @generated
     * @ordered
     */
    protected static final boolean SELECT_ALL_SHEETS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSelectAllSheets() <em>Select All Sheets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSelectAllSheets()
     * @generated
     * @ordered
     */
    protected boolean selectAllSheets = SELECT_ALL_SHEETS_EDEFAULT;

    /**
     * The default value of the '{@link #getSheetList() <em>Sheet List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSheetList()
     * @generated
     * @ordered
     */
    protected static final ArrayList SHEET_LIST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSheetList() <em>Sheet List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSheetList()
     * @generated
     * @ordered
     */
    protected ArrayList sheetList = SHEET_LIST_EDEFAULT;

    /**
     * The default value of the '{@link #getGenerationMode() <em>Generation Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGenerationMode()
     * @generated
     * @ordered
     */
    protected static final String GENERATION_MODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGenerationMode() <em>Generation Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGenerationMode()
     * @generated
     * @ordered
     */
    protected String generationMode = GENERATION_MODE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FileExcelConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.FILE_EXCEL_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSheetName(String newSheetName) {
        String oldSheetName = sheetName;
        sheetName = newSheetName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_NAME,
                    oldSheetName, sheetName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getSheetColumns() {
        if (sheetColumns == null) {
            sheetColumns = new EDataTypeUniqueEList<String>(String.class, this,
                    ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_COLUMNS);
        }
        return sheetColumns;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFirstColumn() {
        return firstColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFirstColumn(String newFirstColumn) {
        String oldFirstColumn = firstColumn;
        firstColumn = newFirstColumn;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__FIRST_COLUMN,
                    oldFirstColumn, firstColumn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLastColumn() {
        return lastColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLastColumn(String newLastColumn) {
        String oldLastColumn = lastColumn;
        lastColumn = newLastColumn;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__LAST_COLUMN,
                    oldLastColumn, lastColumn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getThousandSeparator() {
        return thousandSeparator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setThousandSeparator(String newThousandSeparator) {
        String oldThousandSeparator = thousandSeparator;
        thousandSeparator = newThousandSeparator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__THOUSAND_SEPARATOR,
                    oldThousandSeparator, thousandSeparator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDecimalSeparator(String newDecimalSeparator) {
        String oldDecimalSeparator = decimalSeparator;
        decimalSeparator = newDecimalSeparator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__DECIMAL_SEPARATOR,
                    oldDecimalSeparator, decimalSeparator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isAdvancedSpearator() {
        return advancedSpearator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAdvancedSpearator(boolean newAdvancedSpearator) {
        boolean oldAdvancedSpearator = advancedSpearator;
        advancedSpearator = newAdvancedSpearator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__ADVANCED_SPEARATOR,
                    oldAdvancedSpearator, advancedSpearator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSelectAllSheets() {
        return selectAllSheets;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSelectAllSheets(boolean newSelectAllSheets) {
        boolean oldSelectAllSheets = selectAllSheets;
        selectAllSheets = newSelectAllSheets;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__SELECT_ALL_SHEETS,
                    oldSelectAllSheets, selectAllSheets));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArrayList getSheetList() {
        return sheetList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSheetList(ArrayList newSheetList) {
        ArrayList oldSheetList = sheetList;
        sheetList = newSheetList;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_LIST,
                    oldSheetList, sheetList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGenerationMode() {
        return generationMode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerationMode(String newGenerationMode) {
        String oldGenerationMode = generationMode;
        generationMode = newGenerationMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.FILE_EXCEL_CONNECTION__GENERATION_MODE,
                    oldGenerationMode, generationMode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_NAME:
            return getSheetName();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_COLUMNS:
            return getSheetColumns();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__FIRST_COLUMN:
            return getFirstColumn();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__LAST_COLUMN:
            return getLastColumn();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__THOUSAND_SEPARATOR:
            return getThousandSeparator();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__DECIMAL_SEPARATOR:
            return getDecimalSeparator();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__ADVANCED_SPEARATOR:
            return isAdvancedSpearator();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SELECT_ALL_SHEETS:
            return isSelectAllSheets();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_LIST:
            return getSheetList();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__GENERATION_MODE:
            return getGenerationMode();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_NAME:
            setSheetName((String) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_COLUMNS:
            getSheetColumns().clear();
            getSheetColumns().addAll((Collection<? extends String>) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__FIRST_COLUMN:
            setFirstColumn((String) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__LAST_COLUMN:
            setLastColumn((String) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__THOUSAND_SEPARATOR:
            setThousandSeparator((String) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__DECIMAL_SEPARATOR:
            setDecimalSeparator((String) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__ADVANCED_SPEARATOR:
            setAdvancedSpearator((Boolean) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SELECT_ALL_SHEETS:
            setSelectAllSheets((Boolean) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_LIST:
            setSheetList((ArrayList) newValue);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__GENERATION_MODE:
            setGenerationMode((String) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_NAME:
            setSheetName(SHEET_NAME_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_COLUMNS:
            getSheetColumns().clear();
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__FIRST_COLUMN:
            setFirstColumn(FIRST_COLUMN_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__LAST_COLUMN:
            setLastColumn(LAST_COLUMN_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__THOUSAND_SEPARATOR:
            setThousandSeparator(THOUSAND_SEPARATOR_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__DECIMAL_SEPARATOR:
            setDecimalSeparator(DECIMAL_SEPARATOR_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__ADVANCED_SPEARATOR:
            setAdvancedSpearator(ADVANCED_SPEARATOR_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SELECT_ALL_SHEETS:
            setSelectAllSheets(SELECT_ALL_SHEETS_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_LIST:
            setSheetList(SHEET_LIST_EDEFAULT);
            return;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__GENERATION_MODE:
            setGenerationMode(GENERATION_MODE_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_NAME:
            return SHEET_NAME_EDEFAULT == null ? sheetName != null : !SHEET_NAME_EDEFAULT.equals(sheetName);
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_COLUMNS:
            return sheetColumns != null && !sheetColumns.isEmpty();
        case ConnectionPackage.FILE_EXCEL_CONNECTION__FIRST_COLUMN:
            return FIRST_COLUMN_EDEFAULT == null ? firstColumn != null : !FIRST_COLUMN_EDEFAULT.equals(firstColumn);
        case ConnectionPackage.FILE_EXCEL_CONNECTION__LAST_COLUMN:
            return LAST_COLUMN_EDEFAULT == null ? lastColumn != null : !LAST_COLUMN_EDEFAULT.equals(lastColumn);
        case ConnectionPackage.FILE_EXCEL_CONNECTION__THOUSAND_SEPARATOR:
            return THOUSAND_SEPARATOR_EDEFAULT == null ? thousandSeparator != null : !THOUSAND_SEPARATOR_EDEFAULT
                    .equals(thousandSeparator);
        case ConnectionPackage.FILE_EXCEL_CONNECTION__DECIMAL_SEPARATOR:
            return DECIMAL_SEPARATOR_EDEFAULT == null ? decimalSeparator != null : !DECIMAL_SEPARATOR_EDEFAULT
                    .equals(decimalSeparator);
        case ConnectionPackage.FILE_EXCEL_CONNECTION__ADVANCED_SPEARATOR:
            return advancedSpearator != ADVANCED_SPEARATOR_EDEFAULT;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SELECT_ALL_SHEETS:
            return selectAllSheets != SELECT_ALL_SHEETS_EDEFAULT;
        case ConnectionPackage.FILE_EXCEL_CONNECTION__SHEET_LIST:
            return SHEET_LIST_EDEFAULT == null ? sheetList != null : !SHEET_LIST_EDEFAULT.equals(sheetList);
        case ConnectionPackage.FILE_EXCEL_CONNECTION__GENERATION_MODE:
            return GENERATION_MODE_EDEFAULT == null ? generationMode != null : !GENERATION_MODE_EDEFAULT.equals(generationMode);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (SheetName: ");
        result.append(sheetName);
        result.append(", sheetColumns: ");
        result.append(sheetColumns);
        result.append(", firstColumn: ");
        result.append(firstColumn);
        result.append(", lastColumn: ");
        result.append(lastColumn);
        result.append(", thousandSeparator: ");
        result.append(thousandSeparator);
        result.append(", decimalSeparator: ");
        result.append(decimalSeparator);
        result.append(", advancedSpearator: ");
        result.append(advancedSpearator);
        result.append(", selectAllSheets: ");
        result.append(selectAllSheets);
        result.append(", sheetList: ");
        result.append(sheetList);
        result.append(", generationMode: ");
        result.append(generationMode);
        result.append(')');
        return result.toString();
    }

} //FileExcelConnectionImpl
