// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.update;

import org.talend.core.model.properties.ConnectionItem;

/**
 * ggu class global comment. Detailled comment
 */
public abstract class UpdateResult {

    private boolean checked = true;

    private boolean fromItem = false;

    private Object object = null;

    private EUpdateResult resultType = null;

    private IUpdateItemType updateType = null;

    private Object parameter = null;

    private String remark = null;

    private boolean readOnlyProcess = false;

    private ConnectionItem connItem;

    private boolean needReloadJoblet = true;

    protected String jobInfor;

    protected boolean isJoblet;
    
    protected boolean isSparkJoblet;
    
    protected boolean isSparkStreamingJoblet;

    protected boolean isMR;

    protected boolean isStreaming;

    /**
     * closed process only save ID and VERSION in UpdateResult
     */
    protected String objectId;

    protected String objectVersion;

    /**
     * For opened process job is an instance of Process, For closed process job must be null to save some memory
     */
    private Object job = null;
    
    private RepositoryUpdateManager repositoryUpdateManager;

    /**
     * Getter for readOnlyProcess.
     * 
     * @return the readOnlyProcess
     */
    public boolean isReadOnlyProcess() {
        return this.readOnlyProcess;
    }

    /**
     * Sets the readOnlyProcess.
     * 
     * @param readOnlyProcess the readOnlyProcess to set
     */
    public void setReadOnlyProcess(boolean readOnlyProcess) {
        this.readOnlyProcess = readOnlyProcess;
    }

    private boolean readOnly = false;

    /**
     * only for repository item update.
     */
    // private Object processOrItem = null;

    /**
     * ggu UpdateCheckResult constructor comment.
     * 
     * @param item
     */
    public UpdateResult(Object object) {
        super();
        this.object = object;
    }

    /**
     * 
     * ggu Comment method "setResult".
     * 
     * @param updateType
     * @param resultType
     * @param parameter
     * @param remark
     */
    public void setResult(IUpdateItemType updateType, EUpdateResult resultType, Object parameter, String remark) {
        this.updateType = updateType;
        this.resultType = resultType;
        this.parameter = parameter;
        this.remark = remark;
        // must initialize the checked==true, so it will show in the dialog
        switch (resultType) {
        case UPDATE:
            break;
        case ADD:
        case DELETE:
            setChecked(true);
            break;
        case JOBLET_UPDATE:
        case RELOAD:
            setChecked(true);
            break;
        default:
            setChecked(true);
            setReadOnly(true);
        }
    }

    /**
     * 
     * hwang Comment method "setResult".
     * 
     * @param updateType
     * @param resultType
     * @param parameter
     * @param remark
     * @param open
     */
    public void setResult(EUpdateItemType updateType, EUpdateResult resultType, Object parameter, String remark,
            boolean openedJoblet) {
        this.updateType = updateType;
        this.resultType = resultType;
        this.parameter = parameter;
        this.remark = remark;
        // must initialize the checked==true, so it will show in the dialog
        switch (resultType) {
        case UPDATE:
            break;
        case ADD:
        case DELETE:
            setChecked(true);
            break;
        case JOBLET_UPDATE:
        case RELOAD:
            if (openedJoblet) {
                setChecked(true);
                setReadOnly(true);
            } else {
                setChecked(false);
            }
            break;
        default:
            // if (openedJoblet) {
            setChecked(true);
            // } else {
            // setChecked(false);
            // }

            setReadOnly(true);
        }
    }

    public void setResult(EUpdateItemType updateType, EUpdateResult resultType, Object parameter) {
        setResult(updateType, resultType, parameter, null);
    }

    public void setResult(EUpdateItemType updateType, EUpdateResult resultType) {
        setResult(updateType, resultType, null);
    }

    public void setJob(Object job) {
        this.job = job;
        updateJobInfor();
    }

    public Object getJob() {
        return this.job;
    }

    // public Object getItemProcess() {
    // return this.processOrItem;
    // }
    //
    // public void setItemProcess(Object processOrItem) {
    // this.processOrItem = processOrItem;
    // }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Object getUpdateObject() {
        return this.object;
    }

    public EUpdateResult getResultType() {
        return this.resultType;
    }

    public IUpdateItemType getUpdateType() {
        return this.updateType;
    }

    public Object getParameter() {
        return this.parameter;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public abstract String getJobInfor();

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(checkValues(getJobInfor()));
        sb.append(UpdatesConstants.SPACE);
        sb.append(checkValues(getCategory()));
        sb.append(UpdatesConstants.SPACE);
        sb.append(checkValues(getName()));
        sb.append(UpdatesConstants.SPACE);
        sb.append(checkValues(getResultType()));
        sb.append(UpdatesConstants.SPACE);
        sb.append(checkValues(getRemark()));
        sb.append(UpdatesConstants.SPACE);

        return sb.toString();
    }

    private Object checkValues(Object obj) {
        return obj == null ? UpdatesConstants.EMPTY : obj;

    }

    public ConnectionItem getContextModeConnectionItem() {
        return this.connItem;
    }

    public void setContextModeConnectionItem(ConnectionItem connItem) {
        this.connItem = connItem;
    }

    public abstract String getName();

    public abstract String getCategory();

    public boolean isNeedReloadJoblet() {
        return needReloadJoblet;
    }

    public void setNeedReloadJoblet(boolean needReloadJoblet) {
        this.needReloadJoblet = needReloadJoblet;
    }

    protected abstract void updateJobInfor();

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectVersion() {
        return objectVersion;
    }

    public void setObjectVersion(String objectVersion) {
        this.objectVersion = objectVersion;
    }

    public boolean isJoblet() {
        return isJoblet;
    }
    
    public boolean isSparkJoblet() {
        return isSparkJoblet;
    }
    
    public boolean isSparkStreamingJoblet() {
        return isSparkStreamingJoblet;
    }

    public boolean isMR() {
        return isMR;
    }

    public boolean isStreaming() {
        return this.isStreaming;
    }

    public void setUpdateObject(Object object) {
        this.object = object;
    }

    public boolean isFromItem() {
        return this.fromItem;
    }

    public void setFromItem(boolean fromItem) {
        this.fromItem = fromItem;
    }

    public RepositoryUpdateManager getRepositoryUpdateManager() {
        return repositoryUpdateManager;
    }

    public void setRepositoryUpdateManager(RepositoryUpdateManager repositoryUpdateManager) {
        this.repositoryUpdateManager = repositoryUpdateManager;
    }

}
