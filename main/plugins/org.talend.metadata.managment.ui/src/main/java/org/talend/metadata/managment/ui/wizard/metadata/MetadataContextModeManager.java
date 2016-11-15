// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.metadata.managment.ui.wizard.metadata;

import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;

public class MetadataContextModeManager implements IMetadataContextModeManager {

    private ContextType type;

    public String getOriginalValue(String code) {
        if (code == null) {
            return ConnectionContextHelper.EMPTY;
        }
        return ConnectionContextHelper.getOriginalValue(getSelectedContextType(), code);
    }

    public ContextType getSelectedContextType() {
        return this.type;
    }

    public void setSelectedContextType(ContextType type) {
        this.type = type;

    }

    public void setDefaultContextType(Connection connection) {
        if (connection != null && connection.isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connection, true);
            setSelectedContextType(contextType);
            return;
        }
        setSelectedContextType(null);
    }

}
