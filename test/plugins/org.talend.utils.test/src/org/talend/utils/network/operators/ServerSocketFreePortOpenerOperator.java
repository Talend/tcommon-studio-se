// ============================================================================
package org.talend.utils.network.operators;

import java.net.ServerSocket;
import java.util.Random;

import org.talend.testutils.threading.IThreadSafetyOperator;
import org.talend.utils.network.ServerSocketFreePortOpener;

public class ServerSocketFreePortOpenerOperator implements IThreadSafetyOperator {

    public final static int portRangeBound1 = 10000;

    public final static int portRangeBound2 = 10010;

    Random random = new Random(System.currentTimeMillis());

    protected ServerSocketFreePortOpener serverSocketFreePortOpener;

    protected int nOperationsByOperator;

    protected boolean debug;

    public ServerSocketFreePortOpenerOperator() {
        super();
    }

    /**
     * Getter for debug.
     * 
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Sets the debug.
     * 
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Sets the locker.
     * 
     * @param serverSocketFreePortOpener the locker to set
     */
    public void setServerSocketFreePortOpener(ServerSocketFreePortOpener serverSocketFreePortOpener) {
        this.serverSocketFreePortOpener = serverSocketFreePortOpener;
    }

    /**
     * Sets the nOperationsByOperator.
     * 
     * @param nOperationsByOperator the nOperationsByOperator to set
     */
    public void setnOperationsByOperator(int nOperationsByOperator) {
        this.nOperationsByOperator = nOperationsByOperator;
    }

    public String getThreadId() {
        return "[Thread " + Thread.currentThread().getId() + "] ";
    }

    public String getTime() {
        return "[" + System.currentTimeMillis() + "] ";
    }

    @Override
    public void doOperations() throws Exception {
        if (serverSocketFreePortOpener == null) {
            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
        }
        int nOperationsByOperatorLocal = nOperationsByOperator;
        for (int i = 0; i < nOperationsByOperatorLocal; i++) {
            if (debug) {
                System.out.println(getTime() + getThreadId() + "message=" + i);
            }
            ServerSocket openServerSocketFromRangePort = serverSocketFreePortOpener.openServerSocketFromRangePort(portRangeBound1,
                    portRangeBound2);
            if (openServerSocketFromRangePort != null) {
                Thread.sleep(random.nextInt(100));
                openServerSocketFromRangePort.close();
            }
        }
    }

}
