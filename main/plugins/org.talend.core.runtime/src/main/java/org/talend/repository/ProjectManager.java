// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.model.repository.SVNConstant;
import org.talend.core.model.utils.TalendPropertiesUtil;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.IReferencedProjectService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * ggu class global comment. Detailled comment
 */
public final class ProjectManager {

    public static final String LOCAL = "LOCAL"; //$NON-NLS-1$

    public static final String UNDER_LINE = "_"; //$NON-NLS-1$

    public static final String SEP_CHAR = SVNConstant.SEP_CHAR;

    public static final String NAME_TRUNK = SVNConstant.NAME_TRUNK;

    public static final String NAME_BRANCHES = SVNConstant.NAME_BRANCHES;

    public static final String NAME_TAGS = SVNConstant.NAME_TAGS;

    private static ProjectManager singleton;

    private Project currentProject;

    private Map<String, String> mapProjectUrlToBranchUrl = new HashMap<String, String>();

    private Map<String, List<FolderItem>> foldersMap = new HashMap<String, List<FolderItem>>();

    private ProjectManager() {
        initCurrentProject();
    }

    public static synchronized ProjectManager getInstance() {
        if (singleton == null) {
            singleton = new ProjectManager();
        }
        return singleton;
    }

    public Project getProjectFromProjectLabel(String label) {
        if (currentProject == null) {
            initCurrentProject();
        }
        if (currentProject.getLabel().equals(label)) {
            return currentProject;
        }
        for (Project project : getAllReferencedProjects()) {
            if (project.getLabel().equals(label)) {
                return project;
            }
        }
        return null;
    }

