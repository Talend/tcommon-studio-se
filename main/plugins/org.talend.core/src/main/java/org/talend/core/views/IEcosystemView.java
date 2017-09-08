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
package org.talend.core.views;

/**
 * 
 * DOC gcui class global comment. Detailled comment
 */
public interface IEcosystemView {

    public static final String VIEW_ID = "org.talend.designer.components.ecosystem.ui.views.EcosystemView"; //$NON-NLS-1$

    public void refresh();

    public void refreshVersions();
}
