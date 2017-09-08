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
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdView;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class ViewHelper {

    private ViewHelper() {
    }

    public static boolean addColumns(TdView view, Collection<TdColumn> columns) {
        assert view != null;
        assert columns != null;
        return view.getFeature().addAll(columns);
    }

    /**
     * Method "getColumns".
     * 
     * @param view
     * @return the columns of the view
     */
    public static List<TdColumn> getColumns(TdView view) {
        return ColumnHelper.getColumns(view.getFeature());
    }

    /**
     * Method "getViews" extracts the views from the list.
     * 
     * @param elements any elements that could contain TdView
     * @return the list of TdView found in the given list (never null, but can be empty).
     */
    public static List<TdView> getViews(Collection<? extends EObject> elements) {
        List<TdView> views = new ArrayList<TdView>();
        for (EObject elt : elements) {
            TdView table = SwitchHelpers.VIEW_SWITCH.doSwitch(elt);
            if (table != null) {
                views.add(table);
            }
        }
        return views;
    }

}
