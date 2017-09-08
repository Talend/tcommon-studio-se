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
package org.talend.commons.ui.swt.advanced.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;

/**
 * This class is used for constructing three composites, putting 2 sashes in the middle composite, which is used for
 * changing other 2 composites.
 * 
 * $Id: talend-code-templates.xml 1 2007-3-19 下午04:55:01 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class ThreeCompositesSashForm extends Composite {

    public static final int SASH_WIDTH = 3;

    private Composite leftComposite;

    private Composite rightComposite;

    private Composite midComposite;

    private Sash leftSash;

    private Sash rightSash;

    /**
     * Initialize.
     * 
     * @param parent
     * @param style
     */
    public ThreeCompositesSashForm(Composite parent, int style) {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;

        setLayout(gridLayout);
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        setLayoutData(gridData);

        addComponents();
        addSashListeners();
    }

    /**
     * Changes all widgets's position when sash was moved.
     * 
     * @param shift
     */
    private void setCompositesBounds(int shift) {
        // Set Left Composite Width.
        int leftSashPreLocation = leftSash.getBounds().x;
        leftSash.setLocation(leftSashPreLocation + shift, leftSash.getBounds().y);
        if (leftSash.getBounds().x > 0) {
            if (leftSashPreLocation < 0) {
                leftComposite.setSize(leftComposite.getBounds().width + shift + leftSashPreLocation, leftComposite
                        .getBounds().height);
            } else {
                leftComposite.setSize(leftComposite.getBounds().width + shift, leftComposite.getBounds().height);
            }
        } else {
            leftComposite.setSize(0, leftComposite.getBounds().height);
        }
        // Set middle composite.
        midComposite.setLocation(midComposite.getBounds().x + shift, midComposite.getBounds().y);
        rightSash.setLocation(rightSash.getBounds().x + shift, rightSash.getBounds().y);
        // Set Right Composte Width.
        rightComposite.setLocation(rightComposite.getBounds().x + shift, rightComposite.getBounds().y);
        rightComposite.setSize(rightComposite.getBounds().width - shift, rightComposite.getBounds().height);
    }

    public Composite getLeftComposite() {
        return this.leftComposite;
    }

    public Composite getMidComposite() {
        return this.midComposite;
    }

    public Composite getRightComposite() {
        return this.rightComposite;
    }

    /**
     * qzhang Comment method "addSashListeners".
     */
    private void addSashListeners() {
        leftSash.addListener(SWT.Selection, new Listener() {

            /*
             * (non-Java)
             * 
             * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
             */
            public void handleEvent(Event event) {
                int shift = event.x - leftSash.getBounds().x;
                setCompositesBounds(shift);

            }

        });
        rightSash.addListener(SWT.Selection, new Listener() {

            /*
             * (non-Java)
             * 
             * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
             */
            public void handleEvent(Event event) {
                int shift = event.x - rightSash.getBounds().x;
                setCompositesBounds(shift);
            }

        });

    }

    /**
     * qzhang Comment method "addComponents".
     */
    private void addComponents() {
        leftComposite = new Composite(this, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginBottom = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        GridData gridData = new GridData(GridData.FILL_BOTH);

        leftComposite.setLayout(gridLayout);
        leftComposite.setLayoutData(gridData);

        leftSash = new Sash(this, SWT.VERTICAL | SWT.SMOOTH);
        GridData gridData2 = new GridData(GridData.FILL_VERTICAL);
        leftSash.setLayoutData(gridData2);
        leftSash.setSize(SASH_WIDTH, leftSash.getBounds().height);

        midComposite = new Composite(this, SWT.NONE);
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.marginBottom = 0;
        gridLayout2.marginHeight = 0;
        gridLayout2.marginLeft = 0;
        gridLayout2.marginRight = 0;
        gridLayout2.marginTop = 0;
        gridLayout2.marginWidth = 0;
        gridLayout2.horizontalSpacing = 0;
        gridLayout2.verticalSpacing = 0;
        midComposite.setLayout(gridLayout2);
        GridData gridData3 = new GridData(GridData.FILL_VERTICAL);
        midComposite.setLayoutData(gridData3);

        rightSash = new Sash(this, SWT.VERTICAL | SWT.SMOOTH);
        GridData gridData5 = new GridData(GridData.FILL_VERTICAL);
        rightSash.setLayoutData(gridData5);
        rightSash.setSize(SASH_WIDTH, rightSash.getBounds().height);

        rightComposite = new Composite(this, SWT.NONE);
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.marginBottom = 0;
        gridLayout3.marginHeight = 0;
        gridLayout3.marginLeft = 0;
        gridLayout3.marginRight = 0;
        gridLayout3.marginTop = 0;
        gridLayout3.marginWidth = 0;
        gridLayout3.horizontalSpacing = 0;
        rightComposite.setLayout(gridLayout3);
        GridData gridData4 = new GridData(GridData.FILL_BOTH);
        rightComposite.setLayoutData(gridData4);
    }

    public void attachSizes() {

        Composite composite = (Composite) leftComposite.getChildren()[0];

        Control[] children = (composite).getChildren();
        for (Control control : children) {
            if (control instanceof Table) {
                midComposite.setLocation(midComposite.getBounds().x, control.getBounds().y);
                midComposite.setSize(midComposite.getBounds().width, control.getBounds().height);
            }
        }
    }

    /**
     * qzhang Comment method "setGridDatas".
     * 
     * @return
     */
    public void setGridDatas() {
        Composite composite = (Composite) leftComposite.getChildren()[0];
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.marginBottom = 0;
        gridLayout2.marginHeight = 0;
        gridLayout2.marginLeft = 0;
        gridLayout2.marginRight = 0;
        gridLayout2.marginTop = 0;
        gridLayout2.marginWidth = 0;
        gridLayout2.horizontalSpacing = 0;
        composite.setLayout(gridLayout2);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite composite2 = (Composite) rightComposite.getChildren()[0];
        gridLayout2 = new GridLayout();
        gridLayout2.marginBottom = 0;
        gridLayout2.marginHeight = 0;
        gridLayout2.marginLeft = 0;
        gridLayout2.marginRight = 0;
        gridLayout2.marginTop = 0;
        gridLayout2.marginWidth = 0;
        gridLayout2.horizontalSpacing = 0;
        composite2.setLayout(gridLayout2);
        composite2.setLayoutData(new GridData(GridData.FILL_BOTH));

    }
}
