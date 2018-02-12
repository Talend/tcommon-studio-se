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
package org.talend.core.ui.context.view;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.talend.core.ui.branding.IBrandingConfiguration;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class Contexts {

    private static String currentTitle = ""; //$NON-NLS-1$

    private static String newTitle = ""; //$NON-NLS-1$

    private static final String PERSPECTIVE_DI_ID = "org.talend.rcp.perspective"; //$NON-NLS-1$ 

    public static void switchToCurContextsView() {
        AbstractContextView cxtView = getViewWithPerspectiveIDs();
        if (cxtView != null) {
            updateTitle(cxtView);
            refreshView(cxtView);
        }
    }

    public static void switchToCurContextsView(final IWorkbenchPart part) {
        final AbstractContextView cxtView = getViewWithPerspectiveIDs();
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (cxtView != null) {
                    updateTitle(cxtView);
                    cxtView.refresh(part);
                }
            }
        });
    }

    private static AbstractContextView getViewWithPerspectiveIDs() {
        Set<String> perspIDs = new HashSet<String>();
        perspIDs.add(PERSPECTIVE_DI_ID);
        return getView(perspIDs, AbstractContextView.CTX_ID_DESIGNER);
    }

    /**
     * DOC xqliu Comment method "updateTitle".
     * 
     * @param cxtView
     */
    private static void updateTitle(AbstractContextView cxtView) {
        if (!newTitle.equals(currentTitle)) {
            cxtView.setPartName(newTitle);
            currentTitle = newTitle;
        }
    }

    public static void switchToCurContextsView(String perspectiveId, String viewId) {
        Set<String> perspIDs = new HashSet<String>();
        perspIDs.add(perspectiveId);
        AbstractContextView cxtView = getView(perspIDs, viewId);

        if (cxtView == null) {
            return;
        }
        updateTitle(cxtView);
        refreshView(cxtView);
    }

    public static void refreshContextsView() {
        Set<String> perspIDs = new HashSet<String>();
        perspIDs.add(PERSPECTIVE_DI_ID);
        AbstractContextView cxtView = getView(perspIDs, AbstractContextView.CTX_ID_DESIGNER);
        if (cxtView != null) {
            refreshView(cxtView);
        } else {
            perspIDs = new HashSet<String>();
            perspIDs.add(IBrandingConfiguration.PERSPECTIVE_DQ_ID);
            cxtView = getView(perspIDs, AbstractContextView.CTX_ID_TDQ);
            if (cxtView != null) {
                refreshView(cxtView);
            } else {
                cxtView = forceGetView(AbstractContextView.CTX_ID_DESIGNER);
                if (cxtView != null) {
                    refreshView(cxtView);
                }
            }
        }
    }

    /**
     * DOC xqliu Comment method "forceRefreshContextsView".
     */
    public static void forceRefreshContextsView() {
        refreshContextsView();
    }

    /**
     * DOC xqliu Comment method "forceGetView".
     * 
     * @param viewId
     * @return
     */
    private static AbstractContextView forceGetView(String viewId) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            IViewPart view = page.findView(viewId);
            if (view == null) {
                try {
                    view = page.showView(viewId);
                } catch (Exception e) {
                    org.talend.commons.exception.ExceptionHandler.process(e);
                }
            }
            if (view instanceof AbstractContextView) {
                return (AbstractContextView) view;
            }
        }
        return null;
    }

    /**
     * qzhang Comment method "refreshView".
     */
    private static AbstractContextView getView(Set<String> perspectiveIds, String viewId) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        // seem 16594
        if (page != null) {
            String perId = page.getPerspective().getId();
            if (StringUtils.isNotEmpty(perId) && perspectiveIds.contains(perId)) {
                IViewPart view = page.findView(viewId);
                // if (view == null) {
                // try {
                // view = page.showView(viewId);
                // } catch (Exception e) {
                // ExceptionHandler.process(e);
                // }
                // }
                if (view instanceof AbstractContextView) {
                    return (AbstractContextView) view;
                }
            }
        }
        return null;

    }

    /**
     * qzhang Comment method "refreshView".
     * 
     * @param view
     */
    private static void refreshView(AbstractContextView view) {
        if (view != null) {
            updateTitle(view);
            view.refresh();
        }
    }

    public static void setTitle(String title) {
        newTitle = title;
    }

    /**
     * qzhang Comment method "clearAll".
     */
    public static void clearAll() {
        Set<String> perspIDs = new HashSet<String>();
        perspIDs.add(PERSPECTIVE_DI_ID);
        AbstractContextView cxtView = getView(perspIDs, AbstractContextView.CTX_ID_DESIGNER);
        refreshView(cxtView);
        if (cxtView != null) {
            cxtView.reset();
        }
    }

}
