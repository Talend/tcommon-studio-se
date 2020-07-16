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
import java.util.Random;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 *
 */
public class ServerSocketFreePortOpener {

    private static Random random = new Random(System.currentTimeMillis());

    /**
     * DOC amaumont FreePortFinder constructor comment.
     *
     * @throws IOException
     */
    public ServerSocketFreePortOpener() {
        super();
    }

    /**
     *
     * Return true if the specified port is free.
     *
     * @param port
     * @return
     * @throws IOException
     */
    public ServerSocket openServerSocket(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
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
     * @return ServerSocket or null if can't find an available port
     */
    public ServerSocket openServerSocketFromRangePort(int portRangeBound1, int portRangeBound2) {
        return openServerSocketFromRangePort(portRangeBound1, portRangeBound2, true);
    }

    /**
     *
     * Search the next free port from <code>portRangeBound1</code> to max port <code>portRangeBound2</code>.
     *
     * @param portRangeBound1
     * @param portRangeBound2
     * @param randomizeIndexStart if true, start with a randomized port number between <code>portRangeBound1</code> and
     * <code>portRangeBound2</code>
     * @return ServerSocket or null if can't find an available port
     */
    public ServerSocket openServerSocketFromRangePort(int portRangeBound1, int portRangeBound2, boolean randomizeIndexStart) {

        int portBoundMin = portRangeBound1 < portRangeBound2 ? portRangeBound1 : portRangeBound2;
        int portBoundMax = portRangeBound1 < portRangeBound2 ? portRangeBound2 : portRangeBound1;
        int increment = 0;
        if (randomizeIndexStart) {
            int maxRandomBound = (int) ((double) portBoundMax - (double) portBoundMin) * 3 / 4;
            if (maxRandomBound >= 0) {
                if (maxRandomBound == 0) {
                    increment = 0;
                } else {
                    increment = random.nextInt(maxRandomBound);
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

                    try {
                        ServerSocket openedServerSocket = openServerSocket(port);
                        return openedServerSocket;
                    } catch (IOException e) {
                        // nothing
                    }
                    isFirstLoop = false;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ServerSocketFreePortOpener serverSocketFreePortOpener = new ServerSocketFreePortOpener();
        System.out.println(serverSocketFreePortOpener.openServerSocketFromRangePort(10, 20));
    }

}
