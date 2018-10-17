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
package org.talend.updates.runtime.ui.feature.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.feature.model.IFeatureItem;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class ModelAdapter {

    public static Collection<IFeatureItem> convert(Collection<ExtraFeature> featureList, boolean toUpdate) {
        if (featureList == null || featureList.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Collection<IFeatureItem> featureItems = new LinkedHashSet<>();

        Iterator<ExtraFeature> iterator = featureList.iterator();
        while (iterator.hasNext()) {
            ExtraFeature next = iterator.next();
            IFeatureItem convert = convert(next, toUpdate);
            if (convert != null) {
                featureItems.add(convert);
            }
        }

        return featureItems;
    }

    public static IFeatureItem convert(ExtraFeature feature, boolean toUpdate) {
        if (feature == null) {
            return null;
        }
        AbstractFeatureInfo featureItem = null;
        if (toUpdate) {
            featureItem = new FeatureUpdate();
        } else {
            featureItem = new FeatureDetail();
        }

        featureItem.setFeature(feature);
        featureItem.setTitle(feature.getName());
        featureItem.setDescription(feature.getDescription());

        return featureItem;
    }

}
