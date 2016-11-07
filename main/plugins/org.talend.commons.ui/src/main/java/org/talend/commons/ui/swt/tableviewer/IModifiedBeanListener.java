// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.swt.tableviewer;

/**
 * DOC amaumont class global comment. Detailed comment <br/>
 * 
 * $Id$
 * 
 * @param <B> Type of beans
 */
public interface IModifiedBeanListener<B> {

    public void handleEvent(ModifiedBeanEvent<B> event);

}
