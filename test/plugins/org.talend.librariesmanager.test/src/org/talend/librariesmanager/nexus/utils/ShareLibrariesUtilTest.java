package org.talend.librariesmanager.nexus.utils;

import org.junit.Assert;
import org.junit.Test;

public class ShareLibrariesUtilTest {

    @Test
    public void testGetMavenClassifier() {
        String path = "javax/xml/bind/acxb-test/2.2.6/acxb-test-2.2.6-jdk10.dll";
        String classifier = ShareLibrariesUtil.getMavenClassifier(path, "acxb-test-2.2.6", "dll");
        Assert.assertEquals(classifier, "jdk10");

        String path1 = "org/talend/libraries/aa/6.0.0-SNAPSHOT/aa-6.0.0-20201027.064528-1.dll";
        String classifier1 = ShareLibrariesUtil.getMavenClassifier(path1, "aa-6.0.0-SNAPSHOT", "dll");
        Assert.assertNull(classifier1);
    }

}
