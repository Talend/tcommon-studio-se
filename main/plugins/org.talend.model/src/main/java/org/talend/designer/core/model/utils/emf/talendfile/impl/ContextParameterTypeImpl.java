/**
 * <copyright> </copyright>
 *
 * $Id$
 */
package org.talend.designer.core.model.utils.emf.talendfile.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage;
import org.talend.utils.security.StudioEncryption;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Context Parameter Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#getPrompt <em>Prompt</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#isPromptNeeded <em>Prompt Needed</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#getRepositoryContextId <em>Repository Context Id</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.talend.designer.core.model.utils.emf.talendfile.impl.ContextParameterTypeImpl#getInternalId <em>Internal Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContextParameterTypeImpl extends EObjectImpl implements ContextParameterType {

    /**
     * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getComment()
     * @generated
     * @ordered
     */
    protected static final String COMMENT_EDEFAULT = null;

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
     * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getPrompt() <em>Prompt</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPrompt()
     * @generated
     * @ordered
     */
    protected static final String PROMPT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPrompt() <em>Prompt</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getPrompt()
     * @generated
     * @ordered
     */
    protected String prompt = PROMPT_EDEFAULT;

    /**
     * The default value of the '{@link #isPromptNeeded() <em>Prompt Needed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isPromptNeeded()
     * @generated
     * @ordered
     */
    protected static final boolean PROMPT_NEEDED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isPromptNeeded() <em>Prompt Needed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isPromptNeeded()
     * @generated
     * @ordered
     */
    protected boolean promptNeeded = PROMPT_NEEDED_EDEFAULT;

    /**
     * This is true if the Prompt Needed attribute has been set.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean promptNeededESet;

    /**
     * The default value of the '{@link #getRepositoryContextId() <em>Repository Context Id</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getRepositoryContextId()
     * @generated
     * @ordered
     */
    protected static final String REPOSITORY_CONTEXT_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRepositoryContextId() <em>Repository Context Id</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getRepositoryContextId()
     * @generated
     * @ordered
     */
    protected String repositoryContextId = REPOSITORY_CONTEXT_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getType()
     * @generated
     * @ordered
     */
    protected String type = TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getInternalId() <em>Internal Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInternalId()
     * @generated
     * @ordered
     */
    protected static final String INTERNAL_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInternalId() <em>Internal Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInternalId()
     * @generated
     * @ordered
     */
    protected String internalId = INTERNAL_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected ContextParameterTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return TalendFilePackage.Literals.CONTEXT_PARAMETER_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__COMMENT, oldComment, comment));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPrompt(String newPrompt) {
        String oldPrompt = prompt;
        prompt = newPrompt;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT, oldPrompt, prompt));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isPromptNeeded() {
        return promptNeeded;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setPromptNeeded(boolean newPromptNeeded) {
        boolean oldPromptNeeded = promptNeeded;
        promptNeeded = newPromptNeeded;
        boolean oldPromptNeededESet = promptNeededESet;
        promptNeededESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT_NEEDED, oldPromptNeeded, promptNeeded, !oldPromptNeededESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void unsetPromptNeeded() {
        boolean oldPromptNeeded = promptNeeded;
        boolean oldPromptNeededESet = promptNeededESet;
        promptNeeded = PROMPT_NEEDED_EDEFAULT;
        promptNeededESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT_NEEDED, oldPromptNeeded, PROMPT_NEEDED_EDEFAULT, oldPromptNeededESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetPromptNeeded() {
        return promptNeededESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getRepositoryContextId() {
        return repositoryContextId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setRepositoryContextId(String newRepositoryContextId) {
        String oldRepositoryContextId = repositoryContextId;
        repositoryContextId = newRepositoryContextId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__REPOSITORY_CONTEXT_ID, oldRepositoryContextId, repositoryContextId));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setType(String newType) {
        String oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    public String getRawValue() {
        if (value != null && value.length() > 0 && PasswordEncryptUtil.isPasswordType(getType())) {
            String decryptValue = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).decrypt(value);
            if (decryptValue != null) {
                return decryptValue;
            }
        }
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInternalId() {
        return internalId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInternalId(String newInternalId) {
        String oldInternalId = internalId;
        internalId = newInternalId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TalendFilePackage.CONTEXT_PARAMETER_TYPE__INTERNAL_ID, oldInternalId, internalId));
    }

    public void setRawValue(String newValue) {
        if (newValue != null && newValue.length() > 0 && PasswordEncryptUtil.isPasswordType(getType())) {
            String encryptValue = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM)
                    .encrypt(newValue);
            if (encryptValue != null) {
                setValue(encryptValue);
                return;
            }
        }
        setValue(newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__COMMENT:
                return getComment();
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__NAME:
                return getName();
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT:
                return getPrompt();
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT_NEEDED:
                return isPromptNeeded() ? Boolean.TRUE : Boolean.FALSE;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__REPOSITORY_CONTEXT_ID:
                return getRepositoryContextId();
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__TYPE:
                return getType();
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__VALUE:
                return getValue();
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__INTERNAL_ID:
                return getInternalId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__COMMENT:
                setComment((String)newValue);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__NAME:
                setName((String)newValue);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT:
                setPrompt((String)newValue);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT_NEEDED:
                setPromptNeeded(((Boolean)newValue).booleanValue());
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__REPOSITORY_CONTEXT_ID:
                setRepositoryContextId((String)newValue);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__TYPE:
                setType((String)newValue);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__VALUE:
                setValue((String)newValue);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__INTERNAL_ID:
                setInternalId((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__COMMENT:
                setComment(COMMENT_EDEFAULT);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT:
                setPrompt(PROMPT_EDEFAULT);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT_NEEDED:
                unsetPromptNeeded();
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__REPOSITORY_CONTEXT_ID:
                setRepositoryContextId(REPOSITORY_CONTEXT_ID_EDEFAULT);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__INTERNAL_ID:
                setInternalId(INTERNAL_ID_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__COMMENT:
                return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT:
                return PROMPT_EDEFAULT == null ? prompt != null : !PROMPT_EDEFAULT.equals(prompt);
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__PROMPT_NEEDED:
                return isSetPromptNeeded();
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__REPOSITORY_CONTEXT_ID:
                return REPOSITORY_CONTEXT_ID_EDEFAULT == null ? repositoryContextId != null : !REPOSITORY_CONTEXT_ID_EDEFAULT.equals(repositoryContextId);
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case TalendFilePackage.CONTEXT_PARAMETER_TYPE__INTERNAL_ID:
                return INTERNAL_ID_EDEFAULT == null ? internalId != null : !INTERNAL_ID_EDEFAULT.equals(internalId);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (comment: ");
        result.append(comment);
        result.append(", name: ");
        result.append(name);
        result.append(", prompt: ");
        result.append(prompt);
        result.append(", promptNeeded: ");
        if (promptNeededESet) result.append(promptNeeded); else result.append("<unset>");
        result.append(", repositoryContextId: ");
        result.append(repositoryContextId);
        result.append(", type: ");
        result.append(type);
        result.append(", value: ");
        result.append(value);
        result.append(", internalId: ");
        result.append(internalId);
        result.append(')');
        return result.toString();
    }

} // ContextParameterTypeImpl
