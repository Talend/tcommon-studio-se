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
package org.talend.core.repository.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.MultiKeyMap;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryEditorInput;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * cli class global comment. Detailled comment
 */
public final class DeleteActionCache {

    private static DeleteActionCache singleton = null;

    private DeleteActionCache() {
        singleton = this;
    }

    public static DeleteActionCache getInstance() {
        if (singleton == null) {
            singleton = new DeleteActionCache();
        }
        return singleton;
    }

    public void createRecords() {
        clearRecords();

        setOpenedProcessList(RepositoryManagerHelper.getOpenedProcess());
        setOpenProcessMap(createOpenProcessMap(getOpenedProcessList()));

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        List<IRepositoryViewObject> tmpProcessList = new ArrayList<IRepositoryViewObject>(50);
        try {

            ERepositoryObjectType jobType = ERepositoryObjectType.PROCESS;
            if (jobType != null) {
                List<IRepositoryViewObject> jobs = factory.getAll(jobType, true);
                tmpProcessList.addAll(jobs);
            }
            ERepositoryObjectType jobletType = ERepositoryObjectType.JOBLET;
            if (jobletType != null) {
                List<IRepositoryViewObject> jobletes = factory.getAll(jobletType, true);
                tmpProcessList.addAll(jobletes);
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        setProcessList(tmpProcessList);
    }

    private static MultiKeyMap createOpenProcessMap(List<IProcess2> openedProcessList) {
        MultiKeyMap map = new MultiKeyMap();
        if (openedProcessList != null) {
            for (IProcess2 process : openedProcessList) {
                map.put(process.getId(), process.getName(), process.getVersion(), process);
            }
        }
        return map;
    }

    /**
     * 
     * cli Comment method "clearRecords".
     * 
     * revert the original values.
     */
    public void clearRecords() {
        List<IProcess2> list = getOpenedProcessList();
        if (list != null) {
            list.clear();
        }
        List<IRepositoryViewObject> listobj = getProcessList();
        if (listobj != null) {
            listobj.clear();
        }
        MultiKeyMap map = getOpenProcessMap();
        if (map != null) {
            map.clear();
        }
        map = getRepositoryObjectMap();
        if (map != null) {
            map.clear();
        }

    }

    /*
     * 
     */
    private List<IProcess2> openedProcessList;

    private MultiKeyMap openProcessMap;

    private List<IRepositoryViewObject> processList;

    public List<IProcess2> getOpenedProcessList() {
        return this.openedProcessList;
    }

    private void setOpenedProcessList(List<IProcess2> openedProcessList) {
        this.openedProcessList = openedProcessList;
    }

    public MultiKeyMap getOpenProcessMap() {
        return this.openProcessMap;
    }

    private void setOpenProcessMap(MultiKeyMap openProcessMap) {
        this.openProcessMap = openProcessMap;
    }

    public List<IRepositoryViewObject> getProcessList() {
        return this.processList;
    }

    public void setProcessList(List<IRepositoryViewObject> processList) {
        this.processList = processList;
    }

    /*
     * work for documentation.
     */
    private MultiKeyMap repositoryObjectMap = new MultiKeyMap();

    private MultiKeyMap getRepositoryObjectMap() {
        return this.repositoryObjectMap;
    }

    private static final boolean DEFAULT_VALUE = true;

    private boolean docRefresh = DEFAULT_VALUE;

    private boolean getAlways = DEFAULT_VALUE;

    private boolean isAlways() {
        return this.getAlways;
    }

    /**
     * 
     * cli Comment method "setGetAlways".
     * 
     * work for documentation
     */
    public void setGetAlways(boolean always) {
        this.getAlways = always;
    }

    public boolean isDocRefresh() {
        return this.docRefresh;
    }

    public void setDocRefresh(boolean docRefresh) {
        this.docRefresh = docRefresh;
    }

    public void revertParameters() {
        clearRecords();
        setGetAlways(DEFAULT_VALUE);
        setDocRefresh(DEFAULT_VALUE);

    }

    @SuppressWarnings("unchecked")
    public List<IRepositoryObject> getRepositoryObjects(Project project, ERepositoryObjectType type, boolean withDeleted)
            throws PersistenceException {
        Object result = null;
        if (!isAlways()) {
            result = getRepositoryObjectMap().get(project, type);
        }
        if (result == null) {
            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            List<IRepositoryViewObject> all = factory.getAll(project, type, true, true);
            getRepositoryObjectMap().put(project, type, all);

            if (withDeleted) {
                result = all;
            } else {
                List<IRepositoryViewObject> noDeleted = new ArrayList<IRepositoryViewObject>();
                for (IRepositoryViewObject obj : all) {
                    ERepositoryStatus status = factory.getStatus(obj);
                    if (ERepositoryStatus.DELETED != status) {
                        noDeleted.add(obj);
                    }
                }
                result = noDeleted;
            }

        }
        return (List<IRepositoryObject>) result;
    }

    /**
     * 
     * ggu Comment method "closeRelations".
     * 
     * bug 12883
     */
    public void closeOpenedEditor(final IRepositoryViewObject objToDelete) {
        if (objToDelete != null) {
            Display disp = Display.getCurrent();
            if (disp == null) {
                disp = Display.getDefault();
            }
            if (disp != null) {
                disp.syncExec(new Runnable() {

                    @Override
                    public void run() {
                        closeEditor(objToDelete);
                    }
                });
            } else {
                closeEditor(objToDelete);
            }
        }
    }

    private void closeEditor(final IRepositoryViewObject objToDelete) {
        if (objToDelete != null) {
            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null) {
                IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
                if (page != null) {
                    for (IEditorReference editors : page.getEditorReferences()) {
                        IEditorPart editor = editors.getEditor(false);
                        if (editor != null) {
                            IEditorInput editorInput = editor.getEditorInput();
                            String id = null;
                            if (editorInput != null && editorInput instanceof IRepositoryEditorInput) {
                                Item item = ((IRepositoryEditorInput) editorInput).getItem();
                                if (item != null) {
                                    id = item.getProperty().getId();
                                }
                            }
                            if (objToDelete.getId() != null && objToDelete.getId().equals(id)) {
                                page.closeEditor(editor, false);
                            }
                        }
                    }
                }
            }
        }
    }
}
