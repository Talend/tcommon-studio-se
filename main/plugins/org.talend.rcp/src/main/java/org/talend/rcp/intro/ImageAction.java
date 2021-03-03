// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.service.IExchangeService;
import org.talend.core.service.ITutorialsService;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.rcp.Activator;
import org.talend.rcp.intro.linksbar.LinksToolbarItem;

public class ImageAction extends Action {

    private final IWorkbenchWindow window;

    private String url;

    public ImageAction(IWorkbenchWindow window, String imagePath, String url, String tipText) {
        this.window = window;
        this.url = url;
        ImageDescriptor imageDescriptorFromPlugin = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imagePath);
        setImageDescriptor(imageDescriptorFromPlugin);
        setToolTipText(tipText);
    }

    @Override
    public void run() {
        if (window != null) {
            if (StringUtils.equals(LinksToolbarItem.EXCHANGE_ORIG_URL, url)) {
                IExchangeService service = GlobalServiceRegister.getDefault().getService(IExchangeService.class);
                service.openExchangeEditor();
            } else if (StringUtils.equals(LinksToolbarItem.VIDEOS_ORIG_URL, url)) {
                ITutorialsService service = GlobalServiceRegister.getDefault().getService(ITutorialsService.class);
                service.openTutorialsDialog();
            } else {
                if (!PluginChecker.isTIS() && StringUtils.equals(LinksToolbarItem.CLOUD_ORIG_URL, url)
                        && GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
                    IBrandingService brandingService = GlobalServiceRegister.getDefault().getService(IBrandingService.class);
                    String edition = brandingService.getAcronym();
                    this.url = this.url.replace("dynamic_acronym", edition);//$NON-NLS-1$
                }
                openBrower(url);
            }
        }
    }

    protected void openBrower(String url) {
        Program.launch(url);
    }
}
