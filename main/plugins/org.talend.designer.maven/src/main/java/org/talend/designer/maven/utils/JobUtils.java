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
package org.talend.designer.maven.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.runprocess.IProcessor;

/**
 * created by ycbai on 2015年4月3日 Detailled comment
 *
 */
public class JobUtils {

    public static List<String> getRunningJobFolders(IProcessor processor) {
        List<String> jobFolders = new ArrayList<String>();
        IProcess process = processor.getProcess();
        String mainJobFolder = JavaResourcesHelper.getJobFolderName(process.getName(), process.getVersion());
        jobFolders.add(mainJobFolder);
        Set<JobInfo> childrenJobInfos = getClonedChildrenJobInfos(processor);
        for (JobInfo jobInfo : childrenJobInfos) {
            String jobFolder = JavaResourcesHelper.getJobFolderName(jobInfo.getJobName(), jobInfo.getJobVersion());
            jobFolders.add(jobFolder);
        }
        IProcess baseProcess = ProcessUtils.getTestContainerBaseProcess(process);
        if (baseProcess != null) {// if it is a test container.
            String parentJobFolder = JavaResourcesHelper.getJobFolderName(baseProcess.getName(), baseProcess.getVersion());
            jobFolders.add(parentJobFolder);
        }

        return jobFolders;
    }

    public static Set<JobInfo> getClonedChildrenJobInfos(IProcessor processor) {
        Set<JobInfo> clonedJobInfos = new HashSet<JobInfo>();
        Set<JobInfo> buildChildrenJobs = processor.getBuildChildrenJobs();
        for (JobInfo jobInfo : buildChildrenJobs) {
            JobInfo newJobInfo = new JobInfo(jobInfo.getJobId(), jobInfo.getContextName(), jobInfo.getJobVersion());
            newJobInfo.setJobName(jobInfo.getJobName());
            newJobInfo.setApplyContextToChildren(jobInfo.isApplyContextToChildren());
            newJobInfo.setContext(jobInfo.getContext());
            newJobInfo.setProjectFolderName(jobInfo.getProjectFolderName());
            newJobInfo.setProcessItem(jobInfo.getProcessItem());
            ProcessItem processItem = newJobInfo.getProcessItem();
            if (processItem == null) {
                try {
                    final IRepositoryViewObject obj = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory()
                            .getSpecificVersion(jobInfo.getJobId(), jobInfo.getJobVersion(), true);
                    if (obj != null) {
                        final Item item = obj.getProperty().getItem();
                        if (item instanceof ProcessItem) {
                            processItem = (ProcessItem) item;
                            newJobInfo.setProcessItem(processItem);
                        }
                    }
                } catch (PersistenceException e) {
                    //
                }
            }
            if (processItem != null) {
                IProcess process = jobInfo.getProcess();
                // get the type
                if (process == null && GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                    IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                            IDesignerCoreService.class);
                    process = service.getProcessFromItem(processItem);
                    newJobInfo.setProcess(process);
                }
                final String projectFolderName = JavaResourcesHelper.getProjectFolderName(processItem);
                newJobInfo.setProjectFolderName(projectFolderName);
            }
            clonedJobInfos.add(newJobInfo);
        }
        return clonedJobInfos;
    }

}
