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
package org.talend.core.model.metadata.builder.database;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataConnection;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;
import orgomg.cwm.resource.relational.Table;
import orgomg.cwm.resource.relational.View;

/**
 * wzhang class global comment. Detailled comment
 */
public class TableNode {

    private String value;

    private List<TableNode> children;

    private TableNode parent;

    private int type;

    private String itemType;

    private Catalog catalog;

    private Schema schema;

    private Table table;

    private View view;

    private IMetadataConnection metadataConn;

    private TableInfoParameters paras;

    private List<Object> columnDataList;

    public static final int CATALOG = 1;

    public static final int SCHEMA = 2;

    public static final int TABLE = 3;

    public static final int COLUMN_FAMILY = 4;

    public static final int COLUMN = 5;

    public TableNode() {
        children = new ArrayList<TableNode>();
        columnDataList = new ArrayList<Object>();
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<TableNode> getChildren() {
        return this.children;
    }

    public void addChild(TableNode child) {
        this.children.add(child);
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TableNode getParent() {
        return this.parent;
    }

    public void setParent(TableNode parent) {
        this.parent = parent;
    }

    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Schema getSchema() {
        return this.schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public View getView() {
        return this.view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public IMetadataConnection getMetadataConn() {
        return this.metadataConn;
    }

    public void setMetadataConn(IMetadataConnection metadataConn) {
        this.metadataConn = metadataConn;
    }

    public TableInfoParameters getParas() {
        return this.paras;
    }

    public void setParas(TableInfoParameters paras) {
        this.paras = paras;
    }

    public List<Object> getColumnDataList() {
        return this.columnDataList;
    }

}
