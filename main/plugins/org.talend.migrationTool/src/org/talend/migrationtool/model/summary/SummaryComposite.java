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
package org.talend.migrationtool.model.summary;

import java.util.List;

import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.migration.IMigrationTask.ExecutionResult;
import org.talend.migration.IProjectMigrationTask;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class SummaryComposite extends Composite {

    private static final Color BCK_COLOR = new Color(null, 255, 255, 255);

    public SummaryComposite(Composite parent, int style, List<IProjectMigrationTask> tasks, int size) {
        super(parent, style);
        parent.setBackground(BCK_COLOR);
        this.setLayout(new FillLayout());
        this.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.setBackground(BCK_COLOR);

        ScrolledComposite scrolled = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        scrolled.setLayout(new GridLayout());
        scrolled.setExpandHorizontal(true);
        scrolled.setBackground(BCK_COLOR);

        Composite container = new Composite(scrolled, SWT.NONE);
        scrolled.setContent(container);
        GridLayout layout2 = new GridLayout();
        container.setLayout(layout2);
        container.setBackground(BCK_COLOR);

        TableViewer tableWiewer = new TableViewer(container, SWT.NONE);
        tableWiewer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public void dispose() {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof List) {
                    return ((List) inputElement).toArray();
                }
                return null;
            }
        });

        tableWiewer.setLabelProvider(new LabelProvider() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
             */
            @Override
            public Image getImage(Object element) {
                if (element instanceof IProjectMigrationTask) {
                    ExecutionResult status = ((IProjectMigrationTask) element).getStatus();
                    if (status != null) {
                        switch (status) {
                        case SUCCESS_WITH_ALERT:
                            return ImageProvider.getImage(ECoreImage.STATUS_OK);
                        case FAILURE:
                            return ImageProvider.getImage(ECoreImage.MODULE_ERROR_ICON);

                        default:
                            break;
                        }
                    }

                }

                return super.getImage(element);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
             */
            @Override
            public String getText(Object element) {
                if (element instanceof IProjectMigrationTask) {
                    return ((IProjectMigrationTask) element).getClass().getSimpleName();
                }
                return super.getText(element);
            }
        });

        Table table = tableWiewer.getTable();
        TableLayout tableLayout = new TableLayout();
        table.setLayout(tableLayout);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        GridData data = new GridData(GridData.FILL_BOTH);
        table.setLayoutData(data);
        tableLayout.addColumnData(new ColumnPixelData(500, true));
        new TableColumn(table, SWT.NONE);

        tableWiewer.setInput(tasks);

        container.setSize(container.computeSize(size, SWT.DEFAULT));
    }
}
