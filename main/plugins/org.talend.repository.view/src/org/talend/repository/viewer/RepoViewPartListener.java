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
package org.talend.repository.viewer;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.repository.navigator.RepoViewCommonNavigator;
import org.talend.repository.ui.views.IJobSettingsView;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class RepoViewPartListener implements IPartListener2 {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partActivated(IWorkbenchPartReference partRef) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partBroughtToTop(IWorkbenchPartReference partRef) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
        if (partRef != null && IRepositoryView.VIEW_ID.equals(partRef.getId())) {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().removePartListener(this);
            if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null) {
                IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .findView(IJobSettingsView.ID);
                if (viewPart != null && viewPart instanceof ISelectionChangedListener) {
                    ISelectionChangedListener listener = (ISelectionChangedListener) viewPart;
                    IWorkbenchPart part = partRef.getPart(false);
                    if (part instanceof CommonNavigator) {
                        CommonViewer repViewer = ((CommonNavigator) part).getCommonViewer();
                        if (((RepoViewCommonNavigator) repViewer.getCommonNavigator()).getViewer() != null) {
                            ((RepoViewCommonNavigator) repViewer.getCommonNavigator()).getViewer()
                                    .removeSelectionChangedListener(listener);
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partDeactivated(IWorkbenchPartReference partRef) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partOpened(IWorkbenchPartReference partRef) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partHidden(IWorkbenchPartReference partRef) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        refreshRepViewDescription(partRef);
        if (partRef != null && IRepositoryView.VIEW_ID.equals(partRef.getId())) {
            IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .findView(IJobSettingsView.ID);
            if (viewPart != null && viewPart instanceof ISelectionChangedListener) {
                ISelectionChangedListener listener = (ISelectionChangedListener) viewPart;
                IWorkbenchPart part = partRef.getPart(false);
                if (part instanceof CommonNavigator) {
                    CommonViewer repViewer = ((CommonNavigator) part).getCommonViewer();
                    if (((RepoViewCommonNavigator) repViewer.getCommonNavigator()).getViewer() != null) {
                        ((RepoViewCommonNavigator) repViewer.getCommonNavigator()).getViewer().addSelectionChangedListener(
                                listener);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.IWorkbenchPartReference)
     */
    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {

    }

    private void refreshRepViewDescription(IWorkbenchPartReference curPartRef) {
        if (curPartRef != null && IRepositoryView.VIEW_ID.equals(curPartRef.getId())) {
            IWorkbenchPart part = curPartRef.getPart(false);
            if (part instanceof CommonNavigator) {
                CommonViewer repViewer = ((CommonNavigator) part).getCommonViewer();

                if (repViewer.getCommonNavigator() instanceof RepoViewCommonNavigator) {
                    ((RepoViewCommonNavigator) repViewer.getCommonNavigator()).refreshContentDescription();
                }
            }
        }
    }
}
