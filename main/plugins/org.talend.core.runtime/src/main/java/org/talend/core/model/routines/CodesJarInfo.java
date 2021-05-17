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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * Could get most of codesjar's attribute in EMF model but some are lost when model is unloaded. Store those attributes
 * in this bean.
 */

public class CodesJarInfo {

    private static final String EMPTY_DATE;

    private String projectTechName;

    private String id;

    private String label;

    private String version;

    private ERepositoryObjectType type;

    private List<IMPORTType> imports;

    private String modifiedDate;

    static {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        EMPTY_DATE = ResourceHelper.dateFormat().format(c.getTime());
    }

    private CodesJarInfo() {
        imports = new ArrayList<>();
    }

    public static CodesJarInfo create(Property property) {
        Assert.isTrue(property.getItem() instanceof RoutinesJarItem);
        CodesJarInfo info = new CodesJarInfo();
        info.projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        info.id = property.getId();
        info.label = property.getLabel();
        info.version = property.getVersion();
        info.type = ERepositoryObjectType.getItemType(property.getItem());
        if (((RoutinesJarItem) property.getItem()).getRoutinesJarType() != null) {
            info.imports.addAll(((RoutinesJarItem) property.getItem()).getRoutinesJarType().getImports());
        }
        String modifiedDate = (String) property.getAdditionalProperties().get(ItemProductKeys.DATE.getModifiedKey());
        info.modifiedDate = StringUtils.isNotBlank(modifiedDate) ? modifiedDate : EMPTY_DATE;
        return info;
    }

    public Property getProperty() {
        try {
            IRepositoryViewObject obj = IProxyRepositoryService.get().getProxyRepositoryFactory().getLastVersion(id);
            if (obj != null) {
                return obj.getProperty();
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
     }

    public String getProjectTechName() {
        return projectTechName;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public ERepositoryObjectType getType() {
        return type;
    }

    public List<IMPORTType> getImports() {
        return imports;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public boolean isInCurrentMainProject() {
        return projectTechName.equals(ProjectManager.getInstance().getCurrentProject().getTechnicalLabel());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CodesJarInfo other = (CodesJarInfo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
