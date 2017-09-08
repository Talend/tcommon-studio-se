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

import org.eclipse.swt.graphics.Image;

/**
 * <code>IColumnImageProvider</code> provide a method which return the image for a given bean of the table (element)
 * and the current column. <br/>
 * 
 * $Id: IColumnImageProvider.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 * @param <B> element bean of the <code>TableViewerCreator</code>
 */
public interface IColumnImageProvider<B> {

    /**
     * Get the image.
     * 
     * @param element bean of the <code>TableViewerCreator</code>
     * @return return the image for the current given bean of the <code>TableViewerCreator</code> and the current
     * column
     */
    public Image getImage(B bean);

}
