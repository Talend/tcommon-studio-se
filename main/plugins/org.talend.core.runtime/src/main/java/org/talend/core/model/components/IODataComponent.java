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
package org.talend.core.model.components;

import java.util.List;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: IODataComponent.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class IODataComponent {

    private int columnOption = IMetadataColumn.OPTIONS_NONE;

    private IConnection connection;

    private IMetadataTable newMetadataTable;

    private IMetadataTable connMetadataTable;

    private List<ColumnNameChanged> columnNameChanged = null;

    private static ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);

    /**
     * DOC smallet IODataComponent constructor comment.
     * 
     * @param connection
     */
    public IODataComponent(IConnection connection) {
        super();
        this.connection = connection;
        if (connection != null && connection.getMetadataTable() != null) {
            this.newMetadataTable = connection.getMetadataTable().clone();
            this.connMetadataTable = connection.getMetadataTable().clone();
        }
    }

    public IODataComponent(IConnection connection, IMetadataTable clonedTable) {
        super();
        this.connection = connection;
        this.newMetadataTable = clonedTable;
        if (connection != null && connection.getMetadataTable() != null) {
            this.connMetadataTable = connection.getMetadataTable().clone();
        }
    }

    @Override
    public String toString() {
        return "Connection=[" + connection + "], Table=[" + newMetadataTable + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public String getName() {
        return connection.getName();
    }

    public String getUniqueName() {
        return connection.getUniqueName();
    }

    /**
     * Getter for connection.
     * 
     * @return the connection
     */
    public IConnection getConnection() {
        return this.connection;
    }

    public EConnectionType getConnectionType() {
        return connection.getLineStyle();
    }

    public IMetadataTable getTable() {
        return newMetadataTable;
    }

    public void setTable(IMetadataTable table) {
        this.newMetadataTable = table;
    }

    public INode getTarget() {
        return connection.getTarget();
    }

    public INode getSource() {
        return connection.getSource();
    }

    public boolean hasChanged() {
        return newMetadataTable != null && !newMetadataTable.sameMetadataAs(connMetadataTable, getColumnOption());
    }

    public List<ColumnNameChanged> getColumnNameChanged() {
        if (columnNameChanged == null && coreService != null) {
            columnNameChanged = coreService.getColumnNameChanged(connMetadataTable, newMetadataTable);
        }
        return columnNameChanged;
    }

    private List<ColumnNameChanged> newMetadataColumns;

    public List<ColumnNameChanged> getNewMetadataColumns() {
        if (newMetadataColumns == null && coreService != null) {
            newMetadataColumns = coreService.getNewMetadataColumns(connMetadataTable, newMetadataTable);
        }
        return newMetadataColumns;
    }

    private List<ColumnNameChanged> removeMetadataColumns;

    public List<ColumnNameChanged> getRemoveMetadataColumns() {
        if (removeMetadataColumns == null && coreService != null) {
            removeMetadataColumns = coreService.getRemoveMetadataColumns(connMetadataTable, newMetadataTable);
        }
        return removeMetadataColumns;
    }

    public void setColumnNameChanged(List<ColumnNameChanged> columnNameChanged) {
        this.columnNameChanged = columnNameChanged;
    }

    /**
     * Getter for columnOption.
     * 
     * @return the columnOption
     */
    public int getColumnOption() {
        return this.columnOption;
    }

    /**
     * Sets the columnOption.
     * 
     * @param columnOption the columnOption to set
     */
    public void setColumnOption(int columnOption) {
        this.columnOption = columnOption;
    }

    public IMetadataTable getConnMetadataTable() {
        return connMetadataTable;
    }

    public void setConnMetadataTable(IMetadataTable connMetadataTable) {
        this.connMetadataTable = connMetadataTable;
    }

    /**
     * Getter for newMetadataTable.
     * 
     * @return the newMetadataTable
     */
    public IMetadataTable getNewMetadataTable() {
        return this.newMetadataTable;
    }

    /**
     * Sets the newMetadataTable.
     * 
     * @param newMetadataTable the newMetadataTable to set
     */
    public void setNewMetadataTable(IMetadataTable newMetadataTable) {
        this.newMetadataTable = newMetadataTable;
    }
}
