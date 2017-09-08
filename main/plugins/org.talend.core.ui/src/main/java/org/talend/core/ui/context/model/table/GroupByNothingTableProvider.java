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
import org.talend.core.model.context.JobContext;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextTableValuesComposite;
import org.talend.core.ui.context.model.ContextProviderProxy;

/**
 * cli class global comment. Detailled comment
 */
public class GroupByNothingTableProvider extends ContextProviderProxy {

    private ContextTableValuesComposite parentModel;

    public GroupByNothingTableProvider(ContextTableValuesComposite parentModel) {
        super();
        this.parentModel = parentModel;
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
        return null;
    }

    /**
     * Looks up the context parameter value under the specified context. The index value is the index of context list.
     * 
     * @param sourceId
     * @param contextParaName
     * @param index
     * @return
     */
    private String lookupContextParameterValue(String sourceId, String contextParaName, int index) {
        IContextParameter contextPara = lookupContextParameter(sourceId, contextParaName, index);
        if (contextPara != null)
            return contextPara.getValue();
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel parent = (ContextTableTabParentModel) element;
            IContextParameter contextPara = parent.getContextParameter();
            String tempSourceId = contextPara.getSource();
            String tempName = contextPara.getName();
            if (columnIndex == 0)
                return contextPara.getName();
            else
                return lookupContextParameterValue(tempSourceId, tempName, columnIndex - 1);
        } else if (element instanceof ContextTableTabChildModel) {

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
        List<IContextParameter> contexts = (List<IContextParameter>) inputElement;
        List<ContextTableTabParentModel> output = new ArrayList<ContextTableTabParentModel>();
        if (contexts != null && contexts.size() > 0) {
            for (IContextParameter contextPara : contexts) {
                ContextTableTabParentModel parent = new ContextTableTabParentModel();
                parent.setContextParameter(contextPara);
                output.add(parent);
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
    public Color getForeground(Object element, int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof ContextTableTabParentModel) {
            ContextTableTabParentModel parent = (ContextTableTabParentModel) element;
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
        IContextManager contextManager = parentModel.getContextModelManager().getContextManager();
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
