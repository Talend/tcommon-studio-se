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
package org.talend.core.ui.properties.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class TalendTabbedPropertyComposite extends Composite {

    private TabbedPropertySheetWidgetFactory factory;

    private Composite mainComposite, toolBarComposite, leftComposite, tabComposite;

    private ScrolledComposite scrolledComposite;

    private TalendTabbedPropertyTitle title;

    private TalendTabbedPropertyList listComposite;

    private Button compactButton;

    private Button tableButton;

    private boolean isCompactView = true;

    private boolean displayTitle;

    private boolean displayCompactToolbar;

    /**
     * Constructor for a TabbedPropertyComposite
     * 
     * @param parent the parent widget.
     * @param factory the widget factory.
     * @param displayTitle if <code>true</code>, then the title bar will be displayed.
     */
    public TalendTabbedPropertyComposite(Composite parent, TabbedPropertySheetWidgetFactory factory, boolean displayTitle,
            boolean displayCompactToolbar) {
        super(parent, SWT.NO_FOCUS);
        this.factory = factory;
        this.displayTitle = displayTitle;
        this.displayCompactToolbar = displayCompactToolbar;

        createMainComposite();
        // CSS
        CoreUIPlugin.setCSSClass(this, this.getClass().getSimpleName());
    }

    /**
     * Create the main composite.
     */
    protected void createMainComposite() {
        mainComposite = factory.createComposite(this, SWT.NO_FOCUS);
        mainComposite.setLayout(new FormLayout());
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        mainComposite.setLayoutData(formData);

        createMainContents();
    }

    /**
     * Create the contents in the main composite.
     */
    protected void createMainContents() {
        if (displayTitle) {
            title = new TalendTabbedPropertyTitle(mainComposite, factory);

            FormData data = new FormData();
            data.left = new FormAttachment(0, 0);
            if (displayCompactToolbar) {
                data.right = new FormAttachment(90, 0);
            } else {
                data.right = new FormAttachment(100, 0);
            }
            data.top = new FormAttachment(0, 0);
            title.setLayoutData(data);

            if (displayCompactToolbar) {
                toolBarComposite = new Composite(mainComposite, SWT.NONE);
                compactButton = new Button(toolBarComposite, SWT.PUSH);
                compactButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
                compactButton.setToolTipText(Messages.getString("TalendTabbedPropertyComposite.compactButton.toolTip")); //$NON-NLS-1$

                tableButton = new Button(toolBarComposite, SWT.PUSH);
                tableButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
                tableButton.setToolTipText(Messages.getString("TalendTabbedPropertyComposite.tableButton.toolTip")); //$NON-NLS-1$

                if (isCompactView()) {
                    compactButton.setImage(ImageProvider.getImage(EImage.COMPACT_VIEW));
                    tableButton.setImage(ImageProvider.getImage(EImage.NO_TABLE_VIEW));
                } else {
                    compactButton.setImage(ImageProvider.getImage(EImage.NO_COMPACT_VIEW));
                    tableButton.setImage(ImageProvider.getImage(EImage.TABLE_VIEW));
                }

                Rectangle compactRectangle = compactButton.getBounds();
                tableButton.setBounds(compactRectangle);

                compactButton.setVisible(false);
                tableButton.setVisible(false);

                data = new FormData();
                data.left = new FormAttachment(title, 0);
                data.top = new FormAttachment(0, -5);
                toolBarComposite.setLayoutData(data);
                GridData gridData = new GridData();
                gridData.horizontalAlignment = SWT.RIGHT;
                gridData.verticalAlignment = SWT.TOP;
                compactButton.setData(gridData);
                tableButton.setData(gridData);

                GridLayout layout = new GridLayout();
                layout.numColumns = 2;
                layout.horizontalSpacing = 0;
                layout.verticalSpacing = 0;
                layout.makeColumnsEqualWidth = true;
                toolBarComposite.setLayout(layout);
                toolBarComposite.setBackground(title.getBackground());
            }
        }

        leftComposite = factory.createComposite(mainComposite, SWT.NO_FOCUS);
        leftComposite.setLayout(new FormLayout());

        scrolledComposite = factory.createScrolledComposite(mainComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS);
        scrolledComposite.setLayout(new FormLayout());

        FormData formData = new FormData();
        formData.left = new FormAttachment(leftComposite, 0);
        formData.right = new FormAttachment(100, 0);
        if (displayTitle) {
            formData.top = new FormAttachment(title, 0);
        } else {
            formData.top = new FormAttachment(0, 0);
        }
        formData.bottom = new FormAttachment(100, 0);
        scrolledComposite.setLayoutData(formData);

        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(scrolledComposite, 0);
        if (displayTitle) {
            formData.top = new FormAttachment(title, 0);
        } else {
            formData.top = new FormAttachment(0, 0);
        }
        formData.bottom = new FormAttachment(100, 0);
        leftComposite.setLayoutData(formData);

        tabComposite = factory.createComposite(scrolledComposite, SWT.NO_FOCUS);
        tabComposite.setLayout(new FormLayout());
        tabComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

        scrolledComposite.setContent(tabComposite);
        scrolledComposite.setAlwaysShowScrollBars(false);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setExpandHorizontal(true);

        listComposite = new TalendTabbedPropertyList(leftComposite, factory);
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        listComposite.setLayoutData(formData);

        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        tabComposite.setLayoutData(data);
    }

    /**
     * Get the tabbed property list, which is the list of tabs on the left hand side of this composite.
     * 
     * @return the tabbed property list.
     */
    public TalendTabbedPropertyList getList() {
        return listComposite;
    }

    /**
     * Get the tabbed property title bar.
     * 
     * @return the tabbed property title bar or <code>null</code> if not used.
     */
    public TalendTabbedPropertyTitle getTitle() {
        return title;
    }

    /**
     * Get the tab composite where sections display their property contents.
     * 
     * @return the tab composite.
     */
    public Composite getTabComposite() {
        return tabComposite;
    }

    /**
     * Get the scrolled composite which surrounds the title bar and tab composite.
     * 
     * @return the scrolled composite.
     */
    public ScrolledComposite getScrolledComposite() {
        return scrolledComposite;
    }

    /**
     * Get the widget factory.
     * 
     * @return the widget factory.
     */
    protected TabbedPropertySheetWidgetFactory getFactory() {
        return factory;
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        listComposite.dispose();
        if (displayTitle) {
            title.dispose();
        }
        super.dispose();
    }

    /**
     * Getter for isCompactView.
     * 
     * @return the isCompactView
     */
    public boolean isCompactView() {
        return this.isCompactView;
    }

    /**
     * Sets the isCompactView.
     * 
     * @param isCompactView the isCompactView to set
     */
    public void setCompactView(boolean isCompactView) {
        this.isCompactView = isCompactView;
        if (displayCompactToolbar) {
            if (isCompactView) {
                getCompactButton().setImage(ImageProvider.getImage(EImage.COMPACT_VIEW));
                getTableButton().setImage(ImageProvider.getImage(EImage.NO_TABLE_VIEW));
            } else {
                getCompactButton().setImage(ImageProvider.getImage(EImage.NO_COMPACT_VIEW));
                getTableButton().setImage(ImageProvider.getImage(EImage.TABLE_VIEW));
            }
        }
    }

    public void setCompactViewVisible(boolean visible) {
        if (this.toolBarComposite != null && !toolBarComposite.isDisposed()) {
            toolBarComposite.setVisible(visible);
        }
    }

    /**
     * Getter for compactButton.
     * 
     * @return the compactButton
     */
    public Button getCompactButton() {
        return this.compactButton;
    }

    /**
     * Getter for tableButton.
     * 
     * @return the tableButton
     */
    public Button getTableButton() {
        return this.tableButton;
    }

    /**
     * Getter for toolBarComposite.
     * 
     * @return the toolBarComposite
     */
    public Composite getToolBarComposite() {
        return this.toolBarComposite;
    }

}
