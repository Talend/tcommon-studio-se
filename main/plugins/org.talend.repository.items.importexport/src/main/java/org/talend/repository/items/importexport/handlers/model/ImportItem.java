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
package org.talend.repository.items.importexport.handlers.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler;

/**
 */
public class ImportItem {

    private String itemName;

    private ResourceSet resourceSet = new ResourceSetImpl();

    private Property property;

    protected IPath path;

    private List<String> errors = new ArrayList<String>();

    private List<MigrationTask> migrationTasksToApply = new ArrayList<MigrationTask>();

    private boolean resolved = false;

    private State state = State.NON_EXISTED;

    private boolean locked;

    private boolean imported = false;

    protected String label;

    private IRepositoryViewObject existingItemWithSameId;

    private IRepositoryViewObject existingItemWithSameName;

    protected ERepositoryObjectType repositoryType;

    private String itemId;

    private String importPath;

    private String itemVersion;

    private Project itemProject;

    private String itemProjectVersion;

    private boolean removeProjectStatslog;

    /**
     * add for tdm
     */
    private String itemPath;

    /**
     * Work for some items which are denpended by others, but won't display in the import dialog, will be imported
     * implictly.
     */
    private boolean visible = true;

    private IImportItemsHandler importHandler;

    private boolean isSystemItem = false;

    public ImportItem(IPath path) {
        this.path = path;
    }

    public Item getItem() {
        if (property != null) {
            return property.getItem();
        }
        return null;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }

    public String getItemName() {
        if (itemName == null) {
            IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                    IBrandingService.class);
            boolean allowVerchange = brandingService.getBrandingConfiguration().isAllowChengeVersion();
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(property.getItem());

            StringBuffer sb = new StringBuffer();
            if (itemType != null) {
                sb.append(itemType.toString());
                sb.append(' ');
            }
            sb.append(property.getLabel());

            if (allowVerchange) {
                sb.append(' ');
                sb.append(property.getVersion());
            }
            itemName = sb.toString();
        }
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public IPath getPath() {
        return path;
    }

    public void addError(String message) {
        errors.add(message);
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean isValid() {
        // TODO mhelleboid split validation in two steps to be able to filter a valid item and avoid adding an error
        return errors.isEmpty();
    }

    /**
     * Getter for migrationTasksToApply.
     * 
     * @return the migrationTasksToApply
     */
    public List<MigrationTask> getMigrationTasksToApply() {
        return this.migrationTasksToApply;
    }

    /**
     * Sets the migrationTasksToApply.
     * 
     * @param migrationTasksToApply the migrationTasksToApply to set
     */
    public void setMigrationTasksToApply(List<MigrationTask> migrationTasksToApply) {
        this.migrationTasksToApply = migrationTasksToApply;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public ERepositoryObjectType getType() {
        Item item = getItem();
        if (item != null) {
            return ERepositoryObjectType.getItemType(item);
        }
        return null;
    }

    /**
     * 
     * DOC hcw ImportItem class global comment. Detailled comment
     */
    public enum State {
                       /**
                        * only name existed
                        */
                       NAME_EXISTED,

                       /**
                        * only id existed
                        */
                       ID_EXISTED,

                       NON_EXISTED,

                       /**
                        * only one existed item with same name and same id
                        */
                       NAME_AND_ID_EXISTED,

                       /**
                        * one existed item with same name, while another existed item with same id
                        */
                       NAME_AND_ID_EXISTED_BOTH
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public boolean isImported() {
        return imported;
    }

    public String getLabel() {
        return getLabel(true);
    }

    public String getLabel(boolean checkVersion) {
        if (label == null && property != null) {
            if (checkVersion) {
                IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                        IBrandingService.class);
                boolean allowVerchange = brandingService.getBrandingConfiguration().isAllowChengeVersion();
                if (allowVerchange && property.getItem().isNeedVersion()) {
                    label = property.getLabel() + " " + property.getVersion(); //$NON-NLS-1$
                    return label;
                }
            }
            label = property.getLabel();

        }
        return label;
    }

    public ResourceSet getResourceSet() {
        return resourceSet;
    }

    public void setResourceSet(ResourceSet resourceSet) {
        this.resourceSet = resourceSet;
    }

    public void clear() {
        for (Resource resource : resourceSet.getResources()) {
            resource.unload();
        }
        resourceSet.getResources().clear();
        property = null;
        resourceSet = null;
    }

    public IRepositoryViewObject getExistingItemWithSameId() {
        return existingItemWithSameId;
    }

    public void setExistingItemWithSameId(IRepositoryViewObject existingItemWithSameId) {
        this.existingItemWithSameId = existingItemWithSameId;
    }

    public IRepositoryViewObject getExistingItemWithSameName() {
        return this.existingItemWithSameName;
    }

    public void setExistingItemWithSameName(IRepositoryViewObject existingItemWithSameName) {
        this.existingItemWithSameName = existingItemWithSameName;
    }

    public ERepositoryObjectType getRepositoryType() {
        if (repositoryType == null) {
            repositoryType = ERepositoryObjectType.getItemType(property.getItem());
        }
        return repositoryType;
    }

    public void setRepositoryType(ERepositoryObjectType repositoryType) {
        this.repositoryType = repositoryType;
    }

    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(String itemVersion) {
        this.itemVersion = itemVersion;
    }

    public String getItemProjectVersion() {
        return this.itemProjectVersion;
    }

    public void setItemProjectVersion(String itemProjectVersion) {
        this.itemProjectVersion = itemProjectVersion;
    }

    public void setItemProject(Project itemProject) {
        this.itemProject = itemProject;
    }

    public Project getItemProject() {
        return itemProject;
    }

    public boolean isRemoveProjectStatslog() {
        return this.removeProjectStatslog;
    }

    public void setRemoveProjectStatslog(boolean removeProjectStatslog) {
        this.removeProjectStatslog = removeProjectStatslog;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public IImportItemsHandler getImportHandler() {
        return this.importHandler;
    }

    public void setImportHandler(IImportItemsHandler handler) {
        this.importHandler = handler;
    }

    public void setItemPath(String itemPath) {
        this.itemPath = itemPath;
    }

    public String getItemPath() {
        return this.itemPath;
    }

    public boolean isSystemItem() {
        return this.isSystemItem;
    }

    public void setSystemItem(boolean isSystemItem) {
        this.isSystemItem = isSystemItem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ImportItem)) {
            return false;
        }
        ImportItem other = (ImportItem) obj;
        if (this.path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!this.path.equals(other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ImportItem [path=" + this.path + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
