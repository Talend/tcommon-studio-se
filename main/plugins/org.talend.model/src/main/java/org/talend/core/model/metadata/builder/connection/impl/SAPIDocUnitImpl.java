/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>SAPI Doc Unit</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPIDocUnitImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPIDocUnitImpl#getProgramId <em>Program Id</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPIDocUnitImpl#getGatewayService <em>Gateway Service</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPIDocUnitImpl#isUseXmlOutput <em>Use Xml Output</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPIDocUnitImpl#getXmlFile <em>Xml File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPIDocUnitImpl#isUseHtmlOutput <em>Use Html Output</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.SAPIDocUnitImpl#getHtmlFile <em>Html File</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SAPIDocUnitImpl extends AbstractMetadataObjectImpl implements SAPIDocUnit {

    /**
     * The default value of the '{@link #getProgramId() <em>Program Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getProgramId()
     * @generated
     * @ordered
     */
    protected static final String PROGRAM_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProgramId() <em>Program Id</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getProgramId()
     * @generated
     * @ordered
     */
    protected String programId = PROGRAM_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getGatewayService() <em>Gateway Service</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getGatewayService()
     * @generated
     * @ordered
     */
    protected static final String GATEWAY_SERVICE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGatewayService() <em>Gateway Service</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getGatewayService()
     * @generated
     * @ordered
     */
    protected String gatewayService = GATEWAY_SERVICE_EDEFAULT;

    /**
     * The default value of the '{@link #isUseXmlOutput() <em>Use Xml Output</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseXmlOutput()
     * @generated
     * @ordered
     */
    protected static final boolean USE_XML_OUTPUT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseXmlOutput() <em>Use Xml Output</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseXmlOutput()
     * @generated
     * @ordered
     */
    protected boolean useXmlOutput = USE_XML_OUTPUT_EDEFAULT;

    /**
     * The default value of the '{@link #getXmlFile() <em>Xml File</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmlFile()
     * @generated
     * @ordered
     */
    protected static final String XML_FILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXmlFile() <em>Xml File</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getXmlFile()
     * @generated
     * @ordered
     */
    protected String xmlFile = XML_FILE_EDEFAULT;

    /**
     * The default value of the '{@link #isUseHtmlOutput() <em>Use Html Output</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseHtmlOutput()
     * @generated
     * @ordered
     */
    protected static final boolean USE_HTML_OUTPUT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseHtmlOutput() <em>Use Html Output</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseHtmlOutput()
     * @generated
     * @ordered
     */
    protected boolean useHtmlOutput = USE_HTML_OUTPUT_EDEFAULT;

    /**
     * The default value of the '{@link #getHtmlFile() <em>Html File</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getHtmlFile()
     * @generated
     * @ordered
     */
    protected static final String HTML_FILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHtmlFile() <em>Html File</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getHtmlFile()
     * @generated
     * @ordered
     */
    protected String htmlFile = HTML_FILE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected SAPIDocUnitImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.SAPI_DOC_UNIT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public SAPConnection getConnection() {
        if (eContainerFeatureID() != ConnectionPackage.SAPI_DOC_UNIT__CONNECTION)
            return null;
        return (SAPConnection) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SAPConnection basicGetConnection() {
        if (eContainerFeatureID() != ConnectionPackage.SAPI_DOC_UNIT__CONNECTION)
            return null;
        return (SAPConnection) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConnection(SAPConnection newConnection, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newConnection, ConnectionPackage.SAPI_DOC_UNIT__CONNECTION, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setConnection(SAPConnection newConnection) {
        if (newConnection != eInternalContainer()
                || (eContainerFeatureID() != ConnectionPackage.SAPI_DOC_UNIT__CONNECTION && newConnection != null)) {
            if (EcoreUtil.isAncestor(this, newConnection))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newConnection != null)
                msgs = ((InternalEObject) newConnection).eInverseAdd(this, ConnectionPackage.SAP_CONNECTION__IDOCS,
                        SAPConnection.class, msgs);
            msgs = basicSetConnection(newConnection, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPI_DOC_UNIT__CONNECTION, newConnection,
                    newConnection));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getProgramId() {
        return programId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setProgramId(String newProgramId) {
        String oldProgramId = programId;
        programId = newProgramId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPI_DOC_UNIT__PROGRAM_ID, oldProgramId,
                    programId));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getGatewayService() {
        return gatewayService;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setGatewayService(String newGatewayService) {
        String oldGatewayService = gatewayService;
        gatewayService = newGatewayService;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPI_DOC_UNIT__GATEWAY_SERVICE,
                    oldGatewayService, gatewayService));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseXmlOutput() {
        return useXmlOutput;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setUseXmlOutput(boolean newUseXmlOutput) {
        boolean oldUseXmlOutput = useXmlOutput;
        useXmlOutput = newUseXmlOutput;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPI_DOC_UNIT__USE_XML_OUTPUT,
                    oldUseXmlOutput, useXmlOutput));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getXmlFile() {
        return xmlFile;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setXmlFile(String newXmlFile) {
        String oldXmlFile = xmlFile;
        xmlFile = newXmlFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPI_DOC_UNIT__XML_FILE, oldXmlFile, xmlFile));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseHtmlOutput() {
        return useHtmlOutput;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setUseHtmlOutput(boolean newUseHtmlOutput) {
        boolean oldUseHtmlOutput = useHtmlOutput;
        useHtmlOutput = newUseHtmlOutput;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPI_DOC_UNIT__USE_HTML_OUTPUT,
                    oldUseHtmlOutput, useHtmlOutput));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getHtmlFile() {
        return htmlFile;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setHtmlFile(String newHtmlFile) {
        String oldHtmlFile = htmlFile;
        htmlFile = newHtmlFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.SAPI_DOC_UNIT__HTML_FILE, oldHtmlFile,
                    htmlFile));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.SAPI_DOC_UNIT__CONNECTION:
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            return basicSetConnection((SAPConnection) otherEnd, msgs);
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
        case ConnectionPackage.SAPI_DOC_UNIT__CONNECTION:
            return basicSetConnection(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
        case ConnectionPackage.SAPI_DOC_UNIT__CONNECTION:
            return eInternalContainer().eInverseRemove(this, ConnectionPackage.SAP_CONNECTION__IDOCS, SAPConnection.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.SAPI_DOC_UNIT__CONNECTION:
            if (resolve)
                return getConnection();
            return basicGetConnection();
        case ConnectionPackage.SAPI_DOC_UNIT__PROGRAM_ID:
            return getProgramId();
        case ConnectionPackage.SAPI_DOC_UNIT__GATEWAY_SERVICE:
            return getGatewayService();
        case ConnectionPackage.SAPI_DOC_UNIT__USE_XML_OUTPUT:
            return isUseXmlOutput();
        case ConnectionPackage.SAPI_DOC_UNIT__XML_FILE:
            return getXmlFile();
        case ConnectionPackage.SAPI_DOC_UNIT__USE_HTML_OUTPUT:
            return isUseHtmlOutput();
        case ConnectionPackage.SAPI_DOC_UNIT__HTML_FILE:
            return getHtmlFile();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.SAPI_DOC_UNIT__CONNECTION:
            setConnection((SAPConnection) newValue);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__PROGRAM_ID:
            setProgramId((String) newValue);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__GATEWAY_SERVICE:
            setGatewayService((String) newValue);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__USE_XML_OUTPUT:
            setUseXmlOutput((Boolean) newValue);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__XML_FILE:
            setXmlFile((String) newValue);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__USE_HTML_OUTPUT:
            setUseHtmlOutput((Boolean) newValue);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__HTML_FILE:
            setHtmlFile((String) newValue);
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
        case ConnectionPackage.SAPI_DOC_UNIT__CONNECTION:
            setConnection((SAPConnection) null);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__PROGRAM_ID:
            setProgramId(PROGRAM_ID_EDEFAULT);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__GATEWAY_SERVICE:
            setGatewayService(GATEWAY_SERVICE_EDEFAULT);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__USE_XML_OUTPUT:
            setUseXmlOutput(USE_XML_OUTPUT_EDEFAULT);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__XML_FILE:
            setXmlFile(XML_FILE_EDEFAULT);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__USE_HTML_OUTPUT:
            setUseHtmlOutput(USE_HTML_OUTPUT_EDEFAULT);
            return;
        case ConnectionPackage.SAPI_DOC_UNIT__HTML_FILE:
            setHtmlFile(HTML_FILE_EDEFAULT);
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
        case ConnectionPackage.SAPI_DOC_UNIT__CONNECTION:
            return basicGetConnection() != null;
        case ConnectionPackage.SAPI_DOC_UNIT__PROGRAM_ID:
            return PROGRAM_ID_EDEFAULT == null ? programId != null : !PROGRAM_ID_EDEFAULT.equals(programId);
        case ConnectionPackage.SAPI_DOC_UNIT__GATEWAY_SERVICE:
            return GATEWAY_SERVICE_EDEFAULT == null ? gatewayService != null : !GATEWAY_SERVICE_EDEFAULT.equals(gatewayService);
        case ConnectionPackage.SAPI_DOC_UNIT__USE_XML_OUTPUT:
            return useXmlOutput != USE_XML_OUTPUT_EDEFAULT;
        case ConnectionPackage.SAPI_DOC_UNIT__XML_FILE:
            return XML_FILE_EDEFAULT == null ? xmlFile != null : !XML_FILE_EDEFAULT.equals(xmlFile);
        case ConnectionPackage.SAPI_DOC_UNIT__USE_HTML_OUTPUT:
            return useHtmlOutput != USE_HTML_OUTPUT_EDEFAULT;
        case ConnectionPackage.SAPI_DOC_UNIT__HTML_FILE:
            return HTML_FILE_EDEFAULT == null ? htmlFile != null : !HTML_FILE_EDEFAULT.equals(htmlFile);
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
        result.append(" (programId: ");
        result.append(programId);
        result.append(", gatewayService: ");
        result.append(gatewayService);
        result.append(", useXmlOutput: ");
        result.append(useXmlOutput);
        result.append(", xmlFile: ");
        result.append(xmlFile);
        result.append(", useHtmlOutput: ");
        result.append(useHtmlOutput);
        result.append(", htmlFile: ");
        result.append(htmlFile);
        result.append(')');
        return result.toString();
    }

    public boolean isReadOnly() {
        Connection connection = getConnection();
        return connection == null ? false : connection.isReadOnly();
    }

} // SAPIDocUnitImpl
