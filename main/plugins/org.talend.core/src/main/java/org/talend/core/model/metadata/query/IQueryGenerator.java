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
package org.talend.core.model.metadata.query;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElement;

/**
 * ggu class global comment. Detailled comment
 */
public interface IQueryGenerator {

    public static final String EMPTY = ""; //$NON-NLS-1$

    public static final String ENTER = "\n"; //$NON-NLS-1$

    public static final String SPACE = " "; //$NON-NLS-1$

    public static final String SQL_SPLIT_FIELD = ","; //$NON-NLS-1$

    public static final String SQL_SELECT = "SELECT"; //$NON-NLS-1$

    public static final String SQL_FROM = "FROM"; //$NON-NLS-1$

    public void setParameters(IElement element, IMetadataTable metadataTable, String schema, String realTableName);

    public void setParameters(IElement element, IMetadataTable metadataTable, String schema, String realTableName, boolean isJdbc);

    public abstract String generateQuery();
}
