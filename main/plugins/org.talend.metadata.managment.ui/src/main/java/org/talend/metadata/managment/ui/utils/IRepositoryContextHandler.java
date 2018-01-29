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
package org.talend.metadata.managment.ui.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.model.IConnParamName;

/**
 * created by ldong on Dec 18, 2014 Detailled comment
 * 
 */
public interface IRepositoryContextHandler {

    public boolean isRepositoryConType(Connection connection);

    public List<IContextParameter> createContextParameters(String prefixName, Connection connection, Set<IConnParamName> paramSet);

    public void setPropertiesForContextMode(String prefixName, Connection connection, Set<IConnParamName> paramSet);

    public void setPropertiesForExistContextMode(Connection connection, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> adaptMap);

    public void revertPropertiesForContextMode(Connection connection, ContextType contextType);

    public Set<String> getConAdditionPropertiesForContextMode(Connection conn);
}
