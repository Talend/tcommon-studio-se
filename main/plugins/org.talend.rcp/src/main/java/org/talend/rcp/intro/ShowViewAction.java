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
package org.talend.rcp.intro;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.dialogs.ShowViewDialog;
import org.eclipse.ui.statushandlers.StatusManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.rcp.i18n.Messages;

/**
 * Displays a window for view selection. <br/>
 * 
 * $Id$
 * 
 */
public class ShowViewAction extends Action {

    private static final String ACTION_ID = "org.talend.rcp.intro.ShowViewAction"; //$NON-NLS-1$

    private IEclipseContext activeContext;

    private Params params;

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

        Params parameters = getParams();
        final ShowViewDialog dialog = new ShowViewDialog(window.getShell(), parameters.getApplication(), parameters.getWindow(),
                parameters.getModelService(), parameters.getPartService(), getEclipseContext()) {

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
        final MPartDescriptor[] descriptors = dialog.getSelection();
        for (MPartDescriptor descriptor : descriptors) {
            try {
                boolean viewExist = true;
                if (page instanceof WorkbenchPage) {
                    List<MUIElement> elementList = getMUIElement(descriptor.getElementId(),
                            ((WorkbenchPage) page).getCurrentPerspective());
                    if (elementList.isEmpty()) {
                        viewExist = false;
                    }
                }
                IViewPart viewPart = page.showView(descriptor.getElementId());
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
    
    private Params getParams() {
        if (params == null) {
            try {
                params = new Params();
                ContextInjectionFactory.inject(params, getEclipseContext());
                // params = ContextInjectionFactory.make(Params.class, getEclipseContext());
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return params;
    }

    private IEclipseContext getEclipseContext() {
        if (activeContext == null) {
            IWorkbench workbench = PlatformUI.getWorkbench();
            activeContext = ((IEclipseContext) workbench.getActiveWorkbenchWindow().getService(IEclipseContext.class))
                    .getActiveLeaf();
        }
        return activeContext;
    }

    private static class Params {

        @Inject
        private MApplication application;

        @Inject
        private MWindow window;

        @Inject
        private EModelService modelService;

        @Inject
        private EPartService partService;

        public MApplication getApplication() {
            return application;
        }

        public void setApplication(MApplication application) {
            this.application = application;
        }

        public MWindow getWindow() {
            return window;
        }

        public void setWindow(MWindow window) {
            this.window = window;
        }

        public EModelService getModelService() {
            return modelService;
        }

        public void setModelService(EModelService modelService) {
            this.modelService = modelService;
        }

        public EPartService getPartService() {
            return partService;
        }

        public void setPartService(EPartService partService) {
            this.partService = partService;
        }

    }
}
