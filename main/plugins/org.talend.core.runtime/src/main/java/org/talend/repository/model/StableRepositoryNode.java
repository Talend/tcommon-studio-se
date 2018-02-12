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
package org.talend.repository.model;

import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class StableRepositoryNode extends RepositoryNode {

    private IImage icon;

    private String label;

    private ERepositoryObjectType childrenObjectType;

    /**
     * DOC smallet StableRepositoryNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public StableRepositoryNode(RepositoryNode parent, String label, IImage icon) {
        super(null, parent, ENodeType.STABLE_SYSTEM_FOLDER);
        this.label = label;
        this.icon = icon;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    @Override
    public IImage getIcon() {
        return this.icon;
    }

    /**
     * Sets the icon.
     * 
     * @param icon the icon to set
     */
    @Override
    public void setIcon(IImage icon) {
        this.icon = icon;
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    @Override
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return getType() + "-" + getLabel(); //$NON-NLS-1$
    }

    @Override
    public void dispose() {
        this.icon = null;
        this.label = null;
        super.dispose();
    }

    /**
     * Sets the childrenObjectType.
     * 
     * @param childrenObjectType the childrenObjectType to set
     */
    public void setChildrenObjectType(ERepositoryObjectType childrenObjectType) {
        this.childrenObjectType = childrenObjectType;
    }

    /**
     * Getter for childrenObjectType.
     * 
     * @return the childrenObjectType
     */
    public ERepositoryObjectType getChildrenObjectType() {
        return this.childrenObjectType;
    }

}
