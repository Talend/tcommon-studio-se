// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class ExampleDemoRepositoryHandler implements IRepositoryContentHandler {

    protected XmiResourceManager xmiResourceManager = new XmiResourceManager();

    public ExampleDemoRepositoryHandler() {
    }

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

    @Override
    public Item createNewItem(ERepositoryObjectType type) {
        if (isRepObjType(type)) {
            return DemoFactory.eINSTANCE.createExampleDemoConnectionItem();
        }
        return null;
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType(Item item) {
        if (item.eClass() == DemoPackage.Literals.EXAMPLE_DEMO_CONNECTION_ITEM) {
            return getHandleType();
        }
        return null;
    }

    @Override
    public boolean isRepObjType(ERepositoryObjectType type) {
        return getHandleType().equals(type);
    }

    @Override
    public IImage getIcon(ERepositoryObjectType type) {
        if (isRepObjType(type)) {
            return EExampleDemoImage.DEMO_ICON;
        }
        return null;
    }

    @Override
    public IImage getIcon(Item item) {
        return getIcon(getRepositoryObjectType(item));
    }

    @Override
    public boolean isProcess(Item item) {
        return false;
    }

    @Override
    public ERepositoryObjectType getProcessType() {
        return null;
    }

    @Override
    public ERepositoryObjectType getCodeType() {
        return null;
    }

    @Override
    public void addNode(ERepositoryObjectType type, RepositoryNode recBinNode, IRepositoryViewObject repositoryObject,
            RepositoryNode node) {
    }

    @Override
    public void addContents(Collection<EObject> collection, Resource resource) {

    }

    @Override
    public ERepositoryObjectType getHandleType() {
        return ExampleDemoRepositoryNodeType.repositoryExampleDemoType;
    }

    @Override
    public boolean hasSchemas() {
        return true;
    }

    @Override
    public List<Status> getPropertyStatus(Item item) {
        try {
            return CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getTechnicalStatus();
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    @Override
    public boolean hideAction(IRepositoryNode node, Class actionType) {
        return false;
    }

    @Override
    public boolean isOwnTable(IRepositoryNode node, Class type) {
        return node != null ? isRepObjType(node.getObjectType()) : false;
    }

    @Override
    public IWizard newWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Resource createScreenShotResource(IProject project, Item item, int classifierID, IPath path)
            throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Resource saveScreenShots(Item item) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void copyScreenShotFile(Item originalItem, Item newItem) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteNode(IRepositoryViewObject repViewObject) {
        // TODO Auto-generated method stub

    }

    @Override
    public IRepositoryTypeProcessor getRepositoryTypeProcessor(String repositoryType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getReferenceFileURI(Item item, String extension) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IWizard newSchemaWizard(IWorkbench workbench, boolean creation, IRepositoryViewObject object,
            MetadataTable metadataTable, String[] existingNames, boolean forceReadOnly) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasDynamicIcon() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void copyIcon(Item originalItem, Item newItem){
    }

}
