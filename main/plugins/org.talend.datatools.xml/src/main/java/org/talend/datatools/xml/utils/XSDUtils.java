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
package org.talend.datatools.xml.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.utils.IXSDPopulationUtil;

/**
 * 
 * created by wchen on Aug 11, 2016 Detailled comment
 *
 */
public class XSDUtils {

    private static final String SCHEMA_SYSTEM_ID = "custom"; //$NON-NLS-1$

    public static String EXTENSION_WSDL = "wsdl";

    public static final String EXTENSION_XSD = "xsd"; //$NON-NLS-1$

    public static XSDPopulationUtil2 getXsdHander(String filePath) {
        return getXsdHander(new File(filePath));
    }

    public static XSDPopulationUtil2 getXsdHander(File filePath) {
        XSDPopulationUtil2 population = new XSDPopulationUtil2();
        String fileExtension = getFileExtension(filePath);
        if (EXTENSION_WSDL.equalsIgnoreCase(fileExtension)
                && GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
            IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
            IXSDPopulationUtil xsdPopulationUtil = service.getXSDPopulationUtil();
            if (xsdPopulationUtil instanceof XSDPopulationUtil2) {
                population = (XSDPopulationUtil2) xsdPopulationUtil;
                try {
                    population.loadWSDL(filePath.toURI().toString());
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return population;
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return null;
        }
        return name.substring(lastIndexOf + 1, name.length());
    }

    public static boolean isWsdlHandlerRegistred() {
        return GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class);
    }

    public static Map<String, File> getNSToSchemaMapFromWSDL(String tempPath, String wsdlFile) throws Exception {
        Map<String, File> fileToSchemaMap = new HashMap<String, File>();
        XmlSchemaCollection xmlSchemaCollection = new XmlSchemaCollection();
        Definition wsdlDefinition = getDefinition(wsdlFile);
        if (null != wsdlDefinition.getTypes()) {
            init(xmlSchemaCollection, wsdlDefinition.getTypes());
        }
        for (Collection<Import> wsdlImports : (Collection<Collection<Import>>) wsdlDefinition.getImports().values()) {
            for (Import wsdlImport : wsdlImports) {
                if (null != wsdlImport.getDefinition().getTypes()) {
                    init(xmlSchemaCollection, wsdlImport.getDefinition().getTypes());
                }
            }
        }
        XmlSchema[] schemas = xmlSchemaCollection.getXmlSchema(SCHEMA_SYSTEM_ID);
        if (schemas.length > 0) {
            String fileName = new File(wsdlFile).getName();
            int lastIndexOf = fileName.lastIndexOf(".");
            if (lastIndexOf != -1) {
                fileName = fileName.substring(0, lastIndexOf);
            }
            File tempFolder = new File(tempPath, fileName);
            if (tempFolder.exists()) {
                FilesUtils.deleteFile(tempFolder, true);
            }
            tempFolder.mkdirs();
            for (XmlSchema schema : schemas) {
                String ns = schema.getTargetNamespace();
                ns = ns != null ? ns : "";
                if (!fileToSchemaMap.containsKey(ns)) {
                    File file = initFileContent(tempFolder, schema);
                    fileToSchemaMap.put(ns != null ? ns : "", file);
                }
            }
        }
        return fileToSchemaMap;
    }

    private static void init(XmlSchemaCollection xmlSchemaCollection, Types types) {
        Collection<Schema> schemas = XSDUtils.findExtensibilityElements(types.getExtensibilityElements(), Schema.class);
        for (Schema schema : schemas) {
            xmlSchemaCollection.setBaseUri(schema.getDocumentBaseURI());
            xmlSchemaCollection.read(schema.getElement(), SCHEMA_SYSTEM_ID);
        }
    }

    private static <T> Collection<T> findExtensibilityElements(final Collection<?> extensibilityElements, // ExtensibilityElement
            Class<T> clazz) {
        Collection<T> elements = new ArrayList<T>();
        if (extensibilityElements != null) {
            for (Object element : extensibilityElements) {
                if (clazz.isAssignableFrom(element.getClass())) {
                    elements.add(clazz.cast(element));
                }
            }
        }
        return elements;
    }

    private static Definition getDefinition(String pathToWsdl) throws Exception {
        try {
            WSDLFactory wsdlFactory = WSDLFactory.newInstance();
            WSDLReader newWSDLReader = wsdlFactory.newWSDLReader();

            newWSDLReader.setExtensionRegistry(wsdlFactory.newPopulatedExtensionRegistry());
            newWSDLReader.setFeature(com.ibm.wsdl.Constants.FEATURE_VERBOSE, false);
            return newWSDLReader.readWSDL(pathToWsdl);
        } catch (WSDLException e) {
            throw new Exception("Load " + pathToWsdl + " failed!", e);
        }
    }

    private static File initFileContent(File tempFolder, final XmlSchema schema) throws IOException {
        FileOutputStream outStream = null;
        try {
            File createTempFile = File.createTempFile("tempXSDFile", ".xsd");
            createTempFile.delete();
            File temfile = new File(tempFolder, createTempFile.getName());
            outStream = new FileOutputStream(temfile);
            schema.write(outStream); // this method hangs when using invalid wsdl.
            return temfile;
        } finally {
            if (outStream != null) {
                outStream.close();
            }
        }
    }
}
