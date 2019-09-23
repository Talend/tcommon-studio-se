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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Operator</b></em>', and utility
 * methods for working with them. <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getOperator()
 * @model
 * @generated
 */
public enum Operator implements Enumerator {
    /**
     * The '<em><b>Equals</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #EQUALS_VALUE
     * @generated
     * @ordered
     */
    EQUALS(0, "Equals", "=="),

    /**
     * The '<em><b>Not equals</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #NOT_EQUALS_VALUE
     * @generated
     * @ordered
     */
    NOT_EQUALS(1, "Not_equals", "!="),
    /**
     * The '<em><b>Greater</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #GREATER_VALUE
     * @generated
     * @ordered
     */
    GREATER(2, "Greater", ">"),
    /**
     * The '<em><b>Lower</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #LOWER_VALUE
     * @generated
     * @ordered
     */
    LOWER(3, "Lower", "<"),
    /**
     * The '<em><b>Greater or equals</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #GREATER_OR_EQUALS_VALUE
     * @generated
     * @ordered
     */
    GREATER_OR_EQUALS(4, "Greater_or_equals", ">="),
    /**
     * The '<em><b>Lower or equals</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #LOWER_OR_EQUALS_VALUE
     * @generated
     * @ordered
     */
    LOWER_OR_EQUALS(5, "Lower_or_equals", "<=");

    /**
     * The '<em><b>Equals</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Equals</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #EQUALS
     * @model name="Equals" literal="=="
     * @generated
     * @ordered
     */
    public static final int EQUALS_VALUE = 0;

    /**
     * The '<em><b>Not equals</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Not equals</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NOT_EQUALS
     * @model name="Not_equals" literal="!="
     * @generated
     * @ordered
     */
    public static final int NOT_EQUALS_VALUE = 1;

    /**
     * The '<em><b>Greater</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Greater</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #GREATER
     * @model name="Greater" literal=">"
     * @generated
     * @ordered
     */
    public static final int GREATER_VALUE = 2;

    /**
     * The '<em><b>Lower</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Lower</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LOWER
     * @model name="Lower" literal="<"
     * @generated
     * @ordered
     */
    public static final int LOWER_VALUE = 3;

    /**
     * The '<em><b>Greater or equals</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Greater or equals</b></em>' literal object isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #GREATER_OR_EQUALS
     * @model name="Greater_or_equals" literal=">="
     * @generated
     * @ordered
     */
    public static final int GREATER_OR_EQUALS_VALUE = 4;

    /**
     * The '<em><b>Lower or equals</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Lower or equals</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LOWER_OR_EQUALS
     * @model name="Lower_or_equals" literal="<="
     * @generated
     * @ordered
     */
    public static final int LOWER_OR_EQUALS_VALUE = 5;

    /**
     * An array of all the '<em><b>Operator</b></em>' enumerators.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static final Operator[] VALUES_ARRAY = new Operator[] { EQUALS, NOT_EQUALS, GREATER, LOWER, GREATER_OR_EQUALS,
            LOWER_OR_EQUALS, };

    /**
     * A public read-only list of all the '<em><b>Operator</b></em>' enumerators.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static final List<Operator> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Operator</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static Operator get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            Operator result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Operator</b></em>' literal with the specified name. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    public static Operator getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            Operator result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Operator</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static Operator get(int value) {
        switch (value) {
        case EQUALS_VALUE:
            return EQUALS;
        case NOT_EQUALS_VALUE:
            return NOT_EQUALS;
        case GREATER_VALUE:
            return GREATER;
        case LOWER_VALUE:
            return LOWER;
        case GREATER_OR_EQUALS_VALUE:
            return GREATER_OR_EQUALS;
        case LOWER_OR_EQUALS_VALUE:
            return LOWER_OR_EQUALS;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private Operator(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
        return value;
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
    public String getLiteral() {
        return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }

} // Operator
