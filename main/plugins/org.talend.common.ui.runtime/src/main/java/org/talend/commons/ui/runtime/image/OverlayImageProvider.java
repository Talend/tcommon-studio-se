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
package org.talend.commons.ui.runtime.image;

import java.util.Arrays;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.runtime.model.repository.ECDCStatus;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.image.OverlayImage.EPosition;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class OverlayImageProvider {

    private static final int BUTTOM_RIGHT = 0;

    private static final int BUTTOM_LEFT = 1;

    public static ImageDescriptor getImageWithNew(Image source) {
        ImageDescriptor img = ImageProvider.getImageDesc(ECoreImage.NEW_OVERLAY);
        EPosition position = EPosition.TOP_RIGHT;
        OverlayImage overlayImage = new OverlayImage(source, img, position);
        return overlayImage;
    }

    public static ImageDescriptor getImageWithSpecial(Image source) {
        ImageDescriptor img = ImageProvider.getImageDesc(ECoreImage.MASTER_OVERLAY);
        EPosition position = EPosition.TOP_RIGHT;
        OverlayImage overlayImage = new OverlayImage(source, img, position);
        return overlayImage;
    }

    public static Image getImageWithDocExt(String extension) {
        Image source = ImageProvider.getImage(ECoreImage.DOCUMENTATION_SINGLE_ICON);
        if (extension != null) {
            ImageDescriptor img = null;
            String ext = extension.toLowerCase();
            if (Arrays.asList(new String[] { "doc", "dot" }).contains(ext)) { //$NON-NLS-1$ //$NON-NLS-2$
                img = ImageProvider.getImageDesc(ECoreImage.DOC_WORD_OVERLAY);
            } else if (Arrays.asList(new String[] { "xls", "xlt" }).contains(ext)) { //$NON-NLS-1$ //$NON-NLS-2$
                img = ImageProvider.getImageDesc(ECoreImage.DOC_EXCEL_OVERLAY);
            } else if (Arrays.asList(new String[] { "ppt", "pps", "pot" }).contains(ext)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                img = ImageProvider.getImageDesc(ECoreImage.DOC_PPT_OVERLAY);
            } else if (Arrays.asList(new String[] { "pdf" }).contains(ext)) { //$NON-NLS-1$
                img = ImageProvider.getImageDesc(ECoreImage.DOC_PDF_OVERLAY);
            } else if (Arrays.asList(new String[] { "gif", "jpg", "jpeg", "bmp", "gif", "png" }).contains(ext)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            }
            if (img == null) {
                return source;
            } else {
                return new OverlayImage(source, img, EPosition.BOTTOM_RIGHT).createImage();
            }
        }
        return source;
    }

    public static Image getImageWithStatus(Image source, ERepositoryStatus status) {
        IImage statusOverlay;
        int p = BUTTOM_RIGHT;

        switch (status) {
        case NEW:
            statusOverlay = ECoreImage.NEW_OVERLAY;
            break;
        case LOCK_BY_OTHER:
            statusOverlay = ECoreImage.LOCKED_OTHER_OVERLAY;
            break;
        case LOCK_BY_USER:
            statusOverlay = ECoreImage.LOCKED_USER_OVERLAY;
            break;
        case WARN:
            statusOverlay = ECoreImage.WARN_OVERLAY;
            p = BUTTOM_LEFT;
            break;
        case ERROR:
            statusOverlay = ECoreImage.ERROR_OVERLAY;
            p = BUTTOM_LEFT;
            break;
        default:
            statusOverlay = EImage.EMPTY;
            break;
        }

        ImageDescriptor img = ImageProvider.getImageDesc(statusOverlay);
        EPosition position = null;
        if (p == BUTTOM_RIGHT) {
            position = EPosition.BOTTOM_RIGHT;
        } else if (p == BUTTOM_LEFT) {
            position = EPosition.BOTTOM_LEFT;
        }
        OverlayImage overlayImage = new OverlayImage(source, img, position);
        return ImageProvider.getImage(overlayImage);
    }

    public static ImageDescriptor getImageWithError(Image source) {
        IImage statusOverlay = ECoreImage.ERROR_OVERLAY;
        ImageDescriptor img = ImageProvider.getImageDesc(statusOverlay);
        EPosition position = EPosition.BOTTOM_LEFT;
        OverlayImage overlayImage = new OverlayImage(source, img, position);
        return overlayImage;
    }

    public static ImageDescriptor getImageWithWarn(Image source) {
        IImage statusOverlay = ECoreImage.WARN_OVERLAY;
        ImageDescriptor img = ImageProvider.getImageDesc(statusOverlay);
        EPosition position = EPosition.BOTTOM_LEFT;
        OverlayImage overlayImage = new OverlayImage(source, img, position);
        return overlayImage;
    }

    public static ImageDescriptor getImageWithCDCStatus(Image source, ECDCStatus status) {
        IImage statusOverlay = EImage.EMPTY;
        switch (status) {
        case ADDED:
            statusOverlay = ECoreImage.CDC_ADDED_OVERLAY;
            break;
        case ACTIVATED:
            statusOverlay = ECoreImage.CDC_ACTIVATED_OVERLAY;
            break;
        case NONE:
        default:
            statusOverlay = EImage.EMPTY;
        }
        ImageDescriptor img = ImageProvider.getImageDesc(statusOverlay);
        OverlayImage overlayImage = new OverlayImage(source, img, EPosition.BOTTOM_LEFT);
        return overlayImage;
    }

    public static Image getImageForOverlay(Image source, IImage overlay, EPosition position) {
        if (source == null) {
            return null;
        }
        if (position == null) {
            position = EPosition.BOTTOM_LEFT;
        }
        if (overlay != null) {
            ImageDescriptor img = ImageProvider.getImageDesc(overlay);
            OverlayImage overlayImage = new OverlayImage(source, img, position);
            // return ImageProvider.getOverlayImage(overlayImage);
            return ImageProvider.getImage(overlayImage);
        }
        return source;
    }

    public static Image getImageForOverlay(IImage source, IImage overlay, EPosition position) {
        if (source == null) {
            return null;
        }
        return getImageForOverlay(ImageProvider.getImage(source), overlay, position);
    }
}
