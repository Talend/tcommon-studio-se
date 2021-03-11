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
package org.talend.core.nexus;

import java.io.IOException;

public class ResolverExceptionHandler {

    public static IOException hideCredential(IOException e) {
        // hide the user/password in the error
        String regex = "\\://(.+)\\:(.+)@"; //$NON-NLS-1$
        String message = e.getMessage();
        message = message.replaceAll(regex, "://"); //$NON-NLS-1$
        Exception cause = null;
        if (e.getCause() != null) {
            String causeMessage = e.getCause().getMessage();
            causeMessage = causeMessage.replaceAll(regex, "://"); //$NON-NLS-1$
            cause = new Exception(causeMessage);
        }
        return new IOException(message, cause);
    }

}
