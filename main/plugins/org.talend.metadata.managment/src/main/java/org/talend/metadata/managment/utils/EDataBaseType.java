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
package org.talend.metadata.managment.utils;


/**
 * DOC zshen  class global comment. Detailled comment
 */
public enum EDataBaseType {
    MySQL("MySQL", "mysql"), //$NON-NLS-1$//$NON-NLS-2$
    Oracle("Oracle", "Oracle"), //$NON-NLS-1$
    Microsoft_SQL_Server("Microsoft SQL Server", "Microsoft SQL Server"), //$NON-NLS-1$
    MDM("MDM", "MDM"); //$NON-NLS-1$



    private String productName;

    private String name;
    
    private EDataBaseType(String productName, String name) {
        this.name = name;
        this.productName=productName;
    }

    public String getProductName() {
        return productName;
    }

    public String getName() {
        return name;
    }

}
