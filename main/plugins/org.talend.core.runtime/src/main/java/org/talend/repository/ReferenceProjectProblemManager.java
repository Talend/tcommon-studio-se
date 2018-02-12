package org.talend.repository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.runtime.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

public class ReferenceProjectProblemManager {

    private static ReferenceProjectProblemManager instance;

    private Map<String, String> invalidProjectMap = new HashMap<String, String>();

    public static synchronized ReferenceProjectProblemManager getInstance() {
        if (instance == null) {
            instance = new ReferenceProjectProblemManager();
        }
        return instance;
    }

    public void addInvalidProjectReference(String projectTechnicalLabel, String branchName) {
        if (projectTechnicalLabel != null) {
            invalidProjectMap.put(projectTechnicalLabel, branchName);
        }
    }

    public Set<String> getInvalidProjectReferenceSet() {
        return invalidProjectMap.keySet();
    }

    public void clearAll() {
        invalidProjectMap.clear();
    }

    public static boolean checkCycleReference(Project project, Map<String, List<ProjectReference>> projectRefMap)
            throws PersistenceException, BusinessException {
        List<ProjectReference> referenceList = project.getProjectReferenceList();
        if (referenceList.size() == 0) {
            return true;
        }
        List<String> list = new ArrayList<String>();
        for (ProjectReference projetReference : referenceList) {
            list.add(projetReference.getReferencedProject().getTechnicalLabel());
        }
        projectRefMap.put(project.getTechnicalLabel(), referenceList);
        for (ProjectReference projetReference : referenceList) {
            List<ProjectReference> childReferenceList = getAllReferenceProject(projetReference, projectRefMap,
                    new HashSet<String>(), false);
            projectRefMap.put(projetReference.getReferencedProject().getTechnicalLabel(), childReferenceList);
        }

        return checkCycleReference(projectRefMap);
    }

	public static List<ProjectReference> readProjectReferenceSetting(ProjectReference projetReference)
			throws PersistenceException, BusinessException {
		return readProjectReferenceSetting(projetReference.getReferencedProject(),
				projetReference.getReferencedBranch());
	}

	public static List<ProjectReference> readProjectReferenceSetting(
			org.talend.core.model.properties.Project emfProject, String branch)
			throws PersistenceException, BusinessException {
		byte[] configContent = null;
		Project referencedProject = new Project(emfProject);
		IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault()
				.getService(IProxyRepositoryService.class);
		IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
		if (factory != null) {
			configContent = factory.getReferenceSettingContent(referencedProject, branch);
			if (configContent != null && configContent.length > 0) {
				ReferenceProjectProvider privoder = new ReferenceProjectProvider(referencedProject.getEmfProject(),
						configContent);
				privoder.initSettings();
				return privoder.getProjectReference();
			}
		}
		return new ArrayList<ProjectReference>();
	}

    public static List<ProjectReference> getAllReferenceProject(ProjectReference projectReference,
            Map<String, List<ProjectReference>> projectRefMap, Set<String> addedSet, boolean isReadFromRepository)
            throws PersistenceException, BusinessException {
        List<ProjectReference> result = new ArrayList<ProjectReference>();
        List<ProjectReference> referenceList = null;
        if (projectRefMap.containsKey(projectReference.getReferencedProject().getTechnicalLabel())) {
            referenceList = projectRefMap.get(projectReference.getReferencedProject().getTechnicalLabel());
        } else {
            if (isReadFromRepository) {
                referenceList = readProjectReferenceSetting(projectReference);
                projectRefMap.put(projectReference.getReferencedProject().getTechnicalLabel(), referenceList);
            } else {
                referenceList = new Project(projectReference.getReferencedProject()).getProjectReferenceList();
                projectRefMap.put(projectReference.getReferencedProject().getTechnicalLabel(), referenceList);
            }
        }
        addedSet.add(projectReference.getReferencedProject().getTechnicalLabel());
        if (referenceList != null && referenceList.size() > 0) {
            for (ProjectReference reference : referenceList) {
                result.add(reference);
                if (!addedSet.contains(reference.getReferencedProject().getTechnicalLabel())) {
                    result.addAll(getAllReferenceProject(reference, projectRefMap, addedSet, isReadFromRepository));
                    addedSet.add(reference.getReferencedProject().getTechnicalLabel());
                }
            }
        }
        return result;
    }

    /**
     * 
     * @param referenceMap
     * @return false- cycle reference exist otherwise true
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static boolean checkCycleReference(Map<String, List<ProjectReference>> projectRefMap) {
        List<String> allReferenceList = new ArrayList<String>();
        for (String key : projectRefMap.keySet()) {
            if (!allReferenceList.contains(key)) {
                allReferenceList.add(key);
            }
            List<ProjectReference> referenceList = projectRefMap.get(key);
            for (ProjectReference reference : referenceList) {
                if (!allReferenceList.contains(reference.getReferencedProject().getTechnicalLabel())) {
                    allReferenceList.add(reference.getReferencedProject().getTechnicalLabel());
                }
            }
        }
        List<int[]> prerequisites = new ArrayList<int[]>();
        for (String key : projectRefMap.keySet()) {
            int keyId = allReferenceList.indexOf(key);
            List<ProjectReference> referenceList = projectRefMap.get(key);
            for (ProjectReference reference : referenceList) {
                int refernceId = allReferenceList.indexOf(reference.getReferencedProject().getTechnicalLabel());
                prerequisites.add(new int[] { keyId, refernceId });
            }
        }

        List[] graph = new List[allReferenceList.size()];
        for (int i = 0; i < allReferenceList.size(); i++) {
            graph[i] = new ArrayList<Integer>();
        }
        boolean[] visited = new boolean[allReferenceList.size()];
        for (int i = 0; i < prerequisites.size(); i++) {
            graph[prerequisites.get(i)[1]].add(prerequisites.get(i)[0]);
        }

        for (int i = 0; i < allReferenceList.size(); i++) {
            if (!deepFirstSearch(graph, visited, i)) {
                return false;
            }
        }
        return true;
    }

    private static boolean deepFirstSearch(List[] graph, boolean[] visited, int id) {
        if (visited[id]) {
            return false;
        } else {
            visited[id] = true;
        }

        for (int i = 0; i < graph[id].size(); i++) {
            if (!deepFirstSearch(graph, visited, (int) graph[id].get(i))) {
                return false;
            }
        }
        visited[id] = false;
        return true;
    }

    /**
     * 
     * @param projectRefMap key : project technical label, value : all referenced project list
     * @throws MoreThanOneBranchException
     */
    public static void checkMoreThanOneBranch(Map<String, List<ProjectReference>> projectRefMap) throws BusinessException {
        Map<String, Set<String>> prjectBranchMap = new HashMap<String, Set<String>>();
        for (List<ProjectReference> referenceList : projectRefMap.values()) {
            for (ProjectReference pr : referenceList) {
                Set<String> branchSet = prjectBranchMap.get(pr.getReferencedProject().getTechnicalLabel());
                if (branchSet == null) {
                    branchSet = new HashSet<String>();
                    prjectBranchMap.put(pr.getReferencedProject().getTechnicalLabel(), branchSet);
                }
                branchSet.add(pr.getReferencedBranch());
            }
        }
        StringBuffer sb = new StringBuffer();
        for (String label : prjectBranchMap.keySet()) {
            if (prjectBranchMap.get(label).size() > 1) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(label);
            }
        }
        if (sb.length() > 0) {
            throw new BusinessException(
                    Messages.getString("ReferenceProjectProblemManager.ErrorMoreThanOneBranchUsing", sb.toString()),
                    prjectBranchMap);
        }
    }
}
