// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.ui.setting.project.page;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.projectsetting.AbstractScriptProjectSettingPage;
import org.talend.designer.maven.tools.AggregatorPomsHelper;
import org.talend.designer.maven.ui.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractPersistentProjectSettingPage extends AbstractScriptProjectSettingPage {

    private static boolean isUserIdentified;

    public AbstractPersistentProjectSettingPage() {
        super();
        if (isUserIdentified) {
            isUserIdentified = false;
        }
    }

    public void load() throws IOException {
        // nothing to do
    }

    public void save() throws IOException {
        // nothing to do
    }

    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        if (ok && getScriptTxt() != null && !getScriptTxt().isDisposed() && !isUserIdentified) {
            boolean generatePom = MessageDialog.openQuestion(getShell(), "Question", //$NON-NLS-1$
                    Messages.getString("AbstractPersistentProjectSettingPage.syncAllPoms")); //$NON-NLS-1$
            isUserIdentified = true;
            if (generatePom) {
                try {
                    save();
                    new AggregatorPomsHelper().syncAllPoms();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        return ok;
    }

}
