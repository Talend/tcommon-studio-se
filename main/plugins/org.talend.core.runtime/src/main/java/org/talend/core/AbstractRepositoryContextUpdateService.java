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
package org.talend.core;

import java.util.List;
import java.util.Map;

import org.talend.core.hadoop.repository.HadoopRepositoryUtil;

/**
 * created by ldong on Mar 23, 2015 Detailled comment
 *
 */
public abstract class AbstractRepositoryContextUpdateService implements IRepositoryContextUpdateService {

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

}
