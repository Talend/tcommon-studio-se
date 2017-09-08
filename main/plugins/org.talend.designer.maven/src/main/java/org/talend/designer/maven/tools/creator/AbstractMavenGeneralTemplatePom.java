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
package org.talend.designer.maven.tools.creator;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.template.MavenTemplateManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class AbstractMavenGeneralTemplatePom extends CreateMavenBundleTemplatePom {

    public AbstractMavenGeneralTemplatePom(IFile pomFile, String bundleTemplateName) {
        super(pomFile, IProjectSettingTemplateConstants.PATH_GENERAL + '/' + bundleTemplateName);
    }

    protected InputStream getTemplateStream() throws IOException {
        try {
            return MavenTemplateManager.getBundleTemplateStream(DesignerMavenPlugin.PLUGIN_ID, getBundleTemplatePath());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
