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
package org.talend.core.repository.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.events.SelectionListener;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.model.RepositoryConstants;

/**
 * Provides, using extension points, implementation of many factories.
 * 
 * <ul>
 * <li>IProcessFactory</li>
 * </ul>
 * 
 * $Id: RepositoryFactoryProvider.java 38013 2010-03-05 14:21:59Z mhirt $
 */
public class RepositoryFactoryProvider {

    private static List<IRepositoryFactory> list = null;

    public static final IExtensionPointLimiter REPOSITORY_PROVIDER = new ExtensionPointLimiterImpl(
            "org.talend.core.repository.repository_provider", //$NON-NLS-1$
            "RepositoryFactory", 1, -1); //$NON-NLS-1$

    public static List<IRepositoryFactory> getAvailableRepositories() {
        if (list == null) {
            list = new ArrayList<IRepositoryFactory>();
            List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(REPOSITORY_PROVIDER);
            String hiddenRepos = System.getProperty("hidden.repositories"); //$NON-NLS-1$
            String hiddenRepository[] = new String[]{};
            if (hiddenRepos != null) {
                hiddenRepository = hiddenRepos.split(";"); //$NON-NLS-1$
            }

            for (IConfigurationElement current : extension) {
                try {
                    IRepositoryFactory currentAction = (IRepositoryFactory) current.createExecutableExtension("class"); //$NON-NLS-1$
                    currentAction.setId(current.getAttribute("id")); //$NON-NLS-1$
                    currentAction.setName(current.getAttribute("name")); //$NON-NLS-1$
                    currentAction.setAuthenticationNeeded(new Boolean(current.getAttribute("authenticationNeeded"))); //$NON-NLS-1$
                    currentAction.setDisplayToUser(new Boolean(current.getAttribute("displayToUser")).booleanValue()); //$NON-NLS-1$

                    // Getting dynamic login fields:
                    for (IConfigurationElement currentLoginField : current.getChildren("loginField")) { //$NON-NLS-1$
                        DynamicFieldBean key = new DynamicFieldBean(currentLoginField.getAttribute("id"), //$NON-NLS-1$
                                currentLoginField.getAttribute("name"), //$NON-NLS-1$
                                new Boolean(currentLoginField.getAttribute("required")), //$NON-NLS-1$
                                new Boolean(currentLoginField.getAttribute("password"))); //$NON-NLS-1$
                        currentAction.getFields().add(key);
                    }

                    for (IConfigurationElement currentLoginField : current.getChildren("button")) { //$NON-NLS-1$
                        DynamicButtonBean key = new DynamicButtonBean(currentLoginField.getAttribute("id"), //$NON-NLS-1$
                                currentLoginField.getAttribute("name"), //$NON-NLS-1$
                                (SelectionListener) currentLoginField.createExecutableExtension("selectionListener")); //$NON-NLS-1$
                        currentAction.getButtons().add(key);
                    }

                    for (IConfigurationElement currentLoginField : current.getChildren("choiceField")) { //$NON-NLS-1$
                        DynamicChoiceBean key = new DynamicChoiceBean(currentLoginField.getAttribute("id"), //$NON-NLS-1$
                                currentLoginField.getAttribute("name")); //$NON-NLS-1$
                        for (IConfigurationElement currentChoice : currentLoginField.getChildren("choice")) { //$NON-NLS-1$
                            String value = currentChoice.getAttribute("value"); //$NON-NLS-1$
                            String label = currentChoice.getAttribute("label"); //$NON-NLS-1$
                            key.addChoice(value, label);
                        }
                        currentAction.getChoices().add(key);
                    }
                    if (ArrayUtils.contains(hiddenRepository, currentAction.getId())) {
                        continue;
                    }
                    list.add(currentAction);
                } catch (CoreException e) {
                    // e.printStackTrace();
                    ExceptionHandler.process(e);
                }
            }
        }
        return list;
    }

    public static IRepositoryFactory getRepositoriyById(String id) {
        for (IRepositoryFactory current : getAvailableRepositories()) {
            if (current.getId().equals(id)) {
                return current;
            }
        }
        return null;
    }
}
