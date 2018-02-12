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
package org.talend.repository.ui.views;

import org.eclipse.jface.viewers.ISelection;

/**
 * created by Talend on Mar 17, 2015 Detailled comment
 *
 */
public interface ITestContainerView {

    public static final String ID = "org.talend.testcontainer.core.ui.views.TestContainerView"; //$NON-NLS-1$

    public void cleanDisplay();

    public void refresh();

    public ISelection getSelection();

    public void refreshCurrentViewTab();

    public boolean isCleaned();
}
