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
package swt;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author cantoine
 * 
 */
public class ExpressionXPath {

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory fabriqueDOM = DocumentBuilderFactory.newInstance();
            DocumentBuilder analyseur = fabriqueDOM.newDocumentBuilder();
            Document document = analyseur.parse(new File("C:\\test.xml")); //$NON-NLS-1$
            XPathFactory fabriqueXPath = XPathFactory.newInstance();
            XPath xpath = fabriqueXPath.newXPath();
            String expression = "//bills/bill/line[@id='38']"; //$NON-NLS-1$

            // line[@id='38']

            Node noeud = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
            afficherInfos(noeud);
            // expression = "ancestor::*/logiciel[3]/commentaire";
            // noeud = (Node) xpath.evaluate(
            // expression, noeud, XPathConstants.NODE);
            // afficherInfos(noeud);

            // XMLParserLiaison xpathSupport = new XMLParserLiaisonDefault();
            // XPathProcessor xpathParser = new XPathProcessorImpl(xpathSupport);
            // PrefixResolver prefixResolver = new PrefixResolverDefault(source.getDocumentElement());
            //
            // // create the XPath and initialize it
            // XPath xp = new XPath();
            // String xpString = "//address[child::addressee[text() = '"+name+"']]";
            // xpathParser.initXPath(xp, xpString, prefixResolver);
            //
            // // now execute the XPath select statement
            // XObject list = xp.execute(xpathSupport, source.getDocumentElement(), prefixResolver);

            // return the resulting node
            // return list.nodeset().item(0);

        } catch (IOException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (ParserConfigurationException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (SAXException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (XPathExpressionException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
    }

    public static void afficherInfos(Node noeud) {
        String nom = noeud.getNodeName();
        String valeur = noeud.getNodeValue();
        short type = noeud.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            valeur = noeud.getTextContent();
        }
        if (type == Node.ATTRIBUTE_NODE) {
            valeur = noeud.getTextContent();
        }

        System.out.println(nom + " (" + type + ") = '" + valeur + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
