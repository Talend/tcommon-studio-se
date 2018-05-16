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
package org.talend.commons.runtime.model.emf.provider;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class ResourceHandler {

    public void preLoad(Object resource, InputStream inputStream, Map<?, ?> options) {
        // nothing to do by default
    }

    public void postLoad(Object resource, InputStream inputStream, Map<?, ?> options) {
        // nothing to do by default
    }

    public void preSave(Object resource, OutputStream outputStream, Map<?, ?> options) {
        // nothing to do by default
    }

    public void postSave(Object resource, OutputStream outputStream, Map<?, ?> options) {
        // nothing to do by default
    }

}
