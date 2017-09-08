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
package org.talend.core.model.metadata;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.types.JavaType;

public abstract class MetadataTalendTypeFilter {

    /**
     * This method returns a List of String, representing the data types that must not be shown in the Talend schema.
     * 
     * @return
     */
    protected abstract List<String> getUnsupportedTypes();

    /**
     * This method has the responsibility to remove the unsupported type from the Talend schema. DOC rdubois Comment
     * method "filter".
     * 
     * @param arrayTalendTypes
     * @return
     */
    public String[] filter(String[] arrayTalendTypes) {
        List<String> unsupportedTypes = getUnsupportedTypes();
        List<String> arrayTalendTypesNew = new ArrayList<>();
        for (int i = 0; i < arrayTalendTypes.length; i++) {
            if (!unsupportedTypes.contains(arrayTalendTypes[i])) {
                arrayTalendTypesNew.add(arrayTalendTypes[i]);
            }
        }
        return arrayTalendTypesNew.toArray(new String[arrayTalendTypesNew.size()]);
    }

    public JavaType[] filter(JavaType[] arrayTalendTypes) {
        List<String> unsupportedTypes = getUnsupportedTypes();
        List<JavaType> arrayTalendTypesNew = new ArrayList<>();
        for (int i = 0; i < arrayTalendTypes.length; i++) {
            if (!unsupportedTypes.contains(arrayTalendTypes[i].getLabel())) {
                arrayTalendTypesNew.add(arrayTalendTypes[i]);
            }
        }
        return arrayTalendTypesNew.toArray(new JavaType[arrayTalendTypesNew.size()]);
    }
}
