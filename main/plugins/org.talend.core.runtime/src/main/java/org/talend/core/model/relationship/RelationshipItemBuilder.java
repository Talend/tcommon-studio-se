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
package org.talend.core.model.relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemRelation;
import org.talend.core.model.properties.ItemRelations;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * This class store all relationships between jobs/joblets and other items from the repository. Be sure to update the
 * INDEX_VERSION when change this class. This will force to run the migration task on the next logon on this project.
 * 
 * This actually can deal with: <br>
 * - Context (id = context id) <br>
 * - Property (id = connection id) <br>
 * - Schema (id = connection id + " - " + schema name)<br>
 * - Job (id = job id)<br>
 * - Joblet (id = joblet name)<br>
 * - Query (id = connection id + " - " + query name) <br>
 * - SQL Templates (id = SQLPattern id + "--" + SQLPattern name) <br>
 */
public class RelationshipItemBuilder {

    // upgrade of version must be done each time there is any change in this class.
    // this will force next time the project to upgrade to be sure to be up to date when logon.
    public static final String INDEX_VERSION = "1.3"; //$NON-NLS-1$

    public static final String LATEST_VERSION = "Latest"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(RelationshipItemBuilder.class);

    public static final String JOB_RELATION = "job"; //$NON-NLS-1$

    public static final String TEST_RELATION = "test_case"; //$NON-NLS-1$

    public static final String JOBLET_RELATION = "joblet"; //$NON-NLS-1$

    public static final String SERVICES_RELATION = "services"; //$NON-NLS-1$

    public static final String PROPERTY_RELATION = "property"; //$NON-NLS-1$

    public static final String SCHEMA_RELATION = "schema"; //$NON-NLS-1$

    public static final String VALIDATION_RULE_RELATION = "validationRuleRelation"; //$NON-NLS-1$

    public static final String QUERY_RELATION = "query"; //$NON-NLS-1$

    public static final String CONTEXT_RELATION = "context"; //$NON-NLS-1$

    public static final String SQLPATTERN_RELATION = "sqlPattern"; //$NON-NLS-1$

    public static final String ROUTINE_RELATION = "routine"; //$NON-NLS-1$

    public static final String MAPPER_RELATION = "mapper"; //$NON-NLS-1$

    public static final String SURVIVOR_RELATION = "survivorshipRuleRelation"; //$NON-NLS-1$

    public static final String REPORT_RELATION = "report"; //$NON-NLS-1$

    public static final String RULE_RELATION = "rule"; //$NON-NLS-1$

    public static final String PATTERN_RELATION = "pattern"; //$NON-NLS-1$

    public static final String RESOURCE_RELATION = "resource";

    /*
     * 
     */

    public static final String OPTION_KEY_TYPE = "OPTION_TYPE"; //$NON-NLS-1$

    public static final String OPTION_TYPE_JOB = "JOB"; //$NON-NLS-1$

    public static final String OPTION_TYPE_NODE = "NODE"; //$NON-NLS-1$

    public static final String OPTION_KEY_NODE = "OPTION_NODE"; //$NON-NLS-1$

    private Map<Relation, Set<Relation>> currentProjectItemsRelations;

    private Map<Relation, Set<Relation>> referencesItemsRelations;

    private Map<String, String> hadoopItemReferences = new HashMap<String, String>();

    private boolean loaded = false;

    private boolean loading = false;

    private boolean modified = false;

    private Project aimProject;

    private IProxyRepositoryFactory proxyRepositoryFactory;

    public static final String COMMA = ";"; //$NON-NLS-1$

    private static Map<String, RelationshipItemBuilder> projectToInstanceMap = new HashMap<String, RelationshipItemBuilder>();

    private RelationshipItemBuilder() {

    }

    public static RelationshipItemBuilder getInstance() {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        RelationshipItemBuilder relationshipBuilder = getInstance(currentProject, true);
        return relationshipBuilder;
    }

    public static RelationshipItemBuilder getInstance(String projectTechnicalName) {
        synchronized (projectToInstanceMap) {
            return projectToInstanceMap.get(projectTechnicalName);
        }
    }

