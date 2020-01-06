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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Function</b></em>', and utility
 * methods for working with them. <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFunction()
 * @model
 * @generated
 */
public enum Function implements Enumerator {
    /**
     * The '<em><b>Empty</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #EMPTY_VALUE
     * @generated
     * @ordered
     */
    EMPTY(0, "Empty", ""),

    /**
     * The '<em><b>Lower case</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #LOWER_CASE_VALUE
     * @generated
     * @ordered
     */
    LOWER_CASE(1, "Lower_case", "$source == null? false : $source.toLowerCase().compareTo($target) $operator 0"),

    /**
     * The '<em><b>Upper case</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #UPPER_CASE_VALUE
     * @generated
     * @ordered
     */
    UPPER_CASE(2, "Upper_case", "$source == null? false : $source.toUpperCase().compareTo($target) $operator 0"),

    /**
     * The '<em><b>Lower case first</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #LOWER_CASE_FIRST_VALUE
     * @generated
     * @ordered
     */
    LOWER_CASE_FIRST(3, "Lower_case_first", "$source == null? false : $source.toLowerCase().charAt(0) $operator $target"),

    /**
     * The '<em><b>Upper case first</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #UPPER_CASE_FIRST_VALUE
     * @generated
     * @ordered
     */
    UPPER_CASE_FIRST(4, "Upper_case_first", "$source == null? false : $source.toUpperCase().charAt(0) $operator $target"),

    /**
     * The '<em><b>Length</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #LENGTH_VALUE
     * @generated
     * @ordered
     */
    LENGTH(5, "Length", "$source == null? false : $source.length() $operator $target"),

    /**
     * The '<em><b>Match</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #MATCH_VALUE
     * @generated
     * @ordered
     */
    MATCH(6, "Match", "$source == null? false : $source.matches($target) $operator true");

    /**
     * The '<em><b>Empty</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Empty</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #EMPTY
     * @model name="Empty" literal=""
     * @generated
     * @ordered
     */
    public static final int EMPTY_VALUE = 0;

    /**
     * The '<em><b>Lower case</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Lower Case</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LOWER_CASE
     * @model name="Lower_case" literal="$source == null? false : $source.toLowerCase().compareTo($target) $operator 0"
     * @generated
     * @ordered
     */
    public static final int LOWER_CASE_VALUE = 1;

    /**
     * The '<em><b>Upper case</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Upper Case</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UPPER_CASE
     * @model name="Upper_case" literal="$source == null? false : $source.toUpperCase().compareTo($target) $operator 0"
     * @generated
     * @ordered
     */
    public static final int UPPER_CASE_VALUE = 2;

    /**
     * The '<em><b>Lower case first</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Lower Case First</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LOWER_CASE_FIRST
     * @model name="Lower_case_first" literal="$source == null? false : $source.toLowerCase().charAt(0) $operator $target"
     * @generated
     * @ordered
     */
    public static final int LOWER_CASE_FIRST_VALUE = 3;

    /**
     * The '<em><b>Upper case first</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Upper Case First</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UPPER_CASE_FIRST
     * @model name="Upper_case_first" literal="$source == null? false : $source.toUpperCase().charAt(0) $operator $target"
     * @generated
     * @ordered
     */
    public static final int UPPER_CASE_FIRST_VALUE = 4;

    /**
     * The '<em><b>Length</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Length</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LENGTH
     * @model name="Length" literal="$source == null? false : $source.length() $operator $target"
     * @generated
     * @ordered
     */
    public static final int LENGTH_VALUE = 5;

    /**
     * The '<em><b>Match</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Match</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MATCH
     * @model name="Match" literal="$source == null? false : $source.matches($target) $operator true"
     * @generated
     * @ordered
     */
    public static final int MATCH_VALUE = 6;

    /**
     * An array of all the '<em><b>Function</b></em>' enumerators.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static final Function[] VALUES_ARRAY = new Function[] { EMPTY, LOWER_CASE, UPPER_CASE, LOWER_CASE_FIRST,
            UPPER_CASE_FIRST, LENGTH, MATCH, };

    /**
     * A public read-only list of all the '<em><b>Function</b></em>' enumerators.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static final List<Function> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Function</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static Function get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            Function result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Function</b></em>' literal with the specified name. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    public static Function getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            Function result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Function</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static Function get(int value) {
        switch (value) {
        case EMPTY_VALUE:
            return EMPTY;
        case LOWER_CASE_VALUE:
            return LOWER_CASE;
        case UPPER_CASE_VALUE:
            return UPPER_CASE;
        case LOWER_CASE_FIRST_VALUE:
            return LOWER_CASE_FIRST;
        case UPPER_CASE_FIRST_VALUE:
            return UPPER_CASE_FIRST;
        case LENGTH_VALUE:
            return LENGTH;
        case MATCH_VALUE:
            return MATCH;
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
    private Function(int value, String name, String literal) {
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

} // Function
