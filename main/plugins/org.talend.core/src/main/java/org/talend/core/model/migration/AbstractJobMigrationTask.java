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
package org.talend.core.model.migration;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC stephane class global comment. Detailled comment
 */
public abstract class AbstractJobMigrationTask extends AbstractItemMigrationTask {

    @Override
    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.PROCESS);
        toReturn.add(ERepositoryObjectType.JOBLET);
        toReturn.add(ERepositoryObjectType.TEST_CONTAINER);
        return toReturn;
    }

    public ProcessType getProcessType(Item item) {
        ProcessType processType = null;
        if (item instanceof ProcessItem) {
            processType = ((ProcessItem) item).getProcess();
        }
        if (item instanceof JobletProcessItem) {
            processType = ((JobletProcessItem) item).getJobletProcess();
        }
        if (processType != null) {
            EmfHelper.visitChilds(processType);
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
            if (itemType == ERepositoryObjectType.TEST_CONTAINER
                    && !ConvertJobsUtil.JobType.STANDARD.getDisplayName()
                            .equalsIgnoreCase(getTestContainerJobType(item, processType))) {
                return null;
            }

        }
        return processType;
    }

    protected String getTestContainerJobType(Item item, ProcessType processType) {
        if (item.getState() != null && item.getState().getPath() != null) {
            return ConvertJobsUtil.getTestCaseJobTypeByPath(item.getState().getPath());
        }
        return processType.getJobType();
    }
}
