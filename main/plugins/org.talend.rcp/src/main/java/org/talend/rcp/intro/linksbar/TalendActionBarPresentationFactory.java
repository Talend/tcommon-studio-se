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
package org.talend.rcp.intro.linksbar;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.internal.provisional.action.ICoolBarManager2;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.jface.internal.provisional.action.IToolBarManager2;
import org.eclipse.jface.internal.provisional.action.ToolBarContributionItem2;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.provisional.presentations.IActionBarPresentationFactory;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackPresentation;
import org.talend.rcp.Activator;

/**
 * created by nrousseau on May 15, 2013 Detailled comment
 * 
 */
@Deprecated
public class TalendActionBarPresentationFactory extends AbstractPresentationFactory implements IActionBarPresentationFactory {

    public static final String COOLITEM_LINKS_ID = Activator.PLUGIN_ID + ".CoolItemLinks"; //$NON-NLS-1$

    @Override
    public ICoolBarManager2 createCoolBarManager() {

        return new TalendCoolBarManager(SWT.FLAT);
    }

    @Override
    public IToolBarManager2 createToolBarManager() {
        return new ToolBarManager2(SWT.FLAT | SWT.RIGHT);
    }

    @Override
    public IToolBarManager2 createViewToolBarManager() {
        return new ToolBarManager2(SWT.FLAT | SWT.RIGHT | SWT.WRAP);
    }

    @Override
    public IToolBarContributionItem createToolBarContributionItem(IToolBarManager toolBarManager, String id) {
        return new ToolBarContributionItem2(toolBarManager, id);
    }

    @Override
    public StackPresentation createEditorPresentation(Composite parent, IStackPresentationSite site) {
        return null;
    }

    @Override
    public StackPresentation createViewPresentation(Composite parent, IStackPresentationSite site) {
        return null;
    }

    @Override
    public StackPresentation createStandaloneViewPresentation(Composite parent, IStackPresentationSite site, boolean showTitle) {
        return null;
    }

}
