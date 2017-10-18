// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.aether;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IDynamicMonitor extends IProgressMonitor {

    public void writeMessage(String message);

    public void progress(String message, int step);

}
