// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.repository.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id: URIHelper.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class URIHelper {

    public static IFile getFile(URI uri) {
        return org.talend.core.runtime.util.URIHelper.getFile(uri);
    }

    public static IFile getFile(IPath path) {
        return org.talend.core.runtime.util.URIHelper.getFile(path);
    }

    public static IPath convert(URI uri) {
        return org.talend.core.runtime.util.URIHelper.convert(uri);
    }

    public static URI convert(IPath path) {
        return org.talend.core.runtime.util.URIHelper.convert(path);
    }
}
