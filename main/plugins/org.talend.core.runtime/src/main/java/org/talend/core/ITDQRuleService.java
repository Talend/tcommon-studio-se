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
package org.talend.core;

import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElementParameter;

/**
 * DOC msjian class global comment. Detailed comment
 */
public interface ITDQRuleService extends ITDQService {

    /**
     * This method is used in tos to fill the DQRULES_LIST parameter with rule list values.
     * 
     * @param param
     */
    public void fillTDQRuleList(IElementParameter param);

    /**
     * This method is used to generate query for a DQ rule + component params.
     * 
     * @param dbType (value = SupportDBUrlType.dbkey)
     * @param catalog
     * @param schema
     * @param table
     * @param metadataTable
     * @param ruleParam
     * @param isInvalidRowsComponent
     * @param whereClause
     */
    public String getQueryByRule(IElementParameter ruleParam, IElementParameter dbType, IElementParameter catalog,
            IElementParameter schema, IElementParameter table, IMetadataTable metadataTable, boolean isInvalidRowsComponent,
            IElementParameter whereClause);

    /**
     * Retrieve select columns in the query to update metadata if possible
     * 
     * @param columns
     * @param queryParam
     */
    public void updateOriginalColumnNames(List<IMetadataColumn> columns, IElementParameter queryParam);

}
