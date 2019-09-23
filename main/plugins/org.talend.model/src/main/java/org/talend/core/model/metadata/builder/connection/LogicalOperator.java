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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Logical Operator</b></em>', and
 * utility methods for working with them. <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getLogicalOperator()
 * @model
 * @generated
 */
public enum LogicalOperator implements Enumerator {
    /**
     * The '<em><b>And</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #AND_VALUE
     * @generated
     * @ordered
     */
    AND(0, "And", "&amp;&amp;"),

    /**
     * The '<em><b>Or</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #OR_VALUE
     * @generated
     * @ordered
     */
    OR(1, "Or", "||");

    /**
     * The '<em><b>And</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>AND</b></em>' literal object isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @see #AND
     * @model name="And" literal="&amp;&amp;"
     * @generated
     * @ordered
     */
    public static final int AND_VALUE = 0;

    /**
     * The '<em><b>Or</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>OR</b></em>' literal object isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OR
     * @model name="Or" literal="||"
     * @generated
     * @ordered
     */
    public static final int OR_VALUE = 1;

    /**
     * An array of all the '<em><b>Logical Operator</b></em>' enumerators.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static final LogicalOperator[] VALUES_ARRAY = new LogicalOperator[] { AND, OR, };

    /**
     * A public read-only list of all the '<em><b>Logical Operator</b></em>' enumerators.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static final List<LogicalOperator> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Logical Operator</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static LogicalOperator get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            LogicalOperator result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Logical Operator</b></em>' literal with the specified name.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static LogicalOperator getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            LogicalOperator result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Logical Operator</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static LogicalOperator get(int value) {
        switch (value) {
        case AND_VALUE:
            return AND;
        case OR_VALUE:
            return OR;
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
    private LogicalOperator(int value, String name, String literal) {
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

} // LogicalOperator
