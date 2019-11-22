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
package org.talend.librariesmanager.nexus.utils;

import org.talend.core.runtime.maven.MavenUrlHelper;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class VersionUtil {
    
    public String getSNAPSHOTVersion(String rVersion) {
        if(rVersion == null) {
            return rVersion;
        }
        if(rVersion.contains("-")) {
            return rVersion.substring(0, rVersion.indexOf("-") + 1) + MavenUrlHelper.VERSION_SNAPSHOT;
        }
        return rVersion;
    }

}
