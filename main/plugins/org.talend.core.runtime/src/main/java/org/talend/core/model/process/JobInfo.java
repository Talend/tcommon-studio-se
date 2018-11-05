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
package org.talend.core.model.process;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.ItemCacheManager;

/**
 * DOC nrousseau ProcessController class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class JobInfo {

    private String jobId, jobName, contextName, jobVersion;

    private IProcess process;

    private IContext context;

    boolean applyContextToChildren = false;

    private ProcessItem processItem;

    private JobInfo fatherJobInfo;

    private boolean forceRegenerate;

    private String projectFolderName;

    private boolean testContainer = false;

    private Map<String, Object> argumentsMap;

    private IProcessor processor;

    private IFile pomFile;

    private IFile codeFile;

    private Property jobletProperty;

    private boolean isJoblet;

    public JobInfo(String jobId, String contextName, String version) {
        this.jobId = jobId;
        this.contextName = contextName;
        this.jobVersion = version;
    }

    public JobInfo(Property jobletProperty, String contextName) {
        this.jobId = jobletProperty.getId();
        this.jobName = jobletProperty.getLabel();
        this.jobVersion = jobletProperty.getVersion();
        this.contextName = contextName;
        this.jobletProperty = jobletProperty;
        isJoblet = true;
    }

    /**
     * DOC nrousseau JobInfo constructor comment.
     * 
     * @param process2
     * @param context2
     */
    public JobInfo(IProcess process, IContext context) {
        jobId = process.getId();
        jobName = process.getName();
        contextName = context.getName();
        jobVersion = process.getVersion();
        this.context = context;
        this.process = process;
    }

    /**
     * DOC nrousseau JobInfo constructor comment.
     * 
     * @param process2
     * @param contextName2
     */
    public JobInfo(ProcessItem processItem, String contextName) {
        this.processItem = processItem;
        jobId = processItem.getProperty().getId();
        jobName = processItem.getProperty().getLabel();
        this.contextName = contextName;
        jobVersion = processItem.getProperty().getVersion();

        // check if the selected context exists, if not, use the default context of the job.
        boolean contextExists = false;
        for (Object object : processItem.getProcess().getContext()) {
            if (object instanceof ContextType) {
                if (((ContextType) object).getName() != null && ((ContextType) object).getName().equals(contextName)) {
                    contextExists = true;
                    continue;
                }
            }
        }
        if (!contextExists) {
            this.contextName = processItem.getProcess().getDefaultContext();
        }
    }

    public JobInfo(ProcessItem processItem, Property property, String contextName) {
        this.processItem = processItem;
        jobId = property.getId();
        jobName = property.getLabel();
        this.contextName = contextName;
        jobVersion = property.getVersion();

        // check if the selected context exists, if not, use the default context of the job.
        boolean contextExists = false;
        for (Object object : processItem.getProcess().getContext()) {
            if (object instanceof ContextType) {
                if (((ContextType) object).getName() != null && ((ContextType) object).getName().equals(contextName)) {
                    contextExists = true;
                    continue;
                }
            }
        }
        if (!contextExists) {
            this.contextName = processItem.getProcess().getDefaultContext();
        }
    }

    /**
     * DOC nrousseau JobInfo constructor comment.
     * 
     * @param process2
     * @param contextName2
     */
    public JobInfo(ProcessItem processItem, String contextName, String processVersion) {
        this.processItem = processItem;
        jobId = processItem.getProperty().getId();
        jobName = processItem.getProperty().getLabel();
        this.contextName = contextName;
        jobVersion = processVersion;

        // check if the selected context exists, if not, use the default context of the job.
        boolean contextExists = false;
        for (Object object : processItem.getProcess().getContext()) {
            if (object instanceof ContextType) {
                if (((ContextType) object).getName() != null && ((ContextType) object).getName().equals(contextName)) {
                    contextExists = true;
                    continue;
                }
            }
        }
        if (!contextExists) {
            this.contextName = processItem.getProcess().getDefaultContext();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getContextName()
     */
    public String getContextName() {
        return contextName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setContextName(java.lang.String)
     */
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getJobId()
     */
    public String getJobId() {
        return jobId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setJobId(java.lang.String)
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getProcess()
     */
    public IProcess getProcess() {
        return process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setProcess(org.talend.core.model.process.IProcess)
     */
    public void setProcess(IProcess process) {
        this.process = process;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getContext()
     */
    public IContext getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setContext(org.talend.core.model.process.IContext)
     */
    public void setContext(IContext context) {
        this.context = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getJobVersion()
     */
    public String getJobVersion() {
        return this.jobVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setJobVersion(java.lang.String)
     */
    public void setJobVersion(String jobVersion) {
        this.jobVersion = jobVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#isApplyContextToChildren()
     */
    public boolean isApplyContextToChildren() {
        return this.applyContextToChildren;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setApplyContextToChildren(boolean)
     */
    public void setApplyContextToChildren(boolean applyContextToChildren) {
        this.applyContextToChildren = applyContextToChildren;
    }

    public ProcessItem getProcessItem() {
        return this.processItem;
    }

    public void setProcessItem(ProcessItem processItem) {
        this.processItem = processItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getJobName()
     */
    public String getJobName() {
        return this.jobName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setJobName(java.lang.String)
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + ((contextName == null) ? 0 : contextName.hashCode());
        result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
        result = prime * result + ((jobVersion == null) ? 0 : jobVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        final JobInfo other = (JobInfo) obj;
        // if (contextName == null) {
        // if (other.contextName != null) {
        // return false;
        // }
        // } else if (!contextName.equals(other.contextName)) {
        // return false;
        // }
        if (jobId == null) {
            if (other.jobId != null) {
                return false;
            }
        } else if (!jobId.equals(other.jobId)) {
            return false;
        }
        // if (context == null) {
        // if (other.context != null) {
        // return false;
        // }
        // } else if (!context.equals(other.context)) {
        // return false;
        // }
        // if (process == null) {
        // if (other.process != null) {
        // return false;
        // }
        // } else if (!process.equals(other.process)) {
        // return false;
        // }
        if (jobVersion == null) {
            if (other.jobVersion != null) {
                return false;
            }
        } else if (!jobVersion.equals(other.jobVersion)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "job:" + jobName + " / context:" + contextName + " / version:" + jobVersion; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getFatherJobInfo()
     */
    public JobInfo getFatherJobInfo() {
        return this.fatherJobInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setFatherJobInfo(org.talend.designer.runprocess.IJobInfo)
     */
    public void setFatherJobInfo(JobInfo fatherJobInfo) {
        this.fatherJobInfo = fatherJobInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#isForceRegenerate()
     */
    public boolean isForceRegenerate() {
        return this.forceRegenerate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setForceRegenerate(boolean)
     */
    public void setForceRegenerate(boolean forceRegenerate) {
        this.forceRegenerate = forceRegenerate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#getProjectFolderName()
     */
    public String getProjectFolderName() {
        return this.projectFolderName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IJobInfo#setProjectFolderName(java.lang.String)
     */
    public void setProjectFolderName(String projectFolderName) {
        this.projectFolderName = projectFolderName;
    }

    /**
     * Getter for testContainer.
     * 
     * @return the testContainer
     */
    public boolean isTestContainer() {
        return testContainer;
    }

    /**
     * Sets the testContainer.
     * 
     * @param testContainer the testContainer to set
     */
    public void setTestContainer(boolean testContainer) {
        this.testContainer = testContainer;
    }

    public Map<String, Object> getArgumentsMap() {
        return argumentsMap;
    }

    public void setArgumentsMap(Map<String, Object> argumentsMap) {
        this.argumentsMap = argumentsMap;
    }

    /**
     * Getter for processor.
     * 
     * @return the processor
     */
    public IProcessor getProcessor() {
        return this.processor;
    }

    /**
     * 
     * DOC wchen Comment method "getReloadedProcessor". Reload the processor only if need to get the process/property in
     * the processor
     * 
     * @return
     */
    public IProcessor getReloadedProcessor() {
        if (this.processor == null || processor.getProcess() == null) {
            IProcess process = null;
            ProcessItem processItem = getProcessItem();
            if (processItem == null && getJobId() == null && getJobVersion() != null) {
                processItem = ItemCacheManager.getProcessItem(getJobId(), getJobVersion());
            }
            if (processItem == null) {
                return null;
            }
            if (getProcess() == null) {
                if (processItem != null) {
                    IDesignerCoreService service = CoreRuntimePlugin.getInstance().getDesignerCoreService();
                    process = service.getProcessFromProcessItem(processItem);
                    if (process instanceof IProcess2) {
                        ((IProcess2) process).setProperty(processItem.getProperty());
                    }
                }
                if (process == null) {
                    return null;
                }
            } else {
                process = getProcess();
            }

            Property curProperty = processItem.getProperty();
            if (curProperty == null && process instanceof IProcess2) {
                curProperty = ((IProcess2) process).getProperty();
            }

            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault()
                        .getService(IRunProcessService.class);
                IProcessor processor = service.createCodeProcessor(process, curProperty, ((RepositoryContext) CoreRuntimePlugin
                        .getInstance().getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject().getLanguage(),
                        true);

                setProcessor(processor);
            }
        }
        return this.processor;
    }

    /**
     * Sets the processor.
     * 
     * @param processor the processor to set
     */
    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }

    /**
     * Getter for pomFile.
     * 
     * @return the pomFile
     */
    public IFile getPomFile() {
        return this.pomFile;
    }

    /**
     * Sets the pomFile.
     * 
     * @param pomFile the pomFile to set
     */
    public void setPomFile(IFile pomFile) {
        this.pomFile = pomFile;
    }

    /**
     * Getter for codeFile.
     * 
     * @return the codeFile
     */
    public IFile getCodeFile() {
        return this.codeFile;
    }

    /**
     * Sets the codeFile.
     * 
     * @param codeFile the codeFile to set
     */
    public void setCodeFile(IFile codeFile) {
        this.codeFile = codeFile;
    }

    public Property getJobletProperty() {
        return jobletProperty;
    }

    public boolean isJoblet() {
        return isJoblet;
    }

}
