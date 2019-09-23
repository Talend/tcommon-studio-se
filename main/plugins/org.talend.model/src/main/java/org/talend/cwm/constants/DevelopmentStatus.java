/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.cwm.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Development Status</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.talend.cwm.constants.ConstantsPackage#getDevelopmentStatus()
 * @model
 * @generated
 */
public enum DevelopmentStatus implements Enumerator {
    /**
     * The '<em><b>DRAFT</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DRAFT_VALUE
     * @generated
     * @ordered
     */
    DRAFT(0, "DRAFT", "Draft"),

    /**
     * The '<em><b>PROD</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PROD_VALUE
     * @generated
     * @ordered
     */
    PROD(1, "PROD", "Production");

    /**
     * The '<em><b>DRAFT</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>DRAFT</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #DRAFT
     * @model literal="Draft"
     * @generated
     * @ordered
     */
    public static final int DRAFT_VALUE = 0;

    /**
     * The '<em><b>PROD</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>PROD</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #PROD
     * @model literal="Production"
     * @generated
     * @ordered
     */
    public static final int PROD_VALUE = 1;

    /**
     * An array of all the '<em><b>Development Status</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final DevelopmentStatus[] VALUES_ARRAY = new DevelopmentStatus[] { DRAFT, PROD, };

    /**
     * A public read-only list of all the '<em><b>Development Status</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<DevelopmentStatus> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Development Status</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static DevelopmentStatus get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            DevelopmentStatus result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Development Status</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static DevelopmentStatus getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            DevelopmentStatus result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Development Status</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static DevelopmentStatus get(int value) {
        switch (value) {
        case DRAFT_VALUE:
            return DRAFT;
        case PROD_VALUE:
            return PROD;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private DevelopmentStatus(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }

} //DevelopmentStatus
