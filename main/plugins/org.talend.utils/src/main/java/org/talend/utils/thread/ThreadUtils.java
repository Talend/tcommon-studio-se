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
package org.talend.utils.thread;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 *
 */
public class ThreadUtils {

    /**
     *
     * "waitTimeBool" wait for the given time.
     *
     * @param object wait() will be start on this given object
     * @param time
     * @return true if interrupted, else false
     */
    public static boolean waitTimeBool(Object object, long time) {
        synchronized (object) {
            try {
                object.wait(time);
            } catch (InterruptedException e) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * "waitTimeBool" wait for the given time.
     *
     * @param time
     * @return true if interrupted, else false
     */
    public static boolean waitTimeBool(long time) {
        synchronized (Thread.currentThread()) {
            try {
                Thread.currentThread().wait(time);
            } catch (InterruptedException e) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * "waitTimeBool" wait for the given time.
     *
     * @param object wait() will be start on this given object
     * @param time
     * @throws InterruptedException when interrupted
     */
    public static void waitTimeExcept(Object object, long time) throws InterruptedException {
        synchronized (object) {
            object.wait(time);
        }
    }

    /**
     *
     * "waitTimeBool" wait for the given time.
     *
     * @param time
     * @throws InterruptedException when interrupted
     */
    public static void waitTimeExcept(long time) throws InterruptedException {
        synchronized (Thread.currentThread()) {
            Thread.currentThread().wait(time);
        }
    }

}
