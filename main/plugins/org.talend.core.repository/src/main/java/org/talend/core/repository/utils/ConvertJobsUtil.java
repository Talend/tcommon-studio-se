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
package org.talend.core.repository.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.hadoop.HadoopConstants;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.hd.IHDConstants;
import org.talend.core.runtime.hd.IHDistributionVersion;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.core.convert.IProcessConvertService;
import org.talend.designer.core.convert.IProcessConvertToAllTypeService;
import org.talend.designer.core.convert.ProcessConvertManager;
import org.talend.designer.core.convert.ProcessConverterType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IJobSettingsView;

/**
 * created by hcyi on May 25, 2015 Detailled comment
 *
 */
public class ConvertJobsUtil {

    public static final String FRAMEWORK = HadoopConstants.FRAMEWORK;

    public static final String STORM_FRAMEWORK = HadoopConstants.FRAMEWORK_STORM;

    public static final String SPARKSTREAMING_FRAMEWORK = HadoopConstants.FRAMEWORK_SPARKSTREAMING;

    public static final String MAPREDUCE_FRAMEWORK = HadoopConstants.FRAMEWORK_MAPREDUCE;

    public static final String SPARK_FRAMEWORK = HadoopConstants.FRAMEWORK_SPARK;

    private static final String MR_VERSION = "MR_VERSION"; //$NON-NLS-1$

    public static enum JobType {
        STANDARD("Standard", "_STANDARD_", ERepositoryObjectType.PROCESS), //$NON-NLS-1$ //$NON-NLS-2$
        BIGDATASTREAMING("Big Data Streaming", "_BIG_DATA_STREAMING_", ERepositoryObjectType.PROCESS_STORM), //$NON-NLS-1$ //$NON-NLS-2$
        BIGDATABATCH("Big Data Batch", "_BIG_DATA_BATCH_", ERepositoryObjectType.PROCESS_MR); //$NON-NLS-1$ //$NON-NLS-2$

        private String displayName;

        private String fileName;

        private ERepositoryObjectType repositoryObjectType;

        JobType(String displayName, String fileName, ERepositoryObjectType repositoryObjectType) {
            this.displayName = displayName;
            this.fileName = fileName;
            this.repositoryObjectType = repositoryObjectType;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getFileName() {
            return this.fileName;
        }

        public ERepositoryObjectType getERepositoryObjectType() {
            return this.repositoryObjectType;
        }

        public static String[] getJobTypeToDispaly() {
            String[] dispalyNames = new String[values().length];
            List<String> dispalyNamesList = new ArrayList<String>();
            for (int i = 0; i < values().length; i++) {
                dispalyNamesList.add(i, values()[i].getDisplayName());
            }
            if (!PluginChecker.isStormPluginLoader()) {
                dispalyNamesList.remove(JobType.BIGDATASTREAMING.getDisplayName());
            }
            if (!PluginChecker.isMapReducePluginLoader()) {
                dispalyNamesList.remove(JobType.BIGDATABATCH.getDisplayName());
            }
            dispalyNames = new String[dispalyNamesList.size()];
            for (int j = 0; j < dispalyNamesList.size(); j++) {
                if (dispalyNamesList.get(j) != null) {
                    dispalyNames[j] = dispalyNamesList.get(j);
                }
            }
            return dispalyNames;
        }

        public static JobType getJobTypeByDisplayName(String displayName) {
            if (displayName == null) {
                return null;
            }
            JobType jobTypes[] = JobType.values();
            if (jobTypes == null) {
                return null;
            }
            for (JobType jobType : jobTypes) {
                if (jobType.getDisplayName().equals(displayName)) {
                    return jobType;
                }
            }
            return null;
        }

    }

