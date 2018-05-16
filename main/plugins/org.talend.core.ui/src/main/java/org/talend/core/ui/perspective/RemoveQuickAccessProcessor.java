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
package org.talend.core.ui.perspective;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution;


/**
 * created by nrousseau on Aug 18, 2015
 * Detailled comment
 *
 */
public class RemoveQuickAccessProcessor {

    @Execute
    public void removeQuickAccess(final MApplication application) {
        final List<MTrimContribution> trimContributions = application.getTrimContributions();
        for (final MTrimContribution trimContribution : trimContributions) {
            if ("org.eclipse.ui.ide.application.trimcontribution.QuickAccess".equals(trimContribution.getElementId())) {
                trimContributions.remove(trimContribution);
                break;
            }
        }
    }
}
