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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;


/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40Z nrousseau $
 * 
 * @param <B> element bean of the <code>TableViewerCreator</code>
 */
public interface IColumnLabelProvider<B> {

    /**
     * Get the label to display function of current bean properties.
     * 
     * @param element bean of the <code>TableViewerCreator</code>
     * @return return the text for the current given bean of the <code>TableViewerCreator</code> and the current
     * column. If the null value is returned, getColumnText() is used as normally.
     */
    public String getLabel(B bean);

}
