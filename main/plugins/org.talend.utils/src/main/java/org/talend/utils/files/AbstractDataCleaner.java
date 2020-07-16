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
package org.talend.utils.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.utils.thread.ThreadUtils;

/**
 *
 * DOC amaumont class global comment. Detailled comment
 */
public abstract class AbstractDataCleaner {

    private static Logger log = LoggerFactory.getLogger(AbstractDataCleaner.class);

    private String cleanerLabel;

    private Thread threadCleaner;

    private int frequencyCleaningAction;

    private boolean stop;

    public AbstractDataCleaner(String cleanerLabel, int frequencyCleaningAction) {
        super();
        this.cleanerLabel = cleanerLabel;
        this.frequencyCleaningAction = frequencyCleaningAction;
    }

    /**
     *
     * DOC amaumont Comment method "start".
     *
     * @return
     */
    public boolean start() {

        if (frequencyCleaningAction > 0) {

            threadCleaner = new Thread(cleanerLabel) {

                /*
                 * (non-Javadoc)
                 *
                 * @see java.lang.Thread#run()
                 */
                @Override
                public void run() {
                    cleanLoop();
                }

            };
            threadCleaner.start();
            return true;
        } else {
            return false;
        }
    }

    public void stop() {
        stop = true;
        if (threadCleaner != null) {
            threadCleaner.interrupt();
        }
    }

    private void cleanLoop() {
        log.info(cleanerLabel + " started.");
        while (!stop) {
            try {
                clean();
            } catch (Throwable e) {
                if (log.isDebugEnabled()) {
                    log.debug(e.getMessage(), e);
                } else {
                    log.warn(e.getMessage());
                }
            }
            ThreadUtils.waitTimeBool(frequencyCleaningAction * 1000);
        }
        log.info(cleanerLabel + " stopped.");
    }

    protected abstract void clean();

    /**
     * Getter for frequencyCleaningAction.
     *
     * @return the frequencyCleaningAction
     */
    public int getFrequencyCleaningAction() {
        return this.frequencyCleaningAction;
    }

    /**
     * Sets the frequencyCleaningAction.
     *
     * @param frequencyCleaningAction the frequencyCleaningAction to set
     */
    public void setFrequencyCleaningAction(int frequencyCleaningAction) {
        this.frequencyCleaningAction = frequencyCleaningAction;
    }

}