    public static RelationshipItemBuilder getInstance(Project project, boolean createIfNotExist) {
        String projectName = project.getTechnicalLabel();

        synchronized (projectToInstanceMap) {
            if (projectToInstanceMap.containsKey(projectName)) {
                RelationshipItemBuilder relationshipBuilder = projectToInstanceMap.get(projectName);
                // refresh current project realtime, in case studio switched project
                relationshipBuilder.setAimProject(project);
                return relationshipBuilder;
            }
        }

        if (!createIfNotExist) {
            return null;
        }

        IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        RelationshipItemBuilder instance = createInstance(proxyRepositoryFactory, project);
        synchronized (projectToInstanceMap) {
            projectToInstanceMap.put(projectName, instance);
        }
        return instance;
    }

    public static RelationshipItemBuilder createInstance(IProxyRepositoryFactory repositoryFactory, Project project) {
        RelationshipItemBuilder instance = new RelationshipItemBuilder();
        instance.setAimProject(project);
        instance.setProxyRepositoryFactory(repositoryFactory);
        return instance;
    }

    public static boolean isExist(Project project) {
        String projectName = project.getTechnicalLabel();

        if (projectToInstanceMap.containsKey(projectName)) {
            return true;
        }

        return false;
    }

    public Project getAimProject() {
        return this.aimProject;
    }

    private void setAimProject(Project aimProject) {
        this.aimProject = aimProject;
    }

    private IProxyRepositoryFactory getProxyRepositoryFactory() {
        return this.proxyRepositoryFactory;
    }

    public void setProxyRepositoryFactory(IProxyRepositoryFactory proxyRepositoryFactory) {
        this.proxyRepositoryFactory = proxyRepositoryFactory;
    }

    /**
     * Look for every linked items who use the selected id, no matter the version. Usefull when want to delete an item
     * since it will delete every versions.
     * 
     * @param itemId
     * @param version
     * @param relationType
     * @return
     */
    public List<Relation> getItemsHaveRelationWith(String itemId) {
        return getItemsHaveRelationWith(itemId, null);

    }

    /**
     * Look for every linked items who use the selected id, no matter the version. Usefull when want to delete an item
     * since it will delete every versions.
     * 
     * @param itemId
     * @param version
     * @return
     */
    public List<Relation> getItemsHaveRelationWith(String itemId, String version) {
        if (!loaded) {
            loadRelations();
        }
        Set<Relation> relations = new HashSet<Relation>();
        Set<Relation> itemsRelations = getItemsHaveRelationWith(currentProjectItemsRelations, itemId, version);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        itemsRelations = getItemsHaveRelationWith(referencesItemsRelations, itemId, version);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        return new ArrayList<Relation>(relations);

    }

    /**
     * Look for every linked items who use the selected id as subjob
     * 
     * @param itemId
     * @param version
     * @param relationType
     * @return
     */
    public List<Relation> getItemsHaveRelationWithJob(String itemId, String version) {
        if (!loaded) {
            loadRelations();
        }
        Set<Relation> relations = new HashSet<Relation>();
        Set<Relation> itemsRelations = getItemsHaveRelationWithJob(currentProjectItemsRelations, itemId, version);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        itemsRelations = getItemsHaveRelationWithJob(referencesItemsRelations, itemId, version);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        return new ArrayList<Relation>(relations);

    }

    public List<Relation> getItemsRelatedTo(String itemId, String version, String relationType) {
        if (!loaded) {
            loadRelations();
        }
        Set<Relation> relations = new HashSet<Relation>();
        Set<Relation> itemsRelations = getItemsRelatedTo(currentProjectItemsRelations, itemId, version, relationType);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        itemsRelations = getItemsRelatedTo(referencesItemsRelations, itemId, version, relationType);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        return new ArrayList<Relation>(relations);
    }

