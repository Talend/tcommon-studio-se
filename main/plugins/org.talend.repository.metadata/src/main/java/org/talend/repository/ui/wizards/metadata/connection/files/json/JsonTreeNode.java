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
package org.talend.repository.ui.wizards.metadata.connection.files.json;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.talend.datatools.xml.utils.ATreeNode;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * created by cmeng on Jul 1, 2015 Detailled comment
 *
 */
public class JsonTreeNode extends ATreeNode {

    private String jsonPath;

    private boolean retrievedFlag = false;

    private Map<String, JsonTreeNode> valueMap = new HashMap<String, JsonTreeNode>();

    private Set<JsonNode> values = new HashSet<JsonNode>();

    public String getJsonPath() {
        return this.jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public void putValueMap(String name, JsonTreeNode jsonNode) {
        valueMap.put(name, jsonNode);
    }

    public JsonTreeNode getFromValueMap(String name) {
        if (valueMap == null) {
            return null;
        } else {
            return valueMap.get(name);
        }
    }

    public boolean hasChildren() {
        if (!retrievedFlag) {
            SchemaPopulationUtil.fetchTreeNode(this, 1);
        }
        Object[] children = super.getChildren();
        if (children != null && 0 < children.length) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Object[] getChildren() {
        Object[] children = super.getChildren();
        if (children == null || children.length <= 0) {
            SchemaPopulationUtil.fetchTreeNode(this, 1);
        }
        return super.getChildren();
    }

    public Set<JsonNode> getValues() {
        return this.values;
    }

    public void addValue(JsonNode value) {
        this.values.add(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = (this.jsonPath == null) ? 0 : this.jsonPath.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JsonTreeNode other = (JsonTreeNode) obj;
        if (this.jsonPath == null) {
            if (other.jsonPath != null) {
                return false;
            }
        } else if (!this.jsonPath.equals(other.jsonPath)) {
            return false;
        }
        return true;
    }

    public void setRetrieved() {
        this.retrievedFlag = true;
    }
}
