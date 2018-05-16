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
package org.talend.core.repository.model.dnd;

import java.util.Iterator;
import java.util.List;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.DefaultRepositoryComponentDndFilter;
import org.talend.repository.model.RepositoryNode;

/**
 * created by cmeng on Apr 21, 2015 Detailled comment
 *
 */
public class SalesforceComponentDndFilter extends DefaultRepositoryComponentDndFilter {

    public static final String COMPONENT_T_SALSEFORCE_CONNECTION = "tSalesforceConnection"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_WAVE_BULK_EXEC = "tSalesforceWaveBulkExec"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_WAVE_OUTPUT_BULK_EXEC = "tSalesforceWaveOutputBulkExec"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_INPUT = "tSalesforceInput"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_OUTPUT = "tSalesforceOutput"; //$NON-NLS-1$

    @Override
    public boolean except(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        if (!(item instanceof SalesforceSchemaConnectionItem)) {
            return false;
        }
        if (ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA == type) {
            String componentName = component.getName();
            if (COMPONENT_T_SALSEFORCE_INPUT.equals(componentName) || COMPONENT_T_SALSEFORCE_OUTPUT.equals(componentName)) {
                return true;
            }
        } else if (ERepositoryObjectType.METADATA_SALESFORCE_MODULE == type || ERepositoryObjectType.METADATA_CON_TABLE == type
                || ERepositoryObjectType.METADATA_CON_COLUMN == type) {
            String componentName = component.getName();
            if (COMPONENT_T_SALSEFORCE_CONNECTION.equals(componentName)
                    || COMPONENT_T_SALSEFORCE_WAVE_BULK_EXEC.equals(componentName)
                    || COMPONENT_T_SALSEFORCE_WAVE_OUTPUT_BULK_EXEC.equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean valid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        if (!(item instanceof SalesforceSchemaConnectionItem)) {
            return false;
        }
        if (ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA == type) {
            String componentName = component.getName();
            if (COMPONENT_T_SALSEFORCE_CONNECTION.equals(componentName)
                    || COMPONENT_T_SALSEFORCE_WAVE_BULK_EXEC.equals(componentName)
                    || COMPONENT_T_SALSEFORCE_WAVE_OUTPUT_BULK_EXEC.equals(componentName)) {
                return true;
            }
        } else if (ERepositoryObjectType.METADATA_SALESFORCE_MODULE == type || ERepositoryObjectType.METADATA_CON_TABLE == type
                || ERepositoryObjectType.METADATA_CON_COLUMN == type) {
            String componentName = component.getName();
            if (COMPONENT_T_SALSEFORCE_INPUT.equals(componentName) || COMPONENT_T_SALSEFORCE_OUTPUT.equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<IComponent> sort(List<IComponent> components, Item item, ERepositoryObjectType type, RepositoryNode seletetedNode) {
        if (item instanceof SalesforceSchemaConnectionItem) {
            if (ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA == type && components != null) {
                Iterator<IComponent> iter = components.iterator();
                while (iter.hasNext()) {
                    IComponent component = iter.next();
                    if (COMPONENT_T_SALSEFORCE_CONNECTION.equals(component.getName())) {
                        iter.remove();
                        components.add(0, component);
                        return components;
                    }
                }
            }

        }
        return components;
    }

}
