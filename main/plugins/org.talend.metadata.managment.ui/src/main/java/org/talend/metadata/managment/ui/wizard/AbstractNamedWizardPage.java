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
package org.talend.metadata.managment.ui.wizard;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.CoreRepositoryPlugin;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.KeywordsValidator;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

/**
 * created by nrousseau on Jun 24, 2016 Detailled comment
 *
 */
public abstract class AbstractNamedWizardPage extends WizardPage {

    protected IStatus nameStatus;

    protected List<IRepositoryViewObject> listExistingObjects;

    protected boolean retrieveNameFinished = false;

    protected boolean nameModifiedByUser = false;

    /**
     * DOC nrousseau AbstractNamedWizardPage constructor comment.
     * 
     * @param pageName
     */
    protected AbstractNamedWizardPage(String pageName) {
        super(pageName);
        nameStatus = createOkStatus();
    }

    protected static IStatus createOkStatus() {
        return new Status(IStatus.OK, CoreRepositoryPlugin.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
    }

    protected static IStatus createStatus(int severity, String message) {
        return new Status(severity, CoreRepositoryPlugin.PLUGIN_ID, IStatus.OK, message, null);
    }

    public abstract ERepositoryObjectType getRepositoryObjectType();

    protected void evaluateName(String name) {
        // TUP-4619 check more about '\t' name issue here and avoid NPE error
        if (name == null || name.length() == 0 || StringUtils.trimToNull(name) == null) {
            nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.NameEmptyError")); //$NON-NLS-1$
        } else if (name.startsWith(" ") //$NON-NLS-1$
                || name.startsWith("\t") //$NON-NLS-1$
                || !Pattern.matches(RepositoryConstants.getPattern(getRepositoryObjectType()), name) || name.trim().contains(" ") //$NON-NLS-1$
                || name.trim().contains("\t")) { // $NON-NLS-2$
            nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.NameFormatError")); //$NON-NLS-1$
        } else if (isKeywords(name) || "java".equalsIgnoreCase(name)) {//$NON-NLS-1$
            nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.KeywordsError")); //$NON-NLS-1$
        } else if (name.equalsIgnoreCase(ProjectManager.getInstance().getCurrentProject().getLabel())) {
            nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.SameAsProjectname"));//$NON-NLS-1$
        } else if (nameModifiedByUser) {
            if (retrieveNameFinished) {
                if (!isValid(name)) {
                    nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.ItemExistsError")); //$NON-NLS-1$
                } else {
                    nameStatus = createOkStatus();
                }
            } else {
                nameStatus = createStatus(IStatus.ERROR, "Looking for current items name list"); //$NON-NLS-1$
            }
        } else {
            nameStatus = createOkStatus();
        }
        if (getProperty() != null && nameStatus.getSeverity() == IStatus.OK) {
            getProperty().setLabel(getPropertyLabel(StringUtils.trimToNull(name)));
            getProperty().setDisplayName(StringUtils.trimToNull(name));
        }
        updatePageStatus();
    }

    protected String getPropertyLabel(String name) {
        return name;
    }

    public abstract Property getProperty();

    protected IStatus[] getStatuses() {
        return new IStatus[] { nameStatus };
    };

    protected void updatePageComplete() {
        setMessage(findMostSevere());
        setPageComplete(findMostSevere().getSeverity() != IStatus.ERROR);
    }

    protected IStatus findMostSevere() {
        IStatus[] statuses = getStatuses();
        IStatus severeStatus = statuses[0];
        for (IStatus status : statuses) {
            if (status.getSeverity() > severeStatus.getSeverity()) {
                severeStatus = status;
            }
        }
        return severeStatus;
    }

    protected void updatePageStatus() {
        setMessage(findMostSevere());
        updatePageComplete();
    }

    protected void setMessage(IStatus status) {
        if (IStatus.ERROR == status.getSeverity()) {
            setErrorMessage(status.getMessage());
            // setMessage(""); //$NON-NLS-1$
        } else {
            if (StringUtils.isNotEmpty(status.getMessage())) {
                setMessage(status.getMessage(), status.getSeverity());
            } else {
                setMessage(getDescription());
            }
            setErrorMessage(null);
        }
    }

    /**
     * 
     * DOC ggu Comment method "isKeywords".
     * 
     * @param itemName
     * @return
     */
    protected boolean isKeywords(String itemName) {
        if (getProperty() != null) {
            Item item = getProperty().getItem();
            // see bug 0004157: Using specific name for (main) tream
            if (item instanceof ProcessItem || item instanceof JobletProcessItem || item instanceof RoutineItem) {
                return KeywordsValidator.isKeyword(itemName);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean isValid(String itemName) {

        IProxyRepositoryFactory repositoryFactory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            return repositoryFactory.isNameAvailable(getProperty().getItem(), getPropertyLabel(itemName), listExistingObjects);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

}
