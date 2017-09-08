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
package org.talend.core.service;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.IService;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface ICoreUIService extends IService {

    void updatePalette();

    void deleteJobletConfigurationsFromPalette(String jobletName);

    IPreferenceStore getPreferenceStore();

    MenuManager[] getRepositoryContextualsActionGroups();

    List<ITreeContextualAction> getRepositoryContextualsActions();

    ITreeContextualAction getActionById(String id);

    void componentsReset();

    void initializeComponents(IProgressMonitor monitor);

    void syncAllRules();

    String getPreferenceValue(String key);
    
    void loadComponentsFromProviders(ERepositoryObjectType type);
}
