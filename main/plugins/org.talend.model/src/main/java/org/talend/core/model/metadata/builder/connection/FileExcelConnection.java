/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import java.util.ArrayList;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>File Excel Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getSheetName <em>Sheet Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getSheetColumns <em>Sheet Columns</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getFirstColumn <em>First Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getLastColumn <em>Last Column</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getThousandSeparator <em>Thousand Separator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getDecimalSeparator <em>Decimal Separator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#isAdvancedSpearator <em>Advanced Spearator</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#isSelectAllSheets <em>Select All Sheets</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getSheetList <em>Sheet List</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getGenerationMode <em>Generation Mode</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection()
 * @model
 * @generated
 */
public interface FileExcelConnection extends FileConnection {

    /**
     * Returns the value of the '<em><b>Sheet Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sheet Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sheet Name</em>' attribute.
     * @see #setSheetName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_SheetName()
     * @model required="true"
     * @generated
     */
    String getSheetName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getSheetName <em>Sheet Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sheet Name</em>' attribute.
     * @see #getSheetName()
     * @generated
     */
    void setSheetName(String value);

    /**
     * Returns the value of the '<em><b>Sheet Columns</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sheet Columns</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sheet Columns</em>' attribute list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_SheetColumns()
     * @model
     * @generated
     */
    EList<String> getSheetColumns();

    /**
     * Returns the value of the '<em><b>First Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>First Column</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>First Column</em>' attribute.
     * @see #setFirstColumn(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_FirstColumn()
     * @model
     * @generated
     */
    String getFirstColumn();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getFirstColumn <em>First Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>First Column</em>' attribute.
     * @see #getFirstColumn()
     * @generated
     */
    void setFirstColumn(String value);

    /**
     * Returns the value of the '<em><b>Last Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Last Column</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Last Column</em>' attribute.
     * @see #setLastColumn(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_LastColumn()
     * @model
     * @generated
     */
    String getLastColumn();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getLastColumn <em>Last Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Last Column</em>' attribute.
     * @see #getLastColumn()
     * @generated
     */
    void setLastColumn(String value);

    /**
     * Returns the value of the '<em><b>Thousand Separator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Thousand Separator</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Thousand Separator</em>' attribute.
     * @see #setThousandSeparator(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_ThousandSeparator()
     * @model
     * @generated
     */
    String getThousandSeparator();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getThousandSeparator <em>Thousand Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Thousand Separator</em>' attribute.
     * @see #getThousandSeparator()
     * @generated
     */
    void setThousandSeparator(String value);

    /**
     * Returns the value of the '<em><b>Decimal Separator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Decimal Separator</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Decimal Separator</em>' attribute.
     * @see #setDecimalSeparator(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_DecimalSeparator()
     * @model
     * @generated
     */
    String getDecimalSeparator();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getDecimalSeparator <em>Decimal Separator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Decimal Separator</em>' attribute.
     * @see #getDecimalSeparator()
     * @generated
     */
    void setDecimalSeparator(String value);

    /**
     * Returns the value of the '<em><b>Advanced Spearator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Advanced Spearator</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Advanced Spearator</em>' attribute.
     * @see #setAdvancedSpearator(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_AdvancedSpearator()
     * @model
     * @generated
     */
    boolean isAdvancedSpearator();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#isAdvancedSpearator <em>Advanced Spearator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Advanced Spearator</em>' attribute.
     * @see #isAdvancedSpearator()
     * @generated
     */
    void setAdvancedSpearator(boolean value);

    /**
     * Returns the value of the '<em><b>Select All Sheets</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Select All Sheets</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Select All Sheets</em>' attribute.
     * @see #setSelectAllSheets(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_SelectAllSheets()
     * @model
     * @generated
     */
    boolean isSelectAllSheets();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#isSelectAllSheets <em>Select All Sheets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Select All Sheets</em>' attribute.
     * @see #isSelectAllSheets()
     * @generated
     */
    void setSelectAllSheets(boolean value);

    /**
     * Returns the value of the '<em><b>Sheet List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sheet List</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sheet List</em>' attribute.
     * @see #setSheetList(ArrayList)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_SheetList()
     * @model dataType="org.talend.core.model.metadata.builder.connection.List"
     * @generated
     */
    ArrayList getSheetList();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getSheetList <em>Sheet List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sheet List</em>' attribute.
     * @see #getSheetList()
     * @generated
     */
    void setSheetList(ArrayList value);

    /**
     * Returns the value of the '<em><b>Generation Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Generation Mode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Generation Mode</em>' attribute.
     * @see #setGenerationMode(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileExcelConnection_GenerationMode()
     * @model
     * @generated
     */
    String getGenerationMode();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.FileExcelConnection#getGenerationMode <em>Generation Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Generation Mode</em>' attribute.
     * @see #getGenerationMode()
     * @generated
     */
    void setGenerationMode(String value);

} // FileExcelConnection
