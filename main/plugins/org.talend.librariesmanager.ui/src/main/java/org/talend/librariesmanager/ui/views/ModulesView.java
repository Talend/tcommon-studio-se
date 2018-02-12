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
package org.talend.librariesmanager.ui.views;

import java.util.List;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.commands.ActionHandler;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.talend.core.model.general.ILibrariesService.IChangedLibrariesListener;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.actions.CheckModulesAction;
import org.talend.librariesmanager.ui.actions.ExportCustomSettingsAction;
import org.talend.librariesmanager.ui.actions.ImportCustomSettingsAction;
import org.talend.librariesmanager.ui.actions.ImportExternalJarAction;
import org.talend.librariesmanager.ui.actions.RemoveExternalJarAction;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ModulesView.java 1811 2007-02-05 03:29:11Z qzhang $
 * 
 */
public class ModulesView extends ViewPart {

    private CheckModulesAction checkAction;

    private ModulesViewComposite modulesViewComposite;

    private IChangedLibrariesListener changedLibrariesListener;

    /**
     * Ask the modules view composite to refresh its content.
     * 
     * yzhang Comment method "refresh".
     */
    public void refresh() {
        modulesViewComposite.refresh();
    }

    /*
     * Creat the content of this view.
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        // If the content of this view need to display libries of Java, a new composite for it should be developed. And
        // replace the PerlModulesViewComposite here.

        modulesViewComposite = new ModulesViewComposite(parent);
        makeActions();
        contributeToActionBars();
        modulesViewComposite.refresh();

        changedLibrariesListener = new IChangedLibrariesListener() {

            @Override
            public void afterChangingLibraries() {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        modulesViewComposite.refresh();
                    }
                });
            }
        };
        LibManagerUiPlugin.getDefault().getLibrariesService().addChangeLibrariesListener(changedLibrariesListener);

    }

    @Override
    public void dispose() {
        if (changedLibrariesListener != null) {
            LibManagerUiPlugin.getDefault().getLibrariesService().removeChangeLibrariesListener(changedLibrariesListener);
        }
        super.dispose();
    }

    /*
     * Set focus on the composite within this view.
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        if (this.modulesViewComposite == null) {
            return;
        }
        this.modulesViewComposite.setFocus();
    }

    private void makeActions() {
        checkAction = new CheckModulesAction();

        IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);

        IHandler handler1 = new ActionHandler(checkAction);
        handlerService.activateHandler(checkAction.getActionDefinitionId(), handler1);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(checkAction);
        ImportCustomSettingsAction importSettingAction = new ImportCustomSettingsAction();
        manager.add(importSettingAction);
        ExportCustomSettingsAction exportSettingAction = new ExportCustomSettingsAction();
        manager.add(exportSettingAction);
        RemoveExternalJarAction removeAction = new RemoveExternalJarAction();
        manager.add(removeAction);
        ImportExternalJarAction importAction = new ImportExternalJarAction();
        manager.add(importAction);
        return;
    }

    public void selectUninstalledItem(String componentName, List<String> modulesName) {
        if (this.modulesViewComposite == null) {
            return;
        }
        if (componentName == null) {
            return;
        }
        TableItem[] items = modulesViewComposite.getTableViewerCreator().getTable().getItems();
        for (int i = 0; i < items.length; i++) {
            TableItem item = items[i];
            Object obj = item.getData();
            if (obj instanceof ModuleNeeded) {
                for (String mName : modulesName) {
                    if (((ModuleNeeded) obj).getContext().equals(componentName)
                            && ((ModuleNeeded) obj).getModuleName().equals(mName)) {
                        // if (i == 0) {
                        modulesViewComposite.getTableViewerCreator().getTable().setSelection(i);
                        // }
                        // item.setBackground(new Color(Display.getDefault(), new RGB(255, 102, 102)));
                    }
                }
            }
        }
    }
}