    public static enum JobStreamingFramework {
        STORMFRAMEWORK("Storm", "_STORM_STORM_FRAMEWORK_"), //$NON-NLS-1$ //$NON-NLS-2$
        SPARKSTREAMINGFRAMEWORK("Spark Streaming", "_STORM_SPARKSTREAMING_FRAMEWORK_"); //$NON-NLS-1$ //$NON-NLS-2$

        private String displayName;

        private String fileName;

        JobStreamingFramework(String displayName, String fileName) {
            this.displayName = displayName;
            this.fileName = fileName;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getFileName() {
            return this.fileName;
        }

        public static String[] getFrameworkToDispaly() {
            String[] dispalyNames = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                dispalyNames[i] = values()[i].getDisplayName();
            }
            return dispalyNames;
        }

        public static String[] getFrameworkToDispaly(String framework) {
        	if(framework == null){
        		return getFrameworkToDispaly();
        	}
            String[] dispalyNames = new String[1];
            dispalyNames[0] = framework;
            return dispalyNames;
        }
    }

    public static enum JobBatchFramework {
        MAPREDUCEFRAMEWORK("MapReduce", "_MAPREDUCE_FRAMEWORK_"), //$NON-NLS-1$ //$NON-NLS-2$
        SPARKFRAMEWORK("Spark", "_SPARK_FRAMEWORK_"); //$NON-NLS-1$ //$NON-NLS-2$

        private String displayName;

        private String fileName;

        JobBatchFramework(String displayName, String fileName) {
            this.displayName = displayName;
            this.fileName = fileName;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getFileName() {
            return this.fileName;
        }

        public static String[] getFrameworkToDispaly() {
            String[] dispalyNames = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                dispalyNames[i] = values()[i].getDisplayName();
            }
            return dispalyNames;
        }

        public static String[] getFrameworkToDispaly(String framework) {
        	if(framework == null){
        		return getFrameworkToDispaly();
        	}
            String[] dispalyNames = new String[1];
            dispalyNames[0] = framework;
            return dispalyNames;
        }
    }

    public static enum Status {
        DEVELOPMENT("development", "_DEVELOPMENT_"), //$NON-NLS-1$ //$NON-NLS-2$
        TESTING("testing", "_TESTING_"), //$NON-NLS-1$ //$NON-NLS-2$
        PRODUCTION("production", "_PRODUCTION_"); //$NON-NLS-1$ //$NON-NLS-2$

        private String displayName;

        private String fileName;

        Status(String displayName, String fileName) {
            this.displayName = displayName;
            this.fileName = fileName;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getFileName() {
            return this.fileName;
        }

        public static String[] getStatusToDispaly() {
            String[] dispalyNames = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                dispalyNames[i] = values()[i].getDisplayName();
            }
            return dispalyNames;
        }
    }

