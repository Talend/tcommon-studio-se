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
package org.talend.designer.rowgenerator.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.SourceType;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * Created by bhe on Jun 24, 2021
 *
 *
 */
public class RoutineJarsFunctionParser extends AbstractTalendFunctionParser {

    public static final String MAVEN_PLUGIN_ID = "org.talend.designer.maven";

    private List<String> systems = new ArrayList<String>();

    public RoutineJarsFunctionParser() {
        super();
    }

    @Override
    @SuppressWarnings("restriction")
    public void parse() {
        typeMethods.clear();
        try {
            if (!GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
                return;
            }
            IRepositoryService rpositoryService = (IRepositoryService) GlobalServiceRegister.getDefault()
                    .getService(IRepositoryService.class);

            IProxyRepositoryFactory factory = rpositoryService.getProxyRepositoryFactory();
            List<IRepositoryViewObject> routineJarsObjects = factory.getAll(ERepositoryObjectType.ROUTINESJAR);
            List<CodesJarInfo> jarInfos = new ArrayList<CodesJarInfo>();
            for (IRepositoryViewObject routineJarObject : routineJarsObjects) {
                if (routineJarObject.getProperty().getItem() instanceof RoutinesJarItem) {
                    CodesJarInfo info = CodesJarInfo.create(routineJarObject.getProperty());
                    jarInfos.add(info);
                }
            }

            jarInfos.forEach(e -> {
                try {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                        IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault()
                                .getService(IRunProcessService.class);
                        ITalendProcessJavaProject talendProcessJavaProject = service.getTalendCodesJarJavaProject(e);
                        if (talendProcessJavaProject != null) {
                            IFolder srcFolder = talendProcessJavaProject.getSrcFolder();
                            IPackageFragmentRoot root = talendProcessJavaProject.getJavaProject()
                                    .getPackageFragmentRoot(srcFolder);
                            final List<IJavaElement> elements = new ArrayList<IJavaElement>();

                            addEveryProjectElements(root, elements,
                                    getGroupId() + "." + JavaUtils.JAVA_ROUTINESJAR_DIRECTORY + "." + e.getLabel());

                            // for (IJavaElement element : elements) {
                            // see bug 8055,reversal the getLastName() method
                            for (int i = elements.size(); i > 0; i--) {
                                IJavaElement element = elements.get(i - 1);
                                if (element instanceof ICompilationUnit) {
                                    ICompilationUnit compilationUnit = (ICompilationUnit) element;
                                    IType[] types = compilationUnit.getAllTypes();
                                    if (types.length > 0) {
                                        // SourceType sourceType = (SourceType) types[0];
                                        IMember sourceType = types[0];
                                        if (sourceType != null) {
                                            // processSourceType(sourceType, sourceType.getElementName(),
                                            // sourceType.getFullyQualifiedName(),
                                            // sourceType.getElementName(), false);
                                            processSourceType(sourceType, sourceType.getElementName(),
                                                    types[0].getFullyQualifiedName(), sourceType.getElementName(), false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ExceptionHandler.process(ex);
                }
            });

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.designer.rowgenerator.data.AbstractTalendFunctionParser#processSourceType(org.eclipse.jdt.internal
     * .core.SourceType)
     */
    @SuppressWarnings("restriction")
    @Override
    protected void processSourceType(IMember member, String className, String fullName, String funcName, boolean isSystem) {
        try {
            if (member instanceof SourceType) {
                IMethod[] methods = ((SourceType) member).getMethods();
                for (IMethod method : methods) {
                    super.processSourceType(method, className, fullName, method.getElementName(), systems.contains(className));
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.designer.rowgenerator.data.AbstractTalendFunctionParser#getPackageFragment()
     */
    @Override
    protected String getPackageFragment() {
        return null;
    }

    @Override
    protected ITalendProcessJavaProject getTalendCodeProject() {
        return null;
    }

    protected void addEveryProjectElements(IPackageFragmentRoot root, List<IJavaElement> elements, String packageFragment)
            throws JavaModelException {
        if (root == null || elements == null) {
            return;
        }
        // system
        IPackageFragment Pkg = root.getPackageFragment(packageFragment);
        if (Pkg != null && Pkg.exists()) {
            elements.addAll(Arrays.asList(Pkg.getChildren()));
        }

        ProjectManager projectManager = ProjectManager.getInstance();

        // referenced project.
        projectManager.retrieveReferencedProjects();
        for (Project p : projectManager.getReferencedProjects()) {
            IPackageFragment userPkg = root.getPackageFragment(packageFragment + "." + p.getLabel().toLowerCase()); //$NON-NLS-1$
            if (userPkg != null && userPkg.exists()) {
                elements.addAll(Arrays.asList(userPkg.getChildren()));
            }
        }
    }

    private static String getGroupId() {
        Project p = ProjectManager.getInstance().getCurrentProject();
        ProjectPreferenceManager preferenceManager = new ProjectPreferenceManager(p, MAVEN_PLUGIN_ID, false);
        return preferenceManager.getValue(MavenConstants.PROJECT_GROUPID);
    }

}
