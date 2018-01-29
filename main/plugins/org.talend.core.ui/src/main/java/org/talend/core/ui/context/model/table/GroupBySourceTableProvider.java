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
package org.talend.core.ui.context.model.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
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
import org.talend.core.ui.context.ContextTableValuesComposite;
import org.talend.core.ui.context.model.ContextProviderProxy;
import org.talend.core.ui.context.model.ContextTabChildModel;
import org.talend.core.ui.context.model.template.ContextConstant;
import org.talend.repository.ProjectManager;

/**
 * cli class global comment. Detailled comment
 */
public class GroupBySourceTableProvider extends ContextProviderProxy {

    private ContextTableValuesComposite parentModel;

    public GroupBySourceTableProvider(ContextTableValuesComposite parentModel) {
        super();
        this.parentModel = parentModel;
    }

    /**
     * Looks up the context parameter value under the specified context.
     * 
     * @param sourceId
     * @param contextParaName
     * @param index
     * @return
     */
    private String lookupContextParameterValue(String sourceId, String contextParaName, int index) {
        IContextParameter contextPara = lookupContextParameter(sourceId, contextParaName, index);
        if (contextPara != null) {
            return ContextParameterUtils.checkAndHideValue(contextPara);
        }
        return null;
    }

    /**
     * Looks up the <code>IContextParameter</code> from context list by the specified parameters.
     * 
     * @param sourceId
     * @param contextParaName
     * @param index
     * @return
     */
    private IContextParameter lookupContextParameter(String sourceId, String contextParaName, int index) {
        List<IContext> contextList = parentModel.getContexts();
        int size = contextList.size();
        if (index < size) {
            IContext context = contextList.get(index);
            List<IContextParameter> list = context.getContextParameterList();
            if (list != null && list.size() > 0) {
                for (IContextParameter contextPara : list) {
                    String tempSourceId = contextPara.getSource();
                    String tempContextParaName = contextPara.getName();
                    if (tempSourceId.equals(sourceId) && tempContextParaName.equals(contextParaName)) {
                        return contextPara;
                    }
                }
            }
        }
        return null;
    }

    private String getColumnTextForParent(ContextTableTabParentModel parent, int columnIndex) {
        String text = "";
        String sourceId = parent.getSourceId();
        if (columnIndex == 0) {
            if (IContextParameter.BUILT_IN.equals(sourceId)) {
                return parent.getContextParameter().getName();
            } else {
                return parent.getSourceName();
            }
        } else if (columnIndex == 1) {
            if (IContextParameter.BUILT_IN.equals(sourceId)) {
                String contextParaType = parent.getContextParameter().getType();
                JavaType javaType = ContextParameterJavaTypeManager.getJavaTypeFromId(contextParaType);
                if (javaType != null) {
                    return javaType.getLabel();
                } else {
                    return contextParaType;
                }
            } else {
                List<ContextTabChildModel> children = parent.getChildren();
                if (children != null && children.size() > 0) {
                    for (ContextTabChildModel child : children) {
                        IContextParameter contextPara = child.getContextParameter();
                        String paraName = contextPara.getName();
                        String childSourceId = contextPara.getSource();
                        IContextParameter foundContextPara = lookupContextParameter(childSourceId, paraName, columnIndex);
                        if (foundContextPara != null) {
                            String contextParaType = foundContextPara.getType();
                            JavaType javaType = ContextParameterJavaTypeManager.getJavaTypeFromId(contextParaType);
                            if (javaType != null) {
                                return javaType.getLabel();
                            } else {
                                return contextParaType;
                            }
                        }
                    }
                }
            }
        } else {
            // Returns the other column text, note that the columnIndex is not fixed.
            if (IContextParameter.BUILT_IN.equals(sourceId)) {
                String contextParaName = parent.getContextParameter().getName();
                return lookupContextParameterValue(sourceId, contextParaName, columnIndex - 2);
            } else {
                List<ContextTabChildModel> children = parent.getChildren();
                StringBuffer sb = new StringBuffer();
                if (children != null && children.size() > 0) {
                    for (ContextTabChildModel child : children) {
                        IContextParameter contextPara = child.getContextParameter();
                        String paraName = contextPara.getName();
                        String childSourceId = contextPara.getSource();
                        IContextParameter foundContextPara = lookupContextParameter(childSourceId, paraName, columnIndex - 1);
                        if (foundContextPara != null) {
                            String value = foundContextPara.getValue();
                            if (ContextConstant.NULL_STRING.equals(value)) {
                                sb.append("" + "/");
                            } else {
                                sb.append(ContextParameterUtils.checkAndHideValue(foundContextPara) + "/");
                            }
                        }
                    }
                    if (sb.toString().lastIndexOf("/") != -1) {
                        return sb.toString().substring(0, sb.toString().lastIndexOf("/"));
                    } else {
                        return sb.toString();
                    }
                }
            }
        }

        return text;
    }

