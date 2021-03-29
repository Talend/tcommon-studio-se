// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.runtime.services.IGenericDBService;

/**
 * created by ldong on Mar 23, 2015 Detailled comment
 *
 */
public abstract class AbstractRepositoryContextUpdateService implements IRepositoryContextUpdateService {
    protected IGenericDBService service = GlobalServiceRegister.getDefault().getService(IGenericDBService.class);
    protected String updateHadoopProperties(List<Map<String, Object>> hadoopProperties, String oldValue, String newValue) {
        String finalProperties = null;
        boolean isModified = false;
        if (!hadoopProperties.isEmpty()) {
            for (Map<String, Object> propertyMap : hadoopProperties) {
                String propertyValue = (String) propertyMap.get("VALUE");
                if (propertyValue.equals(oldValue)) {
                    propertyMap.put("VALUE", newValue);
                    isModified = true;
                }
            }
            if (isModified) {
                finalProperties = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(hadoopProperties);
            }
        }
        return finalProperties;
    }
    
    protected boolean updateCompPropertiesContextParameter(Connection conn, String oldValue, String newValue) {
        boolean isModified = false;
        Map<String, String> oldToNewHM = new HashMap<String, String>();
        oldToNewHM.put(oldValue, newValue);
        String compProperties = conn.getCompProperties();
        if (service != null && StringUtils.isNotBlank(compProperties)) {
            service.updateCompPropertiesForContextMode(conn, oldToNewHM);
            isModified = true;
        }
        return isModified;
    }

}
