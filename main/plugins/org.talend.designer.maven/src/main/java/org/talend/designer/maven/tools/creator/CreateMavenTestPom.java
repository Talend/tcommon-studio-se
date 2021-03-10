// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
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

    @Override
    protected ProcessType getProcessType() {
        try {
            Item parentJobItem = ITestContainerProviderService.get().getParentJobItem(getJobProcessor().getProperty().getItem());
            return ((ProcessItem) parentJobItem).getProcess();
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

}
