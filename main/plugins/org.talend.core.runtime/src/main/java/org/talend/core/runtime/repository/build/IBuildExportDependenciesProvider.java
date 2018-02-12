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
package org.talend.core.runtime.repository.build;

import org.talend.core.model.properties.Item;
import org.talend.repository.documentation.ExportFileResource;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IBuildExportDependenciesProvider {

    /**
     * called when exporting a job, this is up to the implementor to check that the item should export the dependencies
     */
    void exportDependencies(ExportFileResource exportResource, Item item);
}
