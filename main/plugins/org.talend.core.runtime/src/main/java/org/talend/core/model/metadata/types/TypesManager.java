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

import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class TypesManager {

    //
    // private static Map<String, String> perlTempMappingXmlToTalend;
    //
    // private static Map<String, String> perlTempMappingTalendToXml;
    //
    // private static boolean initDone = false;
    //
    // public static void initializePerlTypes() {
    // if (!initDone) {
    // perlTempMappingXmlToTalend = new HashMap<String, String>();
    // perlTempMappingTalendToXml = new HashMap<String, String>();
    // perlTempMappingXmlToTalend.put("number", "int");
    // perlTempMappingXmlToTalend.put("decimal", "float");
    // perlTempMappingXmlToTalend.put("boolean", "String"); // ?
    // perlTempMappingXmlToTalend.put("string", "String");
    // perlTempMappingXmlToTalend.put("date", "Day");
    // perlTempMappingXmlToTalend.put("int", "int");
    // perlTempMappingXmlToTalend.put("datetime", "Day");
    //
    // perlTempMappingTalendToXml.put("char", "string");
    // perlTempMappingTalendToXml.put("Day", "string");
    // perlTempMappingTalendToXml.put("double", "decimal");
    // perlTempMappingTalendToXml.put("float", "decimal");
    // perlTempMappingTalendToXml.put("int", "number");
    // perlTempMappingTalendToXml.put("long", "number");
    // perlTempMappingTalendToXml.put("String", "string");
    // initDone = true;
    // }
    // }
    //
    // public static String getNameFromInterfaceType(String type) {
    // switch (LanguageManager.getCurrentLanguage()) {
    // case JAVA:
    // return type;
    // default:
    // initializePerlTypes();
    // return perlTempMappingTalendToXml.get(type);
    // }
    // }
    //    
    // public static String getTalendTypeFromXmlType(String type) {
    // switch (LanguageManager.getCurrentLanguage()) {
    // case JAVA:
    // return type;
    // default:
    // initializePerlTypes();
    // return perlTempMappingXmlToTalend.get(type);
    // }
    // }

    public static String getDBTypeFromTalendType(String dbms, String talendType) {
        // String typeName = getNameFromInterfaceType(talendType);//before
        // return MetadataTalendType.getMappingTypeRetriever(dbms).getDefaultSelectedDbType(typeName);
        return MetadataTalendType.getMappingTypeRetriever(dbms).getDefaultSelectedDbType(talendType);
    }

    public static boolean checkDBType(String dbms, String talendType, String dbType) {
        MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(dbms);
        return mappingTypeRetriever.isAdvicedTalendToDbType(talendType, dbType);
    }
}
