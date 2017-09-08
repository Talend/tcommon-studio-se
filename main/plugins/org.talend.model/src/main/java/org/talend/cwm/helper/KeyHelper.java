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

package org.talend.cwm.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdColumn;
import orgomg.cwm.objectmodel.core.StructuralFeature;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.PrimaryKey;

/**
 * this provides a set of helpers static method to deal with Primary and Foreign key of the talend DB metamodel <br/>
 * 
 * created on 29 avr. 2010 by sgandon
 */
public class KeyHelper {

    /**
     * return the list of columns associated with the given key
     * 
     * @param theKey to find the associated columns
     * @return list of the column related to this key
     */
    public static List<TdColumn> getKeyRelatedColumns(PrimaryKey theKey) {
        List<TdColumn> result = new ArrayList<TdColumn>(theKey.getFeature().size());
        EList<StructuralFeature> features = theKey.getFeature();
        for (StructuralFeature feature : features) {
            TdColumn column = SwitchHelpers.COLUMN_SWITCH.doSwitch(feature);
            if (column != null) {
                result.add(column);
            } // else keep going
        }
        return result;
    }

    /**
     * return the list of columns associated with the given key
     * 
     * @param theKey to find the associated columns
     * @return list of the column related to this key
     */
    public static List<TdColumn> getKeyRelatedColumns(ForeignKey key) {
        // unfotunatly the feature attribute does not belong to some common classes between Foreign and Primary key
        // This is why this code is duplicated
        List<TdColumn> result = new ArrayList<TdColumn>(key.getFeature().size());
        EList<StructuralFeature> features = key.getFeature();
        for (StructuralFeature feature : features) {
            TdColumn column = SwitchHelpers.COLUMN_SWITCH.doSwitch(feature);
            if (column != null) {
                result.add(column);
            } // else keep going
        }
        return result;
    }
}
