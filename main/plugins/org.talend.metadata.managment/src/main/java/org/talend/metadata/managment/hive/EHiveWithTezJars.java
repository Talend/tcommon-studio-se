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
package org.talend.metadata.managment.hive;

/**
 *
 * created by ycbai on 2015年5月4日 Detailled comment
 *
 */
public enum EHiveWithTezJars {

    HDP_2_1(new String[] { "commons-collections4-4.0.jar", "guice-3.0.jar",
            "hadoop-mapreduce-client-shuffle-2.4.0.2.1.1.0-385.jar", "jettison-1.3.4.jar", "snappy-java-1.0.4.1.jar",
            "tez-api-0.4.0.2.1.1.0-385.jar", "tez-common-0.4.0.2.1.1.0-385.jar", "tez-dag-0.4.0.2.1.1.0-385.jar",
            "tez-mapreduce-0.4.0.2.1.1.0-385.jar", "tez-mapreduce-examples-0.4.0.2.1.1.0-385.jar",
            "tez-runtime-internals-0.4.0.2.1.1.0-385.jar", "tez-runtime-library-0.4.0.2.1.1.0-385.jar",
            "tez-tests-0.4.0.2.1.1.0-385.jar" }),

    // HDP_2_2(new String[] { "tez-api-0.5.2.2.2.0.0-2041.jar", "tez-common-0.5.2.2.2.0.0-2041.jar",
    // "tez-dag-0.5.2.2.2.0.0-2041.jar", "tez-mapreduce-0.5.2.2.2.0.0-2041.jar",
    // "tez-mbeans-resource-calculator-0.5.2.2.2.0.0-2041.jar", "tez-runtime-internals-0.5.2.2.2.0.0-2041.jar",
    // "tez-runtime-library-0.5.2.2.2.0.0-2041.jar", "tez-yarn-timeline-history-0.5.2.2.2.0.0-2041.jar",
    // "commons-cli-1.2.jar", "commons-codec-1.4.jar", "commons-collections4-4.0.jar", "commons-collections-3.2.1.jar",
    // "commons-io-2.4.jar", "commons-lang-2.6.jar", "commons-logging-1.1.3.jar", "commons-math3-3.1.1.jar",
    // "guava-11.0.2.jar", "hadoop-annotations-2.6.0.2.2.0.0-2041.jar",
    // "hadoop-mapreduce-client-common-2.6.0.2.2.0.0-2041.jar", "hadoop-mapreduce-client-core-2.6.0.2.2.0.0-2041.jar",
    // "jettison-1.3.4.jar", "jsr305-2.0.3.jar", "log4j-1.2.17.jar", "protobuf-java-2.5.0.jar" }),

    HDP_2_2(new String[] { "aopalliance-1.0.jar", "apacheds-i18n-2.0.0-M15.jar", "apacheds-kerberos-codec-2.0.0-M15.jar",
            "api-asn1-api-1.0.0-M20.jar", "api-util-1.0.0-M20.jar", "asm-3.1.jar", "avro-1.7.4.jar",
            "commons-beanutils-1.7.0.jar", "commons-beanutils-core-1.8.0.jar", "commons-compress-1.4.1.jar",
            "commons-configuration-1.6.jar", "commons-digester-1.8.jar", "commons-net-3.1.jar", "curator-client-2.6.0.jar",
            "curator-framework-2.6.0.jar", "curator-recipes-2.6.0.jar", "gson-2.2.4.jar", "guice-3.0.jar",
            "guice-servlet-3.0.jar", "hadoop-auth-2.6.0.2.2.0.0-2041.jar", "hadoop-common-2.6.0.2.2.0.0-2041.jar",
            "hadoop-hdfs-2.6.0.2.2.0.0-2041.jar", "hadoop-yarn-api-2.6.0.2.2.0.0-2041.jar",
            "hadoop-yarn-client-2.6.0.2.2.0.0-2041.jar", "hadoop-yarn-common-2.6.0.2.2.0.0-2041.jar", "htrace-core-3.0.4.jar",
            "httpclient-4.2.5.jar", "httpcore-4.2.4.jar", "jackson-core-2.2.3.jar", "jackson-core-asl-1.9.13.jar",
            "jackson-jaxrs-1.9.13.jar", "jackson-mapper-asl-1.9.13.jar", "jackson-xc-1.9.13.jar", "javax.inject-1.jar",
            "jaxb-impl-2.2.3-1.jar", "jersey-client-1.9.jar", "jersey-core-1.9.jar", "jersey-guice-1.9.jar",
            "jersey-json-1.9.jar", "jersey-server-1.9.jar", "jetty-util-6.1.26.hwx.jar",
            "microsoft-windowsazure-storage-sdk-0.6.0.jar", "netty-3.6.2.Final.jar", "paranamer-2.3.jar", "servlet-api-2.5.jar",
            "slf4j-api-1.7.5.jar", "slf4j-log4j12-1.7.5.jar", "snappy-java-1.0.4.1.jar", "stax-api-1.0.1.jar",
            "xercesImpl-2.9.1.jar", "xml-apis-1.3.04.jar", "xmlenc-0.52.jar", "xz-1.0.jar", "zookeeper-3.4.6.jar" }),

