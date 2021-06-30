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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;

/**
 * Created by bhe on Jun 24, 2021
 *
 *
 */
public class RoutineJarsFunctionParser extends AbstractTalendFunctionParser {

    public static final String MAVEN_PLUGIN_ID = "org.talend.designer.maven";

    private List<String> systems = new ArrayList<String>();

    private final Set<CodesJarInfo> infos = new HashSet<CodesJarInfo>();

    public RoutineJarsFunctionParser() {
        super();
        initProcessCodesJarInfo();
    }

    private void initProcessCodesJarInfo() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            IRepositoryObject process = (IRepositoryObject) service.getActiveProcess();
            Item processItem = process.getProperty().getItem();
            if (processItem instanceof ProcessItem) {
                ProcessType pt = ((ProcessItem) processItem).getProcess();

                List<RoutinesParameterType> rps = pt.getParameters().getRoutinesParameter();

                rps.forEach(rp -> {
                    CodesJarInfo info = CodesJarResourceCache.getCodesJarById(rp.getId());
                    if (info != null) {
                        infos.add(info);
                    }
                });
            }
        }
    }

    @Override
    @SuppressWarnings("restriction")
    public void parse() {
        typeMethods.clear();
        try {

            Set<CodesJarInfo> jarInfos = CodesJarResourceCache.getAllCodesJars();

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

    @Override
    protected Function parseJavaCommentToFunctions(String string, String className, String fullName, String funcName,
            boolean isSystem) {
        Function func = super.parseJavaCommentToFunctions(string, className, fullName, funcName, isSystem);

        // set routine jars dependency missing or not
        infos.forEach(info -> {
            if (func.getRoutineJarName().equals(info.getLabel())) {
                func.setRountineJarDependencyMissing(false);
            }
        });

        return func;
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
