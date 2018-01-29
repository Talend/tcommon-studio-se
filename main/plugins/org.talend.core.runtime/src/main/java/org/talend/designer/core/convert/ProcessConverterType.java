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
package org.talend.designer.core.convert;

/**
 * This enum is used to store the type of converter. Created by Marvin Wang on Feb 18, 2013.
 * 
 * <pre>
 * <li>{@link ProcessConverterType#CONVERTER_FOR_MAPREDUCE}.
 * <li>{@link ProcessConverterType#CONVERTER_FOR_JOBLET}.
 * <li>{@link ProcessConverterType#CONVERTER_FOR_ROUTE}.
 * 
 */
public enum ProcessConverterType {

    CONVERTER_FOR_MAPREDUCE("CONVERTER_FOR_MAPREDUCE"), //$NON-NLS-1$
    CONVERTER_FOR_JOBLET("CONVERTER_FOR_JOBLET"), //$NON-NLS-1$
    CONVERTER_FOR_SPARK_JOBLET("CONVERTER_FOR_SPARK_JOBLET"), //$NON-NLS-1$
    CONVERTER_FOR_SPARK_STREAMING_JOBLET("CONVERTER_FOR_SPARK_STREAMING_JOBLET"), //$NON-NLS-1$
    CONVERTER_FOR_STORM("CONVERTER_FOR_STORM"), //$NON-NLS-1$
    CONVERTER_FOR_ROUTE("CONVERTER_FOR_ROUTE");//$NON-NLS-1$

    private String converterType;

    ProcessConverterType(String converterType) {
        this.converterType = converterType;
    }
}
