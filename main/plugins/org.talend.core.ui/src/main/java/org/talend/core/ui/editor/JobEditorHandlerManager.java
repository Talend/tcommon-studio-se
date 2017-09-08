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
package org.talend.core.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * This manager is used to find {@link IJobEditorHandler} by the extension point
 * <b>org.talend.designer.core.ui.editorHandler</b>. See the method {@link #extractEditorInputFactory(String)} to
 * extract a {@link IJobEditorHandler}. Created by Marvin Wang on Apr 17, 2013.
 */
public class JobEditorHandlerManager {

    /**
     * Repository object type attribute, value is <code>RepositoryObjectType</code>.
     */
    private final static String ATT_REPOSITORY_OBJECT_TYPE = "RepositoryObjectType"; //$NON-NLS-1$

    /**
     * Class attribute, value is <code>class</code>.
     */
    private final static String ATT_CLASS = "class"; //$NON-NLS-1$

    private static final String EXTENSION_POINT_JOB_EDITOR_INPUT_FACTORY = "org.talend.designer.core.ui.editorHandler"; //$NON-NLS-1$

    private static JobEditorHandlerManager instance;

    private static IConfigurationElement[] configurationElements = null;

    static {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        configurationElements = registry.getConfigurationElementsFor(EXTENSION_POINT_JOB_EDITOR_INPUT_FACTORY);
    }

    private JobEditorHandlerManager() {
    }

    public synchronized static JobEditorHandlerManager getInstance() {
        if (instance == null) {
            instance = new JobEditorHandlerManager();
        }
        return instance;
    }

    /**
     * Extracts the editor input factory {@link IJobEditorHandler} by the repository object type
     * {@link ERepositoryObjectType} which is defined in extension point. The value is from the type of
     * {@link ERepositoryObjectType#getType()}. Added by Marvin Wang on Apr 18, 2013.
     * 
     * @param repObjType should not be <code>null</code>.
     * @return <code>null</code> if the given parameter is <code>null</code>, or can not find any factories to match the
     * given parameter.
     */
    public IJobEditorHandler extractEditorInputFactory(String repObjType) {
        IJobEditorHandler factory = null;
        if (repObjType != null) {
            if (configurationElements != null && configurationElements.length > 0) {
                for (IConfigurationElement confElement : configurationElements) {
                    String value = confElement.getAttribute(ATT_REPOSITORY_OBJECT_TYPE);
                    if (repObjType.equals(value)) {
                        try {
                            factory = (IJobEditorHandler) confElement.createExecutableExtension(ATT_CLASS);
                            break;
                        } catch (CoreException e) {
                            CommonExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
        return factory;
    }
}
