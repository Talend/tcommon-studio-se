package org.talend.core.repository.logintask;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.ProjectDataJsonProvider;
import org.talend.login.AbstractLoginTask;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;

public class SyncLibrariesLoginTask extends AbstractLoginTask implements IRunnableWithProgress {

    private static ICoreService coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);

    @Override
    public boolean isCommandlineTask() {
        return true;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            if (ProjectDataJsonProvider.hasFilledProjectSettingFile(ProjectManager.getInstance().getCurrentProject())) {
                return;
            }
        } catch (PersistenceException e1) {
            ExceptionHandler.process(e1);
            return;
        }
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(
                new RepositoryWorkUnit<Void>(Messages.getString("SyncLibrariesLoginTask.createStatsLogAndImplicitParamter")) {

                    @Override
                    protected void run() throws LoginException, PersistenceException {
                        try {
                            IWorkspace workspace = ResourcesPlugin.getWorkspace();
                            IResourceRuleFactory ruleFactory = workspace.getRuleFactory();
                            ProjectManager projectManager = ProjectManager.getInstance();
                            ISchedulingRule refreshRule = ruleFactory.refreshRule(
                                    projectManager.getResourceProject(projectManager.getCurrentProject().getEmfProject()));
                            workspace.run(new IWorkspaceRunnable() {

                                @Override
                                public void run(IProgressMonitor monitor) throws CoreException {
                                    coreService
                                            .createStatsLogAndImplicitParamter(ProjectManager.getInstance().getCurrentProject());
                                }
                            }, refreshRule, IWorkspace.AVOID_UPDATE, monitor);
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                });
    }

}
