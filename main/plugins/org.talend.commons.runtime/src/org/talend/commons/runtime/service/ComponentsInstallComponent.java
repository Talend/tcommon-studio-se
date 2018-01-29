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
package org.talend.commons.runtime.service;

import java.io.File;
import java.util.List;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface ComponentsInstallComponent extends P2InstallComponent {

    String FOLDER_COMPS = "components"; //$NON-NLS-1$

    String FOLDER_M2_REPOSITORY = "m2/repository"; //$NON-NLS-1$

    void setLogin(boolean login);

    void setComponentFolder(File componentFolder);

    List<File> getFailedComponents();
}
