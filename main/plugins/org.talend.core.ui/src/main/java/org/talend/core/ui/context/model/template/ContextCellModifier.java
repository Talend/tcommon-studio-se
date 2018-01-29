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

import org.eclipse.swt.widgets.TreeItem;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.context.ContextTemplateComposite;
import org.talend.core.ui.context.model.AbstractContextCellModifier;

/**
 * ggu class global comment. Detailled comment
 */
public class ContextCellModifier extends AbstractContextCellModifier {

    private ContextTemplateComposite variableTabComposite;

    public ContextCellModifier(ContextTemplateComposite parentComposite) {
        this(parentComposite, false);
        this.variableTabComposite = parentComposite;
    }

    public ContextCellModifier(ContextTemplateComposite parentComposite, boolean reposFlag) {
        super(parentComposite, reposFlag);
        this.variableTabComposite = parentComposite;
    }

    protected ContextTemplateComposite getParentMode() {
        return (ContextTemplateComposite) super.getParentMode();
    }

    private String getParentModelSourceId(ContextVariableTabParentModel parent) {
        boolean isGroupBySource = variableTabComposite.isGroupBySource();
        String sourceId = null;
        if (isGroupBySource)
            sourceId = parent.getSourceId();
        else
            sourceId = parent.getContextParameter().getSource();
        return sourceId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
     */
    public boolean canModify(Object element, String property) {
        if (getModelManager().isReadOnly()) {
            return false;
        }

        if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            String sourceId = getParentModelSourceId(parent);
            if (IContextParameter.BUILT_IN.equals(sourceId)) {
                if (ContextConstant.NAME_COLUMN_NAME.equals(property) || ContextConstant.COMMENT_COLUMN_NAME.equals(property)
                        || ContextConstant.TYPE_COLUMN_NAME.equals(property))
                    return true;
            } else
                return false;
        }

        IContextParameter para = getRealParameter(element);
        if (para == null) {
            return false;
        }

        if (!para.isBuiltIn() && !isRepositoryMode()) {
            // not built-in, not update
            return false;

        }

        if (property.equals(ContextConstant.SOURCE_COLUMN_NAME) || property.equals(ContextConstant.SCRIPTCODE_COLUMN_NAME)) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    public Object getValue(Object element, String property) {
        IContextParameter contextPara = null;
        if (element instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) element;
            contextPara = parent.getContextParameter();
            if (ContextConstant.NAME_COLUMN_NAME.equals(property)) {
                return contextPara.getName();
            } else if (ContextConstant.SOURCE_COLUMN_NAME.equals(property)) {

            } else if (ContextConstant.TYPE_COLUMN_NAME.equals(property)) {
                String s = ContextManagerHelper.convertFormat(contextPara.getType());
                final ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
                if (codeLanguage == ECodeLanguage.JAVA) {
                    for (int i = 0; i < ContextParameterJavaTypeManager.getJavaTypesLabels().length; i++) {
                        if (s.equals(ContextParameterJavaTypeManager.getJavaTypesLabels()[i])) {
                            return i;
                        }
                    }
                    return -1;
                } else {
                    for (int i = 0; i < ContextParameterJavaTypeManager.getPerlTypesLabels().length; i++) {
                        if (s.equals(ContextParameterJavaTypeManager.getPerlTypesLabels()[i])) {
                            return i;
                        }
                    }
                    return -1;
                }
            } else if (ContextConstant.SCRIPTCODE_COLUMN_NAME.equals(property)) {
                final ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
                if (codeLanguage == ECodeLanguage.JAVA)
                    return ContextParameterUtils.getNewScriptCode(contextPara.getName(), codeLanguage);
                else
                    return contextPara.getScriptCode();
            } else if (ContextConstant.COMMENT_COLUMN_NAME.equals(property)) {
                return contextPara.getComment();
            }
        }
        return null; //$NON-NLS-1$
    }

