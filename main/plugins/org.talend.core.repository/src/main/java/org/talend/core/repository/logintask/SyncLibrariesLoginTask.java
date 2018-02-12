package org.talend.core.repository.logintask;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.repository.i18n.Messages;
import org.talend.login.AbstractLoginTask;
import org.talend.repository.ProjectManager;

public class SyncLibrariesLoginTask extends AbstractLoginTask implements IRunnableWithProgress {

    private static ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);

    @Override
    public boolean isCommandlineTask() {
        return true;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        TimeMeasure.display = CommonsPlugin.isDebugMode();
        TimeMeasure.displaySteps = CommonsPlugin.isDebugMode();
        TimeMeasure.measureActive = CommonsPlugin.isDebugMode();

        TimeMeasure.begin("SyncLibraries");

        SubMonitor subMonitor = SubMonitor.convert(monitor, 1);
        SubMonitor currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
        currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.synchronizeLibraries"), 1); //$NON-NLS-1$
        coreService.syncLibraries(currentMonitor);
        TimeMeasure.step("SyncLibraries", "sync components libraries");
        if (monitor != null && monitor.isCanceled()) {
            throw new OperationCanceledException(""); //$NON-NLS-1$
        }
        // remove the auto-build to enhance the build speed and application's use
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceDescription description = workspace.getDescription();
        description.setAutoBuilding(false);
        try {
            workspace.setDescription(description);
        } catch (CoreException e) {
            // do nothing
        }
        coreService.createStatsLogAndImplicitParamter(ProjectManager.getInstance().getCurrentProject());
        if (monitor != null && monitor.isCanceled()) {
            throw new OperationCanceledException(""); //$NON-NLS-1$
        }

        if (monitor != null && monitor.isCanceled()) {
            throw new OperationCanceledException(""); //$NON-NLS-1$
        }

        TimeMeasure.end("SyncLibraries");
        TimeMeasure.display = false;
        TimeMeasure.displaySteps = false;
        TimeMeasure.measureActive = false;
    }

}
