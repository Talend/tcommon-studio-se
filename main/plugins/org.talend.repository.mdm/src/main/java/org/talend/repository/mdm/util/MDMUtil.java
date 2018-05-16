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
package org.talend.repository.mdm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.metadata.managment.mdm.AbsMdmConnectionHelper;
import org.talend.metadata.managment.mdm.S60MdmConnectionHelper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MDMUtil {

    public static List<String> getConcepts(XSDSchema schema) {
        EList xsdElementDeclarations = schema.getElementDeclarations();
        List<String> list = new ArrayList<String>();
        for (XSDElementDeclaration el : (XSDElementDeclaration[]) xsdElementDeclarations
                .toArray(new XSDElementDeclaration[xsdElementDeclarations.size()])) {
            if (!el.getIdentityConstraintDefinitions().isEmpty()) {
                list.add(el.getName());
            }
        }
        return list;
    }

    /**
     * Returns and XSDSchema Object from an xsd
     * 
     * @param schema
     * @return
     * @throws Exception
     */
    public static XSDSchema getXSDSchema(String schema) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);
        InputSource source = new InputSource(new StringReader(schema));
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(source);
        XSDSchema xsdSchema = null;

        xsdSchema = XSDSchemaImpl.createSchema(document.getDocumentElement());

        return xsdSchema;
    }

    public static Concept getConcept(MDMConnection connection, MetadataTable table) {
        if (table == null || connection == null) {
            return null;
        }
        for (Object obj : connection.getSchemas()) {
            if (obj instanceof Concept) {
                Concept concept = (Concept) obj;
                if (concept.getLabel() != null && concept.getLabel().equals(table.getLabel())) {
                    return concept;
                }

            }
        }

        return null;

    }

    public static File getTempTemplateXSDFile() {
        IPath tempPath = new Path(System.getProperty("user.dir")).append("temp"); //$NON-NLS-1$ //$NON-NLS-2$
        File tempFile = tempPath.toFile();
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        File file = new File(tempFile, "template.xsd"); //$NON-NLS-1$
        return file;
    }

    public static void initConcepts(MDMConnection mdmConn) throws Exception {
        AbsMdmConnectionHelper connectionHelper = null;
        connectionHelper = new S60MdmConnectionHelper();
        connectionHelper.initConcept(mdmConn, getTempTemplateXSDFile());
        // IPath temp = new Path(System.getProperty("user.dir")).append("temp");
        // xsdFilePath = temp.toOSString() + "\\template.xsd";
        // XtentisBindingStub stub = null;
        // String userName = mdmConn.getUsername();
        // String password = mdmConn.getValue(mdmConn.getPassword(), false);
        // String server = mdmConn.getServer();
        // String port = mdmConn.getPort();
        // String universe = mdmConn.getUniverse();
        // String datamodel = mdmConn.getDatamodel();
        // WSUniversePK[] universes = null;
        // WSUniversePK universePK = null;
        // WSDataModelPK modelPK = null;
        // XtentisServiceLocator xtentisService = new XtentisServiceLocator();
        //        xtentisService.setXtentisPortEndpointAddress("http://" + server + ":" + port + "/talend/TalendPort"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        // XtentisPort_PortType xtentisWS = xtentisService.getXtentisPort();
        // stub = (XtentisBindingStub) xtentisWS;
        // stub.setUsername(userName);
        // stub.setPassword(password);
        // stub.ping(new WSPing());
        // try {
        //            universes = stub.getUniversePKs(new WSGetUniversePKs("")); //$NON-NLS-1$
        // } catch (Exception e) {
        // universes = null;
        // }
        // if (universes != null) {
        // for (WSUniversePK universe2 : universes) {
        // if (universe2.getPk().equals(universe)) {
        // universePK = universe2;
        // break;
        // }
        // }
        // }
        //        //        if (universePK != null && universe != null && !"".equals(universe)) { //$NON-NLS-1$
        //        if (universe != null && !"".equals(universe)) { //$NON-NLS-1$
        //            stub.setUsername(universe + "/" + userName); //$NON-NLS-1$
        // stub.setPassword(password);
        // } else {
        // stub.setUsername(userName);
        // stub.setPassword(password);
        // }
        //        WSDataModelPK[] models = stub.getDataModelPKs(new WSRegexDataModelPKs(""));//$NON-NLS-1$
        // if (models == null) {
        // return;
        // }
        // for (WSDataModelPK model : models) {
        // if (model.getPk().equals(datamodel)) {
        // modelPK = model;
        // break;
        // }
        // }
        // if (modelPK == null) {
        // return;
        // }
        //
        // WSDataModel model = stub.getDataModel(new WSGetDataModel(modelPK));
        // if (model == null) {
        // return;
        // }
        // writeInFile(model.getXsdSchema());
        // List<String> list = MDMUtil.getConcepts(MDMUtil.getXSDSchema(model.getXsdSchema()));
        // concepts.addAll(list);
        // return concepts;
    }

    private static void writeInFile(String schema) {
        File file = getTempTemplateXSDFile();
        StringReader reader = new StringReader(schema);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
            char[] c = new char[1024];
            int l = 0;
            while ((l = reader.read(c)) != -1) {
                writer.write(c, 0, l);
            }
            writer.flush();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
