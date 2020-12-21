package org.talend.librariesmanager.nexus.utils;

import org.junit.Assert;
import org.junit.Test;

public class ShareLibrariesUtilTest {

    @Test
    public void testGetMavenClassifier() {
        String path = "javax/xml/bind/acxb-test/2.2.6/acxb-test-2.2.6-jdk10.dll";
        String classifier = ShareLibrariesUtil.getMavenClassifier(path, "acxb-test-2.2.6", "dll");
        Assert.assertEquals(classifier, "jdk10");
        //
        String path1 = "org/talend/libraries/aa/6.0.0-SNAPSHOT/aa-6.0.0-20201027.064528-1.dll";
        String classifier1 = ShareLibrariesUtil.getMavenClassifier(path1, "aa-6.0.0-SNAPSHOT", "dll");
        Assert.assertNull(classifier1);

        String path2 = "org/talend/libraries/ldapjdk/6.0.0/ldapjdk-6.0.0-jdk9.dll";
        String classifier2 = ShareLibrariesUtil.getMavenClassifier(path2, "ldapjdk-6.0.0", "dll");
        Assert.assertEquals(classifier2, "jdk9");

        //
        String path3 = "org/talend/libraries/acxb-test-2.2.6-jdk8/6.0.0-SNAPSHOT/acxb-test-2.2.6-jdk8-6.0.0-20201103.062422-1.dll";
        String classifier3 = ShareLibrariesUtil.getMavenClassifier(path3, "acxb-test-2.2.6-jdk8-6.0.0-SNAPSHOT", "dll");
        Assert.assertNull(classifier3);

        String path4 = "org/talend/libraries/acxb-test-2.2.6-jdk8/6.0.0-SNAPSHOT/acxb-test-2.2.6-jdk8-6.0.0-SNAPSHOT-jdk8.dll";
        String classifier4 = ShareLibrariesUtil.getMavenClassifier(path4, "acxb-test-2.2.6-jdk8-6.0.0-SNAPSHOT", "dll");
        Assert.assertEquals(classifier4, "jdk8");
    }

}
