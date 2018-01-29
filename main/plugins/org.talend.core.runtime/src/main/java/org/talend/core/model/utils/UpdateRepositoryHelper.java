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
package org.talend.core.model.utils;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.UpdatesConstants;

/**
 * ggu class global comment. Detailled comment
 */
public final class UpdateRepositoryHelper {

    /**
     * 
     * ggu Comment method "getRepositoryAliasName".
     * 
     * @param item
     * @return
     */
    public static String getRepositorySourceName(Item item) {
        String source = UpdatesConstants.EMPTY;
        if (item != null) {
            String aliasName = null;
            if (item instanceof ConnectionItem) {
                ERepositoryObjectType repositoryObjectType = ERepositoryObjectType.getItemType(item);
                aliasName = repositoryObjectType.getAlias();
                Connection connection = ((ConnectionItem) item).getConnection();
                if (connection instanceof DatabaseConnection) {
                    String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, UpdatesConstants.TYPE,
                            null);
                    aliasName += " (" + currentDbType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                if (repositoryObjectType.getType().equals("SERVICES")) {
                    aliasName = "SERVICES";
                }
            } else if (item instanceof ContextItem) {
                aliasName = UpdatesConstants.CONTEXT;
            }
            //
            source = (aliasName == null ? UpdatesConstants.EMPTY : aliasName);
            source = source + UpdatesConstants.COLON + item.getProperty().getLabel();
        }
        return source;
    }

    public static List<String> getAllHadoopConnectionTypes() {
        List<String> hadoopTypeList = new ArrayList<String>();
        hadoopTypeList.add("METADATA_NOSQL_CONNECTIONS"); //$NON-NLS-1$
        hadoopTypeList.add("HADOOPCLUSTER"); //$NON-NLS-1$
        hadoopTypeList.add("HCATALOG"); //$NON-NLS-1$
        hadoopTypeList.add("HDFS"); //$NON-NLS-1$
        hadoopTypeList.add("OOZIE"); //$NON-NLS-1$
        return hadoopTypeList;
    }

}