    /**
     * 
     * DOC cmeng Comment method "getItemsHaveRelationWith".
     * 
     * @param itemsRelations
     * @param itemId
     * @param version if null, then won't check
     * @return
     */
    private Set<Relation> getItemsHaveRelationWith(Map<Relation, Set<Relation>> itemsRelations, String itemId, String version) {

        /**
         * if verison is null, then won't check
         */

        Set<Relation> relations = new HashSet<Relation>();

        for (Relation baseItem : itemsRelations.keySet()) {
            for (Relation relatedItem : itemsRelations.get(baseItem)) {
                String id = relatedItem.getId();
                if (id != null) {
                    Relation tmpRelatedItem = null;
                    if (id.indexOf(" - ") != -1) { //$NON-NLS-1$
                        try {
                            tmpRelatedItem = (Relation) relatedItem.clone();
                            tmpRelatedItem.setId(id.split(" - ")[0]); //$NON-NLS-1$
                        } catch (CloneNotSupportedException e) {
                            log.error(e);
                        }
                    } else {
                        tmpRelatedItem = relatedItem;
                    }
                    if (tmpRelatedItem != null && itemId.equals(id)) {
                        boolean isEqual = true;

                        if (version != null) {
                            /**
                             * if verison is null, then won't check
                             */
                            String curVersion = tmpRelatedItem.getVersion();

                            if (!LATEST_VERSION.equals(version) && LATEST_VERSION.equals(curVersion)) {
                                try {
                                    IRepositoryViewObject latest = getProxyRepositoryFactory()
                                            .getLastVersion(getAimProject(), id);
                                    if (latest != null) {
                                        curVersion = latest.getVersion();
                                    }
                                } catch (PersistenceException e) {
                                    ExceptionHandler.process(e);
                                }
                            }
                            if (version.equals(curVersion)) {
                                isEqual = true;
                            } else {
                                isEqual = false;
                            }
                        }

                        if (isEqual) {
                            relations.add(baseItem);
                            break;
                        }
                    }
                }
            }
        }

        return relations;
    }

    private Set<Relation> getItemsHaveRelationWithJob(Map<Relation, Set<Relation>> itemsRelations, String itemId, String version) {
        Set<Relation> relations = new HashSet<Relation>();

        for (Relation baseItem : itemsRelations.keySet()) {
            for (Relation relatedItem : itemsRelations.get(baseItem)) {
                String id = relatedItem.getId();
                if (relatedItem.getType().equals(JOB_RELATION) && itemId.equals(id)) {
                    if ("Latest".equals(relatedItem.getVersion())) {
                        try {
                            IRepositoryViewObject latest = getProxyRepositoryFactory().getLastVersion(getAimProject(), id);
                            if (!latest.getVersion().equals(version)) {
                                continue;
                            }
                        } catch (PersistenceException e) {
                            continue;
                        }
                    } else if (!relatedItem.getVersion().equals(version)) {
                        continue;
                    }

                    relations.add(baseItem);
                    break;
                }
            }
        }

        return relations;
    }

    private Set<Relation> getItemsRelatedTo(Map<Relation, Set<Relation>> itemsRelations, String itemId, String version,
            String relationType) {

        Relation itemToTest = new Relation();

        itemToTest.setId(itemId);
        itemToTest.setType(relationType);
        itemToTest.setVersion(version);
        if (itemsRelations.containsKey(itemToTest)) {
            return itemsRelations.get(itemToTest);
        }

        Set<Relation> relations = new HashSet<Relation>();

        for (Relation baseItem : itemsRelations.keySet()) {
            for (Relation relatedItem : itemsRelations.get(baseItem)) {
                String id = relatedItem.getId();
                if (id != null) {
                    Relation tmpRelatedItem = null;
                    if (id.indexOf(" - ") != -1) { //$NON-NLS-1$
                        try {
                            tmpRelatedItem = (Relation) relatedItem.clone();
                            tmpRelatedItem.setId(id.split(" - ")[0]); //$NON-NLS-1$
                            tmpRelatedItem.setType(relationType);
                        } catch (CloneNotSupportedException e) {
                            log.error(e);
                        }
                    } else {
                        tmpRelatedItem = relatedItem;
                    }
                    if (tmpRelatedItem != null && tmpRelatedItem.equals(itemToTest)) {
                        relations.add(baseItem);
                        break;
                    }
                }
            }
        }

        return relations;
    }

    public List<Relation> getItemsJobRelatedTo(String itemId, String version, String relationType) {
        if (!loaded) {
            loadRelations();
        }
        Set<Relation> relations = new HashSet<Relation>();
        Set<Relation> itemsRelations = getItemsJobRelatedTo(currentProjectItemsRelations, itemId, version, relationType);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        itemsRelations = getItemsJobRelatedTo(referencesItemsRelations, itemId, version, relationType);
        if (itemsRelations != null) {
            relations.addAll(itemsRelations);
        }
        return new ArrayList<Relation>(relations);
    }

