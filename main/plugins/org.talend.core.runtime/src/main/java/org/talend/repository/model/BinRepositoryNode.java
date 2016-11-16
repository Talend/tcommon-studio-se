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
package org.talend.repository.model;

import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.runtime.i18n.Messages;

public class BinRepositoryNode extends StableRepositoryNode {

    /**
     * @param object
     * @param parent
     * @param type
     */
    public BinRepositoryNode(RepositoryNode parent) {
        super(parent, Messages.getString("BinRepositoryNode.label"), null); //$NON-NLS-1$
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public IImage getIcon() {
        if (hasChildren()) {
            return ECoreImage.RECYCLE_BIN_FULL_ICON;
        } else {
            return ECoreImage.RECYCLE_BIN_EMPTY_ICON;
        }
    }

    @Override
    public boolean isBin() {
        return true;
    }
}
