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
package org.talend.registration.wizards.register;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.registration.ERepositoryImages;
import org.talend.registration.i18n.Messages;
import org.talend.repository.ui.login.connections.ConnectionUserPerReader;

/**
 * DOC zli class global comment. Detailled comment
 */
public class RegisterWizardPage3 extends AbstractBasicWizardDialog {

    private String email = null;

    public RegisterWizardPage3(Shell parentShell, IWizard wizard) {
        super(parentShell, wizard);

    }

    /**
     * DOC zli RegisterWizardPage1 constructor comment.
     * 
     * @param parentShell
     * @param wizard
     */
    public RegisterWizardPage3(Shell parentShell, IWizard wizard, String email) {
        super(parentShell, wizard);
        this.email = email;
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

        Composite centerComposite = new Composite(container, SWT.NONE);
        centerComposite.setLayout(new GridLayout());// top.setLayoutData(new
        // GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        centerComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

        // createSpacer(centerComposite, SWT.NONE);

        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        String thanks1 = Messages.getString("RegisterWizardPage.legalthanks1", brandingService.getCorporationName());
        String thanks2 = Messages.getString("RegisterWizardPage.legalthanks2", brandingService.getCorporationName());
        Font font2 = new Font(null, "Arial", 10, SWT.BOLD);

        Composite c1 = new Composite(centerComposite, SWT.NONE);
        c1.setLayout(new GridLayout());
        c1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        Label label1 = new Label(c1, SWT.NONE);
        label1.setText(thanks1);
        label1.setFont(font2);

        Composite c2 = new Composite(centerComposite, SWT.NONE);
        c2.setLayout(new GridLayout());
        c2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        Label label2 = new Label(c2, SWT.NONE);
        label2.setText(thanks2);
        label2.setFont(font2);

        // createSpacer(centerComposite, SWT.NONE);
        createSpacer(centerComposite, SWT.NONE);

        String string = Messages.getString("RegisterWizardPage.legalconfirm", brandingService.getCorporationName());
        Composite c3 = new Composite(centerComposite, SWT.NONE);
        c3.setLayout(new GridLayout());
        c3.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        Label label3 = new Label(c3, SWT.NONE);
        label3.setText(string);
        // label3.setFont(font2);
        // createLegalInfos(centerComposite, 1, string2);

        createSpacer(centerComposite, SWT.NONE);

        Composite bottomComposite = new Composite(container, SWT.NONE);
        bottomComposite.setLayout(new GridLayout(3, true));

        bottomComposite.setLayoutData(new GridData());

        new Label(bottomComposite, SWT.NONE);
        // label3.setLayoutData(new GridData());

        new ImageCanvas(bottomComposite, ImageProvider.getImageDesc(ERepositoryImages.REGISTER_ICO));

        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.FINISH_ID, IDialogConstants.FINISH_LABEL, true);
    }

    private void saveConnectionBean(String email) {
        ConnectionUserPerReader perReader = ConnectionUserPerReader.getInstance();
        List<ConnectionBean> cons = perReader.forceReadConnections();
        if (cons.isEmpty()) { // if have exited some connections, don't add default connection.
            cons = new ArrayList<ConnectionBean>();
            ConnectionBean bean = ConnectionBean.getDefaultConnectionBean();
            bean.setUser(email);
            cons.add(bean);
            perReader.saveConnections(cons);
        }
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.FINISH_ID == buttonId) {
            if (email != null) {
                saveConnectionBean(email);
            }
            close();
        }

    }

    @Override
    protected void createCenterComposite(Composite composite, int style) {
    }

}
