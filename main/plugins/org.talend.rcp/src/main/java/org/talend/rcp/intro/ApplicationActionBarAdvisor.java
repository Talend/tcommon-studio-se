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

import org.apache.commons.lang.IllegalClassException;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.rcp.i18n.Messages;
import org.talend.rcp.intro.linksbar.LinkToolbarLabel;
import org.talend.rcp.intro.linksbar.LinksToolbarItem;

/**
 * DOC ccarbone class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction introAction;

    private IWorkbenchWindow window;

    private final IActionBarConfigurer actionBarConfigurer;

    private IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);

    ActionBarBuildHelper helper;
    
	private ImageAction learnImageAction;
	private ImageAction askImageAction;
	private ImageAction exchangeImageAction;
	private ImageAction videoImageAction;
	private ImageAction cloudImageAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
        actionBarConfigurer = configurer;
        helper = (ActionBarBuildHelper) service.getBrandingConfiguration().getHelper();
        if (helper == null) {
            helper = new ActionBarBuildHelper();
            service.getBrandingConfiguration().setHelper(helper);
        }
        // check
        if (!(helper instanceof ActionBarBuildHelper)) {
            throw new IllegalClassException(Messages.getString("ApplicationActionBarAdvisor.IllegalClass")); //$NON-NLS-1$
        }
        helper.setActionBarConfigurer(configurer);
    }

    public static final IExtensionPointLimiter GLOBAL_ACTIONS = new ExtensionPointLimiterImpl("org.talend.core.global_actions", //$NON-NLS-1$
            "GlobalAction"); //$NON-NLS-1$

    @Override
    protected void makeActions(final IWorkbenchWindow myWindow) {
        this.window = myWindow;
        helper.setWindow(window);
        introAction = ActionFactory.INTRO.create(myWindow);
        register(introAction);
        CloseIntroAction action = new CloseIntroAction();
        register(action);
		learnImageAction = new ImageAction(myWindow, "icons/demo.png", LinksToolbarItem.LEARN_ORIG_URL,
				"LinksToolbarItem_Learn");
		register(learnImageAction);
		askImageAction = new ImageAction(myWindow, "icons/irc_protocol.png", LinksToolbarItem.ASK_ORIG_URL,
				"LinksToolbarItem_7");
		register(askImageAction);
		exchangeImageAction = new ImageAction(myWindow, "icons/exchange_view.png", LinksToolbarItem.EXCHANGE_ORIG_URL,
				"LinksToolbarItem_exchange");
		register(exchangeImageAction);
		videoImageAction = new ImageAction(myWindow, "icons/videos_icon16x16.png", LinksToolbarItem.VIDEOS_ORIG_URL,
				"LinksToolbarItem_videos");
		register(videoImageAction);
		if (!PluginChecker.isTIS()) {
			cloudImageAction = new ImageAction(myWindow, "icons/cloud.png", LinksToolbarItem.CLOUD_ORIG_URL,
					"LinksToolbarItem_cloud");
			register(cloudImageAction);
		}
        registerGlobalActions();
    }

    private void registerGlobalActions() {
        actionBarConfigurer.registerGlobalAction(ActionFactory.SAVE.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.SAVE_ALL.create(window));// add for bug TDI-20305
        actionBarConfigurer.registerGlobalAction(ActionFactory.UNDO.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.REDO.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.CUT.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.COPY.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.PASTE.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.DELETE.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.SELECT_ALL.create(window));
        actionBarConfigurer.registerGlobalAction(ActionFactory.REFRESH.create(window));
        // MOD mzhao 2009-06-24 feature 7673
        actionBarConfigurer.registerGlobalAction(ActionFactory.RESET_PERSPECTIVE.create(window));
    }

    // private static final String[] ACTIONSETID = new String[] {
    //            "org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo", //$NON-NLS-1$
    //            "org.eclipse.ui.edit.text.actionSet.annotationNavigation", "org.eclipse.ui.NavigateActionSet", //$NON-NLS-1$ //$NON-NLS-2$
    //            "org.eclipse.ui.WorkingSetActionSet", "org.eclipse.ui.edit.text.actionSet.navigation", //$NON-NLS-1$ //$NON-NLS-2$
    //            "org.eclipse.search.searchActionSet", "org.eclipse.ui.externaltools.ExternalToolsSet", "org.talend.repository.bootTalendActionSet" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    @Override
    protected void fillMenuBar(final IMenuManager menuBar) {
        helper.fillMenuBar(menuBar);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface. action.ICoolBarManager)
     */
    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
        helper.fillCoolBar(coolBar);
		IToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		toolBarManager.add(learnImageAction);
		toolBarManager.add(new LinkToolbarLabel(LinksToolbarItem.LEARN_URL, "LinksToolbarItem_Learn"));
		toolBarManager.add(askImageAction);
		toolBarManager.add(new LinkToolbarLabel(LinksToolbarItem.ASK_URL, "LinksToolbarItem_7"));
		toolBarManager.add(exchangeImageAction);
		toolBarManager.add(new LinkToolbarLabel(LinksToolbarItem.EXCHANGE_URL, "LinksToolbarItem_exchange"));
		toolBarManager.add(videoImageAction);
		toolBarManager.add(new LinkToolbarLabel(LinksToolbarItem.VIDEOS_URL, "LinksToolbarItem_videos"));
		if (!PluginChecker.isTIS()) {
			toolBarManager.add(cloudImageAction);
			toolBarManager.add(new LinkToolbarLabel(LinksToolbarItem.CLOUD_URL, "LinksToolbarItem_cloud"));
		}
		coolBar.add(new ToolBarContributionItem(toolBarManager, LinksToolbarItem.COOLITEM_LINKS_ID));
	}

    public ActionBarBuildHelper getHelper() {
        return this.helper;
    }

}
