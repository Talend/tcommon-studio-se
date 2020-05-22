// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.update;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.AbstractRepositoryContextUpdateService;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.runtime.services.IGenericDBService;

public class GenericDbContextUpdateService extends AbstractRepositoryContextUpdateService {

    IGenericDBService service = GlobalServiceRegister.getDefault().getService(IGenericDBService.class);

    @Override
    public boolean accept(Connection connection) {
        return connection.getCompProperties() != null;
    }

    @Override
    public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
        boolean isModified = false;
        Map<String, String> oldToNewHM = new HashMap<String, String>();
        oldToNewHM.put(oldValue, newValue);
        String compProperties = conn.getCompProperties();
        if (StringUtils.isNotBlank(compProperties)) {
            service.updateCompPropertiesForContextMode(conn, oldToNewHM);
            isModified = true;
        }
        return isModified;
    }

}
