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
package org.talend.core.ui.perspective;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.talend.core.ui.CoreUIPlugin;

/**
 * Changes the active perspective. <br/>
 * 
 * $Id: ChangePerspectiveAction.java 1774 2007-02-03 02:05:47 +0000 (Sat, 03 Feb 2007) bqian $
 * 
 */
public class ChangePerspectiveAction extends Action {

    /** Id of the perspective to move to front. */
    private String perspectiveId;

    /**
     * Constructs a new ChangePerspectiveAction.
     */
    public ChangePerspectiveAction(String perspectiveId) {
        super(perspectiveId, AS_CHECK_BOX);

        this.perspectiveId = perspectiveId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        if (!perspectiveId.equals(page.getPerspective().getId())) {
            try {
                workbench.showPerspective(perspectiveId, workbench.getActiveWorkbenchWindow());
            } catch (WorkbenchException e) {
                IStatus status = new Status(IStatus.ERROR, CoreUIPlugin.PLUGIN_ID, IStatus.OK, "Show perspective failed.", e); //$NON-NLS-1$
                CoreUIPlugin.getDefault().getLog().log(status);
            }
        }
    }
}
