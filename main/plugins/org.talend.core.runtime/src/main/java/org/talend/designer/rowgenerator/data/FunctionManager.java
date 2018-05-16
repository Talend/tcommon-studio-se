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
package org.talend.designer.rowgenerator.data;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.core.service.IPigMapService;

/**
 * class global comment. Detailled comment <br/>
 * 
 * $Id: FunctionManager.java,v 1.13 2007/01/31 05:20:51 pub Exp $
 * 
 */
public class FunctionManager {

    public static final String PURE_PERL_NAME = "..."; //$NON-NLS-1$

    public static final String PURE_PERL_DESC = Messages.getString("FunctionManager.PurePerl.Desc"); //$NON-NLS-1$

    public static final String PURE_PERL_PARAM = Messages.getString("FunctionManager.PurePerl.ParaName"); //$NON-NLS-1$

    private List<TalendType> talendTypes = null;

    public static final String PERL_FUN_PREFIX = "sub{"; //$NON-NLS-1$

    public static final String PERL_FUN_SUFFIX = ")}"; //$NON-NLS-1$

    public static final String FUN_PARAM_SEPARATED = ","; //$NON-NLS-1$

    public static final String JAVA_METHOD_SEPARATED = "."; //$NON-NLS-1$

    public static final String FUN_PREFIX = "("; //$NON-NLS-1$

    public static final String FUN_SUFFIX = ")"; //$NON-NLS-1$

    public static final String EMPTY_STRING = " "; //$NON-NLS-1$

    /**
     * qzhang Comment method "getFunctionByName".
     * 
     * @param name is TalendType name.
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Function> getFunctionsByType(String name) {
        List<Function> funtions = new ArrayList<Function>();

        for (TalendType talendType : talendTypes) {
            if (talendType.getName().equals(name)) {
                funtions.addAll(talendType.getFunctions());
            }
        }
        funtions.add(createCustomizeFunction());
        return funtions;
    }

    private Function createCustomizeFunction() {
        Function function = new Function();
        function.setName(PURE_PERL_NAME);
        function.setDescription(PURE_PERL_DESC);
        StringParameter param = new StringParameter();
        List<Parameter> params = new ArrayList<Parameter>();
        param.setName(PURE_PERL_PARAM);
        params.add(param);
        function.setParameters(params);
        return function;
    }

    public RepositoryContext getRepositoryContext() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    @SuppressWarnings("unchecked")
    public FunctionManager() {
        // this code move to FunctionParser .
        //
        // for (int i = 0; i < list.size(); i++) {
        // URL url = list.get(i);
        // try {
        // url = FileLocator.toFileURL(url);
        // File file = new File(url.getFile());
        // files.add(file);
        // } catch (Exception e) {
        // ExceptionHandler.process(e);
        // }
        // }
        AbstractFunctionParser parser = null;
        parser = new RoutineFunctionParser();
        parser.parse();
        talendTypes = parser.getList();
    }

    @SuppressWarnings("unchecked")
    public FunctionManager(String type) {
        AbstractFunctionParser parser = null;
        if (JavaUtils.JAVA_PIG_DIRECTORY.equals(type)) {
            // pig map expressionbuilder
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IPigMapService.class)) {
                final IPigMapService service = (IPigMapService) GlobalServiceRegister.getDefault().getService(
                        IPigMapService.class);
                parser = service.pigFunctionParser();
                parser.parse();
                talendTypes = parser.getList();
            }
        }
    }

    public static boolean isJavaProject() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        return (codeLanguage == ECodeLanguage.JAVA);
    }

    public List<TalendType> getTalendTypes() {
        return this.talendTypes;
    }

    /**
     * 
     * DOC ggu Comment method "getFunctionMethod".
     * 
     * @param f
     */
    public static String getFunctionMethod(Function f) {
        String newValue = ""; //$NON-NLS-1$
        if (f != null) {

            final List<Parameter> parameters = f.getParameters();
            if (isJavaProject()) {
                String fullName = f.getClassName() + JAVA_METHOD_SEPARATED + f.getName();
                newValue = fullName + FUN_PREFIX;
                for (Parameter pa : parameters) {
                    newValue += pa.getValue() + FUN_PARAM_SEPARATED;
                }
                if (!parameters.isEmpty()) {
                    newValue = newValue.substring(0, newValue.length() - 1);
                }
                newValue += FUN_SUFFIX;

            } else {
                newValue = f.getName() + FUN_PREFIX;
                for (Parameter pa : parameters) {
                    newValue += pa.getValue() + FUN_PARAM_SEPARATED;
                }
                if (!parameters.isEmpty()) {
                    newValue = newValue.substring(0, newValue.length() - 1);
                }
                newValue += FUN_SUFFIX;
            }
        }
        return newValue;
    }

}
