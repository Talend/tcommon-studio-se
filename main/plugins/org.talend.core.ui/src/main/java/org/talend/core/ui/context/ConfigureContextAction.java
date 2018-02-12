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
package org.talend.core.ui.context;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.update.EUpdateItemType;
import org.talend.core.model.update.IUpdateItemType;
import org.talend.core.model.update.IUpdateManager;
import org.talend.core.model.update.UpdateManagerHelper;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.model.update.extension.UpdateManagerProviderDetector;

/**
 * An action that can config the contexts. <br/>
 * 
 */
public class ConfigureContextAction extends Action {

    private Shell shell;

    private IContextModelManager manager;

    @SuppressWarnings("restriction")
    public ConfigureContextAction(IContextModelManager modelManager, Shell shell) {
        super("Configure Contexts..."); //$NON-NLS-1$
        this.manager = modelManager;
        this.shell = shell;
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.CONTEXT_CONF_ICON));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        ContextSetConfigurationDialog dialog = new ContextSetConfigurationDialog(shell, manager);
        if (dialog.open() == IDialogConstants.OK_ID) {
            List<IContext> result = dialog.getResultContexts();
            manager.getContextManager().setListContext(result);
            Command command = new Command() {

                @Override
                public void execute() {
                    if (manager.getProcess() != null && manager.getProcess() instanceof IProcess2) {
                        IUpdateManager updateManager = manager.getProcess().getUpdateManager();
                        if (updateManager != null) {
                            List<UpdateResult> updatesNeeded = new ArrayList<UpdateResult>();
                            updatesNeeded.addAll(updateManager.getUpdatesNeeded(EUpdateItemType.CONTEXT));
                            final IUpdateItemType jobletContextType = UpdateManagerProviderDetector.INSTANCE
                                    .getUpdateItemType(UpdateManagerHelper.TYPE_JOBLET_CONTEXT);
                            if (jobletContextType != null) {
                                updatesNeeded.addAll(updateManager.getUpdatesNeeded(jobletContextType));
                            }
                            updateManager.executeUpdates(updatesNeeded);
                        }
                    } else {
                        // set the report editor dirty according to the manager(TdqContextViewComposite)
                        IContextManager contextManager = manager.getContextManager();
                        if (contextManager instanceof JobContextManager) {
                            JobContextManager jobContextManager = (JobContextManager) contextManager;
                            jobContextManager.setModified(true);
                        }
                    }
                    // refresh both
                    manager.refresh();
                }
            };

            if (manager.getCommandStack() == null) {
                command.execute();
            } else {
                manager.getCommandStack().execute(command);
            }
        }
    }
}
