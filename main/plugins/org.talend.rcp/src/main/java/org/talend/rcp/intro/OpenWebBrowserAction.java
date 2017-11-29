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

import java.net.URLDecoder;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class OpenWebBrowserAction implements IIntroAction {

    private static final String USERGUIDE_URL = "https://www.talend.com/download/data-integration?qt-product_download_tabs=2#qt-product_download_tabs"; //$NON-NLS-1$

    private static final String REFERENCEGUIDE_URL = "https://www.talend.com/download/data-integration?qt-product_download_tabs=2#qt-product_download_tabs"; //$NON-NLS-1$

    private static final String TUTORIALS_URL = "https://www.talendforge.org/tutorials"; //$NON-NLS-1$

    private static final String FORUMS_URL = "https://community.talend.com"; //$NON-NLS-1$

    private static final String TRAINNING_URL = "https://www.talend.com/training/talend-on-demand-training"; //$NON-NLS-1$

    private static final String TOS_DI = "https://www.talend.com/download/data-integration?qt-product_download_tabs=2#qt-product_download_tabs"; //$NON-NLS-1$

    private static final String TOS_DQ = "https://www.talend.com/download/data-quality?qt-product_download_tabs=2#qt-product_download_tabs"; //$NON-NLS-1$

    private static final String TOS_ESB = "https://www.talend.com/download/esb?qt-product_download_tabs=2#qt-product_download_tabs"; //$NON-NLS-1$

    private static final String TOS_MDM = "https://www.talend.com/download/mdm?qt-product_download_tabs=2#qt-product_download_tabs"; //$NON-NLS-1$

    private static final String TOS_BD = "https://www.talend.com/download/big-data?qt-product_download_tabs=2#qt-product_download_tabs"; //$NON-NLS-1$

    private static final String TALEND_HELP_CENTER = "https://help.talend.com";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    @Override
    public void run(IIntroSite site, Properties params) {
        if (params == null) {
            return;
        }
        Object type = params.get("type");
        if (type != null) {
            if ("showUserGuide".equals(type.toString())) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
                    IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                            IBrandingService.class);
                    String userManuals = brandingService.getUserManuals();
                    if ("DI".equals(userManuals)) {
                        Program.launch(TOS_DI);
                    } else if ("DQ".equals(userManuals)) {
                        Program.launch(TOS_DQ);
                    } else if ("ESB".equals(userManuals)) {
                        Program.launch(TOS_ESB);
                    } else if ("MDM".equals(userManuals)) {
                        Program.launch(TOS_MDM);
                    } else if ("BD".equals(userManuals)) {
                        Program.launch(TOS_BD);
                    }
                }
            } else if ("showReferenceGuide".equals(type.toString())) {
                Program.launch(REFERENCEGUIDE_URL);
            } else if ("showTutorials".equals(type.toString())) {
                Program.launch(TUTORIALS_URL);
            } else if ("showForums".equals(type.toString())) {
                Program.launch(FORUMS_URL);
            } else if ("showTrainning".equals(type.toString())) {
                Program.launch(TRAINNING_URL);
            } else if ("showTalendHelpCenter".equals(type.toString())) {
                Program.launch(TALEND_HELP_CENTER);
            } else if ("showUrl".equals(type.toString())) { //$NON-NLS-1$
                String url = (String) params.get("url"); //$NON-NLS-1$
                if (StringUtils.isNotEmpty(url)) {
                    try {
                        url = URLDecoder.decode(url, "UTF-8"); //$NON-NLS-1$
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                    Program.launch(url);
                }
            }
        }
    }
}
