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
package org.talend.librariesmanager.utils;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.junit.Test;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleToInstall;

/**
 * created by wchen on 2016年2月18日 Detailled comment
 *
 */
public class RemoteModulesHelperTest {

    /**
     * Test method for
     * {@link org.talend.librariesmanager.utils.RemoteModulesHelper#getNotInstalledModulesRunnable(java.util.List, java.util.List, boolean)}
     * .
     * 
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    @Test
    public void testGetNotInstalledModulesRunnableListOfModuleNeededListOfModuleToInstallBoolean()
            throws InvocationTargetException, InterruptedException {
        List<ModuleNeeded> neededModules = new ArrayList<ModuleNeeded>();
        ModuleNeeded m1 = new ModuleNeeded("tMysqlInput", "mysql.jar", "", true, null, null,
                "mvn:org.talend.libraries/mysql/6.0.0");
        ModuleNeeded m2 = new ModuleNeeded("tMysqlInput", "mysql.jar", "", true, null, null,
                "mvn:org.talend.libraries/mysql/6.1.0");
        ModuleNeeded m3 = new ModuleNeeded("tMyComponent1", "test.jar", "", true, null, null,
                "mvn:org.talend.libraries/test/6.0.0");
        ModuleNeeded m4 = new ModuleNeeded("tMyComponent2", "test.jar", "", true, null, null,
                "mvn:org.talend.libraries/test/6.0.0");
        ModuleNeeded m5 = new ModuleNeeded("tMyComponent3", "test.exe", "", true, null, null,
                "mvn:org.talend.libraries/test/6.0.0");
        neededModules.add(m1);
        neededModules.add(m2);
        neededModules.add(m3);
        neededModules.add(m4);
        neededModules.add(m5);
        List<ModuleToInstall> toInstall1 = new ArrayList<ModuleToInstall>();
        IRunnableWithProgress notInstalledModulesRunnable = RemoteModulesHelper.getInstance().getNotInstalledModulesRunnable(
                neededModules, toInstall1, false);
        notInstalledModulesRunnable.run(new NullProgressMonitor());
        assertEquals(4, toInstall1.size());

    }
}
