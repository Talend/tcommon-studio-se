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
package org.talend.core.ui;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.properties.Item;

/**
 * created by Talend on Sep 24, 2015 Detailled comment
 *
 */
public interface ITestContainerCoreService extends IService {

    Item getParentJobItem(Item item) throws PersistenceException;

    boolean isValidTestCase(Item item, ComponentCategory category);

    public boolean isStandard(Item item);

    public boolean isSpark(Item item);

    public boolean isSparkStreaming(Item item);
}
