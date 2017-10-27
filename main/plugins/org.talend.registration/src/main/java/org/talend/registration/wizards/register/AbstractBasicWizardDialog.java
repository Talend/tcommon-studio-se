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
package org.talend.registration.wizards.register;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.registration.ERepositoryImages;
import org.talend.registration.i18n.Messages;

/**
 * DOC zli class global comment. Detailled comment
 */
public class AbstractBasicWizardDialog extends Dialog {

    private IWizard wizard;

    private String title;

    public static final int WIDTH_HIT = 660;

    public static final Color YELLOW_GREEN_COLOR = new Color(null, 159, 181, 38);// 143, 163, 35

    public static final Font TITLE_FONT = new Font(null, "Arial", 18, SWT.BOLD);// Arial courier

    public static final String REGISTER_TITLE = "Join the Talend community!";

    // protected String emailStr;
    //
    // protected String countryStr;

    /**
     * DOC informix AbstractBasicWizardDialog constructor comment.
     * 
     * @param parentShell
     */
    public AbstractBasicWizardDialog(Shell parentShell, IWizard wizard) {
        super(parentShell);
        this.wizard = wizard;
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.APPLICATION_MODAL);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons by default
        createButton(parent, IDialogConstants.BACK_ID, IDialogConstants.BACK_LABEL, false);
        createButton(parent, IDialogConstants.NEXT_ID, IDialogConstants.NEXT_LABEL, true);
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setModified(true);
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        newShell.setText(Messages.getString("RegisterWizard.windowTitle", brandingService.getFullProductName())); //$NON-NLS-1$
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        // this.getShell().setText(title);
        return control;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // set the whole page white
        parent.setBackground(new Color(null, new RGB(255, 255, 255)));
        parent.setBackgroundMode(SWT.INHERIT_FORCE);

        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);

        Composite top = new Composite(container, SWT.NONE);
        top.setLayout(new GridLayout());
        top.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        Label label = new Label(top, SWT.NONE);
        label.setText(REGISTER_TITLE);
        label.setFont(TITLE_FONT);
        label.setForeground(YELLOW_GREEN_COLOR);

        createCenterComposite(container, SWT.NONE);
        createBottomComposite(container, SWT.NONE);

        return container;
    }

    protected void createCenterComposite(Composite composite, int style) {
        Composite center = new Composite(composite, SWT.NONE);
        GridLayout gd = new GridLayout(2, false);
        center.setLayout(gd);
        GridData layoutData = new GridData();
        center.setLayoutData(layoutData);

        ImageCanvas cc = new ImageCanvas(center, ImageProvider.getImageDesc(ERepositoryImages.REGISTER_ICO)); //$NON-NLS-1$
        cc.setLayout(new GridLayout());
        cc.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        rightComposite(center, SWT.NONE);
    }

    protected void rightComposite(Composite composite, int style) {
    }

    protected void createBottomComposite(Composite composite, int style) {

    }

    protected void createLegalInfos(Composite composite, int columnSpan, String string) {
        Composite localComposite = new Composite(composite, SWT.NONE);
        localComposite.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = columnSpan;
        localComposite.setLayoutData(gd);

        Label legalInfos = new Label(localComposite, SWT.WRAP);
        gd = new GridData(GridData.FILL_BOTH);
        legalInfos.setLayoutData(gd);
        legalInfos.setText(string); //$NON-NLS-1$
    }

    protected void createLegalInfos(Composite composite, int columnSpan, String string, int style, Font font) {
        Composite localComposite = new Composite(composite, SWT.NONE);
        localComposite.setLayout(new GridLayout());

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = columnSpan;
        localComposite.setLayoutData(gd);

        Label legalInfos = new Label(localComposite, SWT.WRAP);
        gd = new GridData(style);
        legalInfos.setLayoutData(gd);
        legalInfos.setText(string); //$NON-NLS-1$
        legalInfos.setFont(font);
    }

    protected void createSpacer(Composite composite, int columnSpan) {
        Label label = new Label(composite, SWT.NONE);
        GridData gd = new GridData();
        gd.horizontalSpan = columnSpan;
        label.setLayoutData(gd);
    }

    /**
     * Canvas displaying an image.<br/>
     * 
     * $Id: LoginDialog.java 47428 2010-08-28 14:58:01Z sgandon $
     * 
     */
    protected class ImageCanvas extends Canvas {

        private Image img;

        public ImageCanvas(Composite parent, ImageDescriptor imgDesc) {
            super(parent, SWT.NONE);

            if (imgDesc != null) {
                img = imgDesc.createImage();
                addPaintListener(new PaintListener() {

                    public void paintControl(PaintEvent e) {
                        e.gc.drawImage(img, 0, 0);
                    }
                });
            }
        }

        /*
         * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
         */
        @Override
        public Point computeSize(int wHint, int hHint, boolean changed) {
            Point size;
            if (img != null) {
                size = new Point(img.getBounds().width, img.getBounds().height);
            } else {
                size = super.computeSize(wHint, hHint, changed);
            }
            return size;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.widgets.Widget#dispose()
         */
        @Override
        public void dispose() {
            if (img != null) {
                img.dispose();
                img = null;
            }

            super.dispose();
        }

    }
}
