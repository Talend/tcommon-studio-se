package org.talend.core.model.context.link;

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
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;


public class ProcessItemContextLinkService extends AbstractItemContextLinkService {

    @Override
    public boolean accept(Item item) {
        if (item instanceof ProcessItem || item instanceof JobletProcessItem) {
            return true;
        }
        return false;
    }

    @Override
    public boolean saveItemLink(Item item) throws PersistenceException {
        if (item instanceof ProcessItem) {
            ProcessItem processItem = (ProcessItem) item;
            return saveContextLink(processItem.getProcess().getContext(), item);
        } else if (item instanceof JobletProcessItem) {
            JobletProcessItem jobletItem = (JobletProcessItem) item;
            return saveContextLink(jobletItem.getJobletProcess().getContext(), item);
        }
        return false;
    }

    public ItemContextLink loadItemLink(Item item) throws PersistenceException {
        return ContextLinkService.getInstance().doLoadContextLinkFromJson(item);
    }

}
