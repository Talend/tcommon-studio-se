/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.core.model.metadata.builder.connection;

import orgomg.cwm.resource.record.Field;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * represents a metada column which contains source (such as DB) definitions as weel as Talend mappings
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getSourceType <em>Source Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getTalendType <em>Talend Type</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#isKey <em>Key</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#isNullable <em>Nullable</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getTable <em>Table</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getOriginalField <em>Original Field</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getDisplayField <em>Display Field</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getOriginalLength <em>Original Length</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getRelatedEntity <em>Related Entity</em>}</li>
 *   <li>{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getRelationshipType <em>Relationship Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn()
 * @model
 * @generated
 */
public interface MetadataColumn extends AbstractMetadataObject, Field {

    /**
     * Returns the value of the '<em><b>Source Type</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Type</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> Schema DB type (VARCHAR for example ), can be initialised from DB
     * column type and modified by the user.) This is maintained in synch with the TalendType (at least in the Table
     * schema editor).
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Source Type</em>' attribute.
     * @see #setSourceType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_SourceType()
     * @model
     * @generated
     */
    String getSourceType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getSourceType <em>Source Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Type</em>' attribute.
     * @see #getSourceType()
     * @generated
     */
    void setSourceType(String value);

    /**
     * Returns the value of the '<em><b>Default Value</b></em>' attribute. The default value is <code>""</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default Value</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * @deprecated Use initialValue instead (This represents the default value for column. This may be changed by the
     * user.)
     * 
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default Value</em>' attribute.
     * @see #setDefaultValue(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_DefaultValue()
     * @model default="" transient="true" volatile="true"
     * @generated
     */
    String getDefaultValue();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getDefaultValue <em>Default Value</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' attribute.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(String value);

    /**
     * Returns the value of the '<em><b>Talend Type</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Talend Type</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> java type used by Talend for handling this column elements; This
     * seems to be synched with the sourceType. This must be the case for schema used for Table creation. <!--
     * end-model-doc -->
     * 
     * @return the value of the '<em>Talend Type</em>' attribute.
     * @see #setTalendType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_TalendType()
     * @model
     * @generated
     */
    String getTalendType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getTalendType <em>Talend Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Talend Type</em>' attribute.
     * @see #getTalendType()
     * @generated
     */
    void setTalendType(String value);

    /**
     * Returns the value of the '<em><b>Key</b></em>' attribute. The default value is <code>"false"</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Key</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> Whether this column is a considered a key, in a business meaning
     * (This is not technical). This may apply to file, xml or dB columns. May be changed by the user. When retrieving
     * Metadata from DB this will be set to true if the column belong to the primary key. <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Key</em>' attribute.
     * @see #setKey(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_Key()
     * @model default="false"
     * @generated
     */
    boolean isKey();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#isKey <em>Key</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Key</em>' attribute.
     * @see #isKey()
     * @generated
     */
    void setKey(boolean value);

    /**
     * Returns the value of the '<em><b>Nullable</b></em>' attribute. The default value is <code>"true"</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nullable</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> whether this column supports null values. May be changed by the
     * user. <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Nullable</em>' attribute.
     * @see #setNullable(boolean)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_Nullable()
     * @model default="true" dataType="orgomg.cwm.objectmodel.core.Boolean"
     * @generated
     */
    boolean isNullable();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#isNullable <em>Nullable</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Nullable</em>' attribute.
     * @see #isNullable()
     * @generated
     */
    void setNullable(boolean value);

    /**
     * Returns the value of the '<em><b>Table</b></em>' reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Table</em>' container reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> reference to the containing table or view <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Table</em>' reference.
     * @see #setTable(MetadataTable)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_Table()
     * @model resolveProxies="false" transient="true" volatile="true" derived="true"
     * @generated
     */
    MetadataTable getTable();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getTable <em>Table</em>}' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Table</em>' reference.
     * @see #getTable()
     * @generated
     */
    void setTable(MetadataTable value);

    /**
     * Returns the value of the '<em><b>Original Field</b></em>' attribute. The default value is <code>""</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Original Field</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * @deprecated use g(s)etName Logical name of the column <!-- end-model-doc -->
     * @return the value of the '<em>Original Field</em>' attribute.
     * @see #setOriginalField(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_OriginalField()
     * @model default="" transient="true" volatile="true"
     * @generated
     */
    String getOriginalField();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getOriginalField <em>Original Field</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Original Field</em>' attribute.
     * @see #getOriginalField()
     * @generated
     */
    void setOriginalField(String value);

    /**
     * Returns the value of the '<em><b>Pattern</b></em>' attribute. The default value is <code>""</code>. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pattern</em>' attribute isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> pattern mainly used for date parsing <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Pattern</em>' attribute.
     * @see #setPattern(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_Pattern()
     * @model default=""
     * @generated
     */
    String getPattern();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getPattern <em>Pattern</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Pattern</em>' attribute.
     * @see #getPattern()
     * @generated
     */
    void setPattern(String value);

    /**
     * Returns the value of the '<em><b>Display Field</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Display Field</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Display Field</em>' attribute.
     * @see #setDisplayField(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_DisplayField()
     * @model
     * @generated
     */
    String getDisplayField();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getDisplayField <em>Display Field</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Display Field</em>' attribute.
     * @see #getDisplayField()
     * @generated
     */
    void setDisplayField(String value);

    /**
     * Returns the value of the '<em><b>Original Length</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Original Length</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Original Length</em>' attribute.
     * @see #setOriginalLength(long)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_OriginalLength()
     * @model dataType="orgomg.cwm.objectmodel.core.Integer"
     * @generated
     */
    long getOriginalLength();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getOriginalLength <em>Original Length</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Original Length</em>' attribute.
     * @see #getOriginalLength()
     * @generated
     */
    void setOriginalLength(long value);

    /**
     * Returns the value of the '<em><b>Related Entity</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Related Entity</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Related Entity</em>' attribute.
     * @see #setRelatedEntity(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_RelatedEntity()
     * @model default=""
     * @generated
     */
    String getRelatedEntity();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getRelatedEntity <em>Related Entity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Related Entity</em>' attribute.
     * @see #getRelatedEntity()
     * @generated
     */
    void setRelatedEntity(String value);

    /**
     * Returns the value of the '<em><b>Relationship Type</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Relationship Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Relationship Type</em>' attribute.
     * @see #setRelationshipType(String)
     * @see org.talend.core.model.metadata.builder.connection.ConnectionPackage#getMetadataColumn_RelationshipType()
     * @model default=""
     * @generated
     */
    String getRelationshipType();

    /**
     * Sets the value of the '{@link org.talend.core.model.metadata.builder.connection.MetadataColumn#getRelationshipType <em>Relationship Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Relationship Type</em>' attribute.
     * @see #getRelationshipType()
     * @generated
     */
    void setRelationshipType(String value);

} // MetadataColumn
