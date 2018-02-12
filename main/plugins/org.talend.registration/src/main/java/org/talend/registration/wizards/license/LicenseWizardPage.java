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
package org.talend.registration.wizards.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.utils.TalendPropertiesUtil;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.registration.i18n.Messages;

/**
 * Page for new project details. <br/>
 * 
 * $Id: LicenseWizardPage.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class LicenseWizardPage extends WizardPage {

    /** CLUF field. */
    private Text clufText;

    private Browser clufBrowser;

    /**
     * Constructs a new LicenseWizardPage.
     * 
     * @param server
     * @param password
     * @param author
     */
    public LicenseWizardPage() {
        super("WizardPage"); //$NON-NLS-1$

        setTitle(Messages.getString("LicenseWizard.title")); //$NON-NLS-1$
        setDescription(""); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        ((GridData) parent.getLayoutData()).widthHint = 520;
        ((GridData) parent.getLayoutData()).heightHint = 280;
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);

        Label subTitleLabel = new Label(container, SWT.NONE);
        subTitleLabel.setText(Messages.getString("LicenseWizard.subtitle")); //$NON-NLS-1$

        GridData data = new GridData(GridData.FILL_BOTH);
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        boolean haveHtmlDesc = false;
        String filePath = null;
        try {
            URL url = brandingService.getLicenseFile();
            filePath = url.getFile();
            if (TalendPropertiesUtil.isEnabledUseBrowser()) {
                IPath file = new Path(filePath).removeFileExtension();
                file = file.addFileExtension("html"); //$NON-NLS-1$
                if (new File(file.toPortableString()).exists()) {
                    haveHtmlDesc = true;
                    filePath = file.toPortableString();
                }
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        if (haveHtmlDesc) {
            clufBrowser = new Browser(container, SWT.BORDER);
            clufBrowser.setText(getLicense(filePath));
            clufBrowser.setLayoutData(data);
        } else {
            clufText = new Text(container, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.LEFT | SWT.BORDER);
            clufText.setBackground(new Color(null, 255, 255, 255));
            Font font = new Font(clufText.getDisplay(), "courier", 10, SWT.NONE); //$NON-NLS-1$
            addResourceDisposeListener(clufText, font);
            clufText.setFont(font);
            clufText.setEditable(false);
            if (filePath != null) {
                clufText.setText(getLicense(filePath));
            }
            clufText.setLayoutData(data);
        }

        // Label footerLabel = new Label(container, SWT.NONE);
        // footerLabel.setText(Messages.getString("LicenseWizard.footer")); //$NON-NLS-1$

        setControl(container);
        setPageComplete(true);
    }

    private void addResourceDisposeListener(final Control parent, final Resource res) {
        if (parent != null) {
            parent.addDisposeListener(new DisposeListener() {

                @Override
                public void widgetDisposed(DisposeEvent e) {
                    if (res != null && !res.isDisposed()) {
                        res.dispose();
                    }
                    parent.removeDisposeListener(this);
                }
            });
        }

    }

    /**
     * DOC mhirt Comment method "getLicense".
     * 
     * @return
     * @throws PersistenceException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String getLicense(String filePath) {
        String license = ""; //$NON-NLS-1$
        try {
            FileReader fileReader = new FileReader(new File(filePath));
            BufferedReader in = new BufferedReader(fileReader);

            String licenseLine = ""; //$NON-NLS-1$
            while ((licenseLine = in.readLine()) != null) {
                license += licenseLine + "\n"; //$NON-NLS-1$
            }

        } catch (FileNotFoundException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
        return license;
    }
}
