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
package org.talend.core.model.metadata;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * ggu class global comment. Detailled comment
 */
public interface IMetadataContextModeManager {

    public ContextType getSelectedContextType();

    public void setSelectedContextType(ContextType type);

    public String getOriginalValue(String code);

    public void setDefaultContextType(Connection connection);
}
