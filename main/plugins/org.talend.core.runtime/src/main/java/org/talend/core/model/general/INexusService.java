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
package org.talend.core.model.general;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.talend.core.IService;
import org.talend.core.nexus.NexusServerBean;

public interface INexusService extends IService {

    Map upload(NexusServerBean nexusServerBean, String groupId, String artifactId, String version, URL source);

    Map getMavenMetadata(NexusServerBean nexusServerBean, String groupId, String artifactId, String version);

    InputStream getContentInputStream(NexusServerBean nexusServerBean, String r, String g, String a, String v, String e);

    NexusServerBean getPublishNexusServerBean(String repositoryId);
}
