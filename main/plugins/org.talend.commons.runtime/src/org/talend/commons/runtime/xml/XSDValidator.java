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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class XSDValidator {

    private static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage"; //$NON-NLS-1$

    private static final String SCHEMA_VALIDATOR = "http://java.sun.com/xml/jaxp/properties/schemaSource"; //$NON-NLS-1$

    public static Document checkXSD(File fileToCheck, File fileXSD) throws IOException, ParserConfigurationException,
            SAXException {
        final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

        fabrique.setAttribute(SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$
        fabrique.setAttribute(SCHEMA_VALIDATOR, fileXSD);
        fabrique.setValidating(true);

        Document document = null;
        boolean retry = false;

        for (int i = 0; i < 2; i++) {

            DocumentBuilder analyseur = fabrique.newDocumentBuilder();
            analyseur.setErrorHandler(new ErrorHandler() {

                public void error(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void fatalError(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void warning(final SAXParseException exception) throws SAXException {
                    throw exception;
                }

            });

            InputSource fileSource = new InputSource(fileToCheck.toURI().toASCIIString());
            try {
                if (retry) {
                    fileSource.setEncoding(System.getProperty("file.encoding")); //$NON-NLS-1$
                }
                document = analyseur.parse(fileSource);
                break;
            } catch (Exception e) {
                if (retry) {
                    IOException ioe = new IOException();
                    ioe.initCause(e);
                    throw ioe;
                }
                retry = !retry;

            }
        }
        return document;
    }
}
