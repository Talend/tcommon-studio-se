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

import java.io.IOException;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.ui.html.BrowserDynamicPartLocationListener;
import org.talend.registration.i18n.Messages;
import org.talend.registration.intro.regist.TalendForgeRegistHelper;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class TalendForgeDialog extends TrayDialog {

    protected static TalendForgeDialog currentTalendForgeDialog;

    private String userEmail;

    public static final String LOGINCOUNT = "LOGINCOUNT"; //$NON-NLS-1$

    public static final String FONT_ARIAL = "Arial"; //$NON-NLS-1$

    public static final Color YELLOW_GREEN_COLOR = new Color(null, 159, 181, 38);

    private Font font = new Font(null, FONT_ARIAL, 9, SWT.NORMAL);

    private Font fontMac = new Font(null, FONT_ARIAL, 12, SWT.NORMAL);

    private final String osName = System.getProperties().getProperty("os.name"); //$NON-NLS-1$

    protected Browser browser;

    /**
     * DOC Administrator TalendForgeDialog constructor comment.
     * 
     * @param shell
     */
    public TalendForgeDialog(Shell shell, String userEmail) {
        super(shell);
        currentTalendForgeDialog = this;
        this.userEmail = userEmail;
        if (osName.contains("Mac")) { //$NON-NLS-1$
            font = fontMac;
        }
    }

    @Override
    protected void initializeBounds() {
        super.initializeBounds();
        Point location = getInitialLocation(getShell().getSize());
        getShell().setLocation(location.x, location.y);
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("TalendForgeDialog.newProjectTitle")); //$NON-NLS-1$
    }

    @Override
    protected Control createContents(Composite parent) {
        parent.setLayout(new FormLayout());
        Composite composite = new Composite(parent, 0);
        FormData data = new FormData();
        data.height = 670;
        data.width = 670;
        composite.setLayoutData(data);
        composite.setLayout(new FillLayout());
        composite.setBackground(new Color(null, 255, 255, 255));
        // create the dialog area and button bar
        dialogArea = createDialogArea(composite);
        // addListener();
        return composite;
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        browser = new Browser(parent, SWT.NONE);
        try {
            browser.setText(TalendForgeRegistHelper.getHelper().getHtmlContent());
        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        }
        browser.addLocationListener(new BrowserDynamicPartLocationListener());

        return parent;
    }

    @Override
    public void okPressed() {
        super.okPressed();
        font.dispose();
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public Browser getBrowser() {
        return this.browser;
    }

    public static TalendForgeDialog getCurrentTalendForgeDialog() {
        return TalendForgeDialog.currentTalendForgeDialog;
    }
}
