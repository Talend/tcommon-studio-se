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
package org.talend.core.model.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiKeyMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by ggu on Mar 28, 2014 Detailled comment
 * 
 */
public class RepositoryUpdateManagerHelper {

    private RepositoryUpdateManager manager;

    public RepositoryUpdateManagerHelper() {
        this(null);
    }

    public RepositoryUpdateManagerHelper(RepositoryUpdateManager manager) {
        this.manager = manager;
    }

    protected boolean enableCheckItem() {
        return false; // by default, no items
    }

    protected List<Relation> getRelations() {
        return null;
    }

    protected List<UpdateResult> getOtherUpdateResults(IProgressMonitor parentMonitor, List<IProcess2> openedProcessList,
            final Set<IUpdateItemType> types) {
        return Collections.emptyList(); // no, default
    }

    protected void checkAndSetParameters(IProcess2 process2) {

    }

    // ------------------------------------------------------------------------------

    public List<UpdateResult> checkJobItemsForUpdate(IProgressMonitor parentMonitor, final Set<IUpdateItemType> types) {
        if (types == null || types.isEmpty()) {
            return null;
        }

        List<IProcess2> openedProcessList = CoreRuntimePlugin.getInstance().getDesignerCoreService()
                .getOpenedProcess(RepositoryUpdateManager.getEditors());

        try {
            List<UpdateResult> resultList = new ArrayList<UpdateResult>();
            if (enableCheckItem()) {
                // closed job
                resultList.addAll(checkJobItems(parentMonitor, types, openedProcessList));
            }
            // opened job
            for (IProcess2 process : openedProcessList) {
                checkMonitorCanceled(parentMonitor);
                parentMonitor.subTask(RepositoryUpdateManager.getUpdateJobInfor(process.getProperty()));

                List<UpdateResult> resultFromProcess = getResultFromProcess(process, types);
                if (resultFromProcess != null) {
                    resultList.addAll(resultFromProcess);
                }
                parentMonitor.worked(1);
            }
            List<UpdateResult> otherUpdateResults = getOtherUpdateResults(parentMonitor, openedProcessList, types);
            if (otherUpdateResults != null) {
                resultList.addAll(otherUpdateResults);
            }
            parentMonitor.done();
            return resultList;
        } catch (PersistenceException e) {
            //
        }

        return null;
    }

    private boolean isOpenedItem(Item openedItem, MultiKeyMap openProcessMap) {
        if (openedItem == null) {
            return false;
        }
        Property property = openedItem.getProperty();
        return (openProcessMap.get(property.getId(), property.getLabel(), property.getVersion()) != null);
    }

    private List<UpdateResult> checkJobItems(IProgressMonitor monitor, final Set<IUpdateItemType> types,
            List<IProcess2> openedProcessList) throws PersistenceException {
        List<Relation> relations = getRelations();
        if (relations == null) {
            return Collections.emptyList();
        }
        List<UpdateResult> resultList = new ArrayList<UpdateResult>();

        MultiKeyMap openProcessMap = createOpenProcessMap(openedProcessList);
        int size = openedProcessList.size();
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        size = size + relations.size();
        monitor.beginTask(Messages.getString("RepositoryUpdateManager.Check"), size); //$NON-NLS-1$
        monitor.setTaskName(Messages.getString("RepositoryUpdateManager.ItemsToUpdate")); //$NON-NLS-1$

        for (int i = 0; i < relations.size(); i++) {
            Relation relation = relations.get(i);
            IRepositoryViewObject relatedObj = factory.getLastVersion(relation.getId());

            if (relatedObj == null) {
                continue;
            }
            List<IRepositoryViewObject> allVersionList = new ArrayList<IRepositoryViewObject>();
            //

            // must match TalendDesignerPrefConstants.CHECK_ONLY_LAST_VERSION
            boolean checkOnlyLastVersion = Boolean.parseBoolean(CoreRuntimePlugin.getInstance().getDesignerCoreService()
                    .getPreferenceStore("checkOnlyLastVersion")); //$NON-NLS-1$
            // get all version
            allVersionList = new ArrayList<IRepositoryViewObject>();
            if (!checkOnlyLastVersion) {
                List<IRepositoryViewObject> allVersion = factory.getAllVersion(relatedObj.getId());
                for (IRepositoryViewObject object : allVersion) {
                    if (factory.getStatus(object) != ERepositoryStatus.LOCK_BY_OTHER) {
                        allVersionList.add(object);
                    }
                }
            } else {
                // assume that repositoryObj is the last version, otherwise we should call
                // factory.getLastVersion(repositoryObj.getId());
                IRepositoryViewObject lastVersion = relatedObj; // factory.getLastVersion(repositoryObj.getId());
                ERepositoryStatus status = factory.getStatus(lastVersion);
                if (status != ERepositoryStatus.LOCK_BY_OTHER) {
                    allVersionList.add(lastVersion);
                }
            }

            //

            checkMonitorCanceled(monitor);

            for (IRepositoryViewObject repositoryObj : allVersionList) {
                checkMonitorCanceled(monitor);
                Item item = repositoryObj.getProperty().getItem();
                // avoid the opened job
                if (isOpenedItem(item, openProcessMap)) {
                    continue;
                }
                monitor.subTask(RepositoryUpdateManager.getUpdateJobInfor(repositoryObj.getProperty()));
                List<UpdateResult> updatesNeededFromItems = getUpdatesNeededFromItems(monitor, item, types);
                if (updatesNeededFromItems != null && !updatesNeededFromItems.isEmpty()) {
                    resultList.addAll(updatesNeededFromItems);
                }

                if (!ERepositoryStatus.LOCK_BY_USER.equals(factory.getStatus(item))) {
                    if (repositoryObj instanceof RepositoryObject) {
                        ((RepositoryObject) repositoryObj).unload();
                    }
                }

            }
            monitor.worked(1);
        }
        return resultList;
    }

