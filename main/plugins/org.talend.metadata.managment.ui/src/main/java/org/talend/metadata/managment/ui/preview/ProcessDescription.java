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
package org.talend.metadata.managment.ui.preview;

import org.talend.core.repository.model.preview.ExcelSchemaBean;
import org.talend.core.repository.model.preview.LDAPSchemaBean;
import org.talend.core.repository.model.preview.SalesforceSchemaBean;
import org.talend.core.repository.model.preview.WSDLSchemaBean;

/**
 * Describes a process for shadow execution. <br/>
 * 
 * $Id: ProcessDescription.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class ProcessDescription extends AbstractProcessDescription {

    private LDAPSchemaBean ldapSchemaBean;

    private WSDLSchemaBean wsdlSchemaBean;

    private ExcelSchemaBean excelSchemaBean;

    private SalesforceSchemaBean salesforceSchemaBean;

    /**
     * Getter for wsdlSchemaBean.
     * 
     * @return the wsdlSchemaBean
     */
    @Override
    public WSDLSchemaBean getWsdlSchemaBean() {
        return this.wsdlSchemaBean;
    }

    /**
     * Sets the wsdlSchemaBean.
     * 
     * @param wsdlSchemaBean the wsdlSchemaBean to set
     */
    @Override
    public void setWsdlSchemaBean(WSDLSchemaBean wsdlSchemaBean) {
        this.wsdlSchemaBean = wsdlSchemaBean;
    }

    /**
     * Constructs a new ProcessDescription.
     */
    public ProcessDescription() {
        super();
    }

    @Override
    public LDAPSchemaBean getLdapSchemaBean() {
        return this.ldapSchemaBean;
    }

    @Override
    public void setLdapSchemaBean(LDAPSchemaBean ldapSchemaBean) {
        this.ldapSchemaBean = ldapSchemaBean;
    }

    /**
     * Getter for excelSchemaBean.
     * 
     * @return the excelSchemaBean
     */
    @Override
    public ExcelSchemaBean getExcelSchemaBean() {
        return this.excelSchemaBean;
    }

    /**
     * Sets the excelSchemaBean.
     * 
     * @param excelSchemaBean the excelSchemaBean to set
     */
    @Override
    public void setExcelSchemaBean(ExcelSchemaBean excelSchemaBean) {
        this.excelSchemaBean = excelSchemaBean;
    }

    /**
     * Getter for salesforceSchemaBean.
     * 
     * @return the salesforceSchemaBean
     */
    @Override
    public SalesforceSchemaBean getSalesforceSchemaBean() {
        return this.salesforceSchemaBean;
    }

    /**
     * Sets the salesforceSchemaBean.
     * 
     * @param salesforceSchemaBean the salesforceSchemaBean to set
     */
    @Override
    public void setSalesforceSchemaBean(SalesforceSchemaBean salesforceSchemaBean) {
        this.salesforceSchemaBean = salesforceSchemaBean;
    }

}