    private void initCurrentProject() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        if (ctx != null) {
            RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
            if (repositoryContext != null) {
                currentProject = repositoryContext.getProject();
                if (currentProject != null) {
                    resolveRefProject(currentProject.getEmfProject());
                }
                return;
            }
        }
        currentProject = null;
    }

    @SuppressWarnings("unchecked")
    private void resolveRefProject(org.talend.core.model.properties.Project p) {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        if (p != null && ctx != null) {
            String parentBranch = ProjectManager.getInstance().getMainProjectBranch(p);
            if (parentBranch != null) {
                for (ProjectReference pr : (List<ProjectReference>) p.getReferencedProjects()) {
                    if (pr.getBranch() == null || parentBranch.equals(pr.getBranch())) {
                        resolveRefProject(pr.getReferencedProject()); // only to resolve all
                    }
                }
            }
        }
    }

    private void resolveSubRefProject(org.talend.core.model.properties.Project p, List<Project> allReferencedprojects) {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        if (ctx != null && p != null) {
            String parentBranch = ProjectManager.getInstance().getMainProjectBranch(p);
            if (parentBranch != null) {
                for (ProjectReference pr : (List<ProjectReference>) p.getReferencedProjects()) {
                    if (pr.getBranch() == null || parentBranch.equals(pr.getBranch())) {
                        Project project = new Project(pr.getReferencedProject(), false);
                        allReferencedprojects.add(project);
                        resolveSubRefProject(pr.getReferencedProject(), allReferencedprojects); // only to resolve all
                    }
                }
            }
        }
    }

    /**
     * 
     * retrieve the referenced projects of current project.
     */
    public void retrieveReferencedProjects(List<Project> referencedprojects) {
        referencedprojects.clear();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
            if (factory != null) {
                List<org.talend.core.model.properties.Project> rProjects = factory
                        .getReferencedProjects(this.getCurrentProject());
                if (rProjects != null) {
                    for (org.talend.core.model.properties.Project p : rProjects) {
                        Project project = new Project(p);
                        resolveRefProject(p);
                        referencedprojects.add(project);
                    }
                }
            }
        }
    }

    @Deprecated
    public void retrieveReferencedProjects() {
        // not usefull anymore
    }

    /**
     * return current project.
     * 
     */
    public Project getCurrentProject() {
        initCurrentProject();
        return this.currentProject;
    }

    /**
     * 
     * return the referenced projects of current project.
     */
    public List<Project> getReferencedProjects() {
        List<Project> referencedprojects = new ArrayList<Project>();
        retrieveReferencedProjects(referencedprojects);
        return referencedprojects;
    }

    /**
     * 
     * return all the referenced projects of current project.
     */
    public List<Project> getAllReferencedProjects() {
        List<Project> allReferencedprojects = new ArrayList<Project>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            if (this.getCurrentProject() == null) {
                // only appears if there is some other exception before.
                // just return an empty list in this case.
                return allReferencedprojects;
            }
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
            if (factory != null) {
                List<org.talend.core.model.properties.Project> rProjects = factory
                        .getReferencedProjects(this.getCurrentProject());
                if (rProjects != null) {
                    for (org.talend.core.model.properties.Project p : rProjects) {
                        Project project = new Project(p, false);
                        allReferencedprojects.add(project);
                        resolveSubRefProject(p, allReferencedprojects);
                    }
                }
            }
        }
        return allReferencedprojects;
    }

    /**
     * 
     * return the referenced projects of the project.
     */
    @SuppressWarnings("unchecked")
    public List<Project> getReferencedProjects(Project project) {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        if (project != null && ctx != null) {
            if (project.equals(this.currentProject)) {
                return getReferencedProjects();
            }
            String parentBranch = getMainProjectBranch(project);

            List<Project> refProjects = new ArrayList<Project>();
            for (ProjectReference refProject : (List<ProjectReference>) project.getEmfProject().getReferencedProjects()) {
                if (refProject.getBranch() == null || parentBranch.equals(refProject.getBranch())) {
                    refProjects.add(new Project(refProject.getReferencedProject(), false));
                }
            }
            return refProjects;
        }
        return Collections.emptyList();
    }

    /**
     * 
     * return the project by object.
     */
    public org.talend.core.model.properties.Project getProject(EObject object) {
        if (object != null) {
            if (object instanceof org.talend.core.model.properties.Project) {
                return (org.talend.core.model.properties.Project) object;
            }
            if (object instanceof Property) {
                return getProject(((Property) object).getItem());
            }
            if (object instanceof Item) {
                return getProject(((Item) object).getParent());
            }
        }

        // default
        Project p = getCurrentProject();
        if (p != null) {
            return p.getEmfProject();
        }
        return null;
    }

    public IProject getResourceProject(org.talend.core.model.properties.Project project) {
        if (project != null) {
            try {
                return ResourceUtils.getProject(project.getTechnicalLabel());
            } catch (PersistenceException e) {
                //
            }
        }
        Project p = getCurrentProject();
        if (p != null) {
            return ResourcesPlugin.getWorkspace().getRoot().getProject(p.getEmfProject().getTechnicalLabel());
        }
        return null;
    }

    public IProject getResourceProject(EObject object) {
        return getResourceProject(getProject(object));
    }

    /**
     * 
     * ggu Comment method "isInCurrentMainProject".
     * 
     * check the EObject in current main project.
     */
    public boolean isInCurrentMainProject(EObject object) {
        if (object != null) {
            org.talend.core.model.properties.Project project = getProject(object);
            Project p = getCurrentProject();
            if (project != null && p != null) {
                return project.getTechnicalLabel().equals(p.getEmfProject().getTechnicalLabel());
            }
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "isInCurrentMainProject".
     * 
     * check the node in current main project.
     */
    public boolean isInCurrentMainProject(IRepositoryNode node) {
        if (node != null) {
            Project curP = getCurrentProject();
            if (PluginChecker.isRefProjectLoaded()) {
                IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault().getService(
                        IReferencedProjectService.class);
                if (service != null && service.isMergeRefProject() && curP != null) {
                    IRepositoryViewObject object = node.getObject();
                    if (object == null) {
                        return true;
                    }
                    org.talend.core.model.properties.Project emfProject = getProject(object.getProperty().getItem());
                    org.talend.core.model.properties.Project curProject = curP.getEmfProject();
                    return emfProject.equals(curProject);

                } else {
                    IProjectRepositoryNode root = node.getRoot();
                    if (root != null) {
                        Project project = root.getProject();
                        if (project != null) {
                            return project.equals(curP);
                        } else {
                            return true;
                        }
                    }
                }
                // refplugin is not loaded
            } else {
                IProjectRepositoryNode root = node.getRoot();
                if (root != null) {
                    Project project = root.getProject();
                    if (project != null && curP != null) {
                        return project.getTechnicalLabel().equals(curP.getTechnicalLabel());
                    } else {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public static IProjectRepositoryNode researchProjectNode(Project project) {
        final IRepositoryView repositoryView = RepositoryManager.getRepositoryView();
        if (repositoryView != null && project != null) {
            IProjectRepositoryNode root = repositoryView.getRoot();
            return researchProjectNode(root, project);
        }
        return null;
    }

    private static IProjectRepositoryNode researchProjectNode(IProjectRepositoryNode root, Project project) {
        final String technicalLabel = project.getTechnicalLabel();
        if (project == null || root.getProject().getTechnicalLabel().equals(technicalLabel)) {
            return root;
        }
        IRepositoryNode refRoot = root.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
        if (refRoot != null) {
            for (IRepositoryNode node : refRoot.getChildren()) {
                if (node instanceof IProjectRepositoryNode) {
                    IProjectRepositoryNode pNode = (IProjectRepositoryNode) node;
                    final IProjectRepositoryNode foundProjectNode = researchProjectNode(pNode, project);
                    if (foundProjectNode != null) {
                        return foundProjectNode;
                    }
                }
            }
        }
        return null;
    }

    public String getCurrentBranchURL(Project project) {
        if (mapProjectUrlToBranchUrl != null && project != null && project.getEmfProject() != null) {
            return mapProjectUrlToBranchUrl.get(project.getEmfProject().getUrl());
        }
        return null;
    }

    public String getCurrentBranchURL(String projectUrl) {
        return mapProjectUrlToBranchUrl.get(projectUrl);
    }

    public void setCurrentBranchURL(Project project, String currentBranch) {
        mapProjectUrlToBranchUrl.put(project.getEmfProject().getUrl(), currentBranch);
    }

    public List<FolderItem> getFolders(org.talend.core.model.properties.Project project) {
        if (!foldersMap.containsKey(project.getTechnicalLabel())) {
            foldersMap.put(project.getTechnicalLabel(), new ArrayList<FolderItem>());
        }
        return foldersMap.get(project.getTechnicalLabel());
    }

    public static boolean enableSpecialTechnicalProjectName() {
        // FIXME TDI-21185, add the function to enable disabling this function.
        return TalendPropertiesUtil.isEnabledMultiBranchesInWorkspace();
    }

    /**
     * 
     * DOC ggu Comment method "getLocalTechnicalProjectName". TDI-21185
     * 
     * @param projectLabel
     * @return
     */

    public static String getLocalTechnicalProjectName(String projectLabel) {
        if (projectLabel != null) {
            String technicalName = getTechnicalProjectLabel(projectLabel);
            if (enableSpecialTechnicalProjectName()) {
                return LOCAL + UNDER_LINE + technicalName;
            }
            return technicalName;
        }
        return null;
    }

    public static String getTechnicalProjectLabel(String lable) {
        String projectLabel = lable;
        if (projectLabel != null) {
            // TDI-6044
            projectLabel = projectLabel.trim();

            projectLabel = projectLabel.toUpperCase(Locale.ENGLISH);
            projectLabel = projectLabel.replace(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
            projectLabel = projectLabel.replace("-", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return projectLabel;
    }

    /**
     * 
     * DOC ggu Comment method "getProjectDisplayLabel".
     * 
     * @param project
     * @return
     */
    public static String getProjectDisplayLabel(org.talend.core.model.properties.Project project) {
        if (project != null) {
            if (project.getLabel().equals(project.getTechnicalLabel())) {
                return project.getLabel();
            }
            return project.getLabel() + " (" + project.getTechnicalLabel() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * 
     * DOC ldong Comment method "getCurrentBranchLabel".
     * 
     * @param project
     * @return
     */
    public static String getCurrentBranchLabel(Project project) {
        // just for TAC session,they do not want the label start with "/"
        String branchSelection = NAME_TRUNK;
        String branchSelectionFromProject = ProjectManager.getInstance().getMainProjectBranch(project);
        if (branchSelectionFromProject != null) {
            branchSelection = branchSelectionFromProject;
        } /*
           * else { ProjectManager.getInstance().setMainProjectBranch(project, NAME_TRUNK); }
           */

        if (!branchSelection.contains(NAME_TAGS) && !branchSelection.contains(NAME_BRANCHES)
                && !branchSelection.contains(NAME_TRUNK) && !branchSelection.contains("master")) { //$NON-NLS-1$
            branchSelection = NAME_BRANCHES + branchSelection;
        }
        return branchSelection;
    }

    public String getMainProjectBranch(Project project) {
        return project != null ? getMainProjectBranch(project.getEmfProject()) : null;
    }

    public String getMainProjectBranch(org.talend.core.model.properties.Project project) {
        return project != null ? getMainProjectBranch(project.getTechnicalLabel()) : null;
    }

    /**
     * 
     * DOC ggu Comment method "getMainProjectBranch".
     * 
     * @param technicalLabel
     * @return
     */
    public String getMainProjectBranch(String technicalLabel) {
        Map<String, String> fields = getRepositoryContextFields();
        if (fields == null || technicalLabel == null) {
            return null;
        }
        String branchKey = IProxyRepositoryFactory.BRANCH_SELECTION + SVNConstant.UNDER_LINE_CHAR + technicalLabel;
        if (fields.containsKey(branchKey)) {
            String branchForMainProject = fields.get(branchKey);
            return branchForMainProject;
        }
        return null;
    }

    /**
     * DOC ggu Comment method "getRepositoryContextFields".
     * 
     * @return
     */
    private Map<String, String> getRepositoryContextFields() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        if (ctx == null) {
            return null;
        }
        RepositoryContext repContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (repContext == null) {
            return null;
        }
        Map<String, String> fields = repContext.getFields();
        return fields;
    }

    public void setMainProjectBranch(Project project, String branchValue) {
        if (project != null) {
            setMainProjectBranch(project.getEmfProject(), branchValue);
        }
    }

    public void setMainProjectBranch(org.talend.core.model.properties.Project project, String branchValue) {
        if (project != null) {
            setMainProjectBranch(project.getTechnicalLabel(), branchValue);
        }
    }

    /**
     * 
     * DOC ggu Comment method "setMainProjectBranch".
     * 
     * When use this method to set the branch value, make sure that have set the RepositoryContext object in context
     * "ctx.putProperty(Context.REPOSITORY_CONTEXT_KEY, repositoryContext)"
     * 
     * @param technicalLabel
     * @param branchValue
     */
    public void setMainProjectBranch(String technicalLabel, String branchValue) {
        Map<String, String> fields = getRepositoryContextFields();
        if (fields == null || technicalLabel == null) {
            return;
        }
        String key = IProxyRepositoryFactory.BRANCH_SELECTION + SVNConstant.UNDER_LINE_CHAR + technicalLabel;
        // TDI-23291:when branchValue is null,should not set "" to the branchkey.
        if (branchValue != null) {
            fields.put(key, branchValue);
        }
    }

    /**
     * Expected to be called when logoff project, to clear all infos in memory.
     */
    public void clearAll() {
        getAllReferencedProjects().clear();
        mapProjectUrlToBranchUrl.clear();
        clearFolderCache();
    }

    public void clearFolderCache() {
        foldersMap.clear();
    }
}
