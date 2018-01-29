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

import org.eclipse.swt.widgets.TreeItem;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.context.ContextTableValuesComposite;
import org.talend.core.ui.context.model.AbstractContextCellModifier;
import org.talend.core.ui.context.model.template.ContextConstant;

/**
 * cli class global comment. Detailled comment
 */
public class ContextTableCellModifier extends AbstractContextCellModifier {

    public ContextTableCellModifier(ContextTableValuesComposite parentModel, boolean reposFlag) {
        super(parentModel, reposFlag);
    }

    @Override
    protected ContextTableValuesComposite getParentMode() {
        return (ContextTableValuesComposite) super.getParentMode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean canModify(Object element, String property) {
        if (getModelManager().isReadOnly()) {
            return false;
        }

        if (element instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel contextTableTabParentModel = (ContextTableTabParentModel) element;
            IContextParameter contextPara = contextTableTabParentModel.getContextParameter();
            if (contextPara != null) {
                String sourceId = contextPara.getSource();
                if (IContextParameter.BUILT_IN.equals(sourceId)) {
                    if (!ContextTableConstants.COLUMN_NAME_PROPERTY.equals(property)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    @Override
    public Object getValue(Object element, String property) {
        if (element instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel contextTableTabParentModel = (ContextTableTabParentModel) element;
            IContextParameter contextPara = contextTableTabParentModel.getContextParameter();
            List<IContext> contextList = getParentMode().getContexts();
            if (property.equals(ContextTableConstants.COLUMN_TYPE_PROPERTY)) {
                String s = ContextManagerHelper.convertFormat(contextPara.getType());
                for (int i = 0; i < ContextParameterJavaTypeManager.getJavaTypesLabels().length; i++) {
                    if (s.equals(ContextParameterJavaTypeManager.getJavaTypesLabels()[i])) {
                        return i;
                    }
                }
                return -1;
            } else {
                if (contextList != null && contextList.size() > 0) {
                    for (IContext context : contextList) {
                        String contextName = context.getName();
                        if (property.equals(contextName)) {
                            String tempSourceId = contextPara.getSource();
                            String tempName = contextPara.getName();
                            IContextParameter tempContextPara = context.getContextParameter(tempSourceId, tempName);
                            return tempContextPara.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }

    private int getValueIndex(String value) {
        String typeLabel = value;
        JavaType javaType = ContextParameterJavaTypeManager.getJavaTypeFromId(value);
        if (javaType != null) {
            typeLabel = javaType.getLabel();
        }
        String[] TypeValues = ContextParameterJavaTypeManager.getPerlTypesLabels();
        for (int i = 0; i < TypeValues.length; i++) {
            if (TypeValues[i].equals(typeLabel)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * zwang Comment method "getRealParameter".
     * 
     * @param property
     * @param templatePara
     * @return
     */
    public IContextParameter getRealParameter(String property, Object element) {
        IContextParameter para = null;
        IContext context = null;

        if (!(property.equals(ContextTableConstants.COLUMN_NAME_PROPERTY))) {
            context = getContextManager().getContext(property);
        }
        if (context == null) {
            return null;
        }

        if (parentMode.isGroupBySource()) {
            if (element instanceof ContextTableTabParentModel) {
                if (IContextParameter.BUILT_IN.equals(((ContextTableTabParentModel) element).getSourceId())) {
                    IContextParameter builtContextParameter = ((ContextTableTabParentModel) element).getContextParameter();
                    if (builtContextParameter != null) {
                        para = context.getContextParameter(builtContextParameter.getName());
                    }
                }
            } else if (element instanceof ContextTableTabChildModel) {
                ContextTableTabChildModel child = (ContextTableTabChildModel) element;
                String sourceId = child.getContextParameter().getSource();
                para = context.getContextParameter(sourceId, ((ContextTableTabChildModel) element).getContextParameter()
                        .getName());
            }
        } else {
            if (element instanceof ContextTableTabParentModel) {
                ContextTableTabParentModel contextTableTabParentModel = (ContextTableTabParentModel) element;
                IContextParameter contextPara = contextTableTabParentModel.getContextParameter();
                if (contextPara != null) {
                    String sourceId = contextPara.getSource();
                    String sourceName = contextPara.getName();
                    para = context.getContextParameter(sourceId, sourceName);
                }
            } else if (element instanceof ContextTableTabChildModel) {
                ContextTableTabChildModel child = (ContextTableTabChildModel) element;
                para = context.getContextParameter(child.getContextParameter().getName());
            }
        }
        return para;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public void modify(Object element, final String property, final Object value) {
        TreeItem item = (TreeItem) element;
        final Object object = item.getData();

        if (object instanceof ContextTableTabParentModel) {
            List<IContext> contextList = getParentMode().getContexts();
            if (property.equals(ContextTableConstants.COLUMN_TYPE_PROPERTY)) {
                int index = -1;
                ContextTableTabParentModel parent = (ContextTableTabParentModel) object;
                IContextParameter contextPara = parent.getContextParameter();
                String originalName = contextPara.getName();
                String s = ContextManagerHelper.convertFormat(contextPara.getType());
                for (int i = 0; i < ContextParameterJavaTypeManager.getJavaTypesLabels().length; i++) {
                    if (s.equals(ContextParameterJavaTypeManager.getJavaTypesLabels()[i])) {
                        index = i;
                    }
                }
                if (index == ((Integer) value)) {
                    return;
                }
                String newType = getRealType(ContextParameterJavaTypeManager.getJavaTypesLabels()[(Integer) value]);
                contextPara.setType(newType);
                String name = contextPara.getName();
                for (IContext context : contextList) { // getContextManager().getListContext()
                    for (IContextParameter contextParameter : context.getContextParameterList()) {
                        if (name.equals(contextParameter.getName())) {
                            contextParameter.setType(newType);
                        }
                    }
                }
                List<Object> updateObjs = new ArrayList<Object>();
                updateObjs.add(object);
                lookupSameNameNode(contextPara.getSource(), originalName, item, updateObjs);
                updateRelatedNode(updateObjs.toArray(), contextPara);
            } else {
                // add all nodes that need to update.
                List<Object> list = new ArrayList<Object>();
                list.add(object);

                IContextParameter para = null;
                for (int i = 0; i < (contextList.size() + 2); i++) {
                    if (property.equals(getParentMode().getColumnProperties()[i])) {
                        para = getRealParameter(getParentMode().getColumnProperties()[i], object);
                        getParentMode().getValueChecker().checkErrors(item, i, para);
                    }
                }

                if (para == null) {
                    return;
                }

                String originalName = para.getName();
                String sourceId = null;

                if (element instanceof ContextTableTabChildModel) {
                    ContextTableTabChildModel child = (ContextTableTabChildModel) element;
                    sourceId = child.getContextParameter().getSource();
                }

                for (IContext context : contextList) {
                    if (property.equals(context.getName())) {
                        if (para.getValue().equals(value)) {
                            return;
                        }
                        para.setValue((String) value);
                    }
                }
                // set updated flag.
                List<Object> updateObjs = new ArrayList<Object>();
                updateObjs.add(object);
                lookupSameNameNode(para.getSource(), originalName, item, updateObjs);
                updateRelatedNode(updateObjs.toArray(), para);
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
                if (obj instanceof ContextTableTabChildModel) {
                    ContextTableTabChildModel child = (ContextTableTabChildModel) obj;
                    String tempSourceId = child.getContextParameter().getSource();
                    String name = child.getName();
                    if (!sourceId.equals(tempSourceId) && name.equals(name)) {
                        updateObjs.add(obj);
                    }
                }
            }
        }
        return updateObjs.toArray();
    }
}
