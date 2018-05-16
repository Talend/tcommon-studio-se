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
package org.talend.core.ui.context.model.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.model.ContextProviderProxy;
import org.talend.core.ui.context.model.ContextTabParentModel;

/**
 * cli class global comment. Detailled comment
 * 
 * A label and content provider for the treeviewer which groups the Contexts by variable.
 */
public class GroupByVariableProvider extends ContextProviderProxy {

    private IContextModelManager modelManager;

    public GroupByVariableProvider() {
        super();
    }

    public GroupByVariableProvider(IContextModelManager modelManager) {
        this();
        this.modelManager = modelManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ContextTreeTabParentModel) {
            if (columnIndex == 0)
                return ((ContextTreeTabParentModel) element).getName();
        } else if (element instanceof ContextTreeTabChildModel) {
            ContextTreeTabChildModel child = (ContextTreeTabChildModel) element;
            switch (columnIndex) {
            case 1:
                return child.getContextName();
            case 3:
                return child.getPromptValue();
            case 4:
                return ContextParameterUtils.checkAndHideValue(child.getContextParameter());
            case 5:
                return child.getCommentValue();
            default:
                return "";
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
        if (columnIndex == 2 && (element instanceof ContextTreeTabChildModel)) {
            ContextTreeTabChildModel son = (ContextTreeTabChildModel) element;
            if (son.getContextParameter() == null) {
                son.getClass();
            }
            if (son.getContextParameter() != null && son.getContextParameter().isPromptNeeded()) {
                return ImageProvider.getImage(EImage.CHECKED_ICON);
            } else {
                return ImageProvider.getImage(EImage.UNCHECKED_ICON);
            }

        }
        return null;
    }

    private ContextTreeTabParentModel getContextParent(String sourceId, String parentName, List<ContextTabParentModel> parents) {
        ContextTreeTabParentModel treeParent = null;
        for (ContextTabParentModel parent : parents) {
            String tempName = parent.getName();
            String tempSourceId = parent.getSourceId();
            if (tempName.equals(parentName) && tempSourceId.equals(sourceId)) {
                treeParent = (ContextTreeTabParentModel) parent;
                break;
            }
        }
        if (treeParent == null) {
            treeParent = new ContextTreeTabParentModel(sourceId, parentName);
            parents.add(treeParent);
        }
        return treeParent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        List<IContext> contexts = (List<IContext>) inputElement;
        List<ContextTabParentModel> output = new ArrayList<ContextTabParentModel>();

        if (contexts != null && !contexts.isEmpty()) {
            for (IContext context : contexts) {
                List<IContextParameter> contextParas = context.getContextParameterList();
                if (contextParas != null && contextParas.size() > 0) {
                    for (IContextParameter contextPara : contextParas) {
                        String sourceId = contextPara.getSource();
                        String name = contextPara.getName();
                        ContextTreeTabParentModel parent = getContextParent(sourceId, name, output);
                        ContextTreeTabChildModel child = new ContextTreeTabChildModel(context.getName());
                        child.setContextParameter(contextPara);
                        parent.addChild(child);
                    }
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
        if (parentElement instanceof ContextTreeTabParentModel) {
            ContextTreeTabParentModel parent = (ContextTreeTabParentModel) parentElement;
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
        if (element instanceof ContextTreeTabChildModel) {
            ContextTreeTabChildModel child = (ContextTreeTabChildModel) element;
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
        boolean has = false;
        if (element instanceof ContextTreeTabParentModel) {
            ContextTreeTabParentModel parent = (ContextTreeTabParentModel) element;
            has = parent.hasChildren();
        }
        return has;
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
        if (element instanceof ContextTreeTabParentModel) {
            ContextTreeTabParentModel parent = (ContextTreeTabParentModel) element;
            String name = parent.getName();
            if (hasSameNameElement(name))
                return Display.getDefault().getSystemColor(SWT.COLOR_RED);
        }
        return null;
    }

    private boolean hasSameNameElement(String name) {
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
