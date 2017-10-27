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
package org.talend.core.model.metadata.builder.database;

import java.sql.SQLException;

import org.talend.cwm.softwaredeployment.SoftwaredeploymentFactory;
import org.talend.cwm.softwaredeployment.TdDataManager;
import org.talend.cwm.softwaredeployment.TdMachine;

/**
 * DOC scorreia class global comment. Detailled comment
 * 
 * @deprecated this class does not seem to be used anymore. Remove it?
 */
public class DataManagerBuilder extends CwmBuilder {

    private final TdDataManager dataManager;

    private final TdMachine machine;

    /**
     * DOC scorreia DataManagerBuilder constructor comment.
     * 
     * @param conn
     * @throws SQLException
     */
    // public DataManagerBuilder(Connection conn) throws SQLException {
    // super(conn);
    // this.dataManager = initializeDataManager();
    // this.machine = initializeMachine();
    // }
    public DataManagerBuilder(org.talend.core.model.metadata.builder.connection.Connection conn) throws SQLException {
        super(conn);
        this.dataManager = initializeDataManager();
        this.machine = initializeMachine();
    }

    /**
     * DOC scorreia Comment method "initializeMachine".
     * 
     * @return
     */
    private TdMachine initializeMachine() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOC scorreia Comment method "initializeDataManager".
     * 
     * @return
     */
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
