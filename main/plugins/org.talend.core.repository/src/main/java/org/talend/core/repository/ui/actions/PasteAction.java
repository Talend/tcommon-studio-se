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
package org.talend.core.repository.ui.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.core.ui.ICDCProviderService;
import org.talend.core.ui.IJobletProviderService;
import org.talend.designer.core.convert.ProcessConvertManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PasteAction extends AContextualAction {

    private static PasteAction singleton;

    public PasteAction() {
        super();
        setId(ActionFactory.PASTE.getId());
        this.setText(Messages.getString("PasteAction.thisText.paste")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.PASTE_ICON));
        //        this.setActionDefinitionId("pasteItem"); //$NON-NLS-1$
        singleton = this;
    }

    public static PasteAction getInstance() {
        return singleton;
    }

    @Override
    protected void doRun() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        RepositoryNode target = (RepositoryNode) selection.getFirstElement();
        CopyObjectAction copyObjectAction = CopyObjectAction.getInstance();
        TreeSelection selectionInClipboard = (TreeSelection) LocalSelectionTransfer.getTransfer().getSelection();
        if (selectionInClipboard != null) {
            for (Object currentSource : selectionInClipboard.toArray()) {
                RepositoryNode sourceNode = (RepositoryNode) currentSource;
                // To check if the source copied node meets the condition to convert or paste for TDI-25360.(Ctrl+V)
                if (ProcessConvertManager.getInstance().CheckConvertProcess(sourceNode, target)) {
                    String jobNewName = null;
                    String jobTypeValue = null;
                    String frameworkNewValue = null;
                    if (target.getContentType() == ERepositoryObjectType.PROCESS) {
                        jobTypeValue = ConvertJobsUtil.JobType.STANDARD.getDisplayName();
                    } else if (target.getContentType() == ERepositoryObjectType.PROCESS_STORM) {
                        jobTypeValue = ConvertJobsUtil.JobType.BIGDATASTREAMING.getDisplayName();
                        frameworkNewValue = ConvertJobsUtil.JobStreamingFramework.SPARKSTREAMINGFRAMEWORK.getDisplayName();
                    } else if (target.getContentType() == ERepositoryObjectType.PROCESS_MR) {
                        jobTypeValue = ConvertJobsUtil.JobType.BIGDATABATCH.getDisplayName();
                        frameworkNewValue = ConvertJobsUtil.JobBatchFramework.MAPREDUCEFRAMEWORK.getDisplayName();
                    }
                    try {
                        jobNewName = ConvertJobsUtil.getDuplicateName(sourceNode, sourceNode.getObject().getLabel());
                    } catch (BusinessException e) {
                        jobNewName = sourceNode.getObject().getLabel();
                    }
                    //
                    Object sourceFramework = null;
                    if (sourceNode.getObject() != null) {
                        sourceFramework = sourceNode.getObject().getProperty().getAdditionalProperties()
                                .get(ConvertJobsUtil.FRAMEWORK);
                    }
                    ConvertJobsUtil.createOperation(jobNewName, jobTypeValue, frameworkNewValue, sourceNode.getObject());
                    // reset the framework
                    if (sourceNode.getObject() != null) {
                        Object currentFramework = sourceNode.getObject().getProperty().getAdditionalProperties()
                                .get(ConvertJobsUtil.FRAMEWORK);
                        if (sourceFramework != null && currentFramework != null && !sourceFramework.equals(currentFramework)) {
                            sourceNode.getObject().getProperty().getAdditionalProperties()
                                    .put(ConvertJobsUtil.FRAMEWORK, sourceFramework);
                        }
                    }
                } else {
                    try {
                        if (copyObjectAction.validateAction((RepositoryNode) currentSource, target)) {
                            copyObjectAction.execute((RepositoryNode) currentSource, target);
                        } else {
                            MessageDialog.openWarning(new Shell(), Messages.getString("PasteObjectAction.error.title"), Messages //$NON-NLS-1$
                                    .getString("PasteObjectAction.error.labelAlreadyExists")); //$NON-NLS-1$
                        }
                    } catch (BusinessException e) {
                        MessageBoxExceptionHandler.process(e);
                    } catch (Exception e) {
                        CommonExceptionHandler.process(e);
                    }
                }
            }

            ERepositoryObjectType contentType = target.getContentType();
            if (contentType == null) {
                contentType = target.getObjectType();
            }
            if (contentType != null && contentType == ERepositoryObjectType.JOBLET) {
                if (PluginChecker.isJobLetPluginLoaded()) {
                    IJobletProviderService jobletService = (IJobletProviderService) GlobalServiceRegister.getDefault()
                            .getService(IJobletProviderService.class);
                    if (jobletService != null) {
                        jobletService.loadComponentsFromProviders();
                    }
                }
            }
        }
    }

    @Override
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean enabled = true;
        // if (selection.size() != 1) { // only one current node selected
        // setEnabled(false);
        // return;
        // }

        Object targetObject = selection.getFirstElement();
        if (!(targetObject instanceof RepositoryNode)) {
            setEnabled(false);
            return;
        }

        RepositoryNode target = (RepositoryNode) targetObject;
        if (!(LocalSelectionTransfer.getTransfer().getSelection() instanceof TreeSelection)) {
            setEnabled(false);
            return;
        }

        if (target.isBin()) {
            setEnabled(false);
            return;
        }

        if (target.getObject() != null && target.getObject().getProperty() != null
                && target.getObject().getProperty().getItem() != null) {
            Item item = target.getObject().getProperty().getItem();
            if (item.getState() != null && item.getState().isDeleted()) {
                setEnabled(false);
                return;
            }
        }

        TreeSelection selectionInClipboard = (TreeSelection) LocalSelectionTransfer.getTransfer().getSelection();
        IProxyRepositoryFactory proxyFactory = ProxyRepositoryFactory.getInstance();
        IRepositoryViewObject object = target.getObject();

        if (target.getContentType() == ERepositoryObjectType.JOB_DOC
                || target.getContentType() == ERepositoryObjectType.JOBLET_DOC
                || target.getContentType() == ERepositoryObjectType.GENERATED
                || target.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC
                || target.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC
                || target.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.REFERENCED_PROJECTS
                || (target.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_VALIDATION_RULES && target
                        .getParent().getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_VALIDATIONS_RULES_FOLDER)) {
            visible = false;
            enabled = false;
        } else if (object != null) {
            if (target != null && proxyFactory.getStatus(object) == ERepositoryStatus.READ_ONLY) {
                enabled = false;
                visible = false;
            } else if (target != null && object instanceof Folder && enabled) {
                visible = true;
            }
        } else if (isReferencedProject(target)) {
            visible = false;
            enabled = false;
        }
        if (enabled && selectionInClipboard != null && !((StructuredSelection) selectionInClipboard).isEmpty()) {
            visible = true;
            ERepositoryObjectType objectType = null;
            for (Object obj : ((StructuredSelection) selectionInClipboard).toArray()) {
                if (enabled) {
                    if (!(obj instanceof RepositoryNode)) {
                        enabled = false;
                        break;
                    }
                    RepositoryNode sourceNode = (RepositoryNode) obj;
                    ERepositoryObjectType type = sourceNode.getObjectType();
                    if (objectType != null && objectType != type) {
                        enabled = false; // different objects was copyed
                        break;
                    } else {
                        objectType = type;
                    }
                    if (CopyObjectAction.getInstance().validateAction(sourceNode, target)) {
                        enabled = true;
                    } else {
                        enabled = false;
                    }
                } else {
                    break;
                }
            }
        } else {
            enabled = false;
            visible = false;
        }
        // for cdc
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICDCProviderService.class)) {
            ICDCProviderService cdcService = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(
                    ICDCProviderService.class);
            if (cdcService != null && cdcService.isSubscriberTableNode(target)) {
                enabled = false;
                visible = false;
            }
        }
        setEnabled(enabled);
    }

    public boolean isReferencedProject(RepositoryNode node) {
        if (node.getType() == ENodeType.REFERENCED_PROJECT) {
            return true;
        } else if (node.getParent() != null) {
            node = node.getParent();
            if (isReferencedProject(node)) {
                return true;
            }
        }
        return false;
    }

    private boolean visible;

    /**
     * Getter for visible.
     * 
     * @return the visible
     */
    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the visible.
     * 
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
