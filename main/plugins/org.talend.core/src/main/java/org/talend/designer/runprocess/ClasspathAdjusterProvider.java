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
package org.talend.designer.runprocess;

import java.util.List;

import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;

/**
 * created by wchen on Jul 20, 2017 Detailled comment
 *
 */
public class ClasspathAdjusterProvider {

    private static List<IClasspathAdjuster> adjusters;

    public synchronized static List<IClasspathAdjuster> getClasspathAdjuster() {
        if (adjusters == null) {
            adjusters = ExtensionImplementationProvider.getInstance(new ExtensionPointLimiterImpl(
                    "org.talend.core.classpath_adjuster", "ClasspathAdjuster", 0, -1));
        }
        return adjusters;
    }

}