    private Set<Relation> getItemsJobRelatedTo(Map<Relation, Set<Relation>> itemsRelations, String itemId, String version,
            String relationType) {

        Relation itemToTest = new Relation();
        Set<Relation> jobRelations = new HashSet<Relation>();

        itemToTest.setId(itemId);
        itemToTest.setType(relationType);
        itemToTest.setVersion(version);
        if (itemsRelations.containsKey(itemToTest)) {
            Set<Relation> relations = itemsRelations.get(itemToTest);
            for (Relation relatedItem : relations) {
                if (relatedItem.getType().equals(relationType)) {
                    jobRelations.add(relatedItem);
                }
            }
            return jobRelations;
        }

        return jobRelations;
    }

    public static void destroy(Project project) throws Exception {
        if (project == null) {
            return;
        }
        String projectName = project.getTechnicalLabel();
        RelationshipItemBuilder relationshipItemBuilder = null;
        synchronized (projectToInstanceMap) {
            if (projectToInstanceMap.containsKey(projectName)) {
                relationshipItemBuilder = projectToInstanceMap.remove(projectName);
            }
        }
        if (relationshipItemBuilder != null) {
            relationshipItemBuilder.unloadRelations();
        }
    }

    public void unloadRelations() {
        loaded = false;
        clearAllItemsRelations();
    }

    public void clearAllItemsRelations() {
        if (currentProjectItemsRelations != null) {
            currentProjectItemsRelations.clear();
        }
        if (referencesItemsRelations != null) {
            referencesItemsRelations.clear();
        }
    }

    public void cleanTypeRelations(String baseType, String relationType, boolean save) {
        if (!loaded) {
            loadRelations();
        }
        // because only save for current project
        modified = cleanTypeRelations(getCurrentProjectItemsRelations(), baseType, relationType);
        cleanTypeRelations(referencesItemsRelations, baseType, relationType);

        if (save) {
            saveRelations();
        }
    }

    private boolean cleanTypeRelations(Map<Relation, Set<Relation>> projectRelations, String baseType, String relationType) {
        if (projectRelations == null) {
            return false;
        }
        boolean modified = false;
        // clean up base type first
        if (baseType != null) {
            List<Relation> cleanupItems = new ArrayList<Relation>();
            for (Relation base : projectRelations.keySet()) {
                if (baseType.equals(base.getType())) {
                    cleanupItems.add(base);
                }
            }
            modified = !cleanupItems.isEmpty();
            //
            for (Relation r : cleanupItems) {
                projectRelations.remove(r);
            }
        }

        // deal with the left for relation type
        if (relationType != null) {
            for (Relation base : projectRelations.keySet()) {
                Set<Relation> relation = projectRelations.get(base);
                if (relation != null) {
                    Iterator<Relation> iterator = relation.iterator();
                    while (iterator.hasNext()) {
                        Relation r = iterator.next();
                        if (relationType.equals(r.getType())) {
                            iterator.remove();
                            modified = true;
                        }
                    }
                }
            }
        }
        return modified;
    }

    private void loadRelations() {
        if (loading) {
            return;
        }
        loading = true;
        currentProjectItemsRelations = new HashMap<Relation, Set<Relation>>();
        referencesItemsRelations = new HashMap<Relation, Set<Relation>>();

        loadRelations(currentProjectItemsRelations, getAimProject());

        List<Project> referencedProjects = ProjectManager.getInstance().getReferencedProjects(getProxyRepositoryFactory(),
                getAimProject());
        for (Project p : referencedProjects) {
            loadRelations(referencesItemsRelations, p);
        }

        loading = false;
        loaded = true;

    }

