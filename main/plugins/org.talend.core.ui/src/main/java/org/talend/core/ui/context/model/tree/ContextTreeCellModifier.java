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
package org.talend.core.ui.context.model.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.TreeItem;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextTreeValuesComposite;
import org.talend.core.ui.context.model.AbstractContextCellModifier;

/**
 * cli class global comment. Detailled comment
 */
public class ContextTreeCellModifier extends AbstractContextCellModifier {

    public ContextTreeCellModifier(ContextTreeValuesComposite parentModel, boolean repositoryFlag) {
        super(parentModel, repositoryFlag);

    }

    protected ContextTreeValuesComposite getParentMode() {
        return (ContextTreeValuesComposite) super.getParentMode();
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
        IContextParameter para = getRealParameter(element);
        if (para == null) {
            return false;
        }

        if (!para.isBuiltIn() && !repositoryFlag) {
            // not built-in, not update
            return false;

        }

        if (property.equals(ContextTreeConstants.VARIABLE_COLUMN_PROPERTY)
                || property.equals(ContextTreeConstants.CONTEXT_COLUMN_PROPERTY)) {
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
        IContextParameter para = getRealParameter(element);

        if (para != null) {
            if (property.equals(ContextTreeConstants.VALUE_COLUMN_PROPERTY)) {
                return para.getDisplayValue();
            } else if (property.equals(ContextTreeConstants.PROMPT_COLUMN_PROPERTY)) {
                return para.getPrompt();
            } else if (property.equals(ContextTreeConstants.PROMPTNEEDED_COLUMN_PROPERTY)) {
                return para.isPromptNeeded();
            }
        }
        return ""; //$NON-NLS-1$
    }

    /**
     * bqian Comment method "getRealParameter".
     * 
     * @param property
     * @param templatePara
     * @return
     */
    public IContextParameter getRealParameter(Object element) {
        IContextParameter para = null;

        if (element instanceof IContextParameter) {
            para = (IContextParameter) element;
        } else if (element instanceof ContextTreeTabChildModel) {
            para = ((ContextTreeTabChildModel) element).getContextParameter();
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

        final IContextParameter para = getRealParameter(object);
        if (para == null) {
            return;
        }
        String originalName = para.getName();
        if (property.equals(ContextTreeConstants.VALUE_COLUMN_PROPERTY)) {
            if (para.getDisplayValue().equals(value)) {
                getParentMode().getViewer().getTree().update();
                return;
            }
            para.setValue((String) value);
            getParentMode().getValueChecker().checkErrors(item, ContextTreeConstants.VARIABLE_COLUMN_INDEX, para);
            // viewer.getTree().update();
        } else if (property.equals(ContextTreeConstants.PROMPT_COLUMN_PROPERTY)) {
            if (para.getPrompt().equals(value)) {
                return;
            }
            para.setPrompt((String) value);
        } else if (property.equals(ContextTreeConstants.PROMPTNEEDED_COLUMN_PROPERTY)) {
            if (para.isPromptNeeded() == ((Boolean) value).booleanValue()) {
                return;
            }
            para.setPromptNeeded((Boolean) value);
        }

        // set updated flag.
        List<Object> updateObjs = new ArrayList<Object>();
        updateObjs.add(object);
        lookupSameNameNode(para.getSource(), originalName, item, updateObjs);
        updateRelatedNode(updateObjs.toArray(), para);
        // setAndRefreshFlags(object, para);

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
                if (obj instanceof ContextTreeTabParentModel) {
                    ContextTreeTabParentModel parent = (ContextTreeTabParentModel) obj;
                    String tempSourceId = parent.getSourceId();
                    String name = parent.getName();
                    if (!sourceId.equals(tempSourceId) && nodeName.equals(name))
                        updateObjs.add(obj);
                }
            }
        }
        return updateObjs.toArray();
    }

}
