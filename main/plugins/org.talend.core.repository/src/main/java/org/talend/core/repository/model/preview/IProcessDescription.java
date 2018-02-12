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
package org.talend.core.repository.model.preview;

import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataTable;

/**
 * Description of a Process.
 * 
 * $Id: IProcessDescription.java 663 2006-11-21 17:21:53 +0000 (星期二, 21 十一月 2006) cantoine $
 * 
 */
/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public interface IProcessDescription {

    /**
     * Getter for escapeCharacter.
     * 
     * @return the escapeCharacter
     */
    public String getEscapeCharacter();

    /**
     * Sets the escapeCharacter.
     * 
     * @param escapeCharacter the escapeCharacter to set
     */
    public void setEscapeCharacter(String escapeCharacter);

    /**
     * Getter for fieldSeparator.
     * 
     * @return the fieldSeparator
     */
    public String getFieldSeparator();

    /**
     * Sets the fieldSeparator.
     * 
     * @param fieldSeparator the fieldSeparator to set
     */
    public void setFieldSeparator(String fieldSeparator);

    /**
     * Getter for filepath.
     * 
     * @return the filepath
     */
    public String getFilepath();

    /**
     * Sets the filepath.
     * 
     * @param filepath the filepath to set
     */
    public void setFilepath(String filepath);

    /**
     * Getter for footerRow.
     * 
     * @return the footerRow
     */
    public int getFooterRow();

    /**
     * Sets the footerRow.
     * 
     * @param footerRow the footerRow to set
     */
    public void setFooterRow(int footerRow);

    /**
     * Getter for headerRow.
     * 
     * @return the headerRow
     */
    public int getHeaderRow();

    /**
     * Sets the headerRow.
     * 
     * @param headerRow the headerRow to set
     */
    public void setHeaderRow(int headerRow);

    /**
     * Getter for rowSeparator.
     * 
     * @return the rowSeparator
     */
    public String getRowSeparator();

    /**
     * Sets the rowSeparator.
     * 
     * @param rowSeparator the rowSeparator to set
     */
    public void setRowSeparator(String rowSeparator);

    /**
     * Getter for server.
     * 
     * @return the server
     */
    public String getServer();

    /**
     * Sets the server.
     * 
     * @param server the server to set
     */
    public void setServer(String server);

    /**
     * Getter for textEnclosure.
     * 
     * @return the textEnclosure
     */
    public String getTextEnclosure();

    /**
     * Sets the textEnclosure.
     * 
     * @param textEnclosure the textEnclosure to set
     */
    public void setTextEnclosure(String textEnclosure);

    /**
     * Getter for limitRows.
     * 
     * @return the limitRows
     */
    public int getLimitRows();

    /**
     * Sets the limitRows.
     * 
     * @param limitRows the limitRows to set
     */
    public void setLimitRows(int limitRows);

    /**
     * DOC ocarbone Comment method "setRemoveEmptyRow".
     * 
     * @param boolean
     */
    public void setRemoveEmptyRow(boolean selection);

    /**
     * DOC ocarbone Comment method "getRemoveEmptyRowsToSkip".
     * 
     * @return boolean
     */
    public boolean getRemoveEmptyRowsToSkip();

    /**
     * Getter for pattern.
     * 
     * @return the pattern
     */
    public String getPattern();

    /**
     * Sets the pattern.
     * 
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern);

    /**
     * Getter for encoding.
     * 
     * @return the encoding
     */
    public String getEncoding();

    /**
     * Sets the encoding.
     * 
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding);

    /**
     * Getter for schema.
     * 
     * @return the schema
     */
    public List<IMetadataTable> getSchema();

    /**
     * Sets the schema.
     * 
     * @param schema the schema to set
     */
    public void setSchema(List<IMetadataTable> schema);

    /**
     * Getter for loopQuery.
     * 
     * @return the loopQuery
     */
    public String getLoopQuery();

    /**
     * Sets the loopQuery.
     * 
     * @param loopQuery the loopQuery to set
     */
    public void setLoopQuery(String loopQuery);

    /**
     * Getter for loopLimit.
     * 
     * @return the loopLimit
     */
    public Integer getLoopLimit();

    /**
     * Sets the loopLimit.
     * 
     * @param loopLimit the loopLimit to set
     */
    public void setLoopLimit(Integer loopLimit);

    /**
     * Getter for mapping.
     * 
     * @return the mapping
     */
    public List<Map<String, String>> getMapping();

    /**
     * Sets the mapping.
     * 
     * @param mapping the mapping to set
     */
    public void setMapping(List<Map<String, String>> mapping);

    /**
     * Administrator Comment method "getLdapSchemaBean".
     * 
     * @return
     */
    public LDAPSchemaBean getLdapSchemaBean();

    /**
     * Administrator Comment method "setLdapSchemaBean".
     * 
     * @param ldapSchemaBean
     */
    public void setLdapSchemaBean(LDAPSchemaBean ldapSchemaBean);

    /**
     * Administrator Comment method "getLdapSchemaBean".
     * 
     * @return
     */
    public WSDLSchemaBean getWsdlSchemaBean();

    /**
     * Administrator Comment method "setLdapSchemaBean".
     * 
     * @param ldapSchemaBean
     */
    public void setWsdlSchemaBean(WSDLSchemaBean wsdlSchemaBean);

    /**
     * 
     * DOC YeXiaowei Comment method "addExcelSchemaBean".
     * 
     * @param excelSchemaBean
     */
    public void setExcelSchemaBean(ExcelSchemaBean excelSchemaBean);

    /**
     * 
     * DOC YeXiaowei Comment method "getExcelSchemaBean".
     * 
     * @return
     */
    public ExcelSchemaBean getExcelSchemaBean();

    /**
     * Getter for salesforceSchemaBean.
     * 
     * @return the salesforceSchemaBean
     */
    public SalesforceSchemaBean getSalesforceSchemaBean();

    /**
     * Sets the salesforceSchemaBean.
     * 
     * @param salesforceSchemaBean the salesforceSchemaBean to set
     */
    public void setSalesforceSchemaBean(SalesforceSchemaBean salesforceSchemaBean);

    public boolean isSplitRecord();

    public String getReadbyMode();

    public void setReadbyMode(String readbyMode);
}
