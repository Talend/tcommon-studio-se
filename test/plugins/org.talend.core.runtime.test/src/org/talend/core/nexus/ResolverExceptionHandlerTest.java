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
package org.talend.core.nexus;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

public class ResolverExceptionHandlerTest {

    @Test
    public void testHideCredential() {
        String credencialStr1 = "talend-custom-libs-admin:talend-custom-libs-admin@";
        String credencialStr2 = "studio-dl-client:studio-dl-client@";
        String message = "Error resolving artifact org.slf4j:jcl-over-slf4j:pom:1.7.25: [Could not find artifact org.slf4j:jcl-over-slf4j:pom:1.7.25 in talend-custom-libs-release (http://talend-custom-libs-admin:talend-custom-libs-admin@localhost:8083/repository/talend-custom-libs-release/), Could not transfer artifact org.slf4j:jcl-over-slf4j:pom:1.7.25 from/to repo_-1349825302 (https://studio-dl-client:studio-dl-client@talend-update.talend.com/nexus/content/groups/dynamicdistribution/): No such host is known (talend-update.talend.com)]";
        IOException cause = new IOException(message);
        IOException e = new IOException(message, cause);
        IOException cleanException = ResolverExceptionHandler.hideCredential(e);

        assertFalse(cleanException.getMessage().contains(credencialStr1));
        assertFalse(cleanException.getCause().getMessage().contains(credencialStr1));
        assertFalse(cleanException.getMessage().contains(credencialStr2));
        assertFalse(cleanException.getCause().getMessage().contains(credencialStr2));
    }

}
