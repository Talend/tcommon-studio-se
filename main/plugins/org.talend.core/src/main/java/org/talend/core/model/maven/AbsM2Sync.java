package org.talend.core.model.maven;
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

import java.io.File;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbsM2Sync {

    public static final String EXTENTION_POINT = "org.talend.core.m2Sync";

    public static final String EXTENTION_CLASS = "class";

    abstract public void sync(File dest) throws Exception;

}
