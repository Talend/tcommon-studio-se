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
package org.talend.core.runtime.repository.build;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BuildExportRegistryReaderTest {

    class BuildProviderTestClass extends AbstractBuildProvider {

    }

    @Test
    public void test_processBuildProviders_order() {
        BuildProviderRegistry registry1 = new BuildProviderRegistry();
        registry1.id = "test1";
        registry1.provider = new BuildProviderTestClass();
        registry1.provider.buildType = new BuildType("Type1", "my first type", 10);

        BuildProviderRegistry registry2 = new BuildProviderRegistry();
        registry2.id = "test2";
        registry2.provider = new BuildProviderTestClass();
        registry2.provider.buildType = new BuildType("Type2", "my second type", 20);

        BuildExportRegistryReader reader = new BuildExportRegistryReader();
        reader.buildProvidersMap = new HashMap<String, BuildProviderRegistry>();

        reader.buildProvidersMap.put(registry1.id, registry1);
        reader.buildProvidersMap.put(registry2.id, registry2);

        reader.processBuildProviders();

        AbstractBuildProvider[] buildProviders = reader.buildProviders;

        Assert.assertNotNull(buildProviders);
        Assert.assertEquals(2, buildProviders.length);

        Assert.assertEquals(registry1.provider, buildProviders[0]);
        Assert.assertEquals("Type1", buildProviders[0].buildType.name);

        Assert.assertEquals(registry2.provider, buildProviders[1]);
        Assert.assertEquals("Type2", buildProviders[1].buildType.name);
    }

    @Test
    public void test_processBuildProviders_override() {
        BuildProviderRegistry registry1 = new BuildProviderRegistry();
        registry1.id = "test1";
        registry1.provider = new BuildProviderTestClass();
        registry1.provider.buildType = new BuildType("Type1", "my first type", 10);

        BuildProviderRegistry registry2 = new BuildProviderRegistry();
        registry2.id = "test2";
        registry2.provider = new BuildProviderTestClass();
        registry2.provider.buildType = new BuildType("Type2", "my second type", 20);

        BuildProviderRegistry registry3 = new BuildProviderRegistry();
        registry3.id = "my-test";
        registry3.provider = new BuildProviderTestClass();
        registry3.provider.buildType = new BuildType("MyType", "I'm not first one yet", 30); // diff order
        registry3.overrideId = registry1.id; // override first one.

        BuildExportRegistryReader reader = new BuildExportRegistryReader();
        reader.buildProvidersMap = new HashMap<String, BuildProviderRegistry>();

        reader.buildProvidersMap.put(registry1.id, registry1);
        reader.buildProvidersMap.put(registry2.id, registry2);
        reader.buildProvidersMap.put(registry3.id, registry3);

        reader.processBuildProviders();

        AbstractBuildProvider[] buildProviders = reader.buildProviders;

        Assert.assertNotNull(buildProviders);
        Assert.assertEquals(2, buildProviders.length);

        Assert.assertEquals(registry2.provider, buildProviders[0]);
        Assert.assertEquals("Type2", buildProviders[0].buildType.name);

        Assert.assertEquals(registry3.provider, buildProviders[1]);
        Assert.assertEquals("MyType", buildProviders[1].buildType.name);
    }
}
