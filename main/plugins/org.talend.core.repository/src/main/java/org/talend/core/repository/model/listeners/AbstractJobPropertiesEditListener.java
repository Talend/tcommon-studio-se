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

import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.repository.documentation.ERepositoryActionName;

/**
 * DOC talend class global comment. Detailled comment
 */
public abstract class AbstractJobPropertiesEditListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (!event.getPropertyName().equals(ERepositoryActionName.PROPERTIES_CHANGE.getName())) {
            return;
        }

        if (!(event.getOldValue() instanceof String[])) {
            return;
        }
        if (!(event.getNewValue() instanceof Property)) {
            return;
        }
        String[] originalJobNameAndVersion = (String[]) event.getOldValue();
        Property jobProperty = (Property) event.getNewValue();
        if (jobProperty.getItem() instanceof ProcessItem || jobProperty.getItem() instanceof JobletProcessItem) {
            execute(jobProperty, originalJobNameAndVersion);
        }
    }

    public abstract void execute(Property property, String[] originalJobNameAndVersion);
}
