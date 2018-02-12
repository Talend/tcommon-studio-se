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
package org.talend.core.model.metadata.query.generator;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.metadata.query.AbstractQueryGenerator;

/**
 * ggu class global comment. Detailled comment
 */
public class OldDelegateQueryGenerator extends AbstractQueryGenerator {

    public OldDelegateQueryGenerator(EDatabaseTypeName dbType) {
        super(dbType);
    }

    @Override
    public String generateQueryDelegate() {
        return QueryUtil.generateNewQueryDelegate(getElement(), getMetadataTable(), getDBType().getDisplayName(), getSchema(),
                getRealTableName());
    }
}
