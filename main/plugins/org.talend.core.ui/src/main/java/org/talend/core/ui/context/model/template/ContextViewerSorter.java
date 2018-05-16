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
package org.talend.core.ui.context.model.template;

import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ContextViewerSorter extends ViewerSorter {

    private boolean original = false; // flag

    //
    private int direction;

    private TreeViewer sortViewer;

    private TreeColumn column;

    //
    private IContextManager contextManager;

    //
    private int index = 0;

    /*
     * sort column
     */

    public ContextViewerSorter(TreeViewer sortViewer, TreeColumn column, int direction) {
        super();
        this.original = false;
        this.direction = direction;
        this.sortViewer = sortViewer;
        this.column = column;
    }

    /*
     * original order
     * 
     * original must be true.
     */
    public ContextViewerSorter(IContextManager contextManager) {
        super();
        this.original = true;
        this.contextManager = contextManager;
    }

    @Override
    public void sort(Viewer viewer, Object[] elements) {
        if (!original && sortViewer != null) {
            final Tree tree = sortViewer.getTree();
            while (index < tree.getColumns().length && tree.getColumn(index) != column) {
                index++;
            }
        }
        super.sort(viewer, elements);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        if (original) {
            if (e1 instanceof ContextVariableTabParentModel && e2 instanceof ContextVariableTabParentModel) {
                final int index1 = getContextParameterIndex(contextManager,
                        ((ContextVariableTabParentModel) e1).getContextParameter());
                final int index2 = getContextParameterIndex(contextManager,
                        ((ContextVariableTabParentModel) e2).getContextParameter());
                return new Integer(index1).compareTo(index2);
            }

        } else {
            if (sortViewer != null) {
                ITableLabelProvider labelProvider = (ITableLabelProvider) sortViewer.getLabelProvider();

                final String text1 = labelProvider.getColumnText(e1, index);
                final String text2 = labelProvider.getColumnText(e2, index);

                return getComparator().compare((text1 != null ? text1 : ""), (text2 != null ? text2 : "")) * direction; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return 0; // keep
    }

    private int getContextParameterIndex(final IContextManager contextManager, IContextParameter param) {
        if (contextManager == null || param == null) {
            return -1;
        }
        final List<IContextParameter> contextParameterList = contextManager.getDefaultContext().getContextParameterList();
        for (int i = 0; i < contextParameterList.size(); i++) {
            if (param.getName().equals(contextParameterList.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }
}
