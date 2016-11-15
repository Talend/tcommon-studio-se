// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.repository.model.preview;

import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataTable;

public interface IProcessDescription {

    public String getEscapeCharacter();

    public void setEscapeCharacter(String escapeCharacter);

    public String getFieldSeparator();

    public void setFieldSeparator(String fieldSeparator);

    public String getFilepath();

    public void setFilepath(String filepath);

    public int getFooterRow();

    public void setFooterRow(int footerRow);

    public int getHeaderRow();

    public void setHeaderRow(int headerRow);

    public String getRowSeparator();

    public void setRowSeparator(String rowSeparator);

    public String getServer();

    public void setServer(String server);

    public String getTextEnclosure();

    public void setTextEnclosure(String textEnclosure);

    public int getLimitRows();

    public void setLimitRows(int limitRows);

    public void setRemoveEmptyRow(boolean selection);

    public boolean getRemoveEmptyRowsToSkip();

    public String getPattern();

    public void setPattern(String pattern);

    public String getEncoding();

    public void setEncoding(String encoding);

    public List<IMetadataTable> getSchema();

    public void setSchema(List<IMetadataTable> schema);

    public String getLoopQuery();

    public void setLoopQuery(String loopQuery);

    public Integer getLoopLimit();

    public void setLoopLimit(Integer loopLimit);

    public List<Map<String, String>> getMapping();

    public void setMapping(List<Map<String, String>> mapping);

    public LDAPSchemaBean getLdapSchemaBean();

    public void setLdapSchemaBean(LDAPSchemaBean ldapSchemaBean);

    public WSDLSchemaBean getWsdlSchemaBean();

    public void setWsdlSchemaBean(WSDLSchemaBean wsdlSchemaBean);

    public void setExcelSchemaBean(ExcelSchemaBean excelSchemaBean);

    public ExcelSchemaBean getExcelSchemaBean();

    public SalesforceSchemaBean getSalesforceSchemaBean();

    public void setSalesforceSchemaBean(SalesforceSchemaBean salesforceSchemaBean);

    public boolean isSplitRecord();

    public String getReadbyMode();

    public void setReadbyMode(String readbyMode);
}
