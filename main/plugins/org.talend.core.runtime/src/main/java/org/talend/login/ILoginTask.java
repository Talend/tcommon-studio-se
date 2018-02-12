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
package org.talend.login;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * created by wchen on 2015-5-15 Detailled comment Define a login taks that will be execute when login project
 */
public interface ILoginTask {

    public Date getOrder();

    public boolean isCommandlineTask();

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;
}
