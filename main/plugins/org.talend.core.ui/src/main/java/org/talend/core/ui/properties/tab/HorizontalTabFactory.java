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
package org.talend.core.ui.properties.tab;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.ITabItem;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class HorizontalTabFactory {

    private TalendTabbedPropertyComposite tabbedPropertyComposite;

    private TalendTabbedPropertyTitle title;

    private TalendTabbedPropertyViewer tabbedPropertyViewer;

    private TabbedPropertySheetWidgetFactory widgetFactory;

    public void initComposite(Composite parent, boolean displayCompactToolbar) {

        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayoutData(new GridData(GridData.FILL_BOTH));
        panel.setLayout(new FormLayout());

        widgetFactory = new TabbedPropertySheetWidgetFactory();
        tabbedPropertyComposite = new TalendTabbedPropertyComposite(panel, widgetFactory, true, displayCompactToolbar);
        widgetFactory.paintBordersFor(tabbedPropertyComposite);
        tabbedPropertyComposite.setLayout(new FormLayout());
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        tabbedPropertyComposite.setLayoutData(formData);

        tabbedPropertyViewer = new TalendTabbedPropertyViewer(tabbedPropertyComposite.getList());
        tabbedPropertyViewer.setContentProvider(new TabListContentProvider());
        tabbedPropertyViewer.setLabelProvider(new TabbedPropertySheetPageLabelProvider());

        title = tabbedPropertyComposite.getTitle();
    }

    public Composite getTabComposite() {
        return tabbedPropertyComposite.getTabComposite();
    }

    public Composite getTitleComposite() {
        return tabbedPropertyComposite.getTitle();
    }

    /**
     * yzhang Comment method "setSelection".
     * 
     * @param selection
     */
    public void setSelection(IStructuredSelection selection) {
        tabbedPropertyViewer.setSelection(selection);
    }

    /**
     * yzhang Comment method "addSelectionChangedListener".
     * 
     * @param listener
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (tabbedPropertyViewer != null) {
            tabbedPropertyViewer.addSelectionChangedListener(listener);
        }
    }

    /**
     * yzhang Comment method "setInput".
     * 
     * @param descriptors
     */
    public void setInput(List<TalendPropertyTabDescriptor> descriptors) {
        tabbedPropertyViewer.setInput(descriptors);
    }

    public List<TalendPropertyTabDescriptor> getInput() {
        return (List<TalendPropertyTabDescriptor>) tabbedPropertyViewer.getInput();
    }

    public TalendPropertyTabDescriptor getSelection() {
        return (TalendPropertyTabDescriptor) ((IStructuredSelection) tabbedPropertyViewer.getSelection()).getFirstElement();
    }

    /**
     * yzhang Comment method "setTitle".
     * 
     * @param label
     * @param image
     */
    public void setTitle(String label, Image image) {
        title.setTitle(label, image);
    }

    public void setTitleImage(Image image) {
        title.setTitleImage(image);
    }

    /**
     * yzhang HorizontalTabFactory class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
     * 
     */
    private class TabbedPropertySheetPageLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            if (element instanceof ITabItem) {
                return ((ITabItem) element).getText();
            }
            return null;
        }
    }

    /**
     * yzhang HorizontalTabFactory class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
     * 
     */
    private class TabListContentProvider implements IStructuredContentProvider {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        @Override
        public Object[] getElements(Object inputElement) {
            if (!(inputElement instanceof List)) {
                return (Object[]) inputElement;
            }
            return ((List) inputElement).toArray();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        @Override
        public void dispose() {

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

    }

    public TabbedPropertySheetWidgetFactory getWidgetFactory() {
        return this.widgetFactory;
    }

    /**
     * Getter for tabbedPropertyComposite.
     * 
     * @return the tabbedPropertyComposite
     */
    public TalendTabbedPropertyComposite getTabbedPropertyComposite() {
        return this.tabbedPropertyComposite;
    }

}
