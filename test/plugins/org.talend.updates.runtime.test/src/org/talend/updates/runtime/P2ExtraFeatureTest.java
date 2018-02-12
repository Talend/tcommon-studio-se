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
package org.talend.updates.runtime;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;
import org.talend.commons.utils.VersionUtils;
import org.talend.updates.runtime.model.IuP2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeature;


/**
 * DOC Talend  class global comment. Detailled comment
 */
public class P2ExtraFeatureTest {

    @Test
    public void testGetP2RepositoryURI() {
        org.osgi.framework.Version studioVersion = new org.osgi.framework.Version(VersionUtils.getTalendVersion());
        String version = studioVersion.getMajor() + "." + studioVersion.getMinor() + "." + studioVersion.getMicro();
        
        P2ExtraFeature p2 = new IuP2ExtraFeature(null);
        URI uri = p2.getP2RepositoryURI(null,false);
        assertTrue(uri.equals(URI.create(String.valueOf(version))));
        
        p2 = new IuP2ExtraFeature(null);
        uri = p2.getP2RepositoryURI("default.update.site.url",true);
        assertTrue(uri.equals(URI.create(String.valueOf(version))));
        
        p2 = new IuP2ExtraFeature("http://update.talend.com/Studio/tos/");
        uri = p2.getP2RepositoryURI("default.update.site.url",false);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/"+version)));
        
        p2 = new IuP2ExtraFeature("http://update.talend.com/Studio/tos");
        uri = p2.getP2RepositoryURI("default.update.site.url",false);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/"+version)));
        
        p2 = new IuP2ExtraFeature("http://update.talend.com/Studio/tos/");
        uri = p2.getP2RepositoryURI("default.update.site.url",true);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/"+version)));
        
        p2 = new IuP2ExtraFeature("http://update.talend.com/Studio/tos");
        uri = p2.getP2RepositoryURI(null,false);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/"+version)));
        
        System.setProperty("talend.p2.repo.url", "http://update.talend.com/Studio/tos/"+version);
        p2 = new IuP2ExtraFeature(null);
        uri = p2.getP2RepositoryURI(null,false);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/"+version)));
        
        p2 = new IuP2ExtraFeature("http://update.talend.com/Studio/tos");
        uri = p2.getP2RepositoryURI(null,false);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/"+version)));
        
        System.setProperty("default.update.site.url","http://update.talend.com/Studio/tos/");
        p2 = new IuP2ExtraFeature(null);
        uri = p2.getP2RepositoryURI("default.update.site.url",false);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/")));
        
        p2 = new IuP2ExtraFeature("http://update.talend.com/Studio/tos/");
        uri = p2.getP2RepositoryURI("default.update.site.url",false);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/")));
        
        p2 = new IuP2ExtraFeature("http://update.talend.com/Studio/tos/");
        uri = p2.getP2RepositoryURI("default.update.site.url",true);
        assertTrue(uri.equals(URI.create("http://update.talend.com/Studio/tos/"+version)));
        
    }

}
