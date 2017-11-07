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
package org.talend.migration.check;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by wchen on Jan 27, 2015 Detailled comment
 *
 */
public abstract class AbsMigrationCheckHandler implements IMigrationCheckHandler {

    protected ERepositoryObjectType objectType;

    protected String type;

    protected String name;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.check.IMigrationCheckHandler#getType()
     */
    @Override
    public String getType() {
        return this.type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.migration.check.IMigrationCheckHandler#setType(org.talend.core.model.repository.ERepositoryObjectType)
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.check.IMigrationCheckHandler#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.check.IMigrationCheckHandler#setName()
     */
    @Override
    public String setName(String name) {
        return this.name = name;
    }

    protected ERepositoryObjectType getObjectType() {
        if (objectType == null && getType() != null) {
            objectType = ERepositoryObjectType.getType(getType());
        }
        return objectType;
    }

    protected List<IRepositoryViewObject> getObjectsToCheck() throws PersistenceException {
        if (getObjectType() != null) {
            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            RootContainer<String, IRepositoryViewObject> processesContainer = factory.getMetadata(getObjectType());
            return processesContainer.getAbsoluteMembers().objects();
        }
        return null;
    }

    protected MigrateItemInfo getItemInfo(Property property) {
        MigrateItemInfo itemInfo = new MigrateItemInfo();
        itemInfo.setAuthor(property.getAuthor().getLogin());
        itemInfo.setCreated(property.getCreationDate());
        itemInfo.setLabel(property.getLabel());
        itemInfo.setLast_modified(property.getModificationDate());
        itemInfo.setType(getType());
        itemInfo.setType_name(getName());
        itemInfo.setVersion(property.getVersion());
        return itemInfo;
    }

    protected void checkProcessComponentMissing(IProcess currentProcess, MigrateItemInfo itemInfo) {
        if (currentProcess instanceof IProcess2) {
            List<NodeType> unloadedNode = ((IProcess2) currentProcess).getUnloadedNode();
            if (unloadedNode != null && !unloadedNode.isEmpty()) {
                for (NodeType nodeType : unloadedNode) {
                    Problem problem = new Problem();
                    problem.setCategory(ProblemCategory.COMPONENT_MISSING);
                    problem.setProblem(nodeType.getComponentName());
                    itemInfo.getProblems().add(problem);
                }
            }

        }
    }

    protected boolean checkCompilationError(IFile codeFile, MigrateItemInfo itemInfo) throws Exception {
        boolean hasCompilationError = false;
        if (codeFile != null && codeFile.exists()) {
            IMarker[] markers = codeFile.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);
            for (IMarker marker : markers) {
                Integer lineNr = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
                String message = (String) marker.getAttribute(IMarker.MESSAGE);
                Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
                if (severity == IMarker.SEVERITY_ERROR) {
                    String errorMessage = message + " at line " + lineNr;
                    Problem problem = new Problem();
                    problem.setCategory(ProblemCategory.COMPILATION_ERROR);
                    problem.setProblem(errorMessage);
                    itemInfo.getProblems().add(problem);
                    hasCompilationError = true;
                }
            }

        } else {
            Problem problem = new Problem();
            problem.setCategory(ProblemCategory.COMPILATION_ERROR);
            problem.setProblem("Can not find source codes :" + codeFile);
            itemInfo.getProblems().add(problem);
            hasCompilationError = true;
        }
        return hasCompilationError;
    }

    protected void checkModules(Set<String> neededLibraries, Set<ModuleNeeded> allModulesWithStatus, MigrateItemInfo itemInfo) {
        // check libraries status
        if (neededLibraries == null || neededLibraries.isEmpty()) {
            return;
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            ILibraryManagerService service = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            for (String libName : neededLibraries) {
                String jarPath = service.getJarPath(libName);
                if (jarPath == null || !new File(jarPath).exists()) {
                    Problem problem = new Problem();
                    problem.setCategory(ProblemCategory.JAR_MISSING);
                    problem.setProblem(libName);
                    itemInfo.getProblems().add(problem);
                }

            }
        }
    }

}
