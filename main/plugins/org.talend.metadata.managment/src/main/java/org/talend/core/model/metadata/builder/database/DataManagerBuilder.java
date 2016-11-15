// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata.builder.database;

import java.sql.SQLException;

import org.talend.cwm.softwaredeployment.SoftwaredeploymentFactory;
import org.talend.cwm.softwaredeployment.TdDataManager;
import org.talend.cwm.softwaredeployment.TdMachine;

/**
 * @deprecated this class does not seem to be used anymore. Remove it?
 */
public class DataManagerBuilder extends CwmBuilder {

    private final TdDataManager dataManager;

    private final TdMachine machine;

    public DataManagerBuilder(org.talend.core.model.metadata.builder.connection.Connection conn) throws SQLException {
        super(conn);
        this.dataManager = initializeDataManager();
        this.machine = initializeMachine();
    }

    private TdMachine initializeMachine() {
        return null;
    }

    private TdDataManager initializeDataManager() {
        // TODO scorreia get data manager informations

        TdDataManager dataMgr = SoftwaredeploymentFactory.eINSTANCE.createTdDataManager();
        // TODO scorreia set a name?
        return dataMgr;
    }

    public TdDataManager getDataManager() {
        return dataManager;
    }

    public TdMachine getMachine() {
        return machine;
    }

}
