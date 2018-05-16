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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;

/**
 * DOC informix class global comment. Detailled comment
 */
public interface ISalesforceModuleParser {

    ArrayList login(String endPoint, String username, String password) throws Exception;

    ArrayList login(String endPoint, String username, String password, String timeOut) throws Exception;

    void describeGlobalSample();

    /**
     * Fetch a module from SF and transfor to Talend metadata data type. DOC YeXiaowei Comment method
     * "fetchMetaDataColumns".
     * 
     * @param module
     * @return
     */
    List<IMetadataColumn> fetchMetaDataColumns(String module);

    /**
     * Getter for login.
     * 
     * @return the login
     */
    boolean isLogin();

    /**
     * Sets the login.
     * 
     * @param login the login to set
     */
    void setLogin(boolean login);

    /**
     * Getter for currentModuleName.
     * 
     * @return the currentModuleName
     */
    String getCurrentModuleName();

    /**
     * Sets the currentModuleName.
     * 
     * @param currentModuleName the currentModuleName to set
     */
    void setCurrentModuleName(String currentModuleName);

    /**
     * Getter for currentMetadataColumns.
     * 
     * @return the currentMetadataColumns
     */
    List<IMetadataColumn> getCurrentMetadataColumns();

    /**
     * Sets the currentMetadataColumns.
     * 
     * @param currentMetadataColumns the currentMetadataColumns to set
     */
    void setCurrentMetadataColumns(List<IMetadataColumn> currentMetadataColumns);

}
