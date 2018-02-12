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
package org.talend.core.model.update.extension;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.IUpdateItemType;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.model.update.UpdatesConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by ggu on Mar 27, 2014 Detailled comment
 * 
 */
public abstract class AbstractRepositoryUpdateManagerProvider implements IRepositoryUpdateManagerProvider {

    protected boolean enableCheckItem;

    protected IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.update.extension.IRepositoryUpdateManagerProvider#validateAction(org.eclipse.jface.viewers
     * .TreeViewer, org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public boolean validateAction(TreeViewer viewer, IStructuredSelection selection) {
        if (viewer != null && selection != null) {
            // only work for one item
            if (!selection.isEmpty() && selection.size() == 1) {
                // not read only
                if (!factory.isUserReadOnlyOnCurrentProject()) {
                    Object obj = selection.getFirstElement();
                    if (obj != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.update.extension.IRepositoryUpdateManagerProvider#updateForRepository(IStructuredSelection)
     */
    @Override
    public boolean updateForRepository(IStructuredSelection selection) {
        if (!needPropagate(selection)) {
            return false;
        }
        /*
         * NOTE: Most of functions are similar with RepositoryUpdateManager.doWork, so if update the old dowork, maybe
         * need update here too.
         */

        // retrieve Results
        List<UpdateResult> updateResults = retrieveUpdateResults(selection);

        // valid results
        List<UpdateResult> validResults = validResults(updateResults);

        // do update
        if (doUpdate(validResults)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * DOC ggu Comment method "needPropagate".
     * 
     * @return
     */
    @Override
    public boolean needPropagate(IStructuredSelection selection) {
        if (needForcePropagation(selection)) {
            return true;
        }

        // by default, will propagate always.
        return true;
    }

    @Override
    public boolean needForcePropagation(IStructuredSelection selection) {
        return false; // diable to force propagating by default
    }

    /**
     * 
     * DOC ggu Comment method "retrieveUpdateResults".
     * 
     * @param node
     * @return
     */
    public List<UpdateResult> retrieveUpdateResults(final IStructuredSelection selection) {
        final List<UpdateResult> results = new ArrayList<UpdateResult>();
        boolean cancelable = !needForcePropagation(selection);

        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                List<UpdateResult> returnResult = retrieveUpdateResults(monitor, selection);
                if (returnResult != null) {
                    results.addAll(returnResult);
                }
            }
        };

        try {
            // final ProgressMonitorJobsDialog dialog = new ProgressMonitorJobsDialog(null);
            // dialog.run(true, cancelable, runnable);
            final ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
            dialog.run(true, cancelable, runnable);

            // PlatformUI.getWorkbench().getProgressService().run(true, true, runnable);
            return results;
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            if (!e.getMessage().equals(UpdatesConstants.MONITOR_IS_CANCELED)) {
                ExceptionHandler.process(e); // if not cancele, log it.
            }
        }
        return Collections.emptyList();
    }

    protected abstract Set<IUpdateItemType> getTypes();

    /**
     * 
     * DOC ggu Comment method "retrieveUpdateResults".
     * 
     * retrieve the result for current node.
     * 
     * @param selection
     * @return
     */
    public abstract List<UpdateResult> retrieveUpdateResults(IProgressMonitor monitor, IStructuredSelection selection);

    /**
     * 
     * DOC ggu Comment method "validResults".
     * 
     * will do some validation for results, if need.
     * 
     * @param updateResults
     * @return
     */
    public List<UpdateResult> validResults(List<UpdateResult> updateResults) {
        return updateResults; // by default, nothing to check and valid. so retrurn it directly.
    }

    /**
     * 
     * DOC ggu Comment method "doUpdate".
     * 
     * @param validResults
     * @return
     */

    protected boolean doUpdate(List<UpdateResult> validResults) {
        if (validResults != null && !validResults.isEmpty()) {
            IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
            if (designerCoreService != null) {
                return designerCoreService.executeUpdatesManager(validResults, false);
            }
        }
        return false;
    }

    /**
     * @return false, will only check the opened job.
     */
    @Override
    public boolean enableCheckItem(final IRepositoryViewObject object) {
        return enableCheckItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.update.extension.IRepositoryUpdateManagerProvider#setEnableCheckItem(boolean)
     */
    @Override
    public void setEnableCheckItem(boolean checkItem) {
        this.enableCheckItem = checkItem;
    }

}
