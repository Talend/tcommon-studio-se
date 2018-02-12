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
package org.talend.core.repository.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.i18n.Messages;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class ItemReferenceBean {

    public final static String EMPTY_STRING = ""; //$NON-NLS-1$

    public final static String SPACE = " "; //$NON-NLS-1$

    public final static ItemReferenceBean[] EMPTY_ARRAY = new ItemReferenceBean[0];

    private String itemName;

    private String referenceItemName;

    private String itemVersion;

    private boolean isItemDeleted = false;

    private String referenceItemVersion;

    private String referenceItemPath;

    private String referenceProjectName;

    private ERepositoryObjectType itemType;

    private ERepositoryObjectType referenceItemType;

    private boolean isReferenceItemDeleted = false;

    private boolean isHost = false;

    private int nodeNum = 1;

    private ItemReferenceBean parent;

    private List<ItemReferenceBean> children = new ArrayList<ItemReferenceBean>();

    public int getNodeNum() {
        return this.nodeNum;
    }

    public void addNodeNum() {
        this.nodeNum = this.nodeNum + 1;
    }

    public ItemReferenceBean getParent() {
        return this.parent;
    }

    public void setParent(ItemReferenceBean parent) {
        this.parent = parent;
    }

    public List<ItemReferenceBean> getChildren() {
        return this.children;
    }

    public void addChild(ItemReferenceBean child) {
        this.children.add(child);
    }

    public void addChildren(List<ItemReferenceBean> children) {
        this.children.addAll(children);
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getReferenceItemName() {
        return this.referenceItemName;
    }

    public void setReferenceItemName(String referenceItemName) {
        this.referenceItemName = referenceItemName;
    }

    public String getItemVersion() {
        return this.itemVersion;
    }

    public void setItemVersion(String itemVersion) {
        this.itemVersion = itemVersion;
    }

    public String getReferenceItemVersion() {
        return this.referenceItemVersion;
    }

    public void setReferenceItemVersion(String referenceItemVersion) {
        this.referenceItemVersion = referenceItemVersion;
    }

    public String getReferenceItemPath() {
        return this.referenceItemPath;
    }

    public void setReferenceItemPath(String referenceItemPath) {
        this.referenceItemPath = referenceItemPath;
    }

    public String getReferenceProjectName() {
        return this.referenceProjectName;
    }

    public void setReferenceProjectName(String referenceProjectName) {
        this.referenceProjectName = referenceProjectName;
    }

    public ERepositoryObjectType getItemType() {
        return this.itemType;
    }

    public void setItemType(ERepositoryObjectType itemType) {
        this.itemType = itemType;
    }

    public boolean isItemDeleted() {
        return this.isItemDeleted;
    }

    public void setItemDeleted(boolean isItemDeleted) {
        this.isItemDeleted = isItemDeleted;
    }

    public ERepositoryObjectType getReferenceItemType() {
        return this.referenceItemType;
    }

    public void setReferenceItemType(ERepositoryObjectType referenceItemType) {
        this.referenceItemType = referenceItemType;
    }

    public boolean isReferenceItemDeleted() {
        return this.isReferenceItemDeleted;
    }

    public void setReferenceItemDeleted(boolean isReferenceItemDeleted) {
        this.isReferenceItemDeleted = isReferenceItemDeleted;
    }

    public boolean isHost() {
        return this.isHost;
    }

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    public String getWholeItemName() {
        return itemName + SPACE + itemVersion;
    }

    public String getWholeRefItemName() {
        if (isReferenceItemDeleted) {
            return referenceItemName + SPACE + referenceItemVersion
                    + " (" + Messages.getString("ItemReferenceDialog.deletedInfor") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        } else {
            String prefix = EMPTY_STRING;
            if (StringUtils.isNotEmpty(referenceItemPath)) {
                prefix = referenceItemPath + File.separator;
            }
            return prefix + referenceItemName + SPACE + referenceItemVersion;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.itemName == null) ? 0 : this.itemName.hashCode());
        result = prime * result + ((this.itemType == null) ? 0 : this.itemType.hashCode());
        result = prime * result + ((this.itemVersion == null) ? 0 : this.itemVersion.hashCode());
        result = prime * result + ((this.referenceItemName == null) ? 0 : this.referenceItemName.hashCode());
        result = prime * result + ((this.referenceItemPath == null) ? 0 : this.referenceItemPath.hashCode());
        result = prime * result + ((this.referenceItemType == null) ? 0 : this.referenceItemType.hashCode());
        result = prime * result + ((this.referenceItemVersion == null) ? 0 : this.referenceItemVersion.hashCode());
        result = prime * result + ((this.referenceProjectName == null) ? 0 : this.referenceProjectName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemReferenceBean other = (ItemReferenceBean) obj;
        if (this.itemName == null) {
            if (other.itemName != null)
                return false;
        } else if (!this.itemName.equals(other.itemName))
            return false;
        if (this.itemType == null) {
            if (other.itemType != null)
                return false;
        } else if (!this.itemType.equals(other.itemType))
            return false;
        if (this.itemVersion == null) {
            if (other.itemVersion != null)
                return false;
        } else if (!this.itemVersion.equals(other.itemVersion))
            return false;
        if (this.referenceItemName == null) {
            if (other.referenceItemName != null)
                return false;
        } else if (!this.referenceItemName.equals(other.referenceItemName))
            return false;
        if (this.referenceItemPath == null) {
            if (other.referenceItemPath != null)
                return false;
        } else if (!this.referenceItemPath.equals(other.referenceItemPath))
            return false;
        if (this.referenceItemType == null) {
            if (other.referenceItemType != null)
                return false;
        } else if (!this.referenceItemType.equals(other.referenceItemType))
            return false;
        if (this.referenceItemVersion == null) {
            if (other.referenceItemVersion != null)
                return false;
        } else if (!this.referenceItemVersion.equals(other.referenceItemVersion))
            return false;
        if (this.referenceProjectName == null) {
            if (other.referenceProjectName != null)
                return false;
        } else if (!this.referenceProjectName.equals(other.referenceProjectName))
            return false;
        return true;
    }

}
