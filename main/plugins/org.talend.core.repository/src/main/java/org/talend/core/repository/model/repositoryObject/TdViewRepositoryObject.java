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
package org.talend.core.repository.model.repositoryObject;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.relational.TdView;
import org.talend.repository.model.IRepositoryNode;


/**
 * DOC klliu  class global comment. Detailled comment
 */
public class TdViewRepositoryObject extends MetadataTableRepositoryObject {

    private IRepositoryNode repositoryNode;

    private String id;

    private String label;

    private String tableName;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * DOC klliu TDQMetadataViewRepositoryObject constructor comment.
     */
    public TdViewRepositoryObject(IRepositoryViewObject repositoryViewObject, TdView view) {
        super(repositoryViewObject, view);
    }

    public TdView getTdView() {
        return (TdView) super.table;
    }

    public IRepositoryViewObject getViewObject() {
        return super.viewObject;
    }

    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.METADATA_CON_VIEW;
    }

    public void setRepositoryNode(IRepositoryNode node) {
        this.repositoryNode = node;
    }

    public IRepositoryNode getRepositoryNode() {
        return this.repositoryNode;
    }

}
