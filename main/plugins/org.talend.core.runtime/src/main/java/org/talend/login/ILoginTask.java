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
package org.talend.login;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.PluginChecker;

/**
 * created by wchen on 2015-5-15 Detailled comment Define a login taks that will be execute when login project
 */
public interface ILoginTask {

    public Date getOrder();

    public boolean isCommandlineTask();

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

    default void execute(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        if (CommonsPlugin.isHeadless() || CommonsPlugin.isJUnitTest() || PluginChecker.isSWTBotLoaded()
                || CommonsPlugin.isTUJTest() || !isBackground()) {
            run(monitor);
        } else {
            new Thread(() -> {
                try {
                    run(monitor);
                } catch (InvocationTargetException | InterruptedException e) {
                    ExceptionHandler.process(e);
                }
            }, getClass().getCanonicalName()).start();
        }
    }
    /**
     * Which indicates the task will be executed for each logon of a project, by default return false(execute only once
     * at the time of logon studio).
     */
    boolean isRequiredAlways();

    boolean isBackground();

}
