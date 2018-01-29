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
package org.talend.librariesmanager.ui.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.views.ModulesViewComposite;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class RemoveExternalJarAction extends Action {

    /**
     * qzhang RemoveExternalJarAction constructor comment.
     */
    public RemoveExternalJarAction() {
        super();
        setText("Remove external JARs"); //$NON-NLS-1$
        setDescription("Remove external JARs"); //$NON-NLS-1$
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        setEnabled(true);
        init();
    }

    List<ModuleNeeded> modules;

    public void init() {
        modules = new ArrayList<ModuleNeeded>();
        if (ModulesViewComposite.getTableViewerCreator() != null) {
            ModulesViewComposite.getTableViewerCreator().getTable().addSelectionListener(new SelectionListener() {

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {

                }

                @Override
                public void widgetSelected(SelectionEvent e) {
                    modules.clear();
                    TableItem[] selection = ModulesViewComposite.getTableViewerCreator().getTable().getSelection();
                    for (TableItem tableItem : selection) {
                        ModuleNeeded needed = (ModuleNeeded) tableItem.getData();
                        if (ModuleNeeded.UNKNOWN.equals(needed.getContext())) {
                            modules.add(needed);
                        } else {
                            setEnabled(false);
                            return;
                        }
                    }
                    setEnabled(true);
                }
            });
            setEnabled(false);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

            @Override
            public void run() {
                for (ModuleNeeded module : modules) {
                    try {
                        LibManagerUiPlugin.getDefault().getLibrariesService().undeployLibrary(module.getModuleName());
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                    IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                            IRunProcessService.class);
                    ITalendProcessJavaProject talendProcessJavaProject = processService.getTalendProcessJavaProject();
                    if (talendProcessJavaProject != null) {
                        IJavaProject javaProject = talendProcessJavaProject.getJavaProject();
                        List<IClasspathEntry> projectLibraries = new ArrayList<IClasspathEntry>();
                        try {
                            IClasspathEntry[] resolvedClasspath = javaProject.getResolvedClasspath(true);
                            projectLibraries.addAll(Arrays.asList(resolvedClasspath));
                            for (ModuleNeeded module : modules) {
                                IClasspathEntry foundEntry = null;
                                for (IClasspathEntry entry : resolvedClasspath) {
                                    if (entry.getPath().toPortableString().contains(module.getModuleName())) {
                                        foundEntry = entry;
                                        break;
                                    }
                                }
                                if (foundEntry != null) {
                                    projectLibraries.remove(foundEntry);
                                }
                            }
                            javaProject.setRawClasspath(projectLibraries.toArray(new IClasspathEntry[projectLibraries.size()]),
                                    null);
                            setEnabled(false);
                        } catch (JavaModelException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }

            }
        });
    }
}
