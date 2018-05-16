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

import org.talend.core.model.properties.Item;
import org.talend.migration.IMigrationTask.ExecutionResult;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IMigrateDIMetadataItemService extends IService {

    /**
     * DOC bZhou Comment method "migrateDIItems".
     * 
     * This method to invode the migration task in DI to do migration when import items from DQ perspective.
     * 
     * It should be removed after merge the migrate mechanism.
     * 
     * @param item
     * @return
     */
    ExecutionResult migrateDIItems(Item item);
}
