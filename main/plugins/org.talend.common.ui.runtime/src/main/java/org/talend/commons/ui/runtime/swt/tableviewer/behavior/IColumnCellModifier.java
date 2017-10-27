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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * @param <B> bean
 */
public interface IColumnCellModifier<B> {

    /**
     * 
     * Can modify the value at current column in the given bean.
     * 
     * @param bean
     * @return true if can be modified
     */
    public boolean canModify(B bean);

    /**
     * 
     * Get the value at current column from the given bean.
     * 
     * @param bean
     * @param value
     * @return value at current column in the given bean, if null the DefaultCellModifier will try to get the value.
     */
    public Object getValue(B bean);

    /**
     * 
     * DOC amaumont Comment method "modify".
     * 
     * @param bean
     * @param value
     * @return true if modify has modified bean, else false so DefaultCellModifier will process the modification.
     */
    public boolean modify(B bean, Object value);

}
