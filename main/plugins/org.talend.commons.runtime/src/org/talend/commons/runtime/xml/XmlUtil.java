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
package org.talend.commons.runtime.xml;

/**
 * ggu class global comment. Detailled comment
 */
public final class XmlUtil {

    public static final String XML = "xml"; //$NON-NLS-1$

    public static final String XSD = "xsd"; //$NON-NLS-1$

    public static final String WSDL = "wsdl"; //$NON-NLS-1$

    public static final String FILE_XML_SUFFIX = '.' + XML;

    public static final String FILE_XSD_SUFFIX = '.' + XSD;

    public static final String FILE_WSDL_SUFFIX = '.' + WSDL;

    public static boolean isXMLFile(String value) {
        if (value != null) {
            return value.toLowerCase().endsWith(FILE_XML_SUFFIX);
        }
        return false;
    }

    public static boolean isXSDFile(String value) {
        if (value != null) {
            return value.toLowerCase().endsWith(FILE_XSD_SUFFIX);
        }
        return false;
    }

    public static boolean isWSDLFile(String value) {
        if (value != null) {
            return value.toLowerCase().endsWith(FILE_WSDL_SUFFIX);
        }
        return false;
    }
}