    HDP_2_3(new String[] { "aopalliance-1.0.jar", "apacheds-i18n-2.0.0-M15.jar", "apacheds-kerberos-codec-2.0.0-M15.jar",
            "api-asn1-api-1.0.0-M20.jar", "api-util-1.0.0-M20.jar", "asm-3.2.jar", "avro-1.7.5.jar",
            "commons-beanutils-1.7.0.jar", "commons-beanutils-core-1.8.0.jar", "commons-compress-1.4.1.jar",
            "commons-configuration-1.6.jar", "commons-digester-1.8.jar", "commons-net-3.1.jar", "curator-client-2.7.1.jar",
            "curator-framework-2.7.1.jar", "curator-recipes-2.7.1.jar", "gson-2.2.4.jar", "guice-3.0.jar",
            "guice-servlet-3.0.jar", "hadoop-auth-2.7.1.2.3.2.0-2950.jar", "hadoop-common-2.7.1.2.3.2.0-2950.jar",
            "hadoop-hdfs-2.7.1.2.3.2.0-2950.jar", "hadoop-yarn-api-2.7.1.2.3.2.0-2950.jar",
            "hadoop-yarn-client-2.7.1.2.3.2.0-2950.jar", "hadoop-yarn-common-2.7.1.2.3.2.0-2950.jar",
            "htrace-core-3.1.0-incubating.jar", "httpclient-4.2.5.jar", "httpcore-4.2.5.jar", "jackson-core-2.2.3.jar",
            "jackson-core-asl-1.9.13.jar", "jackson-jaxrs-1.9.13.jar", "jackson-mapper-asl-1.9.13.jar", "jackson-xc-1.9.13.jar",
            "javax.inject-1.jar", "jaxb-impl-2.2.3-1.jar", "jersey-client-1.9.jar", "jersey-core-1.9.jar",
            "jersey-guice-1.9.jar", "jersey-json-1.9.jar", "jersey-server-1.9.jar", "jetty-util-6.1.26.hwx.jar",
            "microsoft-windowsazure-storage-sdk-0.6.0.jar", "netty-3.6.2.Final.jar", "paranamer-2.3.jar", "servlet-api-2.5.jar",
            "slf4j-api-1.7.10.jar", "slf4j-log4j12-1.7.10.jar", "snappy-java-1.0.4.1.jar", "stax-api-1.0-2.jar",
            "xercesImpl-2.9.1.jar", "xml-apis-1.3.04.jar", "xmlenc-0.52.jar", "xz-1.0.jar", "zookeeper-3.4.6.2.3.2.0-2950.jar" }),
    MAPR401(new String[] { "commons-collections4-4.0.jar", "guava-11.0.2.jar", "guice-3.0.jar",
            "hadoop-mapreduce-client-shuffle-2.4.1-mapr-1408.jar", "jettison-1.3.4.jar", "snappy-java-1.0.4.1.jar",
            "tez-api-0.4.0-mapr-1408.jar", "tez-common-0.4.0-mapr-1408.jar", "tez-dag-0.4.0-mapr-1408.jar",
            "tez-mapreduce-0.4.0-mapr-1408.jar", "tez-mapreduce-examples-0.4.0-mapr-1408.jar",
            "tez-runtime-internals-0.4.0-mapr-1408.jar", "tez-runtime-library-0.4.0-mapr-1408.jar",
            "tez-tests-0.4.0-mapr-1408.jar" }),

    ;

    private String[] jars;

    EHiveWithTezJars(String[] jars) {
        this.jars = jars;
    }

    public String getName() {
        return name();
    }

    public String[] getJars() {
        return this.jars;
    }

    public static String[] getJarsByVersion(String version) {
        EHiveWithTezJars[] versions = values();
        for (EHiveWithTezJars tVersion : versions) {
            if (tVersion.getName().equals(version)) {
                return tVersion.getJars();
            }
        }
        return null;
    }

}
