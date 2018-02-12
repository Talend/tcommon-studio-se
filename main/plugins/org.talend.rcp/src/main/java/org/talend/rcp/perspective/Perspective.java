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
package org.talend.rcp.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;

/**
 * DOC ccarbone class global comment. Detailed comment <br/>
 * 
 * $Id$
 * 
 */
public class Perspective implements IPerspectiveFactory {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout(IPageLayout layout) {
        IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
        service.getBrandingConfiguration().initPerspective(layout);

        // TDI-17770 Need to fix problem of refresh of repository view when use Talend product as plugin.
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new PerspectiveAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.ui.IPerspectiveListener#perspectiveActivated(org.eclipse.ui.IWorkbenchPage,
             * org.eclipse.ui.IPerspectiveDescriptor)
             */
            @Override
            public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
                String pId = perspective.getId();
                // TDI-21143 : Studio repository view : remove all refresh call to repo view
                // IRepositoryView view = RepositoryManager.getRepositoryView();
                // if (view != null) {
                // if (IBrandingConfiguration.PERSPECTIVE_DI_ID.equals(pId)
                // || IBrandingConfiguration.PERSPECTIVE_CAMEL_ID.equals(pId)) {
                // view.refresh();
                // }
                // }
            }
        });
    }
}
