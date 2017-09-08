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
package org.talend.core.ui.services;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.IService;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.process.IElement;
import org.talend.core.sqlbuilder.util.ConnectionParameters;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface ISQLBuilderService extends IService {

    void closeIfSQLBuilderDialog(Object shellData);

    void openSQLBuilderDialog(ConnectionParameters connParameters, Composite composite, IElement elem, String propertyName,
            CommandStack commandStack, Object abstractElementPropertySectionController, Object abstractMultiPageTalendEditor);

    Dialog openSQLBuilderDialog(Shell parentShell, String processName, ConnectionParameters connParameters);

    void updateSqlBuilderDialogTitle(String labelText, String name, String uniqueName);

    void closeSqlBuilderDialogs(String name);

    DatabaseConnection createConnection(ConnectionParameters parameters);
}
