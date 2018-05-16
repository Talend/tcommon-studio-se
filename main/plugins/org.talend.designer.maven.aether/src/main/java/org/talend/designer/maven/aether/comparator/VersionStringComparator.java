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
package org.talend.designer.maven.aether.comparator;

import java.util.Comparator;

import org.eclipse.aether.util.StringUtils;
import org.eclipse.aether.util.version.GenericVersionScheme;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionScheme;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class VersionStringComparator implements Comparator<String> {

    private VersionScheme versionScheme = new GenericVersionScheme();

    @Override
    public int compare(String arg0, String arg1) {
        if (StringUtils.isEmpty(arg0) && StringUtils.isEmpty(arg1)) {
            return 0;
        }
        if (StringUtils.isEmpty(arg0)) {
            return -1;
        }
        if (StringUtils.isEmpty(arg1)) {
            return 1;
        }
        try {
            Version version0 = versionScheme.parseVersion(arg0);
            Version version1 = versionScheme.parseVersion(arg1);
            return version0.compareTo(version1);
        } catch (InvalidVersionSpecificationException e) {
            return arg0.compareTo(arg1);
        }
    }

}
