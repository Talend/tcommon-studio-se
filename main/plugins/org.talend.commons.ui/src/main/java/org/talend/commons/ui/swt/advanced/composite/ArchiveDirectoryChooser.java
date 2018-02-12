// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.commons.ui.swt.advanced.composite;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.talend.commons.ui.i18n.Messages;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000) nrousseau $
 * 
 */
public class ArchiveDirectoryChooser {

    private String[] fileFilterString = { "*.*" }; //$NON-NLS-1$

    private Button itemFromDirectoryRadio;

    private Combo directoryPathField;

    private Button browseDirectoriesButton;

    private Button itemFromArchiveRadio;

    private Combo archivePathField;

    private Button browseArchivesButton;

    private String previouslyBrowsedDirectory = ""; //$NON-NLS-1$

    private String previouslyBrowsedArchive = ""; //$NON-NLS-1$

    private String lastPath = ""; //$NON-NLS-1$

    private Composite workArea;

    public ArchiveDirectoryChooser(Composite parent) {
        createControl(parent);
    }

    public Control getControl() {
        return workArea;
    }

    public void createControl(Composite parent) {
        workArea = new Composite(parent, SWT.NONE);
        workArea.setLayout(new GridLayout());
        workArea.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
        createItemRoot(workArea);
    }

