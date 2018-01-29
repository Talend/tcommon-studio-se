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
package org.talend.repository.viewer.label;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.ui.view.RepositoryLabelProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

public class RepositoryViewLabelProvider extends AbstractRepoViewLabelProvider {

    /**
     * DOC sgandon class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
     * 
     */
    private final class CommonNavigatorRepositoryViewForLabelProvider implements IRepositoryView {

        private final String viewerId;

        /**
         * DOC sgandon JobsLabelProvider.CommonNavigatorRepositoryView constructor comment.
         */
        public CommonNavigatorRepositoryViewForLabelProvider(String viewerId) {
            this.viewerId = viewerId;
        }

        @Override
        public Object getAdapter(Class adapter) {
            return null;
        }

        @Override
        public void setFocus() {

        }

        @Override
        public void removePropertyListener(IPropertyListener listener) {

        }

        @Override
        public String getTitleToolTip() {
            return null;
        }

        @Override
        public Image getTitleImage() {
            return null;
        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public IWorkbenchPartSite getSite() {
            return null;
        }

        @Override
        public void dispose() {

        }

        @Override
        public void createPartControl(Composite parent) {

        }

        @Override
        public void addPropertyListener(IPropertyListener listener) {

        }

        @Override
        public void saveState(IMemento memento) {

        }

        @Override
        public void init(IViewSite site, IMemento memento) throws PartInitException {

        }

        @Override
        public void init(IViewSite site) throws PartInitException {

        }

        @Override
        public IViewSite getViewSite() {
            return null;
        }

        @Override
        public void refreshView() {

        }

        @Override
        public void refreshAllChildNodes(RepositoryNode rootNode) {

        }

        @Override
        public void refresh(Object object) {

        }

        @Override
        public void refresh() {

        }

        @Override
        public StructuredViewer getViewer() {
            return null;
        }

        @Override
        public IProjectRepositoryNode getRoot() {
            return null;
        }

        @Override
        public boolean getExpandedState(Object object) {
            boolean result = false;

            IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewerId);
            if (view instanceof CommonNavigator) {
                result = ((CommonNavigator) view).getCommonViewer().getExpandedState(object);
            }// else default is false
            return result;
        }

        @Override
        public void expand(Object object, boolean state) {

        }

        @Override
        public void expand(Object object) {

        }

        @Override
        public boolean containsRepositoryType(ERepositoryObjectType type) {
            return false;
        }

    }

    RepositoryLabelProvider repoLabelProv;

    @Override
    public Image getImage(Object element) {
        return repoLabelProv.getImage(element);
    }

    @Override
    public String getText(Object element) {
        if (element instanceof RepositoryNode) {// Only handle repository
            // node
            return repoLabelProv.getText(element);
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
        repoLabelProv.addListener(listener);
    }

    @Override
    public void dispose() {
        repoLabelProv.dispose();
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
        repoLabelProv.removeListener(listener);
    }

    @Override
    public void init(ICommonContentExtensionSite aConfig) {
        String viewerId = aConfig.getService().getViewerId();
        repoLabelProv = new RepositoryLabelProvider(new CommonNavigatorRepositoryViewForLabelProvider(viewerId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.label.AbstractRepoViewLabelProvider#getFont(java.lang.Object)
     */
    @Override
    public Font getFont(Object element) {
        return repoLabelProv.getFont(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.label.AbstractRepoViewLabelProvider#getBackground(java.lang.Object)
     */
    @Override
    public Color getBackground(Object element) {
        return repoLabelProv.getBackground(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.label.AbstractRepoViewLabelProvider#getForeground(java.lang.Object)
     */
    @Override
    public Color getForeground(Object element) {
        return repoLabelProv.getForeground(element);
    }
}
