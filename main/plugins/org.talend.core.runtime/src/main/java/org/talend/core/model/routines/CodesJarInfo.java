// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.routines;

import org.talend.core.model.properties.Property;
import org.talend.repository.ProjectManager;

/**
 * Could get most of codesjar's attribute in EMF model but some are lost when model is unloaded. Store those attributes
 * in this bean.
 */

public class CodesJarInfo {

    private Property property;

    private String projectTechName;

    private CodesJarInfo() {

    }

    public static CodesJarInfo create(Property property) {
        CodesJarInfo info = new CodesJarInfo();
        info.property = property;
        info.projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        return info;
    }

    public Property getProperty() {
        return property;
    }

    public String getProjectTechName() {
        return projectTechName;
    }

    public boolean isInCurrentMainProject() {
        return projectTechName.equals(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectTechName == null) ? 0 : projectTechName.hashCode());
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CodesJarInfo other = (CodesJarInfo) obj;
        if (projectTechName == null && other.projectTechName != null) {
            return false;
        } else if (!projectTechName.equals(other.projectTechName)) {
            return false;
        }
        if (property == null && other.property != null) {
            return false;
        } else if (property != null && other.property != null) {
            if (!property.getId().equals(other.property.getId()) || !property.getLabel().equals(other.property.getLabel())
                    || !property.getVersion().equals(other.property.getVersion())) {
                return false;
            }
        }
        return true;
    }

}
