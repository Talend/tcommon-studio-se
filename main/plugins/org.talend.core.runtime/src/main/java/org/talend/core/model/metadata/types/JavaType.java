// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata.types;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public final class JavaType {

    private String label;

    private String id;

    private Class nullableClass;

    private Class primitiveClass;

    private boolean generateWithCanonicalName;

    // only to know for object input/output stream, if should base on readObject or not.
    private boolean objectBased;

    /**
     * @param label
     * @param primitiveType
     * @param clazzType
     */
    protected JavaType(Class nullableClass, Class primitiveClass) {
        super();
        this.nullableClass = nullableClass;
        this.primitiveClass = primitiveClass;
        this.label = primitiveClass.getSimpleName() + " | " + nullableClass.getSimpleName(); //$NON-NLS-1$
        this.id = createId(nullableClass.getSimpleName());
    }

    /**
     * DOC amaumont Comment method "createId".
     * 
     * @param simpleName
     * @return
     */
    private String createId(String value) {
        return "id_" + value; //$NON-NLS-1$
    }

    /**
     * @param label
     * @param nullableClass
     */
    public JavaType(Class nullableClass, boolean generateWithCanonicalName, boolean objectBased) {
        super();
        this.nullableClass = nullableClass;
        this.label = nullableClass.getSimpleName();
        this.id = createId(nullableClass.getSimpleName());
        this.generateWithCanonicalName = generateWithCanonicalName;
        this.objectBased = objectBased;
    }

    public JavaType(Class nullableClass, boolean generateWithCanonicalName, String label) {
        super();
        this.nullableClass = nullableClass;
        this.label = label;
        this.id = createId(label);
        this.generateWithCanonicalName = generateWithCanonicalName;
    }

    /**
     * @param label
     * @param id
     * @param nullableClass
     * @param primitiveClass
     */
    protected JavaType(String id, Class nullableClass, Class primitiveClass, String label) {
        super();
        this.label = label;
        this.nullableClass = nullableClass;
        this.primitiveClass = primitiveClass;
        this.id = createId(nullableClass.getSimpleName());
    }

    /**
     * DOC amaumont JavaType constructor comment.
     * 
     * @param id
     * @param nullableClass
     * @param primitiveClass
     */
    protected JavaType(String id, Class nullableClass, Class primitiveClass) {
        super();
        this.id = id;
        this.nullableClass = nullableClass;
        this.primitiveClass = primitiveClass;
    }

    /**
     * DOC amaumont JavaType constructor comment.
     * 
     * @param id
     * @param nullableClass
     */
    public JavaType(String id, Class nullableClass) {
        super();
        this.id = id;
        this.nullableClass = nullableClass;
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for objectClass.
     * 
     * @return the objectClass
     */
    public Class getNullableClass() {
        return this.nullableClass;
    }

    /**
     * Getter for primitiveClass. Can be null.
     * 
     * @return the primitiveClass
     */
    public Class getPrimitiveClass() {
        return this.primitiveClass;
    }

    /**
     * Getter for generateWithCanonicalName.
     * 
     * @return the generateWithCanonicalName
     */
    public boolean isGenerateWithCanonicalName() {
        return this.generateWithCanonicalName;
    }

    /**
     * 
     * @return
     * @author
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("JavaType["); //$NON-NLS-1$
        buffer.append("label = ").append(label); //$NON-NLS-1$
        buffer.append(" nullableClass = ").append(nullableClass); //$NON-NLS-1$
        buffer.append(" primitiveClass = ").append(primitiveClass); //$NON-NLS-1$
        buffer.append("]"); //$NON-NLS-1$
        return buffer.toString();
    }

    public boolean isPrimitive() {
        return this.primitiveClass != null;
    }

    public boolean isObjectBased() {
        return objectBased;
    }

    public void setObjectBased(boolean objectBased) {
        this.objectBased = objectBased;
    }
}
