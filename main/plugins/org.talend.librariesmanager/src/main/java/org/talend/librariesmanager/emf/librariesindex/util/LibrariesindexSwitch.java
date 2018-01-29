/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.librariesmanager.emf.librariesindex.util;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.talend.librariesmanager.emf.librariesindex.*;
import org.talend.librariesmanager.emf.librariesindex.LibrariesIndex;
import org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.talend.librariesmanager.emf.librariesindex.LibrariesindexPackage
 * @generated
 */
public class LibrariesindexSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static LibrariesindexPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LibrariesindexSwitch() {
        if (modelPackage == null) {
            modelPackage = LibrariesindexPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @parameter ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case LibrariesindexPackage.LIBRARIES_INDEX: {
                LibrariesIndex librariesIndex = (LibrariesIndex)theEObject;
                T result = caseLibrariesIndex(librariesIndex);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case LibrariesindexPackage.JAR_TO_RELATIVE_PATH: {
                @SuppressWarnings("unchecked") Map.Entry<String, String> jarToRelativePath = (Map.Entry<String, String>)theEObject;
                T result = casejarToRelativePath(jarToRelativePath);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case LibrariesindexPackage.CUSTOM_URI_MAP: {
                CustomURIMap customURIMap = (CustomURIMap)theEObject;
                T result = caseCustomURIMap(customURIMap);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Libraries Index</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Libraries Index</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLibrariesIndex(LibrariesIndex object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>jar To Relative Path</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>jar To Relative Path</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casejarToRelativePath(Map.Entry<String, String> object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Custom URI Map</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Custom URI Map</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCustomURIMap(CustomURIMap object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //LibrariesindexSwitch
