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
package org.talend.core.ui.component.preference.provider;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.components.ComponentCategory;

/**
 * created by nrousseau on Aug 11, 2014 Detailled comment
 *
 */
/**
 * created by nrousseau on Aug 11, 2014 Detailled comment
 *
 */
public interface IPaletteItem {

    public ImageDescriptor getImageDesc();

    /**
     * Label who can contain translation.
     * 
     * @return
     */
    public String getLabel();

    public List<IPaletteItem> getChildren();

    /**
     * Get the palette type / component category for the current item.
     * 
     * @return
     */
    public ComponentCategory getPaletteType();

    /**
     * Get the current family the current component. Value empty for other things than component
     * 
     * @return
     */
    public String getFamily();

    public IPaletteItem getParent();

    public void setParent(IPaletteItem paletteItem);

}
