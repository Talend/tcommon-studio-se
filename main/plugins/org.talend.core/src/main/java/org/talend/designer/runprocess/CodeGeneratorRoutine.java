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
package org.talend.designer.runprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.runtime.services.IDesignerMavenService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * Build RoutineName for PerlHeader.
 *
 * $Id: CodeGeneratorRoutine.java 14854 2008-06-06 16:05:36Z mhelleboid $
 *
 */
public final class CodeGeneratorRoutine {

    /**
     * Default Constructor. Must not be used.
     */
    private CodeGeneratorRoutine() {
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRequiredRoutineName(IProcess process) {
        Set<String> neededRoutines = process.getNeededRoutines();
        ECodeLanguage currentLanguage = LanguageManager.getCurrentLanguage();
        String perlConn = "::"; //$NON-NLS-1$
        String builtInPath = ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + perlConn + "system" + perlConn; //$NON-NLS-1$

        if (neededRoutines == null || neededRoutines.isEmpty()) {
            try {
                IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
                List<IRepositoryViewObject> routines = factory.getAll(ProjectManager.getInstance().getCurrentProject(),
                        ERepositoryObjectType.ROUTINES);
                for (Project project : ProjectManager.getInstance().getAllReferencedProjects()) {
                    List<IRepositoryViewObject> routinesFromRef = factory.getAll(project, ERepositoryObjectType.ROUTINES);
                    for (IRepositoryViewObject routine : routinesFromRef) {
                        if (!((RoutineItem) routine.getProperty().getItem()).isBuiltIn()) {
                            routines.add(routine);
                        }
                    }
                }
                neededRoutines = new HashSet<String>();
                for (IRepositoryViewObject object : routines) {
                    neededRoutines.add(object.getLabel());
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        if (currentLanguage == ECodeLanguage.PERL) {
            List<IRepositoryViewObject> routines;
            try {
                IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
                routines = factory.getAll(ERepositoryObjectType.ROUTINES);
                for (Project project : ProjectManager.getInstance().getAllReferencedProjects()) {
                    List<IRepositoryViewObject> routinesFromRef = factory.getAll(project, ERepositoryObjectType.ROUTINES);
                    for (IRepositoryViewObject routine : routinesFromRef) {
                        if (!((RoutineItem) routine.getProperty().getItem()).isBuiltIn()) {
                            routines.add(routine);
                        }
                    }
                }
                Set<String> newNeededRoutines = new HashSet<String>();
                for (IRepositoryViewObject object : routines) {
                    if (neededRoutines.contains(object.getLabel())) {
                        neededRoutines.remove(object.getLabel());
                        if (((RoutineItem) object.getProperty().getItem()).isBuiltIn()) {
                            newNeededRoutines.add(builtInPath + object.getLabel());
                        } else {
                            String userPath = ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + perlConn
                                    + ProjectManager.getInstance().getProject(object.getProperty().getItem()).getTechnicalLabel()
                                    + perlConn;
                            newNeededRoutines.add(userPath + object.getLabel());
                        }
                    }
                }
                neededRoutines = newNeededRoutines;
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return new ArrayList<String>(neededRoutines);
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRequiredCodesJarName(IProcess process) {
        IDesignerMavenService designerMavenService = IDesignerMavenService.get();
        List<String> neededCodesJars = new ArrayList<>();
        if (process instanceof IProcess2) {
            List<Item> all = new ArrayList<>();
            Item currentItem = ((IProcess2) process).getProperty().getItem();
            all.add(currentItem);
            if (currentItem instanceof ProcessItem) {
                ITestContainerProviderService testContainerService = ITestContainerProviderService.get();
                if (testContainerService != null && testContainerService.isTestContainerItem(currentItem)) {
                    try {
                        all.add(testContainerService.getParentJobItem(currentItem));
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                }
                all.addAll(ProcessorUtilities.getChildrenJobInfo(currentItem, false, true).stream().filter(JobInfo::isJoblet)
                        .map(info -> info.getJobletProperty().getItem()).collect(Collectors.toSet()));
                all.forEach(item -> {
                    EList<RoutinesParameterType> routinesParameterTypes = null;
                    if (item instanceof ProcessItem && ((ProcessItem) item).getProcess() != null
                            && ((ProcessItem) item).getProcess().getParameters() != null) {
                        routinesParameterTypes = ((ProcessItem) item).getProcess().getParameters().getRoutinesParameter();
                    } else if (item instanceof JobletProcessItem && ((JobletProcessItem) item).getJobletProcess() != null
                            && ((JobletProcessItem) item).getJobletProcess().getParameters() != null) {
                        routinesParameterTypes = ((JobletProcessItem) item).getJobletProcess().getParameters()
                                .getRoutinesParameter();
                    }
                    if (routinesParameterTypes != null) {
                        routinesParameterTypes.stream().filter(r -> r.getType() != null)
                                .map(r -> CodesJarResourceCache.getCodesJarById(r.getId()))
                                .filter(info -> info != null && hasInnerCodes(info))
                                .forEach(info -> neededCodesJars.add(designerMavenService.getImportGAVPackageForCodesJar(info)));
                    }
                });
            }
        }
        return neededCodesJars;
    }

    private static boolean hasInnerCodes(CodesJarInfo info) {
        try {
            IFolder folder = ResourceUtils
                    .getProject(ProjectManager.getInstance().getProjectFromProjectTechLabel(info.getProjectTechName()))
                    .getFolder(ERepositoryObjectType.getFolderName(info.getType())).getFolder(info.getLabel());
            return folder.exists() && folder.members().length > 0;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    /**
     * 
     * Deprecate for won't use pigudf any more
     * 
     * @param process
     * @return
     */
    @Deprecated
    public static List<String> getRequiredPigudfName(IProcess process) {
        return Collections.EMPTY_LIST;
    }
}
