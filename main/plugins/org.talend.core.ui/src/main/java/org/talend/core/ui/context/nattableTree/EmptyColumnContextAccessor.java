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
package org.talend.core.ui.context.nattableTree;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;

/**
 * created by ldong on Jul 24, 2014 Detailled comment
 * 
 */
public class EmptyColumnContextAccessor<R> implements IColumnPropertyAccessor<R> {

    private static final Log log = LogFactory.getLog(ReflectiveColumnPropertyAccessor.class);

    private final List<String> propertyNames;

    public EmptyColumnContextAccessor(String[] propertyNames) {
        this.propertyNames = Arrays.asList(propertyNames);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return propertyNames.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#getDataValue(java.lang.Object, int)
     */
    @Override
    public Object getDataValue(R arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#setDataValue(java.lang.Object, int,
     * java.lang.Object)
     */
    @Override
    public void setDataValue(R arg0, int arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.data.IColumnPropertyResolver#getColumnIndex(java.lang.String)
     */
    @Override
    public int getColumnIndex(String propertyName) {
        return propertyNames.indexOf(propertyName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.nebula.widgets.nattable.data.IColumnPropertyResolver#getColumnProperty(int)
     */
    @Override
    public String getColumnProperty(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
