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
package org.talend.core.model.metadata;

import java.util.Map;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IMetadataColumn.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IMetadataColumn {

    public static final int OPTIONS_NONE = 0;

    public static final int OPTIONS_IGNORE_KEY = 1 << 0;

    public static final int OPTIONS_IGNORE_NULLABLE = 1 << 1;

    public static final int OPTIONS_IGNORE_COMMENT = 1 << 2;

    public static final int OPTIONS_IGNORE_PATTERN = 1 << 3;

    public static final int OPTIONS_IGNORE_DBTYPE = 1 << 4;

    public static final int OPTIONS_IGNORE_LENGTH = 1 << 5;

    public static final int OPTIONS_IGNORE_PRECISION = 1 << 6;

    public static final int OPTIONS_IGNORE_DEFAULT = 1 << 7;

    public static final int OPTIONS_IGNORE_TALENDTYPE = 1 << 8;

    public static final int OPTIONS_IGNORE_LABEL = 1 << 9;

    public static final int OPTIONS_IGNORE_DBCOLUMNNAME = 1 << 10;

    public static final int OPTIONS_IGNORE_BIGGER_SIZE = 1 << 11;

    public static final int OPTIONS_IGNORE_RELATEDENTITY = 1 << 12;

    public static final int OPTIONS_IGNORE_RELATIONSHIPTYPE = 1 << 13;

    public static final int OPTIONS_IGNORE_ORIGINALLENGTH = 1 << 14;

    public static final int OPTIONS_IGNORE_USED = 1 << 15;

    public static final int OPTIONS_IGNORE_ALL = OPTIONS_IGNORE_LABEL | OPTIONS_IGNORE_TALENDTYPE | OPTIONS_IGNORE_DEFAULT
            | OPTIONS_IGNORE_PRECISION | OPTIONS_IGNORE_LENGTH | OPTIONS_IGNORE_DBTYPE | OPTIONS_IGNORE_PATTERN
            | OPTIONS_IGNORE_COMMENT | OPTIONS_IGNORE_NULLABLE | OPTIONS_IGNORE_KEY | OPTIONS_IGNORE_DBCOLUMNNAME
            | OPTIONS_IGNORE_BIGGER_SIZE | OPTIONS_IGNORE_RELATEDENTITY | OPTIONS_IGNORE_RELATIONSHIPTYPE;

    public String getId();

    public void setId(String i);

    public String getLabel();

    public void setLabel(String label);

    public boolean isKey();

    public void setKey(boolean key);

    public String getType();

    public void setType(String sourceType);

    public String getTalendType();

    public void setTalendType(String talendType);

    public String getPattern();

    public void setPattern(String pattern);

    public Integer getLength();

    public void setLength(Integer length);

    public Integer getOriginalLength();

    public void setOriginalLength(Integer originalLength);

    public boolean isNullable();

    public void setNullable(boolean nullable);

    public Integer getPrecision();

    public void setPrecision(Integer precision);

    public String getDefault();

    public void setDefault(String defaut);

    public String getComment();

    public void setComment(String comment);

    public IMetadataColumn clone();

    public IMetadataColumn clone(boolean withCustoms);

    public boolean sameMetacolumnAs(IMetadataColumn metaColumn);

    public boolean sameMetacolumnAs(IMetadataColumn other, int options);

    public boolean isReadOnly();

    public void setReadOnly(boolean readOnly);

    public boolean isCustom();

    public void setCustom(boolean custom);

    public void setCustomId(int customId); // only for custom columns to sort them

    public int getCustomId(); // only for custom columns to sort them

    public String getOriginalDbColumnName();

    public void setOriginalDbColumnName(String originalDbColumnName);

    public String getRelatedEntity();

    public String getRelationshipType();

    public void setRelatedEntity(String relatedEntity);

    public void setRelationshipType(String relationshipType);

    public String getExpression();

    public void setExpression(String expression);

    public Map<String, String> getAdditionalField();

    public boolean isUsefulColumn();

    public void setUsefulColumn(boolean isUseful);

}
