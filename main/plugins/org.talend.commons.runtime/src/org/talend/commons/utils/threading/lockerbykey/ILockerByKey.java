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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DOC amaumont class global comment. Detailled comment
 */
public interface ILockerByKey<KP> {

    public abstract int getCleanPeriod();

    public abstract void shutdownNow();

    public abstract void shutdown();

    public abstract LockerValue<KP> getLockerValue(KP key);

    public abstract boolean unlock(KP key);

    public abstract boolean tryLock(KP key, long timeout, TimeUnit unit) throws InterruptedException;

    public abstract boolean tryLock(KP key, long timeout) throws InterruptedException;

    public abstract boolean tryLock(KP key);

    public abstract void lockInterruptibly(KP key) throws InterruptedException;

    public abstract boolean isLocked(KP key);

    public abstract void clean();

    public abstract List<LockerValue<KP>> getSuspectLocks(long timeDetectionLimitMs);

    public abstract void setDetectSuspectLocks(boolean detectSuspectLocks);

    public abstract boolean isDetectSuspectLocks();

}
