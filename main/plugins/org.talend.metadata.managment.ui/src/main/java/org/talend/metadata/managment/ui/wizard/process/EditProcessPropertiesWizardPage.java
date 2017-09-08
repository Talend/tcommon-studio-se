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
package org.talend.metadata.managment.ui.wizard.process;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.core.repository.utils.ConvertJobsUtil.JobType;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.utils.JobImageUtil;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.wizard.PropertiesWizard;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * Created by Marvin Wang on Feb 18, 2013.
 */
public class EditProcessPropertiesWizardPage extends PropertiesWizardPage {

    private Button convertBtn;

    protected CCombo jobTypeCCombo;

    protected CCombo framework;

    /**
     * DOC marvin EditProcessPropertiesWizardPage constructor comment.
     * 
     * @param pageName
     * @param property
     * @param destinationPath
     */
    public EditProcessPropertiesWizardPage(String pageName, Property property, IPath destinationPath) {
        super(pageName, property, destinationPath);
        // TODO Auto-generated constructor stub
    }

    /**
     * DOC marvin EditProcessPropertiesWizardPage constructor comment.
     * 
     * @param pageName
     * @param property
     * @param destinationPath
     * @param readOnly
     * @param editPath
     */
    public EditProcessPropertiesWizardPage(String pageName, Property property, IPath destinationPath, boolean readOnly,
            boolean editPath) {
        super(pageName, property, destinationPath, readOnly, editPath);
        // TODO Auto-generated constructor stub
    }

    /**
     * DOC marvin EditProcessPropertiesWizardPage constructor comment.
     * 
     * @param pageName
     * @param property
     * @param destinationPath
     * @param readOnly
     * @param editPath
     * @param lastVersionFound
     */
    public EditProcessPropertiesWizardPage(String pageName, Property property, IPath destinationPath, boolean readOnly,
            boolean editPath, String lastVersionFound) {
        super(pageName, property, destinationPath, readOnly, editPath, lastVersionFound);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        boolean alreadyEditedByUser = ((EditProcessPropertiesWizard) this.getWizard()).isAlreadyEditedByUser();
        if (alreadyEditedByUser) {
            Label label = new Label(container, SWT.NONE);
            label.setForeground(ColorConstants.red);
            label.setText(Messages.getString("PropertiesWizard.alreadyLockedByUser")); //$NON-NLS-1$
            GridData gridData = new GridData();
            gridData.horizontalSpan = 2;
            label.setLayoutData(gridData);
        }
        
        super.createControl(container);
        // Added by Marvin Wang on Jan. 29, 2013.
        // createBottomPart(container);

        setControl(container);
        updateContent();
        addListeners();
        setPageComplete(false);
    }
    
    

