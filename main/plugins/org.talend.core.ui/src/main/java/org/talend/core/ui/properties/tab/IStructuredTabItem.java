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
package org.talend.core.ui.properties.tab;

import org.eclipse.ui.views.properties.tabbed.ITabItem;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public interface IStructuredTabItem extends ITabItem {

    public static final boolean EXPANDED = true;

    public static final boolean COLLAPSED = false;

    public static final boolean SUBITEM = true;

    public static final boolean NONE_SUBITEM = false;

    public IStructuredTabItem[] getSubItems();

    public boolean hasSubItems();

    public void setExpanded(boolean expaned);

    public boolean isExpanded();

    public void setSubItem(boolean subItem);

    public boolean isSubItem();

}
