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
package org.talend.updates.runtime.ui.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.talend.updates.runtime.i18n.Messages;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class UIUtils {

    private static FormColors formColors = new FormColors(Display.getDefault());

    public static FormColors getFormColors() {
        return formColors;
    }

    public static Image scaleImage(Image image, int width, int height) {
        Image scaled = new Image(Display.getDefault(), width, height);

        GC gc = new GC(scaled, SWT.NONE);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
        gc.dispose();

        ImageData imageData = scaled.getImageData();
        imageData.transparentPixel = imageData.palette.getPixel(new RGB(255, 255, 255));
        Image transparentImage = new Image(Display.getDefault(), imageData);
        scaled.dispose();

        return transparentImage;
    }

    public static void checkMonitor(IProgressMonitor monitor) throws Exception {
        if (monitor != null) {
            if (monitor.isCanceled()) {
                throw new InterruptedException(Messages.getString("UIUtils.exception.interrupt")); //$NON-NLS-1$
            }
        }
    }
}
