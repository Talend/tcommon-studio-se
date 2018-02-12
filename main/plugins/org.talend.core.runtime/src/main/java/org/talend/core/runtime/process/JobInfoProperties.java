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
package org.talend.core.runtime.process;

import java.util.Properties;

import org.talend.commons.CommonsPlugin;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by ggu on 4 Feb 2015 Detailled comment
 *
 */
public class JobInfoProperties extends Properties {

    private static final long serialVersionUID = -1522349324061260504L;

    public static final String JOB_DATE_FORMAT = "yyyy-MM-dd HHmmssSSS"; //$NON-NLS-1$

    private static final String UNDER_LINE_CHAR = "_"; //$NON-NLS-1$

    public static final String PROJECT_ID = "projectId"; //$NON-NLS-1$

    public static final String PROJECT_NAME = "project"; //$NON-NLS-1$

    public static final String JOB_ID = "jobId"; //$NON-NLS-1$

    public static final String JOB_NAME = "job"; //$NON-NLS-1$

    public static final String JOB_TYPE = "jobType"; //$NON-NLS-1$

    public static final String JOB_VERSION = "jobVersion"; //$NON-NLS-1$

    public static final String DATE = "date"; //$NON-NLS-1$

    public static final String BRANCH = "branch"; //$NON-NLS-1$

    public static final String COMMANDLINE_VERSION = "cmdLineVersion"; //$NON-NLS-1$

    public static final String CONTEXT_NAME = "contextName"; //$NON-NLS-1$

    public static final String APPLY_CONTEXY_CHILDREN = "applyContextToChildren"; //$NON-NLS-1$

    public static final String ADD_STATIC_CODE = "statistics"; //$NON-NLS-1$

    private final ProcessItem processItem;

    private final String contextName;

    private final boolean applyContextToChild, addStat;

    public JobInfoProperties(ProcessItem processItem, String contextName, boolean applyContextToChild, boolean addStat) {
        super();
        this.processItem = processItem;
        this.contextName = contextName;
        this.applyContextToChild = applyContextToChild;
        this.addStat = addStat;

        initProperty();
    }

    private void initProperty() {
        JobInfo jobInfo = new JobInfo(processItem, processItem.getProcess().getDefaultContext(), processItem.getProperty()
                .getVersion());
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        setProperty(PROJECT_ID, String.valueOf(currentProject.getEmfProject().getId()));
        setProperty(PROJECT_NAME, currentProject.getTechnicalLabel());

        String branchKey = IProxyRepositoryFactory.BRANCH_SELECTION + UNDER_LINE_CHAR + currentProject.getTechnicalLabel();
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext rc = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (rc.getFields().containsKey(branchKey) && rc.getFields().get(branchKey) != null) {
            String branchSelection = rc.getFields().get(branchKey);
            setProperty(BRANCH, branchSelection);
        }

        setProperty(JOB_ID, jobInfo.getJobId());
        setProperty(JOB_NAME, jobInfo.getJobName());
        String jobType = processItem.getProcess().getJobType();
        if (jobType == null) {
            /*
             * should call ConvertJobsUtil.getJobTypeFromFramework(processItem)
             * 
             * ConvertJobsUtil.JobType.STANDARD.getDisplayName
             */
            jobType = "Standard"; //$NON-NLS-1$
        }
        setProperty(JOB_TYPE, jobType);
        setProperty(JOB_VERSION, jobInfo.getJobVersion());
        setProperty(CONTEXT_NAME, this.contextName);

        setProperty(APPLY_CONTEXY_CHILDREN, String.valueOf(applyContextToChild));
        if (CommonsPlugin.isHeadless()) {
            setProperty(ADD_STATIC_CODE, String.valueOf(addStat));
        } else {
            setProperty(ADD_STATIC_CODE, Boolean.TRUE.toString()); // TDI-23641, in studio, false always.
        }
        setProperty(COMMANDLINE_VERSION, VersionUtils.getVersion());

    }
}
