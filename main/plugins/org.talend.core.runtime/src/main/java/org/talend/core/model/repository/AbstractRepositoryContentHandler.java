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
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Status;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public abstract class AbstractRepositoryContentHandler implements IRepositoryContentHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getIcon(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    public IImage getIcon(ERepositoryObjectType type) {
        return null;
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
        // Do nothing by default.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#addContents(java.util.Collection,
     * org.eclipse.emf.ecore.resource.Resource)
     */
    @Override
    public void addContents(Collection<EObject> collection, Resource resource) {
        // Do nothing by default.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getIcon(org.talend.core.model.properties.Item)
     */
    @Override
    public IImage getIcon(Item item) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getHandleType()
     */
    @Override
    public ERepositoryObjectType getHandleType() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#hasSchemas()
     */
    @Override
    public boolean hasSchemas() {
        return false;
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
        if (isProcess(item)) {
            try {
                return CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getTechnicalStatus();
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
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
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#newWizard(org.eclipse.ui.IWorkbench, boolean,
     * org.talend.repository.model.RepositoryNode, java.lang.String[])
     */
    @Override
    public IWizard newWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
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
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#deleteNode(org.talend.core.model.repository.
     * IRepositoryViewObject)
     */
    @Override
    public void deleteNode(final IRepositoryViewObject repViewObject) {
        if (repViewObject != null && repViewObject.getProperty() != null) {
            final Item item = repViewObject.getProperty().getItem();
            if (item != null && isProcess(item)) {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceRunnable operation = new IWorkspaceRunnable() {

                    @Override
                    public void run(IProgressMonitor monitor) throws CoreException {
                        try {
                            deleteNode(item);
                        } catch (Exception e) {
                            throw new CoreException(
                                    new org.eclipse.core.runtime.Status(
                                            IStatus.ERROR,
                                            CoreRuntimePlugin.PLUGIN_ID,
                                            Messages.getString(
                                                    "AbstractRepositoryContentHandler.deleteNode.exception", repViewObject.getLabel()), e)); //$NON-NLS-1$
                        }
                    }
                };

                try {
                    ISchedulingRule schedulingRule = workspace.getRoot();
                    // the update the project files need to be done in the workspace runnable to avoid all
                    // notification of changes before the end of the modifications.
                    workspace.run(operation, schedulingRule, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    protected void deleteNode(Item item) throws Exception {
        // Do nothing by default.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getRepositoryTypeProcessor()
     */
    @Override
    public IRepositoryTypeProcessor getRepositoryTypeProcessor(String repositoryType) {
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
        // Does nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#getReferenceFileURI(org.eclipse.emf.common.util.URI,
     * java.lang.String)
     */
    @Override
    public URI getReferenceFileURI(Item item, String extension) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.talend.core.model.repository.IRepositoryContentHandler#hasDynamicIcon()
     */
    @Override
    public boolean hasDynamicIcon() {
        return false;
    }

    @Override
    public void copyIcon(Item originalItem, Item newItem){
        
    }

    @Override
    public int openWizardDialog(IRepositoryNode repoNode, IWizard wizard) {
        WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        wizardDialog.setPageSize(780, 540);
        wizardDialog.create();
        return wizardDialog.open();
    }
}
