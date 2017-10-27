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
package org.talend.core.repository.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.core.repository.utils.ConvertJobsUtil.JobBatchFramework;
import org.talend.core.repository.utils.ConvertJobsUtil.JobStreamingFramework;
import org.talend.core.repository.utils.ConvertJobsUtil.JobType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * created by hcyi on May 25, 2015 Detailled comment
 *
 */
public class DuplicateDialog extends CustomInputDialog {

    private LabelledCombo jobTypeCombo;

    private LabelledCombo frameworkCombo;

    private String jobTypeValue = null;

    private String frameworkValue = null;

    private RepositoryNode sourceNode;

    private Composite typeGroup;

    public DuplicateDialog(RepositoryNode sourceNode, String dialogTitle, String dialogMessage, String jobNameValue,
            IInputValidator validator) {
        super(null, dialogTitle, dialogMessage, jobNameValue, validator);
        this.sourceNode = sourceNode;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        super.createMessageWidget(parent, composite);
        super.createInputWidget(composite);
        typeGroup = new Composite(composite, SWT.NONE);
        GridLayout typeLayout = new GridLayout();
        typeLayout.numColumns = 6;
        typeGroup.setLayout(typeLayout);
        typeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        jobTypeCombo = new LabelledCombo(typeGroup, "Job Type:", "job type", JobType.getJobTypeToDispaly(), 2, true); //$NON-NLS-1$//$NON-NLS-2$
        frameworkCombo = new LabelledCombo(typeGroup, "Framework:", "framework", new String[0], 2, true); //$NON-NLS-1$//$NON-NLS-2$
        super.createErrorMessageWidget(composite);
        applyDialogFont(composite);
        // add listener
        addListener();
        // init
        init();
        return parent;
    }

    private void init() {
        if (sourceNode != null) {
            // job type
            final Object contentType = sourceNode.getProperties(EProperties.CONTENT_TYPE);
            if (contentType == ERepositoryObjectType.PROCESS || contentType == ERepositoryObjectType.JOBLET) {
                jobTypeValue = JobType.STANDARD.getDisplayName();
                jobTypeCombo.setText(jobTypeValue);
            } else if (contentType == ERepositoryObjectType.PROCESS_MR || contentType == ERepositoryObjectType.SPARK_JOBLET) {
                jobTypeValue = JobType.BIGDATABATCH.getDisplayName();
                jobTypeCombo.setText(jobTypeValue);
            } else if (contentType == ERepositoryObjectType.PROCESS_STORM || contentType == ERepositoryObjectType.SPARK_STREAMING_JOBLET) {
                jobTypeValue = JobType.BIGDATASTREAMING.getDisplayName();
                jobTypeCombo.setText(jobTypeValue);
            } else if (contentType == ERepositoryObjectType.PROCESS_ROUTE
                || contentType == ERepositoryObjectType.PROCESS_ROUTELET) {
                jobTypeCombo.setVisible(false);
                frameworkCombo.setVisible(false);
            }
            // framework
            Property property = sourceNode.getObject().getProperty();
            Item item = property.getItem();
            Object obj = ConvertJobsUtil.getFramework(item);
            if (obj != null) {
                frameworkValue = ConvertJobsUtil.getFramework(item).toString();
                frameworkCombo.setText(frameworkValue);
            }
        }
    }

    private void addListener() {
        jobTypeCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                jobTypeValue = jobTypeCombo.getText();
                updateJobFrameworkPart();
                typeGroup.layout();
            }
        });
        frameworkCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                frameworkValue = frameworkCombo.getText();
            }
        });
    }

    public String getJobTypeValue() {
        return this.jobTypeValue;
    }

    public String getFrameworkValue() {
        return this.frameworkValue;
    }

    private void updateJobFrameworkPart() {
    	boolean isJoblet = false;
    	if(sourceNode.getObject().getProperty().getItem() instanceof JobletProcessItem){
    		isJoblet = true;
    	}
        if (JobType.STANDARD.getDisplayName().equals(jobTypeValue)) {
            frameworkCombo.getCombo().setItems(new String[0]);
            frameworkCombo.setReadOnly(true);
        } else if (JobType.BIGDATABATCH.getDisplayName().equals(jobTypeValue)) {
        	String[] items = null;
        	if(isJoblet){
        		items = JobBatchFramework.getFrameworkToDispaly(JobBatchFramework.SPARKFRAMEWORK.getDisplayName());
        	}else{
        		items = JobBatchFramework.getFrameworkToDispaly();
        	}
            frameworkCombo.getCombo().setItems(items);
            if (items.length > 0) {
                frameworkCombo.getCombo().select(0);
            }
            frameworkCombo.setReadOnly(false);
        } else if (JobType.BIGDATASTREAMING.getDisplayName().equals(jobTypeValue)) {
            String[] items = null;
            if(isJoblet){
            	items = JobStreamingFramework.getFrameworkToDispaly(JobStreamingFramework.SPARKSTREAMINGFRAMEWORK.getDisplayName());
            }else{
            	items = JobStreamingFramework.getFrameworkToDispaly();
            }
            frameworkCombo.getCombo().setItems(items);
            if (items.length > 0) {
                frameworkCombo.getCombo().select(0);
            }
            frameworkCombo.setReadOnly(false);
        }
    }
}
