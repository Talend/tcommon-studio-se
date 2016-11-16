// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import org.talend.core.model.properties.Project;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.model.ContextProviderProxy;
import org.talend.repository.ProjectManager;

/**
 * A label and content provider for the treeviewer which groups the Contexts by source.
 * 
 */
public class GroupBySourceProvider extends ContextProviderProxy {

    private IContextModelManager modelManager = null;

    public GroupBySourceProvider(IContextModelManager modelManager) {
        super();
        this.modelManager = modelManager;
        if (modelManager == null) {
            throw new NullPointerException();
        }
    }

    private String getColumnTextForBuiltinParent(ContextVariableTabParentModel parent, int columnIndex) {
        IContextParameter contextPara = parent.getContextParameter();
        switch (columnIndex) {
        case 0:
            return contextPara.getName();
        case 1:
            return contextPara.getSource();
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

    private String getColumnTextForNonBuiltinParent(ContextVariableTabParentModel parent, int columnIndex) {
        String sourceName = parent.getSourceName();
        if (sourceName == null)
            sourceName = parent.getSourceId();
        switch (columnIndex) {
        case 0:
        case 1:
            return sourceName;
        default:
            return ContextConstant.LINE_STRING;
        }
    }

    private String getColumnTextForParent(ContextVariableTabParentModel parent, int columnIndex) {
        String sourceId = parent.getSourceId();
        if (IContextParameter.BUILT_IN.equals(sourceId))
            return getColumnTextForBuiltinParent(parent, columnIndex);
        else
            return getColumnTextForNonBuiltinParent(parent, columnIndex);
    }

    private String getColumnTextForChild(ContextVariableTabChildModel child, int columnIndex) {
        IContextParameter contextPara = child.getContextParameter();
        switch (columnIndex) {
        case 0:
            return contextPara.getName();
        case 1:
            String sourceId = contextPara.getSource();
            ContextItem contextItem = ContextUtils.getContextItemById2(sourceId);
            if (contextItem != null)
                return contextItem.getProperty().getLabel();
            else
                return sourceId;
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

    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            return getColumnTextForParent(parent, columnIndex);
        } else if (element instanceof ContextVariableTabChildModel) {
            ContextVariableTabChildModel child = (ContextVariableTabChildModel) element;
            return getColumnTextForChild(child, columnIndex);
        }
        return "";
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    private void handleBuiltInNode(IContextParameter para, List<ContextVariableTabParentModel> output) {
        String sourceId = para.getSource();
        ContextVariableTabParentModel firstLevelNode = new ContextVariableTabParentModel();
        String sourceLabel = sourceId;
        ContextItem contextItem = ContextUtils.getContextItemById2(sourceId);
        if (contextItem != null) {
            sourceLabel = contextItem.getProperty().getLabel();
            final ProjectManager pm = ProjectManager.getInstance();
            if (!pm.isInCurrentMainProject(contextItem)) {
                final Project project = pm.getProject(contextItem);
                if (project != null) {
                    firstLevelNode.setProjectLabel(project.getLabel());
                }
            }
        }
        firstLevelNode.setSourceName(sourceLabel);
        firstLevelNode.setSourceId(sourceId);
        firstLevelNode.setContextParameter(para);
        output.add(firstLevelNode);
    }

    private void handleNonBuiltInNode(IContextParameter para, List<ContextVariableTabParentModel> output) {
        String sourceId = para.getSource();
        ContextVariableTabParentModel firstLevelNode = null;
        if (sourceId != null) {
            firstLevelNode = lookupContextParentForNonBuiltinNode(sourceId, output);
            if (firstLevelNode == null) {
                firstLevelNode = new ContextVariableTabParentModel();
                output.add(firstLevelNode);
                String sourceLabel = sourceId;
                ContextItem contextItem = ContextUtils.getContextItemById2(sourceId);
                if (contextItem != null) {
                    sourceLabel = contextItem.getProperty().getLabel();
                    final ProjectManager pm = ProjectManager.getInstance();
                    if (!pm.isInCurrentMainProject(contextItem)) {
                        final Project project = pm.getProject(contextItem);
                        if (project != null) {
                            firstLevelNode.setProjectLabel(project.getLabel());
                        }
                    }
                }
                firstLevelNode.setSourceName(sourceLabel);
                firstLevelNode.setSourceId(sourceId);
            }

            ContextVariableTabChildModel child = new ContextVariableTabChildModel();
            child.setContextParameter(para);
            child.setParent(firstLevelNode);
            firstLevelNode.addChild(child);
        }
    }

    /**
     * Invoker should ensure that the given parameter <code>sourceId</code> is not <code>null</code>
     * 
     * @param para is not <code>null</code>
     * @param output
     * @return
     */
    private ContextVariableTabParentModel lookupContextParentForNonBuiltinNode(String sourceId,
            List<ContextVariableTabParentModel> output) {
        ContextVariableTabParentModel firstLevelNode = null;
        if (output != null && output.size() > 0) {
            for (ContextVariableTabParentModel parent : output) {
                String tempSourceId = parent.getSourceId();
                if (tempSourceId != null && sourceId.equals(tempSourceId)) {
                    firstLevelNode = parent;
                    break;
                }
            }
        }
        return firstLevelNode;
    }

    public Object[] getElements(Object inputElement) {
        List<IContext> contexts = (List<IContext>) inputElement;

        List<ContextVariableTabParentModel> output = new ArrayList<ContextVariableTabParentModel>();
        if (!contexts.isEmpty()) {
            for (IContextParameter para : contexts.get(0).getContextParameterList()) {
                String sourceId = para.getSource();
                if (IContextParameter.BUILT_IN.equals(sourceId)) {
                    handleBuiltInNode(para, output);
                } else {
                    handleNonBuiltInNode(para, output);
                }
            }
        }
        return output.toArray();
    }

    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) parentElement;
            return parent.getChildren().toArray();
        }
        return new Object[0];
    }

    public Object getParent(Object element) {
        if (element instanceof ContextVariableTabChildModel) {
            ContextVariableTabChildModel child = (ContextVariableTabChildModel) element;
            return child.getParent();
        }
        return null;
    }

    public boolean hasChildren(Object element) {
        if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            return parent.hasChildren();
        }
        return false;
    }

    public Color getForeground(Object element, int columnIndex) {

        return null;
    }

    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof ContextVariableTabChildModel) {
            ContextVariableTabChildModel child = (ContextVariableTabChildModel) element;
            IContextParameter contextPara = child.getContextParameter();
            String name = contextPara.getName();
            if (hasSameNameContextParameters(name))
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
        } else if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            String sourceId = parent.getSourceId();
            if (IContextParameter.BUILT_IN.equals(sourceId)) {
                IContextParameter contextParameter = parent.getContextParameter();
                String name = contextParameter.getName();
                if (hasSameNameContextParameters(name))
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
            }
        }

        return null;
    }

    private boolean hasSameNameContextParameters(String name) {
        boolean has = false;
        IContextManager contextManager = modelManager.getContextManager();
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
