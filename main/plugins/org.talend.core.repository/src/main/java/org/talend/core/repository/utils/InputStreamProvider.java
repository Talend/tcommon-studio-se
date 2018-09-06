package org.talend.core.repository.utils;
//============================================================================
//
//Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
//This source code is available under agreement available at
//%InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
//You should have received a copy of the agreement
//along with this program; if not, write to Talend SA
//9 rue Pages 92150 Suresnes, France
//
//============================================================================
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;

public interface InputStreamProvider {
    public InputStream getStream(IPath path) throws IOException;
}
