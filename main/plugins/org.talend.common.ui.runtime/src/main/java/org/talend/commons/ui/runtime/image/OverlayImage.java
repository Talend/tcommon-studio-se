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

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class OverlayImage extends CompositeImageDescriptor {

    /**
     * DOC smallet OverlayImage class global comment. Detailled comment
     * 
     * $Id$
     * 
     */
    public enum EPosition {
        TOP_RIGHT(false, true),
        TOP_LEFT(true, true),
        BOTTOM_RIGHT(false, false),
        BOTTOM_LEFT(true, false);

        private boolean left;

        private boolean top;

        private EPosition(boolean left, boolean top) {
            this.left = left;
            this.top = top;
        }

        /**
         * Getter for left.
         * 
         * @return the left
         */
        public boolean isLeft() {
            return this.left;
        }

        /**
         * Getter for top.
         * 
         * @return the top
         */
        public boolean isTop() {
            return this.top;
        }

    }

    private ImageDescriptor mOverlay;

    public ImageDescriptor getMOverlay() {
        return mOverlay;
    }

    public void setMOverlay(ImageDescriptor overlay) {
        mOverlay = overlay;
    }

    private Image mimage;

    private boolean mleft;

    private boolean mtop;

    public OverlayImage(Image baseImage, ImageDescriptor overlay, boolean left, boolean top) {
        mimage = baseImage;
        mOverlay = overlay;
        mleft = left;
        mtop = top;
    }

    public OverlayImage(Image baseImage, ImageDescriptor overlay, EPosition position) {
        this(baseImage, overlay, position.isLeft(), position.isTop());
    }

    protected void drawCompositeImage(int width, int height) {
        drawImage(mimage.getImageData(), 0, 0);
        if (mOverlay != null) {
            ImageData id = mOverlay.getImageData();
            int ox, oy;
            if (mleft) {
                ox = 0;
            } else {
                ox = width - id.width;
            }
            if (mtop) {
                oy = 0;
            } else {
                oy = height - id.height;
            }
            if (id != null) {
                drawImage(id, ox, oy);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
     */
    protected Point getSize() {
        return new Point(16, 16);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.mOverlay == null) ? 0 : this.mOverlay.hashCode());
        result = prime * result + ((this.mimage == null) ? 0 : this.mimage.hashCode());
        result = prime * result + (this.mleft ? 1231 : 1237);
        result = prime * result + (this.mtop ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        final OverlayImage other = (OverlayImage) obj;
        if (this.mOverlay == null) {
            if (other.mOverlay != null)
                return false;
        } else if (!this.mOverlay.equals(other.mOverlay))
            return false;
        if (this.mimage == null) {
            if (other.mimage != null)
                return false;
        } else if (!this.mimage.equals(other.mimage))
            return false;
        if (this.mleft != other.mleft)
            return false;
        if (this.mtop != other.mtop)
            return false;
        return true;
    }
}
