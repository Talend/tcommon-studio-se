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
package org.talend.commons.utils.network;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IProxySelectorProvider {

    Object getKey();

    boolean canHandle(final URI uri);

    List<Proxy> select(final URI uri);

    void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe);

}
