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

import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.runtime.i18n.Messages;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class BinRepositoryNode extends StableRepositoryNode {

    /**
     * DOC smallet StableRepositoryNode constructor comment.
     * 
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
