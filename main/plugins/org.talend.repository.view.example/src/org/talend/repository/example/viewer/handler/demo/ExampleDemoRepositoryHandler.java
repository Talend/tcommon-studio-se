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
package org.talend.repository.example.viewer.handler.demo;

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
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Status;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryTypeProcessor;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.example.image.EExampleDemoImage;
import org.talend.repository.example.model.demo.DemoFactory;
import org.talend.repository.example.model.demo.DemoPackage;
import org.talend.repository.example.model.demo.ExampleDemoConnectionItem;
import org.talend.repository.example.viewer.node.ExampleDemoRepositoryNodeType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class ExampleDemoRepositoryHandler implements IRepositoryContentHandler {

    protected XmiResourceManager xmiResourceManager = new XmiResourceManager();

    /**
     * DOC ggu ExampleDemoRepositoryHandler constructor comment.
     */
    public ExampleDemoRepositoryHandler() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#create(org.eclipse.core.resources.IProject,
     * org.talend.core.model.properties.Item, int, org.eclipse.core.runtime.IPath)
     */
    @Override
    public Resource create(IProject project, Item item, int classifierID, IPath path) throws PersistenceException {
        ERepositoryObjectType repositoryObjectType = getRepositoryObjectType(item);
        if (isRepObjType(repositoryObjectType)) {
            ExampleDemoConnectionItem demoItem = (ExampleDemoConnectionItem) item;
            Resource itemResource = xmiResourceManager.createItemResource(project, item, path, repositoryObjectType, false);
            itemResource.getContents().add(demoItem.getConnection());
            return itemResource;
        }
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#save(org.talend.core.model.properties.Item)
     */
    @Override
    public Resource save(Item item) throws PersistenceException {
        ERepositoryObjectType repositoryObjectType = getRepositoryObjectType(item);
        if (isRepObjType(repositoryObjectType)) {
            ExampleDemoConnectionItem demoItem = (ExampleDemoConnectionItem) item;
            Resource itemResource = xmiResourceManager.getItemResource(item);
            itemResource.getContents().clear();
            itemResource.getContents().add(demoItem.getConnection());
            return itemResource;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#createNewItem(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    public Item createNewItem(ERepositoryObjectType type) {
        if (isRepObjType(type)) {
            return DemoFactory.eINSTANCE.createExampleDemoConnectionItem();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#getRepositoryObjectType(org.talend.core.model.properties
     * .Item)
     */
    @Override
    public ERepositoryObjectType getRepositoryObjectType(Item item) {
        if (item.eClass() == DemoPackage.Literals.EXAMPLE_DEMO_CONNECTION_ITEM) {
            return getHandleType();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#isRepObjType(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    public boolean isRepObjType(ERepositoryObjectType type) {
        return getHandleType().equals(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getIcon(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    public IImage getIcon(ERepositoryObjectType type) {
        if (isRepObjType(type)) {
            return EExampleDemoImage.DEMO_ICON;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getIcon(org.talend.core.model.properties.Item)
     */
    @Override
    public IImage getIcon(Item item) {
        return getIcon(getRepositoryObjectType(item));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#isProcess(org.talend.core.model.properties.Item)
     */
    @Override
    public boolean isProcess(Item item) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getProcessType()
     */
    @Override
    public ERepositoryObjectType getProcessType() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getCodeType()
     */
    @Override
    public ERepositoryObjectType getCodeType() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#addNode(org.talend.core.model.repository.
     * ERepositoryObjectType, org.talend.repository.model.RepositoryNode,
     * org.talend.core.model.repository.IRepositoryViewObject, org.talend.repository.model.RepositoryNode)
     */
    @Override
    public void addNode(ERepositoryObjectType type, RepositoryNode recBinNode, IRepositoryViewObject repositoryObject,
            RepositoryNode node) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#addContents(java.util.Collection,
     * org.eclipse.emf.ecore.resource.Resource)
     */
    @Override
    public void addContents(Collection<EObject> collection, Resource resource) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getHandleType()
     */
    @Override
    public ERepositoryObjectType getHandleType() {
        return ExampleDemoRepositoryNodeType.repositoryExampleDemoType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#hasSchemas()
     */
    @Override
    public boolean hasSchemas() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#getPropertyStatus(org.talend.core.model.properties
     * .Item)
     */
    @Override
    public List<Status> getPropertyStatus(Item item) {
        try {
            return CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getTechnicalStatus();
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#hideAction(org.talend.repository.model.IRepositoryNode
     * , java.lang.Class)
     */
    @Override
    public boolean hideAction(IRepositoryNode node, Class actionType) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#isOwnTable(org.talend.repository.model.IRepositoryNode
     * , java.lang.Class)
     */
    @Override
    public boolean isOwnTable(IRepositoryNode node, Class type) {
        return node != null ? isRepObjType(node.getObjectType()) : false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#newWizard(org.eclipse.ui.IWorkbench, boolean,
     * org.talend.repository.model.RepositoryNode, java.lang.String[])
     */
    @Override
    public IWizard newWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#createScreenShotResource(org.eclipse.core.resources
     * .IProject, org.talend.core.model.properties.Item, int, org.eclipse.core.runtime.IPath)
     */
    @Override
    public Resource createScreenShotResource(IProject project, Item item, int classifierID, IPath path)
            throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#saveScreenShots(org.talend.core.model.properties.Item)
     */
    @Override
    public Resource saveScreenShots(Item item) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#copyScreenShotFile(org.talend.core.model.properties
     * .Item, org.talend.core.model.properties.Item)
     */
    @Override
    public void copyScreenShotFile(Item originalItem, Item newItem) throws IOException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#deleteNode(org.talend.core.model.repository.
     * IRepositoryViewObject)
     */
    @Override
    public void deleteNode(IRepositoryViewObject repViewObject) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getRepositoryTypeProcessor(java.lang.String)
     */
    @Override
    public IRepositoryTypeProcessor getRepositoryTypeProcessor(String repositoryType) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#getReferenceFileURI(org.talend.core.model.properties
     * .Item, java.lang.String)
     */
    @Override
    public URI getReferenceFileURI(Item item, String extension) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#newSchemaWizard(org.eclipse.ui.IWorkbench,
     * boolean, org.talend.core.model.repository.IRepositoryViewObject,
     * org.talend.core.model.metadata.builder.connection.MetadataTable, java.lang.String[], boolean)
     */
    @Override
    public IWizard newSchemaWizard(IWorkbench workbench, boolean creation, IRepositoryViewObject object,
            MetadataTable metadataTable, String[] existingNames, boolean forceReadOnly) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.talend.core.model.repository.IRepositoryContentHandler#hasDynamicIcon()
     */
    @Override
    public boolean hasDynamicIcon() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void copyIcon(Item originalItem, Item newItem){
        
    }

}
