/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.OutputSAPFunctionParameterTable;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParamData;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SAPTestInputParameterTable;
import org.talend.cwm.helper.SAPFunctionHelper;
import org.talend.cwm.relational.TdTable;
import orgomg.cwm.foundation.businessinformation.Document;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>SAP Function Unit</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getOutputType <em>Output Type
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getOutputTableName <em>Output
 * Table Name</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getInputParameterTable <em>
 * Input Parameter Table</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getOutputParameterTable <em>
 * Output Parameter Table</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getMetadataTable <em>Metadata
 * Table</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getConnection <em>Connection
 * </em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getTables <em>Tables</em>}</li>
 * <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPFunctionUnitImpl#getTestInputParameterTable <em>
 * Test Input Parameter Table</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class SAPFunctionUnitImpl extends AbstractMetadataObjectImpl implements SAPFunctionUnit {

    /**
     * The default value of the '{@link #getOutputType() <em>Output Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getOutputType()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOutputType() <em>Output Type</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getOutputType()
     * @generated
     * @ordered
     */
    protected String outputType = OUTPUT_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getOutputTableName() <em>Output Table Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getOutputTableName()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_TABLE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOutputTableName() <em>Output Table Name</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getOutputTableName()
     * @generated
     * @ordered
     */
    protected String outputTableName = OUTPUT_TABLE_NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getInputParameterTable() <em>Input Parameter Table</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getInputParameterTable()
     * @generated
     * @ordered
     */
    protected InputSAPFunctionParameterTable inputParameterTable;

    /**
     * The cached value of the '{@link #getOutputParameterTable() <em>Output Parameter Table</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getOutputParameterTable()
     * @generated
     * @ordered
     */
    protected OutputSAPFunctionParameterTable outputParameterTable;

    /**
     * The cached value of the '{@link #getMetadataTable() <em>Metadata Table</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getMetadataTable()
     * @generated
     * @ordered
     */
    protected MetadataTable metadataTable;

    /**
     * The cached value of the '{@link #getConnection() <em>Connection</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConnection()
     * @generated
     * @ordered
     */
    protected SAPConnection connection;

    /**
     * The cached value of the '{@link #getTables() <em>Tables</em>}' containment reference list.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getTables()
     * @generated
     * @ordered
     */
    protected EList<MetadataTable> tables;

    /**
     * The cached value of the '{@link #getInputTables() <em>Input Tables</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInputTables()
     * @generated
     * @ordered
     */
    protected EList<MetadataTable> inputTables;

    /**
     * The cached value of the '{@link #getTestInputParameterTable() <em>Test Input Parameter Table</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getTestInputParameterTable()
     * @generated
     * @ordered
     */
    protected SAPTestInputParameterTable testInputParameterTable;

    /**
     * The cached value of the '{@link #getParamData() <em>Param Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParamData()
     * @generated
     * @ordered
     */
    protected SAPFunctionParamData paramData;

    /**
     * The default value of the '{@link #isAsXmlSchema() <em>As Xml Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAsXmlSchema()
     * @generated
     * @ordered
     */
    protected static final boolean AS_XML_SCHEMA_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAsXmlSchema() <em>As Xml Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAsXmlSchema()
     * @generated
     * @ordered
     */
    protected boolean asXmlSchema = AS_XML_SCHEMA_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected SAPFunctionUnitImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAP_FUNCTION_UNIT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getOutputType() {
        return outputType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setOutputType(String newOutputType) {
        String oldOutputType = outputType;
        outputType = newOutputType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TYPE,
                    oldOutputType, outputType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getOutputTableName() {
        return outputTableName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setOutputTableName(String newOutputTableName) {
        String oldOutputTableName = outputTableName;
        outputTableName = newOutputTableName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TABLE_NAME,
                    oldOutputTableName, outputTableName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public InputSAPFunctionParameterTable getInputParameterTable() {
        if (inputParameterTable != null && inputParameterTable.eIsProxy()) {
            InternalEObject oldInputParameterTable = (InternalEObject) inputParameterTable;
            inputParameterTable = (InputSAPFunctionParameterTable) eResolveProxy(oldInputParameterTable);
            if (inputParameterTable != oldInputParameterTable) {
                InternalEObject newInputParameterTable = (InternalEObject) inputParameterTable;
                NotificationChain msgs = oldInputParameterTable.eInverseRemove(this,
                        ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                        InputSAPFunctionParameterTable.class, null);
                if (newInputParameterTable.eInternalContainer() == null) {
                    msgs = newInputParameterTable.eInverseAdd(this,
                            ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                            InputSAPFunctionParameterTable.class, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE, oldInputParameterTable,
                            inputParameterTable));
            }
        }
        return inputParameterTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public InputSAPFunctionParameterTable basicGetInputParameterTable() {
        return inputParameterTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInputParameterTable(InputSAPFunctionParameterTable newInputParameterTable,
            NotificationChain msgs) {
        InputSAPFunctionParameterTable oldInputParameterTable = inputParameterTable;
        inputParameterTable = newInputParameterTable;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE, oldInputParameterTable, newInputParameterTable);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setInputParameterTable(InputSAPFunctionParameterTable newInputParameterTable) {
        if (newInputParameterTable != inputParameterTable) {
            NotificationChain msgs = null;
            if (inputParameterTable != null)
                msgs = ((InternalEObject) inputParameterTable).eInverseRemove(this,
                        ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                        InputSAPFunctionParameterTable.class, msgs);
            if (newInputParameterTable != null)
                msgs = ((InternalEObject) newInputParameterTable).eInverseAdd(this,
                        ConnectionPackage.INPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                        InputSAPFunctionParameterTable.class, msgs);
            msgs = basicSetInputParameterTable(newInputParameterTable, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE,
                    newInputParameterTable, newInputParameterTable));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public OutputSAPFunctionParameterTable getOutputParameterTable() {
        if (outputParameterTable != null && outputParameterTable.eIsProxy()) {
            InternalEObject oldOutputParameterTable = (InternalEObject) outputParameterTable;
            outputParameterTable = (OutputSAPFunctionParameterTable) eResolveProxy(oldOutputParameterTable);
            if (outputParameterTable != oldOutputParameterTable) {
                InternalEObject newOutputParameterTable = (InternalEObject) outputParameterTable;
                NotificationChain msgs = oldOutputParameterTable.eInverseRemove(this,
                        ConnectionPackage.OUTPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                        OutputSAPFunctionParameterTable.class, null);
                if (newOutputParameterTable.eInternalContainer() == null) {
                    msgs = newOutputParameterTable.eInverseAdd(this,
                            ConnectionPackage.OUTPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                            OutputSAPFunctionParameterTable.class, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE, oldOutputParameterTable,
                            outputParameterTable));
            }
        }
        return outputParameterTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public OutputSAPFunctionParameterTable basicGetOutputParameterTable() {
        return outputParameterTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOutputParameterTable(OutputSAPFunctionParameterTable newOutputParameterTable,
            NotificationChain msgs) {
        OutputSAPFunctionParameterTable oldOutputParameterTable = outputParameterTable;
        outputParameterTable = newOutputParameterTable;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE, oldOutputParameterTable, newOutputParameterTable);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setOutputParameterTable(OutputSAPFunctionParameterTable newOutputParameterTable) {
        if (newOutputParameterTable != outputParameterTable) {
            NotificationChain msgs = null;
            if (outputParameterTable != null)
                msgs = ((InternalEObject) outputParameterTable).eInverseRemove(this,
                        ConnectionPackage.OUTPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                        OutputSAPFunctionParameterTable.class, msgs);
            if (newOutputParameterTable != null)
                msgs = ((InternalEObject) newOutputParameterTable).eInverseAdd(this,
                        ConnectionPackage.OUTPUT_SAP_FUNCTION_PARAMETER_TABLE__FUNCTION_UNIT,
                        OutputSAPFunctionParameterTable.class, msgs);
            msgs = basicSetOutputParameterTable(newOutputParameterTable, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE,
                    newOutputParameterTable, newOutputParameterTable));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public MetadataTable getMetadataTable() {
        if (metadataTable != null && metadataTable.eIsProxy()) {
            InternalEObject oldMetadataTable = (InternalEObject) metadataTable;
            metadataTable = (MetadataTable) eResolveProxy(oldMetadataTable);
            if (metadataTable != oldMetadataTable) {
                InternalEObject newMetadataTable = (InternalEObject) metadataTable;
                NotificationChain msgs = oldMetadataTable.eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE, null, null);
                if (newMetadataTable.eInternalContainer() == null) {
                    msgs = newMetadataTable.eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                            - ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE, null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE, oldMetadataTable, metadataTable));
            }
        }
        return metadataTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public MetadataTable basicGetMetadataTable() {
        return metadataTable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMetadataTable(MetadataTable newMetadataTable, NotificationChain msgs) {
        MetadataTable oldMetadataTable = metadataTable;
        metadataTable = newMetadataTable;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE, oldMetadataTable, newMetadataTable);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMetadataTable(MetadataTable newMetadataTable) {
        if (newMetadataTable != metadataTable) {
            NotificationChain msgs = null;
            if (metadataTable != null)
                msgs = ((InternalEObject) metadataTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE, null, msgs);
            if (newMetadataTable != null)
                msgs = ((InternalEObject) newMetadataTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE, null, msgs);
            msgs = basicSetMetadataTable(newMetadataTable, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE,
                    newMetadataTable, newMetadataTable));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPConnection getConnection() {
        if (connection != null && connection.eIsProxy()) {
            InternalEObject oldConnection = (InternalEObject) connection;
            connection = (SAPConnection) eResolveProxy(oldConnection);
            if (connection != oldConnection) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConnectionPackage.SAP_FUNCTION_UNIT__CONNECTION,
                            oldConnection, connection));
            }
        }
        return connection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPConnection basicGetConnection() {
        return connection;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setConnection(SAPConnection newConnection) {
        SAPConnection oldConnection = connection;
        connection = newConnection;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__CONNECTION, oldConnection,
                    connection));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataTable> getTables() {
        if (tables == null) {
            tables = new EObjectContainmentEList.Resolving<MetadataTable>(MetadataTable.class, this,
                    ConnectionPackage.SAP_FUNCTION_UNIT__TABLES);
        }
        return tables;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataTable> getInputTables() {
        if (inputTables == null) {
            inputTables = new EObjectContainmentEList.Resolving<MetadataTable>(MetadataTable.class, this,
                    ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_TABLES);
        }
        return inputTables;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPTestInputParameterTable getTestInputParameterTable() {
        if (testInputParameterTable != null && testInputParameterTable.eIsProxy()) {
            InternalEObject oldTestInputParameterTable = (InternalEObject) testInputParameterTable;
            testInputParameterTable = (SAPTestInputParameterTable) eResolveProxy(oldTestInputParameterTable);
            if (testInputParameterTable != oldTestInputParameterTable) {
                InternalEObject newTestInputParameterTable = (InternalEObject) testInputParameterTable;
                NotificationChain msgs = oldTestInputParameterTable.eInverseRemove(this,
                        ConnectionPackage.SAP_TEST_INPUT_PARAMETER_TABLE__FUNCTION_UNIT, SAPTestInputParameterTable.class, null);
                if (newTestInputParameterTable.eInternalContainer() == null) {
                    msgs = newTestInputParameterTable.eInverseAdd(this,
                            ConnectionPackage.SAP_TEST_INPUT_PARAMETER_TABLE__FUNCTION_UNIT, SAPTestInputParameterTable.class,
                            msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE, oldTestInputParameterTable,
                            testInputParameterTable));
            }
        }
        return testInputParameterTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPTestInputParameterTable basicGetTestInputParameterTable() {
        return testInputParameterTable;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTestInputParameterTable(SAPTestInputParameterTable newTestInputParameterTable,
            NotificationChain msgs) {
        SAPTestInputParameterTable oldTestInputParameterTable = testInputParameterTable;
        testInputParameterTable = newTestInputParameterTable;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE, oldTestInputParameterTable,
                    newTestInputParameterTable);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setTestInputParameterTable(SAPTestInputParameterTable newTestInputParameterTable) {
        if (newTestInputParameterTable != testInputParameterTable) {
            NotificationChain msgs = null;
            if (testInputParameterTable != null)
                msgs = ((InternalEObject) testInputParameterTable).eInverseRemove(this,
                        ConnectionPackage.SAP_TEST_INPUT_PARAMETER_TABLE__FUNCTION_UNIT, SAPTestInputParameterTable.class, msgs);
            if (newTestInputParameterTable != null)
                msgs = ((InternalEObject) newTestInputParameterTable).eInverseAdd(this,
                        ConnectionPackage.SAP_TEST_INPUT_PARAMETER_TABLE__FUNCTION_UNIT, SAPTestInputParameterTable.class, msgs);
            msgs = basicSetTestInputParameterTable(newTestInputParameterTable, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE, newTestInputParameterTable,
                    newTestInputParameterTable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParamData getParamData() {
        if (paramData != null && paramData.eIsProxy()) {
            InternalEObject oldParamData = (InternalEObject) paramData;
            paramData = (SAPFunctionParamData) eResolveProxy(oldParamData);
            if (paramData != oldParamData) {
                InternalEObject newParamData = (InternalEObject) paramData;
                NotificationChain msgs = oldParamData.eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA, null, null);
                if (newParamData.eInternalContainer() == null) {
                    msgs = newParamData.eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                            - ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA, null, msgs);
                }
                if (msgs != null)
                    msgs.dispatch();
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA,
                            oldParamData, paramData));
            }
        }
        return paramData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPFunctionParamData basicGetParamData() {
        return paramData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetParamData(SAPFunctionParamData newParamData, NotificationChain msgs) {
        SAPFunctionParamData oldParamData = paramData;
        paramData = newParamData;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA, oldParamData, newParamData);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParamData(SAPFunctionParamData newParamData) {
        if (newParamData != paramData) {
            NotificationChain msgs = null;
            if (paramData != null)
                msgs = ((InternalEObject) paramData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA, null, msgs);
            if (newParamData != null)
                msgs = ((InternalEObject) newParamData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA, null, msgs);
            msgs = basicSetParamData(newParamData, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA, newParamData,
                    newParamData));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isAsXmlSchema() {
        return asXmlSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAsXmlSchema(boolean newAsXmlSchema) {
        boolean oldAsXmlSchema = asXmlSchema;
        asXmlSchema = newAsXmlSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__AS_XML_SCHEMA,
                    oldAsXmlSchema, asXmlSchema));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    public void setDocument(String document) {
        Document firstDocument = SAPFunctionHelper.getFirstDocument(this);
        String oldDocument = firstDocument.getReference();
        firstDocument.setReference(document);
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAP_FUNCTION_UNIT__DOCUMENT, oldDocument,
                    document));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE:
            if (inputParameterTable != null)
                msgs = ((InternalEObject) inputParameterTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE, null, msgs);
            return basicSetInputParameterTable((InputSAPFunctionParameterTable) otherEnd, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE:
            if (outputParameterTable != null)
                msgs = ((InternalEObject) outputParameterTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE, null, msgs);
            return basicSetOutputParameterTable((OutputSAPFunctionParameterTable) otherEnd, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE:
            if (testInputParameterTable != null)
                msgs = ((InternalEObject) testInputParameterTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE, null, msgs);
            return basicSetTestInputParameterTable((SAPTestInputParameterTable) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE:
            return basicSetInputParameterTable(null, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE:
            return basicSetOutputParameterTable(null, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE:
            return basicSetMetadataTable(null, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__TABLES:
            return ((InternalEList<?>) getTables()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_TABLES:
            return ((InternalEList<?>) getInputTables()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE:
            return basicSetTestInputParameterTable(null, msgs);
        case ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA:
            return basicSetParamData(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TYPE:
            return getOutputType();
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TABLE_NAME:
            return getOutputTableName();
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE:
            if (resolve)
                return getInputParameterTable();
            return basicGetInputParameterTable();
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE:
            if (resolve)
                return getOutputParameterTable();
            return basicGetOutputParameterTable();
        case ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE:
            if (resolve)
                return getMetadataTable();
            return basicGetMetadataTable();
        case ConnectionPackage.SAP_FUNCTION_UNIT__CONNECTION:
            if (resolve)
                return getConnection();
            return basicGetConnection();
        case ConnectionPackage.SAP_FUNCTION_UNIT__TABLES:
            return getTables();
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_TABLES:
            return getInputTables();
        case ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE:
            if (resolve)
                return getTestInputParameterTable();
            return basicGetTestInputParameterTable();
        case ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA:
            if (resolve)
                return getParamData();
            return basicGetParamData();
        case ConnectionPackage.SAP_FUNCTION_UNIT__AS_XML_SCHEMA:
            return isAsXmlSchema();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TYPE:
            setOutputType((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TABLE_NAME:
            setOutputTableName((String) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE:
            setInputParameterTable((InputSAPFunctionParameterTable) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE:
            setOutputParameterTable((OutputSAPFunctionParameterTable) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE:
            setMetadataTable((MetadataTable) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__CONNECTION:
            setConnection((SAPConnection) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__TABLES:
            getTables().clear();
            getTables().addAll((Collection<? extends MetadataTable>) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_TABLES:
            getInputTables().clear();
            getInputTables().addAll((Collection<? extends MetadataTable>) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE:
            setTestInputParameterTable((SAPTestInputParameterTable) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA:
            setParamData((SAPFunctionParamData) newValue);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__AS_XML_SCHEMA:
            setAsXmlSchema((Boolean) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TYPE:
            setOutputType(OUTPUT_TYPE_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TABLE_NAME:
            setOutputTableName(OUTPUT_TABLE_NAME_EDEFAULT);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE:
            setInputParameterTable((InputSAPFunctionParameterTable) null);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE:
            setOutputParameterTable((OutputSAPFunctionParameterTable) null);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE:
            setMetadataTable((MetadataTable) null);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__CONNECTION:
            setConnection((SAPConnection) null);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__TABLES:
            getTables().clear();
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_TABLES:
            getInputTables().clear();
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE:
            setTestInputParameterTable((SAPTestInputParameterTable) null);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA:
            setParamData((SAPFunctionParamData) null);
            return;
        case ConnectionPackage.SAP_FUNCTION_UNIT__AS_XML_SCHEMA:
            setAsXmlSchema(AS_XML_SCHEMA_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TYPE:
            return OUTPUT_TYPE_EDEFAULT == null ? outputType != null : !OUTPUT_TYPE_EDEFAULT.equals(outputType);
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_TABLE_NAME:
            return OUTPUT_TABLE_NAME_EDEFAULT == null ? outputTableName != null : !OUTPUT_TABLE_NAME_EDEFAULT
                    .equals(outputTableName);
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_PARAMETER_TABLE:
            return inputParameterTable != null;
        case ConnectionPackage.SAP_FUNCTION_UNIT__OUTPUT_PARAMETER_TABLE:
            return outputParameterTable != null;
        case ConnectionPackage.SAP_FUNCTION_UNIT__METADATA_TABLE:
            return metadataTable != null;
        case ConnectionPackage.SAP_FUNCTION_UNIT__CONNECTION:
            return connection != null;
        case ConnectionPackage.SAP_FUNCTION_UNIT__TABLES:
            return tables != null && !tables.isEmpty();
        case ConnectionPackage.SAP_FUNCTION_UNIT__INPUT_TABLES:
            return inputTables != null && !inputTables.isEmpty();
        case ConnectionPackage.SAP_FUNCTION_UNIT__TEST_INPUT_PARAMETER_TABLE:
            return testInputParameterTable != null;
        case ConnectionPackage.SAP_FUNCTION_UNIT__PARAM_DATA:
            return paramData != null;
        case ConnectionPackage.SAP_FUNCTION_UNIT__AS_XML_SCHEMA:
            return asXmlSchema != AS_XML_SCHEMA_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (OutputType: ");
        result.append(outputType);
        result.append(", OutputTableName: ");
        result.append(outputTableName);
        result.append(", asXmlSchema: ");
        result.append(asXmlSchema);
        result.append(')');
        return result.toString();
    }

    public boolean isReadOnly() {
        Connection connection = getConnection();
        return connection == null ? false : connection.isReadOnly();
    }

} // SAPFunctionUnitImpl