    private void createItemRoot(Composite workArea) {
        Composite projectGroup = new Composite(workArea, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = 0;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        itemFromDirectoryRadio = new Button(projectGroup, SWT.RADIO);
        // itemFromDirectoryRadio.setText(DataTransferMessages.WizardProjectsImportPage_RootSelectTitle);
        itemFromDirectoryRadio.setText(Messages.getString("ArchiveDirectoryChooser.WizardProjectsImportPage_RootSelectTitle")); //$NON-NLS-1$

        this.directoryPathField = new Combo(projectGroup, SWT.SINGLE | SWT.BORDER);

        this.directoryPathField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

        browseDirectoriesButton = new Button(projectGroup, SWT.PUSH);
        // browseDirectoriesButton.setText(DataTransferMessages.DataTransfer_browse);
        browseDirectoriesButton.setText(Messages.getString("ArchiveDirectoryChooser.DataTransfer_browse")); //$NON-NLS-1$
        setButtonLayoutData(browseDirectoriesButton);

        // new project from archive radio button
        itemFromArchiveRadio = new Button(projectGroup, SWT.RADIO);
        // itemFromArchiveRadio.setText(DataTransferMessages.WizardProjectsImportPage_ArchiveSelectTitle);
        itemFromArchiveRadio.setText(Messages.getString("ArchiveDirectoryChooser.WizardProjectsImportPage_ArchiveSelectTitle")); //$NON-NLS-1$

        // project location entry field
        archivePathField = new Combo(projectGroup, SWT.SINGLE | SWT.BORDER);

        archivePathField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        // browse button
        browseArchivesButton = new Button(projectGroup, SWT.PUSH);
        // browseArchivesButton.setText(DataTransferMessages.DataTransfer_browse);
        browseArchivesButton.setText(Messages.getString("ArchiveDirectoryChooser.DataTransfer_browse")); //$NON-NLS-1$
        setButtonLayoutData(browseArchivesButton);

        itemFromDirectoryRadio.setSelection(true);
        archivePathField.setEnabled(false);
        browseArchivesButton.setEnabled(false);

        browseDirectoriesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleLocationDirectoryButtonPressed();
            }
        });

        browseArchivesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleLocationArchiveButtonPressed();
            }
        });

        directoryPathField.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                    lastPath = directoryPathField.getText().trim();
                }
            }

        });

        directoryPathField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
                lastPath = directoryPathField.getText().trim();
            }

        });

        archivePathField.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                    lastPath = archivePathField.getText().trim();
                }
            }
        });

        archivePathField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
                lastPath = archivePathField.getText().trim();
            }
        });

        itemFromDirectoryRadio.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                directoryRadioSelected();
            }
        });

        itemFromArchiveRadio.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                archiveRadioSelected();
            }
        });
    }

    private void archiveRadioSelected() {
        if (itemFromArchiveRadio.getSelection()) {
            directoryPathField.setEnabled(false);
            browseDirectoriesButton.setEnabled(false);
            archivePathField.setEnabled(true);
            browseArchivesButton.setEnabled(true);
            lastPath = archivePathField.getText().trim();
            archivePathField.setFocus();
        }
    }

    public boolean isArchiveTypeSelected() {
        return itemFromArchiveRadio.getSelection();
    }

    protected GridData setButtonLayoutData(Button button) {
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        int widthHint = IDialogConstants.BUTTON_WIDTH;
        Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = Math.max(widthHint, minSize.x);
        button.setLayoutData(data);
        return data;
    }

    protected void handleLocationDirectoryButtonPressed() {

        DirectoryDialog dialog = new DirectoryDialog(directoryPathField.getShell());
        // dialog.setMessage(DataTransferMessages.FileExport_selectDestinationMessage);
        dialog.setMessage(Messages.getString("ArchiveDirectoryChooser.FileExport_selectDestinationMessage")); //$NON-NLS-1$

        String dirName = directoryPathField.getText().trim();
        if (dirName.length() == 0) {
            dirName = previouslyBrowsedDirectory;
        }

        if (dirName.length() == 0) {
            // dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getLocation().toOSString());
            dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
        } else {
            File path = new File(dirName);
            if (path.exists()) {
                dialog.setFilterPath(new Path(dirName).toOSString());
            }
        }

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            previouslyBrowsedDirectory = selectedDirectory;
            directoryPathField.setText(previouslyBrowsedDirectory);
            lastPath = directoryPathField.getText().trim();
        }

    }

    /**
     * The browse button has been selected. Select the location.
     */
    protected void handleLocationArchiveButtonPressed() {

        FileDialog dialog = new FileDialog(archivePathField.getShell(), SWT.SAVE);
        dialog.setFilterExtensions(fileFilterString);
        // dialog.setText(DataTransferMessages.ArchiveExport_description);
        dialog.setText(Messages.getString("ArchiveDirectoryChooser.ArchiveExport_description")); //$NON-NLS-1$
        String fileName = archivePathField.getText().trim();
        if (fileName.length() == 0) {
            fileName = previouslyBrowsedArchive;
        }

        if (fileName.length() == 0) {
            // dialog.setFilterPath(IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getLocation().toOSString());
            dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
        } else {
            File path = new File(fileName);
            if (path.exists()) {
                dialog.setFilterPath(new Path(fileName).toOSString());
            }
        }

        String selectedArchive = dialog.open();
        if (selectedArchive != null) {
            previouslyBrowsedArchive = selectedArchive;
            archivePathField.setText(previouslyBrowsedArchive);
            lastPath = archivePathField.getText().trim();
        }
    }

    private void directoryRadioSelected() {
        if (itemFromDirectoryRadio.getSelection()) {
            directoryPathField.setEnabled(true);
            browseDirectoriesButton.setEnabled(true);
            archivePathField.setEnabled(false);
            browseArchivesButton.setEnabled(false);
            lastPath = directoryPathField.getText().trim();
            directoryPathField.setFocus();
        }
    }

    /**
     * Sets the fileFilterString.
     * 
     * @param fileFilterString the fileFilterString to set
     */
    public void setFileFilterString(String[] fileFilterString) {
        this.fileFilterString = fileFilterString;
    }

    /**
     * Getter for lastPath.
     * 
     * @return the lastPath
     */
    public String getLastPath() {
        return this.lastPath;
    }

    /**
     * DOC bqian Comment method "setArchiveSelected".
     * 
     * @param isArchive
     */
    public void setArchiveSelected(boolean isArchive) {
        itemFromArchiveRadio.setSelection(isArchive);
        // archiveRadioSelected();
    }

    /**
     * DOC bqian Comment method "setDestinationValue".
     * 
     * @param value
     */
    public void setDestinationValue(String value) {
        if (isArchiveTypeSelected()) {
            archivePathField.setText(value);
        } else {
            directoryPathField.setText(value);
        }
    }

    /**
     * DOC bqian Comment method "addDestinationItem".
     * 
     * @param string
     */
    public void addDestinationItem(String string) {
        if (isArchiveTypeSelected()) {
            archivePathField.add(string);
        } else {
            directoryPathField.add(string);
        }
    }

    // protected void displayErrorDialog(String message) {
    // MessageDialog.openError(getContainer().getShell(), getErrorDialogTitle(), message);
    // }
    //
    // @SuppressWarnings("restriction")
    // protected String getErrorDialogTitle() {
    // return IDEWorkbenchMessages.WizardExportPage_internalErrorTitle;
    // }

}