    /**
     * zwang Comment method "getRealParameter".
     * 
     * @param property
     * @param templatePara
     * @return
     */
    private IContextParameter getRealParameter(Object element) {
        IContextParameter para = null;
        IContext context = getContextManager().getDefaultContext();

        if (getParentMode().isGroupBySource()) {
            if (element instanceof ContextVariableTabParentModel) {
                String sourceId = ((ContextVariableTabParentModel) element).getSourceId();
                if (IContextParameter.BUILT_IN.equals(sourceId)) {
                    para = context.getContextParameter(sourceId, ((ContextVariableTabParentModel) element).getContextParameter()
                            .getName());
                }
            } else if (element instanceof ContextVariableTabChildModel) {
                String sourceId = ((ContextVariableTabChildModel) element).getContextParameter().getSource();
                para = context.getContextParameter(sourceId,
                        (((ContextVariableTabChildModel) element).getContextParameter()).getName());
            }
        } else {
            if (element instanceof ContextVariableTabParentModel) {
                para = context.getContextParameter(((ContextVariableTabParentModel) element).getContextParameter().getName());
            }
        }
        return para;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    public void modify(Object element, final String property, final Object value) {
        TreeItem item = (TreeItem) element;
        final Object object = item.getData();
        if (object instanceof ContextVariableTabParentModel) {
            ContextVariableTabParentModel parent = (ContextVariableTabParentModel) object;
            IContextParameter contextPara = parent.getContextParameter();
            String originalName = contextPara.getName();
            String sourceId = contextPara.getSource();
            if (ContextConstant.NAME_COLUMN_NAME.equals(property)) {
                // contextPara.setName((String) value);
                getParentMode().renameParameter(originalName, sourceId, (String) value, isRepositoryMode());
            } else if (ContextConstant.TYPE_COLUMN_NAME.equals(property)) {
                // TODO get the right type.
                int index = -1;
                String s = ContextManagerHelper.convertFormat(contextPara.getType());
                final ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
                if (codeLanguage == ECodeLanguage.JAVA) {
                    for (int i = 0; i < ContextParameterJavaTypeManager.getJavaTypesLabels().length; i++) {
                        if (s.equals(ContextParameterJavaTypeManager.getJavaTypesLabels()[i])) {
                            index = i;
                        }
                    }
                    if (index == ((Integer) value)) {
                        return;
                    }
                    String newType = getRealType(ContextParameterJavaTypeManager.getJavaTypesLabels()[(Integer) value]);
                    String name = contextPara.getName();
                    for (IContext context : getContextManager().getListContext()) {
                        for (IContextParameter contextParameter : context.getContextParameterList()) {
                            if (name.equals(contextParameter.getName())) {
                                contextParameter.setType(newType);
                            }
                        }
                    }
                }
                // String newType = getRealType(ContextParameterJavaTypeManager.getJavaTypesLabels()[(Integer) value]);
                // contextPara.setType((String) newType);
            } else if (ContextConstant.COMMENT_COLUMN_NAME.equals(property)) {
                contextPara.setComment((String) value);
            }
            List<Object> updateObjs = new ArrayList<Object>();
            updateObjs.add(object);
            lookupSameNameNode(contextPara.getSource(), originalName, item, updateObjs);
            updateRelatedNode(updateObjs.toArray(), contextPara);
        }
    }

    private void lookupSameNameFromChilren(String sourceId, String nodeName, TreeItem item, List<Object> updateObjs) {
        TreeItem[] items = item.getItems();
        if (items != null && items.length > 0) {
            for (TreeItem tempItem : items) {
                Object obj = tempItem.getData();
                if (obj instanceof ContextVariableTabChildModel) {
                    ContextVariableTabChildModel son = (ContextVariableTabChildModel) obj;
                    String paraName = son.getContextParameter().getName();
                    if (nodeName.equals(paraName))
                        updateObjs.add(obj);
                }
            }
        }
    }

    /**
     * To look up all nodes that have the same variable name from input model.
     * 
     * @param nodeName
     * @return
     */
    private Object[] lookupSameNameNode(String sourceId, String nodeName, TreeItem item, List<Object> updateObjs) {
        TreeItem[] items = item.getParent().getItems();
        if (items != null && items.length > 0) {
            for (TreeItem tempItem : items) {
                Object obj = tempItem.getData();
                if (obj instanceof ContextVariableTabParentModel) {
                    ContextVariableTabParentModel parent = (ContextVariableTabParentModel) obj;
                    String tempSourceId = parent.getSourceId();
                    if (!IContextParameter.BUILT_IN.equals(tempSourceId))
                        lookupSameNameFromChilren(sourceId, nodeName, tempItem, updateObjs);
                } else if (obj instanceof ContextVariableTabChildModel) {
                    ContextVariableTabChildModel son = (ContextVariableTabChildModel) obj;
                    String paraName = son.getContextParameter().getName();
                    if (nodeName.equals(paraName))
                        updateObjs.add(obj);
                }
            }
        }
        return updateObjs.toArray();
    }

    private String getRealType(String type) {
        final ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            StringBuffer sb = new StringBuffer("id_"); //$NON-NLS-1$
            JavaType javaType = JavaTypesManager.getJavaTypeFromLabel(type);
            if (type.indexOf(ContextConstant.DOWNWARDS_STRING) != -1) {
                return javaType.getId();
            } else {
                if (javaType != null) {
                    return javaType.getId();
                } else {
                    return sb.append(type.trim()).toString();
                }

            }
        } else {
            return type;
        }
    }
}
