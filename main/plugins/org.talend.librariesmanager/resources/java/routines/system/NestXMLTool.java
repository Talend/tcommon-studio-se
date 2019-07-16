// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.util.List;

/**
 * created by talend2 on 2013-10-17 Detailled comment
 *
 */
public class NestXMLTool {

    public static void parseAndAdd(org.dom4j.Element nestRoot, String value) {
        try {
            org.dom4j.Document doc4Str = org.dom4j.DocumentHelper.parseText("<root>" + value + "</root>");
            nestRoot.setContent(doc4Str.getRootElement().content());
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            nestRoot.setText(value);
        }
    }

    public static void setText(org.dom4j.Element element, String value) {
        if (value.startsWith("<![CDATA[") && value.endsWith("]]>")) {
            String text = value.substring(9, value.length() - 3);
            element.addCDATA(text);
        } else {
            element.setText(value);
        }
    }

    public static void replaceDefaultNameSpace(org.dom4j.Element nestRoot, org.dom4j.Element declaredDefaultNamespaceElement) {
        if (nestRoot != null) {
            List<org.dom4j.Namespace> declaredNamespaces = nestRoot.declaredNamespaces();
            for (org.dom4j.Namespace namespace : declaredNamespaces) {
                if ("".equals(namespace.getPrefix()) && !"".equals(namespace.getURI())) {// current element declare a
                                                                                         // default namespace
                    declaredDefaultNamespaceElement = nestRoot;
                    break;
                }
            }

            for (org.dom4j.Element tmp : (java.util.List<org.dom4j.Element>) nestRoot.elements()) {
                if (declaredDefaultNamespaceElement != null
                        && (tmp.getQName().getNamespace() == org.dom4j.Namespace.NO_NAMESPACE)) {
                    tmp.setQName(org.dom4j.DocumentHelper.createQName(tmp.getName(), declaredDefaultNamespaceElement.getQName()
                            .getNamespace()));
                }
                replaceDefaultNameSpace(tmp, declaredDefaultNamespaceElement);
            }
        }
    }

    public static void removeEmptyElement(org.dom4j.Element root) {
        if (root != null) {
            for (org.dom4j.Element tmp : (java.util.List<org.dom4j.Element>) root.elements()) {
                removeEmptyElement(tmp);
            }
            if (root.content().size() == 0 && root.attributes().size() == 0 && root.declaredNamespaces().size() == 0) {
                if (root.getParent() != null) {
                    root.getParent().remove(root);
                }
            }
        }
    }

    //do some work after document has been generated
    public static void generateOk(routines.system.Document doc,boolean removeEmptyElement) {
        if(doc == null || doc.getDocument() == null) {
            return;
        }

        replaceDefaultNameSpace(doc.getDocument().getRootElement(),null);

        if(removeEmptyElement) {
            removeEmptyElement(doc.getDocument().getRootElement());
        }
    }

}
