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
package org.talend.commons.ui.html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.ui.internal.intro.impl.model.util.BundleUtil;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TalendHtmlModelUtil {

    private static String TAG_BODY = "body"; //$NON-NLS-1$

    private static String TAG_HEAD = "head"; //$NON-NLS-1$

    private static String TAG_BASE = "base"; //$NON-NLS-1$

    public static String TAG_DIV = "div"; //$NON-NLS-1$

    public static String TAG_HEAD_LINK = "link"; //$NON-NLS-1$

    public static String TAG_OPTION = "option"; //$NON-NLS-1$

    public static String TAG_IMG = "img"; //$NON-NLS-1$    

    private static String TAG_PARAM = "param"; //$NON-NLS-1$

    private static String ATT_SRC = "src"; //$NON-NLS-1$

    private static String ATT_HREF = "href"; //$NON-NLS-1$

    private static String ATT_CITE = "cite"; //$NON-NLS-1$

    private static String ATT_LONGDESC = "longdesc"; //$NON-NLS-1$

    private static String ATT_DATA = "data"; //$NON-NLS-1$

    private static String ATT_CODEBASE = "codebase"; //$NON-NLS-1$

    private static String ATT_VALUE = "value"; //$NON-NLS-1$

    private static String ATT_VALUE_TYPE = "valuetype"; //$NON-NLS-1$

    private static String ATT_REL = "rel"; //$NON-NLS-1$

    private static String ATT_TYPE = "type"; //$NON-NLS-1$

    public static void updateResourceAttributes(Element element, String base, Bundle bundle) {
        // doUpdateResourceAttributes(element, base, bundle);
        NodeList children = element.getElementsByTagName("*"); //$NON-NLS-1$
        for (int i = 0; i < children.getLength(); i++) {
            Element child = (Element) children.item(i);
            doUpdateResourceAttributes(child, base, bundle);
        }
    }

    private static void doUpdateResourceAttributes(Element element, String base, Bundle bundle) {
        qualifyAttribute(element, ATT_SRC, base, bundle);
        qualifyAttribute(element, ATT_HREF, base, bundle);
        qualifyAttribute(element, ATT_CITE, base, bundle);
        qualifyAttribute(element, ATT_LONGDESC, base, bundle);
        qualifyAttribute(element, ATT_CODEBASE, base, bundle);
        qualifyAttribute(element, ATT_DATA, base, bundle);
        qualifyValueAttribute(element, base, bundle);
    }

    private static void qualifyAttribute(Element element, String attributeName, String base, Bundle bundle) {
        if (element.hasAttribute(attributeName)) {
            String attributeValue = element.getAttribute(attributeName);
            // if (new IntroURLParser(attributeValue).hasProtocol())
            if (hasProtocol(attributeValue)) {
                return;
            }

            // resolve the resource against the nl mechanism.
            String attributePath = BundleUtil.getResolvedResourceLocation(base, attributeValue, bundle);
            element.setAttribute(attributeName, attributePath);
        }
    }

    public static boolean hasProtocol(String url) {
        boolean hasProtocol = false;
        if (url == null) {
            return hasProtocol;
        }
        URL url_inst = null;
        try {
            url_inst = new URL(url);
        } catch (MalformedURLException e) {
            // not a valid URL. set state.
            return hasProtocol;
        }

        if (url_inst.getProtocol() != null) {
            // URL has some valid protocol. Check to see if it is an intro url.
            hasProtocol = true;
        }
        return hasProtocol;
    }

    private static void qualifyValueAttribute(Element element, String base, Bundle bundle) {
        if (element.hasAttribute(ATT_VALUE) && element.hasAttribute(ATT_VALUE_TYPE)
                && element.getAttribute(ATT_VALUE_TYPE).equals("ref") //$NON-NLS-1$
                && element.getLocalName().equals(TAG_PARAM)) {
            String value = element.getAttribute(ATT_VALUE);
            // if (new IntroURLParser(value).hasProtocol())
            if (hasProtocol(value)) {
                return;
            }
            // resolve the resource against the nl mechanism.
            String attributePath = BundleUtil.getResolvedResourceLocation(base, value, bundle);
            element.setAttribute(ATT_VALUE, attributePath);
        }
    }

    /**
     * Returns an array version of the passed NodeList. Used to work around DOM design issues.
     */
    public static Node[] getArray(NodeList nodeList) {
        Node[] nodes = new Node[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes[i] = nodeList.item(i);
        }
        return nodes;
    }

    public static Element createElement(Document dom, String elementName, Properties attributes) {

        // make sure to create element with any namespace uri to enable finding
        // it again using Dom.getElementsByTagNameNS()
        Element element = dom.createElementNS("", elementName); //$NON-NLS-1$
        if (attributes != null) {
            Enumeration e = attributes.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                element.setAttribute(key, attributes.getProperty(key));
            }
        }
        return element;
    }

    /*
     * Util method similar to DOM getElementById() method, but it works without an id attribute being specified. Deep
     * searches all children in this container's DOM for the first child with the given id. The element retrieved must
     * have the passed local name. Note that in an XHTML file (aka DOM) elements should have a unique id within the
     * scope of a document. We use local name because this allows for finding intro anchors, includes and dynamic
     * content element regardless of whether or not an xmlns was used in the xml.
     */
    public static Element getElementById(Document dom, String id, String localElementName) {

        NodeList children = dom.getElementsByTagNameNS("*", localElementName); //$NON-NLS-1$
        for (int i = 0; i < children.getLength(); i++) {
            Element element = (Element) children.item(i);
            if (element.getAttribute("id").equals(id)) {
                return element;
            }
        }
        // non found.
        return null;

    }
}
