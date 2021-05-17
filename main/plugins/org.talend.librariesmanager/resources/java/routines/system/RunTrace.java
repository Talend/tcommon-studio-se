// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RunTrace implements Runnable {

    private boolean openSocket = true;

    public void openSocket(boolean openSocket) {
        this.openSocket = openSocket;
    }

    private java.util.concurrent.ConcurrentHashMap<String, TraceDataBean> processTraces = new java.util.concurrent.ConcurrentHashMap<String, TraceDataBean>();

    private Map<String, String> subjobMap = new HashMap<String, String>();

    private java.net.Socket s;

    private NoHeaderObjectOutputStream oos;

    private NoHeaderObjectInputStream ois;

    private boolean jobIsFinished = false;

    private String str = ""; //$NON-NLS-1$

    private Thread t;

    public void startThreadTrace(String clientHost, int portTraces) throws java.io.IOException, java.net.UnknownHostException {
        if (!openSocket) {
            return;
        }
        System.out.println("[trace] connecting to socket on port " + portTraces); //$NON-NLS-1$
        s = new java.net.Socket(clientHost, portTraces);
        oos = new NoHeaderObjectOutputStream(s.getOutputStream());
        System.out.println("[trace] connected"); //$NON-NLS-1$
        t = new Thread(this);
        t.start();

    }

    public void run() {
        synchronized (this) {
            try {
                while (!jobIsFinished) {
                    wait(100);
                }
            } catch (InterruptedException e) {
                System.out.println("[trace] interrupted"); //$NON-NLS-1$
            }
        }
    }

    public void stopThreadTrace() {
        if (!openSocket) {
            return;
        }
        jobIsFinished = true;
        try {
            oos.close();
            s.close();
            System.out.println("[trace] disconnected"); //$NON-NLS-1$
        } catch (java.io.IOException ie) {
        }
    }

    public synchronized boolean isNextRow() {
        if (!openSocket) {
            return false;
        }
        try {
            askForStatus();
            ois = new NoHeaderObjectInputStream(s.getInputStream(), TraceDataBean.class, TraceStatusBean.class);
            TraceBean traceBean = (TraceBean) ois.readObject();
            return traceBean.equals(TraceStatusBean.NEXT_ROW);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean isNextBreakpoint() {
        if (!openSocket) {
            return false;
        }
        try {
            askForStatus();
            ois = new NoHeaderObjectInputStream(s.getInputStream(), TraceDataBean.class, TraceStatusBean.class);
            TraceBean traceBean = (TraceBean) ois.readObject();
            return traceBean.equals(TraceStatusBean.NEXT_BREAKPOINT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void waitForUserAction() throws InterruptedException {
        if (!openSocket) {
            return;
        }
        try {
            boolean action = false;
            oos.writeObject(TraceStatusBean.UI_STATUS);
            do {
                ois = new NoHeaderObjectInputStream(s.getInputStream(), TraceDataBean.class, TraceStatusBean.class);
                TraceBean traceBean = (TraceBean) ois.readObject();
                if (traceBean.equals(TraceStatusBean.STATUS_WAITING)) {
                    oos.writeObject(TraceStatusBean.UI_STATUS);
                    Thread.sleep(100);
                } else {
                    action = true;
                }
            } while (!action);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isPause() {
        if (!openSocket) {
            return false;
        }
        try {
            askForStatus();
            ois = new NoHeaderObjectInputStream(s.getInputStream(), TraceDataBean.class, TraceStatusBean.class);
            TraceBean traceBean = (TraceBean) ois.readObject();
            return traceBean.equals(TraceStatusBean.PAUSE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private synchronized void askForStatus() throws IOException {
        oos.writeObject(TraceStatusBean.ID_STATUS);
    }

    private String connectionId = "";

    public synchronized void sendTrace(String connectionId, String startNodeCid, LinkedHashMap datas) throws IOException {
        if (!openSocket) {
            return;
        }
        subjobMap.put(connectionId, startNodeCid);
        Iterator<Entry<String, String>> ite = subjobMap.entrySet().iterator();
        boolean sameSub = false;
        while (ite.hasNext()) {
            Entry<String, String> en = ite.next();
            if (en.getKey().equals(connectionId)) {
                continue;
            }
            if (en.getValue().equals(startNodeCid)) {
                sameSub = true;
                break;
            }
        }
        if (sameSub && processTraces.size() > 1) { // if the connections are more than one, will check
            if (connectionId.equals(this.connectionId)) {
                return;
            }
        }
        TraceDataBean bean;
        if (processTraces.containsKey(connectionId)) {
            bean = processTraces.get(connectionId);
        } else {
            bean = new TraceDataBean(connectionId);
        }
        bean.setNbLine(bean.getNbLine() + 1);
        processTraces.put(connectionId, bean);
        bean.setData(datas);

        oos.writeUnshared(bean); // envoi d'un message
        oos.flush();
        this.connectionId = connectionId;
    }
}
