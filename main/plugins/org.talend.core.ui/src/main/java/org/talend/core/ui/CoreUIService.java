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
package org.talend.core.ui;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.service.ICoreUIService;
import org.talend.core.ui.actions.ActionsHelper;
import org.talend.core.ui.component.ComponentPaletteUtilities;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.core.ui.services.IRulesProviderService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class CoreUIService implements ICoreUIService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.ICoreUIService#updatePalette()
     */
    @Override
    public void updatePalette() {
        ComponentPaletteUtilities.updatePalette(false);
    }

    @Override
    public void deleteJobletConfigurationsFromPalette(String jobletName) {
        IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
        if (designerCoreUIService != null) {
            designerCoreUIService.deleteJobletConfigurationsFromPalette(jobletName);
        }
    }

    @Override
    public IPreferenceStore getPreferenceStore() {
        return CoreUIPlugin.getDefault().getPreferenceStore();
    }

    @Override
    public MenuManager[] getRepositoryContextualsActionGroups() {
        return ActionsHelper.getRepositoryContextualsActionGroups();
    }

    @Override
    public List<ITreeContextualAction> getRepositoryContextualsActions() {
        return ActionsHelper.getRepositoryContextualsActions();
    }

    @Override
    public ITreeContextualAction getActionById(String id) {
        return ActionsHelper.getActionById(id);
    }

    @Override
    public void componentsReset() {
        ComponentsFactoryProvider.getInstance().reset();
    }

    @Override
    public void initializeComponents(IProgressMonitor monitor) {
        // second parameter to say only during login
        ComponentsFactoryProvider.getInstance().initializeComponents(monitor, true);
    }

    @Override
    public void syncAllRules() {
        if (PluginChecker.isRulesPluginLoaded()
                && GlobalServiceRegister.getDefault().isServiceRegistered(IRulesProviderService.class)) {
            IRulesProviderService rulesService = (IRulesProviderService) GlobalServiceRegister.getDefault()
                    .getService(IRulesProviderService.class);
            if (rulesService != null) {
                rulesService.syncAllRules();
            }
        }

    }

    @Override
    public String getPreferenceValue(String key) {
        return CoreUIPlugin.getDefault().getPreferenceStore().getString(key);
    }

    @Override
    public void loadComponentsFromProviders(ERepositoryObjectType type) {
        IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
        if (designerCoreUIService != null) {
            designerCoreUIService.loadComponentsFromProviders(type);
        }
    }
}
