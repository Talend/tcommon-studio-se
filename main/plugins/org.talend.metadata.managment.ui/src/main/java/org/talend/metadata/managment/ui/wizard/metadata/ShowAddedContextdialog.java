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
package org.talend.metadata.managment.ui.wizard.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.repository.UpdateRepositoryUtils;

/**
 * ggu class global comment. Detailled comment
 */
public class ShowAddedContextdialog extends SelectionDialog {

    private static final String TITILE = Messages.getString("ShowAddedContextdialog.Title"); //$NON-NLS-1$

    private final List<AddedContextBean> addedVarBeans = new ArrayList<AddedContextBean>();

    public ShowAddedContextdialog(Map<String, Set<String>> addedVarsMap) {
        this(addedVarsMap, false);
    }

    public ShowAddedContextdialog(Map<String, Set<String>> addedVarsMap, boolean isIdKey) {
        super(PlatformUI.getWorkbench().getDisplay().getActiveShell());
        Assert.isNotNull(addedVarsMap);
        for (String idOrLabel : addedVarsMap.keySet()) {
            addedVarBeans.add(new AddedContextBean(idOrLabel, addedVarsMap.get(idOrLabel), isIdKey));
        }
        init();
    }

    private void init() {
        setShellStyle(getShellStyle() | SWT.RESIZE);
        setBlockOnOpen(true);
        // setDefaultImage(ImageProvider.getImage(ECoreImage.CONTEXT_ICON));
        setTitle(TITILE);
        setMessage(Messages.getString("ShowAddedContextdialog.Messages")); //$NON-NLS-1$
        setHelpAvailable(false);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setImage(ImageProvider.getImage(ECoreImage.CONTEXT_ICON));
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setFont(parent.getFont());
        createMessageArea(composite);

        //
        Group inner = new Group(composite, SWT.NONE);
        inner.setText(Messages.getString("ShowAddedContextdialog.Variables")); //$NON-NLS-1$
        inner.setFont(composite.getFont());
        inner.setLayout(new GridLayout());
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumHeight = 150;
        gridData.minimumWidth = 200;
        inner.setLayoutData(gridData);

        TreeViewer treeViewer = new TreeViewer(inner);
        treeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        Tree tree = treeViewer.getTree();
        tree.setHeaderVisible(false);
        tree.setLinesVisible(false);

        treeViewer.setContentProvider(new AddedContextProvider());
        treeViewer.setLabelProvider(new AddedContextProvider());
        treeViewer.setInput(addedVarBeans);

        treeViewer.expandAll();

        return composite;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.YES_ID == buttonId) {
            okPressed();
        } else if (IDialogConstants.NO_ID == buttonId) {
            cancelPressed();
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.YES_ID, IDialogConstants.YES_LABEL, true);
        createButton(parent, IDialogConstants.NO_ID, IDialogConstants.NO_LABEL, false);
    }

    /**
     * 
     * ggu AddedContextBean class global comment. Detailled comment
     */
    class AddedContextBean {

        String idOrLabel;

        Set<String> vars;

        boolean isIdKey;

        AddedContextBean(String idOrLabel, Set<String> vars, boolean isIdKey) {
            super();
            this.idOrLabel = idOrLabel;
            this.vars = vars;
            this.isIdKey = isIdKey;
        }

        public String getIdOrLabel() {
            return this.idOrLabel;
        }

        public Set<String> getVars() {
            return this.vars;
        }

        public boolean isIdKey() {
            return this.isIdKey;
        }

    }

    /**
     * 
     * ggu AddedContextTreeContentProvider class global comment. Detailled comment
     */
    class AddedContextProvider extends ArrayContentProvider implements ITreeContentProvider, ITableLabelProvider, ILabelProvider {

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof AddedContextBean) {
                return ((AddedContextBean) parentElement).getVars().toArray();
            }
            return null;
        }

        @Override
        public Object getParent(Object element) {
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            return getChildren(element) != null;
        }

        @Override
        public String getText(Object element) {
            return getColumnText(element, 0);
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof AddedContextBean) {
                AddedContextBean element2 = (AddedContextBean) element;
                if (element2.isIdKey()) {
                    ConnectionItem connItem = UpdateRepositoryUtils.getConnectionItemByItemId(element2.getIdOrLabel());
                    if (connItem != null && connItem.getConnection().isContextMode()) {
                        ContextItem item = ContextUtils.getContextItemById(connItem.getConnection().getContextId());
                        if (item != null) {
                            return item.getProperty().getLabel();
                        }
                    }
                }
                return element2.getIdOrLabel();
            } else if (element instanceof String) {
                return ((String) element);
            }
            return null;
        }

        @Override
        public Image getImage(Object element) {
            return getColumnImage(element, 0);
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public void addListener(ILabelProviderListener listener) {
        }

        @Override
        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        @Override
        public void removeListener(ILabelProviderListener listener) {

        }

        @Override
        public void dispose() {
            super.dispose();
        }

    }

}
