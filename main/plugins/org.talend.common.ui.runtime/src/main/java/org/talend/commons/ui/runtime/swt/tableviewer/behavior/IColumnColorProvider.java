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

import org.eclipse.swt.graphics.Color;

/**
 * <code>IColumnColorProvider</code> provide a method which return the background color and the foreground color for a
 * given bean of the table (element) and the current column. <br/>
 * 
 * $Id: IColumnImageProvider.java 1155 2006-12-21 09:51:10Z amaumont $
 * 
 * @param <B> element bean of the <code>TableViewerCreator</code>
 */
public interface IColumnColorProvider<B> {

    /**
     * Get the background color.
     * 
     * @param element bean of the <code>TableViewerCreator</code>
     * @return return the background color for the current given bean of the <code>TableViewerCreator</code> and the
     * current column
     */
    public Color getBackgroundColor(B bean);

    /**
     * Get the foreground color.
     * 
     * @param element bean of the <code>TableViewerCreator</code>
     * @return return the foreground color for the current given bean of the <code>TableViewerCreator</code> and the
     * current column
     */
    public Color getForegroundColor(B bean);

}
