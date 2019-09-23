/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ebcdic Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.EbcdicConnectionImpl#getMidFile <em>Mid File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.EbcdicConnectionImpl#getDataFile <em>Data File</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.EbcdicConnectionImpl#getCodePage <em>Code Page</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.EbcdicConnectionImpl#getSourceFileStart <em>Source File Start</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.EbcdicConnectionImpl#getSourceFileEnd <em>Source File End</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EbcdicConnectionImpl extends FileConnectionImpl implements EbcdicConnection {

    /**
     * The default value of the '{@link #getMidFile() <em>Mid File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMidFile()
     * @generated
     * @ordered
     */
    protected static final String MID_FILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMidFile() <em>Mid File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMidFile()
     * @generated
     * @ordered
     */
    protected String midFile = MID_FILE_EDEFAULT;

    /**
     * The default value of the '{@link #getDataFile() <em>Data File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataFile()
     * @generated
     * @ordered
     */
    protected static final String DATA_FILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDataFile() <em>Data File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataFile()
     * @generated
     * @ordered
     */
    protected String dataFile = DATA_FILE_EDEFAULT;

    /**
     * The default value of the '{@link #getCodePage() <em>Code Page</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCodePage()
     * @generated
     * @ordered
     */
    protected static final String CODE_PAGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCodePage() <em>Code Page</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCodePage()
     * @generated
     * @ordered
     */
    protected String codePage = CODE_PAGE_EDEFAULT;

    /**
     * The default value of the '{@link #getSourceFileStart() <em>Source File Start</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceFileStart()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_FILE_START_EDEFAULT = "6";

    /**
     * The cached value of the '{@link #getSourceFileStart() <em>Source File Start</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceFileStart()
     * @generated
     * @ordered
     */
    protected String sourceFileStart = SOURCE_FILE_START_EDEFAULT;

    /**
     * The default value of the '{@link #getSourceFileEnd() <em>Source File End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceFileEnd()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_FILE_END_EDEFAULT = "72";

    /**
     * The cached value of the '{@link #getSourceFileEnd() <em>Source File End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceFileEnd()
     * @generated
     * @ordered
     */
    protected String sourceFileEnd = SOURCE_FILE_END_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EbcdicConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.EBCDIC_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMidFile() {
        return midFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMidFile(String newMidFile) {
        String oldMidFile = midFile;
        midFile = newMidFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EBCDIC_CONNECTION__MID_FILE, oldMidFile,
                    midFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDataFile() {
        return dataFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataFile(String newDataFile) {
        String oldDataFile = dataFile;
        dataFile = newDataFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EBCDIC_CONNECTION__DATA_FILE, oldDataFile,
                    dataFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCodePage() {
        return codePage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCodePage(String newCodePage) {
        String oldCodePage = codePage;
        codePage = newCodePage;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EBCDIC_CONNECTION__CODE_PAGE, oldCodePage,
                    codePage));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSourceFileStart() {
        return sourceFileStart;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceFileStart(String newSourceFileStart) {
        String oldSourceFileStart = sourceFileStart;
        sourceFileStart = newSourceFileStart;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_START,
                    oldSourceFileStart, sourceFileStart));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSourceFileEnd() {
        return sourceFileEnd;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceFileEnd(String newSourceFileEnd) {
        String oldSourceFileEnd = sourceFileEnd;
        sourceFileEnd = newSourceFileEnd;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_END,
                    oldSourceFileEnd, sourceFileEnd));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.EBCDIC_CONNECTION__MID_FILE:
            return getMidFile();
        case ConnectionPackage.EBCDIC_CONNECTION__DATA_FILE:
            return getDataFile();
        case ConnectionPackage.EBCDIC_CONNECTION__CODE_PAGE:
            return getCodePage();
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_START:
            return getSourceFileStart();
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_END:
            return getSourceFileEnd();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case ConnectionPackage.EBCDIC_CONNECTION__MID_FILE:
            setMidFile((String) newValue);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__DATA_FILE:
            setDataFile((String) newValue);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__CODE_PAGE:
            setCodePage((String) newValue);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_START:
            setSourceFileStart((String) newValue);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_END:
            setSourceFileEnd((String) newValue);
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
        case ConnectionPackage.EBCDIC_CONNECTION__MID_FILE:
            setMidFile(MID_FILE_EDEFAULT);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__DATA_FILE:
            setDataFile(DATA_FILE_EDEFAULT);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__CODE_PAGE:
            setCodePage(CODE_PAGE_EDEFAULT);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_START:
            setSourceFileStart(SOURCE_FILE_START_EDEFAULT);
            return;
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_END:
            setSourceFileEnd(SOURCE_FILE_END_EDEFAULT);
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
        case ConnectionPackage.EBCDIC_CONNECTION__MID_FILE:
            return MID_FILE_EDEFAULT == null ? midFile != null : !MID_FILE_EDEFAULT.equals(midFile);
        case ConnectionPackage.EBCDIC_CONNECTION__DATA_FILE:
            return DATA_FILE_EDEFAULT == null ? dataFile != null : !DATA_FILE_EDEFAULT.equals(dataFile);
        case ConnectionPackage.EBCDIC_CONNECTION__CODE_PAGE:
            return CODE_PAGE_EDEFAULT == null ? codePage != null : !CODE_PAGE_EDEFAULT.equals(codePage);
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_START:
            return SOURCE_FILE_START_EDEFAULT == null ? sourceFileStart != null : !SOURCE_FILE_START_EDEFAULT
                    .equals(sourceFileStart);
        case ConnectionPackage.EBCDIC_CONNECTION__SOURCE_FILE_END:
            return SOURCE_FILE_END_EDEFAULT == null ? sourceFileEnd != null : !SOURCE_FILE_END_EDEFAULT.equals(sourceFileEnd);
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
        result.append(" (MidFile: ");
        result.append(midFile);
        result.append(", DataFile: ");
        result.append(dataFile);
        result.append(", CodePage: ");
        result.append(codePage);
        result.append(", SourceFileStart: ");
        result.append(sourceFileStart);
        result.append(", SourceFileEnd: ");
        result.append(sourceFileEnd);
        result.append(')');
        return result.toString();
    }

} //EbcdicConnectionImpl
