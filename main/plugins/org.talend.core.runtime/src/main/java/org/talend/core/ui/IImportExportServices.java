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
package org.talend.core.ui;

import org.talend.core.IService;
import org.talend.core.model.properties.Item;

/**
 * created by Talend on Dec 30, 2015 Detailled comment
 *
 */
public interface IImportExportServices extends IService {

    public Item getItem(Object importNode);

}
