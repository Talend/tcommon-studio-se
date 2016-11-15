// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;

public class TypesManager {

    public static String getDBTypeFromTalendType(String dbms, String talendType) {
        return MetadataTalendType.getMappingTypeRetriever(dbms).getDefaultSelectedDbType(talendType);
    }

    public static boolean checkDBType(String dbms, String talendType, String dbType) {
        MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(dbms);
        return mappingTypeRetriever.isAdvicedTalendToDbType(talendType, dbType);
    }
}
