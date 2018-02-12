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
package org.talend.core.model.utils;

/**
 * DOC acer class global comment. Detailled comment <br/>
 * 
 */
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.talend.core.runtime.i18n.Messages;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

/**
 * bqian Use xml xsd file to validate the xml file. <br/>
 * 
 */
public class XSDValidater {

    File xsdFile = null;

    /**
     * Sets the xsdFile.
     * 
     * @param xsdFile the xsdFile to set
     */
    public void setXsdFile(File xsdFile) {
        this.xsdFile = xsdFile;
    }

    public void setXsdFile(String xsdFile) {
        File file = new File(xsdFile);
        if (!file.exists()) {
            throw new IllegalArgumentException(Messages.getString("XSDValidater.IllegalArgument")); //$NON-NLS-1$
        }
        this.xsdFile = file;
    }

    /**
     * Validate xml with xsd by dom.
     * 
     * @param xsd the reader of xsd file.
     * @param xml the reader of xml file.
     * @throws Exception
     */
    public void validateWithDom(Reader xsd, Reader xml) throws Exception {
        // Load up the document
        DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
        factory.setNamespaceAware(true);
        // Set up an XML Schema validator, using the supplied schema
        Source schemaSource = new StreamSource(xsd);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaSource);

        // Instead of explicitly validating, assign the Schema to the factory
        factory.setSchema(schema);

        // Parsers from this factory will automatically validate against the
        // associated schema
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(xml));
        schema.newValidator().validate(new DOMSource(doc));
    }

    /**
     * Validate xml with xsd by sax.
     * 
     * @param xsd the reader of xsd file.
     * @param xml the reader of xml file.
     * @throws Exception
     */
    public void validateWithSax(Reader xsd, Reader xml) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = null;
        spf.setNamespaceAware(true);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        spf.setSchema(sf.newSchema(new SAXSource(new InputSource(xsd))));
        parser = spf.newSAXParser();
        DefaultHandler handler = new DefaultHandler();
        parser.parse(new InputSource(xml), handler);
    }

    public void validateWithDom(File xsd, File xml) throws Exception {
        this.validateWithDom(new FileReader(xsd), new FileReader(xml));
    }

    public void validateWithDom(File xml) throws Exception {
        validateWithDom(new FileReader(xml));
    }

    public void validateWithDom(Reader xml) throws Exception {
        if (this.xsdFile == null || !xsdFile.exists()) {
            throw new IllegalArgumentException(Messages.getString("XSDValidater.IllegalArgument")); //$NON-NLS-1$
        }
        validateWithDom(new FileReader(xsdFile), xml);
    }

}
