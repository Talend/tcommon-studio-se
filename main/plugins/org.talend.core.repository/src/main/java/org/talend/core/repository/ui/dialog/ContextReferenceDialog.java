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
package org.talend.core.repository.ui.dialog;

import java.io.File;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ContextReferenceBean;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class ContextReferenceDialog extends SelectionDialog {

    /**
     * DOC hwang ContextReferenceDialog class global comment. Detailled comment
     */
    class ContextsProvide extends LabelProvider implements ITableLabelProvider {

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0) {
                if (element instanceof ContextReferenceBean) {
                    ContextReferenceBean bean = (ContextReferenceBean) element;
                    if (bean.getRelateType() == null) {
                        return null;
                    }
                    if (ERepositoryObjectType.PROCESS != null
                            && bean.getRelateType().equals(ERepositoryObjectType.PROCESS.getType())) {
                        return ImageProvider.getImage(ECoreImage.PROCESS_ICON);
                    } else if (ERepositoryObjectType.METADATA_CONNECTIONS != null
                            && bean.getRelateType().equals(ERepositoryObjectType.METADATA_CONNECTIONS.getType())) {
                        return ImageProvider.getImage(ECoreImage.METADATA_CONNECTION_ICON);
                    } else if (ERepositoryObjectType.METADATA_FILE_DELIMITED != null
                            && bean.getRelateType().equals(ERepositoryObjectType.METADATA_FILE_DELIMITED.getType())) {
                        return ImageProvider.getImage(ECoreImage.METADATA_FILE_DELIMITED_ICON);
                    } else if (ERepositoryObjectType.METADATA_FILE_EXCEL != null
                            && bean.getRelateType().equals(ERepositoryObjectType.METADATA_FILE_EXCEL.getType())) {
                        return ImageProvider.getImage(ECoreImage.METADATA_FILE_EXCEL_ICON);
                    } else if (ERepositoryObjectType.METADATA_FILE_XML != null
                            && bean.getRelateType().equals(ERepositoryObjectType.METADATA_FILE_XML.getType())) {
                        return ImageProvider.getImage(ECoreImage.METADATA_FILE_XML_ICON);
                    } else if (ERepositoryObjectType.JOBLET != null
                            && bean.getRelateType().equals(ERepositoryObjectType.JOBLET.getType())) {
                        return ImageProvider.getImage(ECoreImage.JOBLET_ICON);
                    }
                    // if (bean.isRelate()) {
                    // return ImageProvider.getImage(ECoreImage.PROCESS_ICON);
                    // } else {
                    // return ImageProvider.getImage(ECoreImage.JOBLET_ICON);
                    // }
                }
            }

            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            ContextReferenceBean bean = (ContextReferenceBean) element;
            switch (columnIndex) {
            case 1:
                return bean.getProjectName() + " "; //$NON-NLS-1$
            case 2:
                if (bean.isDelete()) {
                    return bean.getRelateName()
                            + " " + bean.getRelateVersion() + "(" + Messages.getString("ContextReferenceDialog.Recycle") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                } else {
                    String prefix = ""; //$NON-NLS-1$
                    if (bean.getRelatePath() != null && !"".equals(bean.getRelatePath())) { //$NON-NLS-1$
                        prefix = bean.getRelatePath() + File.separator;
                    }
                    return prefix + bean.getRelateName() + " " + bean.getRelateVersion(); //$NON-NLS-1$
                }
            case 3:
                return " " + bean.getRelateType();
            case 0:
            default:
                return ""; //$NON-NLS-1$
            }
        }

    }

    private IRepositoryViewObject objToDelete;

    private List<ContextReferenceBean> referenceList;

    public ContextReferenceDialog(Shell parentShell, IRepositoryViewObject objToDelete, List<ContextReferenceBean> referenceList) {
        super(parentShell);
        setShellStyle(SWT.RESIZE | getShellStyle());
        this.objToDelete = objToDelete;
        this.referenceList = referenceList;
        if (objToDelete != null) {
            Item item = objToDelete.getProperty().getItem();
            if (item != null) {
                setMessage(Messages.getString(
                        "ContextReferenceDialog.kindMessages1", item.getProperty().getLabel(), item.getProperty().getVersion())); //$NON-NLS-1$
            }
        }
        setHelpAvailable(false);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ContextReferenceDialog.Title")); //$NON-NLS-1$
        newShell.setSize(650, 250);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        createMessageArea(composite);
        Label label = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        TableViewer viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

        viewer.setLabelProvider(new ContextsProvide());
        viewer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public void dispose() {
                //

            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                //

            }

            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement != null) {
                    return ((List) inputElement).toArray();
                }
                return new Object[0];
            }
        });

        Table table = viewer.getTable();
        TableLayout tableLayout = new TableLayout();
        table.setLayout(tableLayout);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = 100;
        data.widthHint = 255;
        table.setLayoutData(data);

        // column
        TableColumn column;

        tableLayout.addColumnData(new ColumnPixelData(20, false));
        column = new TableColumn(table, SWT.NONE);
        column.setText(""); //$NON-NLS-1$

        tableLayout.addColumnData(new ColumnPixelData(200, true));
        column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.getString("JobletReferenceDialog.project")); //$NON-NLS-1$

        tableLayout.addColumnData(new ColumnPixelData(200, true));
        column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.getString("ContextReferenceDialog.ReferenceJob")); //$NON-NLS-1$
        table.setSortColumn(column);

        tableLayout.addColumnData(new ColumnPixelData(200, true));
        column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.getString("ContextReferenceDialog.Types")); //$NON-NLS-1$
        column.setAlignment(SWT.CENTER);
        column.setToolTipText(Messages.getString("ContextReferenceDialog.NodeTypeTip")); //$NON-NLS-1$

        viewer.setInput(referenceList);
        return composite;
    }
}
