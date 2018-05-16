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
package org.talend.metadata.managment.ui.wizard;

import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.ILastVersionChecker;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.metadata.managment.ui.wizard.context.ContextWizard;
import org.talend.metadata.managment.ui.wizard.documentation.DocumentationCreateWizard;
import org.talend.metadata.managment.ui.wizard.documentation.DocumentationUpdateWizard;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC hywang class global comment. Detailled comment
 */
public abstract class CheckLastVersionRepositoryWizard extends RepositoryWizard implements ILastVersionChecker {

    protected ConnectionItem connectionItem;

    protected MetadataTable metadataTable;

    // protected MetadataTable metadataTableCopy;
    //
    // protected Connection connectionCopy;

    public CheckLastVersionRepositoryWizard(IWorkbench workbench, boolean creation) {
        super(workbench, creation, false);
    }

    public CheckLastVersionRepositoryWizard(IWorkbench workbench, boolean creation, boolean forceReadOnly) {
        super(workbench, creation, forceReadOnly);
    }

    @Override
    public boolean performFinish() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Initializes the copies including connection and metadata table. The input parameter is not <code>null</code>.
     */
    // protected void initConnectionCopy(Connection connection) {
    // this.connectionCopy = cloneConnectionCopy(connection);
    // if (connectionCopy != null)
    // this.metadataTableCopy = ConnectionHelper.getTableById(connectionCopy, metadataTable.getId());
    // }

    /**
     * Clones a copy of connection.
     * 
     * @param connection
     */
    // protected Connection cloneConnectionCopy(Connection connection) {
    // Connection connectionCopy = EcoreUtil.copy(connection);
    // EList<Package> dataPackage = connection.getDataPackage();
    // Collection<Package> newDataPackage = EcoreUtil.copyAll(dataPackage);
    // ConnectionHelper.addPackages(newDataPackage, connectionCopy);
    // return connectionCopy;
    // }

    /**
     * Applys the copies to the actural object, inclues connection and metadata table.
     */
    // protected void applyConnectionCopy() {
    // connectionItem.setConnection(connectionCopy);
    // this.metadataTable = metadataTableCopy;
    // }

    @Override
    public boolean isRepositoryObjectEditable() {
        if (getVersionItem() != null && !creation) {
            if (this instanceof ContextWizard || this instanceof DocumentationCreateWizard
                    || this instanceof DocumentationUpdateWizard) {
                return super.isRepositoryObjectEditable() && isLastVersion(getVersionItem());
            }
        }
        if (getConnectionItem() != null && !creation) {
            return super.isRepositoryObjectEditable() && isLastVersion(getConnectionItem());
        }
        return super.isRepositoryObjectEditable();
    }

    public Item getVersionItem() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.ILastVersionChecker#isLastVersion(org.talend.core.model.properties.Item)
     */
    public boolean isLastVersion(Item item) {
        if (item.getProperty() != null) {
            if (item.getProperty().getId() == null) {
                return true;
            }
            try {
                List<IRepositoryViewObject> allVersion = ProxyRepositoryFactory.getInstance().getAllVersion(
                        item.getProperty().getId());
                if (allVersion != null && !allVersion.isEmpty()) {
                    String lastVersion = VersionUtils.DEFAULT_VERSION;
                    for (IRepositoryViewObject object : allVersion) {
                        if (VersionUtils.compareTo(object.getVersion(), lastVersion) > 0) {
                            lastVersion = object.getVersion();
                        }
                    }
                    if (VersionUtils.compareTo(item.getProperty().getVersion(), lastVersion) == 0) {
                        return true;
                    }
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.ILastVersionChecker#setLastVersion(java.lang.Boolean)
     */
    public void setLastVersion(Boolean lastVersion) {
        // TODO Auto-generated method stub
    }

    protected void updateConnectionItem() throws CoreException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        IWorkspaceRunnable operation = new IWorkspaceRunnable() {

            public void run(IProgressMonitor monitor) throws CoreException {
                try {
                    factory.save(connectionItem);
                    closeLockStrategy();
                } catch (PersistenceException e) {
                    throw new CoreException(new Status(IStatus.ERROR, "org.talend.metadata.management.ui",
                            "Error when save the connection", e));
                }
            }
        };
        ISchedulingRule schedulingRule = workspace.getRoot();
        // the update the project files need to be done in the workspace runnable to avoid all
        // notification of changes before the end of the modifications.
        workspace.run(operation, schedulingRule, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
    }

    @Override
    public boolean performCancel() {
        // connectionCopy = null;
        // metadataTableCopy = null;
        return super.performCancel();
    }

    protected void refreshInFinish(boolean isModified) {
        if (isModified) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                        IDesignerCoreService.class);
                if (service != null) {
                    service.refreshComponentView(connectionItem);
                }
            }
        }
    }
}
