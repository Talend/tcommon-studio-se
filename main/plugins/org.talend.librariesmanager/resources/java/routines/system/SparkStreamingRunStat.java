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
package routines.system;

import java.util.ArrayList;
import java.util.List;

/**
 * created by bchen on Jul 24, 2013 Detailled comment
 *
 */
public class SparkStreamingRunStat extends MRRunStat {

    protected List<StatBean> messages = new ArrayList<>();

    public SparkStreamingRunStat() {
        super();
    }

    public class StatBean {

        private String subjobId;

        private int batchCompleted;

        private int batchStarted;

        private String lastProcessingDelay;

        private String lastSchedulingDelay;

        private String lastTotalDelay;

        public String getSubjobId() {
            return this.subjobId;
        }

        public void setSubjobId(String subjobId) {
            this.subjobId = subjobId;
        }

        public int getBatchCompleted() {
            return this.batchCompleted;
        }

        public void setBatchCompleted(int batchCompleted) {
            this.batchCompleted = batchCompleted;
        }

        public int getBatchStarted() {
            return this.batchStarted;
        }

        public void setBatchStarted(int batchStarted) {
            this.batchStarted = batchStarted;
        }

        public String getLastProcessingDelay() {
            return this.lastProcessingDelay;
        }

        public void setLastProcessingDelay(String lastProcessingDelay) {
            this.lastProcessingDelay = lastProcessingDelay;
        }

        public String getLastSchedulingDelay() {
            return this.lastSchedulingDelay;
        }

        public void setLastSchedulingDelay(String lastSchedulingDelay) {
            this.lastSchedulingDelay = lastSchedulingDelay;
        }

        public String getLastTotalDelay() {
            return this.lastTotalDelay;
        }

        public void setLastTotalDelay(String lastTotalDelay) {
            this.lastTotalDelay = lastTotalDelay;
        }

        public String toStatFormat() {
            String stats = rootPid + "|" + fatherPid + "|" + pid + "|" + this.subjobId + "|" + this.batchCompleted + "|"
                    + this.batchStarted + "|" + this.lastProcessingDelay + "|" + this.lastSchedulingDelay + "|"
                    + this.lastTotalDelay;

            return stats;
        }
    }

    public StatBean createSparkStreamingStatBean() {
        return new StatBean();
    }

    @Override
    public void sendMessages() {
        for (StatBean message : messages) {
            pred.println(message.toStatFormat());
        }
        messages.clear();
    }

    public synchronized void updateSparkStreamingData(StatBean message) {
        messages.add(message);
    }

    // for feature:10589
    private String rootPid = null;

    private String fatherPid = null;

    private String pid = "0";

    // Notice: this API should be invoked after startThreadStat() closely.
    public void setAllPID(String rootPid, String fatherPid, String pid) {
        this.rootPid = rootPid;
        this.fatherPid = fatherPid;
        this.pid = pid;
    }
}