    private void loadRelations(Map<Relation, Set<Relation>> itemRelations, Project project) {

        for (Object o : project.getEmfProject().getItemsRelations()) {
            ItemRelations relations = (ItemRelations) o;
            Relation baseItem = new Relation();

            if (relations.getBaseItem() == null) {
                log.log(Level.WARN, "Error when load the relationship, so rebuild all..."); //$NON-NLS-1$
                loaded = false;
                project.getEmfProject().getItemsRelations().clear();
                buildIndex(itemRelations, project, new NullProgressMonitor());
                return;
            }

            baseItem.setId(relations.getBaseItem().getId());
            baseItem.setType(relations.getBaseItem().getType());
            baseItem.setVersion(relations.getBaseItem().getVersion());

            itemRelations.put(baseItem, new HashSet<Relation>());
            for (Object o2 : relations.getRelatedItems()) {
                ItemRelation emfRelatedItem = (ItemRelation) o2;

                Relation relatedItem = new Relation();
                relatedItem.setId(emfRelatedItem.getId());
                relatedItem.setType(emfRelatedItem.getType());
                relatedItem.setVersion(emfRelatedItem.getVersion());

                itemRelations.get(baseItem).add(relatedItem);
            }
        }

    }

    public boolean isNeedSaveRelations() {
        if (loaded && modified) {
            return true;
        }
        return false;
    }

    public void saveRelations() {
        if (!loaded && !modified) {
            return;
        }
        Project currentProject = getAimProject();
        List<ItemRelations> oldRelations = new ArrayList<ItemRelations>(currentProject.getEmfProject().getItemsRelations());
        List<ItemRelations> usedList = new ArrayList<ItemRelations>();
        for (Relation relation : currentProjectItemsRelations.keySet()) {
            ItemRelations itemRelations = null;
            boolean exist = false;
            for (ItemRelations relations : oldRelations) {
                boolean isIdSame = relations.getBaseItem().getId().equals(relation.getId());
                boolean isVersionSame = StringUtils.equals(relations.getBaseItem().getVersion(), relation.getVersion());
                if (isIdSame && isVersionSame) {
                    usedList.add(relations);
                    itemRelations = relations;
                    exist = true;
                    break;
                }
            }
            if (itemRelations == null) {
                itemRelations = PropertiesFactory.eINSTANCE.createItemRelations();

                ItemRelation baseItem = PropertiesFactory.eINSTANCE.createItemRelation();
                itemRelations.setBaseItem(baseItem);

                baseItem.setId(relation.getId());
                baseItem.setType(relation.getType());
                baseItem.setVersion(relation.getVersion());
            }

            // sort by type
            List<Relation> relationItemsList = new ArrayList<Relation>(currentProjectItemsRelations.get(relation));
            Collections.sort(relationItemsList, new Comparator<Relation>() {

                @Override
                public int compare(Relation o1, Relation o2) {
                    return o1.getType().compareTo(o2.getType());
                }
            });
            List<ItemRelation> usedRelationList = new ArrayList<ItemRelation>();
            List<ItemRelation> oldRelatedItems = new ArrayList<ItemRelation>(itemRelations.getRelatedItems());
            for (Relation relatedItem : relationItemsList) {
                boolean found = false;
                for (ItemRelation item : oldRelatedItems) {
                    boolean isIdSame = false;
                    String itemId = item.getId();
                    if (itemId != null) {
                        isIdSame = itemId.equals(relatedItem.getId());
                    }
                    boolean isVersionSame = false;
                    if (isIdSame) {
                        isVersionSame = StringUtils.equals(item.getVersion(), relatedItem.getVersion());
                    }
                    boolean sameType = false;
                    if (isVersionSame) {
                        sameType = StringUtils.equals(item.getType(), relatedItem.getType());
                    }
                    if (isIdSame && isVersionSame && sameType) {
                        usedRelationList.add(item);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    continue;
                }
                ItemRelation emfRelatedItem = PropertiesFactory.eINSTANCE.createItemRelation();
                emfRelatedItem.setId(relatedItem.getId());
                emfRelatedItem.setType(relatedItem.getType());
                emfRelatedItem.setVersion(relatedItem.getVersion());
                itemRelations.getRelatedItems().add(emfRelatedItem);
            }
            // get unused relation items
            oldRelatedItems.removeAll(usedRelationList);
            // remove unused relation items
            itemRelations.getRelatedItems().removeAll(oldRelatedItems);
            if (!exist) {
                currentProject.getEmfProject().getItemsRelations().add(itemRelations);
            }
        }
        oldRelations.removeAll(usedList);
        currentProject.getEmfProject().getItemsRelations().removeAll(oldRelations);
        try {
            getProxyRepositoryFactory().saveProject(currentProject);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        modified = false;
    }

    private String getTypeFromItem(Item item) {
        String type = null;
        boolean isTestContainer = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null ) {
                isTestContainer = testContainerService.isTestContainerItem(item);
            }
        }
        if (item instanceof ProcessItem) {
            type = JOB_RELATION;
        } else if (item instanceof JobletProcessItem) {
            type = JOBLET_RELATION;
        }else if(isTestContainer){
            type = TEST_RELATION;
        }else {
            throw new RuntimeException(Messages.getString("RelationshipItemBuilder.unexpect.item", item.getClass().getName())); //$NON-NLS-1$
        }

        return type;
    }

