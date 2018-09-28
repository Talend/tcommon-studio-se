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
package org.talend.core.service;

import java.util.List;

import org.talend.core.IService;
import org.talend.core.model.properties.Item;


/**
 * created by hcyi on Sep 25, 2018
 * Detailled comment
 *
 */
public interface IMetadataBridgeService extends IService {

    public boolean exportMetadataBridge(List<Item> items, String filter, String directory);
}
