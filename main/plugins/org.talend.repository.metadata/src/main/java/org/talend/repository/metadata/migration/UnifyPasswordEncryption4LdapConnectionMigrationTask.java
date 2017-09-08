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
package org.talend.repository.metadata.migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.LDAPSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * created by ggu on Aug 29, 2014 Detailled comment
 *
 */
public class UnifyPasswordEncryption4LdapConnectionMigrationTask extends AbstractItemMigrationTask {

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.METADATA_LDAP_SCHEMA);
        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org .talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        if (item instanceof LDAPSchemaConnectionItem) {
            Connection connection = ((LDAPSchemaConnectionItem) item).getConnection();
            if (connection instanceof LDAPSchemaConnection) {
                LDAPSchemaConnection ldapConn = (LDAPSchemaConnection) connection;
                try {
                    if (!ldapConn.isContextMode()) {
                        // before this migration ,the pass is raw, didn't encrypt.
                        String pass = ldapConn.getBindPassword();
                        ldapConn.setBindPassword(ldapConn.getValue(pass, true));
                        factory.save(item, true);
                        return ExecutionResult.SUCCESS_NO_ALERT;
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                    return ExecutionResult.FAILURE;
                }
            }
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2014, 8, 29, 12, 0, 0);
        return gc.getTime();
    }

}
