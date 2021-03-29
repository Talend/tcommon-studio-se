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
import java.net.SocketAddress;
import java.net.URI;

import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractProxySelectorProvider implements IProxySelectorProvider {

    private boolean isDebugMode = CommonsPlugin.isDebugMode();

    @Override
    public Object getKey() {
        return this;
    }

    @Override
    public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
        if (isDebugMode) {
            ExceptionHandler.process(ioe);
        }
    }

}