    public List<ERepositoryObjectType> getSupportRepObjTypes(String relationType) {
        if (JOB_RELATION.equals(relationType)) {
            return ERepositoryObjectType.getAllTypesOfProcess();
        } else if (JOBLET_RELATION.equals(relationType)) {
            return ERepositoryObjectType.getAllTypesOfJoblet();
        }  else if (TEST_RELATION.equals(relationType)) {
            return ERepositoryObjectType.getAllTypesOfTestContainer();
        } else {
            throw new RuntimeException(Messages.getString("RelationshipItemBuilder.unexpect.relation", relationType)); //$NON-NLS-1$
        }
    }

    private void clearItemsRelations(Item baseItem) {
        Relation relation = new Relation();
        relation.setId(baseItem.getProperty().getId());
        relation.setType(getTypeFromItem(baseItem));
        relation.setVersion(baseItem.getProperty().getVersion());

        if (currentProjectItemsRelations.containsKey(relation)) {
            currentProjectItemsRelations.get(relation).clear();
        }
        if (referencesItemsRelations.containsKey(relation)) {
            referencesItemsRelations.get(relation).clear();
        }
    }

    public static void mergeRelationship(Map<Relation, Set<Relation>> itemRelations, Map<Relation, Set<Relation>> newRelations) {
        if (itemRelations != null && newRelations != null) {
            for (Relation relation : newRelations.keySet()) {
                if (!itemRelations.containsKey(relation)) {
                    itemRelations.put(relation, new HashSet<Relation>());
                }
                itemRelations.get(relation).addAll(newRelations.get(relation));
            }
        }
    }

    private void addRelationShip(Item baseItem, String relatedId, String relatedVersion, String type) {
        Relation relation = new Relation();
        relation.setId(baseItem.getProperty().getId());
        relation.setType(getTypeFromItem(baseItem));
        relation.setVersion(baseItem.getProperty().getVersion());

        Relation addedRelation = new Relation();
        addedRelation.setId(relatedId);
        addedRelation.setType(type);
        addedRelation.setVersion(relatedVersion);
        Map<Relation, Set<Relation>> itemRelations = getRelatedRelations(baseItem);

        if (!itemRelations.containsKey(relation)) {
            itemRelations.put(relation, new HashSet<Relation>());
        }
        itemRelations.get(relation).add(addedRelation);
    }

    private Map<Relation, Set<Relation>> getRelatedRelations(Item baseItem) {
        Map<Relation, Set<Relation>> itemRelations = currentProjectItemsRelations;
        if (!ProjectManager.getInstance().isInMainProject(getAimProject(), baseItem)) {
            itemRelations = referencesItemsRelations;
        }
        return itemRelations;

    }

    public boolean isAlreadyBuilt(Project project) {
        return !project.getEmfProject().getItemsRelations().isEmpty();
    }

