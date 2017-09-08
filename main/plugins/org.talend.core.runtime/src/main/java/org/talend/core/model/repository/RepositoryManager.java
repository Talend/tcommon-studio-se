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
package org.talend.core.model.repository;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.JobScriptItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.IJobletProviderService;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * ggu class global comment. Detailled comment
 */
public final class RepositoryManager {

    public static final String PATTERNS_SEPARATOR = ","; //$NON-NLS-1$

    /**
     * 
     * DOC wchen DesignerCorePreference disappear in some case ,so i change to use repository preference store.
     * 
     * @return
     */
    public static IPreferenceStore getRepositoryPreferenceStore() {
        return CoreRuntimePlugin.getInstance().getRepositoryService().getRepositoryPreferenceStore();
    }

    public static IRepositoryView getRepositoryView() {
        return RepositoryManagerHelper.getRepositoryView();
    }

    /**
     * 
     * for create
     */
    public static void refreshCreatedNode(ERepositoryObjectType type) {
        // qli modified to fix the bug 6659.
        if (type != null) {
            syncRoutineAndJoblet(type);
        }
    }

    /**
     * 
     * qli Comment method "syncRoutineAndJoblet".
     * 
     * synchronize the routines and the joblets created by other users.
     */
    public static void syncRoutineAndJoblet(ERepositoryObjectType type) {
        if (type == null) {
            return;
        }
        if (type.equals(ERepositoryObjectType.PIG_UDF)) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
                ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                        ICodeGeneratorService.class);
                try {
                    codeGenService.createRoutineSynchronizer().syncAllPigudf();
                } catch (SystemException e) {
                    ExceptionHandler.process(e);
                }
            }
        } else if (type.equals(ERepositoryObjectType.ROUTINES)) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
                ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                        ICodeGeneratorService.class);
                try {
                    codeGenService.createRoutineSynchronizer().syncAllRoutines();
                } catch (SystemException e) {
                    ExceptionHandler.process(e);
                }
            }
        } else if (type.equals(ERepositoryObjectType.JOBLET)) {
            if (PluginChecker.isJobLetPluginLoaded()) {
                IJobletProviderService jobletService = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                        IJobletProviderService.class);
                if (jobletService != null) {
                    jobletService.loadComponentsFromProviders();
                }
            }
        } else {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
                ICamelDesignerCoreService service = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                        ICamelDesignerCoreService.class);
                if (type.equals(service.getBeansType())) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ICodeGeneratorService.class)) {
                        ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault()
                                .getService(ICodeGeneratorService.class);
                        try {
                            codeGenService.createRoutineSynchronizer().syncAllBeans();
                        } catch (SystemException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

    public static void syncUserComponents() {
        if (PluginChecker.isSVNProviderPluginLoaded()) {
            IComponentsService codeGenService = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                    IComponentsService.class);
            if (codeGenService != null) {
                codeGenService.getComponentsFactory().loadUserComponentsFromComponentsProviderExtension();
            }
        }

    }

    /**
     * 
     * ggu Comment method "isOpenedItemInEditor".
     * 
     * for jobs/joblets/business diagrams/routines/sql patterns
     */
    public static boolean isOpenedItemInEditor(IRepositoryViewObject objectToMove) {
        try {
            if (objectToMove != null) {
                IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (activeWorkbenchWindow != null) {
                    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                    if (activePage != null) {
                        IEditorReference[] editorReferences = activePage.getEditorReferences();
                        if (editorReferences != null) {
                            for (IEditorReference editorReference : editorReferences) {
                                IEditorInput editorInput = editorReference.getEditorInput();
                                if ((editorInput != null && editorInput instanceof IRepositoryEditorInput)) {
                                    IRepositoryEditorInput rInput = (IRepositoryEditorInput) editorInput;
                                    Property openedProperty = rInput.getItem().getProperty();
                                    if (openedProperty.getId().equals(objectToMove.getId())
                                            && VersionUtils.compareTo(openedProperty.getVersion(), objectToMove.getVersion()) == 0) {
                                        return true;
                                    }
                                } else if (objectToMove.getProperty().getItem() instanceof BusinessProcessItem) {
                                    Object obj = editorInput.getAdapter(IRepositoryEditorInput.class);
                                    if (obj instanceof IRepositoryEditorInput) {
                                        IRepositoryEditorInput rInput = (IRepositoryEditorInput) obj;
                                        Property openedProperty = rInput.getItem().getProperty();
                                        if (openedProperty.getId().equals(objectToMove.getId())
                                                && VersionUtils.compareTo(openedProperty.getVersion(), objectToMove.getVersion()) == 0) {
                                            return true;
                                        }
                                    }

                                } else if (objectToMove.getProperty().getItem() instanceof JobScriptItem
                                        && editorInput instanceof FileEditorInput) {
                                    FileEditorInput rInput = (FileEditorInput) editorInput;
                                    IPath path = rInput.getPath();
                                    String[] seg = path.lastSegment().split("_");
                                    if (objectToMove.getProperty().getItem().getProperty().getLabel().equals(seg[0])) {
                                        return true;
                                    }
                                }else if (editorInput.getAdapter(RepositoryNode.class) != null) {
                                	RepositoryNode node = (RepositoryNode) editorInput.getAdapter(RepositoryNode.class);
                                	IRepositoryViewObject object = node.getObject();
                                	if (object != null && object.getId().equals(objectToMove.getId())
                                            && VersionUtils.compareTo(object.getVersion(), objectToMove.getVersion()) == 0) {
                                        return true;
                                    }
								}
                            }
                        }
                    }
                }
            }
        } catch (PartInitException e) {
            ExceptionHandler.process(e);
        }
        return false;
    }


    /**
     * 
     * ggu Comment method "isEditableItemInEditor".
     * 
     * it's editable also.
     * 
     * for jobs/joblets/business diagrams/routines/sql patterns
     */
    public static boolean isEditableItemInEditor(IRepositoryViewObject objectToMove) {
        try {
            if (objectToMove != null) {
                IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (activeWorkbenchWindow != null) {
                    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                    if (activePage != null) {
                        IEditorReference[] editorReferences = activePage.getEditorReferences();
                        if (editorReferences != null) {
                            Property property = objectToMove.getProperty().getItem().getProperty();

                            for (IEditorReference editorReference : editorReferences) {
                                IEditorInput editorInput = editorReference.getEditorInput();
                                if (editorInput != null) {
                                    IRepositoryEditorInput rInput = null;
                                    // for business/routine/sql pattern
                                    IPersistableElement persistableElement = editorInput.getPersistable();
                                    if (persistableElement != null && persistableElement instanceof IRepositoryEditorInput) {
                                        rInput = (IRepositoryEditorInput) persistableElement;
                                    }
                                    // for job/joblet/routine/sql pattern
                                    if (editorInput instanceof IRepositoryEditorInput) {
                                        rInput = (IRepositoryEditorInput) editorInput;
                                    }
                                    if (rInput != null) {
                                        Property openedProperty = rInput.getItem().getProperty();
                                        if (openedProperty.getId().equals(property.getId())
                                                && VersionUtils.compareTo(openedProperty.getVersion(), property.getVersion()) == 0
                                                && !rInput.isReadOnly()) {

                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (PartInitException e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

}
