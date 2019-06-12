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
public class SparkRunStat extends MRRunStat {

    private List<StatBean> messages = new ArrayList<>();

    public SparkRunStat() {
        super();
    }

    public class StatBean {

        private String connectionId;

        private int mode;

        private float progress;

        private int jobId;

        public String getConnectionId() {
            return this.connectionId;
        }

        public void setConnectionId(String connectionId) {
            this.connectionId = connectionId;
        }

        public int getJobId() {
            return this.jobId;
        }

        public void setJobId(int jobId) {
            this.jobId = jobId;
        }

        public int getMode() {
            return this.mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public float getProgress() {
            return this.progress;
        }

        public void setProgress(float progress) {
            this.progress = progress;
        }

        public List<String> toStatFormat() {
            List<String> stats = new ArrayList<>();
            for (String connectionId : this.connectionId.split(";")) {
                stats.add("1|" + rootPid + "|" + fatherPid + "|" + pid + "|" + connectionId + "|" + this.jobId + "|" + this.mode
                        + "|" + this.progress);
            }
            return stats;
        }

    }

    public StatBean createSparkStatBean() {
        return new StatBean();
    }

    @Override
    public void sendMessages() {
        for (StatBean message : messages) {
            for (String stat : message.toStatFormat()) {
                pred.println(stat);
            }
        }
        messages.clear();
    }

    public synchronized void updateSparkProgress(StatBean message) {
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