    private void buildIndex(Map<Relation, Set<Relation>> itemRelations, Project project, IProgressMonitor monitor) {
        modified = true;

        if (!project.getEmfProject().getItemsRelations().isEmpty()) {
            loadRelations(itemRelations, project);
            if (loaded) { // check if already loaded successfully
                return;
            }
        }

        IProxyRepositoryFactory factory = getProxyRepositoryFactory();
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
        try {
            for (ERepositoryObjectType curTyp : getTypes()) {
                if (curTyp != null) {
                    list.addAll(factory.getAll(project, curTyp, true, true));
                }
            }
            monitor.beginTask(Messages.getString("RelationshipItemBuilder.buildingIndex"), list.size()); //$NON-NLS-1$

            if (list.isEmpty()) {
                return;
            }

            for (IRepositoryViewObject object : list) {
                Item item = object.getProperty().getItem();
                monitor.subTask(Messages.getString("RelationshipItemBuilder.forItem") + item.getProperty().getLabel()); //$NON-NLS-1$
                addOrUpdateItem(item, true);
                monitor.worked(1);
                if (monitor.isCanceled()) {
                    return;
                }
            }
            saveRelations();
            monitor.done();
            loaded = true;

        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

    private List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.PROCESS);
        toReturn.add(ERepositoryObjectType.PROCESS_ROUTE);
        toReturn.add(ERepositoryObjectType.JOBLET);
        return toReturn;
    }

    public void updateItemVersion(Item baseItem, String oldVersion, String id, Map<String, String> versions)
            throws PersistenceException {
        updateItemVersion(baseItem, oldVersion, id, versions, false);
    }

    public void updateItemVersion(Item baseItem, String oldVersion, String id, Map<String, String> versions,
            boolean avoidSaveProject) throws PersistenceException {
        IProxyRepositoryFactory factory = getProxyRepositoryFactory();
        IRepositoryViewObject obj = factory.getSpecificVersion(id, oldVersion, avoidSaveProject);
        Item item = obj.getProperty().getItem();
        // String itemVersion = item.getProperty().getVersion();
        Project currentProject = getAimProject();
        Project project = new Project(ProjectManager.getInstance().getProject(currentProject, item));
        if (!loaded) {
            loadRelations();
        }
        if (!currentProject.equals(project)) { // only current project
            return;
        }
        ProcessType processType = null;
        if (item instanceof ProcessItem) {
            processType = ((ProcessItem) item).getProcess();
        }
        if (item instanceof JobletProcessItem) {
            processType = ((JobletProcessItem) item).getJobletProcess();
        }
        if (processType != null) {
            for (Object o : processType.getNode()) {
                if (o instanceof NodeType) {
                    NodeType currentNode = (NodeType) o;
                    if ("tRunJob".equals(currentNode.getComponentName())) { //$NON-NLS-1$
                        // in case of tRunJob
                        String jobIdStr = null;
                        String jobVersion = LATEST_VERSION;
                        String nowVersion = "";
                        Set<String> jobIdSet = new HashSet<String>();
                        for (Object o2 : currentNode.getElementParameter()) {
                            if (o2 instanceof ElementParameterType) {
                                ElementParameterType param = (ElementParameterType) o2;
                                if (param.getName().equals("PROCESS:PROCESS_TYPE_PROCESS") //$NON-NLS-1$
                                        || param.getName().equals("PROCESS_TYPE_PROCESS")) { //$NON-NLS-1$
                                    // feature 19312
                                    String jobIds = param.getValue();
                                    String[] jobsArr = jobIds.split(RelationshipItemBuilder.COMMA);
                                    for (String jobId : jobsArr) {
                                        if (StringUtils.isNotEmpty(jobId)) {
                                            jobIdSet.add(jobId);
                                        }
                                        jobIdStr = jobId;
                                    }
                                }
                                if (param.getName().equals("PROCESS:PROCESS_TYPE_VERSION") //$NON-NLS-1$
                                        || param.getName().equals("PROCESS_TYPE_VERSION")) { //$NON-NLS-1$
                                    jobVersion = param.getValue();
                                    if (jobVersion.equals(LATEST_VERSION)) {
                                        if (!versions.isEmpty()) {
                                            nowVersion = versions.get(jobIdStr);
                                            param.setValue(nowVersion);
                                        }
                                    }
                                }
                            }
                        }
                        for (String jobId : jobIdSet) {
                            addRelationShip(item, jobId, nowVersion, JOB_RELATION);
                            factory.save(project, item);
                        }
                    }
                }
            }
        }
        if (!avoidSaveProject) {
            saveRelations();
        }
    }

    public boolean supportRelation(Item item) {
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);

        List<ERepositoryObjectType> supportTypes = getSupportTypes();

        if (supportTypes != null && supportTypes.contains(itemType)) {
            return true;
        }

