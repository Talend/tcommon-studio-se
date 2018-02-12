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
package org.talend.core.model.metadata.builder.database;

import java.sql.Connection;

import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdView;
import org.talend.utils.sql.metadata.constants.TableType;

/**
 * @author scorreia
 * 
 * A class for creating Views from a connection.
 */
public class ViewBuilder extends AbstractTableBuilder<TdView> {

    public ViewBuilder(Connection conn) {
        super(conn, TableType.VIEW);
    }

    /**
     * ViewBuilder constructor.
     * 
     * @param conn the connection from which the views will be created.
     */
    public ViewBuilder(org.talend.core.model.metadata.builder.connection.Connection conn) {
        super(conn, TableType.VIEW);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.builders.AbstractTableBuilder#createTable()
     */
    @Override
    protected TdView createTable() {
        TdView view = RelationalFactory.eINSTANCE.createTdView();
        view.setTableType(ExtractMetaDataFromDataBase.ETableTypes.TABLETYPE_VIEW.getName());
        view.setLabel(view.getName());
        return view;
    }

}
