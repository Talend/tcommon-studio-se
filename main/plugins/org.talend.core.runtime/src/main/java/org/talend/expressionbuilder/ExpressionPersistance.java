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
package org.talend.expressionbuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.model.expressionbuilder.Expression;
import org.talend.commons.runtime.model.expressionbuilder.Variable;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.expression.EMFExpression;
import org.talend.core.model.expression.EMFVariable;
import org.talend.core.model.expression.ExpressionFactory;
import org.talend.core.model.expression.ExpressionPackage;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.RepositoryConstants;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionPersistance.java 下午01:26:21 2007-9-24 +0000 (2007-9-24) yzhang $
 * 
 */
public class ExpressionPersistance {

    public static final String           EXPRESSION_FOLDER_NAME    = "ExpressionBuilder"; //$NON-NLS-1$

    public static final String           CONFIGURATION_FOLDER_NAME = "configuration";    //$NON-NLS-1$

    private String                       ownerId;

    private static ExpressionPersistance expressionPersistance;

    private String                       path;

    private ResourceSet                  resourceSet;

    private EList<EObject>               contents;

    private String                       rootFolderPath;

    /**
     * yzhang ExpressionPersistance constructor comment.
     */
    private ExpressionPersistance() {
    }

    /**
     * yzhang Comment method "getInstance".
     * 
     * @return
     */
    public static ExpressionPersistance getInstance() {
        if (expressionPersistance == null) {
            expressionPersistance = new ExpressionPersistance();
        }
        return expressionPersistance;
    }

    /**
     * Getter for ownerId.
     * 
     * @return the ownerId
     */
    public String getOwnerId() {
        return this.ownerId;
    }

    /**
     * Sets the ownerId.
     * 
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path.
     * 
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * yzhang Comment method "convert".
     * 
     * @param expression
     * @return
     */
    private EMFExpression convert(Expression expression) {

        EMFExpression emfExpression = ExpressionFactory.eINSTANCE.createEMFExpression();
        emfExpression.setId(this.ownerId);
        emfExpression.setExpression(expression.getExpression());
        for (Variable variable : expression.getVariables()) {
            EMFVariable emfVariable = ExpressionFactory.eINSTANCE.createEMFVariable();
            emfVariable.setName(variable.getName());
            emfVariable.setValue(variable.getValue());
            emfVariable.setTalendType(variable.getTalendType());
            emfVariable.setNullable(variable.isNullable());
            emfExpression.getVariables().add(emfVariable);
        }

        return emfExpression;
    }

    /**
     * yzhang Comment method "convert".
     * 
     * @param emfExpression
     * @return
     */
    private Expression convert(EMFExpression emfExpression) {

        List<Variable> vars = new ArrayList<Variable>();

        for (EMFVariable emfVar : emfExpression.getVariables()) {
            vars.add(new Variable(emfVar.getName(), emfVar.getValue(), emfVar.getTalendType(), emfVar.isNullable()));
        }

        return new Expression(emfExpression.getExpression(), vars);
    }