        return false;
    }

    public List<ERepositoryObjectType> getSupportTypes() {
        List<ERepositoryObjectType> supportTypes = new ArrayList<ERepositoryObjectType>();

        List<ERepositoryObjectType> processTypes = getSupportRepObjTypes(JOB_RELATION);
        if (processTypes != null && !processTypes.isEmpty()) {
            supportTypes.addAll(processTypes);
        }

        List<ERepositoryObjectType> jobletTypes = getSupportRepObjTypes(JOBLET_RELATION);
        if (jobletTypes != null && !jobletTypes.isEmpty()) {
            supportTypes.addAll(jobletTypes);
        }
        
        List<ERepositoryObjectType> testTypes = getSupportRepObjTypes(TEST_RELATION);
        if (testTypes != null && !testTypes.isEmpty()) {
            supportTypes.addAll(testTypes);
        }

        return supportTypes;
    }

    public void addOrUpdateItem(Item item) {
        addOrUpdateItem(item, false);
    }

    public void addOrUpdateItem(Item item, boolean fromMigration) {
        if (!supportRelation(item)) {
            return;
        }
        if (!loaded) {
            loadRelations();
        }
        if (item == null) {
            return;
        }

        boolean relationsModified = true;
        Relation relation = new Relation();
        relation.setId(item.getProperty().getId());
        relation.setType(getTypeFromItem(item));
        relation.setVersion(item.getProperty().getVersion());

        Set<Relation> oldProjectRelations = null;
        if (currentProjectItemsRelations.containsKey(relation)) {
            oldProjectRelations = new HashSet<Relation>(currentProjectItemsRelations.get(relation));
            currentProjectItemsRelations.get(relation).clear();
        }

        clearItemsRelations(item);

        final Map<Relation, Set<Relation>> itemRelations = getRelatedRelations(item);

        IItemRelationshipHandler[] itemRelationshipHandlers = RelationshipRegistryReader.getInstance()
                .getItemRelationshipHandlers();
        for (IItemRelationshipHandler handler : itemRelationshipHandlers) {
            Map<Relation, Set<Relation>> relations = handler.find(item);
            mergeRelationship(itemRelations, relations);
        }

        if (oldProjectRelations != null) {
            // check if there is any changes on the relations.
            Set<Relation> newProjectRelations = currentProjectItemsRelations.get(relation);
            if (oldProjectRelations.size() == newProjectRelations.size()) {
                relationsModified = false;
                for (Relation newRelation : newProjectRelations) {
                    if (!oldProjectRelations.contains(newRelation)) {
                        relationsModified = true;
                        break;
                    }
                }
            }
            if (!relationsModified) {
                currentProjectItemsRelations.get(relation).addAll(oldProjectRelations);
            }
        }
        if (relationsModified && !modified) {
            modified = true;
        }
        if (!fromMigration && modified) {
            saveRelations();
        }
    }

    public void removeItemRelations(Item item) {
        if (!loaded) {
            loadRelations();
        }
        modified = true;

        ProcessType processType = null;
        if (item instanceof ProcessItem) {
            processType = ((ProcessItem) item).getProcess();
        }
        if (item instanceof JobletProcessItem) {
            processType = ((JobletProcessItem) item).getJobletProcess();
        }

        if (processType != null) {
            Relation relation = new Relation();
            relation.setId(item.getProperty().getId());
            relation.setType(getTypeFromItem(item));
            relation.setVersion(item.getProperty().getVersion());

            Map<Relation, Set<Relation>> itemRelations = getRelatedRelations(item);

            if (itemRelations.containsKey(relation)) {
                itemRelations.get(relation).clear();
                itemRelations.remove(relation);
                saveRelations();
            }
        }
    }

    public void removeItemRelations(Relation relation, boolean save) {
        Map<Relation, Set<Relation>> itemRelations = getCurrentProjectItemsRelations();
        if (itemRelations != null && itemRelations.containsKey(relation)) {
            itemRelations.get(relation).clear();
            itemRelations.remove(relation);
            if (save) {
                saveRelations();
            }
        }
    }

    public Map<Relation, Set<Relation>> getCurrentProjectItemsRelations() {
        return this.currentProjectItemsRelations;
    }
}
