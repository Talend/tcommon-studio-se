package org.talend.core.service;

import org.talend.core.IService;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.repository.IRepositoryViewObject;

public interface IResourcesDependenciesService extends IService {

    public void copyToExtResourceFolder(IRepositoryViewObject repoObject, String jobLabel, String version);

    public String getResourcePathForContext(IProcess process, String contextName);

}
