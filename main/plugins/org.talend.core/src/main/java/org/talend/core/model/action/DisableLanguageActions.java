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
package org.talend.core.model.action;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.Project;

/**
 * Use to disabled some actions following the project language.<br/>
 * 
 */
public class DisableLanguageActions implements IStartup {

    public static boolean exectued;

    public void earlyStartup() {
        List<String> ids = null;
        RepositoryContext context = (RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (context != null) {
            Project project = context.getProject();
        } else {
            return;
        }

        ECodeLanguage lan = LanguageManager.getCurrentLanguage();

        switch (lan) {
        case JAVA:
            ids = Arrays.asList(new String[] { "org.talend.help.perl.OpenPerlHelpAction" }); //$NON-NLS-1$
            break;
        case PERL:
            ids = null;
            break;
        }

        if (ids != null && !ids.isEmpty()) {
            final String[] toolsID = ids.toArray(new String[] {});
            final IWorkbench workbench = PlatformUI.getWorkbench();
            workbench.getDisplay().asyncExec(new Runnable() {

                public void run() {
                    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                    if (window != null) {
                        Menu menubar = window.getShell().getMenuBar();
                        MenuItem[] mItems = menubar.getItems();

                        for (MenuItem item : mItems) {
                            Object menuItemData = item.getData();
                            if (menuItemData == null) {
                                continue;
                            }

                            if (menuItemData instanceof MenuManager) {
                                MenuManager menumanager = (MenuManager) menuItemData;
                                IContributionItem[] items2 = menumanager.getItems();
                                for (IContributionItem contributionItem : items2) {
                                    disableActionFRomIContributionItem(toolsID, contributionItem);
                                }
                            } else if (menuItemData instanceof IContributionItem) {
                                disableActionFRomIContributionItem(toolsID, (IContributionItem) menuItemData);
                            }
                        }
                    }
                }

                /**
                 * DOC smallet Comment method "disableActionFRomIContributionItem".
                 * 
                 * @param toolsID
                 * @param contributionItem
                 */
                private void disableActionFRomIContributionItem(final String[] toolsID, IContributionItem contributionItem) {
                    SubContributionItem cItem;
                    if (contributionItem instanceof SubContributionItem) {
                        cItem = (SubContributionItem) contributionItem;
                        IContributionItem conItems = cItem.getInnerItem();
                        if (conItems instanceof ActionContributionItem) {
                            ActionContributionItem items = (ActionContributionItem) conItems;
                            String id = cItem.getId();
                            if ((id != null) && (Arrays.binarySearch(toolsID, id) >= 0)) {
                                items.getAction().setEnabled(false);
                            }
                        }
                    }
                }
            });

            exectued = true;
        }
    }
}
