// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.metadata.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * DOC jding  class global comment. Detailled comment
 */
public class RemoveDeprecateSASItemMigrationTask extends AbstractItemMigrationTask {

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2021, 4, 20, 17, 50, 30);
        return gc.getTime();
    }

    @Override
    public ExecutionResult execute(Item item) {
        if (item instanceof DatabaseConnectionItem) {
            String typeName = null;
            DatabaseConnectionItem connItem = (DatabaseConnectionItem) item;
            typeName = connItem.getTypeName();
            if (StringUtils.isBlank(typeName)) {
                Connection connection = connItem.getConnection();
                if (connection instanceof DatabaseConnection) {
                    typeName = ((DatabaseConnection) connection).getDatabaseType();
                }
            }
            if (StringUtils.isBlank(typeName) || item.getProperty() == null
                    || !EDatabaseTypeName.SAS.getXmlName().equals(typeName)) {
                return ExecutionResult.NOTHING_TO_DO;
            }

            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                IRepositoryViewObject repositoryObject = factory.getSpecificVersion(item.getProperty().getId(),
                        item.getProperty().getVersion(), true);
                // to delete all version items
                factory.deleteObjectPhysical(repositoryObject);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return ExecutionResult.FAILURE;
            }

        } else {
            return ExecutionResult.NOTHING_TO_DO;
        }

        return ExecutionResult.SUCCESS_NO_ALERT;
    }

}
