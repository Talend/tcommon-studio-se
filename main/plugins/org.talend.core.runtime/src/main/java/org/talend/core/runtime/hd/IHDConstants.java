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
package org.talend.core.runtime.hd;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IHDConstants {

    /**
     * same as ICustomDistribution.DISTRIBUTION_NAME
     */
    static final String DISTRIBUTION_CUSTOM = "CUSTOM";

    /**
     * must be full class name of {@link HadoopComponent}
     */
    static final String SERVICE_HADOOP = "org.talend.hadoop.distribution.component.HadoopComponent";

    /**
     * must be full class name of {@link MRComponent}
     */
    static final String SERVICE_MR = "org.talend.hadoop.distribution.component.MRComponent";

    /**
     * must be full class name of {@link SparkComponent}
     */
    static final String SERVICE_SPARK = "org.talend.hadoop.distribution.component.SparkComponent";

    /**
     * must be full class name of {@link SparkBatchComponent}
     */
    static final String SERVICE_SPARK_BATCH = "org.talend.hadoop.distribution.component.SparkBatchComponent";

    /**
     * must be full class name of {@link SparkStreamingComponent}
     */
    static final String SERVICE_SPARK_STREAMING = "org.talend.hadoop.distribution.component.SparkStreamingComponent";

}
