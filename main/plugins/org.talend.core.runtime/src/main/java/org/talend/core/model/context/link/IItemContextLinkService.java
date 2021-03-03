// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.context.link;

import java.io.InputStream;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;

public interface IItemContextLinkService {

    boolean accept(Item item);

    boolean saveItemLink(Item item) throws PersistenceException;

    ItemContextLink loadItemLink(Item item) throws PersistenceException;

    boolean mergeItemLink(Item item, ItemContextLink backupContextLink, InputStream otherVersionLinkFile)
            throws PersistenceException;

}
