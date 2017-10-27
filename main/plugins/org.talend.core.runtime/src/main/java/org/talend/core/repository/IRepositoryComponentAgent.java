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
package org.talend.core.repository;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * ggu class global comment. Detailled comment
 */
public interface IRepositoryComponentAgent {

    public String getInputComponent(Item item, ERepositoryObjectType type);

    public String getOutputComponent(Item item, ERepositoryObjectType type);

    public String getDefaultComponent(Item item, ERepositoryObjectType type);
}
