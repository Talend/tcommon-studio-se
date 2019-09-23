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
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.HL7FileNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>HL7 Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7ConnectionImpl#getStartChar <em>Start Char</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7ConnectionImpl#getEndChar <em>End Char</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7ConnectionImpl#getRoot <em>Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.HL7ConnectionImpl#getOutputFilePath <em>Output File Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HL7ConnectionImpl extends FileConnectionImpl implements HL7Connection {

    /**
     * The default value of the '{@link #getStartChar() <em>Start Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartChar()
     * @generated
     * @ordered
     */
    protected static final String START_CHAR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStartChar() <em>Start Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartChar()
     * @generated
     * @ordered
     */
    protected String startChar = START_CHAR_EDEFAULT;

    /**
     * The default value of the '{@link #getEndChar() <em>End Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndChar()
     * @generated
     * @ordered
     */
    protected static final String END_CHAR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEndChar() <em>End Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndChar()
     * @generated
     * @ordered
     */
    protected String endChar = END_CHAR_EDEFAULT;

    /**
     * The cached value of the '{@link #getRoot() <em>Root</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRoot()
     * @generated
     * @ordered
     */
    protected EList<HL7FileNode> root;

    /**
     * The default value of the '{@link #getOutputFilePath() <em>Output File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFilePath()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FILE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOutputFilePath() <em>Output File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFilePath()
     * @generated
     * @ordered
     */
    protected String outputFilePath = OUTPUT_FILE_PATH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HL7ConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.HL7_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getStartChar() {
        return startChar;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartChar(String newStartChar) {
        String oldStartChar = startChar;
        startChar = newStartChar;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_CONNECTION__START_CHAR, oldStartChar,
                    startChar));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEndChar() {
        return endChar;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndChar(String newEndChar) {
        String oldEndChar = endChar;
        endChar = newEndChar;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_CONNECTION__END_CHAR, oldEndChar, endChar));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<HL7FileNode> getRoot() {
        if (root == null) {
            root = new EObjectContainmentEList.Resolving<HL7FileNode>(HL7FileNode.class, this,
                    ConnectionPackage.HL7_CONNECTION__ROOT);
        }
        return root;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFilePath() {
        return outputFilePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFilePath(String newOutputFilePath) {
        String oldOutputFilePath = outputFilePath;
        outputFilePath = newOutputFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.HL7_CONNECTION__OUTPUT_FILE_PATH,
                    oldOutputFilePath, outputFilePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.HL7_CONNECTION__ROOT:
            return ((InternalEList<?>) getRoot()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.HL7_CONNECTION__START_CHAR:
            return getStartChar();
        case ConnectionPackage.HL7_CONNECTION__END_CHAR:
            return getEndChar();
        case ConnectionPackage.HL7_CONNECTION__ROOT:
            return getRoot();
        case ConnectionPackage.HL7_CONNECTION__OUTPUT_FILE_PATH:
            return getOutputFilePath();
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
        case ConnectionPackage.HL7_CONNECTION__START_CHAR:
            setStartChar((String) newValue);
            return;
        case ConnectionPackage.HL7_CONNECTION__END_CHAR:
            setEndChar((String) newValue);
            return;
        case ConnectionPackage.HL7_CONNECTION__ROOT:
            getRoot().clear();
            getRoot().addAll((Collection<? extends HL7FileNode>) newValue);
            return;
        case ConnectionPackage.HL7_CONNECTION__OUTPUT_FILE_PATH:
            setOutputFilePath((String) newValue);
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
        case ConnectionPackage.HL7_CONNECTION__START_CHAR:
            setStartChar(START_CHAR_EDEFAULT);
            return;
        case ConnectionPackage.HL7_CONNECTION__END_CHAR:
            setEndChar(END_CHAR_EDEFAULT);
            return;
        case ConnectionPackage.HL7_CONNECTION__ROOT:
            getRoot().clear();
            return;
        case ConnectionPackage.HL7_CONNECTION__OUTPUT_FILE_PATH:
            setOutputFilePath(OUTPUT_FILE_PATH_EDEFAULT);
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
        case ConnectionPackage.HL7_CONNECTION__START_CHAR:
            return START_CHAR_EDEFAULT == null ? startChar != null : !START_CHAR_EDEFAULT.equals(startChar);
        case ConnectionPackage.HL7_CONNECTION__END_CHAR:
            return END_CHAR_EDEFAULT == null ? endChar != null : !END_CHAR_EDEFAULT.equals(endChar);
        case ConnectionPackage.HL7_CONNECTION__ROOT:
            return root != null && !root.isEmpty();
        case ConnectionPackage.HL7_CONNECTION__OUTPUT_FILE_PATH:
            return OUTPUT_FILE_PATH_EDEFAULT == null ? outputFilePath != null : !OUTPUT_FILE_PATH_EDEFAULT.equals(outputFilePath);
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
        result.append(" (StartChar: ");
        result.append(startChar);
        result.append(", EndChar: ");
        result.append(endChar);
        result.append(", outputFilePath: ");
        result.append(outputFilePath);
        result.append(')');
        return result.toString();
    }

} //HL7ConnectionImpl
