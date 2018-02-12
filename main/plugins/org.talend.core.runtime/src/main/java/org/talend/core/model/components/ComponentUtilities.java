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
package org.talend.core.model.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.runtime.model.components.IComponentConstants;
import org.talend.core.model.process.UniqueNodeNameGenerator;
import org.talend.designer.core.model.utils.emf.talendfile.ColumnType;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ComponentUtilities {

    public static final String FAMILY_SEPARATOR_REGEX = "\\|"; //$NON-NLS-1$

    private static final String CASE_SENSITIVE = "CASE_SENSITIVE"; //$NON-NLS-1$

    private static final String KEY_ATTRIBUTE = "KEY_ATTRIBUTE"; //$NON-NLS-1$

    private static final String SCHEMA_COLUMN = "SCHEMA_COLUMN"; //$NON-NLS-1$

    public static String getNodePropertyValue(NodeType node, String property) {
        ElementParameterType prop = getNodeProperty(node, property);
        if (prop == null) {
            return null;
        } else {
            return prop.getValue();
        }
    }

    public static ElementParameterType getNodeProperty(NodeType node, String property) {
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            if (t.getName().equals(property)) {
                return t;
            }
        }
        return null;
    }

    public static void addNodeProperty(NodeType node, String name, String field) {
        ElementParameterType property = TalendFileFactory.eINSTANCE.createElementParameterType();
        property.setName(name);
        property.setField(field);
        node.getElementParameter().add(property);
    }

    public static void setNodeProperty(NodeType node, String name, List<ElementValueType> values) {
        ElementParameterType property = getNodeProperty(node, name);

        for (ElementValueType value : values) {
            property.getElementValue().add(value);
        }
    }

    public static void setNodeValue(NodeType node, String name, String value) {
        ElementParameterType property = getNodeProperty(node, name);
        property.setValue(value);
    }

    public static void removeNodeProperty(NodeType node, String property) {
        // TODO SML Use getNodeProperty
        EList elementParameter = node.getElementParameter();
        Iterator iterator = elementParameter.iterator();
        for (Object o = iterator.next(); iterator.hasNext(); o = iterator.next()) {
            ElementParameterType t = (ElementParameterType) o;
            if (t.getName().equals(property)) {
                iterator.remove();
            }
        }
    }

    public static String getNodeUniqueName(NodeType node) {
        return ComponentUtilities.getNodePropertyValue(node, IComponentConstants.UNIQUE_NAME);
    }

    public static void setNodeUniqueName(NodeType node, String newName) {
        ElementParameterType t = getNodeProperty(node, IComponentConstants.UNIQUE_NAME);
        t.setValue(newName);
    }

    public static void replaceInNodeParameterValue(NodeType node, String oldName, String newName) {
        String oldName2 = "\\b" + oldName + "\\b"; //$NON-NLS-1$ //$NON-NLS-2$
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            String value = t.getValue();
            if (value != null) {
                String replaceAll = value.replaceAll(oldName2, newName);
                t.setValue(replaceAll);
            }
        }
    }

    public static String generateUniqueNodeName(String baseName, ProcessType process) {
        List<String> uniqueNodeNameList = new ArrayList<String>();
        for (Object o : process.getNode()) {
            NodeType nt = (NodeType) o;
            uniqueNodeNameList.add(getNodeUniqueName(nt));
        }
        return UniqueNodeNameGenerator.generateUniqueNodeName(baseName, uniqueNodeNameList);
    }

    public static List<ElementValueType> createtUniqRowV2UniqueKey(NodeType node) {
        // TODO SML Move in a more specific class
        List<ElementValueType> values = new ArrayList<ElementValueType>();
        TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;

        MetadataType object = (MetadataType) node.getMetadata().get(0);

        for (Object o : object.getColumn()) {
            ColumnType tagada = (ColumnType) o;
            ElementValueType elementValue = fileFact.createElementValueType();
            elementValue.setElementRef(SCHEMA_COLUMN);
            elementValue.setValue(tagada.getName());
            values.add(elementValue);

            ElementValueType elementValue2 = fileFact.createElementValueType();
            elementValue2.setElementRef(KEY_ATTRIBUTE);
            elementValue2.setValue(new Boolean(tagada.isKey()).toString());
            values.add(elementValue2);

            ElementValueType elementValue3 = fileFact.createElementValueType();
            elementValue3.setElementRef(CASE_SENSITIVE);
            elementValue3.setValue("false"); //$NON-NLS-1$
            values.add(elementValue3);
        }
        return values;
    }

    public static NodeType getNodeTypeFromUniqueName(ProcessType processType, String uniqueName) {
        for (Object oNodeType : processType.getNode()) {
            NodeType nodeType = (NodeType) oNodeType;
            if (getNodeUniqueName(nodeType).equals(uniqueName)) {
                return nodeType;
            }
        }
        return null;
    }

    private static ProcessType getNodeProcessType(NodeType node) {
        return (ProcessType) node.eContainer();
    }

    public static ContextType getNodeCurrentContextType(NodeType node) {
        ProcessType processType = getNodeProcessType(node);
        String defaultContext = processType.getDefaultContext();
        EList context = processType.getContext();

        for (Object object : context) {
            ContextType contextType = (ContextType) object;
            if (defaultContext.endsWith(contextType.getName())) {
                return contextType;
            }
        }
        return null;
    }

    public static String getExtFolder(String folder) {
        String path = folder;
        // bug fix : several headless instance should not use the same folder
        if (CommonsPlugin.isStoreLibsInWorkspace()) {
            String workspaceName = ResourcesPlugin.getWorkspace().getRoot().getLocation().lastSegment();
            path = folder + "-" + workspaceName; //$NON-NLS-1$
        }
        return path;
    }

    public static Collection<IComponent> filterVisibleComponents(Collection<IComponent> components) {
        if (components == null || components.isEmpty()) {
            return components;
        }

        Iterator<IComponent> iter = components.iterator();
        while (iter.hasNext()) {
            IComponent component = iter.next();
            if (component == null || !isComponentVisible(component)) {
                iter.remove();
            }
        }

        return components;
    }

    public static boolean isComponentVisible(IComponent component) {
        String family = component.getTranslatedFamilyName();
        String oraFamily = component.getOriginalFamilyName();
        String[] strings = family.split(FAMILY_SEPARATOR_REGEX);
        String[] oraStrings = oraFamily.split(FAMILY_SEPARATOR_REGEX);
        boolean isVisible = false;
        for (int j = 0; j < strings.length; j++) {
            if (component.isVisible(oraStrings[j])) {
                isVisible = true;
                break;
            }
        }
        return isVisible;
    }

    @SuppressWarnings("unchecked")
    public static List<ConnectionType> getNodeOutputConnections(NodeType node) {
        return getNodeConnections(node, ConnectorType.OUTPUT);
    }

    @SuppressWarnings("unchecked")
    public static List<ConnectionType> getNodeInputConnections(NodeType node) {
        return getNodeConnections(node, ConnectorType.INPUT);
    }

    @SuppressWarnings("unchecked")
    private static List<ConnectionType> getNodeConnections(NodeType node, ConnectorType connectorType) {
        ProcessType processType = getNodeProcessType(node);
        String nodeUniqueName = getNodeUniqueName(node);
        List<ConnectionType> connList = new ArrayList<ConnectionType>();
        switch (connectorType) {
        case INPUT: {
            for (ConnectionType conn : (List<ConnectionType>) processType.getConnection()) {
                if (conn.getTarget().equals(nodeUniqueName)) {
                    connList.add(conn);
                }
            }
            break;
        }
        case OUTPUT: {
            for (ConnectionType conn : (List<ConnectionType>) processType.getConnection()) {
                if (conn.getSource().equals(nodeUniqueName)) {
                    connList.add(conn);
                }
            }
        }
        }
        return connList;
    }

    private enum ConnectorType {
        INPUT,
        OUTPUT
    }

}
