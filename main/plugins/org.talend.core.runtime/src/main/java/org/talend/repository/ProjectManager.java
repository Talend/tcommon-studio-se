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
package org.talend.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

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

    public static final String BRANCHES_PREFIX = "branches/";

    public static final String ORIGIN_PREFIX = "origin/";

    private static ProjectManager singleton;

    private Project currentProject;

    private Map<String, String> mapProjectUrlToBranchUrl = new HashMap<String, String>();
    
	private static final Map<String, Integer> projectLabelToIdMap = new HashMap<String, Integer>();

    private Map<String, List<FolderItem>> foldersMap = new HashMap<String, List<FolderItem>>();

    private Set<String> beforeLogonRecords;

    private Set<String> logonRecords;

    private Set<String> migrationRecords;

    private Set<Object> updatedRemoteHandlerRecords;

    private ProjectManager() {
        beforeLogonRecords = new HashSet<String>();
        logonRecords = new HashSet<String>();
        migrationRecords = new HashSet<String>();
        updatedRemoteHandlerRecords = new HashSet<Object>();
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

    public Project getProjectFromProjectTechLabel(String label) {
        if (currentProject == null) {
            initCurrentProject();
        }

        if (currentProject.getTechnicalLabel().equals(label)) {
            return currentProject;
        }
        for (Project project : getAllReferencedProjects()) {
            if (project.getTechnicalLabel().equals(label)) {
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
                return;
            }
        }
        currentProject = null;
    }

    private void resolveSubRefProject(org.talend.core.model.properties.Project p, List<Project> allReferencedprojects,
            Set<String> resolvedProjectLabels) {

        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        if (ctx != null && p != null) {
            String parentBranch = ProjectManager.getInstance().getMainProjectBranch(p);
            if (parentBranch != null) {
                resolvedProjectLabels.add(p.getTechnicalLabel());
                for (ProjectReference pr : new Project(p).getProjectReferenceList()) {
                    if (ProjectManager.validReferenceProject(p, pr)
                            && !resolvedProjectLabels.contains(pr.getReferencedProject().getTechnicalLabel())) {
                        Project project = new Project(pr.getReferencedProject(), false);
                        allReferencedprojects.add(project);
                        resolveSubRefProject(pr.getReferencedProject(), allReferencedprojects, resolvedProjectLabels); // only to resolve all
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
        return getReferencedProjects(currentProject);
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
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault()
                    .getService(IProxyRepositoryService.class);
            IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
            if (factory != null) {
                List<org.talend.core.model.properties.Project> rProjects = factory
                        .getReferencedProjects(this.getCurrentProject());
                if (rProjects != null) {
                    for (org.talend.core.model.properties.Project p : rProjects) {
                        Project project = new Project(p);
                        allReferencedprojects.add(project);
                        resolveSubRefProject(p, allReferencedprojects, new HashSet<String>());
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
    public List<Project> getReferencedProjects(Project project) {
        if (project != null) {
            List<Project> refProjects = new ArrayList<Project>();
            for (ProjectReference refProject : project.getProjectReferenceList()) {
                if (ProjectManager.validReferenceProject(project.getEmfProject(), refProject)) {
                    refProjects.add(new Project(getProject(refProject.getReferencedProject()), false));
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
                if (((Item) object).getParent() == null) { // may be a routelet from reference project
                    org.talend.core.model.properties.Project refProject = getProjectFromItemWithoutParent((Item)object);
                    if (refProject != null) {
                        return refProject;
                    }
                }
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

    /*
     * Returns the project name found from the current path if the parent is null
     */
    private org.talend.core.model.properties.Project getProjectFromItemWithoutParent(Item item) {

        final String URI_PREFIX = "platform:/resource/"; //$NON-NLS-1$

        org.talend.core.model.properties.ItemState state = item.getState();

        if ( state != null) {
            if (state instanceof org.eclipse.emf.ecore.impl.EObjectImpl) {
                org.eclipse.emf.common.util.URI eProxyUri = ((org.eclipse.emf.ecore.impl.EObjectImpl)state).eProxyURI();
                if (eProxyUri == null) {
                    return null;
                }
                String eProxyUriString = eProxyUri.toString();
                if (eProxyUriString != null && eProxyUriString.startsWith(URI_PREFIX)) {
                    
                    String tmpString = eProxyUriString.substring(URI_PREFIX.length());
                    String projectLabel = tmpString.substring(0, tmpString.indexOf("/")); //$NON-NLS-1$

                    if (currentProject == null) {
                        initCurrentProject();
                    }

                    if (currentProject.getTechnicalLabel().equalsIgnoreCase(projectLabel)) {
                        return currentProject.getEmfProject();
                    }
                    for (Project project : getAllReferencedProjects()) {
                        if (project.getTechnicalLabel().equalsIgnoreCase(projectLabel)) {
                            return project.getEmfProject();
                        }
                    }
                }
            }
        }

        return null;
    }

    public org.talend.core.model.properties.Project getProject(Project project, EObject object) {
        if (object != null) {
            if (object instanceof org.talend.core.model.properties.Project) {
                return (org.talend.core.model.properties.Project) object;
            }
            if (object instanceof Property) {
                return getProject(project, ((Property) object).getItem());
            }
            if (object instanceof Item) {
                return getProject(project, ((Item) object).getParent());
            }
        }

        // default
        if (project != null) {
            return project.getEmfProject();
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
        return isInMainProject(getCurrentProject(), object);
    }

    public boolean isInMainProject(Project mainProject, EObject object) {
        if (object != null) {
            org.talend.core.model.properties.Project project = getProject(mainProject, object);
            if (project != null && mainProject != null) {
                return project.getTechnicalLabel().equals(mainProject.getEmfProject().getTechnicalLabel());
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
                IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault()
                        .getService(IReferencedProjectService.class);
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
        String branchForMainProject = project != null ? getMainProjectBranch(project.getTechnicalLabel()) : null;
        return branchForMainProject;
    }

    /**
     *
     * DOC ggu Comment method "getMainProjectBranch".
     *
     * @param technicalLabel
     * @return
     */
    public String getMainProjectBranch(String technicalLabel) {
        String branchForMainProject = null;
        Map<String, String> fields = getRepositoryContextFields();
        if (fields == null || technicalLabel == null) {
            return branchForMainProject;
        }
        String branchKey = IProxyRepositoryFactory.BRANCH_SELECTION + SVNConstant.UNDER_LINE_CHAR + technicalLabel;
        if (fields.containsKey(branchKey)) {
            branchForMainProject = fields.get(branchKey);
        }
        if (branchForMainProject != null) {
            branchForMainProject = getFormatedBranchName(branchForMainProject);
        }
        return branchForMainProject;
    }

    public String getFormatedBranchName(String branchName) {
        String formatedBranchName = branchName;
        if (!branchName.startsWith(SVNConstant.NAME_TAGS + SVNConstant.SEP_CHAR)
                && !branchName.startsWith(SVNConstant.NAME_BRANCHES + SVNConstant.SEP_CHAR)
                && !branchName.startsWith(SVNConstant.NAME_ORIGIN + SVNConstant.SEP_CHAR)
                && !branchName.equals(SVNConstant.NAME_TRUNK) && !branchName.equals(SVNConstant.NAME_MASTER)) {
            formatedBranchName = SVNConstant.NAME_BRANCHES + SVNConstant.SEP_CHAR + branchName;
        }
        return formatedBranchName;
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
        mapProjectUrlToBranchUrl.clear();
        clearFolderCache();
    }

    public void clearFolderCache() {
        foldersMap.clear();
    }

    /**
     * Returns the type of project (local / svn / git).
     *
     * @param project
     * @return
     */
    public String getProjectType(Project project) {
        String projectType = "local"; //$NON-NLS-1$
        if (!currentProject.getEmfProject().isLocal()) {
            JSONObject projectInfo;
            try {
                projectInfo = new JSONObject(currentProject.getEmfProject().getUrl());
                projectType = projectInfo.getString("storage"); //$NON-NLS-1$
            } catch (JSONException e) {
                // nothing to do, consider local by default
            }
        }
        return projectType;
    }

    public static boolean validReferenceProject(org.talend.core.model.properties.Project mainProject,
            ProjectReference projectReference) {
        if (mainProject == null || projectReference == null) {
            return false;
        }
        if (mainProject.getTechnicalLabel().equals(projectReference.getReferencedProject().getTechnicalLabel())) {
            return false;
        }
        String branchForMainProject = ProjectManager.getInstance().getMainProjectBranch(mainProject);

        return validReferenceProject(branchForMainProject, projectReference);
    }

    public static boolean validReferenceProject(String branchForMainProject, ProjectReference projectReference) {
        if (projectReference == null) {
            return false;
        }

        if (projectReference.getBranch() == null
                || (branchForMainProject != null && (branchForMainProject.equals(projectReference.getBranch())
                        || branchForMainProject.equals(ORIGIN_PREFIX + projectReference.getBranch())))) {
            return true;
        }

        return false;
    }

    public static String getCleanBranchName(String branchName) {
        if (branchName == null) {
            return null;
        }
        if (branchName.startsWith(ORIGIN_PREFIX)) {
            return branchName.substring(ORIGIN_PREFIX.length());
        }
        if (branchName.startsWith(BRANCHES_PREFIX)) {
            return branchName.substring(BRANCHES_PREFIX.length());
        }
        return branchName;
    }

    public Set<String> getBeforeLogonRecords() {
        return this.beforeLogonRecords;
    }

    public Set<String> getLogonRecords() {
        return this.logonRecords;
    }

    public Set<String> getMigrationRecords() {
        return this.migrationRecords;
    }

    public Set<Object> getUpdatedRemoteHandlerRecords() {
        return this.updatedRemoteHandlerRecords;
    }

	public static void clearCachedProjectIds() {
		projectLabelToIdMap.clear();
	}

	public static void cacheProjectId(String projectLabel, Integer projectId) {
		projectLabelToIdMap.put(projectLabel, projectId);
	}

	public static String getCachedProjectId(String projectLabel) {
		if (projectLabelToIdMap.get(projectLabel) != null) {
			return String.valueOf(projectLabelToIdMap.get(projectLabel));
		}
		return null;
	}
}
