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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.talend.testutils.threading.AbstractThreadSafetyTester;
import org.talend.utils.network.operators.ServerSocketFreePortOpenerOperator;

/**
 * DOC amaumont class global comment. Detailled comment
 */
public class ServerSocketFreePortOpenerTest {

    private static final boolean DEBUG = false;

    @Test
    public void testOpensServerSocketFromRangePortSameReference() throws IOException {

        ServerSocketFreePortOpener serverSocketFreePortOpener = new ServerSocketFreePortOpener();
        ServerSocket serverSocket;
        int[] portsToOpen = new int[] { 10000, 10001, 10002, 10003, 10004 };
        List<ServerSocket> servers = openPorts(portsToOpen);
        try {

            int freePort = -1;

            for (int i = 0; i < 1000; i++) {
                if (isPortFree(10005 + i)) {
                    freePort = 10005 + i;
                    break;
                }
            }

            if (freePort == -1) {
                fail("Can't find a free port");
            }

            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, freePort, false);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }

            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, freePort, true);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }

            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, 10000, false);
            assertNull("Can't find free port expected", serverSocket);

            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, 10000, true);
            assertNull("Can't find free port expected", serverSocket);

            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(freePort, freePort, false);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }

            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(freePort, freePort, true);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }
        } finally {
            closeServerSockets(servers);
        }

        for (int port : portsToOpen) {
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(port, port, false);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(port));
            }
        }

    }

    @Test
    public void testOpensServerSocketFromRangePortDifferentReference() throws IOException {

        ServerSocketFreePortOpener serverSocketFreePortOpener = null;
        ServerSocket serverSocket;
        int[] portsToOpen = new int[] { 10000, 10001, 10002, 10003, 10004 };
        List<ServerSocket> servers = openPorts(portsToOpen);
        try {
            int freePort = -1;

            for (int i = 0; i < 1000; i++) {
                if (isPortFree(10005 + i)) {
                    freePort = 10005 + i;
                    break;
                }
            }

            if (freePort == -1) {
                fail("Can't find a free port");
            }

            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, freePort, false);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }

            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, freePort, true);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }

            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, 10000, false);
            assertNull("Can't find free port expected", serverSocket);

            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(10000, 10000, true);
            assertNull("Can't find free port expected", serverSocket);

            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(freePort, freePort, false);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }

            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(freePort, freePort, true);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(freePort));
                servers.add(serverSocket);
            }
        } finally {
            closeServerSockets(servers);
        }

        for (int port : portsToOpen) {
            serverSocketFreePortOpener = new ServerSocketFreePortOpener();
            serverSocket = serverSocketFreePortOpener.openServerSocketFromRangePort(port, port, false);
            if (serverSocket != null) {
                int localPort = serverSocket.getLocalPort();
                assertThat(localPort, is(port));
            }
        }

    }

    @Test(timeout = 10000)
    // Those test seem to fail when launched from OSGI and succeed when launched from pure java.
    // I (SeB.G.) could not figure out why.
    public void testThreadSafetySameReference() throws Exception {
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 50;
        ServerSocketFreePortOpener serverSocketFreePortOpener = new ServerSocketFreePortOpener();
        // case where the same instance is used for each operator
        launchThreadSafetyTest(serverSocketFreePortOpener, nOperatorsByClassOperator, nOperationsByOperator,
                ServerSocketFreePortOpenerOperator.class);
    }

    @Test(timeout = 5000)
    // @Test
    public void testThreadSafetyNewReference() throws Exception {
        final int nOperatorsByClassOperator = 30;
        final int nOperationsByOperator = 50;
        // case where an instance is created for each operator
        launchThreadSafetyTest(null, nOperatorsByClassOperator, nOperationsByOperator, ServerSocketFreePortOpenerOperator.class);
    }

    private void launchThreadSafetyTest(final ServerSocketFreePortOpener serverSocketFreePortOpener,
            final int nOperatorsByClassOperator, final int nOperationsByOperator,
            Class<? extends ServerSocketFreePortOpenerOperator>... classOperators) throws Exception {

        class ThreadSafetyTester extends AbstractThreadSafetyTester<ServerSocketFreePortOpenerOperator> {

            public ThreadSafetyTester(int nOperatorsByClassOperator,
                    Class<? extends ServerSocketFreePortOpenerOperator>... classOperators) {
                super(nOperatorsByClassOperator, classOperators);
            }

            /*
             * (non-Javadoc)
             *
             * @see org.talend.commons.utils.threading.AbstractThreadSafetyTester#initInstance(org.talend.commons.utils.
             * threading .IThreadSafetyOperator)
             */
            @Override
            protected void initInstance(ServerSocketFreePortOpenerOperator operatorInstance) {
                operatorInstance.setDebug(DEBUG);
                operatorInstance.setServerSocketFreePortOpener(serverSocketFreePortOpener);
                operatorInstance.setnOperationsByOperator(nOperationsByOperator);
            }

            /*
             * (non-Javadoc)
             *
             * @see org.talend.commons.utils.threading.AbstractThreadSafetyTester#assertFinal()
             */
            @Override
            protected void assertFinal() throws IOException, InterruptedException {
                Thread.sleep(1000);
                for (int port = ServerSocketFreePortOpenerOperator.portRangeBound1; port <= ServerSocketFreePortOpenerOperator.portRangeBound2; port++) {
                    assertThat("Port " + port + " should be free!", isPortFree(port), is(true));
                }

            }

        }

        @SuppressWarnings("unchecked")
        ThreadSafetyTester lockerThreadSafetyTester = new ThreadSafetyTester(nOperatorsByClassOperator, classOperators);
        lockerThreadSafetyTester.start();
    }

    /**
     *
     * Return true if the specified port is free.
     *
     * @param port
     * @return Return true if the specified port is free
     */
    public boolean isPortFree(int port) {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (Throwable e) {
            if (DEBUG) {
                System.err.println("socket error on port : " + port);
                e.printStackTrace(System.err);
            }
            return false;
        } finally {
            try {
                serverSocket.close();
            } catch (Throwable e) {
                // nothing
            }
        }
        return true;
    }

    private List<ServerSocket> openPorts(int[] ports) throws IOException {
        List<ServerSocket> serverSockets = new ArrayList<ServerSocket>();

        ServerSocketFreePortOpener serverSocketFreePortOpener = new ServerSocketFreePortOpener();
        for (int port : ports) {
            ServerSocket openedServerSocket = null;
            try {
                openedServerSocket = serverSocketFreePortOpener.openServerSocket(port);
                serverSockets.add(openedServerSocket);
            } catch (Exception e) {
                System.out.println("Warning: Port " + port + " already used");
            }
        }
        return serverSockets;
    }

    private void closeServerSockets(List<ServerSocket> servers) {
        for (ServerSocket serverSocket : servers) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
