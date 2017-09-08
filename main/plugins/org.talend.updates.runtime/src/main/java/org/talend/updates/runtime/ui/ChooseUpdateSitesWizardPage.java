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
package org.talend.updates.runtime.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.UpdateSiteLocationType;

/**
 * created by sgandon on 25 févr. 2013 Detailled comment
 * 
 */
public class ChooseUpdateSitesWizardPage extends WizardPage {

    private Text CustomSiteText;

    private Text localFolderText;

    private Button btnCustomUpdateSite;

    private Button localFolderBrowseButton;

    private Button btnLocalFolder;

    private final UpdateWizardModel updateWizardModel;

    private Button btnDefaultRemoteSites;

    private SelectObservableValue featureRepoLocationTypeObservable;

    /**
     * Create the wizard.
     * 
     * @param updateWizardModel
     */
    public ChooseUpdateSitesWizardPage(UpdateWizardModel updateWizardModel) {
        super("wizardPage"); //$NON-NLS-1$
        this.updateWizardModel = updateWizardModel;
        setTitle(Messages.getString("ChooseUpdateSitesWizardPage.page.title")); //$NON-NLS-1$
        setDescription(Messages.getString("ChooseUpdateSitesWizardPage.page.description")); //$NON-NLS-1$
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            checkDefaultChecked();
        }
    }

    /**
     * Create contents of the wizard.
     * 
     * @param parent
     */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(new GridLayout(3, false));

        btnDefaultRemoteSites = new Button(container, SWT.RADIO);
        btnDefaultRemoteSites.setText(Messages.getString("ChooseUpdateSitesWizardPage.default.remote.update.site")); //$NON-NLS-1$
        btnDefaultRemoteSites.setToolTipText(Messages.getString("ChooseUpdateSitesWizardPage.default.remote.site.tooltip")); //$NON-NLS-1$
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        btnCustomUpdateSite = new Button(container, SWT.RADIO);
        GridData gd_btnCustomUpdateSite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_btnCustomUpdateSite.verticalIndent = 5;
        btnCustomUpdateSite.setLayoutData(gd_btnCustomUpdateSite);
        btnCustomUpdateSite.setText(Messages.getString("ChooseUpdateSitesWizardPage.custom.remote.update.site")); //$NON-NLS-1$
        btnCustomUpdateSite.setToolTipText(Messages.getString("ChooseUpdateSitesWizardPage.custom.remote.site.tooltip")); //$NON-NLS-1$

        CustomSiteText = new Text(container, SWT.BORDER);
        GridData gd_CustomSiteText = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_CustomSiteText.widthHint = 275;
        CustomSiteText.setLayoutData(gd_CustomSiteText);

        btnLocalFolder = new Button(container, SWT.RADIO);
        btnLocalFolder.setText(Messages.getString("ChooseUpdateSitesWizardPage.local.folder")); //$NON-NLS-1$
        btnLocalFolder.setToolTipText(Messages.getString("ChooseUpdateSitesWizardPage.local.folder.tooltip")); //$NON-NLS-1$

        localFolderText = new Text(container, SWT.BORDER);
        localFolderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        localFolderBrowseButton = new Button(container, SWT.NONE);
        localFolderBrowseButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
                directoryDialog.setFilterPath(localFolderText.getText());
                directoryDialog.setMessage(Messages.getString("ChooseUpdateSitesWizardPage.choose.local.folder.dialog.title")); //$NON-NLS-1$
                String path = directoryDialog.open();
                if (path != null) {
                    localFolderText.setText(path);
                }
            }
        });
        localFolderBrowseButton.setText("…"); //$NON-NLS-1$
        initDataBindings();
        setPageComplete(true);
    }

    protected void initDataBindings() {
        featureRepoLocationTypeObservable = new SelectObservableValue(UpdateSiteLocationType.class);
        {
            DataBindingContext defaultRemoteBC = new DataBindingContext();
            // define default Repo bindings
            IObservableValue btnDefaultRemoteSitesObserveSelection = SWTObservables.observeSelection(btnDefaultRemoteSites);
            featureRepoLocationTypeObservable.addOption(UpdateSiteLocationType.DEFAULT_REPO,
                    btnDefaultRemoteSitesObserveSelection);
            // fake binding to trigger an update of the defaultRemoteBC validation to reset the validation message
            defaultRemoteBC.bindValue(btnDefaultRemoteSitesObserveSelection, new WritableValue());
            // bind the validation messages to the wizard page
            WizardPageSupport.create(this, defaultRemoteBC);
        }
        // define remote custom Repo url bindings
        // define validator for text fields
        {
            DataBindingContext remoteRepoBC = new DataBindingContext();
            UpdateValueStrategy afterConvertRemoteRepoValidator = new UpdateValueStrategy()
                    .setAfterConvertValidator(updateWizardModel.new RemoteRepoURIValidator());
            // bind selection to model value
            IObservableValue btnCustomUpdateSiteObserveSelection = SWTObservables.observeSelection(btnCustomUpdateSite);
            featureRepoLocationTypeObservable.addOption(UpdateSiteLocationType.REMOTE_REPO, btnCustomUpdateSiteObserveSelection);
            // bind selection to enable text field
            IObservableValue textObserveEnabled = SWTObservables.observeEnabled(CustomSiteText);
            remoteRepoBC.bindValue(textObserveEnabled, btnCustomUpdateSiteObserveSelection, null, null);
            // bind text modification to model with validation
            IObservableValue customSiteTextObserveText = SWTObservables.observeText(CustomSiteText, SWT.Modify);
            remoteRepoBC.bindValue(customSiteTextObserveText,
                    PojoObservables.observeValue(updateWizardModel, "featureRepositories.remoteRepoUriStr"), //$NON-NLS-1$
                    afterConvertRemoteRepoValidator, null);
            // bind the validation messages to the wizard page
            WizardPageSupport.create(this, remoteRepoBC);
        }
        {
            // define local folder Repo bindings
            DataBindingContext localRepoBC = new DataBindingContext();
            UpdateValueStrategy afterConvertLocalFolderValidator = new UpdateValueStrategy()
                    .setAfterConvertValidator(updateWizardModel.new LocalRepoFolderValidator());

            // bind selection to model
            IObservableValue btnLocalFolderObserveSelection = SWTObservables.observeSelection(btnLocalFolder);
            featureRepoLocationTypeObservable.addOption(UpdateSiteLocationType.LOCAL_FOLDER, btnLocalFolderObserveSelection);

            // bind selection to text fiedl enabled
            IObservableValue localFolderTextObserveEnabled = SWTObservables.observeEnabled(localFolderText);
            localRepoBC.bindValue(localFolderTextObserveEnabled, btnLocalFolderObserveSelection, null, null);
            // bind selection to browse button enabled
            IObservableValue localFolderBrowseButtonObserveEnabled = SWTObservables.observeEnabled(localFolderBrowseButton);
            localRepoBC.bindValue(localFolderBrowseButtonObserveEnabled, btnLocalFolderObserveSelection, null, null);
            // bind text field to model
            IObservableValue localFolderTextObserveText = SWTObservables.observeText(localFolderText, SWT.Modify);
            localRepoBC.bindValue(localFolderTextObserveText,
                    PojoObservables.observeValue(updateWizardModel, "featureRepositories.localRepoPathStr"), //$NON-NLS-1$
                    afterConvertLocalFolderValidator, null);
            //
            // bind the validation messages to the wizard page
            WizardPageSupport.create(this, localRepoBC);
        }
        DataBindingContext radioBC = new DataBindingContext();
        radioBC.bindValue(featureRepoLocationTypeObservable,
                PojoObservables.observeValue(updateWizardModel, "featureRepositories.updateSiteLocationType")); //$NON-NLS-1$

    }

    private void checkDefaultChecked() {
        Set<UpdateSiteLocationType> types = new HashSet<UpdateSiteLocationType>();
        final Set<ExtraFeature> selectedExtraFeatures = updateWizardModel.getSelectedExtraFeatures();
        for (ExtraFeature feature : selectedExtraFeatures) {
            types.addAll(feature.getUpdateSiteCompatibleTypes());
        }

        final boolean hasDefaultRepo = types.contains(UpdateSiteLocationType.DEFAULT_REPO);
        final boolean hasRemoteRepo = types.contains(UpdateSiteLocationType.REMOTE_REPO);
        final boolean hasLocalFolder = types.contains(UpdateSiteLocationType.LOCAL_FOLDER);

        btnDefaultRemoteSites.setEnabled(hasDefaultRepo);
        btnCustomUpdateSite.setEnabled(hasRemoteRepo);
        localFolderBrowseButton.setEnabled(hasLocalFolder);

        UpdateSiteLocationType type = null;
        if (hasDefaultRepo) {
            type = UpdateSiteLocationType.DEFAULT_REPO;
        } else {
            if (hasRemoteRepo) {
                type = UpdateSiteLocationType.REMOTE_REPO;
            } else {
                type = UpdateSiteLocationType.LOCAL_FOLDER;
            }
        }
        featureRepoLocationTypeObservable.setValue(type);

    }

}
