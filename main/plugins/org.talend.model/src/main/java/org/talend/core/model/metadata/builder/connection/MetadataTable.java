/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Metadata Table</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * representation of a of set of columns
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataTable#getSourceName <em>Source Name</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataTable#getTableType <em>Table Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataTable#isAttachedCDC <em>Attached CDC</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataTable#isActivatedCDC <em>Activated CDC</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataTable#getColumns <em>Columns</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataTable#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataTable#getAdditionalProperties <em>Additional Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable()
 * @model
 * @generated
 */
public interface MetadataTable extends AbstractMetadataObject, orgomg.cwm.objectmodel.core.Class {

    /**
     * Returns the value of the '<em><b>Source Name</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Name</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * @deprecated use g(s)etName() name of the table, that is actual DB table name for DB tables <!-- end-model-doc -->
     * @return the value of the '<em>Source Name</em>' attribute.
     * @see #setSourceName(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable_SourceName()
     * @model transient="true" volatile="true"
     * @generated
     */
    String getSourceName();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataTable#getSourceName <em>Source Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Name</em>' attribute.
     * @see #getSourceName()
     * @generated
     */
    void setSourceName(String value);

    /**
     * Returns the value of the '<em><b>Columns</b></em>' reference list. The list contents are of type
     * {@link org.talend.core.model.metadata.builder.connection.MetadataColumn}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Columns</em>' containment reference list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> List of columns related to this table <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Columns</em>' reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable_Columns()
     * @model transient="true" volatile="true"
     * @generated
     */
    EList<MetadataColumn> getColumns();

    /**
     * Returns the value of the '<em><b>Connection</b></em>' reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connection</em>' container reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * @deprecated use MetadataTableHelper.getFirstconnection() ref to the connection that contains this table <!--
     * end-model-doc -->
     * @return the value of the '<em>Connection</em>' reference.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable_Connection()
     * @model transient="true" changeable="false" volatile="true"
     * @generated
     */
    Connection getConnection();

    /**
     * Returns the value of the '<em><b>Additional Properties</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additional Properties</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Additional Properties</em>' map.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable_AdditionalProperties()
     * @model mapType="org.talend.core.model.metadata.builder.connection.AdditionalProperties<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
     * @generated
     */
    EMap<String, String> getAdditionalProperties();

    /**
     * Returns the value of the '<em><b>Table Type</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Table Type</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> of value of TABLE, VIEW, SYNONYM, ALL_SYNONYM <!-- end-model-doc
     * -->
     * 
     * @return the value of the '<em>Table Type</em>' attribute.
     * @see #setTableType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable_TableType()
     * @model
     * @generated
     */
    String getTableType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataTable#getTableType <em>Table Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Table Type</em>' attribute.
     * @see #getTableType()
     * @generated
     */
    void setTableType(String value);

    /**
     * Returns the value of the '<em><b>Attached CDC</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attached CDC</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> whether a CDC table is attached to this table <!-- end-model-doc
     * -->
     * 
     * @return the value of the '<em>Attached CDC</em>' attribute.
     * @see #setAttachedCDC(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable_AttachedCDC()
     * @model
     * @generated
     */
    boolean isAttachedCDC();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataTable#isAttachedCDC <em>Attached CDC</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Attached CDC</em>' attribute.
     * @see #isAttachedCDC()
     * @generated
     */
    void setAttachedCDC(boolean value);

    /**
     * Returns the value of the '<em><b>Activated CDC</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Activated CDC</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> whether CDC is activated, that is the trigger are set to record
     * the changes <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Activated CDC</em>' attribute.
     * @see #setActivatedCDC(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataTable_ActivatedCDC()
     * @model
     * @generated
     */
    boolean isActivatedCDC();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataTable#isActivatedCDC <em>Activated CDC</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Activated CDC</em>' attribute.
     * @see #isActivatedCDC()
     * @generated
     */
    void setActivatedCDC(boolean value);

} // MetadataTable
