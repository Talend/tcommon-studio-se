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
package org.talend.commons.runtime.model.emf;

import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.xml.sax.SAXException;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class TalendXMIResource extends XMIResourceImpl {

    public TalendXMIResource() {
        super();
    }

    public TalendXMIResource(URI uri) {
        super(uri);
    }

    /*
     * this is overriden to provide our own implmentation the SAX parser because the JDK 1.6 sax parser is bugged and
     * cannot load XML 1.1 file correctly.
     * 
     * @see org.talend.teneo.model.TalendDatastoreTest#testReadingXMLversionOnePointOneInUTF8()
     * 
     * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl#createXMLLoad()
     */
    @Override
    protected XMLLoad createXMLLoad() {
        return new XMILoadImpl(createXMLHelper()) {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl#makeParser()
             */
            @Override
            protected SAXParser makeParser() throws ParserConfigurationException, SAXException {
                // this is made to avoid the jdk 1.6 SAX parser bug so we instanciate the xercer lib bundled in
                // talend instead of the one by default in the JRE
                return new org.apache.xerces.jaxp.SAXParserFactoryImpl().newSAXParser();
            }
        };
    }

    /*
     * this is overriden to provide our own implmentation of the escape class in order to remove all \0 control
     * characters because they are not authorised in XML files. see TDI-23991
     * 
     * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl#createXMLSave()
     */
    @Override
    protected XMLSave createXMLSave() {
        return new XMISaveImpl(createXMLHelper()) {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.emf.ecore.xmi.impl.XMISaveImpl#init(org.eclipse.emf.ecore.xmi.XMLResource,
             * java.util.Map)
             */
            @Override
            protected void init(XMLResource resource, Map<?, ?> options) {
                super.init(resource, options);
                escape = new Escape() {

                    @Override
                    public String convert(String input) {
                        // just replace all \0 by an emty string
                        String newInput = input.replace("" + '\0', ""); //$NON-NLS-1$ //$NON-NLS-2$
                        return super.convert(newInput);
                    }
                };
                if (escape != null) {// this was copied from the super method to initialise the escape instance.

                    int maxSafeChar = 1114111;
                    if (encoding != null) {
                        if (encoding.equalsIgnoreCase("ASCII") || encoding.equalsIgnoreCase("US-ASCII")) {
                            maxSafeChar = 127;
                        } else if (encoding.equalsIgnoreCase("ISO-8859-1")) {
                            maxSafeChar = 255;
                        }
                    }
                    escape.setMappingLimit(maxSafeChar);

                    if (!"1.0".equals(xmlVersion)) {
                        escape.setAllowControlCharacters(true);
                    }
                    escape.setUseCDATA(Boolean.TRUE.equals(options.get(XMLResource.OPTION_ESCAPE_USING_CDATA)));
                }
            }
        };
    }
}
