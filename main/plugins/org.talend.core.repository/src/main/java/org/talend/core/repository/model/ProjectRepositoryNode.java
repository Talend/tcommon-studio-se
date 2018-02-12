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
package org.talend.core.repository.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.RuntimeExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.repository.IExtendRepositoryNode;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.MetadataManager;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.SAPBWTable;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.ValidationRulesConnection;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.JobletDocumentationItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.ValidationRulesConnectionItem;
import org.talend.core.model.repository.DynaEnum;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.model.repository.RepositoryNodeProviderRegistryReader;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.QueryRepositoryObject;
import org.talend.core.repository.model.repositoryObject.SAPFunctionRepositoryObject;
import org.talend.core.repository.model.repositoryObject.SAPIDocRepositoryObject;
import org.talend.core.repository.model.repositoryObject.SalesforceModuleRepositoryObject;
import org.talend.core.repository.recyclebin.RecycleBinManager;
import org.talend.core.repository.ui.utils.ProjectRepositoryNodeCache;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.ui.ICDCProviderService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SAPBWTableHelper;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.BinRepositoryNode;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.StableRepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.utils.sql.ConnectionUtils;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class ProjectRepositoryNode extends RepositoryNode implements IProjectRepositoryNode {

    private RepositoryNode recBinNode, sqlPatternNode, docNode, metadataNode, metadataConNode, metadataFileNode,
            metadataSAPConnectionNode, metadataEbcdicConnectionNode, metadataHL7ConnectionNode, metadataRulesNode,
            metadataBRMSConnectionNode, metadataValidationRulesNode, metadataEDIFactConnectionNode;

    private RepositoryNode refProject;

    public static boolean refProjectBool = false;

    public static boolean refreshBool = false;

    private static ProjectRepositoryNode defaultProjRepoNode;

    private static ProjectRepositoryNode dummyProjRepoNode;

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private org.talend.core.model.general.Project project;

    private Map<Object, List<Project>> nodeAndProject;

    private Map<String, RepositoryNode> repositoryNodeMap = new HashMap<String, RepositoryNode>();

    private Map<String, RepositoryNode> genericNodesMap = new HashMap<>();

    private String currentPerspective; // set the current perspective

    private List<FolderItem> delFolderItems = new ArrayList<FolderItem>();

    private ProjectRepositoryNodeCache nodeCache;

    private int options = IRepositoryFactory.OPTION_ONLY_LAST_VERSION | IRepositoryFactory.OPTION_DYNAMIC_OBJECTS;

    /**
     * DOC nrousseau ProjectRepositoryNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public ProjectRepositoryNode(org.talend.core.model.general.Project project, IRepositoryViewObject object,
            RepositoryNode parent, RepositoryNode root, ENodeType type) {
        super(object, parent, type);
        nodeCache = new ProjectRepositoryNodeCache();
        // for referenced project
        this.project = project;
        setRoot(this);
    }

    public ProjectRepositoryNode(ProjectRepositoryNode projectNode) {
        this(projectNode.getProject(), projectNode.getObject(), projectNode.getParent(), (RepositoryNode) projectNode.getRoot(),
                projectNode.getType());

        this.setProperties(EProperties.LABEL, projectNode.getProperties(EProperties.LABEL));
        this.setProperties(EProperties.CONTENT_TYPE, projectNode.getProperties(EProperties.CONTENT_TYPE));
    }

    // base project
    public ProjectRepositoryNode(IRepositoryObject object, RepositoryNode parent, ENodeType type) {
        this(ProjectManager.getInstance().getCurrentProject(), object, parent, null, type);
    }

    public static ProjectRepositoryNode getInstance() {
        if (!ProxyRepositoryFactory.getInstance().isFullLogonFinished()) {
            if (dummyProjRepoNode == null) {
                dummyProjRepoNode = new ProjectRepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);
                dummyProjRepoNode.initialize(null);
            }
            return dummyProjRepoNode;
        }
        if (defaultProjRepoNode == null) {
            defaultProjRepoNode = new ProjectRepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);
            defaultProjRepoNode.initialize(null);
        }
        if (defaultProjRepoNode.isDisposed) {
            synchronized (defaultProjRepoNode) {
                /**
                 * check again
                 */
                if (defaultProjRepoNode.isDisposed) {
                    defaultProjRepoNode.isDisposed = false;
                    defaultProjRepoNode.project = ProjectManager.getInstance().getCurrentProject();
                    defaultProjRepoNode.setRoot(defaultProjRepoNode);
                    defaultProjRepoNode.initialize(defaultProjRepoNode.currentPerspective);
                }
            }
        }
        return defaultProjRepoNode;
    }

    public void initialize(String currentPerspective) {
        this.currentPerspective = currentPerspective;
        nodeAndProject = new HashMap<Object, List<Project>>();
        IRepositoryNode curParentNode = this;

        List<IRepositoryNode> nodes = curParentNode.getChildren();
        // 0. Recycle bin
        recBinNode = new BinRepositoryNode(this);
        nodes.add(recBinNode);

        // 7. Metadata
        metadataNode = new RepositoryNode(null, this, ENodeType.STABLE_SYSTEM_FOLDER);
        metadataNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA);
        metadataNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA);
        nodes.add(metadataNode);

        // 7.17 Rules and BRMS
        if (PluginChecker.isSurvivorshipPluginLoaded() || PluginChecker.isRulesPluginLoaded()
                || PluginChecker.isBRMSPluginLoaded()) {
            StableRepositoryNode baseRulesNode = new StableRepositoryNode(this,
                    Messages.getString("ProjectRepositoryNode.rulesManagement"), //$NON-NLS-1$
                    ECoreImage.METADATA_RULES_ICON);
            baseRulesNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_RULES_MANAGEMENT);
            metadataNode.getChildren().add(baseRulesNode);

            if (PluginChecker.isBRMSPluginLoaded()) {
                metadataBRMSConnectionNode = new RepositoryNode(null, this, ENodeType.SYSTEM_FOLDER);
                metadataBRMSConnectionNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_BRMS);
                metadataBRMSConnectionNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_BRMS);
                baseRulesNode.getChildren().add(metadataBRMSConnectionNode);
            }
            // 7.14 RULES
            if (PluginChecker.isRulesPluginLoaded()) {
                metadataRulesNode = new RepositoryNode(null, this, ENodeType.SYSTEM_FOLDER);
                metadataRulesNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_FILE_RULES);
                metadataRulesNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_FILE_RULES);
                baseRulesNode.getChildren().add(metadataRulesNode);
            }
        }

        // Reference Projects
        if (PluginChecker.isRefProjectLoaded() && getParent() != this && project != null && !project.isLocal()
                && (project.equals(ProjectManager.getInstance().getCurrentProject())
                        || project.getProjectReferenceList().size() > 0)) {
            refProject = new RepositoryNode(null, this, ENodeType.SYSTEM_FOLDER);
            refProject.setProperties(EProperties.LABEL, ERepositoryObjectType.REFERENCED_PROJECTS);
            refProject.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.REFERENCED_PROJECTS);
            nodes.add(refProject);
        }
        // *init the repository node from extension
        initExtensionRepositoryNodes(curParentNode);
        // init the repository nodes from component service
        initNodesFromComponentSerivice(metadataNode);
        // delete the hidden nodes
        deleteHiddenNodes(nodes);

        try {
            hideHiddenNodesDependsUserRight();
        } catch (JSONException e) {
            ExceptionHandler.process(e);
        }

        collectRepositoryNodes(curParentNode);
    }

    private void deleteHiddenNodes(List<IRepositoryNode> nodes) {

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
            List<IRepositoryNode> hiddens = service.getBrandingConfiguration().getHiddenRepositoryCategory(this, "DI");
            for (IRepositoryNode node : hiddens) {
                Iterator<IRepositoryNode> it = nodes.iterator();
                while (it.hasNext()) {
                    IRepositoryNode curNode = it.next();
                    if (curNode.getContentType() != null && curNode.getContentType().equals(ERepositoryObjectType.METADATA)) {
                        for (IRepositoryNode curNode2 : curNode.getChildren()) {
                            if (curNode2.equals(node)) {
                                curNode.getChildren().remove(curNode2);
                                break;
                            }
                        }
                    }
                    if (curNode.equals(node)) {
                        it.remove();
                    }
                }
            }
        }
    }

    private void collectRepositoryNodes(IRepositoryNode curParentNode) {
        if (repositoryNodeMap == null) {
            repositoryNodeMap = new HashMap<String, RepositoryNode>();
        }
        repositoryNodeMap.clear();
        collectRepositoryNodes(curParentNode.getChildren());
    }

    private void collectRepositoryNodes(List<IRepositoryNode> nodes) {

        if (nodes != null) {
            for (IRepositoryNode node : nodes) {
                if (node.getParent() instanceof ProjectRepositoryNode) { // root node of type
                    ERepositoryObjectType roType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                    if (roType != null) { // bin is null
                        String typeName = roType.name();
                        if (repositoryNodeMap.containsKey(typeName)) {
                            // later, will do something.
                        } else {
                            repositoryNodeMap.put(typeName, (RepositoryNode) node);
                        }
                    }
                }
                collectRepositoryNodes(node.getChildren());
            }
        }

    }

    private String[] getUserAuthorization() throws JSONException {
        User currentUser = ProxyRepositoryFactory.getInstance().getRepositoryContext().getUser();
        String addData = currentUser.getAdditionnalData();
        if (addData == null) {
            String[] userRights = {};
            return userRights;
        }
        JSONObject o = new JSONObject(addData);
        List<String> rightsAsList = new ArrayList<String>();
        Iterator<String> it = o.sortedKeys();

        while (it.hasNext()) {
            String key = it.next();
            if (o.getBoolean(key)) {
                rightsAsList.add(key);
            }
        }
        String[] rights = rightsAsList.toArray(new String[] {});
        return rights;
    }

    private void hideHiddenNodesDependsUserRight() throws JSONException {
        String[] userRights = getUserAuthorization();
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>(this.getChildren());
        for (IRepositoryNode node : nodes) {
            ERepositoryObjectType contentType = node.getContentType();
            if (contentType == null) {
                continue;
            }
            String[] contentRight = contentType.getUserRight();
            if (contentRight != null && contentRight.length > 0 && userRights != null && userRights.length > 0) {
                for (int i = 0; i < contentRight.length; i++) {
                    if (!ArrayUtils.contains(userRights, contentRight[i])) {
                        removeNode(this, node);
                    }
                }
            }

        }
    }

    private void removeNode(IRepositoryNode container, IRepositoryNode node) {
        List<IRepositoryNode> nodes = container.getChildren();

        if (nodes.contains(node)) {
            nodes.remove(node);
        } else {
            for (IRepositoryNode n : nodes) {
                removeNode(n, node);
            }
        }
    }

    private void initNodesFromComponentSerivice(RepositoryNode curParentNode) {
        IGenericWizardService wizardService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
        }
        if (wizardService != null) {
            List<RepositoryNode> nodes = wizardService.createNodesFromComponentService(curParentNode);
            for (RepositoryNode repNode : nodes) {
                ERepositoryObjectType repType = (ERepositoryObjectType) repNode.getProperties(EProperties.CONTENT_TYPE);
                if (repType != null) {
                    genericNodesMap.put(repType.getType(), repNode);
                }
            }
        }
    }

    private void initExtensionRepositoryNodes(final IRepositoryNode curParentNode) {
        Map<ERepositoryObjectType, RepositoryNode> repositoryNodeExtensionMap = new HashMap<ERepositoryObjectType, RepositoryNode>();

        RepositoryNodeProviderRegistryReader repoReader = RepositoryNodeProviderRegistryReader.getInstance();
        ERepositoryObjectType[] allDynamicTypes = repoReader.getAllDynamicTypes();

        for (ERepositoryObjectType dynamicType : allDynamicTypes) {
            ENodeType nodeType = repoReader.getNodeType(dynamicType);
            RepositoryNode dynamicNode = new RepositoryNode(null, this, nodeType);

            dynamicNode.setProperties(EProperties.LABEL, dynamicType);
            dynamicNode.setProperties(EProperties.CONTENT_TYPE, dynamicType);

            IExtendRepositoryNode extendNode = repoReader.getExtendNodeByType(dynamicType);
            if (extendNode != null) {
                dynamicNode.setIcon(extendNode.getNodeImage());

                RepositoryNode[] children = (RepositoryNode[]) extendNode.getChildren();
                if (children != null && (children.length > 0)) {
                    for (RepositoryNode nodeToAdd : children) {
                        dynamicNode.getChildren().add(nodeToAdd);
                        nodeToAdd.setParent(dynamicNode);
                        nodeToAdd.setRoot(this);
                    }
                }
            }
            repositoryNodeExtensionMap.put(dynamicType, dynamicNode);
        }

        // init the existed map for extension
        collectRepositoryNodes(curParentNode);
        //
        for (ERepositoryObjectType dynamicType : repositoryNodeExtensionMap.keySet()) {
            RepositoryNode dynamicNode = repositoryNodeExtensionMap.get(dynamicType);
            ERepositoryObjectType[] parentTypes = dynamicType.getParentTypesArray();
            if (parentTypes != null && parentTypes.length > 0) {
                for (ERepositoryObjectType parentType : parentTypes) {
                    RepositoryNode parentNode = null;
                    if (repositoryNodeExtensionMap.containsKey(parentType)) {
                        // the parent is dynamic node too.
                        parentNode = repositoryNodeExtensionMap.get(parentType);
                    } else {
                        parentNode = getRootRepositoryNode(parentType);
                    }
                    if (parentNode != null) {
                        parentNode.getChildren().add(dynamicNode);
                    }
                }
            } else { // add in root of project
                curParentNode.getChildren().add(dynamicNode);
            }
        }
    }

    /**
     * DOC nrousseau Comment method "initializeChildren".
     * 
     * @param parent
     */
    public void initializeChildren(Object parent) {
        initializeChildren(project, parent);
        if (PluginChecker.isRefProjectLoaded() && getMergeRefProject()) {
            intitializeRefProject(project, parent);
        }
    }

    private void intitializeRefProject(org.talend.core.model.general.Project project, Object parent) {
        if (parent instanceof IRepositoryNode && ((IRepositoryNode) parent).isBin()) {
            return; // don't check the deleted item for ref-projects
        }
        for (ProjectReference refProject : project.getProjectReferenceList()) {
            Project p = refProject.getReferencedProject();
            // no need caching
            /*
             * List<Project> list = nodeAndProject.get(parent); if (list == null) { list = new ArrayList<Project>();
             * nodeAndProject.put(parent, list); } if (list.contains(p)) { return; } else { list.add(p); }
             */
            initializeChildren(new org.talend.core.model.general.Project(p), parent);
            intitializeRefProject(new org.talend.core.model.general.Project(p), parent);
        }

    }

    public void clearNodeAndProjectCash() {
        nodeAndProject.clear();
    }

    public void initializeChildren(org.talend.core.model.general.Project newProject, Object parent) {
        try {
            if (parent == docNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.DOCUMENTATION, true), docNode,
                        ERepositoryObjectType.DOCUMENTATION);
            } else if (parent == metadataConNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_CONNECTIONS, true),
                        metadataConNode, ERepositoryObjectType.METADATA_CONNECTIONS);
            } else if (parent == metadataSAPConnectionNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_SAPCONNECTIONS, true),
                        metadataSAPConnectionNode, ERepositoryObjectType.METADATA_SAPCONNECTIONS);
            } else if (parent == metadataEbcdicConnectionNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_FILE_EBCDIC, true),
                        metadataEbcdicConnectionNode, ERepositoryObjectType.METADATA_FILE_EBCDIC);
            } else if (parent == metadataHL7ConnectionNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_FILE_HL7, true),
                        metadataHL7ConnectionNode, ERepositoryObjectType.METADATA_FILE_HL7);
            } else if (parent == metadataBRMSConnectionNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_FILE_BRMS, true),
                        metadataBRMSConnectionNode, ERepositoryObjectType.METADATA_FILE_BRMS);
            } else if (parent == sqlPatternNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.SQLPATTERNS, true), sqlPatternNode,
                        ERepositoryObjectType.SQLPATTERNS);
            } else if (parent == metadataFileNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_FILE_DELIMITED, true),
                        metadataFileNode, ERepositoryObjectType.METADATA_FILE_DELIMITED);
            } else if (parent == metadataRulesNode) { // feature 6484 added
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_FILE_RULES, true),
                        metadataRulesNode, ERepositoryObjectType.METADATA_FILE_RULES);
            } else if (parent == metadataValidationRulesNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_VALIDATION_RULES, true),
                        metadataValidationRulesNode, ERepositoryObjectType.METADATA_VALIDATION_RULES);
            } else if (parent == metadataEDIFactConnectionNode) {
                convert(newProject, factory.getMetadata(newProject, ERepositoryObjectType.METADATA_EDIFACT, true),
                        metadataEDIFactConnectionNode, ERepositoryObjectType.METADATA_EDIFACT);
            } else if (parent instanceof StableRepositoryNode
                    && ((StableRepositoryNode) parent).getContentType() == ERepositoryObjectType.CODE) {
                // do nothing
            } else if (parent == refProject) {
                if (!getMergeRefProject()) {
                    handleReferenced(refProject);
                }
            } else if (parent == recBinNode) {
                List<RepositoryNode> foldersList = new ArrayList<RepositoryNode>();
                if (newProject != null && newProject.getEmfProject() != null) {
                    List<FolderItem> folderItems = ProjectManager.getInstance().getFolders(newProject.getEmfProject());
                    for (FolderItem folder : new ArrayList<FolderItem>(folderItems)) {
                        addItemToRecycleBin(newProject, (RepositoryNode) parent, folder, foldersList);
                    }
                }
            } else if (parent instanceof RepositoryNode) {
                RepositoryNode repositoryNode = (RepositoryNode) parent;
                ERepositoryObjectType contentType = repositoryNode.getContentType();
                if (contentType != null && contentType.isResourceItem()) {
                    RootContainer<String, IRepositoryViewObject> container = factory.getObjectFromFolder(newProject, contentType, null, options);
                    convert(newProject, container, repositoryNode, contentType);
                    addExtraChildren(contentType, newProject,repositoryNode);
                }
            }
        } catch (PersistenceException e) {
            RuntimeExceptionHandler.process(e);
        }
    }
    
    private void addExtraChildren(ERepositoryObjectType contentType, org.talend.core.model.general.Project newProject, 
            RepositoryNode repositoryNode) throws PersistenceException{
        if(contentType != ERepositoryObjectType.METADATA_CONNECTIONS){
            return;
        }
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            extraTypes.addAll(dbService.getExtraTypes());
        }
        for(ERepositoryObjectType extraType : extraTypes){
            convert(newProject, factory.getMetadata(newProject, extraType, true), repositoryNode, contentType, true);
        }
    }

    private ERepositoryObjectType getFolderContentType(FolderItem folderItem) {
        if (!folderItem.getType().equals(FolderType.SYSTEM_FOLDER_LITERAL)) {
            if (!(folderItem.getParent() instanceof FolderItem)) {
                return null; // appears only for a folder for expression builder !
            }
            return getFolderContentType((FolderItem) folderItem.getParent());
        }
        for (ERepositoryObjectType objectType : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            String folderName;
            try {
                folderName = ERepositoryObjectType.getFolderName(objectType);
            } catch (Exception e) {
                // just catch exception to avoid the types who don't have folders
                continue;
            }
            if (folderName.contains("/")) { //$NON-NLS-1$
                String[] folders = folderName.split("/"); //$NON-NLS-1$
                FolderItem currentFolderItem = folderItem;
                boolean found = true;
                for (int i = folders.length - 1; i >= 0; i--) {
                    if (!currentFolderItem.getProperty().getLabel().equals(folders[i])) {
                        found = false;
                        break;
                    }
                    if (i > 0) {
                        if (!(currentFolderItem.getParent() instanceof FolderItem)) {
                            found = false;
                            break;
                        }
                        currentFolderItem = (FolderItem) currentFolderItem.getParent();
                    }
                }
                if (found) {
                    return objectType;
                }
            } else {
                if (folderName.equals(folderItem.getProperty().getLabel())) {
                    return objectType;
                }
            }
        }
        if (folderItem.getParent() instanceof FolderItem) {
            return getFolderContentType((FolderItem) folderItem.getParent());
        }
        return null;
    }

    public void buildDeletedItemsTree(RepositoryNode rootNode) {
        // build deleted folders
        // need to use the current project to avoid the cache influence
        List<String> folders = RecycleBinManager.getInstance()
                .getDeletedFolders(ProjectManager.getInstance().getCurrentProject());
        Collections.sort(folders);
        for (String fullPath : folders) {
            String folderPath = null;
            final IPath fPath = new Path(fullPath);
            ERepositoryObjectType currentType = null;
            for (DynaEnum<? extends DynaEnum<?>> type : ERepositoryObjectType.values()) {
                ERepositoryObjectType objectType = (ERepositoryObjectType) type;
                if (objectType.isResouce()) {
                    IPath typePath = new Path(objectType.getFolder());
                    if (typePath.isPrefixOf(fPath)) {
                        folderPath = fPath.makeRelativeTo(typePath).toString();
                        currentType = objectType;
                        break;
                    }
                }
            }

            if (currentType != null) {
                buildFolders(rootNode, currentType, folderPath, rootNode);
            }
        }
        addDeletedElements(rootNode, rootNode.getChildren());
    }

    /**
     * DOC nrousseau Comment method "addDeletedElements".
     * 
     * @param project2
     * @param nodes
     */
    private void addDeletedElements(RepositoryNode rootNode, List<IRepositoryNode> rootNodes) {
        List<IRepositoryViewObject> objects = RecycleBinManager.getInstance().getDeletedObjects(rootNode.getRoot().getProject());
        List<IRepositoryViewObject> elements = new ArrayList<IRepositoryViewObject>();
        ITestContainerProviderService testContainerService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            testContainerService = (ITestContainerProviderService) GlobalServiceRegister.getDefault()
                    .getService(ITestContainerProviderService.class);
        }
        for (IRepositoryViewObject currentObject : objects) {
            if (testContainerService == null
                    || !testContainerService.isTestContainerType(currentObject.getRepositoryObjectType())) {
                elements.add(currentObject);
            }
        }

        for (IRepositoryViewObject currentObject : elements) {
            RepositoryNode parent = getFolder(currentObject.getRepositoryObjectType(), currentObject.getPath(), rootNodes);
            RepositoryNode parentNode = parent;
            if (parentNode == null) {
                parentNode = rootNode;
            }
            if (currentObject.isDeleted()) {
                RepositoryNode repNode = new RepositoryNode(new RepositoryViewObject(currentObject.getProperty()), parentNode,
                        ENodeType.REPOSITORY_ELEMENT);
                repNode.setProperties(EProperties.CONTENT_TYPE, currentObject.getRepositoryObjectType());
                repNode.setProperties(EProperties.LABEL, currentObject.getLabel());
                parentNode.getChildren().add(repNode);
                repNode.setParent(parentNode);
            } else {
                addDeletedSubItems(currentObject.getProperty().getItem(), parentNode);
            }
        }
        objects.removeAll(elements);
        addDeletedTestCases(rootNode, objects);
    }

    /**
     * DOC nrousseau Comment method "addDeletedElements".
     * 
     * @param project2
     * @param nodes
     */
    private void addDeletedTestCases(RepositoryNode rootNode, List<IRepositoryViewObject> elements) {
        List<IRepositoryNode> rootNodes = rootNode.getChildren();
        ITestContainerProviderService testContainerService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            testContainerService = (ITestContainerProviderService) GlobalServiceRegister.getDefault()
                    .getService(ITestContainerProviderService.class);
        }
        for (IRepositoryViewObject currentObject : elements) {
            if (testContainerService != null
                    && testContainerService.isTestContainerType(currentObject.getRepositoryObjectType())) {
                String originalID = testContainerService.getOriginalID(currentObject);
                RepositoryNode parentNode = getTestCaseParent(rootNodes, originalID);
                if (parentNode == null) {
                    parentNode = rootNode;
                }
                if (currentObject.isDeleted()) {
                    RepositoryNode repNode = new RepositoryNode(new RepositoryViewObject(currentObject.getProperty()), parentNode,
                            ENodeType.REPOSITORY_ELEMENT);
                    repNode.setProperties(EProperties.CONTENT_TYPE, currentObject.getRepositoryObjectType());
                    repNode.setProperties(EProperties.LABEL, currentObject.getLabel());
                    parentNode.getChildren().add(repNode);
                    repNode.setParent(parentNode);
                }
            }
        }
    }

    public boolean hasDeletedSubItem(ConnectionItem connectionItem) {
        RepositoryNode repNode = new RepositoryNode(null, null, ENodeType.SIMPLE_FOLDER);
        addDeletedSubItems(connectionItem, repNode);
        return !repNode.getChildren().isEmpty();
    }

    /**
     * DOC hwang Comment method "getTestCaseParent".
     * 
     * @param repositoryObjectType
     * @param path
     * @return
     */

    private RepositoryNode getTestCaseParent(List<IRepositoryNode> rootNodes, String originalID) {
        for (IRepositoryNode node : rootNodes) {
            if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                if (node.getId().equals(originalID)) {
                    return (RepositoryNode) node;
                }
            } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
                RepositoryNode pNode = getTestCaseParent(node.getChildren(), originalID);
                if (pNode != null) {
                    return pNode;
                }
            }
        }
        return null;
    }

    /**
     * DOC nrousseau Comment method "getFolder".
     * 
     * @param repositoryObjectType
     * @param path
     * @return
     */
    private RepositoryNode getFolder(ERepositoryObjectType currentType, String path, List<IRepositoryNode> rootNodes) {
        if (RepositoryNodeUtilities.isGenericDBExtraType(currentType)) {
            currentType = ERepositoryObjectType.METADATA_CONNECTIONS;
        }
        if (path == null || path.isEmpty()) {
            return null;
        }
        RepositoryNode folderNode = null;
        String folderName;
        String remainingPath = path;
        if (path.contains("/")) {
            folderName = path.substring(0, path.indexOf("/"));
            remainingPath = path.substring(path.indexOf("/") + 1);
        } else {
            folderName = path;
            remainingPath = null;
        }
        for (IRepositoryNode node : rootNodes) {
            if (node.getType() == ENodeType.SIMPLE_FOLDER && node.getProperties(EProperties.CONTENT_TYPE).equals(currentType)) {
                if (node.getProperties(EProperties.LABEL).equals(folderName)) {
                    folderNode = (RepositoryNode) node;
                    break;
                }
                if (remainingPath != null && node.getObject().getPath() != null && !node.getObject().getPath().isEmpty()) {
                    String fullPath = node.getObject().getPath() + "/" + node.getProperties(EProperties.LABEL);
                    if (fullPath.equals(path)) {
                        folderNode = (RepositoryNode) node;
                        remainingPath = null;
                        break;
                    } else if (path.startsWith(fullPath)) {
                        folderNode = (RepositoryNode) node;
                        remainingPath = path.substring(fullPath.length() + 1);
                        break;
                    }
                }
            }
        }
        if (folderNode != null) {
            if (remainingPath == null) {
                return folderNode;
            }
            return getFolder(currentType, remainingPath, folderNode.getChildren());
        }

        return null;
    }

    /**
     * DOC nrousseau Comment method "buildFolders".
     * 
     * @param currentType
     * @param path
     * @param nodes
     */
    private void buildFolders(RepositoryNode rootNode, ERepositoryObjectType currentType, String path,
            RepositoryNode previousNode) {
        String originalPath;
        if (path.contains("/")) {
            originalPath = path.substring(0, path.lastIndexOf("/"));
        } else {
            originalPath = "";
        }

        RepositoryNode folderNode = getFolder(currentType, originalPath, rootNode.getChildren());
        RepositoryNode parentNode = folderNode;
        if (folderNode == null) {
            parentNode = rootNode;
        }

        FolderItem item = ProxyRepositoryFactory.getInstance().getFolderItem(project, currentType, new Path(path));
        if (item == null) {
            return;
        }
        item.getState().setDeleted(true);
        Folder folder = new Folder(item.getProperty(), currentType);

        folderNode = new RepositoryNode(folder, parentNode, ENodeType.SIMPLE_FOLDER);
        folderNode.setProperties(EProperties.CONTENT_TYPE, currentType);
        folderNode.setProperties(EProperties.LABEL, folder.getLabel());
        parentNode.getChildren().add(folderNode);
        folderNode.setParent(parentNode);
    }

    private void addItemToRecycleBin(org.talend.core.model.general.Project newProject, RepositoryNode parentNode, Item item,
            List<RepositoryNode> foldersList) {
        if (isGeneratedJobItem(item)) {
            return;
        }
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
        RepositoryNode currentParentNode = parentNode;
        if (item instanceof FolderItem) {
            itemType = getFolderContentType((FolderItem) item);
            if (itemType == null) {
                return;
            }

            // MOD qiongli 2011-1-21 filter TDQ root folder.
            if (itemType != null && itemType.isDQItemType() && !itemType.isSharedType()) {
                return;
            } // ~
            if (item.getState().isDeleted()) {
                // need to display this folder in the recycle bin.
                Folder folder = new Folder(item.getProperty(), itemType);
                RepositoryNode folderNode = null;
                for (RepositoryNode existingFolder : foldersList) {
                    if (existingFolder.getContentType() == null) {
                        // this can appear temporary when another user has deleted a folder
                        break;
                    }
                    if (existingFolder.getContentType().equals(folder.getRepositoryObjectType())
                            && existingFolder.getProperties(EProperties.LABEL).equals(folder.getLabel())) {
                        folderNode = existingFolder;
                        break;
                    }
                }

                if (folderNode == null) {
                    folderNode = new RepositoryNode(folder, parentNode, ENodeType.SIMPLE_FOLDER);
                    folderNode.setProperties(EProperties.CONTENT_TYPE, itemType);
                    folderNode.setProperties(EProperties.LABEL, folder.getLabel());
                    foldersList.add(folderNode);
                    parentNode.getChildren().add(folderNode);
                    folderNode.setParent(parentNode);
                }

                for (Item curItem : (List<Item>) new ArrayList(((FolderItem) item).getChildren())) {
                    addItemToRecycleBin(newProject, folderNode, curItem, foldersList);
                }
                currentParentNode = folderNode;
            } else {
                for (Item curItem : (List<Item>) new ArrayList(((FolderItem) item).getChildren())) {
                    addItemToRecycleBin(newProject, parentNode, curItem, foldersList);
                }
            }
        } else if (item.getState() != null && item.getState().isDeleted()) {
            try {
                IRepositoryViewObject lastVersion = ProxyRepositoryFactory.getInstance().getLastVersion(newProject,
                        item.getProperty().getId());
                if (lastVersion != null && item.getProperty().getVersion().equals(lastVersion.getVersion())) {
                    RepositoryNode repNode = new RepositoryNode(new RepositoryViewObject(item.getProperty()), currentParentNode,
                            ENodeType.REPOSITORY_ELEMENT);
                    repNode.setProperties(EProperties.CONTENT_TYPE, itemType);
                    repNode.setProperties(EProperties.LABEL, item.getProperty().getLabel());
                    currentParentNode.getChildren().add(repNode);
                    repNode.setParent(currentParentNode);
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }

        } else {// bug 17768
            addDeletedSubItems(item, currentParentNode);
        }
    }

    /**
     * DOC nrousseau Comment method "addDeletedSubItems".
     * 
     * @param item
     * @param currentParentNode
     */
    private void addDeletedSubItems(Item item, RepositoryNode currentParentNode) {
        if (item instanceof ConnectionItem) {
            Connection connection = ((ConnectionItem) item).getConnection();

            // for sap
            if (connection instanceof SAPConnection) {
                SAPConnection sapConnection = (SAPConnection) connection;
                EList<SAPFunctionUnit> funtions = sapConnection.getFuntions();
                if (funtions != null) {
                    for (int i = 0; i < funtions.size(); i++) {
                        SAPFunctionUnit unit = funtions.get(i);
                        if (SubItemHelper.isDeleted(unit)) {
                            RepositoryNode tableNode = createSAPNode(new RepositoryViewObject(item.getProperty()),
                                    currentParentNode, unit);
                            currentParentNode.getChildren().add(tableNode);
                            tableNode.setParent(currentParentNode);
                        } else {
                            for (MetadataTable table : ConnectionHelper.getTables(connection, unit)) {
                                if (SubItemHelper.isDeleted(table)) {
                                    RepositoryNode tableNode = createMetatableNode(currentParentNode,
                                            new RepositoryViewObject(item.getProperty()), table,
                                            ERepositoryObjectType.METADATA_CON_TABLE);
                                    currentParentNode.getChildren().add(tableNode);
                                    tableNode.setParent(currentParentNode);
                                }
                            }
                        }
                    }
                }
                EList<SAPIDocUnit> iDocs = sapConnection.getIDocs();
                if (iDocs != null) {
                    for (int i = 0; i < iDocs.size(); i++) {
                        SAPIDocUnit unit = iDocs.get(i);
                        if (SubItemHelper.isDeleted(unit)) {
                            RepositoryNode tableNode = createSAPNode(new RepositoryViewObject(item.getProperty()),
                                    currentParentNode, unit);
                            currentParentNode.getChildren().add(tableNode);
                            tableNode.setParent(currentParentNode);
                        }
                    }
                }
            }

            Set<MetadataTable> tables = null;
            IGenericWizardService wizardService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
                wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault()
                        .getService(IGenericWizardService.class);
            }
            if (wizardService != null && wizardService.isGenericItem(item)) {
                List<MetadataTable> metadataTables = wizardService.getMetadataTables(connection);
                tables = new HashSet<>(metadataTables);
            } else {
                tables = ConnectionHelper.getTables(connection);
            }
            for (MetadataTable table : tables) {
                if (SubItemHelper.isDeleted(table)) {
                    RepositoryNode tableNode = createMetatableNode(currentParentNode,
                            new RepositoryViewObject(item.getProperty()), table, ERepositoryObjectType.METADATA_CON_TABLE);
                    currentParentNode.getChildren().add(tableNode);
                    tableNode.setParent(currentParentNode);
                }
            }

            if (connection != null) {
                QueriesConnection queriesConnection = connection.getQueries();
                if (queriesConnection != null) {
                    for (Query query : queriesConnection.getQuery()) {
                        if (SubItemHelper.isDeleted(query)) {
                            RepositoryNode queryNode = createQueryNode(currentParentNode,
                                    new RepositoryViewObject(item.getProperty()), query);
                            currentParentNode.getChildren().add(queryNode);
                            queryNode.setParent(currentParentNode);
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * ggu Comment method "isGeneratedJobItem".
     * 
     * feature 4393
     */
    private boolean isGeneratedJobItem(Item item) {
        if (item != null) {
            if (item instanceof JobDocumentationItem || item instanceof JobletDocumentationItem) {
                return true;
            }
        }
        return false;
    }

    private RepositoryNode getSQLPatternNode(String parentLabel, String label) {
        if (getMergeRefProject()) {
            List<IRepositoryNode> sqlChildren = getRootRepositoryNode(ERepositoryObjectType.SQLPATTERNS).getChildren();
            // List<RepositoryNode> sqlChildren = parent.getChildren();

            for (IRepositoryNode sqlChild : sqlChildren) {
                if (label.equalsIgnoreCase(sqlChild.toString())) {
                    return (RepositoryNode) sqlChild;
                }
                for (IRepositoryNode userDefined : sqlChild.getChildren()) {
                    if (label.equalsIgnoreCase(((RepositoryNode) userDefined).getProperties(EProperties.LABEL).toString())) {
                        if (sqlChild.toString().equalsIgnoreCase(parentLabel)) {
                            return (RepositoryNode) userDefined;
                        }
                    }

                }
            }
        }
        return null;
    }

    private List<FolderItem> getDelFolderItems(List list, List<FolderItem> delFolderItems) {
        for (Object obj : list) {
            if (obj instanceof FolderItem) {
                FolderItem folderItem = (FolderItem) obj;
                if (folderItem.getState().isDeleted()) {
                    delFolderItems.add(folderItem);
                } else if (folderItem.getChildren() != null && !folderItem.getChildren().isEmpty()) {
                    getDelFolderItems(folderItem.getChildren(), delFolderItems);
                }
            }
        }
        return delFolderItems;
    }

    private void handleDelFolderItems(org.talend.core.model.general.Project newProject, RepositoryNode parent) {
        if (!delFolderItems.isEmpty()) {
            delFolderItems.clear();
        }
        if (newProject != null && newProject.getEmfProject() != null) {
            List<FolderItem> folderItems = ProjectManager.getInstance().getFolders(newProject.getEmfProject());
            for (FolderItem folder : folderItems) {
                String folderName = folder.getProperty().getLabel();
                if (("process".equals(folderName) || "joblets".equals(folderName)) && folder.getChildren() != null
                        && !folder.getChildren().isEmpty()) {
                    getDelFolderItems(folder.getChildren(), delFolderItems);
                }
            }
        }
    }
    
    private void convert(org.talend.core.model.general.Project newProject, Container fromModel, RepositoryNode parent,
            ERepositoryObjectType type) {
        convert(newProject, fromModel, parent, type, false);
    }

    private void convert(org.talend.core.model.general.Project newProject, Container fromModel, RepositoryNode parent,
            ERepositoryObjectType type, boolean extra) {

        if (parent == null || fromModel == null) {
            return;
        }

        if (type.equals(ERepositoryObjectType.DOCUMENTATION)) {
            handleDelFolderItems(newProject, parent);
        }

        for (Object obj : fromModel.getSubContainer()) {
            Container container = (Container) obj;
            Folder oFolder = new Folder((Property) container.getProperty(), type);
            boolean found = false;
            // add for bug TDI-26084, whether or not hide folders under job_doc/joblet_doc.
            if (type.equals(ERepositoryObjectType.JOB_DOC) || type.equals(ERepositoryObjectType.JOBLET_DOC)) {
                for (FolderItem delFolder : delFolderItems) {
                    String parentName = ((FolderItem) delFolder.getParent()).getProperty().getLabel();
                    String oFolderPath = oFolder.getPath();
                    String jobPath = "jobs" + delFolder.getState().getPath();
                    String jobPath1 = "jobs/" + delFolder.getState().getPath();
                    String jobletPath = "joblets" + delFolder.getState().getPath();
                    String jobletPath1 = "joblets/" + delFolder.getState().getPath();
                    if (oFolder.getLabel().equals(delFolder.getProperty().getLabel())) {
                        if ("process".equals(parentName) && oFolderPath.equals(jobPath)) {
                            found = true;
                        } else if ("joblets".equals(parentName) && oFolderPath.equals(jobletPath)) {
                            found = true;
                        } else if (oFolderPath.equals(jobPath1) || oFolderPath.equals(jobletPath1)) {
                            found = true;
                        }
                        break;
                    }
                }
            }
            if (found) {
                continue;
            }
            if (oFolder.getProperty() == null) {
                continue;
            }

            RepositoryNode folder = null;

            String label = container.getLabel();
            if (label.equals("bin") || label.startsWith(".")) { //$NON-NLS-1$ //$NON-NLS-2$
                continue;
            }
            // for system folder
            if (RepositoryConstants.SYSTEM_DIRECTORY.equals(label)) {
                if (getMergeRefProject()) {
                    parent.getChildren();
                    boolean existSystemFolder = false;
                    for (IRepositoryNode node : parent.getChildren()) {
                        if (RepositoryConstants.SYSTEM_DIRECTORY.equalsIgnoreCase(node.getLabel())) {
                            existSystemFolder = true;
                            break;
                        }
                    }
                    IBrandingService breaningService = (IBrandingService) GlobalServiceRegister.getDefault()
                            .getService(IBrandingService.class);

                    if (!existSystemFolder && !breaningService.isPoweredOnlyCamel()) {
                        folder = new StableRepositoryNode(parent, RepositoryConstants.SYSTEM_DIRECTORY,
                                ECoreImage.FOLDER_CLOSE_ICON);
                        parent.getChildren().add(folder);
                    } else {
                        continue;
                    }
                } else {

                    folder = new StableRepositoryNode(parent, RepositoryConstants.SYSTEM_DIRECTORY, ECoreImage.FOLDER_CLOSE_ICON);
                    parent.getChildren().add(folder);
                }

            } else
            // ERepositoryObjectType.GENERATED
            if (type.equals(ERepositoryObjectType.DOCUMENTATION) && label.equalsIgnoreCase("generated")) {//$NON-NLS-1$
                // if (PluginChecker.isDocumentationPluginLoaded()) {
                // use CNF content provider instead
                // convertDocumentation(newProject, container, parent, type);
                // }
                continue;
            } else {
                if (getMergeRefProject()) {
                    String a = parent.getProperties(EProperties.LABEL).toString();
                    if (type == ERepositoryObjectType.SQLPATTERNS) {
                        folder = getSQLPatternNode(a, label);
                    }
                    if (folder == null) {
                        if (newProject != this.project && !hasTalendItems(container)) {
                            continue;
                        }
                        folder = avoidReCreateFolder(parent, oFolder, extra);
                        if(folder == null){
                            folder = new RepositoryNode(oFolder, parent, ENodeType.SIMPLE_FOLDER);
                            if (factory.getStatus(oFolder) != ERepositoryStatus.DELETED) {
                                parent.getChildren().add(folder);
                            } 
                        }
                    }
                } else {
                    folder = avoidReCreateFolder(parent, oFolder, extra);
                    if(folder == null){
                        folder = new RepositoryNode(oFolder, parent, ENodeType.SIMPLE_FOLDER);
                        if (factory.getStatus(oFolder) != ERepositoryStatus.DELETED) {
                            parent.getChildren().add(folder);
                        } 
                    }
                    
                }

            }
            folder.setProperties(EProperties.LABEL, label);
            folder.setProperties(EProperties.CONTENT_TYPE, type);
            convert(newProject, container, folder, type);
        }
        List<IRepositoryViewObject> validationRules = null;

        try {
            // note: could be optimized and get this only for metadata related repository types.
            // but the loss of performance is really minimized still here.
            validationRules = factory.getAll(ERepositoryObjectType.METADATA_VALIDATION_RULES);
        } catch (PersistenceException e2) {
            ExceptionHandler.process(e2);
        }

        // not folder or folders have no subFolder
        for (Object obj : fromModel.getMembers()) {
            IRepositoryViewObject repositoryObject = (IRepositoryViewObject) obj;
            try {
                if (!repositoryObject.isDeleted()) {
                    addNode(parent, type, repositoryObject, validationRules);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
                ExceptionHandler
                        .log(Messages.getString("ProjectRepositoryNode.itemInvalid", repositoryObject.getRepositoryObjectType(), //$NON-NLS-1$ ,
                                repositoryObject.getLabel()));
            }
        }
    }
    
    private RepositoryNode avoidReCreateFolder(RepositoryNode parent, Folder oFolder, boolean extra){
        RepositoryNode folder = null;
        if(extra){
            for(IRepositoryNode child : parent.getChildren()){
                if(child.getType() == ENodeType.SIMPLE_FOLDER && child.getObject().getLabel().equals(oFolder.getLabel())){
                    folder = (RepositoryNode)child;
                    break;
                }
            }
        }
        return folder;
    }

    private boolean hasTalendItems(Container container) {
        if (!container.getMembers().isEmpty()) {
            return true;
        }
        for (Object obj : container.getSubContainer()) {
            Container subContainer = (Container) obj;
            boolean hasTalendItems = hasTalendItems(subContainer);
            if (!hasTalendItems) {
                continue;
            }
            return hasTalendItems;
        }

        return false;

    }

    private void handleReferenced(RepositoryNode parent) {
        if (parent.getType().equals(ENodeType.SYSTEM_FOLDER)) {
            for (ProjectReference refProject : project.getProjectReferenceList()) {
                if (ProjectManager.validReferenceProject(project.getEmfProject(), refProject)) {
                    Project emfProject = refProject.getReferencedProject();
                    ProjectRepositoryNode referencedProjectNode = new ProjectRepositoryNode(
                            new org.talend.core.model.general.Project(emfProject), null, parent, this,
                            ENodeType.REFERENCED_PROJECT);
                    referencedProjectNode.setProperties(EProperties.LABEL, emfProject.getLabel());
                    referencedProjectNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.REFERENCED_PROJECTS);
                    parent.getChildren().add(referencedProjectNode);
                    // fix the bug for Ref-project
                    // TDI-23358, revert to before
                    // ProjectManager.getInstance().updateViewProjectNode(referencedProjectNode);

                    referencedProjectNode.initialize(currentPerspective);
                }
            }
        }

    }

    private void addNode(RepositoryNode parent, ERepositoryObjectType type, IRepositoryViewObject repositoryObject,
            List<IRepositoryViewObject> validationRules) {

        boolean isAvaliableInTOS = true; // this flag filter the databaseconnections which didn't supported by TOS but
        DatabaseConnection dbMetadataConnection = null;
        // TOP
        if (type == ERepositoryObjectType.METADATA_CONNECTIONS) {
            Connection conn = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
            if(conn instanceof DatabaseConnection){
                dbMetadataConnection = (DatabaseConnection) conn;
                isAvaliableInTOS = EDatabaseTypeName.getTypeFromDbType(dbMetadataConnection.getDatabaseType(), false) == null ? false
                        : true;
            }
        }

        Connection connection = null;
        if (type == ERepositoryObjectType.METADATA_CONNECTIONS && isAvaliableInTOS) {
            connection = dbMetadataConnection;
        } else if (type == ERepositoryObjectType.METADATA_SAPCONNECTIONS) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_POSITIONAL) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_REGEXP) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_XML) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_EXCEL) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_LDIF) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_LDAP_SCHEMA) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_GENERIC_SCHEMA) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_WSDL_SCHEMA) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_EBCDIC) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_HL7) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_FTP) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_FILE_BRMS) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_MDMCONNECTION) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        } else if (type == ERepositoryObjectType.METADATA_EDIFACT) {
            connection = ((ConnectionItem) repositoryObject.getProperty().getItem()).getConnection();
        }

        RepositoryNode node = createRepositoryNode(parent, type, repositoryObject, connection);
        if ((repositoryObject.isDeleted() && parent.getObject() == null)
                || (repositoryObject.isDeleted() && !parent.getObject().isDeleted())) {
            // recBinNode.getChildren().add(node);
            // node.setParent(recBinNode);
        } else {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                        .getService(IDesignerCoreService.class);
                if (designerCoreService != null) {
                    for (IRepositoryNode repositoryNode : parent.getChildren()) {
                        if (repositoryNode.getObject() != null) {
                            if (repositoryNode.getObject().getId().equals(repositoryObject.getId())) {
                                Problem problem = new Problem();
                                problem.setDescription(type.name() + " " + repositoryNode.getObject().getLabel() + " " //$NON-NLS-1$ //$NON-NLS-2$
                                        + repositoryNode.getObject().getVersion() + " and " + repositoryObject.getLabel() + " " //$NON-NLS-1$ //$NON-NLS-2$
                                        + repositoryObject.getVersion() + " have the same ID."); //$NON-NLS-1$
                                problem.setStatus(ProblemStatus.WARNING);
                                designerCoreService.addProblems(problem);
                            }
                        }
                    }
                }
            }
            if (isAvaliableInTOS) {
                parent.getChildren().add(node);
            }
        }

        if (null != connection) {
            createTables(node, repositoryObject, connection, validationRules);
        }

        for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
            handler.addNode(type, recBinNode, repositoryObject, node);
        }
    }

    private RepositoryNode createRepositoryNode(RepositoryNode parent, ERepositoryObjectType repObjType,
            IRepositoryViewObject repositoryObject, Connection connection) {
        RepositoryNode node = new RepositoryNode(repositoryObject, parent, ENodeType.REPOSITORY_ELEMENT);

        node.setProperties(EProperties.CONTENT_TYPE, repObjType);
        node.setProperties(EProperties.LABEL, repositoryObject.getLabel());

        if (connection instanceof SAPConnection) {
            IRepositoryNode cacheNode = nodeCache.getCache(node);
            if (cacheNode != null && cacheNode instanceof RepositoryNode) {
                node = (RepositoryNode) cacheNode;
                node.getChildren().clear();
            } else {
                nodeCache.addCache(node, true);
            }
        }
        return node;
    }

    /**
     * DOC tguiu Comment method "createTables".
     * 
     * @param node
     * @param repositoryObjectType TODO
     * @param iMetadataConnection
     * @param metadataConnection
     */
    private void createTables(RepositoryNode node, final IRepositoryViewObject repObj, EList list,
            ERepositoryObjectType repositoryObjectType, List<IRepositoryViewObject> validationRules) {
        for (Object currentTable : list) {
            if (currentTable == null) {
                continue;
            }
            if (currentTable instanceof org.talend.core.model.metadata.builder.connection.MetadataTable) {
                org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable = (org.talend.core.model.metadata.builder.connection.MetadataTable) currentTable;
                RepositoryNode tableNode = createMetatableNode(node, repObj, metadataTable, repositoryObjectType);
                if (SubItemHelper.isDeleted(metadataTable)) {
                    // no need to do anything here, the deleted table will be added in function addItemToRecycleBin
                } else {
                    node.getChildren().add(tableNode);
                }
                if (metadataTable.getColumns().size() > 0) {
                    createColumns(tableNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_COLUMN);
                    createValidationRules(tableNode, repObj, metadataTable, ERepositoryObjectType.METADATA_VALIDATION_RULES,
                            validationRules);
                }
            } else if (currentTable instanceof Query) {
                Query query = (Query) currentTable;
                RepositoryNode queryNode = createQueryNode(node, repObj, query);
                if (SubItemHelper.isDeleted(query)) {
                    // recBinNode.getChildren().add(queryNode);
                } else {
                    node.getChildren().add(queryNode);
                }

            }
        }
    }

    /**
     * DOC cantoine Comment method "createTable".
     * 
     * @param node
     * @param metadataTable
     * @param repositoryObjectType TODO
     * @param iMetadataConnection
     */
    private void createTable(RepositoryNode node, final IRepositoryViewObject repObj,
            org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable,
            ERepositoryObjectType repositoryObjectType, List<IRepositoryViewObject> validationRules) {
        if (metadataTable == null) {
            return;
        }
        RepositoryNode tableNode = createMetatableNode(node, repObj, metadataTable, repositoryObjectType);
        if (!TableHelper.isDeleted(metadataTable)) {
            node.getChildren().add(tableNode);
        }
        if (metadataTable.getColumns().size() > 0) {
            createColumns(tableNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_COLUMN);
            createValidationRules(tableNode, repObj, metadataTable, ERepositoryObjectType.METADATA_VALIDATION_RULES,
                    validationRules);
        }
    }

    private void createColumns(RepositoryNode node, final IRepositoryViewObject repObj,
            org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable,
            ERepositoryObjectType repositoryObjectType) {
        List<MetadataColumn> columnList = new ArrayList<MetadataColumn>();
        columnList.addAll(metadataTable.getColumns());
        int num = columnList.size();
        StringBuffer floderName = new StringBuffer();
        floderName.append(Messages.getString("ProjectRepositoryNode.columns"));//$NON-NLS-1$
        floderName.append("(");//$NON-NLS-1$
        floderName.append(num);
        floderName.append(")");//$NON-NLS-1$
        RepositoryNode columnsNode = new StableRepositoryNode(node, floderName.toString(), ECoreImage.FOLDER_CLOSE_ICON);
        node.getChildren().add(columnsNode);

        for (MetadataColumn column : columnList) {
            if (column == null) {
                continue;
            }
            RepositoryNode columnNode = createMataColumnNode(columnsNode, repObj, column, repositoryObjectType);
            columnsNode.getChildren().add(columnNode);

        }
    }

    private void createValidationRules(RepositoryNode node, final IRepositoryViewObject repObj,
            org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable,
            ERepositoryObjectType repositoryObjectType, List<IRepositoryViewObject> validationRules) {
        if (validationRules.isEmpty()) {
            return;
        }
        IRepositoryViewObject vo = node.getObject();
        if (vo instanceof MetadataTableRepositoryObject) {
            vo = ((MetadataTableRepositoryObject) vo).getViewObject();
        }
        if (vo != null) {
            String schema = vo.getId();
            schema = schema + " - " + metadataTable.getLabel(); //$NON-NLS-1$
            if (metadataTable instanceof SAPBWTable) {
                String innerIOType = ((SAPBWTable) metadataTable).getInnerIOType();
                if (innerIOType != null) {
                    schema = schema + " - " + innerIOType; //$NON-NLS-1$
                }
            }
            List<IRepositoryViewObject> objs = getValidationRuleObjsFromSchema(validationRules, schema);
            if (objs.size() > 0) {
                int num = objs.size();
                StringBuffer floderName = new StringBuffer();
                floderName.append(Messages.getString("ProjectRepositoryNode.validationRules")); //$NON-NLS-1$
                floderName.append("(");//$NON-NLS-1$
                floderName.append(num);
                floderName.append(")");//$NON-NLS-1$
                RepositoryNode validationRulesNode = new StableRepositoryNode(node, floderName.toString(),
                        ECoreImage.FOLDER_CLOSE_ICON);
                validationRulesNode.setProperties(EProperties.CONTENT_TYPE,
                        ERepositoryObjectType.METADATA_VALIDATIONS_RULES_FOLDER);
                node.getChildren().add(validationRulesNode);
                for (IRepositoryViewObject obj : objs) {
                    addNode(validationRulesNode, ERepositoryObjectType.METADATA_VALIDATION_RULES, obj, validationRules);
                }
            }
        }
    }

    private List<IRepositoryViewObject> getValidationRuleObjsFromSchema(List<IRepositoryViewObject> validationRules,
            String schema) {
        List<IRepositoryViewObject> objs = new ArrayList<IRepositoryViewObject>();
        if (validationRules != null && validationRules.size() > 0) {
            for (IRepositoryViewObject member : validationRules) {
                if (member != null && member.getProperty() != null) {
                    Item item = member.getProperty().getItem();
                    if (item != null && item instanceof ValidationRulesConnectionItem) {
                        ValidationRulesConnectionItem validItem = (ValidationRulesConnectionItem) item;
                        ValidationRulesConnection connection = (ValidationRulesConnection) validItem.getConnection();
                        if (connection != null && schema.equals(connection.getBaseSchema()) && !objs.contains(member)) {
                            objs.add(member);
                        }
                    }
                }
            }
        }

        return objs;
    }

    private void createTables(RepositoryNode node, final IRepositoryViewObject repObj, Connection metadataConnection,
            List<IRepositoryViewObject> validationRules) {

        // // 5.GENERIC SCHEMAS
        // RepositoryNode genericSchemaNode = new StableRepositoryNode(node, Messages
        // .getString("RepositoryContentProvider.repositoryLabel.GenericSchema"), ECoreImage.FOLDER_CLOSE_ICON);
        // node.getChildren().add(genericSchemaNode);

        if (metadataConnection instanceof DatabaseConnection) {

            // 1.Tables:
            RepositoryNode tablesNode = new StableRepositoryNode(node, Messages.getString("ProjectRepositoryNode.tableSchemas"), //$NON-NLS-1$
                    ECoreImage.FOLDER_CLOSE_ICON);
            node.getChildren().add(tablesNode);

            // 2.VIEWS:
            RepositoryNode viewsNode = new StableRepositoryNode(node, Messages.getString("ProjectRepositoryNode.viewSchemas"), //$NON-NLS-1$
                    ECoreImage.FOLDER_CLOSE_ICON);
            node.getChildren().add(viewsNode);

            // 3.SYNONYMS:
            RepositoryNode synonymsNode = new StableRepositoryNode(node,
                    Messages.getString("ProjectRepositoryNode.synonymSchemas"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
            node.getChildren().add(synonymsNode);

            // 4.CALCULATION VIEWS:
            RepositoryNode calculationViewsNode = new StableRepositoryNode(node,
                    Messages.getString("ProjectRepositoryNode.calculationViewSchemas"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
            if (PluginChecker.isTIS() && EDatabaseTypeName.SAPHana.getDisplayName()
                    .equals(((DatabaseConnection) metadataConnection).getDatabaseType())) {
                node.getChildren().add(calculationViewsNode);
            }

            DatabaseConnection dbconn = (DatabaseConnection) metadataConnection;
            List<MetadataTable> allTables = ConnectionHelper.getTablesWithOrders(dbconn);

            // /* only refresh and show tables in current schema or catalog,see bug 0015769 */
            // Set<org.talend.core.model.metadata.builder.connection.MetadataTable> allTables = ProjectNodeHelper
            // .getTablesFromSpecifiedDataPackage(dbconn);
            // /*
            // * bug 18514,if the schema is imported from file for sas databaseconnection,need retrieve again from all
            // * datapackages
            // */
            // if (allTables.isEmpty() && dbconn.getDatabaseType().equals(EDatabaseTypeName.SAS.getDisplayName())) {
            // allTables = ConnectionHelper.getTables(dbconn);
            // }

            // Iterator metadataTables = ConnectionHelper.getTables(metadataConnection).iterator();
            for (MetadataTable metadataTable : allTables) {

                // bug 17768
                if (SubItemHelper.isDeleted(metadataTable)) {
                    continue;
                }

                String typeTable = null;
                if (metadataTable != null && metadataTable.getTableType() != null) {
                    typeTable = metadataTable.getTableType();
                    if (typeTable.equals(MetadataManager.TYPE_TABLE)) {
                        createTable(tablesNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);

                    } else if (typeTable.equals(MetadataManager.TYPE_VIEW)) {
                        createTable(viewsNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);

                    } else if (typeTable.equals(MetadataManager.TYPE_SYNONYM)) {
                        createTable(synonymsNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_TABLE,
                                validationRules);
                    } else if (typeTable.equals(MetadataManager.TYPE_CALCULATION_VIEW)) {
                        createTable(calculationViewsNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_TABLE,
                                validationRules);
                    }
                    // bug 0017782 ,db2's SYNONYM need to convert to ALIAS;
                    else if (typeTable.equals(MetadataManager.TYPE_ALIAS)) {
                        createTable(synonymsNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_TABLE,
                                validationRules);
                    }
                    // else if (typeTable.equals("GENERIC_SCHEMA")) {
                    // //TODO not finished.
                    // createTable(recBinNode, tablesNode, repObj, metadataTable,
                    // ERepositoryObjectType.METADATA_CON_TABLE);
                    // }
                } else {
                    createTable(tablesNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
                }
            }

            // if (!node.getChildren().contains(tablesNode)) {
            // node.getChildren().add(tablesNode);
            // }

            // createTables(recBinNode, node, repObj, metadataConnection.getTables());

            // 4.Queries:
            boolean isImpala = EDatabaseTypeName.IMPALA.getDisplayName().equals(dbconn.getDatabaseType());
            if (!ConnectionUtils.isHiveConnection(dbconn.getURL()) || isImpala) {
                RepositoryNode queriesNode = new StableRepositoryNode(node, Messages.getString("ProjectRepositoryNode.queries"), //$NON-NLS-1$
                        ECoreImage.FOLDER_CLOSE_ICON);
                node.getChildren().add(queriesNode);
                QueriesConnection queriesConnection = (metadataConnection).getQueries();
                if (queriesConnection != null) {
                    createTables(queriesNode, repObj, queriesConnection.getQuery(), ERepositoryObjectType.METADATA_CON_TABLE,
                            validationRules);
                }
            }

            // 5. Change Data Capture
            Item item = node.getObject().getProperty().getItem();
            if (item instanceof DatabaseConnectionItem) {
                DatabaseConnectionItem connectionItem = (DatabaseConnectionItem) item;
                DatabaseConnection connection = (DatabaseConnection) connectionItem.getConnection();
                if (PluginChecker.isCDCPluginLoaded()) {
                    ICDCProviderService service = (ICDCProviderService) GlobalServiceRegister.getDefault()
                            .getService(ICDCProviderService.class);
                    if (service != null && service.canCreateCDCConnection(connection)) {
                        RepositoryNode cdcNode = new StableRepositoryNode(node,
                                Messages.getString("ProjectRepositoryNode.cdcFoundation"), //$NON-NLS-1$
                                ECoreImage.FOLDER_CLOSE_ICON);
                        node.getChildren().add(cdcNode);
                        service.createCDCTypes(recBinNode, cdcNode, connection.getCdcConns());
                    }
                }
            }
        } else if (metadataConnection instanceof SAPConnection) {
            // The sap wizard plugin is loaded
            // 1.Tables:
            createSAPTableNodes(repObj, metadataConnection, node, validationRules);

            // 2.Functions:
            StableRepositoryNode functionNode = new StableRepositoryNode(node,
                    Messages.getString("ProjectRepositoryNode.sapBapi"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
            functionNode.setChildrenObjectType(ERepositoryObjectType.METADATA_SAP_FUNCTION);
            node.getChildren().add(functionNode);

            createSAPFunctionNodes(repObj, metadataConnection, functionNode, validationRules);

            // add idocs
            StableRepositoryNode iDocNode = new StableRepositoryNode(node, Messages.getString("ProjectRepositoryNode.sapIDocs"), //$NON-NLS-1$
                    ECoreImage.FOLDER_CLOSE_ICON);
            iDocNode.setChildrenObjectType(ERepositoryObjectType.METADATA_SAP_IDOC);
            node.getChildren().add(iDocNode);
            createSAPIDocNodes(repObj, metadataConnection, iDocNode);

            // 4. BW AdvancedDataStoreObject:
            createSAPBWAdvancedDataStoreObjectNodes(repObj, metadataConnection, node, validationRules);

            // 5. BW DataSource:
            createSAPBWDataSourceNodes(repObj, metadataConnection, node, validationRules);

            // 6. BW DataStoreObject:
            createSAPBWDataStoreObjectNodes(repObj, metadataConnection, node, validationRules);

            // 7. BW InfoCube:
            createSAPBWInfoCubeNodes(repObj, metadataConnection, node, validationRules);

            // 8. BW InfoObject:
            createSAPBWInfoObjectNodes(repObj, metadataConnection, node, validationRules);

            // 8. BW Business Content Extractor:
            createSAPContentExtractorNodes(repObj, metadataConnection, node, validationRules);
        } else if (metadataConnection instanceof SalesforceSchemaConnection) {
            createSalesforceModuleNodes(repObj, metadataConnection, node, validationRules);
        } else {
            Set<org.talend.core.model.metadata.builder.connection.MetadataTable> tableset = ConnectionHelper
                    .getTables(metadataConnection);
            EList tables = new BasicEList();
            tables.addAll(tableset);
            createTables(node, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
        }
    }

    private void createSAPTableNodes(IRepositoryViewObject repObj, Connection metadataConnection, RepositoryNode node,
            List<IRepositoryViewObject> validationRules) {
        StableRepositoryNode tableContainer = new StableRepositoryNode(node,
                Messages.getString("ProjectRepositoryNode.sapTables"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        tableContainer.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        tableContainer.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_TABLE);

        IRepositoryNode cacheNode = nodeCache.getCache(tableContainer);
        if (cacheNode != null && cacheNode instanceof StableRepositoryNode) {
            tableContainer = (StableRepositoryNode) cacheNode;
            tableContainer.getChildren().clear();
        } else {
            nodeCache.addCache(tableContainer, true);
        }

        node.getChildren().add(tableContainer);

        List<MetadataTable> tablesWithOrders = ConnectionHelper.getTablesWithOrders(metadataConnection);
        EList tables = new BasicEList();
        for (MetadataTable tablesWithOrder : tablesWithOrders) {
            EMap<String, String> properties = tablesWithOrder.getAdditionalProperties();
            String partitionKey = properties.get(EProperties.CONTENT_TYPE.name());
            if (!ERepositoryObjectType.METADATA_SAP_CONTENT_EXTRACTOR.name().equals(partitionKey)) {
                tables.add(tablesWithOrder);
            }
        }
        createTables(tableContainer, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);

    }

    private void createSAPBWAdvancedDataStoreObjectNodes(IRepositoryViewObject repObj, Connection metadataConnection,
            RepositoryNode node, List<IRepositoryViewObject> validationRules) {
        StableRepositoryNode container = new StableRepositoryNode(node,
                Messages.getString("ProjectRepositoryNode.sapBWAdvancedDataStoreObject"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        container.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        container.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_BW_ADVANCEDDATASTOREOBJECT);

        IRepositoryNode cacheNode = nodeCache.getCache(container);
        if (cacheNode != null && cacheNode instanceof StableRepositoryNode) {
            container = (StableRepositoryNode) cacheNode;
            container.getChildren().clear();
        } else {
            nodeCache.addCache(container, true);
        }
        node.getChildren().add(container);

        EList<SAPBWTable> advancedDataStoreObjects = ((SAPConnection) metadataConnection).getBWAdvancedDataStoreObjects();
        EList tables = new BasicEList();
        tables.addAll(advancedDataStoreObjects);
        createTables(container, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
    }

    private void createSAPBWDataSourceNodes(IRepositoryViewObject repObj, Connection metadataConnection, RepositoryNode node,
            List<IRepositoryViewObject> validationRules) {
        StableRepositoryNode container = new StableRepositoryNode(node,
                Messages.getString("ProjectRepositoryNode.sapBWDataSource"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        container.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        container.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_BW_DATASOURCE);

        IRepositoryNode cacheNode = nodeCache.getCache(container);
        if (cacheNode != null && cacheNode instanceof StableRepositoryNode) {
            container = (StableRepositoryNode) cacheNode;
            container.getChildren().clear();
        } else {
            nodeCache.addCache(container, true);
        }

        node.getChildren().add(container);

        EList<SAPBWTable> datasources = ((SAPConnection) metadataConnection).getBWDataSources();
        EList tables = new BasicEList();
        tables.addAll(datasources);
        createTables(container, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
    }

    private void createSAPBWDataStoreObjectNodes(IRepositoryViewObject repObj, Connection metadataConnection, RepositoryNode node,
            List<IRepositoryViewObject> validationRules) {
        StableRepositoryNode container = new StableRepositoryNode(node,
                Messages.getString("ProjectRepositoryNode.sapBWDataStoreObject"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        container.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        container.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_BW_DATASTOREOBJECT);

        IRepositoryNode cacheNode = nodeCache.getCache(container);
        if (cacheNode != null && cacheNode instanceof StableRepositoryNode) {
            container = (StableRepositoryNode) cacheNode;
            container.getChildren().clear();
        } else {
            nodeCache.addCache(container, true);
        }

        node.getChildren().add(container);

        EList<SAPBWTable> dataStoreObjects = ((SAPConnection) metadataConnection).getBWDataStoreObjects();
        EList tables = new BasicEList();
        tables.addAll(dataStoreObjects);
        createTables(container, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
    }

    private void createSAPBWInfoCubeNodes(IRepositoryViewObject repObj, Connection metadataConnection, RepositoryNode node,
            List<IRepositoryViewObject> validationRules) {
        StableRepositoryNode container = new StableRepositoryNode(node, Messages.getString("ProjectRepositoryNode.sapBWInfoCube"), //$NON-NLS-1$
                ECoreImage.FOLDER_CLOSE_ICON);
        container.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        container.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_BW_INFOCUBE);

        IRepositoryNode cacheNode = nodeCache.getCache(container);
        if (cacheNode != null && cacheNode instanceof StableRepositoryNode) {
            container = (StableRepositoryNode) cacheNode;
            container.getChildren().clear();
        } else {
            nodeCache.addCache(container, true);
        }

        node.getChildren().add(container);

        EList<SAPBWTable> infoCubes = ((SAPConnection) metadataConnection).getBWInfoCubes();
        EList tables = new BasicEList();
        tables.addAll(infoCubes);
        createTables(container, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
    }

    private void createSAPBWInfoObjectNodes(IRepositoryViewObject repObj, Connection metadataConnection, RepositoryNode node,
            List<IRepositoryViewObject> validationRules) {
        StableRepositoryNode container = new StableRepositoryNode(node,
                Messages.getString("ProjectRepositoryNode.sapBWInfoObject"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        container.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        container.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_BW_INFOOBJECT);

        IRepositoryNode cacheNode = nodeCache.getCache(container);
        if (cacheNode != null && cacheNode instanceof StableRepositoryNode) {
            container = (StableRepositoryNode) cacheNode;
            container.getChildren().clear();
        } else {
            nodeCache.addCache(container, true);
        }

        node.getChildren().add(container);

        EList<SAPBWTable> infoObjects = ((SAPConnection) metadataConnection).getBWInfoObjects();
        EList<SAPBWTable> tables = new BasicEList<SAPBWTable>();
        for (SAPBWTable bwTable : infoObjects) {
            if (!SAPBWTableHelper.IO_INNERTYPE_BASIC.equals(bwTable.getInnerIOType())) {
                tables.add(bwTable);
            }
        }
        createTables(container, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
    }

    private void createSAPContentExtractorNodes(IRepositoryViewObject repObj, Connection metadataConnection, RepositoryNode node,
            List<IRepositoryViewObject> validationRules) {
        StableRepositoryNode tableContainer = new StableRepositoryNode(node,
                Messages.getString("ProjectRepositoryNode.sapContentExtractor"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        tableContainer.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        tableContainer.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_CONTENT_EXTRACTOR);
        IRepositoryNode cacheNode = nodeCache.getCache(tableContainer);
        if (cacheNode != null && cacheNode instanceof StableRepositoryNode) {
            tableContainer = (StableRepositoryNode) cacheNode;
            tableContainer.getChildren().clear();
        } else {
            nodeCache.addCache(tableContainer, true);
        }

        node.getChildren().add(tableContainer);
        List<MetadataTable> tablesWithOrders = ConnectionHelper.getTablesWithOrders(metadataConnection);
        EList tables = new BasicEList();
        for (MetadataTable tablesWithOrder : tablesWithOrders) {
            EMap<String, String> properties = tablesWithOrder.getAdditionalProperties();
            String partitionKey = properties.get(EProperties.CONTENT_TYPE.name());
            if (ERepositoryObjectType.METADATA_SAP_CONTENT_EXTRACTOR.name().equals(partitionKey)) {
                tables.add(tablesWithOrder);
            }
        }
        createTables(tableContainer, repObj, tables, ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
    }

    private void createSalesforceModuleNodes(IRepositoryViewObject rebObj, Connection metadataConnection,
            RepositoryNode connectionNode, List<IRepositoryViewObject> validationRules) {
        EList modules = ((SalesforceSchemaConnection) metadataConnection).getModules();
        for (int i = 0; i < modules.size(); i++) {
            SalesforceModuleUnit unit = (SalesforceModuleUnit) modules.get(i);
            RepositoryNode tableNode = createSalesforceNode(rebObj, connectionNode, unit);

            createTables(tableNode, rebObj, unit.getTables(), ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
            if (!SubItemHelper.isDeleted(unit)) {
                connectionNode.getChildren().add(tableNode);
            }

        }
    }

    /**
     * DOC YeXiaowei Comment method "createSAPFunctionNodes".
     * 
     * @param metadataConnection
     * @param functionNode
     */
    private void createSAPFunctionNodes(IRepositoryViewObject rebObj, Connection metadataConnection, RepositoryNode functionNode,
            List<IRepositoryViewObject> validationRules) {
        EList functions = ((SAPConnection) metadataConnection).getFuntions();
        if (functions == null || functions.isEmpty()) {
            return;
        }
        for (int i = 0; i < functions.size(); i++) {
            SAPFunctionUnit unit = (SAPFunctionUnit) functions.get(i);
            RepositoryNode tableNode = createSAPNode(rebObj, functionNode, unit);

            // create input and output container

            RepositoryNode inputNode = new StableRepositoryNode(tableNode,
                    Messages.getString("ProjectRepositoryNode.sapFunctions.inputSchema"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
            inputNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_FUNCTION);
            tableNode.getChildren().add(inputNode);

            createTables(inputNode, rebObj, unit.getInputTables(), ERepositoryObjectType.METADATA_CON_TABLE, validationRules);

            RepositoryNode outputNode = new StableRepositoryNode(tableNode,
                    Messages.getString("ProjectRepositoryNode.sapFunctions.outputSchema"), ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
            outputNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_FUNCTION);
            tableNode.getChildren().add(outputNode);

            createTables(outputNode, rebObj, unit.getTables(), ERepositoryObjectType.METADATA_CON_TABLE, validationRules);
            if (SubItemHelper.isDeleted(unit)) {
                // recBin.getChildren().add(tableNode);
            } else {
                functionNode.getChildren().add(tableNode);
            }

        }
    }

    /**
     * DOC zli Comment method "createSAPIDocNodes".
     * 
     * @param recBin
     * @param rebObj
     * @param metadataConnection
     * @param iDocNode
     */
    private void createSAPIDocNodes(IRepositoryViewObject rebObj, Connection metadataConnection, RepositoryNode iDocNode) {
        EList iDocs = ((SAPConnection) metadataConnection).getIDocs();
        if (iDocs == null || iDocs.isEmpty()) {
            return;
        }
        for (int i = 0; i < iDocs.size(); i++) {
            SAPIDocUnit unit = (SAPIDocUnit) iDocs.get(i);
            RepositoryNode tableNode = createSAPNode(rebObj, iDocNode, unit);
            if (SubItemHelper.isDeleted(unit)) {
                // recBin.getChildren().add(tableNode);
            } else {
                iDocNode.getChildren().add(tableNode);
            }

        }
    }

    private RepositoryNode createSalesforceNode(IRepositoryViewObject rebObj, RepositoryNode moduleNode,
            SalesforceModuleUnit unit) {
        SalesforceModuleRepositoryObject modelObj = new SalesforceModuleRepositoryObject(rebObj, moduleNode, unit);
        modelObj.setLabel(unit.getModuleName());
        RepositoryNode tableNode = new RepositoryNode(modelObj, moduleNode, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, modelObj.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SALESFORCE_MODULE);
        return tableNode;
    }

    /**
     * DOC YeXiaowei Comment method "createSAPNode".
     * 
     * @param rebObj
     * @param functionNode
     * @param unit
     * @return
     */
    private RepositoryNode createSAPNode(IRepositoryViewObject rebObj, RepositoryNode functionNode, SAPFunctionUnit unit) {
        SAPFunctionRepositoryObject modelObj = new SAPFunctionRepositoryObject(rebObj, functionNode, unit);
        modelObj.setLabel(unit.getName());
        RepositoryNode tableNode = new RepositoryNode(modelObj, functionNode, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, modelObj.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_FUNCTION);
        return tableNode;
    }

    private RepositoryNode createSAPNode(IRepositoryViewObject rebObj, RepositoryNode functionNode, SAPIDocUnit unit) {
        SAPIDocRepositoryObject modelObj = new SAPIDocRepositoryObject(rebObj, functionNode, unit);
        modelObj.setLabel(unit.getName());
        RepositoryNode tableNode = new RepositoryNode(modelObj, functionNode, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, modelObj.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_IDOC);
        return tableNode;
    }

    /**
     * DOC tguiu Comment method "createMetatable".
     * 
     * @param node
     * @param table
     * @param repositoryObjectType TODO
     * @param iMetadataFileDelimited
     * @return
     */
    private RepositoryNode createMetatableNode(RepositoryNode node, IRepositoryViewObject repObj,
            final org.talend.core.model.metadata.builder.connection.MetadataTable table,
            ERepositoryObjectType repositoryObjectType) {
        MetadataTableRepositoryObject modelObj = new MetadataTableRepositoryObject(repObj, table);
        RepositoryNode tableNode = new RepositoryNode(modelObj, node, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, modelObj.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, repositoryObjectType);
        return tableNode;

    }

    private RepositoryNode createMataColumnNode(RepositoryNode node, IRepositoryViewObject repObj, MetadataColumn column,
            ERepositoryObjectType repositoryObjectType) {
        MetadataColumnRepositoryObject columnObj = new MetadataColumnRepositoryObject(repObj, column);
        RepositoryNode columnNode = new RepositoryNode(columnObj, node, ENodeType.REPOSITORY_ELEMENT);
        columnNode.setProperties(EProperties.LABEL, columnObj.getLabel());
        columnNode.setProperties(EProperties.CONTENT_TYPE, repositoryObjectType);
        return columnNode;
    }

    /**
     * DOC cantoine Comment method "createQueryNode".
     * 
     * @param node
     * @param repObj
     * @param query
     * @return
     */
    private RepositoryNode createQueryNode(RepositoryNode node, IRepositoryViewObject repObj, Query query) {
        QueryRepositoryObject modelObj = new QueryRepositoryObject(repObj, query);
        RepositoryNode tableNode = new RepositoryNode(modelObj, node, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, query.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_QUERY);
        return tableNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProjectRepositoryNode#getProject()
     */
    @Override
    public org.talend.core.model.general.Project getProject() {
        return this.project;
    }

    @Override
    public RepositoryNode getRootRepositoryNode(ERepositoryObjectType type) {
        return getRootRepositoryNode(type, false);
    }

    @Override
    public RepositoryNode getRootRepositoryNode(ERepositoryObjectType type, boolean tryInit) {
        RepositoryNode rootTypeNode = null;
        if (type != null) {
            String typeName = type.name();
            if (repositoryNodeMap.containsKey(typeName)) {
                rootTypeNode = repositoryNodeMap.get(typeName);
                if (tryInit) {
                    initNode(rootTypeNode);
                }

            }
        }
        return rootTypeNode;
    }

    public RepositoryNode getRootRepositoryNode(IRepositoryNode repNode, boolean tryInit) {
        RepositoryNode rootRepoNode = null;
        ERepositoryObjectType repObjType = getRepositoryType(repNode);

        if (repObjType != null) {
            rootRepoNode = getRootRepositoryNode(repObjType, tryInit);
        }

        return rootRepoNode;
    }

    @Override
    public Map<String, RepositoryNode> getGenericTopNodesMap() {
        return genericNodesMap;
    }

    @Override
    public final void initNode(final IRepositoryNode rootTypeNode) {
        SafeRunner.run(new ISafeRunnable() {

            @Override
            public void handleException(Throwable exception) {
                ExceptionHandler.process(exception);
            }

            @Override
            public void run() throws Exception {
                if (rootTypeNode instanceof RepositoryNode && rootTypeNode.getParent() instanceof ProjectRepositoryNode
                        && !((RepositoryNode) rootTypeNode).isInitialized()) {
                    ((ProjectRepositoryNode) rootTypeNode.getParent()).initializeChildren(rootTypeNode);
                    ((RepositoryNode) rootTypeNode).setInitialized(true);
                }
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.nodes.IProjectRepositoryNode#getRecBinNode()
     */
    @Override
    public RepositoryNode getRecBinNode() {
        return this.recBinNode;
    }

    public boolean getMergeRefProject() {
        IPreferenceStore preferenceStore = RepositoryManager.getRepositoryPreferenceStore();
        if (!preferenceStore.contains(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT)) {
            return false;
        }
        return preferenceStore.getBoolean(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT);
    }

    @Override
    public String getLabel() {
        if (getProject() != null) {// compute branch url to add to the project label.
            return getProject().getLabel();
        }
        return super.getLabel();
    }

    @Override
    public void dispose() {
        boolean doDispose = isEnableDisposed();

        if (doDispose) {
            this.project = null;
            this.recBinNode = null;
        }
        // Ref
        if (refProject != null) {
            for (IRepositoryNode refP : refProject.getChildren()) {
                if (refP instanceof IProjectRepositoryNode) {
                    // dispose the ref-projects also.
                    if (refP instanceof RepositoryNode) {
                        ((RepositoryNode) refP).setEnableDisposed(doDispose);
                    }
                    refP.dispose();
                }
            }
        }
        // for all
        for (DynaEnum<? extends DynaEnum<?>> de : ERepositoryObjectType.values()) {
            if (de instanceof ERepositoryObjectType) {
                RepositoryNode rootRepositoryNode = getRootRepositoryNode((ERepositoryObjectType) de);
                if (rootRepositoryNode != null && !rootRepositoryNode.isDisposed()) {
                    // dispose the root node for type.
                    rootRepositoryNode.setEnableDisposed(doDispose);
                    rootRepositoryNode.dispose();
                }
            }
        }

        //
        if (this.repositoryNodeMap != null) {
            for (String type : this.repositoryNodeMap.keySet()) {
                RepositoryNode repositoryNode = this.repositoryNodeMap.get(type);
                if (repositoryNode != null && !repositoryNode.isDisposed()) {
                    // dispose the root node for type.
                    repositoryNode.setEnableDisposed(doDispose);
                    repositoryNode.dispose();
                }
            }
            if (doDispose) {
                this.repositoryNodeMap.clear();
            }
        }

        if (doDispose && this.nodeAndProject != null) {
            nodeAndProject.clear();
        }

        if (doDispose) {
            // use reflect to set the object to null.
            final Field[] declaredFields = this.getClass().getDeclaredFields();
            for (Field f : declaredFields) {
                f.setAccessible(true);
                try {
                    final Object object = f.get(this);
                    if (object == defaultProjRepoNode) {
                        continue;
                    }
                    if (object == dummyProjRepoNode) {
                        continue;
                    }
                    if (object instanceof RepositoryNode) {
                        f.set(this, null);
                    }

                } catch (IllegalArgumentException e) {
                    //
                } catch (IllegalAccessException e) {
                    //
                }
            }
        }

        nodeCache.clearCache();
        super.dispose();
    }

    public void cleanup() {
        if (defaultProjRepoNode == null) {
            return;
        }

        /**
         * to make sure defaultProjRepoNode is initialized
         */
        getInstance();

        // reinitialize every children
        for (IRepositoryNode childNode : defaultProjRepoNode.getChildren()) {
            if (childNode instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) childNode;
                node.setInitialized(false);
                node.getChildren().clear();
            }
        }

    }

    public IRepositoryNode findNodeFromModel(IRepositoryNode oldNode) {
        IRepositoryNode topNode = getRootRepositoryNode(getRepositoryType(oldNode));
        return findNode(topNode, oldNode);
    }

    public ERepositoryObjectType getRepositoryType(IRepositoryNode node) {
        IRepositoryNode testNode = node;
        while (testNode != null) {
            ERepositoryObjectType repObjType = testNode.getContentType();
            if (repObjType != null) {
                return repObjType;
            }
            testNode = testNode.getParent();
            if (testNode instanceof ProjectRepositoryNode) {
                break;
            }
        }
        return null;
    }

    private IRepositoryNode findNode(IRepositoryNode modelNode, IRepositoryNode oldNode) {
        if (modelNode == null || oldNode == null) {
            return null;
        }
        if (isSameNode(modelNode, oldNode)) {
            return modelNode;
        }
        IRepositoryNode latestNode = null;
        List<IRepositoryNode> children = modelNode.getChildren();
        if (children != null && !children.isEmpty()) {
            for (IRepositoryNode child : children) {
                IRepositoryNode node = findNode(child, oldNode);
                if (node != null) {
                    return node;
                }
            }
        }
        return latestNode;
    }

    private boolean isSameNode(IRepositoryNode left, IRepositoryNode right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.equals(right)) {
            return true;
        }
        return false;
    }

    /**
     * The key is the relative path, for exmaple: Local_Project/metadata/dbConnection/mysql_0.1.properties
     */
    public IRepositoryNode removeCache(String key) {
        return nodeCache.removeCache(key);
    }
    
    public void setOptions(int options) {
        this.options = options;
    }
    
}
