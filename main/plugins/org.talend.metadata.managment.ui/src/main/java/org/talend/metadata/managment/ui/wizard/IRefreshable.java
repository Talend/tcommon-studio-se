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
package org.talend.metadata.managment.ui.wizard;

/**
 * Composite which support refreshing.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: I.java 上午10:23:38 2007-6-15 +0000 (2007-6-15) yzhang $
 * 
 */
public interface IRefreshable {

    /**
     * Refresh the concrete composite.
     * 
     * yzhang Comment method "refresh".
     */
    public void refresh();

}
