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
package org.talend.core.runtime.projectsetting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.documentation.ERepositoryActionName;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ProjectPreferenceManager {

    /**
     * copied from EclipsePreferences.
     */
    public static final String DEFAULT_PREFERENCES_DIRNAME = ".settings"; //$NON-NLS-1$

    public static final String PREFS_FILE_EXTENSION = "prefs"; //$NON-NLS-1$

    private String qualifier;

    private IProject project;

    private ProjectScope projectScope;

    private IPreferenceStore store;
    
    private boolean isCurrentProject;
    
    private static IRunProcessService runProcessService;
    
    private static IRepositoryService repositoryService;
    
    static {
    	if(GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
    	if(GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
    		repositoryService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
    	}
    }

    public ProjectPreferenceManager(String fileName) {
        this(ProjectManager.getInstance().getCurrentProject(), fileName);
        isCurrentProject = true;
    }

    public ProjectPreferenceManager(Project p, String fileName) {
        super();
        try {
            Assert.isNotNull(p);
            IProject project = ResourceUtils.getProject(p);
            init(project, fileName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        addPropertyChangeListener();
    }

    public ProjectPreferenceManager(IProject project, String fileName) {
        super();
        init(project, fileName);
        addPropertyChangeListener();
    }

    private void init(IProject project, String fileName) {
        Assert.isNotNull(fileName);
        Assert.isNotNull(project);
        this.qualifier = fileName;
        this.project = project;
        this.projectScope = new ProjectScope(project);
        this.store = new ScopedPreferenceStore(this.projectScope, this.qualifier);
    }

    private void addPropertyChangeListener() {
    	if (repositoryService != null) {
        	repositoryService.getProxyRepositoryFactory().addPropertyChangeListener(new ProjectPreferenceReloadListener());
        }
    }

    public String getQualifier() {
        return qualifier;
    }

    public IProject getProject() {
        return project;
    }

    public ProjectScope getProjectScope() {
        return projectScope;
    }

    private IPath getLocation() {
        return getProjectScope().getLocation().append(getQualifier()).addFileExtension(PREFS_FILE_EXTENSION);
    }

    public IPreferenceStore getPreferenceStore() {
        return this.store;
    }

    /**
     * @deprecated because don't support the default value setting, so use store directly.
     */
    private IEclipsePreferences getQulifierPreference() {
        return getProjectScope().getNode(getQualifier());
    }

    /**
     * Check the .settings/<qualifier>.prefs file is existed in project or not.
     */
    public boolean exist() {
        return getLocation().toFile().exists();
    }

    public void setValue(String key, int value) {
        // IEclipsePreferences qulifierPreference = getQulifierPreference();
        // qulifierPreference.put(key, value);
        getPreferenceStore().setValue(key, value);
    }

    public void setValue(String key, String value) {
        // IEclipsePreferences qulifierPreference = getQulifierPreference();
        // qulifierPreference.put(key, value);
        getPreferenceStore().setValue(key, value);
    }

    public void setValue(String key, boolean value) {
        // IEclipsePreferences qulifierPreference = getQulifierPreference();
        // qulifierPreference.putBoolean(key, value);
        getPreferenceStore().setValue(key, value);
    }

    public int getInt(String key) {
        // IEclipsePreferences qulifierPreference = getQulifierPreference();
        // return qulifierPreference.getInt(key, false);
        return getPreferenceStore().getInt(key);
    }

    public String getValue(String key) {
        // IEclipsePreferences qulifierPreference = getQulifierPreference();
        // return qulifierPreference.get(key, null);
        return getPreferenceStore().getString(key);
    }

    public boolean getBoolean(String key) {
        // IEclipsePreferences qulifierPreference = getQulifierPreference();
        // return qulifierPreference.getBoolean(key, false);
        return getPreferenceStore().getBoolean(key);
    }

    /**
     * Save the configurations.
     */
    public void save() {
        if (runProcessService != null) {
            runProcessService.storeProjectPreferences(getPreferenceStore());
        }
    }
    
    public void reload() {
    	if (isCurrentProject) {
    		try {
    			Project currentProject = ProjectManager.getInstance().getCurrentProject();
    			init(ResourceUtils.getProject(currentProject), qualifier);
    		} catch (PersistenceException e) {
    			ExceptionHandler.process(e);
    		}
    	}
    }

	class ProjectPreferenceReloadListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getPropertyName().equals(ERepositoryActionName.PROJECT_PREFERENCES_RELOAD.getName())) {
				reload();
			}
		}
		
	}

}
