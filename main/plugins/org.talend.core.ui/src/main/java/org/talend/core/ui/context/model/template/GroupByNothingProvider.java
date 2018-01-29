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
package org.talend.core.ui.context.model.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.model.ContextProviderProxy;

/**
 * A label and content provider for the treeviewer which groups the Contexts by nothing.
 * 
 */
public class GroupByNothingProvider extends ContextProviderProxy {

    private IContextModelManager modelManager = null;

    public GroupByNothingProvider(IContextModelManager modelManager) {
        super();
        this.modelManager = modelManager;
        if (modelManager == null) {
            throw new NullPointerException();
        }
    }

    private IContextManager getContextManager() {
        if (modelManager != null) {
            return modelManager.getContextManager();
        }
        return null;
    }

    private String getColumnTextForJobContext(ContextVariableTabParentModel parent, int columnIndex) {
        IContextParameter contextPara = parent.getContextParameter();
        switch (columnIndex) {
        case 0:
            return contextPara.getName();
        case 1:
            String sourceId = contextPara.getSource();
            if (IContextParameter.BUILT_IN.equals(sourceId))
                return IContextParameter.BUILT_IN;
            else {
                ContextItem contextItem = ContextUtils.getContextItemById2(sourceId);
                if (contextItem != null)
                    return contextItem.getProperty().getLabel();
                else
                    return sourceId;
            }
        case 2:
            String type = contextPara.getType();
            JavaType javaType = ContextParameterJavaTypeManager.getJavaTypeFromId(type);
            if (javaType != null)
                return javaType.getLabel();
            else
                return type;
        case 3:
            final ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
            if (codeLanguage == ECodeLanguage.JAVA)
                return ContextParameterUtils.getNewScriptCode(contextPara.getName(), codeLanguage);
            else
                return contextPara.getScriptCode();
        case 4:
            return contextPara.getComment();
        default:
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            if (columnIndex == 0)
                return getColumnTextForJobContext(parent, 0);
            else if (columnIndex > 0) {
                if (!modelManager.isRepositoryContext()) {
                    return getColumnTextForJobContext(parent, columnIndex);
                } else {
                    return getColumnTextForJobContext(parent, columnIndex + 1);
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ConextTreeValuesComposite.ProviderProxy#getColumnImage(java.lang.Object, int)
     */
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        List<IContext> contexts = (List<IContext>) inputElement;
        List<ContextVariableTabParentModel> output = new ArrayList<ContextVariableTabParentModel>();
        if (contexts != null && contexts.size() > 0) {
            IContext context = contexts.get(0);
            if (context != null) {
                List<IContextParameter> contextParams = context.getContextParameterList();
                for (IContextParameter contextParameter : contextParams) {
                    ContextVariableTabParentModel parent = new ContextVariableTabParentModel();
                    parent.setContextParameter(contextParameter);
                    output.add(parent);
                }
            }
        }
        return output.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) parentElement;
            return parent.getChildren().toArray();
        }
        return new Object[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        if (element instanceof ContextVariableTabChildModel) {
            ContextVariableTabChildModel child = (ContextVariableTabChildModel) element;
            return child.getParent();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            return parent.hasChildren();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
     */
    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            IContextParameter contextPara = parent.getContextParameter();
            String name = contextPara.getName();
            if (hasSameNameContextParameters(name))
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
        }
        return null;
    }

    /**
     * 
     * @param name
     * @return
     */
    private boolean hasSameNameContextParameters(String name) {
        boolean has = false;
        IContextManager contextManager = getContextManager();
        IContext context = contextManager.getDefaultContext();
        if (context instanceof JobContext) {
            JobContext jobContext = (JobContext) context;
            int size = jobContext.getSameNameContextParameterSize(name);
            if (size > 1)
                has = true;
        }
        return has;
    }
}