    @Override
    protected void createFrameworkPart(Composite parent) {
        if (!PluginChecker.isMapReducePluginLoader() && !PluginChecker.isStormPluginLoader()) {
            return;
        }
     // Job type
        Label jobTypeLabel = new Label(parent, SWT.NONE);
        jobTypeLabel.setText(Messages.getString("PropertiesWizardPage.jobTypeLabel")); //$NON-NLS-1$

        Composite typeGroup = new Composite(parent, SWT.NONE);
        typeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout typeGroupLayout = new GridLayout(3, false);
        typeGroupLayout.marginHeight = 0;
        typeGroupLayout.marginWidth = 0;
        typeGroupLayout.horizontalSpacing = 0;
        typeGroup.setLayout(typeGroupLayout);

        jobTypeCCombo = new CCombo(typeGroup, SWT.BORDER);
        jobTypeCCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        jobTypeCCombo.setEditable(false);
        jobTypeCCombo.setItems(JobType.getJobTypeToDispaly());
        jobTypeCCombo.setText(JobType.STANDARD.getDisplayName());
        jobTypeCCombo.setEnabled(!readOnly);

        // Framework
        Label label = new Label(typeGroup, SWT.NONE);
        label.setText("  " + Messages.getString("PropertiesWizardPage.framework") + "  "); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

        framework = new CCombo(typeGroup, SWT.BORDER);
        framework.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        framework.setEditable(false);
        framework.setItems(new String[0]);
        framework.setEnabled(false);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        if (jobTypeCCombo != null) {
            jobTypeCCombo.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    ConvertJobsUtil.updateJobFrameworkPart(jobTypeCCombo.getText(), framework);
                    updatePageStatus();
                    updateFrameworkIcon(framework.getText());
                }
            });
        }
        if (framework != null) {
            framework.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(ModifyEvent e) {
                    updatePageStatus();
                    updateFrameworkIcon(framework.getText());
                }
            });
        }
    }

    protected void updateFrameworkIcon(String framework) {
        IImage wizardIcon = JobImageUtil.getWizardIcon(jobTypeCCombo.getText(), framework);
        if (wizardIcon != null) {
            ImageDescriptor imageDescriptor = ImageProvider.getImageDesc(wizardIcon);
            this.setImageDescriptor(imageDescriptor);
        }
    }

    @Override
    protected void createBottomPart(Composite parent) {
        // Added by Marvin Wang on Feb. 18 , 2013 for common process to add a convert button which can convert the
        // common process to M/R process.
        convertBtn = new Button(parent, SWT.PUSH);
        convertBtn.setText(Messages.getString("EditProcessPropertiesWizardPage.convert.button.name")); //$NON-NLS-1$
        GridDataFactory.swtDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(false, false).applyTo(convertBtn);
        convertBtn.setEnabled(!isReadOnly());
        convertBtn.setVisible(PluginChecker.isMapReducePluginLoader());
        convertBtn.setVisible(false);
    }

    @Override
    protected void regListeners() {
        // regConvertBtnListener();
    }

    /**
     * Registers a listener for convert button. Added by Marvin Wang on Jan 29, 2013.
     */
    protected void regConvertBtnListener() {
        convertBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                PropertiesWizard propWizard = ((PropertiesWizard) getWizard());
                try {

                    doConvert();
                    propWizard.setUnlockRequired(!converter.isOriginalItemDeleted());
                    propWizard.getContainer().getShell().close();
                } catch (PersistenceException e1) {
                    propWizard.setUnlockRequired(!converter.isOriginalItemDeleted());
                    propWizard.getContainer().getShell().close();
                    e1.printStackTrace();
                    e1.printStackTrace();
                } catch (BusinessException e2) {
                    propWizard.setUnlockRequired(!converter.isOriginalItemDeleted());
                    propWizard.getContainer().getShell().close();
                    e2.printStackTrace();
                }
            }
        });
    }

    /**
     * Converts m/r process to common process. Added by Marvin Wang on Jan 29, 2013.
     */
    protected void doConvert() throws PersistenceException, BusinessException {
        IWizard wizard = this.getWizard();
        if (wizard instanceof EditProcessPropertiesWizard) {
            EditProcessPropertiesWizard mrPropWizard = (EditProcessPropertiesWizard) wizard;
            IRepositoryViewObject repViewObject = mrPropWizard.getRepositoryViewObject();
            converter.convertFromProcess(getItem(), repViewObject);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.PropertiesWizardPage#getRepositoryObjectType()
     */
    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        EditProcessPropertiesWizard wizard = (EditProcessPropertiesWizard) getWizard();
        return wizard.getObject().getRepositoryObjectType();
    }

    @Override
    protected List<IRepositoryViewObject> loadRepViewObjectWithOtherTypes() throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();

        // List for all other processes
        List<IRepositoryViewObject> processList = getAllProcessTypeObjectsWithoutCurrentType();
        if (processList != null && !processList.isEmpty()) {
            list.addAll(processList);
        }

        // List for routine
        if (ERepositoryObjectType.ROUTINES != null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
                IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IProxyRepositoryService.class);

                List<IRepositoryViewObject> mrList = service.getProxyRepositoryFactory().getAll(ERepositoryObjectType.ROUTINES,
                        true, false);
                if (mrList != null && mrList.size() > 0) {
                    list.addAll(mrList);
                }
            }
        }

        return list;
    }

}
