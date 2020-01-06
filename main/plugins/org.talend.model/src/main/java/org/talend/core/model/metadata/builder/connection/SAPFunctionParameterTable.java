/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SAP Function Parameter Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterTable#getColumns <em>Columns</em>}</li>
 * </ul>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterTable()
 * @model
 * @generated
 */
public interface SAPFunctionParameterTable extends AbstractMetadataObject {

    /**
     * Returns the value of the '<em><b>Columns</b></em>' containment reference list.
     * The list contents are of type {@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn}.
     * It is bidirectional and its opposite is '{@link org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getParameterTable <em>Parameter Table</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Columns</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Columns</em>' containment reference list.
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getSAPFunctionParameterTable_Columns()
     * @see org.talend.core.model.metadata.builder.connection.SAPFunctionParameterColumn#getParameterTable
     * @model opposite="ParameterTable" containment="true" resolveProxies="true"
     * @generated
     */
    EList<SAPFunctionParameterColumn> getColumns();

} // SAPFunctionParameterTable
