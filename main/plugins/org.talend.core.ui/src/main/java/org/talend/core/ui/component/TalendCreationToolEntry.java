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
package org.talend.core.ui.component;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class TalendCreationToolEntry extends CreationToolEntry {

    private ERepositoryObjectType type;

    /**
     * DOC talend TalendCreationToolEntry constructor comment.
     * 
     * @param label
     * @param shortDesc
     * @param factory
     * @param iconSmall
     * @param iconLarge
     */
    public TalendCreationToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall,
            ImageDescriptor iconLarge, ERepositoryObjectType type) {
        super(label, shortDesc, factory, iconSmall, iconLarge);
        this.type = type;
    }

    public ERepositoryObjectType getRepositoryObjectType() {
        return this.type;
    }

    public CreationFactory getFactory() {
        return this.factory;
    }
}
