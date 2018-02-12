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
package org.talend.designer.maven.ui.setting.repository.node;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.IPreferencePage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.designer.maven.ui.setting.project.page.AbstractPersistentProjectSettingPage;
import org.talend.designer.maven.ui.utils.DesignerMavenUiHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryMavenSettingNode extends RepositoryPreferenceNode {

    private IFile file;

    public RepositoryMavenSettingNode(String id, IFile file) {
        super(id, DesignerMavenUiHelper.displayWithExtension ? file.getName() : file.getProjectRelativePath()
                .removeFileExtension().lastSegment(), ImageProvider.getImageDesc(EImage.KEY_ICON), null);
        this.file = file;
    }

    public IFile getFile() {
        return file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.maven.ui.dialog.RepositoryPreferenceNode#createPage()
     */
    @Override
    public void createPage() {
        super.createPage();
        IPreferencePage page = this.getPage();
        if (page instanceof AbstractPersistentProjectSettingPage) {
            AbstractPersistentProjectSettingPage prefPage = (AbstractPersistentProjectSettingPage) page;
            prefPage.setHeaderMessage("Path: " + this.getFile().getProjectRelativePath());

            try {
                prefPage.load();
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
    }

}
