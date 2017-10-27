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
package org.talend.rcp.intro.starting;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.talend.commons.ui.html.BrowserDynamicPartLocationListener;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.rcp.Activator;

/**
 * DOC wchen class global comment. Detailled comment
 */
public class StartingBrowser extends EditorPart {

    public static final String ID = "org.talend.rcp.intro.starting.StartingBrowser"; //$NON-NLS-1$

    protected static final String ICON_WHITE_PATH = "/icons/appli_white_16x16.png"; //$NON-NLS-1$

    public StartingBrowser() {
        // Image[] images = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getImages();
        // if (images.length > 0) {
        // this.setTitleImage(images[0]);
        // }

        ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(Activator.class.getResource(ICON_WHITE_PATH));
        if (imageDescriptor != null) {
            Image image = imageDescriptor.createImage();
            if (image != null) {
                this.setTitleImage(image);
            }
        }
    }

    protected Browser browser;

    @Override
    public void createPartControl(Composite parent) {
        try {
            browser = new Browser(parent, SWT.NONE);
            browser.setText(StartingHelper.getHelper().getHtmlContent());
            browser.addLocationListener(new BrowserDynamicPartLocationListener());
            return;
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } catch (Throwable t) {

            Exception ex = new Exception("The internal web browser can not be access,the starting page won't be displayed");
            ExceptionHandler.process(ex);
        }
    }

    @Override
    public void setFocus() {
        if (browser != null) {
            browser.setFocus();
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doSaveAs() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        setPartName(input.getName());
    }

    @Override
    public boolean isDirty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        // TODO Auto-generated method stub
        return false;
    }

}
