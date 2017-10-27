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
package org.talend.core.repository.model.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.talend.core.model.general.Project;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.ProjectManager;
import org.talend.repository.documentation.ERepositoryActionName;

/**
 * DOC talend class global comment. Detailled comment
 */
public abstract class AbstractJobDeleteListener implements PropertyChangeListener {

    public static final int DELETE_LOGICAL = 0;

    public static final int DELETE_PHISICAL = 1;

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        boolean leleteLogical = event.getPropertyName().equals(ERepositoryActionName.DELETE_TO_RECYCLE_BIN.getName());
        boolean deletePhisical = event.getPropertyName().equals(ERepositoryActionName.DELETE_FOREVER.getName());

        if (!leleteLogical && !deletePhisical) {
            return;
        }

        if (!(event.getNewValue() instanceof IRepositoryObject)) {
            return;
        }

        IRepositoryObject object = (IRepositoryObject) event.getNewValue();
        int type = DELETE_LOGICAL;
        if (deletePhisical) {
            type = DELETE_PHISICAL;
        }
        execute(object, type);
    }

    public abstract void execute(IRepositoryObject object, int deleteType);

    protected Project getProject(IRepositoryObject repositoryObject) {
        String projectLabel = repositoryObject.getProjectLabel();
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        if (currentProject.getTechnicalLabel().equalsIgnoreCase(projectLabel)) {
            return currentProject;
        }
        {
            /**
             * Attention!!<br>
             * Can't use the workspace version, since reference project may be different branch with master
             */
            // Project project = ProjectManager.getInstance().getProjectFromProjectLabel(projectLabel);
        }
        org.talend.core.model.properties.Project emfProject = ProjectManager.getInstance()
                .getProject(repositoryObject.getProperty());
        Project project = new Project(emfProject);
        return project;
    }
}
