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
package routines.system;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * created by bchen on Jul 24, 2013 Detailled comment
 * 
 */
public class MRRunStat implements Runnable {

    private static boolean debug = false;

    private java.net.Socket s;

    protected java.io.PrintWriter pred;

    private boolean jobIsFinished = false;

    private List<StatBean> messages = new ArrayList<>();

    public class StatBean {

        private int groupID;

        private int mrJobID;

        private float mapProgress;

        private float reduceProgress;

        /**
         * Getter for groupID.
         * 
         * @return the groupID
         */
        public int getGroupID() {
            return this.groupID;
        }

        /**
         * Sets the groupID.
         * 
         * @param groupID the groupID to set
         */
        public void setGroupID(int groupID) {
            this.groupID = groupID;
        }

        /**
         * Getter for mrJobID.
         * 
         * @return the mrJobID
         */
        public int getMRJobID() {
            return this.mrJobID;
        }

        /**
         * Sets the mrJobID.
         * 
         * @param mrJobID the mrJobID to set
         */
        public void setMRJobID(int mrJobID) {
            this.mrJobID = mrJobID;
        }

        /**
         * Getter for mapProgress.
         * 
         * @return the mapProgress
         */
        public float getMapProgress() {
            return this.mapProgress;
        }

        /**
         * Sets the mapProgress.
         * 
         * @param mapProgress the mapProgress to set
         */
        public void setMapProgress(float mapProgress) {
            this.mapProgress = mapProgress;
        }

        /**
         * Getter for reduceProgress.
         * 
         * @return the reduceProgress
         */
        public float getReduceProgress() {
            return this.reduceProgress;
        }

        /**
         * Sets the reduceProgress.
         * 
         * @param reduceProgress the reduceProgress to set
         */
        public void setReduceProgress(float reduceProgress) {
            this.reduceProgress = reduceProgress;
        }

        /**
         * DOC bchen Comment method "toStatFormat".
         * 
         * @return
         */
        public String toStatFormat() {
            return this.groupID + "|" + this.mrJobID + "|" + this.mapProgress + "|" + this.reduceProgress;
        }

    }

    public StatBean createStatBean() {
        return new StatBean();
    }

    public void startThreadStat(String clientHost, int portStats) throws java.io.IOException, java.net.UnknownHostException {

        System.out.println("[statistics] connecting to socket on port " + portStats); //$NON-NLS-1$
        boolean isConnect = false;
        OutputStream output = null;
        try {
            s = new Socket(clientHost, portStats);
            isConnect = true;
        } catch (Exception e) {
            System.err.println("Unable to connect to " + clientHost + " on the port " + portStats);
        }
        if (isConnect) {
            output = s.getOutputStream();
            System.out.println("[statistics] connected"); //$NON-NLS-1$
        } else {
            output = System.out;
            System.out.println("[statistics] connection refused"); //$NON-NLS-1$
        }
        if (debug) {
            output = System.out;
        }
        pred = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.OutputStreamWriter(output)), true);
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        if (!debug) {
            synchronized (this) {
                try {
                    while (!jobIsFinished) {
                        sendMessages();
                        wait(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("[statistics] interrupted"); //$NON-NLS-1$
                }
            }
        }
    }

    public void stopThreadStat() {
        jobIsFinished = true;
        try {
            sendMessages();
            if (pred != null) {
                pred.close();
            }
            if (s != null && !s.isClosed()) {
                s.close();
            }
            System.out.println("[statistics] disconnected"); //$NON-NLS-1$
        } catch (java.io.IOException ie) {
        }
    }

    public void sendMessages() {
        for (StatBean message : messages) {
            pred.println(message.toStatFormat());
        }
        messages.clear();
    }

    public synchronized void updateMRProgress(StatBean message) {
        messages.add(message);
        if (debug) {
            sendMessages();
        }
    }
}
