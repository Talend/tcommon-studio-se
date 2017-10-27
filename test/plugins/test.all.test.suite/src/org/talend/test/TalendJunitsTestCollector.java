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
package org.talend.test;

import test.common.BundleTestCollector;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class TalendJunitsTestCollector extends TalendTestCollector {

    private Class<?>[] testsToExecute;

    @Override
    public Class<?>[] getTests() {
        if (testsToExecute == null) {
            testsToExecute = super.getTests();
            // codegen must be first one to wait it to finish
            BundleTestCollector testCollector = new BundleTestCollector();
            Class<?>[] allCollectedTestClasses = testCollector.collectTestsClasses(null, "org.talend.designer.codegen.test", "",
                    "org.talend.designer.codegen.junit", "JUnitStudioStartup", false);

            // add a loop, but in all case there should be only one test executed here.
            for (Class<?> clazz : allCollectedTestClasses) {
                System.out.println("## To execute first:" + clazz.getName() + "\n");
                Class<?>[] tempArray = new Class<?>[testsToExecute.length + 1];
                tempArray[0] = clazz;
                for (int i = 0; i < testsToExecute.length; i++) {
                    tempArray[i + 1] = testsToExecute[i];
                }
                testsToExecute = tempArray;
            }

        }
        return testsToExecute;
    }
}
