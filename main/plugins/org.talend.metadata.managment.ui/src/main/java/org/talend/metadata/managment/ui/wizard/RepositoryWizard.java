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
package org.talend.metadata.managment.ui.wizard;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.metadata.managment.ui.MetadataManagmentUiPlugin;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: RepositoryWizard.java 51271 2010-11-15 08:40:42Z nrousseau $
 * 
 */
public abstract class RepositoryWizard extends Wizard {

    protected static Logger log = Logger.getLogger(RepositoryWizard.class);

    protected static final String PID = MetadataManagmentUiPlugin.PLUGIN_ID;

    private IWorkbench workbench;

    protected ISelection selection;

    protected IRepositoryViewObject repositoryObject = null;

    protected String[] existingNames;

    protected IPath pathToSave = null;

    protected boolean creation;

    protected INode node;

    private boolean repositoryObjectEditable = true;

    private boolean forceReadOnly;

    public RepositoryWizard(IWorkbench workbench, boolean creation) {
        this(workbench, creation, false);
    }

    public RepositoryWizard(IWorkbench workbench, boolean creation, boolean forceReadOnly) {
        super();
        this.workbench = workbench;
        this.creation = creation;
        this.forceReadOnly = forceReadOnly;
    }

    /**
     * Getter for workbench.
     * 
     * @return the workbench
     */
    public IWorkbench getWorkbench() {
        return this.workbench;
    }

    /**
     * Sets the workbench.
     * 
     * @param workbench the workbench to set
     */
    public void setWorkbench(IWorkbench workbench) {
        this.workbench = workbench;
    }

    /**
     * DOC ocarbone Comment method "performCancel". Unlock the IRepositoryObject before the close of the wizard.
     * 
     * @param IRepositoryObject
     */
    @Override
    public boolean performCancel() {
        if (repositoryObject != null && repositoryObject.getProperty().eResource() != null) {
            repositoryObject.getProperty().eResource().unload();
        }
        closeLockStrategy();
        return true;
    }

    /**
     * DOC mhelleboid Comment method "reload".
     * 
     * @throws PersistenceException
     */
    private void reload() throws PersistenceException {
        if (repositoryObject != null) {
            ItemState state = repositoryObject.getProperty().getItem().getState();
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
                IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IProxyRepositoryService.class);
                IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
                if (state != null && state.getPath() != null) {
                    IRepositoryViewObject lastVersion = repositoryFactory.getLastVersion(ProjectManager.getInstance()
                            .getCurrentProject(), repositoryObject.getProperty().getId(), state.getPath(), repositoryObject
                            .getRepositoryObjectType());
                    lastVersion.setRepositoryNode(repositoryObject.getRepositoryNode());
                    repositoryObject = lastVersion;
                }
            }
        }
    }

    /**
     * Sets the repositoryObject.
     * 
     * @param repositoryObject the repositoryObject to set
     */
    public void setRepositoryObject(IRepositoryViewObject repositoryViewObject) {
        // RepositoryViewObject is dynamic, here we prefer have a RepositoryObject with a fixed property.
        this.repositoryObject = new RepositoryObject(repositoryViewObject.getProperty());
        if (!PluginChecker.isOnlyTopLoaded()) {
            calculateRepositoryObjectEditable(repositoryViewObject);
        }
    }

    /**
     * DOC ocarbone Comment method "isRepositoryObjectEditable". Check the RepositoryObject (is locked or on recycle
     * bin) and return a boolean represent if the RepositoryObject is editable (true) or readOnly (false).
     * 
     * @return boolean
     */
    public boolean isRepositoryObjectEditable() {
        return repositoryObjectEditable && !forceReadOnly;
    }

    /**
     * DOC matthieu Comment method "calculateObjectEditable".
     * 
     * @return
     */
    private void calculateRepositoryObjectEditable(IRepositoryViewObject repositoryViewObject) {
        if (repositoryObject != null && !forceReadOnly) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            repositoryObjectEditable = factory.isEditableAndLockIfPossible(repositoryViewObject);

            if (!CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected()) {
                final IRepositoryNode repositoryNode = repositoryViewObject.getRepositoryNode();
                IRepositoryView repositoryView = RepositoryManagerHelper.findRepositoryView();
                if (repositoryView != null) {
                    repositoryView.expand(repositoryNode);
                    repositoryView.getViewer().refresh(repositoryObject.getRepositoryObjectType());
                }
            }
        }
    }

    /**
     * DOC ocarbone Comment method "initLockStrategy". If needed, lock the repositoryObject.
     * 
     */
    public void initLockStrategy() {
        if (isRepositoryObjectEditable()) {
            // The lock strategy is useless when the repositoryObject isn't yet created
            if (repositoryObject != null) {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                try {
                    factory.lock(repositoryObject);
                } catch (PersistenceException e1) {
                    String detailError = e1.toString();
                    new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                            detailError);
                    log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                } catch (BusinessException e) {
                    // Nothing to do
                }
            }
        }
    }

    /**
     * DOC ocarbone Comment method "performCancel". Unlock the IRepositoryObject before the close of the wizard.
     * 
     * @param IRepositoryObject
     */
    public void closeLockStrategy() {
        // The lock strategy is useless when the repositoryObject isn't created
        if (repositoryObject != null) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.unlock(repositoryObject);
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (LoginException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    /**
     * 
     * DOC wzhang Comment method "getConnectionItem".
     * 
     * @return
     */
    public ConnectionItem getConnectionItem() {
        return null;
    }

    public boolean isCreation() {
        return this.creation;
    }

    public INode getNode() {
        return this.node;
    }

    public void setNode(INode node) {
        this.node = node;
    }

}
