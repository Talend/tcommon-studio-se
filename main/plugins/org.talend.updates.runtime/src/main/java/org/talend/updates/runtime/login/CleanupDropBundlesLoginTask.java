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
package org.talend.updates.runtime.login;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.login.AbstractLoginTask;
import org.talend.updates.runtime.utils.UpdateTools;

public class CleanupDropBundlesLoginTask extends AbstractLoginTask {

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2019, 12, 5, 23, 0, 0);
        return gc.getTime();
    }

    @Override
    public boolean isCommandlineTask() {
        return true;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            UpdateTools.cleanUpDropBundles();
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

    }

}