    protected List<UpdateResult> getUpdatesNeededFromItems(IProgressMonitor parentMonitor, Item item,
            final Set<IUpdateItemType> types) {
        if (item == null || types == null) {
            return null;
        }
        IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
        if (designerCoreService == null) {
            return null;
        }
        //
        IProcess process = null;
        if (item instanceof ProcessItem) {
            process = designerCoreService.getProcessFromProcessItem((ProcessItem) item);
        } else if (item instanceof JobletProcessItem) {
            process = designerCoreService.getProcessFromJobletProcessItem((JobletProcessItem) item);
        }
        //
        if (process != null && process instanceof IProcess2) {
            IProcess2 process2 = (IProcess2) process;
            // for save item
            List<UpdateResult> resultFromProcess = getResultFromProcess(process2, types);

            for (UpdateResult result : resultFromProcess) {
                result.setFromItem(true);
                if (result.getJob() != null) {
                    result.setJob(null);
                }
                result.setObjectId(item.getProperty().getId());
                if (manager != null) {
                    result.setRepositoryUpdateManager(manager);
                }
            }

            process2.dispose();
            return resultFromProcess;

        }
        return null;
    }

    protected List<UpdateResult> getResultFromProcess(IProcess process, final Set<IUpdateItemType> types) {
        if (process == null || types == null) {
            return null;
        }

        List<UpdateResult> resultList = new ArrayList<UpdateResult>();
        if (process instanceof IProcess2) {
            IProcess2 process2 = (IProcess2) process;
            IUpdateManager updateManager = process2.getUpdateManager();

            checkAndSetParameters(process2);
            //
            for (IUpdateItemType type : types) {
                List<UpdateResult> updatesNeeded = updateManager.getUpdatesNeeded(type, false);
                if (updatesNeeded != null) {
                    resultList.addAll(updatesNeeded);
                }
            }
            if (updateManager instanceof AbstractUpdateManager) {
                AbstractUpdateManager manager = (AbstractUpdateManager) updateManager;
                manager.setFromRepository(false);
            }
        }
        return resultList;
    }

    protected List<UpdateResult> updateAllProcess(IProgressMonitor parentMonitor, List<UpdateResult> resultList,
            List<IProcess2> openedProcessList, final Set<IUpdateItemType> types, final boolean onlySimpleShow) {
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        if (factory == null) {
            return resultList;
        }
        IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
        if (designerCoreService == null) {
            return resultList;
        }
        List<IRepositoryViewObject> processRep = new ArrayList<IRepositoryViewObject>();
        try {

            ERepositoryObjectType jobType = ERepositoryObjectType.PROCESS;
            if (jobType != null) {
                processRep.addAll(factory.getAll(jobType, true));
            }
            ERepositoryObjectType jobletType = ERepositoryObjectType.JOBLET;
            if (jobletType != null) {
                processRep.addAll(factory.getAll(jobletType, true));
            }

            ERepositoryObjectType processMr = ERepositoryObjectType.PROCESS_MR;
            if (processMr != null) {
                processRep.addAll(factory.getAll(processMr, true));
            }
            ERepositoryObjectType processStrom = ERepositoryObjectType.PROCESS_STORM;
            if (processStrom != null) {
                processRep.addAll(factory.getAll(processStrom, true));
            }

            ERepositoryObjectType sparkJoblet = ERepositoryObjectType.SPARK_JOBLET;
            if (sparkJoblet != null) {
                processRep.addAll(factory.getAll(sparkJoblet, true));
            }
            ERepositoryObjectType sparkStreamJoblet = ERepositoryObjectType.SPARK_STREAMING_JOBLET;
            if (sparkStreamJoblet != null) {
                processRep.addAll(factory.getAll(sparkStreamJoblet, true));
            }

        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        // all the jobs
        for (IRepositoryViewObject process : processRep) {
            Item item = process.getProperty().getItem();
            boolean found = false;
            for (IProcess2 open : openedProcessList) {
                if (open.getId().equals(item.getProperty().getId())) {
                    found = true;
                }
            }
            if (found) {
                continue;
            }
            checkMonitorCanceled(parentMonitor);
            parentMonitor.subTask(RepositoryUpdateManager.getUpdateJobInfor(item.getProperty()));

            // List<UpdateResult> resultFromProcess = getResultFromProcess(process, types, onlySimpleShow);

            List<UpdateResult> resultFromProcess = getUpdatesNeededFromItems(parentMonitor, item, types);
            if (resultFromProcess != null) {
                resultList.addAll(resultFromProcess);
            }
            parentMonitor.worked(1);
        }
        return resultList;
    }

    /**
     * Create a hashmap for fash lookup of the specified IProcess.
     * 
     * @param openedProcessList
     * @return
     */
    private MultiKeyMap createOpenProcessMap(List<IProcess2> openedProcessList) {
        MultiKeyMap map = new MultiKeyMap();
        if (openedProcessList != null) {
            for (IProcess2 process : openedProcessList) {
                map.put(process.getId(), process.getName(), process.getVersion(), process);
            }
        }
        return map;
    }

    private void checkMonitorCanceled(IProgressMonitor monitor) {
        if (monitor.isCanceled()) {
            throw new OperationCanceledException(UpdatesConstants.MONITOR_IS_CANCELED);
        }
    }
}
