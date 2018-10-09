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
package org.talend.updates.runtime.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.update.PreferenceKeys;
import org.talend.commons.utils.system.EclipseCommandLine;
import org.talend.updates.runtime.Constants;
import org.talend.updates.runtime.engine.ExtraFeaturesUpdatesFactory;
import org.talend.updates.runtime.engine.InstallNewFeatureJob;
import org.talend.updates.runtime.engine.component.ComponentNexusP2ExtraFeature;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;

/**
 * created by sgandon on 25 févr. 2013 Detailled comment
 * 
 */
public class UpdateStudioWizard extends Wizard {

    private static Logger log = Logger.getLogger(UpdateStudioWizard.class);

    /**
     * wizard node preference key to check is user does not want the wizard to show up at the start.
     */
    public static final String DO_NOT_SHOW_WIZARD_KEY = "do.not.show.wizard"; //$NON-NLS-1$

    /**
     * preference node for the org.talend.updates plugin.
     */
    public static final String ORG_TALEND_UPDATES_PREF_NODE = Constants.PLUGIN_ID;

    UpdateWizardModel updateWizardModel;// model that hold all the parameters set in the wizard

    Button doNotShowAgainCB;

    private SelectExtraFeaturesToInstallWizardPage selectExtraFeaturesToInstallWizardPage;

    public UpdateStudioWizard(Set<ExtraFeature> extraFeatures) {
        this.updateWizardModel = new UpdateWizardModel(extraFeatures);
        setWindowTitle(Messages.getString("UpdateStudioWizard.wizard.windows.title")); //$NON-NLS-1$
    }

    @Override
    public void addPages() {
        // we keep this instancve as a field just to reset wizard state after the initial runnable is finished
        selectExtraFeaturesToInstallWizardPage = new SelectExtraFeaturesToInstallWizardPage(updateWizardModel);
        addPage(selectExtraFeaturesToInstallWizardPage);
        addPage(new ChooseUpdateSitesWizardPage(updateWizardModel));
    }

    private boolean needRestart() {
        boolean _needRestart = false;
        if (updateWizardModel.selectedExtraFeatures != null) {
            for (Object feature : updateWizardModel.selectedExtraFeatures) {
                if (feature instanceof ExtraFeature) {
                    if (((ExtraFeature) feature).needRestart()) {
                        _needRestart = true;
                        break;
                    }
                    if (feature instanceof ComponentNexusP2ExtraFeature) {
                        _needRestart = true;
                        break;
                    }
                }
            }
        }
        return _needRestart;
    }

//    Force a restart instead of reload components
// This can be improved later if we want to avoid the restart
    
//    private boolean needReloadComponents() {
//        boolean _needReload = false;
//        if (updateWizardModel.selectedExtraFeatures != null) {
//            for (Object feature : updateWizardModel.selectedExtraFeatures) {
//                if (feature instanceof ComponentNexusP2ExtraFeature) {
//                    _needReload = true;
//                    break;
//                }
//            }
//        }
//        return _needReload;
//    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean performFinish() {
        storeDoNotShowAgainPref();
        InstallNewFeatureJob installNewFeatureJob = new InstallNewFeatureJob(
                new HashSet<ExtraFeature>(updateWizardModel.selectedExtraFeatures), updateWizardModel.getFeatureRepositories());

