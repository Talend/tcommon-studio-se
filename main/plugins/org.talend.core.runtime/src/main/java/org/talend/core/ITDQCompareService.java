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
package org.talend.core;

import org.talend.core.model.properties.ConnectionItem;
import org.talend.utils.sugars.ReturnCode;

/**
 * created by talend on Dec 19, 2012 Detailled comment
 * 
 */
public interface ITDQCompareService extends IService {

    // ADD zshen 2012-12-19 to reuse reloadAction
    public ReturnCode reloadDatabase(ConnectionItem connectionItem);
}
