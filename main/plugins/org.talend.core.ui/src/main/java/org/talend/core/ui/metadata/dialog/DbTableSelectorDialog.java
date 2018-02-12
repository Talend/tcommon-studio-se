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
package org.talend.core.ui.metadata.dialog;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.metadata.dialog.DbTableSelectorObject.ObjectType;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class DbTableSelectorDialog extends Dialog {

    private static Image dbImage = ImageProvider.getImage(CoreUIPlugin.getImageDescriptor("/icons/database/connection.png")); //$NON-NLS-1$

    private static Image columnImage = ImageProvider.getImage(CoreUIPlugin.getImageDescriptor("/icons/database/columns.gif")); //$NON-NLS-1$

    private static Image tableImage = ImageProvider.getImage(CoreUIPlugin.getImageDescriptor("/icons/database/table.gif")); //$NON-NLS-1$

    private final DbTableSelectorObject object;

    private String selectName;

    private TreeViewer viewer;

    /**
     * qzhang DbTableSelectorDialog constructor comment.
     * 
     * @param parentShell
     * @param object
     */
    public DbTableSelectorDialog(Shell parentShell, DbTableSelectorObject object) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        this.object = object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("DbTableSelectorDialog.textContent")); //$NON-NLS-1$
        newShell.setSize(400, 300);
        newShell.setLocation(getParentShell().getLocation().x + getParentShell().getSize().x / 2,
                getParentShell().getLocation().y + getParentShell().getSize().y / 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout();
        // layout.marginBottom = 0;
        // layout.marginHeight = 0;
        // layout.marginLeft = 0;
        // layout.marginRight = 0;
        // layout.marginTop = 0;
        // layout.marginWidth = 0;

        container.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        container.setLayoutData(gridData);
        viewer = new TreeViewer(container, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        gridData = new GridData(GridData.FILL_BOTH);
        viewer.getTree().setLayoutData(gridData);
        final ListProvider listProvider = new ListProvider();
        viewer.setLabelProvider(listProvider);
        viewer.setContentProvider(listProvider);
        viewer.setInput(object);

        viewer.setSorter(new ViewerSorter() {

            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                if ((e1 instanceof DbTableSelectorObject) && (e2 instanceof DbTableSelectorObject)) {
                    DbTableSelectorObject left = (DbTableSelectorObject) e1;
                    DbTableSelectorObject right = (DbTableSelectorObject) e2;
                    int result = left.getLabel().compareToIgnoreCase(right.getLabel());
                    return result;
                }
                return super.compare(viewer, e1, e2);
            }

        });

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            /*
             * (non-Javadoc)
             * 
             * @seeorg.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.
             * SelectionChangedEvent)
             */
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection() instanceof StructuredSelection) {
                    StructuredSelection selection = (StructuredSelection) event.getSelection();
                    Object firstElement = selection.getFirstElement();
                    if (firstElement instanceof DbTableSelectorObject) {
                        final DbTableSelectorObject dbTableSelectorObject = ((DbTableSelectorObject) firstElement);
                        if (dbTableSelectorObject.getType().compareTo(ObjectType.TABLE) == 0) {
                            setSelectName(dbTableSelectorObject.getLabel());
                            getButton(OK).setEnabled(true);
                        } else {
                            getButton(OK).setEnabled(false);
                        }
                    }
                }
            }

        });
        return container;
    }

    /**
     * qzhang DbTableSelectorDialog class global comment. Detailled comment <br/>
     * 
     */
    private class ListProvider extends LabelProvider implements IStructuredContentProvider, ITreeContentProvider {

        @Override
        public Image getImage(Object element) {
            Image image = super.getImage(element);
            if (element instanceof DbTableSelectorObject) {
                DbTableSelectorObject obj = (DbTableSelectorObject) element;
                ObjectType type = obj.getType();
                switch (type) {
                case COLUMN:
                    image = columnImage;
                    break;
                case DB:
                    image = dbImage;
                    break;
                case TABLE:
                    image = tableImage;
                    break;
                default:
                    break;
                }
            }
            return image;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(Object element) {
            if (element instanceof DbTableSelectorObject) {
                return ((DbTableSelectorObject) element).getLabel();
            }
            return super.getText(element);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        @Override
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof DbTableSelectorObject) {
                final List<DbTableSelectorObject> children = ((DbTableSelectorObject) inputElement).getChildren();
                if (children != null) {
                    return children.toArray(new Object[0]);
                }
            }
            return Collections.EMPTY_LIST.toArray();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        @Override
        public Object[] getChildren(Object parentElement) {
            return getElements(parentElement);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        @Override
        public Object getParent(Object element) {
            if (element instanceof DbTableSelectorObject) {
                DbTableSelectorObject parent = (object).getParent();
                if (parent != null) {
                    return parent;
                }
            }
            return object;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        @Override
        public boolean hasChildren(Object element) {
            return getElements(element).length > 0;
        }
    }

    /**
     * qzhang Comment method "getSelectName".
     * 
     * @return
     */
    public String getSelectName() {
        return this.selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

}
