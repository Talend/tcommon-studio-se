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
package org.talend.librariesmanager.ui.service;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.routines.IRoutineProviderCreator;
import org.talend.core.model.routines.IRoutinesProvider;
import org.talend.librariesmanager.model.service.TosJavaRoutinesProvider;

/**
 * bqian class global comment. Detailled comment
 */
public class TosRoutinesProviderCreator implements IRoutineProviderCreator {

    IRoutinesProvider javaProvider = null;

    public TosRoutinesProviderCreator() {
        javaProvider = new TosJavaRoutinesProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.routines.IRoutineProviderCreator#createIRoutinesProviderByLanguage(org.talend.core.language
     * .ECodeLanguage)
     */
    @Override
    public IRoutinesProvider createIRoutinesProviderByLanguage(ECodeLanguage lan) {
        return javaProvider;
    }
}
