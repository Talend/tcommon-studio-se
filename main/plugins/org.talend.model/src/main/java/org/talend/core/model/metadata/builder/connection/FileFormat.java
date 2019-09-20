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
 * A representation of the literals of the enumeration '<em><b>File Format</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getFileFormat()
 * @model
 * @generated
 */
public enum FileFormat implements Enumerator {
    /**
     * The '<em><b>UNIX</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNIX
     * @generated
     * @ordered
     */
    UNIX_LITERAL(0, "UNIX", "UNIX"),
    /**
     * The '<em><b>MAC</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAC
     * @generated
     * @ordered
     */
    MAC_LITERAL(1, "MAC", "MAC"),
    /**
     * The '<em><b>WINDOWS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #WINDOWS
     * @generated
     * @ordered
     */
    WINDOWS_LITERAL(2, "WINDOWS", "WINDOWS");

    /**
     * The '<em><b>UNIX</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>UNIX</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UNIX_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int UNIX = 0;

    /**
     * The '<em><b>MAC</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MAC</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MAC_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int MAC = 1;

    /**
     * The '<em><b>WINDOWS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>WINDOWS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #WINDOWS_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int WINDOWS = 2;

    /**
     * An array of all the '<em><b>File Format</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final FileFormat[] VALUES_ARRAY = new FileFormat[] { UNIX_LITERAL, MAC_LITERAL, WINDOWS_LITERAL, };

    /**
     * A public read-only list of all the '<em><b>File Format</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<FileFormat> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>File Format</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FileFormat get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FileFormat result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>File Format</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FileFormat getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FileFormat result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>File Format</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FileFormat get(int value) {
        switch (value) {
        case UNIX:
            return UNIX_LITERAL;
        case MAC:
            return MAC_LITERAL;
        case WINDOWS:
            return WINDOWS_LITERAL;
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
    private FileFormat(int value, String name, String literal) {
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
}
