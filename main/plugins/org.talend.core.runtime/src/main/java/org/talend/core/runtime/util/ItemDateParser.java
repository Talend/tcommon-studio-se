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
package org.talend.core.runtime.util;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.repository.item.ItemProductValuesHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ItemDateParser {

    public static Date parseAdditionalDate(Property property, String key) {
        if (property == null || key == null) {
            return null;
        }
        EMap additionalProperties = property.getAdditionalProperties();
        if (!additionalProperties.containsKey(key)) {
            return null;
        }
        Object object = additionalProperties.get(key);
        if (object == null) {
            return null;
        }
        try {
            Date date = ItemProductValuesHelper.DATEFORMAT.parse(object.toString());
            return date;
        } catch (ParseException e) {
            //
        }
        return null;
    }

    public static Date parseAdditionalDate(Map<Object, Object> additionalProperties, String key) {
        if (additionalProperties == null || key == null) {
            return null;
        }
        if (!additionalProperties.containsKey(key)) {
            return null;
        }
        Object object = additionalProperties.get(key);
        if (object == null) {
            return null;
        }

        try {
            Date date = ItemProductValuesHelper.DATEFORMAT.parse(object.toString());
            return date;
        } catch (ParseException e) {
            //
        }
        return null;
    }
}
