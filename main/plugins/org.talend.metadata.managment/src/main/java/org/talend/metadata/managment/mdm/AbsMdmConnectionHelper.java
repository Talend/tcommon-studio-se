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
package org.talend.metadata.managment.mdm;

import java.io.File;
import java.util.List;

import org.talend.core.model.metadata.builder.connection.MDMConnection;

/**
 * created by wchen on Apr 15, 2015 Detailled comment
 *
 */
public abstract class AbsMdmConnectionHelper {

    public abstract Object checkConnection(String url, String universe, String userName, String password) throws Exception;

    public abstract List<String> getPKs(Object stub, String getDataPKsMethod, String dataPKsClass, String pkRegex,
            String getWsDataPKsMethod) throws Exception;

    public abstract void initConcept(MDMConnection mdmConn, File file) throws Exception;

    public abstract String getXsdSchema(Object stub, String resName) throws Exception;

    public abstract void resetUniverseUser(Object stub, String userName);

}