        installNewFeatureJob.schedule();
        // listen to the job end so that we can ask the user to restart the Studio
        installNewFeatureJob.addJobChangeListener(new JobChangeAdapter() {

            @Override
            public void done(IJobChangeEvent jobEvent) {
                MultiStatus results = (MultiStatus) jobEvent.getResult();
                IStatus[] installStatus = results.getChildren();
                boolean hasAnyFailure = false;
                boolean hasAnySuccess = false;
                boolean hasCancel = false;
                for (IStatus status : installStatus) {
                    if (!status.isOK()) {// ask the user to restart the Studio
                        if (status.getSeverity() == IStatus.CANCEL) {
                            hasCancel = true;
                        } else {
                            hasAnyFailure = true;
                        }
                    } else {
                        hasAnySuccess = true;
                    }
                }
                // if cancel,should do nothing,not display any pop message
                if (hasCancel) {
                    return;
                }
                // display message in case of any success
                String firstPartOfMessage = Messages.getString("UpdateStudioWizard.all.feautures.installed.successfully"); //$NON-NLS-1$
                if (hasAnySuccess) {
                    if (hasAnyFailure) {
                        firstPartOfMessage = Messages.getString("UpdateStudioWizard.some.feautures.installed.sucessfully"); //$NON-NLS-1$
                    } // else only success to keep initial message
                    final String finalMessage = firstPartOfMessage
                            + Messages.getString("UpdateStudioWizard.do.you.want.to.restart"); //$NON-NLS-1$
                    Display.getDefault().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            IPreferenceStore store = PlatformUI.getPreferenceStore();
                            // reset the last type of project set.
                            // this will force from the Application class to reset all the perspectives
                            store.putValue("last_started_project_type", "NO_TYPE");
                            if (needRestart()) {
                                boolean isOkToRestart = MessageDialog.openQuestion(getShell(),
                                        Messages.getString("UpdateStudioWizard.install.sucessfull"), finalMessage); //$NON-NLS-1$
                                if (isOkToRestart) {
                                    EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(EclipseCommandLine.CLEAN, null,
                                            false);
                                    PlatformUI.getWorkbench().restart();
                                } else {
                                    store.setValue(PreferenceKeys.NEED_OSGI_CLEAN, true); // will do clean for next
                                                                                          // time.
                                }
                            }
//                            if (needReloadComponents()) {
//                                ICodeGeneratorService service = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
//                                        ICodeGeneratorService.class);
//                                service.refreshTemplates();
//                            }
                        }
                    });
                } // else only failure or canceled so do nothing cause error are reported by Eclipse

            }
        });
        return true;
    }

    @Override
    public boolean performCancel() {
        // fix for TUP-2426
        if (doNotShowAgainCB.getSelection()) {
            storeDoNotShowAgainPref();
        }
        return super.performCancel();
    }

    /**
     * DOC sgandon Comment method "storeDoNotShowAgainPref".
     */
    private void storeDoNotShowAgainPref() {
        ConfigurationScope configurationScope = new ConfigurationScope();
        IEclipsePreferences updatesNode = configurationScope.getNode(ORG_TALEND_UPDATES_PREF_NODE);
        updatesNode.putBoolean(DO_NOT_SHOW_WIZARD_KEY, doNotShowAgainCB.getSelection());
        try {
            updatesNode.flush();
        } catch (BackingStoreException e) {
            log.error("could not store the \"Do No Show this again\" pref of the Install Dialog", e); //$NON-NLS-1$
        }
    }

    void updateCbStateFromPref() {
        ConfigurationScope configurationScope = new ConfigurationScope();
        IEclipsePreferences updatesNode = configurationScope.getNode(ORG_TALEND_UPDATES_PREF_NODE);
        doNotShowAgainCB.setSelection(updatesNode != null ? updatesNode.getBoolean(DO_NOT_SHOW_WIZARD_KEY, false) : false);
    }

    public void show(final Shell shell) {
        WizardDialog wizardDialog = new UpdateStudioWizardDialog(this, shell);
        wizardDialog.setHelpAvailable(false);
        wizardDialog.open();
    }

    /**
     * called right after the dialog is display to launch any initial runnable if no extra feature is set in the model
     * then launch a thread to get them
     * 
     * @param updateStudioWizardDialog
     */
    public void launchInitialRunnable(final UpdateStudioWizardDialog updateStudioWizardDialog) {
        if (updateWizardModel.availableExtraFeatures.isEmpty()) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    try {
                        updateStudioWizardDialog.run(true, true, new IRunnableWithProgress() {

                            @SuppressWarnings("unchecked")
                            @Override
                            public void run(IProgressMonitor iprogressmonitor)
                                    throws InvocationTargetException, InterruptedException {
                                ExtraFeaturesUpdatesFactory extraFeaturesFactory = new ExtraFeaturesUpdatesFactory();
                                extraFeaturesFactory.retrieveUninstalledExtraFeatures(iprogressmonitor,
                                        updateWizardModel.availableExtraFeatures, false);

                            }
                        });
                        // at the end of the runnable, the wizard restore the button states of the start of the wizard,
                        // but the state should be updated cause some
                        // items may already be selected, this forces an upadate of the wizard page state.
                        selectExtraFeaturesToInstallWizardPage.dbc.updateTargets();
                    } catch (InvocationTargetException e) {
                        // an error occured when fetching the modules, so report it to the user.
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        // the thread was interupted
                        ExceptionHandler.process(e);
                    }
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#needsProgressMonitor()
     */
    @Override
    public boolean needsProgressMonitor() {
        return true;
    }

}
