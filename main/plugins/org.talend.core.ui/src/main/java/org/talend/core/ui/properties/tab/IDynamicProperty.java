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
package org.talend.core.ui.properties.tab;

import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.IMultiPageTalendEditor;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public interface IDynamicProperty {

    public BidiMap getHashCurControls();

    public Element getElement();

    public IMultiPageTalendEditor getPart();

    public EComponentCategory getSection();

    public Composite getComposite();

    // public Map<String, IMetadataTable> getRepositoryTableMap();

    public void setCurRowSize(int i);

    public int getCurRowSize();

    /* 16969 */
    // public Map<String, ConnectionItem> getRepositoryConnectionItemMap();

    // public Map<String, Query> getRepositoryQueryStoreMap();

    public Map<String, String> getTableIdAndDbTypeMap();

    public Map<String, String> getTableIdAndDbSchemaMap();

    public void refresh();

    public String getRepositoryAliasName(ConnectionItem connectionItem);

}
