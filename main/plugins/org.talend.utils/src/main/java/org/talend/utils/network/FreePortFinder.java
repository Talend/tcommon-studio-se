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
package org.talend.utils.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 *
 */
public class FreePortFinder {

    private static Logger log = LoggerFactory.getLogger(FreePortFinder.class);

    private static Random random = new Random(System.currentTimeMillis());

    private static Object[] randomLock = new Object[0];

    /**
     * DOC amaumont FreePortFinder constructor comment.
     *
     * @throws IOException
     */
    public FreePortFinder() {
        super();
    }

    public static Set<String> busyPorts = Collections.synchronizedSet(new HashSet<String>());

    /**
     *
     * Return true if the specified port is free. This method is changed by Marvin Wang on Feb. 20 for bug TDI-19166. To
     * try to save all the ports that ever be searched. Make sure the port sent from here is free. If port is no longer
     * to use, remember to invoke the method {@link #removePort(int)} to remove the port.
     *
     * @param port
     * @return Return true if the specified port is free
     */
    public boolean isPortFree(int port) {

        String freePort = String.valueOf(port);
        boolean isBusyPort = true;
        synchronized (busyPorts) {
            isBusyPort = busyPorts.contains(freePort);
            ServerSocket serverSocket = null;
            if (!isBusyPort) {
                try {
                    serverSocket = new ServerSocket(port);
                    isBusyPort = false;
                } catch (Throwable e) {
                    isBusyPort = true;
                    log.debug(e.getMessage());
                } finally {
                    try {
                        serverSocket.close();
                        busyPorts.add(freePort);
                    } catch (Throwable e) {
                        log.debug(e.getMessage());
                    }
                }
            }
        }

        return !isBusyPort;
        // ServerSocket serverSocket = null;
        // try {
        // serverSocket = new ServerSocket(port);
        // } catch (Throwable e) {
        // log.debug(e.getMessage());
        // return false;
        // } finally {
        // try {
        // serverSocket.close();
        // } catch (Throwable e) {
        // log.debug(e.getMessage());
        // }
        // }
        // return true;
    }

    /**
     * Need to invoke the method to remove the assigned port if server socket is closed or the found port is no longer
     * needed. Because all assigned ports are kept in a set. Another case is when invoker calls the method
     * {@link #isPortFree(int)},port will be kept in the set, so if port is no longer to use, remember to invoke this
     * method.
     *
     * @param port
     */
    public void removePort(int port) {
        boolean containsPort = false;
        String usingPort = String.valueOf(port);
        synchronized (busyPorts) {
            containsPort = busyPorts.contains(usingPort);
            if (containsPort)
                busyPorts.remove(usingPort);
        }
    }

    /**
     *
     * Return true if the specified port is free.
     *
     * @param port
     * @return
     */
    protected ServerSocket openServerSocket(int port) {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Throwable e) {
            log.debug(e.getMessage());
        }
        return serverSocket;
    }

    /**
     *
     * Search the next free port from <code>portRangeBound1</code> to max port <code>portRangeBound2</code> with
     * randomize start searching. If port is no longer to use, remember to invoke the method {@link #removePort(int)} to
     * remove the port.
     *
     * @param portRangeBound1
     * @param portRangeBound2
     * @return
     */
    public int searchFreePort(int portRangeBound1, int portRangeBound2) {
        return opensServerSocketFromRangePort(portRangeBound1, portRangeBound2, true);
    }

    /**
     *
     * Search the next free port from <code>portRangeBound1</code> to max port <code>portRangeBound2</code>. If port is
     * no longer to use, remember to invoke the method {@link #removePort(int)} to remove the port.
     *
     * @param portRangeBound1
     * @param portRangeBound2
     * @param randomizeIndexStart if true, start with a randomized port number between <code>portRangeBound1</code> and
     * <code>portRangeBound2</code>
     * @return
     */
    public int opensServerSocketFromRangePort(int portRangeBound1, int portRangeBound2, boolean randomizeIndexStart) {

        int portBoundMin = portRangeBound1 < portRangeBound2 ? portRangeBound1 : portRangeBound2;
        int portBoundMax = portRangeBound1 < portRangeBound2 ? portRangeBound2 : portRangeBound1;
        int increment = 0;
        if (randomizeIndexStart) {
            int maxRandomBound = (int) ((double) portBoundMax - (double) portBoundMin) * 3 / 4;
            if (maxRandomBound >= 0) {
                if (maxRandomBound == 0)
                    increment = 0;
                else {
                    synchronized (randomLock) {
                        increment = random.nextInt(maxRandomBound);

                    }
                }
                int portStart = portBoundMin + increment;
                boolean isFirstLoop = true;
                boolean isFirstPass = true;

                for (int port = portStart; true; port++) {
                    if (port > portBoundMax) {
                        port = portBoundMin;
                    }
                    if (!isFirstLoop && port == portStart) {
                        if (isFirstPass) {
                            isFirstPass = false;
                        } else {
                            break;
                        }
                    }
                    if (isPortFree(port)) {
                        return port;
                    }
                    // System.out.println(port);
                    isFirstLoop = false;
                }
            }

        }
        return -1;
    }

    public static void main(String[] args) {
        FreePortFinder freePortFinder = new FreePortFinder();
        System.out.println(freePortFinder.searchFreePort(10, 20));
    }

}
