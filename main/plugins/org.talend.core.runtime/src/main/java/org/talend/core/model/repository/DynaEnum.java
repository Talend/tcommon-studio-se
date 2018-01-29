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
package org.talend.core.model.repository;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.talend.core.runtime.i18n.Messages;

public class DynaEnum<E extends DynaEnum<E>> {

    private static Map<Class<? extends DynaEnum<?>>, Map<String, DynaEnum<?>>> elements = new LinkedHashMap<Class<? extends DynaEnum<?>>, Map<String, DynaEnum<?>>>();

    private String key;

    protected boolean isStaticNode;

    protected String type;

    public String getKey() {
        return this.key;
    }

    private final int ordinal;

    public final int ordinal() {
        return ordinal;
    }

    public String name() {
        return type;
    }

    protected DynaEnum(String key, String type, boolean isStaticNode, int ordinal) {
        this.key = key;
        this.type = type;
        this.isStaticNode = isStaticNode;
        this.ordinal = ordinal;

        Map<String, DynaEnum<?>> typeElements = elements.get(getClass());
        if (typeElements == null) {
            typeElements = new LinkedHashMap<String, DynaEnum<?>>();
            elements.put(getDynaEnumClass(), typeElements);
        }
        // changed by hqzhang for TDI-20504. we use the upper case string to find type, but type definition for MDM item
        // is not in upper case, have to change them in code.
        typeElements.put(type.toUpperCase(), this);
        // TDI-20504 end

    }

    @SuppressWarnings("unchecked")
    private Class<? extends DynaEnum<?>> getDynaEnumClass() {
        return (Class<? extends DynaEnum<?>>) getClass();
    }

    @Override
    public String toString() {
        if (isStaticNode()) {
            return Messages.getString(key);
        }
        return key;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DynaEnum)) {
            return false;
        }
        DynaEnum other = (DynaEnum) object;
        if (!other.getType().equals(this.getType())) {
            return false;
        }
        // if (!other.getKey().equals(this.getKey())) {
        // return false;
        // }
        return true;
    }

    @Override
    public final int hashCode() {
        return 13 * getType().hashCode();
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public final int compareTo(E other) {
        DynaEnum<?> self = this;
        if (self.getClass() != other.getClass() && // optimization
                self.getDeclaringClass() != other.getDeclaringClass()) {
            throw new ClassCastException();
        }
        return self.ordinal - other.ordinal();
    }

    @SuppressWarnings("unchecked")
    public final Class<E> getDeclaringClass() {
        Class clazz = getClass();
        Class zuper = clazz.getSuperclass();
        return (zuper == DynaEnum.class) ? clazz : zuper;
    }

    @SuppressWarnings("unchecked")
    public static <T extends DynaEnum<T>> T valueOfEnum(Class<T> enumType, String name) {
        // changed by hqzhang for TDI-20504. we use the upper case string to find type, but type definition for MDM item
        // is not in upper case, have to change them in code.
        T t = (T) elements.get(enumType).get(name.toUpperCase());
        // Maybe, need check it.
        // if (t == null) {
        //            ExceptionHandler.process(new IllegalArgumentException("Can't find the " + name)); //$NON-NLS-1$
        // }
        return t;
        // TDI-20504 end
    }

    @SuppressWarnings("unused")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize enum");
    }

    @SuppressWarnings("unused")
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize enum");
    }

    @Override
    protected final void finalize() {
    }

    public static <E> DynaEnum<? extends DynaEnum<?>>[] values() {
        throw new IllegalStateException("Sub class of DynaEnum must implement method valus()");
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] values(Class<E> enumType) {
        Collection<DynaEnum<?>> values = elements.get(enumType).values();
        int n = values.size();
        E[] typedValues = (E[]) Array.newInstance(enumType, n);
        int i = 0;
        for (DynaEnum<?> value : values) {
            Array.set(typedValues, i, value);
            i++;
        }

        return typedValues;
    }

    public boolean isStaticNode() {
        return this.isStaticNode;
    }

    public String getType() {
        return type;
    }
}
