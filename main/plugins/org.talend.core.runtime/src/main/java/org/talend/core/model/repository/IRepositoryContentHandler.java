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
package org.talend.core.model.repository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Status;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public interface IRepositoryContentHandler {

    public Resource create(IProject project, Item item, int classifierID, IPath path) throws PersistenceException;

    /**
     * Sub-classes override this method to create a screen shot resource, if there is no need to create a screen shot
     * resource, then return <code>null</code>. Added by Marvin Wang on Mar 8, 2013.
     * 
     * @param project
     * @param item
     * @param classifierID
     * @param path
     * @return
     */
    Resource createScreenShotResource(IProject project, Item item, int classifierID, IPath path) throws PersistenceException;

    /**
     * This method is used to load screen shot resources to return by the given item. Sub-classes could override this
     * method to return a special resource if required. Added by Marvin Wang on Apr 1, 2013.
     * 
     * @param item
     * @return
     * @throws PersistenceException
     */
    Resource saveScreenShots(Item item) throws PersistenceException;

    /**
     * Client can invoke this method to copy the screen shot resource of original item to new item. Added by Marvin Wang
     * on Apr 15, 2013.
     * 
     * @param originalItem
     * @param newItem
     * @throws IOException
     */
    void copyScreenShotFile(Item originalItem, Item newItem) throws IOException;
    
    void copyIcon(Item originalItem, Item newItem);

    public Resource save(Item item) throws PersistenceException;

    public IImage getIcon(ERepositoryObjectType type);

    public Item createNewItem(ERepositoryObjectType type);

    public boolean isProcess(Item item);

    public boolean isRepObjType(ERepositoryObjectType type);

    public ERepositoryObjectType getProcessType();

    public ERepositoryObjectType getCodeType();

    public ERepositoryObjectType getRepositoryObjectType(Item item);

    public void addNode(ERepositoryObjectType type, RepositoryNode recBinNode, IRepositoryViewObject repositoryObject,
            RepositoryNode node);

    public void addContents(Collection<EObject> collection, Resource resource);

    
    /**
     * Returns true if the function getIcon(Item) is implemented.
     * This will avoid to load property for nothing on the repository (slow)
     * @return
     */
    public boolean hasDynamicIcon();
    
    public IImage getIcon(Item item);

    /**
     * DOC ycbai Comment method "getHandleType".
     * 
     * @return the ERepositoryObjectType which the handler process.
     */
    public ERepositoryObjectType getHandleType();

    /**
     * DOC ycbai Comment method "hasSchemas".
     * 
     * @return whether has schema node.
     */
    public boolean hasSchemas();

    /**
     * DOC ycbai Comment method "getPropertyStatus".
     * 
     * @param item
     * @return the property status list.
     */
    public List<Status> getPropertyStatus(Item item);

    /**
     * DOC ycbai Comment method "hideAction".
     * 
     * @param node
     * @param actionType actionType the type of action which you want to hide for this node.
     * @return true if you want to hide the action.
     */
    public boolean hideAction(IRepositoryNode node, Class actionType);

    /**
     * DOC ycbai Comment method "isOwnTable".
     * 
     * @param node
     * @return true if the table or column node belong to the item.
     */
    public boolean isOwnTable(IRepositoryNode node, Class type);

    /**
     * DOC ycbai Comment method "newWizard".
     * 
     * @param workbench
     * @param creation
     * @param node
     * @param existingNames
     * @return a new connection wizard associated with the node.
     */
    public IWizard newWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames);

    /**
     * 
     * DOC ycbai Comment method "newSchemaWizard".
     * 
     * @param workbench
     * @param creation
     * @param object
     * @param metadataTable
     * @param existingNames
     * @param forceReadOnly
     * @return a new schema wizard associated with the object.
     */
    public IWizard newSchemaWizard(IWorkbench workbench, boolean creation, IRepositoryViewObject object,
            MetadataTable metadataTable, String[] existingNames, boolean forceReadOnly);

    public int openWizardDialog(IRepositoryNode repoNode, IWizard wizard);

    /**
     * 
     * DOC ycbai Comment method "deleteNode".
     * 
     * Delete the relevances of this node.
     * 
     * @param repViewObject
     */
    public void deleteNode(IRepositoryViewObject repViewObject);

    /**
     * DOC ycbai Comment method "getRepositoryTypeProcessor".
     * 
     * @param repositoryType
     * @return the type processor which handles the appearance of node in RepositoryReviewDialog.
     */
    public IRepositoryTypeProcessor getRepositoryTypeProcessor(String repositoryType);

    public URI getReferenceFileURI(Item item, String extension);

}
