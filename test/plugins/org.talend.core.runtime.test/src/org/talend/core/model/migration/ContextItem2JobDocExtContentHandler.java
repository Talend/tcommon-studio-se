package org.talend.core.model.migration;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Status;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryTypeProcessor;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * used to return the JOB_DOC_EXT RepoObjectType when an item of type ContextItem is used
 * */
public class ContextItem2JobDocExtContentHandler implements IRepositoryContentHandler {

    public ContextItem2JobDocExtContentHandler() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Resource create(IProject project, Item item, int classifierID, IPath path) throws PersistenceException {
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
    public Resource save(Item item) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IImage getIcon(ERepositoryObjectType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item createNewItem(ERepositoryObjectType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isProcess(Item item) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRepObjType(ERepositoryObjectType type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ERepositoryObjectType getProcessType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ERepositoryObjectType getCodeType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType(Item item) {
        // FIXME tmp to disable this test. because if add this test, will bring some problems for CNF and migration
        // task.
        // return item instanceof ContextItem ? ERepositoryObjectType.getType("JOB_DOC_EXT") : null;
        return null;
    }

    @Override
    public void addNode(ERepositoryObjectType type, RepositoryNode recBinNode, IRepositoryViewObject repositoryObject,
            RepositoryNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addContents(Collection<EObject> collection, Resource resource) {
        // TODO Auto-generated method stub

    }

    @Override
    public IImage getIcon(Item item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ERepositoryObjectType getHandleType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasSchemas() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Status> getPropertyStatus(Item item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hideAction(IRepositoryNode node, Class actionType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isOwnTable(IRepositoryNode node, Class type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IWizard newWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
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

    /* (non-Javadoc)
     * @see org.talend.core.model.repository.IRepositoryContentHandler#hasDynamicIcon()
     */
    @Override
    public boolean hasDynamicIcon() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void copyIcon(Item originalItem, Item newItem) {
        
    }

    @Override
    public int openWizardDialog(IRepositoryNode repoNode, IWizard wizard) {
        WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        wizardDialog.setPageSize(780, 540);
        wizardDialog.create();
        return wizardDialog.open();
    }
}
