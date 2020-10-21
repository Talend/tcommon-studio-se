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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public class VersionUtil {
    
    public static String getSNAPSHOTVersion(String rVersion) {
        return MavenUrlHelper.getSNAPSHOTVersion(rVersion);
    }

    public static List<MavenArtifact> filterSnapshotArtifacts(List<MavenArtifact> arts) {
        List<MavenArtifact> ret = new ArrayList<MavenArtifact>();
        if (arts == null || arts.isEmpty()) {
            return ret;
        }

        Map<String, List<MavenArtifact>> snapshotArtifacts = new HashMap<String, List<MavenArtifact>>();

        for (MavenArtifact art : arts) {
            if (isSnapshot(art.getVersion())) {
                String key = art.getGroupId() + "-" + art.getArtifactId();
                List<MavenArtifact> groupArts = null;
                if (snapshotArtifacts.containsKey(key)) {
                    groupArts = snapshotArtifacts.get(key);
                    groupArts.add(art);
                } else {
                    groupArts = new ArrayList<MavenArtifact>();
                }
                groupArts.add(art);
            } else {
                ret.add(art);
            }
        }
        
        Set<Entry<String, List<MavenArtifact>>> entries = snapshotArtifacts.entrySet();
        for (Entry<String, List<MavenArtifact>> entry : entries) {
            MavenArtifact art = ShareLibrariesUtil.getLateUpdatedMavenArtifact(entry.getValue());
            if (art != null) {
                ret.add(art);
            }
        }

        return ret;
    }

    public static boolean isSnapshot(String v) {
        if (v != null && v.toUpperCase().endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
            return true;
        }
        return false;
    }


}
