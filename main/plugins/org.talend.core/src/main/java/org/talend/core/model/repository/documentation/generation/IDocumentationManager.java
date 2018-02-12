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
package org.talend.core.model.repository.documentation.generation;

import java.util.List;

import org.talend.core.repository.document.IDocumentationGenerator;
import org.talend.repository.documentation.ExportFileResource;

/**
 * DOC tang class global comment. Detailled comment
 */
public interface IDocumentationManager {

    public List<ExportFileResource> getExportResources(ExportFileResource[] process, String targetPath,
            String... jobVersion) throws Exception;

    public List<ExportFileResource> getExportResources(ExportFileResource[] process);

    public IDocumentationGenerator getDocGenerator();

    public boolean isNeedGenerate();

}
