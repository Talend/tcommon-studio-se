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
package org.talend.core.ui.services;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;


/**
 * DOC hzhao  class global comment. Detailled comment
 */
public interface IHadoopUiService extends IService {

    IPreferenceForm createDynamicDistributionPrefForm(Composite parent, PreferencePage prefPage);

    static IHadoopUiService getInstance() {
        try {
            GlobalServiceRegister serviceRegister = GlobalServiceRegister.getDefault();
            if (serviceRegister.isServiceRegistered(IHadoopUiService.class)) {
                return serviceRegister.getService(IHadoopUiService.class);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

}
