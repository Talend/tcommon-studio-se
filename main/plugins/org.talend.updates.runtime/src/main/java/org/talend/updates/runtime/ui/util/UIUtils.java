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

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.talend.commons.ui.runtime.ColorConstants;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.ui.feature.model.EMessageType;
import org.talend.updates.runtime.ui.feature.model.Message;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class UIUtils {

    private static FormColors formColors;

    private static final Object formColorsLock = new Object();

    public static FormColors getFormColors() {
        if (formColors != null) {
            return formColors;
        }
        synchronized (formColorsLock) {
            if (formColors == null) {
                formColors = new FormColors(Display.getDefault());
            }
        }
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
        boolean isInterrupted = false;
        if (monitor != null) {
            if (monitor.isCanceled()) {
                isInterrupted = true;
            }
        }
        if (Thread.currentThread().isInterrupted()) {
            isInterrupted = true;
        }
        if (isInterrupted) {
            throw new InterruptedException(Messages.getString("UIUtils.exception.interrupt")); //$NON-NLS-1$
        }
    }

    public static void appendMessage(StringBuffer strBuff, Collection<StyleRange> styles, Message message) {
        if (message == null) {
            return;
        }
        String msg = message.getMessage();
        if (StringUtils.isBlank(msg)) {
            return;
        }

        if (styles != null) {
            EMessageType type = message.getType();
            if (type == null) {
                type = EMessageType.INFO;
            }
            Color fontColor = null;
            switch (type) {
            case WARN:
                fontColor = ColorConstants.YELLOW_COLOR;
                break;
            case ERROR:
                fontColor = ColorConstants.RED_COLOR;
                break;
            default:
                fontColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
                break;
            }
            Point msgPosition = new Point(strBuff.length(), msg.length());
            StyleRange msgTyleRange = new StyleRange(msgPosition.x, msgPosition.y, fontColor, null, SWT.ITALIC | SWT.BOLD);
            styles.add(msgTyleRange);
        }
        strBuff.append(msg);
    }
}
