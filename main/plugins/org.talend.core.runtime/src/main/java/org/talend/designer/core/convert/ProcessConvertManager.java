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
package org.talend.designer.core.convert;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * <pre>
 * This manager is used to manage all converters for process, like Map/Reduce process, routines, joblet. In this
 * manager, user can get all convert services which are registed by the extension point <b>
 * <code>org.talend.designer.core.process_convert</code></b> and implement {@link IProcessConvertService}.
 * </pre>
 * 
 * <pre>
 * User also can get the {@link IProcessConvertService} by the special convert type {@link ProcessConverterType}.
 * </pre>
 * 
 * Created by Marvin Wang on Feb 18, 2013.
 */
public class ProcessConvertManager {

    public static final String EXTENSION_POINT_FOR_CONVERT = "org.talend.designer.core.process_convert";//$NON-NLS-1$

    private static ProcessConvertManager convertManager = null;

    private List<IProcessConvertService> processConvertServices = new ArrayList<IProcessConvertService>();

    private ProcessConvertManager() {
    }

    public synchronized static ProcessConvertManager getInstance() {
        if (convertManager == null) {
            convertManager = new ProcessConvertManager();

        }
        return convertManager;
    }

    /**
     * Extracts all convert services which are registed by the extension point <b>
     * <code>org.talend.designer.core.process_convert</code> </b>. Added by Marvin Wang on Mar 19, 2013.
     * 
     * @return
     */
    public List<IProcessConvertService> extractAllConvertServices() {
        if (processConvertServices.size() > 0) {
            return processConvertServices;
        }
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(EXTENSION_POINT_FOR_CONVERT);
        if (extensionPoint != null) {
            IExtension[] extensions = extensionPoint.getExtensions();
            for (IExtension extension : extensions) {
                IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                for (IConfigurationElement configurationElement : configurationElements) {
                    try {
                        Object service = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                        if (service instanceof IProcessConvertService) {
                            processConvertServices.add((IProcessConvertService) service);
                        }
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return processConvertServices;
    }

    /**
     * Extracts the special convert service by the given {@link ProcessConverterType}. Added by Marvin Wang on Mar 19,
     * 2013.
     * 
     * @param convertType
     * @return
     */
    public IProcessConvertService extractConvertService(ProcessConverterType convertType) {
        List<IProcessConvertService> services = extractAllConvertServices();
        if (services != null && services.size() > 0) {
            for (IProcessConvertService convertService : services) {
                ProcessConverterType type = convertService.getConverterType();
                if (convertType == type) {
                    return convertService;
                }
            }
        }
        return null;
    }

    public boolean CheckConvertProcess(RepositoryNode sourceNode, RepositoryNode targetNode) {
        boolean checkConvertProcess = false;
        if (sourceNode.getObject().getRepositoryObjectType() == ERepositoryObjectType.PROCESS
                || sourceNode.getObject().getRepositoryObjectType() == ERepositoryObjectType.PROCESS_STORM
                || sourceNode.getObject().getRepositoryObjectType() == ERepositoryObjectType.PROCESS_MR) {
            if (ENodeType.SYSTEM_FOLDER == targetNode.getType() || ENodeType.SIMPLE_FOLDER == targetNode.getType()) {
                if (sourceNode.getObject().getRepositoryObjectType() == targetNode.getContentType()) {
                    // if it's the same type, no conversion needed
                    return false;
                }
                if (targetNode.getContentType() == ERepositoryObjectType.PROCESS
                        || targetNode.getContentType() == ERepositoryObjectType.PROCESS_STORM
                        || targetNode.getContentType() == ERepositoryObjectType.PROCESS_MR) {
                    checkConvertProcess = true;
                }
            }
        }
        return checkConvertProcess;
    }

    public boolean CheckConvertProcess(ERepositoryObjectType oldType, String oldFrameworkValue, ERepositoryObjectType newType,
            String newFrameworkValue) {
        List<ERepositoryObjectType> processes = getSupportedProcessType();
        if (!processes.contains(oldType) || !processes.contains(newType)) {
            return false;
        }
        if (oldType != newType) {
            return true;
        }

        String oldFramework = oldFrameworkValue;
        if (oldFramework == null) {
            oldFramework = ""; //$NON-NLS-1$
        }
        String newFramework = newFrameworkValue;
        if (newFramework == null) {
            newFramework = ""; //$NON-NLS-1$
        }

        if (oldType == ERepositoryObjectType.PROCESS_MR || oldType == ERepositoryObjectType.PROCESS_STORM) {
            if (!oldFramework.equals(newFramework)) {
                return true;
            }
        }

        return false;
    }

    private List<ERepositoryObjectType> getSupportedProcessType() {
        List<ERepositoryObjectType> processes = new ArrayList<ERepositoryObjectType>();
        if (ERepositoryObjectType.PROCESS != null) {
            processes.add(ERepositoryObjectType.PROCESS);
        }
        if (ERepositoryObjectType.PROCESS_MR != null) {
            processes.add(ERepositoryObjectType.PROCESS_MR);
        }
        if (ERepositoryObjectType.PROCESS_STORM != null) {
            processes.add(ERepositoryObjectType.PROCESS_STORM);
        }
        return processes;
    }
}
