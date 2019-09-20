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
 * A representation of the literals of the enumeration '<em><b>Mdm Concept Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMdmConceptType()
 * @model
 * @generated
 */
public enum MdmConceptType implements Enumerator {
    /**
     * The '<em><b>INPUT</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INPUT_VALUE
     * @generated
     * @ordered
     */
    INPUT(0, "INPUT", "INPUT"),

    /**
     * The '<em><b>OUTPUT</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OUTPUT_VALUE
     * @generated
     * @ordered
     */
    OUTPUT(1, "OUTPUT", "OUTPUT"),

    /**
     * The '<em><b>RECEIVE</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RECEIVE_VALUE
     * @generated
     * @ordered
     */
    RECEIVE(2, "RECEIVE", "RECEIVE");

    /**
     * The '<em><b>INPUT</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>INPUT</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INPUT
     * @model
     * @generated
     * @ordered
     */
    public static final int INPUT_VALUE = 0;

    /**
     * The '<em><b>OUTPUT</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>OUTPUT</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OUTPUT
     * @model
     * @generated
     * @ordered
     */
    public static final int OUTPUT_VALUE = 1;

    /**
     * The '<em><b>RECEIVE</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>RECEIVE</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #RECEIVE
     * @model
     * @generated
     * @ordered
     */
    public static final int RECEIVE_VALUE = 2;

    /**
     * An array of all the '<em><b>Mdm Concept Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final MdmConceptType[] VALUES_ARRAY = new MdmConceptType[] { INPUT, OUTPUT, RECEIVE, };

    /**
     * A public read-only list of all the '<em><b>Mdm Concept Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<MdmConceptType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Mdm Concept Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static MdmConceptType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            MdmConceptType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Mdm Concept Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static MdmConceptType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            MdmConceptType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Mdm Concept Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static MdmConceptType get(int value) {
        switch (value) {
        case INPUT_VALUE:
            return INPUT;
        case OUTPUT_VALUE:
            return OUTPUT;
        case RECEIVE_VALUE:
            return RECEIVE;
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
    private MdmConceptType(int value, String name, String literal) {
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

} //MdmConceptType
