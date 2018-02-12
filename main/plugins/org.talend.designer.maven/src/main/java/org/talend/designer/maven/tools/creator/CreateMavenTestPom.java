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
package org.talend.designer.maven.tools.creator;

import org.eclipse.core.resources.IFile;
import org.talend.designer.runprocess.IProcessor;

/**
 * created by ggu on 4 Feb 2015 Detailled comment
 *
 */
public class CreateMavenTestPom extends CreateMavenJobPom {

    public CreateMavenTestPom(IProcessor jobProcessor, IFile pomFile) {
        super(jobProcessor, pomFile);
    }
    
    @Deprecated
    public CreateMavenTestPom(IProcessor jobProcessor, IFile pomFile, String pomTestRouteTemplateFileName) {
        super(jobProcessor, pomFile);
    }

}
