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
package org.talend.rcp.intro;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.ShowViewDialog;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.views.IViewDescriptor;
import org.talend.rcp.i18n.Messages;

/**
 * Displays a window for view selection. <br/>
 * 
 * $Id$
 * 
 */
public class ShowViewAction extends Action {

    private static final String ACTION_ID = "org.talend.rcp.intro.ShowViewAction"; //$NON-NLS-1$

    /**
     * Constructs a new ShowViewAction.
     */
    public ShowViewAction() {
        super(Messages.getString("ShowViewAction.actionLabel")); //$NON-NLS-1$
        setId(ACTION_ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IWorkbenchPage page = window.getActivePage();
        if (page == null) {
            return;
        }

        final ShowViewDialog dialog = new ShowViewDialog(window, WorkbenchPlugin.getDefault().getViewRegistry()) {

            @Override
            protected Control createDialogArea(Composite parent) {
                Control control = super.createDialogArea(parent);
                // TODO
                // 1) get tree
                // 2) get keyUp/KeyDown listener.
                // 3) remove listener.
                Control[] com = ((Composite) control).getChildren();
                for (Control control2 : com) {
                    if (control2 instanceof Label) {
                        ((Label) control2).setText("");
                    }
                    if (control2 instanceof FilteredTree) {
                        Tree tree = ((FilteredTree) control2).getViewer().getTree();
                        Listener[] listenerDown = tree.getListeners(SWT.KeyDown);
                        Listener[] listeberUp = tree.getListeners(SWT.KeyUp);
                        for (Listener element : listenerDown) {
                            if (element instanceof TypedListener) {
                                if (((TypedListener) element).getEventListener() instanceof KeyListener) {
                                    KeyListener keyLis = (KeyListener) ((TypedListener) element).getEventListener();
                                    tree.removeKeyListener(keyLis);
                                }
                            }

                        }
                        for (Listener element : listeberUp) {
                            if (element instanceof TypedListener) {
                                if (((TypedListener) element).getEventListener() instanceof KeyListener) {
                                    KeyListener keyLis = (KeyListener) ((TypedListener) element).getEventListener();
                                    tree.removeKeyListener(keyLis);
                                }
                            }
                        }
                    }

                }
                return control;
            }

        };

        dialog.open();
        if (dialog.getReturnCode() == Window.CANCEL) {
            return;
        }
        final IViewDescriptor[] descriptors = dialog.getSelection();
        for (IViewDescriptor descriptor : descriptors) {
            try {
                boolean viewExist = true;
                if (page instanceof WorkbenchPage) {
                    List<MUIElement> elementList = getMUIElement(descriptor.getId(),
                            ((WorkbenchPage) page).getCurrentPerspective());
                    if (elementList.isEmpty()) {
                        viewExist = false;
                    }
                }
                IViewPart viewPart = page.showView(descriptor.getId());
                if (!viewExist) {
                    openViewInBottom(viewPart, page);
                    page.activate(viewPart);
                }
            } catch (PartInitException e) {
                //                StatusUtil.handleStatus(e.getStatus(), WorkbenchMessages.ShowView_errorTitle + ": " + e.getMessage(), //$NON-NLS-1$
                // StatusManager.SHOW);
                IStatus istatus = e.getStatus();
                StatusManager.getManager().handle(
                        new Status(istatus.getSeverity(), istatus.getPlugin(), istatus.getCode(),
                                Messages.getString("WorkbenchMessages.ShowView_errorTitle") + ": " + e.getMessage(), //$NON-NLS-1$ //$NON-NLS-2$
                                istatus.getException()), StatusManager.SHOW);
            }
        }
    }

    private boolean openViewInBottom(IViewPart viewPart, IWorkbenchPage workbenchPage) {
        if (!(workbenchPage instanceof WorkbenchPage)) {
            return false;
        }
        MPart part = ((WorkbenchPage) workbenchPage).findPart(viewPart);
        if (part == null || part.getCurSharedRef() == null) {
            return false;
        }
        String folderID = "bottomLayout";//$NON-NLS-1$
        MElementContainer parent = part.getCurSharedRef().getParent();
        if (parent == null || parent.getElementId().equals(folderID)) {
            return false;
        }
        List<MUIElement> elementList = getMUIElement(folderID, ((WorkbenchPage) workbenchPage).getCurrentPerspective());
        if (elementList.isEmpty()) {
            return false;
        }
        MUIElement muiElement = elementList.get(0);
        if (muiElement instanceof MElementContainer) {
            parent.getChildren().remove(part.getCurSharedRef());
            ((MElementContainer) elementList.get(0)).getChildren().add(part.getCurSharedRef());
        }
        return true;
    }

    private List<MUIElement> getMUIElement(String id, MUIElement parent) {
        List<MUIElement> elementList = new ArrayList<MUIElement>();
        if (parent == null) {
            return elementList;
        }
        if (parent instanceof MElementContainer) {
            for (Object object : ((MElementContainer) parent).getChildren()) {
                if (!(object instanceof MUIElement)) {
                    continue;
                }
                MUIElement element = (MUIElement) object;
                if (element.getElementId() != null && element.getElementId().equals(id)) {
                    elementList.add(element);
                    return elementList;
                }
                if ((element instanceof MElementContainer) && !((MElementContainer) element).getChildren().isEmpty()) {
                    elementList.addAll(getMUIElement(id, element));
                }
            }
        }

        return elementList;
    }
}
