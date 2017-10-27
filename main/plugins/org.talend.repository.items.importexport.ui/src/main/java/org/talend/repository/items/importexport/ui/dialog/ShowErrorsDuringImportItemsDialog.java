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
package org.talend.repository.items.importexport.ui.dialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.repository.items.importexport.ui.i18n.Messages;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class ShowErrorsDuringImportItemsDialog extends Dialog {

    public List<String> errors = new ArrayList<String>();

    public Table table;

    private String fDirectory;

    private File fInputFile;

    public ShowErrorsDuringImportItemsDialog(Shell shell, List<String> errors) {
        super(shell);
        this.setShellStyle(this.getShellStyle() | SWT.MIN | SWT.MAX | SWT.RESIZE);
        this.errors = errors;
        fInputFile = Platform.getLogFileLocation().toFile();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ShowErrorsDuringImportItemsDialog_title")); //$NON-NLS-1$
        newShell.setImage(ImageProvider.getImage(EImage.ERROR_ICON));
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID,
                Messages.getString("ShowErrorsDuringImportItemsDialog_exportLogButton_title"), true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, false);
    }

    @Override
    protected void okPressed() {
        handleExport(true);
        super.okPressed();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 500;
        gd.widthHint = 900;
        tableComposite.setLayoutData(gd);

        // List Table
        table = new Table(tableComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn itemName = new TableColumn(table, SWT.NONE);
        itemName.setText("Item Name"); //$NON-NLS-1$
        itemName.setWidth(230);

        TableColumn errorLog = new TableColumn(table, SWT.NONE);
        errorLog.setText("Error Log"); //$NON-NLS-1$
        errorLog.setWidth(330);

        TableColumn path = new TableColumn(table, SWT.NONE);
        path.setText("Path"); //$NON-NLS-1$
        path.setWidth(350);
        setMenu(table);
        addItemElements(errors);
        return parent;
    }

    //
    private void setMenu(final Table table) {
        Listener popUpListener = new Listener() {

            @Override
            public void handleEvent(Event event) {
                StringBuffer sbf = new StringBuffer();
                for (int i : table.getSelectionIndices()) {
                    TableItem item = table.getItem(i);
                    sbf.append(item.getData());
                    sbf.append("\n");
                }
                Object[] data = { "" + sbf };
                Clipboard clipboard = new Clipboard(Display.getCurrent());
                Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
                clipboard.setContents(data, transfers);
            }
        };
        Menu menu = new Menu(table);
        MenuItem copy = new MenuItem(menu, SWT.PUSH);
        copy.setText("Copy");
        copy.setImage(ImageProvider.getImage(EImage.COPY_ICON));
        copy.addListener(SWT.Selection, popUpListener);
        table.setMenu(menu);

        // add key Listener
        table.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == 99 && (e.stateMask & SWT.CTRL) != 0) {
                    StringBuffer sbf = new StringBuffer();
                    for (int i : table.getSelectionIndices()) {
                        TableItem item = table.getItem(i);
                        sbf.append(item.getData());
                        sbf.append("\n");
                    }
                    Object[] data = { "" + sbf };
                    Clipboard clipboard = new Clipboard(Display.getCurrent());
                    Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
                    clipboard.setContents(data, transfers);
                }
            }
        });
    }

    public void addItemElements(List<String> errors) {
        table.setRedraw(false);
        for (String error : errors) {
            if (error != null) {
                String[] messages = error.split(";");
                if (messages.length != 3) {
                    continue;
                }
                TableItem tableItem = new TableItem(table, SWT.NONE);
                tableItem.setData(error);
                tableItem.setImage(0, ImageProvider.getImage(EImage.ERROR_SMALL));
                tableItem.setText(0, messages[0]);
                tableItem.setText(1, messages[1]);
                tableItem.setText(2, messages[2]);
            }
        }
        table.setRedraw(true);
    }

    private void handleExport(boolean exportWholeLog) {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] { "*.log" }); //$NON-NLS-1$
        if (fDirectory != null)
            dialog.setFilterPath(fDirectory);
        String path = dialog.open();
        if (path != null) {
            if (path.indexOf('.') == -1 && !path.endsWith(".log")) //$NON-NLS-1$
                path += ".log"; //$NON-NLS-1$
            File outputFile = new Path(path).toFile();
            fDirectory = outputFile.getParent();
            if (outputFile.exists()) {
                String message = NLS.bind(Messages.getString("ShowErrorsDuringImportItemsDialog_confirmOverwrite_message"),
                        outputFile.toString());
                if (!MessageDialog.openQuestion(getShell(),
                        (exportWholeLog ? Messages.getString("ShowErrorsDuringImportItemsDialog_exportLogButton_title")
                                : Messages.getString("ShowErrorsDuringImportItemsDialog_exportLogEntry")), message))
                    ;
                return;
            }

            Reader in = null;
            Writer out = null;
            try {
                out = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"); //$NON-NLS-1$
                if (exportWholeLog)
                    in = new InputStreamReader(new FileInputStream(fInputFile), "UTF-8"); //$NON-NLS-1$
                copy(in, out);
            } catch (IOException ex) {
                try {
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                } catch (IOException e1) { // do nothing
                }
            }
        }
    }

    private void copy(Reader input, Writer output) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(input);
            writer = new BufferedWriter(output);
            String line;
            while (reader.ready() && ((line = reader.readLine()) != null)) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) { // do nothing
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (writer != null)
                    writer.close();
            } catch (IOException e1) {
                // do nothing
            }
        }
    }
}
