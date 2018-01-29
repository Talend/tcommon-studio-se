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
package org.talend.designer.rowgenerator.data;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.runtime.i18n.Messages;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractTalendFunctionParser extends AbstractFunctionParser {

    private static Logger logger = Logger.getLogger(AbstractTalendFunctionParser.class);

    // k: (Talend type Name).(method Name) v:(class Name).(method Name)
    protected static Map<String, String> typeMethods = new HashMap<String, String>();

    protected static Map<String, String> typePackgeMethods = new HashMap<String, String>();

    public static Map<String, String> getTypeMethods() {
        return typeMethods;
    }

    public static Map<String, String> getTypePackgeMethods() {
        return typePackgeMethods;
    }

    @Override
    public void parse() {
        try {
            if (!GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
                return;
            }
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            ITalendProcessJavaProject talendProcessJavaProject = runProcessService.getTalendProcessJavaProject();
            if (talendProcessJavaProject != null) {
                IFolder srcFolder = talendProcessJavaProject.getSrcFolder();
                IPackageFragmentRoot root = talendProcessJavaProject.getJavaProject().getPackageFragmentRoot(srcFolder);
                List<IJavaElement> elements = new ArrayList<IJavaElement>();
                addEveryProjectElements(root, elements);
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
                                processSourceType(sourceType, sourceType.getElementName(), types[0].getFullyQualifiedName(),
                                        sourceType.getElementName(), false);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    protected void processSourceType(IMember member, String className, String fullName, String funcName, boolean isSystem) {
        if (member != null && GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            try {
                IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                        IDesignerCoreService.class);
                Reader reader = designerCoreService.getJavadocContentAccessContentReader(member);
                if (reader != null) {
                    char[] charBuffer = new char[1024];
                    StringBuffer str = new StringBuffer();
                    int index = 0;
                    while ((index = reader.read(charBuffer)) != -1) {
                        str.append(charBuffer, 0, index);
                        index = 0;
                    }
                    reader.close();
                    parseJavaCommentToFunctions(str.toString(), className, fullName, funcName, isSystem);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    protected void addEveryProjectElements(IPackageFragmentRoot root, List<IJavaElement> elements) throws JavaModelException {
        if (root == null || elements == null) {
            return;
        }
        // system
        IPackageFragment Pkg = root.getPackageFragment(getPackageFragment());
        if (Pkg != null && Pkg.exists()) {
            elements.addAll(Arrays.asList(Pkg.getChildren()));
        }

        ProjectManager projectManager = ProjectManager.getInstance();
        Project currentProject = projectManager.getCurrentProject();
        // current project
        IPackageFragment userPkg = root.getPackageFragment(getPackageFragment() + "." //$NON-NLS-1$
                + currentProject.getLabel().toLowerCase());
        if (userPkg != null && userPkg.exists()) {
            elements.addAll(Arrays.asList(userPkg.getChildren()));
        }
        // referenced project.
        projectManager.retrieveReferencedProjects();
        for (Project p : projectManager.getReferencedProjects()) {
            userPkg = root.getPackageFragment(getPackageFragment() + "." + p.getLabel().toLowerCase()); //$NON-NLS-1$
            if (userPkg != null && userPkg.exists()) {
                elements.addAll(Arrays.asList(userPkg.getChildren()));
            }
        }
    }

    protected abstract String getPackageFragment();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.rowgenerator.data.AbstractFunctionParser#parseDescription(java.lang.String)
     */
    @Override
    protected String parseDescription(String string) {
        String string2 = ""; //$NON-NLS-1$
        if (string.indexOf("{talendTypes}") > 0) { //$NON-NLS-1$
            string2 = string.substring(0, string.indexOf("{talendTypes}")); //$NON-NLS-1$
        }
        return string2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.rowgenerator.data.AbstractFunctionParser#parseFunctionType(java.lang.String)
     */
    @Override
    protected String parseFunctionType(String string) {
        String string2 = super.parseFunctionType(string);
        if (string2 != null && !string2.trim().equals("")) {
            JavaType type = JavaTypesManager.getJavaTypeFromLabel(string2);
            if (type == null) {
                type = JavaTypesManager.getJavaTypeFromName(string2);
            }
            if (type != null) {
                return type.getId();
            }
        }
        return EMPTY_STRING;
    }

    protected Function parseJavaCommentToFunctions(String string, String className, String fullName, String funcName,
            boolean isSystem) {
        try {
            String des = parseDescription(string);
            String category = parseCategoryType(string);
            String functionType = parseFunctionType(string);
            String[] parameter = parseFunctionParameters(string);
            if (functionType != null && category.trim().length() > 0) {
                Parameter[] paras = convertToParameter(parameter);
                Function function = new Function();
                function.setClassName(className);
                function.setName(funcName);
                function.setDescription(des);
                function.setParameters(Arrays.asList(paras));
                if (!category.equals(EMPTY_STRING)) {
                    function.setCategory(category);
                }
                function.setUserDefined(!isSystem);
                TalendType talendType = getTalendType(functionType);
                talendType.addFunctions(function);
                typeMethods.put(functionType + "." + funcName, className + "." + funcName); //$NON-NLS-1$ //$NON-NLS-2$
                typePackgeMethods.put(functionType + "." + funcName, fullName + "." + funcName); //$NON-NLS-1$ //$NON-NLS-2$
                function.setTalendType(talendType);
                return function;
            }
        } catch (Exception e) {
            logger.error(Messages.getString("AbstractTalendFunctionParser.checkMethod", fullName, funcName), e); //$NON-NLS-1$
        }
        return null;
    }
}
