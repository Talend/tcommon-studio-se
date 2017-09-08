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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.core.utils.RegistryReader;
import org.talend.login.ILoginTask;

/**
 * This provides data for extensions points extending org.talend.core.repository.loginTask
 * 
 */
public class LoginTaskRegistryReader extends RegistryReader {

    /**
     * 
     */
    private static final String PRIORITY_ATTRIBUTE = "priority"; //$NON-NLS-1$

    static class LoginTask {

        public final ILoginTask runnable;

        public final Priority priority;

        enum Priority {
            LOWEST,
            LOW,
            NORMAL,
            HIGH,
            HIGHEST
        }

        /**
         * DOC sgandon LoginTaskRegistryReader.LoginTask constructor comment.
         */
        public LoginTask(ILoginTask runnable, Priority priority) {
            this.runnable = runnable;
            this.priority = priority;
        }
    }

    /**
     * 
     */
    private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String LOGIN_TASK_ELEMENT_NAME = "loginTask"; //$NON-NLS-1$

    /**
     * extension point Id
     */
    private static final String LOGIN_TASK_EXTENSION_POINT = "login.task"; //$NON-NLS-1$

    protected List<LoginTask> allLoginTasks;// must use raw type cause the api used is return raw type

    /**
     * DOC sgandon LoginTaskRegistryReader constructor comment.
     * 
     * @param aPluginId
     * @param anExtensionPoint
     */
    public LoginTaskRegistryReader() {
        super(FrameworkUtil.getBundle(LoginTaskRegistryReader.class).getSymbolicName(), LOGIN_TASK_EXTENSION_POINT);
    }

    public ILoginTask[] getAllTaskListInstance() {
        if (allLoginTasks == null) {
            allLoginTasks = new ArrayList<LoginTask>();
            readRegistry();
        }
        sortLoginTasks(allLoginTasks);
        return getAllRunnables(allLoginTasks);
    }

    public ILoginTask[] getAllCommandlineTaskListInstance() {
        List<ILoginTask> commandlineTask = new ArrayList<ILoginTask>();
        ILoginTask[] allTask = getAllTaskListInstance();
        for (ILoginTask task : allTask) {
            if (task.isCommandlineTask()) {
                commandlineTask.add(task);
            }
        }
        return commandlineTask.toArray(new ILoginTask[commandlineTask.size()]);
    }

    /**
     * DOC sgandon Comment method "getAllRunnables".
     * 
     * @param allLoginTasks2
     * @return
     */
    private ILoginTask[] getAllRunnables(List<LoginTask> allLoginTasks2) {
        ILoginTask[] runnables = new ILoginTask[allLoginTasks2.size()];
        int i = 0;
        for (LoginTask loginTask : allLoginTasks2) {
            runnables[i++] = loginTask.runnable;
        }
        return runnables;
    }

    /**
     * sort all login task according to the priority
     * 
     * @param allLoginTasks2
     */
    private void sortLoginTasks(List<LoginTask> allLoginTasks2) {
        Collections.sort(allLoginTasks2, new Comparator<LoginTask>() {

            @Override
            public int compare(LoginTask arg0, LoginTask arg1) {
                int priority = arg1.priority.compareTo(arg0.priority);
                if (priority == 0) {
                    return arg0.runnable.getOrder().compareTo(arg1.runnable.getOrder());
                }
                return priority;
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if (LOGIN_TASK_ELEMENT_NAME.equals(element.getName())) {
            SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

                @Override
                public void run() throws Exception {
                    ILoginTask runnable = (ILoginTask) element.createExecutableExtension(CLASS_ATTRIBUTE);
                    String priorityString = element.getAttribute(PRIORITY_ATTRIBUTE);
                    LoginTask.Priority priority = (priorityString != null && priorityString.length() > 0) ? LoginTask.Priority
                            .valueOf(priorityString.toUpperCase()) : LoginTask.Priority.NORMAL;
                    allLoginTasks.add(new LoginTask(runnable, priority));
                }

            });
            return true;
        }// else return false
        return false;
    }
}
