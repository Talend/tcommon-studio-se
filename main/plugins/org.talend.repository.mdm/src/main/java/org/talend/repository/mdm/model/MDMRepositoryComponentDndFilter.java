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
package org.talend.repository.mdm.model;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.DefaultRepositoryComponentDndFilter;
import org.talend.core.repository.RepositoryComponentManager;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MDMRepositoryComponentDndFilter extends DefaultRepositoryComponentDndFilter {

    private static final String RECEIVE = "Receive"; //$NON-NLS-1$

    public MDMRepositoryComponentDndFilter() {
    }

    @Override
    public boolean valid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        if (!(item instanceof MDMConnectionItem)) {
            return false;
        }
        // for mdm
        MdmConceptType mdmType = null;
        if (item instanceof MDMConnectionItem) {
            MDMConnectionItem mdmItem = (MDMConnectionItem) item;
            if (seletetedNode != null && seletetedNode.getObject() instanceof MetadataTableRepositoryObject) {
                MetadataTableRepositoryObject object = (MetadataTableRepositoryObject) seletetedNode.getObject();
                if (mdmItem.getConnection() instanceof MDMConnection) {
                    MDMConnection connection = (MDMConnection) mdmItem.getConnection();
                    for (Object obj : connection.getSchemas()) {
                        if (obj instanceof Concept && object.getLabel().equals(((Concept) obj).getLabel())) {
                            mdmType = ((Concept) obj).getConceptType();
                        }

                    }
                }
            }
        }

        String componentProductname = component.getRepositoryType();
        if (componentProductname != null && repositoryType.endsWith(componentProductname)
                && validSub(item, type, seletetedNode, component, repositoryType)) {
            if (MdmConceptType.INPUT.equals(mdmType) && component.getName().endsWith(RepositoryComponentManager.INPUT)) {
                return true;
            } else if (MdmConceptType.OUTPUT.equals(mdmType) && component.getName().endsWith(RepositoryComponentManager.OUTPUT)) {
                return true;
            } else if (MdmConceptType.RECEIVE.equals(mdmType) && component.getName().endsWith(RECEIVE)) {
                return true;
            }
        }

        return false;
    }

}
