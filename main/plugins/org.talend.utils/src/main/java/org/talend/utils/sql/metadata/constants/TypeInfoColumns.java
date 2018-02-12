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
package org.talend.utils.sql.metadata.constants;

/**
 * @author scorreia
 * 
 * The names of columns of the {@link java.sql.ResultSet} got from {@link java.sql.DatabaseMetaData#getTypeInfo()}.
 */
public enum TypeInfoColumns {
    TYPE_NAME,
    DATA_TYPE,
    PRECISION,
    LITERAL_PREFIX,
    LITERAL_SUFFIX,
    CREATE_PARAMS,
    NULLABLE,
    CASE_SENSITIVE,
    SEARCHABLE,
    UNSIGNED_ATTRIBUTE,
    FIXED_PREC_SCALE,
    AUTO_INCREMENT,
    LOCAL_TYPE_NAME,
    MINIMUM_SCALE,
    MAXIMUM_SCALE,
    SQL_DATA_TYPE,
    SQL_DATETIME_SUB,
    NUM_PREC_RADIX;
}
