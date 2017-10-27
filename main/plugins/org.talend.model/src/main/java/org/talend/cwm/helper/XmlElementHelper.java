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
package org.talend.cwm.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.talend.cwm.xml.TdXmlContent;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.TdXmlSchema;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class XmlElementHelper {

    public static final String SLASH = "/";//$NON-NLS-1$

    public static final String DOUBLE_SLASH = "//";//$NON-NLS-1$

    public static final String emptyString = "";//$NON-NLS-1$

    private XmlElementHelper() {
    }

    /**
     * DOC xqliu Comment method "isLeafNode".
     * 
     * @param element
     * @return
     */
    public static boolean isLeafNode(TdXmlElementType element) {
        boolean leafNode = false;
        XSDElementDeclaration xsdElementDeclearation = (XSDElementDeclaration) element.getXsdElementDeclaration();
        if (xsdElementDeclearation != null) {
            XSDTypeDefinition xsdTypeDef = xsdElementDeclearation.getTypeDefinition();
            if (xsdTypeDef instanceof XSDSimpleTypeDefinition) {
                leafNode = true;
            }
        }
        return leafNode;
    }

    /**
     * DOC xqliu Comment method "isFromSameTable".
     * 
     * @param xmlElements
     * @return
     */
    public static boolean isFromSameTable(List<TdXmlElementType> xmlElements) {
        assert xmlElements != null;

        Set<String> modelElementNames = new HashSet<String>();
        for (TdXmlElementType xmlElement : xmlElements) {
            ModelElement parentElement = getParentElement(xmlElement);
            String fullName = "";
            if (parentElement instanceof TdXmlSchema) {
                fullName = DOUBLE_SLASH + parentElement.getName();
            } else if (parentElement instanceof TdXmlElementType) {
                fullName = getFullName((TdXmlElementType) parentElement);
            }
            modelElementNames.add(fullName);
        }
        return modelElementNames.size() == 1;
    }

    /**
     * DOC xqliu Comment method "getFullName".
     * 
     * @param xmlElement
     * @return
     */
    public static String getFullName(TdXmlElementType xmlElement) {
        ModelElement parentElement = getParentElement(xmlElement);
        if (parentElement != null) {
            return DOUBLE_SLASH + parentElement.getName() + SLASH + xmlElement.getName();
        }
        return DOUBLE_SLASH + xmlElement.getName();
    }

    /**
     * DOC xqliu Comment method "getParentElement".
     * 
     * @param xmlElement
     * @return
     */
    public static ModelElement getParentElement(TdXmlElementType xmlElement) {
        EObject temp = xmlElement.eContainer();
        if (temp instanceof TdXmlContent) {
            EObject eContainer = temp.eContainer();
            if (eContainer instanceof TdXmlElementType || eContainer instanceof TdXmlSchema) {
                return (ModelElement) eContainer;
            }
        } else if (temp instanceof TdXmlElementType || temp instanceof TdXmlSchema) {
            return (ModelElement) temp;
        }
        return null;
    }

    /**
     * DOC xqliu Comment method "getParentElementName".
     * 
     * @param xmlElement
     * @return
     */
    public static String getParentElementName(TdXmlElementType xmlElement) {
        ModelElement parentElement = getParentElement(xmlElement);
        return parentElement == null ? "" : parentElement.getName();
    }

    /**
     * DOC xqliu Comment method "clearLeafNode".
     * 
     * @param modelElements
     * @return
     */
    public static List<ModelElement> clearLeafNode(List<? extends ModelElement> modelElements) {
        List<ModelElement> result = new ArrayList<ModelElement>();
        for (ModelElement me : modelElements) {
            if (me instanceof TdXmlElementType) {
                if (!isLeafNode((TdXmlElementType) me)) {
                    result.add(me);
                }
            } else {
                result.add(me);
            }
        }
        return result;
    }

    /**
     * DOC xqliu Comment method "getLeafNode".
     * 
     * @param modelElements
     * @return
     */
    public static List<TdXmlElementType> getLeafNode(List<? extends ModelElement> modelElements) {
        List<TdXmlElementType> result = new ArrayList<TdXmlElementType>();
        for (ModelElement me : modelElements) {
            if (me instanceof TdXmlElementType && isLeafNode((TdXmlElementType) me)) {
                result.add((TdXmlElementType) me);
            }
        }
        return result;
    }

    /**
     * 
     * DOC qiongli Comment method "getFullPath".
     * 
     * @param xmlElement
     * @param path
     * @return
     */
    public static String getFullPath(TdXmlElementType xmlElement, String path) {
        if (path == null) {
            path = emptyString;
        }
        if (xmlElement == null) {
            return path;
        }

        ModelElement parentElement = XmlElementHelper.getParentElement(xmlElement);
        TdXmlSchema xmlSchema = SwitchHelpers.XMLSCHEMA_SWITCH.doSwitch(parentElement);
        if (xmlSchema != null) {
            return path;
        } else {
            TdXmlElementType parentXmlElement = SwitchHelpers.XMLELEMENTTYPE_SWITCH.doSwitch(parentElement);
            if (parentXmlElement != null) {
                if (path.equals(emptyString)) {
                    path = parentElement.getName();
                } else {
                    path = parentElement.getName() + SLASH + path;
                }
                return getFullPath(parentXmlElement, path);
            }
        }
        return path;
    }
}
