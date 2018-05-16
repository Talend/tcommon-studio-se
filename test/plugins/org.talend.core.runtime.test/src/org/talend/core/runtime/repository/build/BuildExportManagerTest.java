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

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BuildExportManagerTest {

    class BuildProviderTestClass extends AbstractBuildProvider {

        @Override
        public boolean valid(Map<String, Object> parameters) {
            return true;
        }
    }

    class BuildExportManagerEmptyTestClass extends BuildExportManager {

        @Override
        public AbstractBuildProvider[] getAllBuildProviders() {
            return new AbstractBuildProvider[0];
        }
    }

    class BuildExportManagerTestClass extends BuildExportManagerEmptyTestClass {

        @Override
        public AbstractBuildProvider[] getAllBuildProviders() {
            AbstractBuildProvider[] prividers = new AbstractBuildProvider[3];
            prividers[0] = new BuildProviderTestClass() {

                @Override
                public BuildType getBuildType() {
                    return new BuildType("Type1", "my first type");
                }

            };
            /*
             * keep null for build type
             */
            prividers[1] = new BuildProviderTestClass();
            prividers[2] = new BuildProviderTestClass() {

                @Override
                public BuildType getBuildType() {
                    return new BuildType("Type3", "my third type");
                }
            };

            return prividers;
        }

    }

    @Test
    public void test_getValidBuildTypes_emptyProviders() {
        final BuildType[] validBuildTypes = new BuildExportManagerEmptyTestClass().getValidBuildTypes(Collections.emptyMap());

        Assert.assertNotNull(validBuildTypes);
        Assert.assertEquals(0, validBuildTypes.length);
    }

    @Test
    public void test_getValidBuildTypes_nullBuildTypeInProvider() {
        final BuildType[] validBuildTypes = new BuildExportManagerTestClass().getValidBuildTypes(Collections.emptyMap());

        Assert.assertNotNull(validBuildTypes);
        Assert.assertEquals(2, validBuildTypes.length);
        Assert.assertEquals("Type1", validBuildTypes[0].getName());
        Assert.assertEquals("Type3", validBuildTypes[1].getName());
    }

    @Test
    public void test_getValidBuildProviders_emptyProviders() {
        final AbstractBuildProvider[] validBuildProviders = new BuildExportManagerEmptyTestClass()
                .getValidBuildProviders(Collections.emptyMap());
        Assert.assertNotNull(validBuildProviders);
        Assert.assertEquals(0, validBuildProviders.length);
    }

    @Test
    public void test_getValidBuildProviders() {
        final AbstractBuildProvider[] validBuildProviders = new BuildExportManagerTestClass().getValidBuildProviders(Collections
                .emptyMap());
        Assert.assertNotNull(validBuildProviders);
        Assert.assertEquals(3, validBuildProviders.length);
        Assert.assertEquals("Type1", validBuildProviders[0].getBuildType().getName());
        Assert.assertNull(validBuildProviders[1].getBuildType());
        Assert.assertEquals("Type3", validBuildProviders[2].getBuildType().getName());
    }

    @Test
    public void test_getBuildProvider_emptyProvider() {
        AbstractBuildProvider buildProvider = new BuildExportManagerEmptyTestClass().getBuildProvider("Type1",
                Collections.emptyMap());
        Assert.assertNull(buildProvider);
    }

    @Test
    public void test_getBuildProvider_nullTypeName() {
        AbstractBuildProvider buildProvider = new BuildExportManagerTestClass().getBuildProvider(null, Collections.emptyMap());
        Assert.assertNotNull(buildProvider);
        Assert.assertNotNull(buildProvider.getBuildType());
        Assert.assertEquals("Type1", buildProvider.getBuildType().getName()); // first one by default
    }

    @Test
    public void test_getBuildProvider_withTypeName() {
        AbstractBuildProvider buildProvider = new BuildExportManagerTestClass().getBuildProvider("Type1", Collections.emptyMap());
        Assert.assertNotNull(buildProvider);
        Assert.assertNotNull(buildProvider.getBuildType());
        Assert.assertEquals("Type1", buildProvider.getBuildType().getName());

        buildProvider = new BuildExportManagerTestClass().getBuildProvider("Type3", Collections.emptyMap());
        Assert.assertNotNull(buildProvider);
        Assert.assertNotNull(buildProvider.getBuildType());
        Assert.assertEquals("Type3", buildProvider.getBuildType().getName());
    }

}
