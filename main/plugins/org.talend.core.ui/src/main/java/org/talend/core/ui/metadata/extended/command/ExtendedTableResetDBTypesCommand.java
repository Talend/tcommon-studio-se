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
package org.talend.core.ui.metadata.extended.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.IExtendedTableCommand;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.types.TypesManager;
import org.talend.core.ui.i18n.Messages;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public class ExtendedTableResetDBTypesCommand extends Command implements IExtendedTableCommand {

    public static final String LABEL = Messages.getString("ExtendedTableResetDBTypesCommand.ResetDBTypes.Label"); //$NON-NLS-1$

    private ExtendedTableModel extendedTable;

    private List<String> oldDbTypes;

    private String dbmsId;

    private AbstractExtendedTableViewer extendedTableViewer;

    public ExtendedTableResetDBTypesCommand(ExtendedTableModel extendedTable, String dbmsId,
            AbstractExtendedTableViewer extendedTableViewer) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.dbmsId = dbmsId;
        this.extendedTableViewer = extendedTableViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.commands.AbstractExtendedTableResetDBTypesCommand#execute()
     */
    @Override
    public void execute() {
        oldDbTypes = new ArrayList<String>();

        List beansList = extendedTable.getBeansList();
        for (Object object : beansList) {
            if (object instanceof IMetadataColumn) {
                oldDbTypes.add(((IMetadataColumn) object).getType());
            } else if (object instanceof MetadataColumn) {
                oldDbTypes.add(((MetadataColumn) object).getSourceType());
            }
        }

        for (int i = 0; i < beansList.size(); i++) {
            if (beansList.get(i) instanceof IMetadataColumn) {
                IMetadataColumn column = (IMetadataColumn) beansList.get(i);
                // qli modified to fix the bug 6654.
                if (dbmsId != null) {
                    if (!TypesManager.checkDBType(dbmsId, column.getTalendType(), column.getType())) {
                        column.setType(TypesManager.getDBTypeFromTalendType(dbmsId, column.getTalendType()));
                    }
                }
            } else if (beansList.get(i) instanceof MetadataColumn) {
                MetadataColumn column = (MetadataColumn) beansList.get(i);
                if (dbmsId != null) {
                    if (!TypesManager.checkDBType(dbmsId, column.getTalendType(), column.getSourceType())) {
                        column.setSourceType(TypesManager.getDBTypeFromTalendType(dbmsId, column.getTalendType()));
                    }
                }
            }
        }
        extendedTableViewer.getTableViewerCreator().getTableViewer().refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.commands.AbstractExtendedTableResetDBTypesCommand#undo()
     */
    @Override
    public synchronized void undo() {
        List beansList = extendedTable.getBeansList();

        for (int i = 0; i < beansList.size(); i++) {
            if (beansList.get(i) instanceof IMetadataColumn) {
                IMetadataColumn column = (IMetadataColumn) beansList.get(i);
                column.setType(oldDbTypes.get(i));
            }
        }
        extendedTableViewer.getTableViewerCreator().getTableViewer().refresh();
    }
}
