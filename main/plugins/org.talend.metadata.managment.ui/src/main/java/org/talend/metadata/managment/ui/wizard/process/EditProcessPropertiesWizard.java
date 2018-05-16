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
package org.talend.metadata.managment.ui.wizard.process;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.PluginChecker;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.designer.core.convert.ProcessConvertManager;
import org.talend.designer.core.convert.ProcessConverterType;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.wizard.PropertiesWizard;

/**
 * Created by Marvin Wang on Feb 18, 2013.
 */
public class EditProcessPropertiesWizard extends PropertiesWizard {

    private IRepositoryViewObject repositoryViewObject;

    private EditProcessPropertiesWizardPage mainPage;

    /**
     * DOC marvin EditProcessPropertiesWizard constructor comment.
     * 
     * @param repositoryViewObject
     * @param path
     * @param useLastVersion
     */
    public EditProcessPropertiesWizard(IRepositoryViewObject repositoryViewObject, IPath path, boolean useLastVersion) {
        super(repositoryViewObject, path, useLastVersion);
        this.repositoryViewObject = repositoryViewObject;
        setWindowTitle(Messages.getString("EditProcessPropertiesWizard.wizardTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROCESS_WIZ));
    }

    @Override
    public void addPages() {
        mainPage = new EditProcessPropertiesWizardPage(Messages.getString("EditProcessPropertiesWizard.pageName"), //$NON-NLS-1$
                object.getProperty(), path, isReadOnly(), false, lastVersionFound);
        mainPage.setItem(object.getProperty().getItem());
        mainPage.setConverter(ProcessConvertManager.getInstance().extractConvertService(
                ProcessConverterType.CONVERTER_FOR_MAPREDUCE));
        addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
        if (alreadyEditedByUser) {
            return false;
        }
        if (object != null && object.getProperty() != null && object.getProperty().getItem() != null
                && object.getProperty().getItem() instanceof ProcessItem && PluginChecker.isTIS()
                && mainPage.jobTypeCCombo != null) {
            Item item = object.getProperty().getItem();
            Object frameworkObj = ConvertJobsUtil.getFramework(item);
            String sourceFramework = null;
            if (frameworkObj != null) {
                sourceFramework = frameworkObj.toString();
            }
            String sourceJobType = ConvertJobsUtil.getJobTypeFromFramework(object.getProperty().getItem());
            boolean isNeedConvert = ConvertJobsUtil.isNeedConvert(item, mainPage.jobTypeCCombo.getText(),
                    mainPage.framework.getText(), true);
            if (sourceJobType != null && !isNeedConvert) {
                return super.performFinish();
            }
//            boolean hasTestCase = ConvertJobsUtil.hasTestCase(object.getProperty());
//            if (hasTestCase
//                    && !MessageDialogWithToggle.openConfirm(null, "Warning",
//                            "Warning: You will lost all the testcases when you do converting, do you want to continue?")) {
//                return super.performFinish();
//            }
            // Convert
            boolean convert = true;
            try {
                ConvertJobsUtil
                        .convert(object.getLabel(), mainPage.jobTypeCCombo.getText(), mainPage.framework.getText(), object);
            } catch (Exception e) {
                MessageBoxExceptionHandler.process(e.getCause());
                convert = false;
            }
            return convert;

        } else {
            return super.performFinish();
        }
    }

    /**
     * Getter for repositoryViewObject.
     * 
     * @return the repositoryViewObject
     */
    public IRepositoryViewObject getRepositoryViewObject() {
        return this.repositoryViewObject;
    }

    /**
     * Sets the repositoryViewObject.
     * 
     * @param repositoryViewObject the repositoryViewObject to set
     */
    public void setRepositoryViewObject(IRepositoryViewObject repositoryViewObject) {
        this.repositoryViewObject = repositoryViewObject;
    }

}
