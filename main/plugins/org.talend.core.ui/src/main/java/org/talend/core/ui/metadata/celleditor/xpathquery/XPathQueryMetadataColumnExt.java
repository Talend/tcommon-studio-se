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
package org.talend.core.ui.metadata.celleditor.xpathquery;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataColumn;

/**
 * cli class global comment. Detailled comment
 */
public class XPathQueryMetadataColumnExt extends MetadataColumn {

    private String xpathQuery;

    public XPathQueryMetadataColumnExt(IMetadataColumn metadataColumn) {
        super(metadataColumn);
    }

    public String getXpathQuery() {
        return this.xpathQuery;
    }

    public void setXpathQuery(String xpathQuery) {
        this.xpathQuery = xpathQuery;
    }

    @Override
    public IMetadataColumn clone(boolean withCustoms) {
        XPathQueryMetadataColumnExt newCol = new XPathQueryMetadataColumnExt(super.clone(withCustoms));
        newCol.setXpathQuery(this.getXpathQuery());
        return newCol;
    }

}
