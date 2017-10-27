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
package org.talend.designer.runprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
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

    public static List<String> getRequiredPigudfName(IProcess process) {
        Set<String> neededRoutines = process.getNeededPigudf();
        if (neededRoutines != null) {
            return new ArrayList<String>(neededRoutines);
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
