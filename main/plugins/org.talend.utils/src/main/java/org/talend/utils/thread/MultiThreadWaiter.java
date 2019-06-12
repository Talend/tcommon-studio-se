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

import java.util.List;

/**
 *
 * DOC amaumont class global comment. Detailled comment
 */
public class MultiThreadWaiter {

    private List<? extends Thread> threads;

    /**
     *
     * DOC amaumont MultiThreadWaiter constructor comment.
     *
     * @param threadsToStart list of the unstarted threads
     */
    public MultiThreadWaiter(List<? extends Thread> threadsToStart) {
        super();
        this.threads = threadsToStart;
    }

    public void start() {
        int iterableListSize = threads.size();
        for (int i = 0; i < iterableListSize; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < iterableListSize; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                // nothing
            }
        }
    }

}
