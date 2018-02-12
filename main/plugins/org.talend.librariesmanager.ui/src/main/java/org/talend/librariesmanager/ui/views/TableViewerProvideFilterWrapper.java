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
package org.talend.librariesmanager.ui.views;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.misc.StringMatcher;

/**
 * A TableViewer wrapper use to wrap an exist TableViewer, and provider a text field to filter item with displayed
 * labels. It has a fixed time {@value #DELAY_TO_FILTER_TIME} to delay the filter action when user keep typing the
 * content. It will immediately do filter action when user typed CR keys.
 *
 * @author GaoZone
 * @version 0.1
 */
@SuppressWarnings("restriction")
public class TableViewerProvideFilterWrapper {

    private static final int DELAY_TO_FILTER_TIME = 200;

    private final TableViewer viewer;

    private final Table table;

    private Text filterText;

    private SearchFilter searchFilter;

    private Timer timer;

    private class SearchFilter extends ViewerFilter {

        private StringMatcher matcher;

        @Override
        public boolean select(Viewer _viewer, Object parentElement, Object element) {
            if (matcher == null) {
                return true;
            }
            ITableLabelProvider labelProvider = (ITableLabelProvider) viewer.getLabelProvider();
            int columnCount = table.getColumnCount();
            boolean match = false;
            for (int i = 0; i < columnCount; i++) {
                String columnText = labelProvider.getColumnText(element, i);
                match = matcher.match(columnText);
                if (match) {
                    return true;
                }
            }
            return false;
        }

        public void setPattern(String searchPattern) {
            if (searchPattern == null || searchPattern.length() == 0) {
                this.matcher = null;
            } else {
                String pattern = "*" + searchPattern + "*"; //$NON-NLS-1$//$NON-NLS-2$
                this.matcher = new StringMatcher(pattern, true, false);
            }
        }
    }

    public TableViewerProvideFilterWrapper(TableViewer viewer) {
        this.viewer = viewer;
        table = viewer.getTable();

        // optimize huge table
        doAddFilter();
    }

    private void doAddFilter() {
        wrapComposite();
        createFilter();
        addListener();
    }

    private void createFilter() {
        searchFilter = new SearchFilter();
        viewer.addFilter(searchFilter);
    }

    private void addListener() {
        filterText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                synchronized (e.getSource()) {
                    if (timer != null) {
                        timer.cancel();
                    }

                    timer = new Timer();
                    timer.schedule(createDelayedFilterTask(filterText.getText()), DELAY_TO_FILTER_TIME);
                }
            }
        });

        filterText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                switch (event.keyCode) {
                case SWT.CR:
                case SWT.KEYPAD_CR:
                    if (timer != null) {
                        timer.cancel();
                    }
                    fireFilter(filterText.getText());
                }
            }
        });
    }

    protected TimerTask createDelayedFilterTask(final String text) {

        return new TimerTask() {

            @Override
            public void run() {
                table.getDisplay().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        fireFilter(text);
                    }
                });
            }
        };
    }

    private void fireFilter(String text) {
        searchFilter.setPattern(text);
        try {
            table.setRedraw(false);
            viewer.refresh();
        } finally {
            table.setRedraw(true);
        }
    }

    private void wrapComposite() {
        Composite parent = table.getParent();
        Composite wrapper = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        wrapper.setLayout(layout);
        wrapper.setLayoutData(table.getLayoutData());

        filterText = new Text(wrapper, SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);
        filterText.setLayoutData(GridDataFactory.fillDefaults().create());
        filterText.setMessage("type filter text");

        Composite newParent = new Composite(wrapper, parent.getStyle());
        newParent.setLayoutData(gd);

        newParent.setLayout(parent.getLayout());
        table.setParent(newParent);
    }

    public static TableViewerProvideFilterWrapper wrapViewer(TableViewer viewer) {
        return new TableViewerProvideFilterWrapper(viewer);
    }
}
