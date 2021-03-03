// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC jding class global comment. Detailled comment
 */
public class SortableDependencyTest {

    @Test
    public void testEqualsHashcode() {
        SortableDependency dependency = new SortableDependency();
        dependency.setGroupId("org.example.test");
        dependency.setArtifactId("test");
        SortableDependency dependency1 = new SortableDependency();
        dependency1.setGroupId("org.example.test");
        dependency1.setArtifactId("test");

        Set<SortableDependency> dependencySet = new HashSet<SortableDependency>();
        dependencySet.add(dependency);
        dependencySet.add(dependency1);
        Assert.assertEquals(1, dependencySet.size());
        Assert.assertTrue(dependency.equals(dependency1));

        dependency.setVersion("0.1");
        dependency1.setVersion("0.2");
        dependencySet.add(dependency1);
        Assert.assertEquals(2, dependencySet.size());
        Assert.assertFalse(dependency.equals(dependency1));

    }

}
