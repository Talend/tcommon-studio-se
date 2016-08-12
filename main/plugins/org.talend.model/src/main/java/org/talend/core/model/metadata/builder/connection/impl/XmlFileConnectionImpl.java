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
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.MetadataSchema;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;

import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Xml File Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getXsdFilePath <em>Xsd File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getXmlFilePath <em>Xml File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#isGuess <em>Guess</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getMaskXPattern <em>Mask XPattern</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getRoot <em>Root</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getLoop <em>Loop</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#isInputModel <em>Input Model</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getOutputFilePath <em>Output File Path</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getFileContent <em>File Content</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.XmlFileConnectionImpl#getTargetNameSpace <em>Target Name Space</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XmlFileConnectionImpl extends ConnectionImpl implements XmlFileConnection {

    /**
     * The default value of the '{@link #getXsdFilePath() <em>Xsd File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXsdFilePath()
     * @generated
     * @ordered
     */
    protected static final String XSD_FILE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXsdFilePath() <em>Xsd File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXsdFilePath()
     * @generated
     * @ordered
     */
    protected String xsdFilePath = XSD_FILE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getXmlFilePath() <em>Xml File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXmlFilePath()
     * @generated
     * @ordered
     */
    protected static final String XML_FILE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXmlFilePath() <em>Xml File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXmlFilePath()
     * @generated
     * @ordered
     */
    protected String xmlFilePath = XML_FILE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #isGuess() <em>Guess</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGuess()
     * @generated
     * @ordered
     */
    protected static final boolean GUESS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGuess() <em>Guess</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGuess()
     * @generated
     * @ordered
     */
    protected boolean guess = GUESS_EDEFAULT;

    /**
     * The default value of the '{@link #getMaskXPattern() <em>Mask XPattern</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaskXPattern()
     * @generated
     * @ordered
     */
    protected static final String MASK_XPATTERN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMaskXPattern() <em>Mask XPattern</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaskXPattern()
     * @generated
     * @ordered
     */
    protected String maskXPattern = MASK_XPATTERN_EDEFAULT;

    /**
     * The cached value of the '{@link #getSchema() <em>Schema</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchema()
     * @generated
     * @ordered
     */
    protected EList<XmlXPathLoopDescriptor> schema;

    /**
     * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncoding()
     * @generated
     * @ordered
     */
    protected static final String ENCODING_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncoding()
     * @generated
     * @ordered
     */
    protected String encoding = ENCODING_EDEFAULT;

    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected EList<XMLFileNode> group;

    /**
     * The cached value of the '{@link #getRoot() <em>Root</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRoot()
     * @generated
     * @ordered
     */
    protected EList<XMLFileNode> root;

    /**
     * The cached value of the '{@link #getLoop() <em>Loop</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLoop()
     * @generated
     * @ordered
     */
    protected EList<XMLFileNode> loop;

    /**
     * The default value of the '{@link #isInputModel() <em>Input Model</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isInputModel()
     * @generated
     * @ordered
     */
    protected static final boolean INPUT_MODEL_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isInputModel() <em>Input Model</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isInputModel()
     * @generated
     * @ordered
     */
    protected boolean inputModel = INPUT_MODEL_EDEFAULT;

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
     * The default value of the '{@link #getFileContent() <em>File Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileContent()
     * @generated
     * @ordered
     */
    protected static final byte[] FILE_CONTENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFileContent() <em>File Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFileContent()
     * @generated
     * @ordered
     */
    protected byte[] fileContent = FILE_CONTENT_EDEFAULT;

    /**
     * The default value of the '{@link #getTargetNameSpace() <em>Target Name Space</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetNameSpace()
     * @generated
     * @ordered
     */
    protected static final String TARGET_NAME_SPACE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTargetNameSpace() <em>Target Name Space</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetNameSpace()
     * @generated
     * @ordered
     */
    protected String targetNameSpace = TARGET_NAME_SPACE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XmlFileConnectionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.XML_FILE_CONNECTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXsdFilePath() {
        return xsdFilePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXsdFilePath(String newXsdFilePath) {
        String oldXsdFilePath = xsdFilePath;
        xsdFilePath = newXsdFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__XSD_FILE_PATH,
                    oldXsdFilePath, xsdFilePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXmlFilePath() {
        return xmlFilePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXmlFilePath(String newXmlFilePath) {
        String oldXmlFilePath = xmlFilePath;
        xmlFilePath = newXmlFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__XML_FILE_PATH,
                    oldXmlFilePath, xmlFilePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGuess() {
        return guess;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGuess(boolean newGuess) {
        boolean oldGuess = guess;
        guess = newGuess;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__GUESS, oldGuess, guess));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMaskXPattern() {
        return maskXPattern;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaskXPattern(String newMaskXPattern) {
        String oldMaskXPattern = maskXPattern;
        maskXPattern = newMaskXPattern;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__MASK_XPATTERN,
                    oldMaskXPattern, maskXPattern));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XmlXPathLoopDescriptor> getSchema() {
        if (schema == null) {
            schema = new EObjectContainmentWithInverseEList.Resolving<XmlXPathLoopDescriptor>(XmlXPathLoopDescriptor.class, this,
                    ConnectionPackage.XML_FILE_CONNECTION__SCHEMA, ConnectionPackage.XML_XPATH_LOOP_DESCRIPTOR__CONNECTION);
        }
        return schema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEncoding(String newEncoding) {
        String oldEncoding = encoding;
        encoding = newEncoding;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__ENCODING, oldEncoding,
                    encoding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getGroup() {
        if (group == null) {
            group = new EObjectContainmentEList<XMLFileNode>(XMLFileNode.class, this,
                    ConnectionPackage.XML_FILE_CONNECTION__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getRoot() {
        if (root == null) {
            root = new EObjectContainmentEList.Resolving<XMLFileNode>(XMLFileNode.class, this,
                    ConnectionPackage.XML_FILE_CONNECTION__ROOT);
        }
        return root;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<XMLFileNode> getLoop() {
        if (loop == null) {
            loop = new EObjectContainmentEList<XMLFileNode>(XMLFileNode.class, this, ConnectionPackage.XML_FILE_CONNECTION__LOOP);
        }
        return loop;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isInputModel() {
        return inputModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInputModel(boolean newInputModel) {
        boolean oldInputModel = inputModel;
        inputModel = newInputModel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__INPUT_MODEL,
                    oldInputModel, inputModel));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__OUTPUT_FILE_PATH,
                    oldOutputFilePath, outputFilePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public byte[] getFileContent() {
        return fileContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFileContent(byte[] newFileContent) {
        byte[] oldFileContent = fileContent;
        fileContent = newFileContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__FILE_CONTENT,
                    oldFileContent, fileContent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTargetNameSpace() {
        return targetNameSpace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetNameSpace(String newTargetNameSpace) {
        String oldTargetNameSpace = targetNameSpace;
        targetNameSpace = newTargetNameSpace;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.XML_FILE_CONNECTION__TARGET_NAME_SPACE,
                    oldTargetNameSpace, targetNameSpace));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.XML_FILE_CONNECTION__SCHEMA:
            return ((InternalEList<InternalEObject>) (InternalEList<?>) getSchema()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case ConnectionPackage.XML_FILE_CONNECTION__SCHEMA:
            return ((InternalEList<?>) getSchema()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.XML_FILE_CONNECTION__GROUP:
            return ((InternalEList<?>) getGroup()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.XML_FILE_CONNECTION__ROOT:
            return ((InternalEList<?>) getRoot()).basicRemove(otherEnd, msgs);
        case ConnectionPackage.XML_FILE_CONNECTION__LOOP:
            return ((InternalEList<?>) getLoop()).basicRemove(otherEnd, msgs);
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
        case ConnectionPackage.XML_FILE_CONNECTION__XSD_FILE_PATH:
            return getXsdFilePath();
        case ConnectionPackage.XML_FILE_CONNECTION__XML_FILE_PATH:
            return getXmlFilePath();
        case ConnectionPackage.XML_FILE_CONNECTION__GUESS:
            return isGuess();
        case ConnectionPackage.XML_FILE_CONNECTION__MASK_XPATTERN:
            return getMaskXPattern();
        case ConnectionPackage.XML_FILE_CONNECTION__SCHEMA:
            return getSchema();
        case ConnectionPackage.XML_FILE_CONNECTION__ENCODING:
            return getEncoding();
        case ConnectionPackage.XML_FILE_CONNECTION__GROUP:
            return getGroup();
        case ConnectionPackage.XML_FILE_CONNECTION__ROOT:
            return getRoot();
        case ConnectionPackage.XML_FILE_CONNECTION__LOOP:
            return getLoop();
        case ConnectionPackage.XML_FILE_CONNECTION__INPUT_MODEL:
            return isInputModel();
        case ConnectionPackage.XML_FILE_CONNECTION__OUTPUT_FILE_PATH:
            return getOutputFilePath();
        case ConnectionPackage.XML_FILE_CONNECTION__FILE_CONTENT:
            return getFileContent();
        case ConnectionPackage.XML_FILE_CONNECTION__TARGET_NAME_SPACE:
            return getTargetNameSpace();
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
        case ConnectionPackage.XML_FILE_CONNECTION__XSD_FILE_PATH:
            setXsdFilePath((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__XML_FILE_PATH:
            setXmlFilePath((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__GUESS:
            setGuess((Boolean) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__MASK_XPATTERN:
            setMaskXPattern((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__SCHEMA:
            getSchema().clear();
            getSchema().addAll((Collection<? extends XmlXPathLoopDescriptor>) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__ENCODING:
            setEncoding((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__GROUP:
            getGroup().clear();
            getGroup().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__ROOT:
            getRoot().clear();
            getRoot().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__LOOP:
            getLoop().clear();
            getLoop().addAll((Collection<? extends XMLFileNode>) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__INPUT_MODEL:
            setInputModel((Boolean) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__OUTPUT_FILE_PATH:
            setOutputFilePath((String) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__FILE_CONTENT:
            setFileContent((byte[]) newValue);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__TARGET_NAME_SPACE:
            setTargetNameSpace((String) newValue);
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
        case ConnectionPackage.XML_FILE_CONNECTION__XSD_FILE_PATH:
            setXsdFilePath(XSD_FILE_PATH_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__XML_FILE_PATH:
            setXmlFilePath(XML_FILE_PATH_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__GUESS:
            setGuess(GUESS_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__MASK_XPATTERN:
            setMaskXPattern(MASK_XPATTERN_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__SCHEMA:
            getSchema().clear();
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__ENCODING:
            setEncoding(ENCODING_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__GROUP:
            getGroup().clear();
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__ROOT:
            getRoot().clear();
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__LOOP:
            getLoop().clear();
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__INPUT_MODEL:
            setInputModel(INPUT_MODEL_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__OUTPUT_FILE_PATH:
            setOutputFilePath(OUTPUT_FILE_PATH_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__FILE_CONTENT:
            setFileContent(FILE_CONTENT_EDEFAULT);
            return;
        case ConnectionPackage.XML_FILE_CONNECTION__TARGET_NAME_SPACE:
            setTargetNameSpace(TARGET_NAME_SPACE_EDEFAULT);
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
        case ConnectionPackage.XML_FILE_CONNECTION__XSD_FILE_PATH:
            return XSD_FILE_PATH_EDEFAULT == null ? xsdFilePath != null : !XSD_FILE_PATH_EDEFAULT.equals(xsdFilePath);
        case ConnectionPackage.XML_FILE_CONNECTION__XML_FILE_PATH:
            return XML_FILE_PATH_EDEFAULT == null ? xmlFilePath != null : !XML_FILE_PATH_EDEFAULT.equals(xmlFilePath);
        case ConnectionPackage.XML_FILE_CONNECTION__GUESS:
            return guess != GUESS_EDEFAULT;
        case ConnectionPackage.XML_FILE_CONNECTION__MASK_XPATTERN:
            return MASK_XPATTERN_EDEFAULT == null ? maskXPattern != null : !MASK_XPATTERN_EDEFAULT.equals(maskXPattern);
        case ConnectionPackage.XML_FILE_CONNECTION__SCHEMA:
            return schema != null && !schema.isEmpty();
        case ConnectionPackage.XML_FILE_CONNECTION__ENCODING:
            return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
        case ConnectionPackage.XML_FILE_CONNECTION__GROUP:
            return group != null && !group.isEmpty();
        case ConnectionPackage.XML_FILE_CONNECTION__ROOT:
            return root != null && !root.isEmpty();
        case ConnectionPackage.XML_FILE_CONNECTION__LOOP:
            return loop != null && !loop.isEmpty();
        case ConnectionPackage.XML_FILE_CONNECTION__INPUT_MODEL:
            return inputModel != INPUT_MODEL_EDEFAULT;
        case ConnectionPackage.XML_FILE_CONNECTION__OUTPUT_FILE_PATH:
            return OUTPUT_FILE_PATH_EDEFAULT == null ? outputFilePath != null : !OUTPUT_FILE_PATH_EDEFAULT.equals(outputFilePath);
        case ConnectionPackage.XML_FILE_CONNECTION__FILE_CONTENT:
            return FILE_CONTENT_EDEFAULT == null ? fileContent != null : !FILE_CONTENT_EDEFAULT.equals(fileContent);
        case ConnectionPackage.XML_FILE_CONNECTION__TARGET_NAME_SPACE:
            return TARGET_NAME_SPACE_EDEFAULT == null ? targetNameSpace != null : !TARGET_NAME_SPACE_EDEFAULT
                    .equals(targetNameSpace);
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
        result.append(" (XsdFilePath: ");
        result.append(xsdFilePath);
        result.append(", XmlFilePath: ");
        result.append(xmlFilePath);
        result.append(", Guess: ");
        result.append(guess);
        result.append(", MaskXPattern: ");
        result.append(maskXPattern);
        result.append(", Encoding: ");
        result.append(encoding);
        result.append(", inputModel: ");
        result.append(inputModel);
        result.append(", outputFilePath: ");
        result.append(outputFilePath);
        result.append(", fileContent: ");
        result.append(fileContent);
        result.append(", targetNameSpace: ");
        result.append(targetNameSpace);
        result.append(')');
        return result.toString();
    }

} //XmlFileConnectionImpl
