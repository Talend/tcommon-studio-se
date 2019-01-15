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
package org.talend.utils.xml;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class XmlUtils {

    public static Transformer getXmlSecureTransform() throws TransformerConfigurationException {
        Transformer transformer = getXmlSecureTransformerFactory().newTransformer();
        return transformer;
    }

    public static TransformerFactory getXmlSecureTransformerFactory() throws TransformerConfigurationException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        transFactory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        return transFactory;
    }
    
    public static DocumentBuilderFactory getSecureDocumentBuilderFactory() throws ParserConfigurationException {
        return getSecureDocumentBuilderFactory(true);
    }
    
    public static DocumentBuilderFactory getSecureDocumentBuilderFactory(boolean disAllowDoctypeDecl) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        if (disAllowDoctypeDecl) {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
        }
        return factory;
    }
}