    /**
     * yzhang Comment method "saveExpression".
     * 
     * @param expression
     */
    public void saveExpression(Expression expression) {

        boolean fileExist = initContents();

        EMFExpression emfExpression = convert(expression);
        Resource resource = resourceSet.createResource(URI.createURI("http:///My.expression")); //$NON-NLS-1$

        File file = new File(path);

        if (this.contents != null && fileExist) {
            for (EObject eObject : this.contents) {
                if (eObject instanceof EMFExpression) {
                    if (ownerId != null && ((EMFExpression) eObject).getId() != null
                            && ((EMFExpression) eObject).getId().equals(this.ownerId)) {
                        this.contents.remove(eObject);
                        break;
                    }
                }
            }
            this.contents.clear();
            this.contents.add(emfExpression);
            resource.getContents().clear();
            resource.getContents().addAll(this.contents);
        } else {
            resource.getContents().clear();
            resource.getContents().add(emfExpression);
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            HashMap options = new HashMap(2);
            options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
            options.put(XMLResource.OPTION_XML_VERSION, "1.1"); //$NON-NLS-1$
            resource.save(new FileOutputStream(file), options);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

    }

    /**
     * yzhang Comment method "loadExpression".
     * 
     * @return
     */
    public Expression loadExpression() {
        try {
            if (!initContents()) {
                return new Expression();
            }

            for (EObject eObject : contents) {
                if (eObject instanceof EMFExpression && ownerId != null && ((EMFExpression) eObject).getId() != null
                        && ((EMFExpression) eObject).getId().equals(this.ownerId)) {
                    return convert((EMFExpression) eObject);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            // the xml file may be corrupted, just delete it in order to open the expression builder. the see bug
            // 0004886: Can't open Expression builder in Tmap
            File file = new File(path);
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception ex) {
                    ExceptionHandler.process(ex);
                }
            }
        }
        return new Expression();
    }

    /**
     * yzhang Comment method "initContents".
     */
    private boolean initContents() {

        resourceSet = new ResourceSetImpl();

        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
                .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

        resourceSet.getPackageRegistry().put(ExpressionPackage.eNS_URI, ExpressionPackage.eINSTANCE);

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        URI uri = URI.createFileURI(file.getAbsolutePath());
        Map options = resourceSet.getLoadOptions();
        Resource resource = resourceSet.getResource(uri, true);
        this.contents = resource.getContents();

        return true;
    }

    /**
     * yzhang Comment method "jobNameChanged".
     * 
     * @param oldJobName
     * @param newJobName
     */
    public void jobNameChanged(String oldJobName, String newJobName) {
        IPath oldFilePath = new Path(getExpressionStoreFolderPath()).append(oldJobName + XmlUtil.FILE_XML_SUFFIX);
        IPath newFilePath = new Path(getExpressionStoreFolderPath()).append(newJobName + XmlUtil.FILE_XML_SUFFIX);
        File oldFile = new File(oldFilePath.toOSString());
        if (!oldFile.exists()) {
            return;
        }
        File newFile = new File(newFilePath.toOSString());
        oldFile.renameTo(newFile);
    }

    /**
     * yzhang Comment method "jobDeleted".
     * 
     * @param jobName
     */
    public void jobDeleted(String jobName) {
        IPath filePath = new Path(getExpressionStoreFolderPath()).append(jobName + XmlUtil.FILE_XML_SUFFIX);
        File file = new File(filePath.toOSString());
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * yzhang Comment method "getExpressionStoreFolder".
     * 
     * @return
     */
    private String getExpressionStoreFolderPath() {

        if (rootFolderPath == null) {

            if (path == null) {
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                        .getProperty(Context.REPOSITORY_CONTEXT_KEY);
                Project project = repositoryContext.getProject();
                IProject p = root.getProject(project.getTechnicalLabel());
                String projectPath = p.getLocation().toPortableString() + File.separator + RepositoryConstants.TEMP_DIRECTORY;
                String configurationPath = projectPath + File.separator + ExpressionPersistance.CONFIGURATION_FOLDER_NAME;
                configurationPath = getValidFolderPath(configurationPath, 1);

                String expressionPath = configurationPath + File.separator + ExpressionPersistance.EXPRESSION_FOLDER_NAME;
                expressionPath = getValidFolderPath(expressionPath, 1);
                File expressionFolder = new File(expressionPath);

                path = expressionFolder.getAbsolutePath();
            }

            int index = path.indexOf(EXPRESSION_FOLDER_NAME) + EXPRESSION_FOLDER_NAME.length();

            rootFolderPath = path.substring(0, index);
        }

        return rootFolderPath;

    }

    public String getValidFolderPath(String path, int suffix) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            path = path + suffix;
            return getValidFolderPath(path, suffix);
        }
        return path;

    }

}
