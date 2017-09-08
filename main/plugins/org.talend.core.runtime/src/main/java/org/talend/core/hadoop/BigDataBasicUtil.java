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
package org.talend.core.hadoop;

import org.eclipse.emf.common.util.EMap;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;

/**
 * created by cmeng on Jul 20, 2015 Detailled comment
 *
 */
public class BigDataBasicUtil {

    public static Object getFramework(Item item) {
        if (item == null) {
            return null;
        }
        Property property = item.getProperty();
        if (property != null) {
            EMap additionalProperties = property.getAdditionalProperties();
            if (additionalProperties != null) {
                return additionalProperties.get(HadoopConstants.FRAMEWORK);
            }
        }
        return null;
    }
}
