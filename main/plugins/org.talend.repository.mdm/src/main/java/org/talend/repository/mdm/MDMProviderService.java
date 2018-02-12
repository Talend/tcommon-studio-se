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
package org.talend.repository.mdm;

import java.util.Map;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.IMDMConstant;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.ui.IMDMProviderService;
import org.talend.repository.UpdateRepositoryUtils;
import org.talend.repository.mdm.ui.wizard.MDMWizard;
import org.talend.repository.mdm.ui.wizard.concept.XPathPrefix;
import org.talend.repository.mdm.util.MDMUtil;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class MDMProviderService implements IMDMProviderService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.IMDMProviderService#getRepositoryItem(org.talend.core.model.process.INode)
     */
    public MDMConnectionItem getRepositoryItem(INode node) {
        if (node != null) {
            if (isMDMNode(node)) {
                IElementParameter param = node.getElementParameter(IMDMConstant.PROPERTY);
                IElementParameter typeParam = param.getChildParameters().get(IMDMConstant.PROPERTY_TYPE);
                if (typeParam != null && IMDMConstant.REF_ATTR_REPOSITORY.equals(typeParam.getValue())) {
                    IElementParameter repositoryParam = param.getChildParameters().get(IMDMConstant.REPOSITORY_PROPERTY_TYPE);
                    final String value = (String) repositoryParam.getValue();

                    Item item = UpdateRepositoryUtils.getConnectionItemByItemId(value);
                    if (item != null && item instanceof MDMConnectionItem) {
                        return (MDMConnectionItem) item;
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.IMDMProviderService#isMDMNode(org.talend.core.model.process.INode)
     */
    public boolean isMDMNode(INode node) {
        if (node != null) {
            IElementParameter param = node.getElementParameter(IMDMConstant.PROPERTY);
            if (param != null && param.getFieldType() == EParameterFieldType.PROPERTY_TYPE
                    && IMDMConstant.REPOSITORY_VALUE.equals(param.getRepositoryValue())) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.IMDMProviderService#isRepositorySchemaLine(org.talend.core.model.process.INode,
     * java.util.Map)
     */
    public boolean isRepositorySchemaLine(INode node, Map<String, Object> lineValue) {
        if (lineValue != null && node != null) {
            Object type = lineValue.get(IMDMConstant.FIELD_SCHEMA + IMDMConstant.REF_TYPE);
            if (type != null && IMDMConstant.REF_ATTR_REPOSITORY.equals(type)) {
                String value = (String) lineValue.get(IMDMConstant.FIELD_SCHEMA);
                if (value != null && !"".equals(value)) { //$NON-NLS-1$
                    if (MetadataToolHelper.getMetadataTableFromNodeLabel(node, value) != null) {
                        return true;

                    }
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.IMDMProviderService#newMDMWizard(org.eclipse.ui.IWorkbench, boolean,
     * org.talend.repository.model.RepositoryNode, java.lang.String[])
     */
    public IWizard newWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        if (node == null) {
            return null;
        }
        if (workbench == null) {
            workbench = PlatformUI.getWorkbench();
        }
        return new MDMWizard(workbench, creation, node, existingNames);
    }

    public boolean initConcepts(MDMConnection mdmConn) {
        try {
            MDMUtil.initConcepts(mdmConn);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getXPathPrefixValue(Concept concept) {
        if (concept != null) {
            String xPathPrefix = concept.getXPathPrefix();
            if (xPathPrefix != null) {
                XPathPrefix[] prefixs = XPathPrefix.values();
                for (int i = 0; i < prefixs.length; i++) {
                    if (prefixs[i].name().equals(xPathPrefix)) {
                        return prefixs[i].getDisplayName();
                    }
                }
                // if it's not fixed value ,return xPathPrefix directly
                return xPathPrefix;
            }

        }
        return null;
    }

}
