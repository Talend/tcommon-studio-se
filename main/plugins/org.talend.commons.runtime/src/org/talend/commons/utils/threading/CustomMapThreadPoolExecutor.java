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
package org.talend.commons.utils.threading;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

/**
 * created by wchen on 2014-6-6 Detailled comment
 * 
 */
public class CustomMapThreadPoolExecutor extends TalendCustomThreadPoolExecutor {

    // This map is used to store the tableItems that are selected or unselected by the user.
    // see afterExecute() and beforeExecute(). If an item is in the map, it means that the item's
    // related thread is running.
    Map<Object, AbsRetrieveColumnRunnable> map = Collections.synchronizedMap(new HashMap<Object, AbsRetrieveColumnRunnable>());

    public CustomMapThreadPoolExecutor(int queueCapacity) {
        super(queueCapacity);
    }

    public CustomMapThreadPoolExecutor(int queueCapacity, RejectedExecutionHandler handler) {
        super(queueCapacity, handler);
    }

    public CustomMapThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        AbsRetrieveColumnRunnable runnable = (AbsRetrieveColumnRunnable) r;
        map.remove(runnable.getColumnObject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        AbsRetrieveColumnRunnable runnable = (AbsRetrieveColumnRunnable) r;
        map.put(runnable.getColumnObject(), runnable);
    }

    /**
     * If an item is in the List runningThreads, it means that the item's related thread is running.
     * 
     * @param item
     * @return
     */
    public boolean isThreadRunning(Object item) {
        return map.containsKey(item);
    }

    /**
     * Find the RetrieveColumnRunnable from map and waiting queue. Map stores running runnables
     * 
     * @param key
     * @return
     */
    public synchronized AbsRetrieveColumnRunnable getRunnable(Object key) {
        // Get the runnable from map first, else then find it in the waiting queue.
        AbsRetrieveColumnRunnable runnable = map.get(key);
        if (runnable != null) {
            return runnable;
        }
        for (Object element2 : getQueue()) {
            AbsRetrieveColumnRunnable element = (AbsRetrieveColumnRunnable) element2;
            if (element.getColumnObject() == key) {
                return element;
            }
        }
        return null;
    }

}
