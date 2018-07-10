package org.talend.core.repository.logintask;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
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
        coreService.createStatsLogAndImplicitParamter(ProjectManager.getInstance().getCurrentProject());
    }

}
