// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.sql;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * DOC yyi 2010-07-02.
 * 
 * This class contains the data type regisited in XSDTypeRegister and XSDTypeExtendRegister <br>
 * To convert W3C data types to JDBC types <a>http://www.w3.org/TR/xmlschema-2/#built-in-datatypes</a>
 * 
 * @link org.eclipse.xsd.impl.type.XSDTypeRegister
 * @link org.talend.cwm.management.api.XSDTypeExtendRegister
 */
public final class XSDDataTypeConvertor {

    private XSDDataTypeConvertor() {
    }

    private static Map<String, Integer> map;

    /**
     * DOC get the map yyi 2010-07-01.
     * 
     * @link org.eclipse.xsd.impl.type.XSDTypeRegister
     * @link org.talend.cwm.management.api.XSDTypeExtendRegister
     * @return
     */
    public static Map<String, Integer> getMap() {
        if (map == null) {
            map = new HashMap<String, Integer>();
            map.put("anySimpleType", 2009);
            map.put("anyURI", Types.DATALINK);
            map.put("duration", Types.VARCHAR);
            map.put("base64Binary", Types.BINARY);
            map.put("boolean", Types.BOOLEAN);
            map.put("date", Types.DATE);
            map.put("dateTime", Types.TIMESTAMP);
            map.put("decimal", Types.DECIMAL);
            map.put("integer", Types.DECIMAL);
            map.put("nonPositiveInteger", Types.DECIMAL);
            map.put("long", Types.BIGINT);
            map.put("nonNegativeInteger", Types.DECIMAL);
            map.put("negativeInteger", Types.DECIMAL);
            map.put("int", Types.INTEGER);
            map.put("unsignedLong", Types.DECIMAL);
            map.put("positiveInteger", Types.DECIMAL);
            map.put("short", Types.SMALLINT);
            map.put("unsignedInt", Types.DECIMAL);
            map.put("byte", Types.DECIMAL);
            map.put("unsignedShort", Types.DECIMAL);
            map.put("unsignedByte", Types.DECIMAL);
            map.put("double", Types.DOUBLE);
            map.put("float", Types.FLOAT);
            map.put("gMonth", Types.DATE);
            map.put("gMonthDay", Types.DATE);
            map.put("gDay", Types.DATE);
            map.put("gYearMonth", Types.DATE);
            map.put("gYear", Types.DATE);
            map.put("NOTATION", Types.CLOB);
            map.put("hexBinary", Types.BINARY);
            map.put("QName", Types.CLOB);
            map.put("time", Types.TIMESTAMP);
            map.put("string", Types.CLOB);
        }
        return map;
    }

    /**
     * DOC yyi convert W3C data types to JDBC types <a>http://www.w3.org/TR/xmlschema-2/#built-in-datatypes</a>.
     * 
     * @param xsdDataType
     * @return java.sql.Types
     */
    public static int convertToJDBCType(String xsdDataType) {
        Integer type = getMap().get(xsdDataType);
        return null == type ? 0 : type;

    }
}
