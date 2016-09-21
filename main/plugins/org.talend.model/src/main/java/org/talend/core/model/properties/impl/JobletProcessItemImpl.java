/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.properties.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.designer.joblet.model.JobletProcess;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Joblet Process Item</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.core.model.properties.impl.JobletProcessItemImpl#getJobletProcess <em>Joblet Process</em>}</li>
 * <li>{@link org.talend.core.model.properties.impl.JobletProcessItemImpl#getIcon <em>Icon</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class JobletProcessItemImpl extends ItemImpl implements JobletProcessItem {

    /**
     * The cached value of the '{@link #getJobletProcess() <em>Joblet Process</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getJobletProcess()
     * @generated
     * @ordered
     */
    protected JobletProcess jobletProcess;

    /**
     * The cached value of the '{@link #getIcon() <em>Icon</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getIcon()
     * @generated
     * @ordered
     */
    protected ByteArray icon;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected JobletProcessItemImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.JOBLET_PROCESS_ITEM;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public synchronized JobletProcess getJobletProcess() {
        if (jobletProcess != null && jobletProcess.eIsProxy()) {
            InternalEObject oldJobletProcess = (InternalEObject) jobletProcess;
            jobletProcess = (JobletProcess) eResolveProxy(oldJobletProcess);
            if (jobletProcess.eResource() == null && eResource() != null) {
                URI uri = EcoreUtil.getURI(jobletProcess);
                if (uri.hasFragment()) {
                    uri = uri.trimFragment();
                }
                Resource resource = eResource().getResourceSet().getResource(uri, true);
                for (EObject object : resource.getContents()) {
                    if (object instanceof JobletProcess) {
                        jobletProcess = (JobletProcess) object;
                        break;
                    }
                }
            }
            if (jobletProcess != oldJobletProcess) {
                if (eNotificationRequired()) {
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE,
                            PropertiesPackage.JOBLET_PROCESS_ITEM__JOBLET_PROCESS, oldJobletProcess, jobletProcess));
                }
            }
        }
        return jobletProcess;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public JobletProcess basicGetJobletProcess() {
        return jobletProcess;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setJobletProcess(JobletProcess newJobletProcess) {
        JobletProcess oldJobletProcess = jobletProcess;
        jobletProcess = newJobletProcess;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.JOBLET_PROCESS_ITEM__JOBLET_PROCESS,
                    oldJobletProcess, jobletProcess));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public ByteArray getIcon() {
        if (icon != null && icon.eIsProxy()) {
            InternalEObject oldIcon = (InternalEObject) icon;
            icon = (ByteArray) eResolveProxy(oldIcon);
            if (icon.eResource() == null && eResource() != null &&eResource().getResourceSet()!=null) {
                URI uri = EcoreUtil.getURI(icon);
                if (uri.hasFragment()) {
                    uri = uri.trimFragment();
                }
                Resource resource = eResource().getResourceSet().getResource(uri, true);
                for (EObject object : resource.getContents()) {
                    if (object instanceof ByteArray) {
                        icon = (ByteArray) object;
                        break;
                    }
                }
            }
            if (icon != oldIcon) {
                if (eNotificationRequired()) {
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.JOBLET_PROCESS_ITEM__ICON,
                            oldIcon, icon));
                }
            }
        }
        return icon;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ByteArray basicGetIcon() {
        return icon;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setIcon(ByteArray newIcon) {
        ByteArray oldIcon = icon;
        icon = newIcon;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.JOBLET_PROCESS_ITEM__ICON, oldIcon, icon));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case PropertiesPackage.JOBLET_PROCESS_ITEM__JOBLET_PROCESS:
            if (resolve) {
                return getJobletProcess();
            }
            return basicGetJobletProcess();
        case PropertiesPackage.JOBLET_PROCESS_ITEM__ICON:
            if (resolve) {
                return getIcon();
            }
            return basicGetIcon();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case PropertiesPackage.JOBLET_PROCESS_ITEM__JOBLET_PROCESS:
            setJobletProcess((JobletProcess) newValue);
            return;
        case PropertiesPackage.JOBLET_PROCESS_ITEM__ICON:
            setIcon((ByteArray) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case PropertiesPackage.JOBLET_PROCESS_ITEM__JOBLET_PROCESS:
            setJobletProcess((JobletProcess) null);
            return;
        case PropertiesPackage.JOBLET_PROCESS_ITEM__ICON:
            setIcon((ByteArray) null);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case PropertiesPackage.JOBLET_PROCESS_ITEM__JOBLET_PROCESS:
            return jobletProcess != null;
        case PropertiesPackage.JOBLET_PROCESS_ITEM__ICON:
            return icon != null;
        }
        return super.eIsSet(featureID);
    }

} // JobletProcessItemImpl
