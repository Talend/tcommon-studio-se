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
package org.talend.core.repository.document;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * created by wchen on 2015年8月14日 Detailled comment
 *
 */
public interface IGenerateAllDocumentation {

    public File generateAllDocListHTML();

    public boolean generateAllItemsDocWithProgressMonitor(IProgressMonitor monitor) throws InterruptedException;

    public void setProject(Project project);

    public void setRootNode(RepositoryNode jobletNode);

    public void setGenerateVersions(boolean generateAllVersions);

    public boolean isGenerateAllVersions();

    public File getGeneratedDocHTML();

    public ERepositoryObjectType getDocumentObjectType();

    public ERepositoryObjectType getItemType();

    public String getHTMLFileName();

    public IDocumentationGenerator getHTMLGenerator();

}
