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
package org.talend.core.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.service.IMRProcessService;
import org.talend.core.service.IStormProcessService;
import org.talend.core.ui.IJobletProviderService;
import org.talend.core.ui.ISparkJobletProviderService;
import org.talend.core.ui.ISparkStreamingJobletProviderService;
import org.talend.designer.core.IDesignerCoreService;

/**
 * DOC qzhang class global comment. Detailled comment
 */
public abstract class CustomExternalActions extends SelectionAction {

    public static final int INPUT = 0221;

    public static final int OUTPUT = 0223;

    public static final String ELEM_CLASS = "class"; //$NON-NLS-1$

    public static final String ELEM_LABEL = "label"; //$NON-NLS-1$

    public static final String ELEM_ID = "id"; //$NON-NLS-1$

    public static final String EXTENSION_ID = "org.talend.core.component_custom_action"; //$NON-NLS-1$

    /**
     * DOC qzhang AbstractContextMenuProvider constructor comment.
     */
    public CustomExternalActions() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor());
    }

    /**
     * qzhang Comment method "getComponentType".
     * 
     * @return
     */
    public abstract int getComponentType();

    /**
     * DOC qzhang Comment method "getInstances".
     * 
     * @return
     */
    public static List<SelectionAction> getInstances(IWorkbenchPart part) {
        List<SelectionAction> actions = new ArrayList<SelectionAction>();
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IConfigurationElement[] configurationElementsFor = extensionRegistry.getConfigurationElementsFor(EXTENSION_ID);
        for (IConfigurationElement configurationElement : configurationElementsFor) {
            try {
                CustomExternalActions action = (CustomExternalActions) configurationElement.createExecutableExtension(ELEM_CLASS);
                String attribute = configurationElement.getAttribute(ELEM_LABEL);
                action.setText(attribute);
                attribute = configurationElement.getAttribute(ELEM_ID);
                action.setId(attribute);
                actions.add(action);
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }

        }
        return actions;
    }

    protected boolean isMapReduceEditorActive() {
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(IMRProcessService.class)) {
            return false;
        }
        IMRProcessService mrService = (IMRProcessService) GlobalServiceRegister.getDefault().getService(IMRProcessService.class);
        if (mrService == null) {
            return false;
        }
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return mrService.isMapReduceEditor(activeEditor);
    }
    
    protected boolean isSParkJobletEditorActive() {
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(ISparkJobletProviderService.class)) {
            return false;
        }
        ISparkJobletProviderService mrService = (ISparkJobletProviderService) GlobalServiceRegister.getDefault().getService(ISparkJobletProviderService.class);
        if (mrService == null) {
            return false;
        }
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return mrService.isSparkJobletEditor(activeEditor);
    }
    
    protected boolean isStandardJobEditorActive() {
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            return false;
        }
        IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(IDesignerCoreService.class);
        if(service == null){
            return false;
        }
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return service.isTalendEditor(activeEditor);
    }
    
    protected boolean isStandardJobletEditorActive() {
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(IJobletProviderService.class)) {
            return false;
        }
        IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(IJobletProviderService.class);
        if(service == null){
            return false;
        }
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return service.isJobletEditor(activeEditor);
    }

    protected boolean isStormEditorActive() {
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(IStormProcessService.class)) {
            return false;
        }
        IStormProcessService mrService = (IStormProcessService) GlobalServiceRegister.getDefault().getService(
                IStormProcessService.class);
        if (mrService == null) {
            return false;
        }
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return mrService.isStormEditor(activeEditor);
    }
    
    protected boolean isSParkStreamingJobletEditorActive() {
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(ISparkStreamingJobletProviderService.class)) {
            return false;
        }
        ISparkStreamingJobletProviderService mrService = (ISparkStreamingJobletProviderService) GlobalServiceRegister.getDefault().getService(
        		ISparkStreamingJobletProviderService.class);
        if (mrService == null) {
            return false;
        }
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return mrService.isSparkStreamingJobletEditor(activeEditor);
    }
}
