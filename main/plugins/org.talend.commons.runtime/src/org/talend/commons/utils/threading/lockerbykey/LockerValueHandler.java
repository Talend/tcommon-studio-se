// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.commons.utils.threading.lockerbykey;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Future;

/**
 * class LockerValueHandler.
 */
public class LockerValueHandler {

    private Future<Boolean> future;

    private CyclicBarrier barrier;

    private Thread callerThreadLocker;

    public LockerValueHandler(Future<Boolean> future, CyclicBarrier barrier, Thread callerThreadLocker) {
        super();
        this.future = future;
        this.barrier = barrier;
        this.callerThreadLocker = callerThreadLocker;
    }

    /**
     * Getter for future.
     *
     * @return the future
     */
    public Future<Boolean> getFuture() {
        return future;
    }

    /**
     * Getter for barrier.
     *
     * @return the barrier
     */
    public CyclicBarrier getBarrier() {
        return barrier;
    }

    /**
     * Getter for callerThreadLocker.
     *
     * @return the callerThreadLocker
     */
    public Thread getCallerThreadLocker() {
        return callerThreadLocker;
    }

}
