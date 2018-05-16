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
package org.talend.repository.mdm.repository.migration;

import java.util.Date;
import java.util.GregorianCalendar;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.designerproperties.MDMVersions;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * created by wchen on Apr 20, 2015 Detailled comment
 *
 */
public class MdmAddVersionMigrationTask extends AbstractItemMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        try {
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (item instanceof MDMConnectionItem) {
                MDMConnectionItem mdmItem = (MDMConnectionItem) item;
                MDMConnection connection = (MDMConnection) mdmItem.getConnection();
                if (connection.getVersion() == null) {
                    connection.setVersion(MDMVersions.MDM_S56.getKey());
                    if (connection.getServerUrl() == null) {
                        if (connection.getServer() != null && connection.getPort() != null) {
                            connection.setServerUrl("http://" + connection.getServer() + ":" + connection.getPort()
                                    + "/talend/TalendPort");
                        } else {
                            // set default url
                            connection.setServerUrl("http://localhost:8180/talend/TalendPort");
                        }
                    }
                    factory.save(item, true);
                    return ExecutionResult.SUCCESS_NO_ALERT;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }

        return ExecutionResult.NOTHING_TO_DO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2015, 4, 20, 12, 0, 0);
        return gc.getTime();
    }

}
