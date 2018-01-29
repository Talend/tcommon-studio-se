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
package org.talend.commons.ui.runtime.swt.proposal;

import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.widgets.Control;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: IControlContentAdapterExtended.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public interface IControlContentAdapterExtended extends IControlContentAdapter {

    /**
     * 
     * Return the last word before the cursor. If control contains "test filter value" and cursor is at offset 8, The
     * filter value is "fil".
     * 
     * @param control
     * @return return the last word before the cursor.
     */
    public String getFilterValue(Control control);

    /**
     * Proposal set the used filter.
     * 
     * @param filterValue
     */
    public void setUsedFilterValue(String filterValue);

}
