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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.JobletReferenceBean;

/**
 * ggu class global comment. Detailled comment
 */
public class JobletReferenceDialog extends SelectionDialog {

    /**
     * DOC ggu JobletReferenceDialog class global comment. Detailled comment
     */
    class JobletsProvide extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0) {
                if (element instanceof JobletReferenceBean) {
                    JobletReferenceBean bean = (JobletReferenceBean) element;
                    if (bean.isJob()) {
                        return ImageProvider.getImage(ECoreImage.PROCESS_ICON);
                    } else {
                        return ImageProvider.getImage(ECoreImage.JOBLET_ICON);
                    }
                }
            }

            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            JobletReferenceBean bean = (JobletReferenceBean) element;
            switch (columnIndex) {
            case 1:
                return bean.getProjectName() + " "; //$NON-NLS-1$
            case 2:
                if (bean.isDelete()) {
                    return bean.getJobName()
                            + " " + bean.getJobVersion() + "(" + Messages.getString("JobletReferenceDialog.DeletedInfor") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$  
                } else {
                    String prefix = ""; //$NON-NLS-1$
                    if (bean.getJobPath() != null && !"".equals(bean.getJobPath())) { //$NON-NLS-1$
                        prefix = bean.getJobPath() + File.separator;
                    }
                    return prefix + bean.getJobName() + " " + bean.getJobVersion(); //$NON-NLS-1$
                }
            case 3:
                return "" + bean.getNodeNum(); //$NON-NLS-1$
            case 0:
            default:
                return ""; //$NON-NLS-1$
            }
        }

    }

    private IRepositoryViewObject objToDelete;

    private List<JobletReferenceBean> referenceList;

    public JobletReferenceDialog(Shell parentShell, IRepositoryViewObject objToDelete, List<JobletReferenceBean> referenceList) {
        super(parentShell);
        setShellStyle(SWT.RESIZE | getShellStyle());
        this.objToDelete = objToDelete;
        this.referenceList = referenceList;
        if (objToDelete != null) {
            Item item = objToDelete.getProperty().getItem();
            if (item != null) {
                setMessage(Messages.getString(
                        "JobletReferenceDialog.Messages", item.getProperty().getLabel(), item.getProperty().getVersion())); //$NON-NLS-1$
            }
        }
        setHelpAvailable(false);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setSize(540, 400);
        newShell.setText(Messages.getString("JobletReferenceDialog.Title")); //$NON-NLS-1$
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        createMessageArea(composite);
        Label label = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        TableViewer viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

        viewer.setLabelProvider(new JobletsProvide());
        viewer.setContentProvider(new IStructuredContentProvider() {

            public void dispose() {
                //

            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                //

            }

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
        column.setText(Messages.getString("JobletReferenceDialog.ReferenceJob")); //$NON-NLS-1$
        table.setSortColumn(column);

        tableLayout.addColumnData(new ColumnPixelData(55, true));
        column = new TableColumn(table, SWT.NONE);
        column.setText(Messages.getString("JobletReferenceDialog.NodeTotals")); //$NON-NLS-1$
        column.setAlignment(SWT.CENTER);
        column.setToolTipText(Messages.getString("JobletReferenceDialog.NodeTotalsTip")); //$NON-NLS-1$

        viewer.setInput(referenceList);
        return composite;
    }

    protected void initializeBounds() {
        super.initializeBounds();

        Point size = getShell().getSize();
        Point location = getInitialLocation(size);
        getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
    }
}
