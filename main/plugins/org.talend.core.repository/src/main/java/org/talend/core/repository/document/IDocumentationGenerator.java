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
package org.talend.core.repository.document;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.documentation.ExportFileResource;

/**
 * DOC tang class global comment. Detailled comment
 */
public interface IDocumentationGenerator {

    public void generateDocumentation(ExportFileResource resource, String targetPath, String... jobVersion) throws Exception;

    public void generateHTMLFile(ExportFileResource resource);

    public void generateHTMLFile(ExportFileResource resource, String cssFile);

    public ERepositoryObjectType getRepositoryObjectType();

    public ERepositoryObjectType getItemType();

}
