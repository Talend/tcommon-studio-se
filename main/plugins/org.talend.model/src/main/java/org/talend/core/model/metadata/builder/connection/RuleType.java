/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Rule Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getRuleType()
 * @model
 * @generated
 */
public enum RuleType implements Enumerator {
    /**
     * The '<em><b>REFERENCE</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #REFERENCE_VALUE
     * @generated
     * @ordered
     */
    REFERENCE(0, "REFERENCE", "REFERENCE"),

    /**
     * The '<em><b>BASIC</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BASIC_VALUE
     * @generated
     * @ordered
     */
    BASIC(1, "BASIC", "BASIC"),

    /**
     * The '<em><b>CUSTOM</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CUSTOM_VALUE
     * @generated
     * @ordered
     */
    CUSTOM(2, "CUSTOM", "CUSTOM");

    /**
     * The '<em><b>REFERENCE</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>REFERENCE</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #REFERENCE
     * @model
     * @generated
     * @ordered
     */
    public static final int REFERENCE_VALUE = 0;

    /**
     * The '<em><b>BASIC</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>BASIC</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BASIC
     * @model
     * @generated
     * @ordered
     */
    public static final int BASIC_VALUE = 1;

    /**
     * The '<em><b>CUSTOM</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>CUSTOM</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CUSTOM
     * @model
     * @generated
     * @ordered
     */
    public static final int CUSTOM_VALUE = 2;

    /**
     * An array of all the '<em><b>Rule Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final RuleType[] VALUES_ARRAY = new RuleType[] { REFERENCE, BASIC, CUSTOM, };

    /**
     * A public read-only list of all the '<em><b>Rule Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<RuleType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Rule Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RuleType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RuleType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Rule Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RuleType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RuleType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Rule Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RuleType get(int value) {
        switch (value) {
        case REFERENCE_VALUE:
            return REFERENCE;
        case BASIC_VALUE:
            return BASIC;
        case CUSTOM_VALUE:
            return CUSTOM;
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
    private RuleType(int value, String name, String literal) {
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

} //RuleType
