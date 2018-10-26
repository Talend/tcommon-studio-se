package org.talend.core.service;

import org.eclipse.swt.widgets.Shell;
import org.talend.core.IService;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.repository.IRepositoryViewObject;

public interface IResourcesDependenciesService extends IService {

    public String openResourcesDialogForContext(Shell parentShell);

    public void copyToExtResourceFolder(IRepositoryViewObject repoObject, String jobId, String jobVersion, String version,
            String rootJobLabel);

    public String getResourcePathForContext(IProcess process, String resourceContextValue);

    public String getResourceItemFilePath(String resourceContextValue);

    public void refreshDependencyViewer();

    public void setContextParameterChangeDirtyManually();

    public void removeBuildJobCacheForResource(String resourceId);
}
