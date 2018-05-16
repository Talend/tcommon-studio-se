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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.model.FeatureRepositories;
import org.talend.updates.runtime.model.UpdateSiteLocationType;

/**
 * created by sgandon on 26 févr. 2013 Detailled comment
 * 
 */
public class UpdateWizardModel {

    /**
     * created by sgandon on 25 avr. 2013 Detailled comment
     * 
     */
    class FeatureSelectionValidator extends MultiValidator {

        @Override
        protected IStatus validate() {
            boolean hasFeatureSelected = false;
            for (ExtraFeature feature : (Set<ExtraFeature>) selectedExtraFeatures) {
                if (!(feature instanceof FeatureCategory)) {
                    hasFeatureSelected = true;
                    break;
                }
            }
            if (!hasFeatureSelected) {
                return ValidationStatus.error(Messages
                        .getString("SelectExtraFeaturesToInstallWizardPage.one.feature.must.be.selected")); //$NON-NLS-1$
            } else if (!selectedExtraFeatures.isEmpty() && !canConfigureUpdateSiteLocation()) {
                @SuppressWarnings("unchecked")
                ExtraFeature extraFeature = getFirstExtraFeatureNotAllowingUpdateSiteConfig(selectedExtraFeatures);
                return ValidationStatus.warning(Messages.getString("no.dowload.location.to.configure", extraFeature.getName())); //$NON-NLS-1$
            }
            return ValidationStatus.ok();
        }
    }

    class RemoteRepoURIValidator implements IValidator {

        @Override
        public IStatus validate(Object value) {
            String uriStr = (String) value;
            if (uriStr == null) {
                return ValidationStatus.info(Messages.getString("UpdateWizardModel.url.missing.error")); //$NON-NLS-1$
            }
            try {
                new URL(uriStr);
            } catch (MalformedURLException e) {
                return ValidationStatus.error(Messages.getString("UpdateWizardModel.url.invalid.error")); //$NON-NLS-1$
            }
            return ValidationStatus.ok();
        }
    }

    class LocalRepoFolderValidator implements IValidator {

        @Override
        public IStatus validate(Object value) {
            String uriStr = (String) value;
            if (uriStr == null || "".equals(uriStr)) { //$NON-NLS-1$
                return ValidationStatus.error(Messages.getString("UpdateWizardModel.local.folder.required.error")); //$NON-NLS-1$
            }
            File folder = new File(uriStr);
            if (!folder.exists()) {
                return ValidationStatus.error(Messages.getString("UpdateWizardModel.local.folder.must.exist.error")); //$NON-NLS-1$
            }
            if (!folder.isDirectory()) {
                return ValidationStatus.error(Messages.getString("UpdateWizardModel.local.folder.must.be.folder")); //$NON-NLS-1$
            }
            return ValidationStatus.ok();
        }
    }

    IObservableSet availableExtraFeatures;

    IObservableSet selectedExtraFeatures = new WritableSet();

    Set<ExtraFeature> selectedExtraFeatureBeanSet;

    boolean hasDoNotShowThisAgainChanged = false;

    private FeatureRepositories featureRepositories;

    /**
     * DOC sgandon UpdateWizardModel constructor comment.
     * 
     * @param extraFeatures
     */
    public UpdateWizardModel(Set<ExtraFeature> extraFeatures) {
        // create an observable set of the feature that may be installed
        availableExtraFeatures = extraFeatures != null ? new WritableSet(extraFeatures, ExtraFeature.class) : new WritableSet();
        ;
        this.featureRepositories = new FeatureRepositories();
    }

    public Set<ExtraFeature> getSelectedExtraFeatures() {
        Set<ExtraFeature> features = new LinkedHashSet<ExtraFeature>();
        for (Object feature : selectedExtraFeatures) {
            if (feature instanceof ExtraFeature && !(feature instanceof FeatureCategory)) {
                features.add((ExtraFeature) feature);
            }
        }
        return features;
    }

    /**
     * Getter for selectedExtraFeatureBeanSet.
     * 
     * @return the selectedExtraFeatureBeanSet
     */
    public Set<ExtraFeature> getSelectedExtraFeatureBeanSet() {
        return this.selectedExtraFeatureBeanSet;
    }

    /**
     * Sets the selectedExtraFeatureBeanSet.
     * 
     * @param selectedExtraFeatureBeanSet the selectedExtraFeatureBeanSet to set
     */
    public void setSelectedExtraFeatureBeanSet(Set<ExtraFeature> selectedExtraFeatureBeanSet) {
        this.selectedExtraFeatureBeanSet = selectedExtraFeatureBeanSet;
    }

    /**
     * Getter for featureRepositories.
     * 
     * @return the featureRepositories
     */
    public FeatureRepositories getFeatureRepositories() {
        return this.featureRepositories;
    }

    /**
     * Sets the featureRepositories.
     * 
     * @param featureRepositories the featureRepositories to set
     */
    public void setFeatureRepositories(FeatureRepositories featureRepositories) {
        this.featureRepositories = featureRepositories;
    }

    /**
     * DOC sgandon Comment method "needToSelectUpdateSite".
     * 
     * @return true is all of the selected feature can have a customisable remote update site, and false otherwise
     */
    public boolean canConfigureUpdateSiteLocation() {
        @SuppressWarnings("unchecked")
        ExtraFeature firstExtraFeatureNotAllowingUpdateSiteConfig = getFirstExtraFeatureNotAllowingUpdateSiteConfig(selectedExtraFeatures);
        return firstExtraFeatureNotAllowingUpdateSiteConfig == null;
    }

    /**
     * @param selectedExtraFeatures, list of features to search for.
     * @return the first feature that does not allow to configure the update site or null if none found in the list
     */
    private ExtraFeature getFirstExtraFeatureNotAllowingUpdateSiteConfig(Set<ExtraFeature> extraFeatures) {
        for (ExtraFeature ef : extraFeatures) {
            EnumSet<UpdateSiteLocationType> updateSiteCompatibleTypes = ef.getUpdateSiteCompatibleTypes();
            if (updateSiteCompatibleTypes != null && updateSiteCompatibleTypes.size() == 1
                    && updateSiteCompatibleTypes.contains(UpdateSiteLocationType.DEFAULT_REPO)) {
                return ef;
            }
        }
        return null;
    }
}