    public static void updateJobFrameworkPart(String jobTypeValue, CCombo frameworkCombo) {
        frameworkCombo.setEnabled(true);
        if (JobType.STANDARD.getDisplayName().equals(jobTypeValue)) {
            frameworkCombo.setItems(new String[0]);
            frameworkCombo.setText("");//$NON-NLS-1$
            frameworkCombo.setEnabled(false);
        } else if (JobType.BIGDATABATCH.getDisplayName().equals(jobTypeValue)) {
            String[] items = JobBatchFramework.getFrameworkToDispaly();
            frameworkCombo.setItems(items);
            if (items.length > 0) {
                frameworkCombo.select(0);
            }
        } else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobTypeValue)) {
            String[] items = JobStreamingFramework.getFrameworkToDispaly();
            frameworkCombo.setItems(items);
            if (items.length > 0) {
                frameworkCombo.select(0);
            }
        }
    }

    public static void updateJobFrameworkPart(String jobTypeValue, CCombo frameworkCombo, boolean isJoblet) {
    	if(!isJoblet){
    		updateJobFrameworkPart(jobTypeValue, frameworkCombo);
    		return;
    	}
        frameworkCombo.setEnabled(true);
        if (JobType.STANDARD.getDisplayName().equals(jobTypeValue)) {
            frameworkCombo.setItems(new String[0]);
            frameworkCombo.setText("");//$NON-NLS-1$
            frameworkCombo.setEnabled(false);
        } else if (JobType.BIGDATABATCH.getDisplayName().equals(jobTypeValue)) {
            String[] items = JobBatchFramework.getFrameworkToDispaly(JobBatchFramework.SPARKFRAMEWORK.getDisplayName());
            frameworkCombo.setItems(items);
            if (items.length > 0) {
                frameworkCombo.select(0);
            }
        } else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobTypeValue)) {
            String[] items = JobStreamingFramework.getFrameworkToDispaly(JobStreamingFramework.SPARKSTREAMINGFRAMEWORK.getDisplayName());
            frameworkCombo.setItems(items);
            if (items.length > 0) {
                frameworkCombo.select(0);
            }
        }
    }

    /**
     * get the target execution framework from the field in Job properties
     *
     * @param item
     * @return
     */
    public static String getFramework(Item item) {
        if (item != null) {
            Property property = item.getProperty();
            if (property != null && property.getAdditionalProperties() != null
                    && property.getAdditionalProperties().containsKey(FRAMEWORK)) {
                return (String) property.getAdditionalProperties().get(FRAMEWORK);
            }
        }
        return null;
    }

    public static String getJobTypeFromFramework(Item item) {
        String frameworkObj = ConvertJobsUtil.getFramework(item);
        return getJobTypeFromFramework(frameworkObj);
    }

    /**
     * DOC nrousseau Comment method "getJobTypeFromFramework".
     *
     * @param frameworkObj
     * @return
     */
    public static String getJobTypeFromFramework(String frameworkObj) {
        if (JobBatchFramework.MAPREDUCEFRAMEWORK.getDisplayName().equals(frameworkObj)
                || JobBatchFramework.SPARKFRAMEWORK.getDisplayName().equals(frameworkObj)) {
            return JobType.BIGDATABATCH.getDisplayName();
        } else if (JobStreamingFramework.STORMFRAMEWORK.getDisplayName().equals(frameworkObj)
                || JobStreamingFramework.SPARKSTREAMINGFRAMEWORK.getDisplayName().equals(frameworkObj)) {
            return JobType.BIGDATASTREAMING.getDisplayName();
        } else {
            return JobType.STANDARD.getDisplayName();
        }
    }

    public static String[] getFrameworkItemsByJobType(String jobType) {
        if (JobType.BIGDATABATCH.getDisplayName().equals(jobType)) {
            return JobBatchFramework.getFrameworkToDispaly();
        } else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobType)) {
            return JobStreamingFramework.getFrameworkToDispaly();
        } else {
            return new String[0];
        }
    }

    public static String[] getFrameworkItemsByJobType(String jobType, boolean isJoblet) {
    	if(!isJoblet){
    		return getFrameworkItemsByJobType(jobType);
    	}
        if (JobType.BIGDATABATCH.getDisplayName().equals(jobType)) {
            return JobBatchFramework.getFrameworkToDispaly(JobBatchFramework.SPARKFRAMEWORK.getDisplayName());
        } else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobType)) {
            return JobStreamingFramework.getFrameworkToDispaly(JobStreamingFramework.SPARKSTREAMINGFRAMEWORK.getDisplayName());
        } else {
            return new String[0];
        }
    }

    public static boolean isNeedConvert(Item originalItem, String newJobTypeValue, String newFrameworkValue) {
        return isNeedConvert(originalItem, newJobTypeValue, newFrameworkValue, false);
    }

    public static boolean isNeedConvert(Item originalItem, String newJobTypeValue, String newFrameworkValue,
            boolean needPopupWarning) {
        String oldJobTypeValue = ConvertJobsUtil.getJobTypeFromFramework(originalItem);
        String oldFrameworkValue = null;
        Object frameworkObj = ConvertJobsUtil.getFramework(originalItem);
        if (frameworkObj != null) {
            oldFrameworkValue = frameworkObj.toString();
        }
        JobType oldJobType = JobType.getJobTypeByDisplayName(oldJobTypeValue);
        JobType newJobType = JobType.getJobTypeByDisplayName(newJobTypeValue);
        ERepositoryObjectType oldRepType = (oldJobType == null ? null : oldJobType.getERepositoryObjectType());
        ERepositoryObjectType newRepType = (newJobType == null ? null : newJobType.getERepositoryObjectType());

        boolean isNeedConvert = ProcessConvertManager.getInstance().CheckConvertProcess(oldRepType, oldFrameworkValue,
                newRepType, newFrameworkValue);

        // if need popup warning, then do this check
        if (isNeedConvert && needPopupWarning) {
            if (MAPREDUCE_FRAMEWORK.equals(oldFrameworkValue)) {
                boolean isSpark = SPARK_FRAMEWORK.equals(newFrameworkValue);
                boolean isSparkStreaming = SPARKSTREAMING_FRAMEWORK.equals(newFrameworkValue);
                if (isSpark || isSparkStreaming) {
                    try {
                        IProcessConvertService converter = ProcessConvertManager.getInstance().extractConvertService(
                                ProcessConverterType.CONVERTER_FOR_MAPREDUCE);

                        IProcess process = converter.getProcessFromItem(originalItem, false);
                        IElementParameter mrVersion = process.getElementParameter(MR_VERSION);
                        if (mrVersion != null
                                && GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
                            IHadoopDistributionService hadoopService = (IHadoopDistributionService) GlobalServiceRegister
                                    .getDefault().getService(IHadoopDistributionService.class);
                            IHDistributionVersion distributionVersion = hadoopService.getHadoopDistributionVersion(
                                    (String) mrVersion.getValue(), false);
                            if (distributionVersion != null) {
                                boolean isSupport = false;
                                if (isSpark) {
                                    isSupport = hadoopService.doSupportService(distributionVersion, IHDConstants.SERVICE_SPARK);
                                } else if (isSparkStreaming) {
                                    isSupport = hadoopService.doSupportService(distributionVersion,
                                            IHDConstants.SERVICE_SPARK_STREAMING);
                                }
                                if (!isSupport) {
                                    MessageDialog.openWarning(Display.getDefault().getActiveShell(),
                                            Messages.getString("ConvertJobsUtil.warning.title"), //$NON-NLS-1$
                                            Messages.getString("ConvertJobsUtil.warning.message")); //$NON-NLS-1$
                                }
                            }
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }

            }
        }

        return isNeedConvert;
    }

    public static boolean hasTestCase(Project project, Property property) {
        boolean hasTestCase = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                hasTestCase = testContainerService.hasTestCase(project, property);
            }
        }
        return hasTestCase;
    }

    public static boolean hasTestCase(Property property) {
        boolean hasTestCase = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                hasTestCase = testContainerService.hasTestCase(property);
            }
        }
        return hasTestCase;
    }

    public static Item createOperation(final String newJobName, final String jobTypeValue, final String frameworkValue,
            final IRepositoryViewObject sourceObject) {
        if (sourceObject == null || sourceObject.getProperty() == null || newJobName == null) {
            return null;
        }
        Item item = sourceObject.getProperty().getItem();
        if(item instanceof ProcessItem){
            return createProcessOperation(newJobName, jobTypeValue,  frameworkValue, sourceObject);
        }else if(item instanceof JobletProcessItem){
            return createJobletOperation(newJobName, jobTypeValue,  frameworkValue, sourceObject);
        }
        return null;
    }

    public static Item createProcessOperation(final String newJobName, final String jobTypeValue, final String frameworkValue,
            final IRepositoryViewObject sourceObject) {
        IProcessConvertService converter = null;
        if (sourceObject == null || sourceObject.getProperty() == null || newJobName == null) {
            return null;
        }
        Item item = sourceObject.getProperty().getItem();
        if (JobType.STANDARD.getDisplayName().equals(jobTypeValue)) {
            String sourceJobType = getJobTypeFromFramework(item);
            if (JobType.BIGDATASTREAMING.getDisplayName().equals(sourceJobType)
                    || ERepositoryObjectType.PROCESS_STORM == sourceObject.getRepositoryObjectType()) {
                converter = ProcessConvertManager.getInstance().extractConvertService(ProcessConverterType.CONVERTER_FOR_STORM);
            } else if (JobType.BIGDATABATCH.getDisplayName().equals(sourceJobType)
                    || ERepositoryObjectType.PROCESS_MR == sourceObject.getRepositoryObjectType()) {
                converter = ProcessConvertManager.getInstance().extractConvertService(
                        ProcessConverterType.CONVERTER_FOR_MAPREDUCE);
            }
            if (converter != null && converter instanceof IProcessConvertToAllTypeService) {
                return ((IProcessConvertToAllTypeService) converter).convertToProcess(item, sourceObject, newJobName,
                        jobTypeValue);
            }
        } else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobTypeValue)) {
            converter = ProcessConvertManager.getInstance().extractConvertService(ProcessConverterType.CONVERTER_FOR_STORM);
            if (converter != null && converter instanceof IProcessConvertToAllTypeService) {
                return ((IProcessConvertToAllTypeService) converter).convertToProcessStreaming(item, sourceObject, newJobName,
                        jobTypeValue, frameworkValue);
            }
        } else if (JobType.BIGDATABATCH.getDisplayName().equals(jobTypeValue)) {
            converter = ProcessConvertManager.getInstance().extractConvertService(ProcessConverterType.CONVERTER_FOR_MAPREDUCE);
            if (converter != null && converter instanceof IProcessConvertToAllTypeService) {
                return ((IProcessConvertToAllTypeService) converter).convertToProcessBatch(item, sourceObject, newJobName,
                        jobTypeValue, frameworkValue);
            }
        }
        return null;
    }

    public static Item createJobletOperation(final String newJobName, final String jobTypeValue, final String frameworkValue,
            final IRepositoryViewObject sourceObject) {
        IProcessConvertService converter = null;
        if (sourceObject == null || sourceObject.getProperty() == null || newJobName == null) {
            return null;
        }
        Item item = sourceObject.getProperty().getItem();
        if (JobType.STANDARD.getDisplayName().equals(jobTypeValue)) {
            String sourceJobType = getJobTypeFromFramework(item);
            if (JobType.BIGDATABATCH.getDisplayName().equals(sourceJobType)
                    || ERepositoryObjectType.PROCESS_MR == sourceObject.getRepositoryObjectType()) {
                converter = ProcessConvertManager.getInstance().extractConvertService(
                        ProcessConverterType.CONVERTER_FOR_SPARK_JOBLET);
            }else if(JobType.BIGDATASTREAMING.getDisplayName().equals(sourceJobType)
                    || ERepositoryObjectType.PROCESS_STORM == sourceObject.getRepositoryObjectType()) {
                converter = ProcessConvertManager.getInstance().extractConvertService(ProcessConverterType.CONVERTER_FOR_SPARK_STREAMING_JOBLET);
            }
            if (converter != null && converter instanceof IProcessConvertToAllTypeService) {
                return ((IProcessConvertToAllTypeService) converter).convertToProcess(item, sourceObject, newJobName,
                        jobTypeValue);
            }
        } else if (JobType.BIGDATABATCH.getDisplayName().equals(jobTypeValue)) {
            converter = ProcessConvertManager.getInstance().extractConvertService(ProcessConverterType.CONVERTER_FOR_SPARK_JOBLET);
            if (converter != null && converter instanceof IProcessConvertToAllTypeService) {
                return ((IProcessConvertToAllTypeService) converter).convertToProcessBatch(item, sourceObject, newJobName,
                        jobTypeValue, frameworkValue);
            }
        } else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobTypeValue)) {
            converter = ProcessConvertManager.getInstance().extractConvertService(ProcessConverterType.CONVERTER_FOR_SPARK_STREAMING_JOBLET);
            if (converter != null && converter instanceof IProcessConvertToAllTypeService) {
                return ((IProcessConvertToAllTypeService) converter).convertToProcessStreaming(item, sourceObject, newJobName,
                        jobTypeValue, frameworkValue);
            }
        }
        return null;
    }

    public static boolean convert(String newJobName, String jobTypeValue, String frameworkValue,
            final IRepositoryViewObject sourceObject) throws CoreException {
        final Item newItem = ConvertJobsUtil.createOperation(newJobName, jobTypeValue, frameworkValue, sourceObject);
        if (newItem == null) {
            return false;
        }
        convertTestcases(newItem,sourceObject,jobTypeValue);
        boolean isNewItemCreated = true;
        Property repositoryProperty = sourceObject.getProperty();
        if (repositoryProperty != null) {
            isNewItemCreated = (repositoryProperty.getItem() != newItem);
        }
        final IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        if (isNewItemCreated) {
            // delete the old item
            IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

                @Override
                public void run(final IProgressMonitor monitor) throws CoreException {

                    try {
                        proxyRepositoryFactory.unlock(sourceObject);
                        proxyRepositoryFactory.deleteObjectPhysical(sourceObject);
                        proxyRepositoryFactory.saveProject(ProjectManager.getInstance().getCurrentProject());
                    } catch (PersistenceException e1) {
                        CommonExceptionHandler.process(e1);
                    } catch (LoginException e) {
                        CommonExceptionHandler.process(e);
                    }
                }
            };
            // unlockObject();
            // alreadyEditedByUser = true; // to avoid 2 calls of unlock
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            ISchedulingRule schedulingRule = workspace.getRoot();
            // the update the project files need to be done in the workspace runnable to avoid all notification
            // of changes before the end of the modifications.
            workspace.run(runnable, schedulingRule, IWorkspace.AVOID_UPDATE, null);
            updateJobsettingView();
        } else if (sourceObject.getProperty() != null) {

            IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

                @Override
                public void run(final IProgressMonitor monitor) throws CoreException {
                    try {
                        proxyRepositoryFactory.save(ProjectManager.getInstance().getCurrentProject(), sourceObject.getProperty()
                                .getItem(), false);
                    } catch (PersistenceException e1) {
                        CommonExceptionHandler.process(e1);
                    }
                }
            };

            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            ISchedulingRule schedulingRule = workspace.getRoot();
            workspace.run(runnable, schedulingRule, IWorkspace.AVOID_UPDATE, null);
        }
        return isNewItemCreated;
    }

    private static void updateJobsettingView() {
        IViewPart jobSettingView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .findView(IJobSettingsView.ID);
        if (jobSettingView != null && jobSettingView instanceof IJobSettingsView) {
            ((IJobSettingsView) jobSettingView).cleanDisplay();
        }
    }

    public static void convertTestcases(final Item newItem,final IRepositoryViewObject sourceObject,final String jobTypeValue){
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                testContainerService.copyDataFiles(newItem, sourceObject.getId());
                Item item =  sourceObject.getProperty().getItem();
                if(!(item instanceof ProcessItem)){
                  return;
                }
                testContainerService.copyTestCase(newItem, item, getTestCasePath(newItem,jobTypeValue), null, false);
            }
        }
    }

    public static IPath getTestCasePath(Item newItem, String jobTypeValue) {
        StringBuffer pathName = new StringBuffer();
        if (JobType.STANDARD.getDisplayName().equals(jobTypeValue)) {
            pathName.append(JobType.STANDARD.repositoryObjectType.getFolder());
        }else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobTypeValue)){
            pathName.append(JobType.BIGDATASTREAMING.repositoryObjectType.getFolder());
        }else if (JobType.BIGDATABATCH.getDisplayName().equals(jobTypeValue)){
            pathName.append(JobType.BIGDATABATCH.repositoryObjectType.getFolder());
        } else {
            pathName.append("process");
        }
        pathName.append(File.separator).append(newItem.getProperty().getId());
        final Path path = new Path(pathName.toString());
        return path;
    }

    /**
     * DOC nrousseau Comment method "getFrameworkItemsByJobType".
     *
     * @param repositoryObjectType
     * @return
     */
    public static String[] getFrameworkItemsByJobType(ERepositoryObjectType repositoryObjectType) {
        if (repositoryObjectType != null) {
            if (repositoryObjectType.equals(ERepositoryObjectType.PROCESS_MR)) {
                return JobBatchFramework.getFrameworkToDispaly();
            } else if (repositoryObjectType.equals(ERepositoryObjectType.PROCESS_STORM)) {
                return JobStreamingFramework.getFrameworkToDispaly();
            }else if(repositoryObjectType.equals(ERepositoryObjectType.SPARK_JOBLET)){
            	return JobBatchFramework.getFrameworkToDispaly(JobBatchFramework.SPARKFRAMEWORK.getDisplayName());
            }else if(repositoryObjectType.equals(ERepositoryObjectType.SPARK_STREAMING_JOBLET)){
            	return JobStreamingFramework.getFrameworkToDispaly(JobStreamingFramework.SPARKSTREAMINGFRAMEWORK.getDisplayName());
            }
        }
        return new String[0];
    }

    /**
     * Update framework if change it when duplicating DOC Comment method "updateFramework".
     *
     * @param item
     * @param newFrameworkNewValue
     */
    public static void updateFramework(Item item, String frameworkNewValue) {
        if (item instanceof ProcessItem) {
            Property newProperty = item.getProperty();
            if (newProperty != null && newProperty.getAdditionalProperties() != null
                    && newProperty.getAdditionalProperties().containsKey(ConvertJobsUtil.FRAMEWORK)) {
                String currentFramework = (String) newProperty.getAdditionalProperties().get(ConvertJobsUtil.FRAMEWORK);
                if (currentFramework != null && frameworkNewValue != null && !currentFramework.equals(frameworkNewValue)) {
                    newProperty.getAdditionalProperties().put(ConvertJobsUtil.FRAMEWORK, frameworkNewValue);
                }
            }
        }
    }

    public static String getDuplicateName(RepositoryNode node, String value) throws BusinessException {
        char j = 'a';
        String temp = value + "_a";//$NON-NLS-1$
        while (validJobName(node, temp)) {
            if (j > 'z') {
                //
            }
            temp = value + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return temp;
    }

    public static boolean validJobName(RepositoryNode node, String itemName) {
        IProxyRepositoryFactory proxyRepositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            List<IRepositoryViewObject> listExistingObjects = proxyRepositoryFactory.getAll(ERepositoryObjectType.PROCESS, true,
                    false);
            if (PluginChecker.isStormPluginLoader()) {
                listExistingObjects.addAll(proxyRepositoryFactory.getAll(ERepositoryObjectType.PROCESS_STORM, true, false));
            }
            if (PluginChecker.isMapReducePluginLoader()) {
                listExistingObjects.addAll(proxyRepositoryFactory.getAll(ERepositoryObjectType.PROCESS_MR, true, false));
            }
            if (!proxyRepositoryFactory.isNameAvailable(node.getObject().getProperty().getItem(), itemName, listExistingObjects)) {
                return true;
            }
        } catch (PersistenceException e1) {
            return true;
        }
        return false;
    }
}
