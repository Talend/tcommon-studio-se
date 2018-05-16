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
package org.talend.datatools.xml.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSTypeDefinition;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class XSDPopulationUtil {

    public static XSModel getXSModel(String fileName) throws URISyntaxException, MalformedURLException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        URI uri = null;
        File f = new File(fileName);
        if (f.exists()) {
            uri = f.toURI();
        } else {
            URL url = new URL(fileName);
            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    url.getRef());
        }

        // Then try to parse the input string as a url in web.
        if (uri == null) {
            uri = new URI(fileName);
        }

        // fixed a bug when parse one file contians Franch ,maybe need modification
        XMLSchemaLoader xsLoader = new XMLSchemaLoader();
        XSModel xsModel = xsLoader.loadURI(uri.toString());
        if (xsModel == null) {
            try {
                Grammar loadGrammar = xsLoader.loadGrammar(new XMLInputSource(null, uri.toString(), null, new FileInputStream(f),
                        "ISO-8859-1"));
                xsModel = ((XSGrammar) loadGrammar).toXSModel();
            } catch (XNIException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return xsModel;
    }

    public static List<ATreeNode> getAllRootNodes(XSModel xsModel) throws OdaException {
        List<ATreeNode> rootNodes = new ArrayList<ATreeNode>();

        XSNamedMap map = xsModel.getComponents(XSConstants.ELEMENT_DECLARATION);
        for (int i = 0; i < map.getLength(); i++) {
            XSElementDecl element = (XSElementDecl) map.item(i);
            String elementName = element.getName();
            ATreeNode node = new ATreeNode();
            node.setValue(elementName);
            node.setType(ATreeNode.ELEMENT_TYPE);
            node.setDataType(elementName);
            if (element.getTypeDefinition() instanceof XSComplexTypeDecl) {
                XSComplexTypeDecl complexType = (XSComplexTypeDecl) element.getTypeDefinition();
                if (complexType.getName() != null) {
                    node.setDataType(complexType.getName());
                }
            }
            if (!rootNodes.contains(node)) {
                rootNodes.add(node);
            }
        }

        if (map.getLength() == 0) {
            XSNamedMap ctMap = xsModel.getComponents(XSTypeDefinition.COMPLEX_TYPE);
            for (int i = 0; i < ctMap.getLength(); i++) {
                ATreeNode node = new ATreeNode();
                XSComplexTypeDecl element = (XSComplexTypeDecl) ctMap.item(i);
                String elementName = element.getName();
                if (elementName == null || "anyType".equals(elementName)) {
                    continue;
                }
                node.setValue(element.getName());
                node.setType(ATreeNode.ELEMENT_TYPE);
                node.setDataType(element.getTypeName());
                if (!rootNodes.contains(node)) {
                    rootNodes.add(node);
                }
            }
        }

        return rootNodes;
    }

}
