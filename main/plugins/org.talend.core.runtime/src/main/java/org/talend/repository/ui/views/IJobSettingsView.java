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
package org.talend.repository.ui.views;

import java.util.Map;

import org.eclipse.jface.viewers.ISelection;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IJobSettingsView {

    public static final String ID = "org.talend.designer.core.ui.views.jobsettings.JobSettingsView"; //$NON-NLS-1$

    public static final String JOBTYPE_CHANGED = "JOBTYPE_CHANGED"; //$NON-NLS-1$

    public void cleanDisplay();

    public void refresh();

    public ISelection getSelection();

    public void refreshCurrentViewTab();

    public boolean isCleaned();

    public void onPropertiesChanged(Map<String, Object> maps);

}
