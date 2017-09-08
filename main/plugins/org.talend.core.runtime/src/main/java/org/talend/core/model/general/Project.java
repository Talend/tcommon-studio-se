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
package org.talend.core.model.general;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.process.IElement;
import org.talend.core.model.properties.ExchangeUser;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.User;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.ProjectManager;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: Project.java 46606 2010-08-11 08:33:54Z cli $
 * 
 */
public class Project {

    private org.talend.core.model.properties.Project project;

    // added by achen fix bug 0005991 & 0005993
    private IElement statsAndLog;

    private IElement initialContextLoad;

    // used for create project or sandbox project in remote
    private boolean isSandboxProject;

    private boolean mainProject = true;

    /**
     * Getter for mainProject.
     * 
     * @return the mainProject
     */
    public boolean isMainProject() {
        return this.mainProject;
    }

    /**
     * Sets the mainProject.
     * 
     * @param mainProject the mainProject to set
     */
    public void setMainProject(boolean mainProject) {
        this.mainProject = mainProject;
    }

    // end
    /**
     * DOC smallet Project constructor comment.
     * 
     * @param label
     * @param project
     */
    public Project(org.talend.core.model.properties.Project project) {
        this.project = project;
    }

    public Project(org.talend.core.model.properties.Project project, boolean isMainProject) {
        this.project = project;
        mainProject = isMainProject;
    }

    public Project(String label) {
        this.project = PropertiesFactory.eINSTANCE.createProject();
        project.setLabel(label);
        project.setBigData(isBigdataPluginLoaded());
    }

    public Project() {
        this.project = PropertiesFactory.eINSTANCE.createProject();
        this.project.setBigData(isBigdataPluginLoaded());
    }

    private boolean isBigdataPluginLoaded() {
        return GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopClusterService.class);
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return project.getLabel();
    }

    /**
     * Sets the label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        project.setLabel(label);
    }

    /**
     * Getter for technicalLabel.
     * 
     * @return the technicalLabel
     */
    public String getTechnicalLabel() {
        return project.getTechnicalLabel();
    }

    /**
     * Sets the technicalLabel.
     * 
     * @param technicalLabel the technicalLabel to set
     */
    public void setTechnicalLabel(String technicalLabel) {
        project.setTechnicalLabel(technicalLabel);
    }

    @Override
    public String toString() {
        return getLabel();
    }

    /**
     * Getter for author.
     * 
     * @return the author
     */
    public User getAuthor() {
        return project.getAuthor();
    }

    /**
     * Sets the author.
     * 
     * @param author the author to set
     */
    public void setAuthor(User author) {
        project.setAuthor(author);
    }

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return project.getDescription();
    }

    /**
     * Sets the description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        project.setDescription(description);
    }

    /**
     * Getter for language.
     * 
     * @return the language
     */
    public ECodeLanguage getLanguage() {
        return ECodeLanguage.getCodeLanguage(project.getLanguage());
    }

    /**
     * Sets the language.
     * 
     * @param language the language to set
     */
    public void setLanguage(ECodeLanguage language) {
        project.setLanguage(language.getName());
    }

    /**
     * Getter for local.
     * 
     * @return the local
     */
    public boolean isLocal() {
        return project.isLocal();
    }

    /**
     * Sets the local.
     * 
     * @param local the local to set
     */
    public void setLocal(boolean local) {
        project.setLocal(local);
    }

    /**
     * create technical name.
     * 
     * @param name
     * @return
     */
    public static String createTechnicalName(String name) {
        return ProjectManager.getLocalTechnicalProjectName(name);
    }

    public org.talend.core.model.properties.Project getEmfProject() {
        return project;
    }

    public void setEmfProject(org.talend.core.model.properties.Project project) {
        this.project = project;
    }

    public String getMasterJobId() {
        return project.getMasterJobId();
    }

    public void setMasterJobId(String masterJobId) {
        if (masterJobId != null) {
            project.setMasterJobId(masterJobId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.project == null) ? 0 : this.project.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        if (this.project == null) {
            if (other.project != null) {
                return false;
            }
        } else if (!this.project.equals(other.project)) {
        	if(this.project.getTechnicalLabel().equals(other.project.getTechnicalLabel())) {
                return true;
            }
            return false;
        }
        return true;
    }

    public IElement getStatsAndLog() {
        return this.statsAndLog;
    }

    public void setStatsAndLog(IElement statsAndLog) {
        this.statsAndLog = statsAndLog;
    }

    public IElement getInitialContextLoad() {
        return this.initialContextLoad;
    }

    public void setInitialContextLoad(IElement initialContextLoad) {
        this.initialContextLoad = initialContextLoad;
    }

    public boolean isSandboxProject() {
        return this.isSandboxProject;
    }

    public void setSandboxProject(boolean isSandboxProject) {
        this.isSandboxProject = isSandboxProject;
    }

    public ExchangeUser getExchangeUser() {
        ExchangeUser user = getExchangeUserFromEmail(""); // default one //$NON-NLS-1$

        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext context = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (context.getUser() != null && context.getUser().getLogin() != null) {
            user = getExchangeUserFromEmail(context.getUser().getLogin());
        }
        if (project.getAuthor() != null && "".equals(user.getLogin())) { //$NON-NLS-1$
            user = getExchangeUserFromEmail(project.getAuthor().getLogin());
        }
        return user;
    }

    private ExchangeUser getExchangeUserFromEmail(String connectionEmail) {
        ExchangeUser user = PropertiesFactory.eINSTANCE.createExchangeUser();
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        String string = prefStore.getString(connectionEmail);
        if (string != null) {
            String[] split = string.split(":"); //$NON-NLS-1$
            if (split.length == 3) {
                user.setLogin(split[0]);
                user.setUsername(split[1]);
                user.setPassword(split[2]);
            } else {
                user.setLogin(""); //$NON-NLS-1$
                user.setUsername(""); //$NON-NLS-1$
                user.setPassword(""); //$NON-NLS-1$
            }
        }
        return user;
    }

    public void setExchangeUser(ExchangeUser exchangeUser) {
        // project.setExchangeUser(exchangeUser);
    }
}
