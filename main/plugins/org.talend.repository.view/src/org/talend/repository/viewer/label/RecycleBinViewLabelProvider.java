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
package org.talend.repository.viewer.label;

import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;

public class RecycleBinViewLabelProvider extends AbstractRepoViewLabelProvider {

    @Override
    public Image getImage(Object element) {
        if (isRecycleBin(element)) {
            RepositoryNode recyclebinNode = (RepositoryNode) element;
            if (recyclebinNode.getChildren().isEmpty()) {
                return ImageProvider.getImage(ECoreImage.RECYCLE_BIN_EMPTY_ICON);
            } else {
                return ImageProvider.getImage(ECoreImage.RECYCLE_BIN_FULL_ICON);
            }
        }
        return null; // if others, ignore
    }

    private boolean isRecycleBin(Object element) {
        if (element instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) element;
            return node.isBin();
        }
        return false;

    }

    @Override
    public String getText(Object element) {
        if (isRecycleBin(element)) {
            return ERepositoryObjectType.RECYCLE_BIN.toString();
        }
        return null; // if others, ignore
    }

}
