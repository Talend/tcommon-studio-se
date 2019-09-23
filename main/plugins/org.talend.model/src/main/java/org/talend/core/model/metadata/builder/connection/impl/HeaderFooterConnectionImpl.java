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
import org.talend.core.model.metadata.builder.connection.HeaderFooterConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Header Footer Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HeaderFooterConnectionImpl#isIsHeader <em>Is Header</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HeaderFooterConnectionImpl#getImports <em>Imports</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HeaderFooterConnectionImpl#getMainCode <em>Main Code</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HeaderFooterConnectionImpl#getLibraries <em>Libraries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HeaderFooterConnectionImpl extends ConnectionImpl implements HeaderFooterConnection {

    /**
     * The default value of the '{@link #isIsHeader() <em>Is Header</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsHeader()
     * @generated
     * @ordered
     */
    protected static final boolean IS_HEADER_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsHeader() <em>Is Header</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsHeader()
     * @generated
     * @ordered
     */
    protected boolean isHeader = IS_HEADER_EDEFAULT;

    /**
     * The default value of the '{@link #getImports() <em>Imports</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImports()
     * @generated
     * @ordered
     */
    protected static final String IMPORTS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getImports() <em>Imports</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImports()
     * @generated
     * @ordered
     */
    protected String imports = IMPORTS_EDEFAULT;

    /**
     * The default value of the '{@link #getMainCode() <em>Main Code</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMainCode()
     * @generated
     * @ordered
     */
    protected static final String MAIN_CODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMainCode() <em>Main Code</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMainCode()
     * @generated
     * @ordered
     */
    protected String mainCode = MAIN_CODE_EDEFAULT;

    /**
     * The default value of the '{@link #getLibraries() <em>Libraries</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLibraries()
     * @generated
     * @ordered
     */
    protected static final String LIBRARIES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLibraries() <em>Libraries</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLibraries()
     * @generated
     * @ordered
     */
    protected String libraries = LIBRARIES_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HeaderFooterConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.HEADER_FOOTER_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsHeader() {
        return isHeader;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsHeader(boolean newIsHeader) {
        boolean oldIsHeader = isHeader;
        isHeader = newIsHeader;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HEADER_FOOTER_CONNECTION__IS_HEADER,
                    oldIsHeader, isHeader));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getImports() {
        return imports;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImports(String newImports) {
        String oldImports = imports;
        imports = newImports;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HEADER_FOOTER_CONNECTION__IMPORTS,
                    oldImports, imports));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMainCode() {
        return mainCode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMainCode(String newMainCode) {
        String oldMainCode = mainCode;
        mainCode = newMainCode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HEADER_FOOTER_CONNECTION__MAIN_CODE,
                    oldMainCode, mainCode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLibraries() {
        return libraries;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLibraries(String newLibraries) {
        String oldLibraries = libraries;
        libraries = newLibraries;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HEADER_FOOTER_CONNECTION__LIBRARIES,
                    oldLibraries, libraries));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IS_HEADER:
            return isIsHeader();
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IMPORTS:
            return getImports();
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__MAIN_CODE:
            return getMainCode();
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__LIBRARIES:
            return getLibraries();
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
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IS_HEADER:
            setIsHeader((Boolean) newValue);
            return;
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IMPORTS:
            setImports((String) newValue);
            return;
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__MAIN_CODE:
            setMainCode((String) newValue);
            return;
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__LIBRARIES:
            setLibraries((String) newValue);
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
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IS_HEADER:
            setIsHeader(IS_HEADER_EDEFAULT);
            return;
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IMPORTS:
            setImports(IMPORTS_EDEFAULT);
            return;
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__MAIN_CODE:
            setMainCode(MAIN_CODE_EDEFAULT);
            return;
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__LIBRARIES:
            setLibraries(LIBRARIES_EDEFAULT);
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
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IS_HEADER:
            return isHeader != IS_HEADER_EDEFAULT;
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__IMPORTS:
            return IMPORTS_EDEFAULT == null ? imports != null : !IMPORTS_EDEFAULT.equals(imports);
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__MAIN_CODE:
            return MAIN_CODE_EDEFAULT == null ? mainCode != null : !MAIN_CODE_EDEFAULT.equals(mainCode);
        case ConnectionPackage.HEADER_FOOTER_CONNECTION__LIBRARIES:
            return LIBRARIES_EDEFAULT == null ? libraries != null : !LIBRARIES_EDEFAULT.equals(libraries);
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
        result.append(" (isHeader: ");
        result.append(isHeader);
        result.append(", imports: ");
        result.append(imports);
        result.append(", mainCode: ");
        result.append(mainCode);
        result.append(", libraries: ");
        result.append(libraries);
        result.append(')');
        return result.toString();
    }

} //HeaderFooterConnectionImpl
