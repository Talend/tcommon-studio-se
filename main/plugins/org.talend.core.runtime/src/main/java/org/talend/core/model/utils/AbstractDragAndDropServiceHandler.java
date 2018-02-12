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
package org.talend.core.model.utils;

import java.util.List;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * created by wchen on 2013-5-20 Detailled comment
 * 
 */
public abstract class AbstractDragAndDropServiceHandler implements IDragAndDropServiceHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.utils.IDragAndDropServiceHandler#getComponentValue(org.talend.core.model.metadata.builder
     * .connection.Connection, java.lang.String, org.talend.core.model.metadata.IMetadataTable)
     */
    @Override
    public Object getComponentValue(Connection connection, String value, IMetadataTable table) {
        return getComponentValue(connection, value, table, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.utils.IDragAndDropServiceHandler#isValidForDataViewer(org.talend.core.model.metadata.
     * IMetadataTable)
     */
    @Override
    public boolean isValidForDataViewer(Connection connection, IMetadataTable metadataTable) {
        if (!canHandle(connection)) {
            return false;
        }
        return true;
    }

    protected String getRepositoryValueOfStringType(Connection connection, String value) {
        if (ContextParameterUtils.isContextMode(connection, value)) {
            return value;
        } else {
            return TalendQuoteUtils.addQuotesIfNotExist(value);
        }
    }

    @Override
    public boolean isGenericRepositoryValue(List<ComponentProperties> componentProperties, String paramName) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.utils.IDragAndDropServiceHandler#getGenericRepositoryValue(java.util.List,
     * java.lang.String)
     */
    @Override
    public Object getGenericRepositoryValue(List<ComponentProperties> componentProperties, String paramName) {
        return null;
    }
    
    @Override
    public boolean isGenericPropertiesValue(String paramName){
        return false;
    }
}