    private String getColumnTextForChild(ContextTableTabChildModel child, int columnIndex) {
        String text = "";
        String sourceId = child.getContextParameter().getSource();
        String variableName = child.getContextParameter().getName();
        List<IContext> contextList = parentModel.getContexts();
        int size = contextList.size();
        if (columnIndex == 0) {
            return child.getContextParameter().getName();
        } else if (columnIndex == 1) {
            String contextParaType = child.getContextParameter().getType();
            JavaType javaType = ContextParameterJavaTypeManager.getJavaTypeFromId(contextParaType);
            if (javaType != null) {
                return javaType.getLabel();
            } else {
                return contextParaType;
            }
        } else if (columnIndex - 2 < size) {
            IContext context = contextList.get(columnIndex - 2);
            List<IContextParameter> list = context.getContextParameterList();
            if (list != null && list.size() > 0) {
                for (IContextParameter contextPara : list) {
                    String tempName = contextPara.getName();
                    String tempSourceId = contextPara.getSource();
                    if (tempName.equals(variableName) && tempSourceId.equals(sourceId)) {
                        return ContextParameterUtils.checkAndHideValue(contextPara);
                    }
                }
            }
        }
        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel parent = (ContextTableTabParentModel) element;
            return getColumnTextForParent(parent, columnIndex);
        } else if (element instanceof ContextTableTabChildModel) {
            ContextTableTabChildModel child = (ContextTableTabChildModel) element;
            return getColumnTextForChild(child, columnIndex);
        }

        return "";
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
    @Override
    @SuppressWarnings("unchecked")
    public Object[] getElements(Object inputElement) {
        List<IContextParameter> contexts = (List<IContextParameter>) inputElement;

        List<ContextTableTabParentModel> output = new ArrayList<ContextTableTabParentModel>();
        if (!contexts.isEmpty()) {
            for (IContextParameter para : contexts) {
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

    private void handleBuiltInNode(IContextParameter para, List<ContextTableTabParentModel> output) {
        String sourceId = para.getSource();
        ContextTableTabParentModel firstLevelNode = new ContextTableTabParentModel();
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

    /**
     * Invoker should ensure that the given parameter <code>sourceId</code> is not <code>null</code>
     * 
     * @param para is not <code>null</code>
     * @param output
     * @return
     */
    private ContextTableTabParentModel lookupContextParentForNonBuiltinNode(String sourceId,
            List<ContextTableTabParentModel> output) {
        ContextTableTabParentModel firstLevelNode = null;
        if (output != null && output.size() > 0) {
            for (ContextTableTabParentModel parent : output) {
                String tempSourceId = parent.getSourceId();
                if (tempSourceId != null && sourceId.equals(tempSourceId)) {
                    firstLevelNode = parent;
                    break;
                }
            }
        }
        return firstLevelNode;
    }

    private void handleNonBuiltInNode(IContextParameter para, List<ContextTableTabParentModel> output) {
        String sourceId = para.getSource();
        ContextTableTabParentModel firstLevelNode = null;
        if (sourceId != null) {
            firstLevelNode = lookupContextParentForNonBuiltinNode(sourceId, output);
            if (firstLevelNode == null) {
                firstLevelNode = new ContextTableTabParentModel();
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

            ContextTableTabChildModel child = new ContextTableTabChildModel();
            child.setSourceId(sourceId);
            child.setContextParameter(para);
            child.setParent(firstLevelNode);
            firstLevelNode.addChild(child);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel parent = (ContextTableTabParentModel) parentElement;
            return parent.getChildren().toArray();
        }
        return new Object[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    @Override
    public Object getParent(Object element) {
        if (element instanceof ContextTableTabChildModel) {
            ContextTableTabChildModel child = (ContextTableTabChildModel) element;
            return child.getParent();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel parent = (ContextTableTabParentModel) element;
            return parent.hasChildren();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
     */
    @Override
    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    @Override
    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof ContextTableTabChildModel) {
            ContextTableTabChildModel child = (ContextTableTabChildModel) element;
            IContextParameter contextPara = child.getContextParameter();
            String name = contextPara.getName();
            if (hasSameNameContextParameters(name)) {
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
            }
        } else if (element instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel parent = (ContextTableTabParentModel) element;
            IContextParameter contextPara = parent.getContextParameter();
            if (contextPara != null) {
                String name = parent.getContextParameter().getName();
                if (hasSameNameContextParameters(name)) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
            }
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
        IContextManager contextManager = parentModel.getContextModelManager().getContextManager();
        IContext context = contextManager.getDefaultContext();
        if (context instanceof JobContext) {
            JobContext jobContext = (JobContext) context;
            int size = jobContext.getSameNameContextParameterSize(name);
            if (size > 1) {
                has = true;
            }
        }
        return has;
    }
}
