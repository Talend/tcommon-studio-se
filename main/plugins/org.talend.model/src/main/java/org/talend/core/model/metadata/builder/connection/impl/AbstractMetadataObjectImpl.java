/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection.impl;

import java.util.HashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import orgomg.cwm.objectmodel.core.impl.ModelElementImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Abstract Metadata Object</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#isReadOnly <em>Read Only</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#isSynchronised <em>Synchronised</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.impl.AbstractMetadataObjectImpl#isDivergency <em>Divergency</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractMetadataObjectImpl extends ModelElementImpl implements AbstractMetadataObject {

    /**
     * The default value of the '{@link #getProperties() <em>Properties</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getProperties()
     * @generated
     * @ordered
     */
    protected static final HashMap PROPERTIES_EDEFAULT = (HashMap) ConnectionFactory.eINSTANCE.createFromString(
            ConnectionPackage.eINSTANCE.getMap(), ""); //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getProperties() <em>Properties</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getProperties()
     * @ordered
     */
    protected HashMap properties = (HashMap) PROPERTIES_EDEFAULT.clone();

    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final String ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected String id = ID_EDEFAULT;

    /**
     * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getComment()
     * @generated
     * @ordered
     */
    protected static final String COMMENT_EDEFAULT = ""; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getComment()
     * @generated
     * @ordered
     */
    protected String comment = COMMENT_EDEFAULT;

    /**
     * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected static final String LABEL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected String label = LABEL_EDEFAULT;

    /**
     * The default value of the '{@link #isReadOnly() <em>Read Only</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isReadOnly()
     * @generated
     * @ordered
     */
    protected static final boolean READ_ONLY_EDEFAULT = false;

    /**
     * The default value of the '{@link #isSynchronised() <em>Synchronised</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSynchronised()
     * @generated
     * @ordered
     */
    protected static final boolean SYNCHRONISED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSynchronised() <em>Synchronised</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isSynchronised()
     * @generated
     * @ordered
     */
    protected boolean synchronised = SYNCHRONISED_EDEFAULT;

    /**
     * The default value of the '{@link #isDivergency() <em>Divergency</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isDivergency()
     * @generated
     * @ordered
     */
    protected static final boolean DIVERGENCY_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isDivergency() <em>Divergency</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isDivergency()
     * @generated
     * @ordered
     */
    protected boolean divergency = DIVERGENCY_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected AbstractMetadataObjectImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ConnectionPackage.Literals.ABSTRACT_METADATA_OBJECT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public HashMap getProperties() {
        return properties;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setProperties(HashMap newProperties) {
        HashMap oldProperties = properties;
        properties = newProperties;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.ABSTRACT_METADATA_OBJECT__PROPERTIES,
                    oldProperties, properties));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.ABSTRACT_METADATA_OBJECT__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getComment() {
        return comment;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setComment(String newComment) {
        String oldComment = comment;
        comment = newComment;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.ABSTRACT_METADATA_OBJECT__COMMENT,
                    oldComment, comment));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getLabel() {
        return label;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setLabel(String newLabel) {
        String oldLabel = label;
        label = newLabel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL, oldLabel,
                    label));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isReadOnly() {
        // TODO: implement this method to return the 'Read Only' attribute
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setReadOnly(boolean newReadOnly) {
        // TODO: implement this method to set the 'Read Only' attribute
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSynchronised() {
        return synchronised;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSynchronised(boolean newSynchronised) {
        boolean oldSynchronised = synchronised;
        synchronised = newSynchronised;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.ABSTRACT_METADATA_OBJECT__SYNCHRONISED,
                    oldSynchronised, synchronised));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isDivergency() {
        return divergency;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDivergency(boolean newDivergency) {
        boolean oldDivergency = divergency;
        divergency = newDivergency;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConnectionPackage.ABSTRACT_METADATA_OBJECT__DIVERGENCY,
                    oldDivergency, divergency));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__PROPERTIES:
            return getProperties();
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__ID:
            return getId();
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__COMMENT:
            return getComment();
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL:
            return getLabel();
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__READ_ONLY:
            return isReadOnly();
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__SYNCHRONISED:
            return isSynchronised();
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__DIVERGENCY:
            return isDivergency();
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
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__PROPERTIES:
            setProperties((HashMap) newValue);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__ID:
            setId((String) newValue);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__COMMENT:
            setComment((String) newValue);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL:
            setLabel((String) newValue);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__READ_ONLY:
            setReadOnly((Boolean) newValue);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__SYNCHRONISED:
            setSynchronised((Boolean) newValue);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__DIVERGENCY:
            setDivergency((Boolean) newValue);
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
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__PROPERTIES:
            setProperties(PROPERTIES_EDEFAULT);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__ID:
            setId(ID_EDEFAULT);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__COMMENT:
            setComment(COMMENT_EDEFAULT);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL:
            setLabel(LABEL_EDEFAULT);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__READ_ONLY:
            setReadOnly(READ_ONLY_EDEFAULT);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__SYNCHRONISED:
            setSynchronised(SYNCHRONISED_EDEFAULT);
            return;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__DIVERGENCY:
            setDivergency(DIVERGENCY_EDEFAULT);
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
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__PROPERTIES:
            return PROPERTIES_EDEFAULT == null ? properties != null : !PROPERTIES_EDEFAULT.equals(properties);
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__ID:
            return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__COMMENT:
            return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__LABEL:
            return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__READ_ONLY:
            return isReadOnly() != READ_ONLY_EDEFAULT;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__SYNCHRONISED:
            return synchronised != SYNCHRONISED_EDEFAULT;
        case ConnectionPackage.ABSTRACT_METADATA_OBJECT__DIVERGENCY:
            return divergency != DIVERGENCY_EDEFAULT;
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
        result.append(" (properties: ");
        result.append(properties);
        result.append(", id: ");
        result.append(id);
        result.append(", comment: ");
        result.append(comment);
        result.append(", label: ");
        result.append(label);
        result.append(", synchronised: ");
        result.append(synchronised);
        result.append(", divergency: ");
        result.append(divergency);
        result.append(')');
        return result.toString();
    }

} // AbstractMetadataObjectImpl
