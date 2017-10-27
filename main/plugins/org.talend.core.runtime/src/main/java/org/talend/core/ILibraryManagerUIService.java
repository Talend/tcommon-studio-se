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
package org.talend.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.IRoutinesProvider;

/**
 * created by wchen on 2013-1-24 Detailled comment
 * 
 */
public interface ILibraryManagerUIService extends IService {

    public void installModules(String[] jarNames);

    public void installModules(Collection<ModuleNeeded> requiredModules);

    public Collection<IRoutinesProvider> getRoutinesProviders(ECodeLanguage language);

    public void initializeSystemLibs();

    public Map<String, List<LibraryInfo>> getRoutineAndJars();

    public List<IRepositoryViewObject> collectRelatedRoutines(Set<String> includeRoutineIdOrNames, boolean system,
            ERepositoryObjectType type);

    public String getLibrariesPath(ECodeLanguage language);

    public List<String> getNeedInstallModuleForBundle(String bundleName);

    public boolean isModuleInstalledForBundle(String bundleName);

    public boolean confirmDialog(String originalJarFileName);
}
