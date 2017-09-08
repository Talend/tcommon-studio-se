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
package org.talend.core.runtime.services;

import java.util.Dictionary;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceNode;
import org.talend.core.IService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IMavenUIService extends IService {

    /**
     * Try to add custom maven scripts node in project setting tree.
     */
    void addCustomMavenSettingChildren(IPreferenceNode parent);

    void checkUserSettings(IProgressMonitor monitor);

    void updateMavenResolver(boolean setupRemoteRepository);

    void addMavenConfigurationChangeListener();

    Dictionary<String, String> getTalendMavenSetting();

}
