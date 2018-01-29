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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * Image utilities. <br/>
 * 
 * $Id: ImageUtils.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ImageUtils {

    public enum ICON_SIZE {
        ICON_32(32),
        ICON_24(24),
        ICON_16(16),

        ;

        private int size;

        ICON_SIZE(int size) {
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }

        public String getComment() {
            return this.size + "x" + this.size; //$NON-NLS-1$
        }

    }

    /**
     * Save an image in a file.
     * 
     * @param image
     * @param file
     * @param format SWT.IMAGE_BMP, SWT.IMAGE_GIF, etc.
     */
    public static void save(Image image, String filePath, int format) {
        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[] { image.getImageData() };
        loader.save(filePath, format);
    }

    public static void save(byte[] imageData, String filePath, int format) {
        ImageLoader loader = new ImageLoader();
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        Image img = new Image(null, bais);
        loader.data = new ImageData[] { img.getImageData() };
        loader.save(filePath, format);
        img.dispose();
    }

    // /**
    // * Scale image with provided percent value.
    // */
    // public static Image scale(Image image, int percent) {
    // Transform transform = new Transform(image.getDevice());
    // float scale = (float) percent / 100f;
    // int newWidth = (int) (scale * image.getImageData().width);
    // int newHeight = (int) (scale * image.getImageData().height);
    // transform.scale(scale, scale);
    // GC gc = new GC(image);
    // gc.setTransform(transform);
    // gc.drawImage(image, 0, 0);
    //
    // Image newImage = new Image(image.getDevice(), newWidth, newHeight);
    // gc.copyArea(newImage, 0, 0);
    // return newImage;
    // }

    /**
     * Scale image with provided percent value. changed from orignal Trnasform class use because not compatible with
     * RAP.
     */
    public static Image scale(Image image, int percent) {
        float scale = (float) percent / 100f;
        int newWidth = (int) (scale * image.getImageData().width);
        int newHeight = (int) (scale * image.getImageData().height);
        ImageData imageData = image.getImageData().scaledTo(newWidth, newHeight);
        return ImageDescriptor.createFromImageData(imageData).createImage();
    }

    public static Image scale(Image image, ICON_SIZE size) {
        if (image != null && size != null) {
            ImageData imageData = image.getImageData().scaledTo(size.getSize(), size.getSize());
            return ImageDescriptor.createFromImageData(imageData).createImage();
        }
        return image;
    }

    public static Map<String, Image> propertyImgCachedImages = new HashMap<String, Image>();

    public static Image propertyLabelScale(String id, Image image, ICON_SIZE size) {
        if (image != null && size != null) {
            Image img = propertyImgCachedImages.get(id);
            if (img == null || img.isDisposed()) {
                ImageData imageData = image.getImageData().scaledTo(size.getSize(), size.getSize());
                img = ImageDescriptor.createFromImageData(imageData).createImage();
                propertyImgCachedImages.put(id, img);
            }
            return img;
        }
        return image;
    }

    public static ImageDescriptor scale(ImageDescriptor imageDes, ICON_SIZE size) {
        if (imageDes != null) {
            if (!checkSize(imageDes, size)) {
                ImageData imageData = imageDes.getImageData().scaledTo(size.getSize(), size.getSize());
                return ImageDescriptor.createFromImageData(imageData);
            }
        }
        return imageDes;
    }

    private static Map<byte[], ImageData> imageFromDataCachedImages = new HashMap<byte[], ImageData>();

    /**
     * By default, keep in memory the .
     * 
     * @param data
     * @param keepInMemory
     * @return
     */
    public static ImageDescriptor createImageFromData(byte[] data, boolean... keepInMemory) {
        if (data != null) {
            ImageData img = imageFromDataCachedImages.get(data);
            if (img == null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                img = new ImageData(bais);
                imageFromDataCachedImages.put(data, img);
            }
            return ImageDescriptor.createFromImageData(img);
        }
        return null;
    }

    public static void disposeImages(byte[] data) {
        if (data != null) {
            if (imageFromDataCachedImages.get(data) != null) {
                imageFromDataCachedImages.remove(data);
            }
        }
    }

    public static byte[] saveImageToData(ImageDescriptor imageDes) {
        if (imageDes != null) {
            Image img = imageDes.createImage();
            byte[] bytes = saveImageToData(img);
            img.dispose();
            return bytes;
        }
        return null;
    }

    public static byte[] saveImageToData(Image img) {
        if (img != null) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                BufferedOutputStream stream = new BufferedOutputStream(baos, 8192);
                ImageLoader imageLoader = new ImageLoader();
                imageLoader.data = new ImageData[] { img.getImageData() };
                imageLoader.save(stream, SWT.IMAGE_PNG);
                stream.flush();
                byte[] imageByteArray = baos.toByteArray();
                return imageByteArray;
            } catch (Exception e) {
                ExceptionHandler.process(e);
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                        if (img != null && !img.isDisposed())
                            img.dispose();
                    } catch (IOException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return null;
    }

    public static boolean checkSize(Image image, ICON_SIZE iconSize) {
        if (iconSize == null) {
            return true; // don't check the size
        }
        if (image != null) {
            if (image.getImageData().width == iconSize.getSize() && image.getImageData().height == iconSize.getSize()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkSize(ImageDescriptor imageDes, ICON_SIZE iconSize) {
        if (iconSize == null) {
            return true; // don't check the size
        }
        if (imageDes != null) {
            if (imageDes.getImageData().width == iconSize.getSize() && imageDes.getImageData().height == iconSize.getSize()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * DOC amaumont Comment method "changeAlpha".
     * 
     * @param image
     * @param alpha 0 is meaning fully transparent, 255 is meaning fully opaque
     * @return
     */
    public static Image changeAlpha(Image image, int alpha) {

        ImageData fullImageData = image.getImageData();
        int width = fullImageData.width;
        int height = fullImageData.height;
        byte[] alphaData = new byte[height * width];
        for (int y = 0; y < height; y++) {
            byte[] alphaRow = new byte[width];
            for (int x = 0; x < width; x++) {
                alphaRow[x] = (byte) alpha;
            }
            System.arraycopy(alphaRow, 0, alphaData, y * width, width);
        }
        fullImageData.alphaData = alphaData;
        Image modifiedImage = new Image(image.getDevice(), fullImageData);
        return modifiedImage;

    }

    /**
     * 
     * ggu Comment method "AdjustSize".
     * 
     * do the better size of image, when scale.
     */
    public static Point AdjustSize(final Point originalSize, final Point requiredSize) {
        Point newSize = new Point(originalSize.x, originalSize.y);

        // if the originalSize less than requiredSize, will keep
        if (originalSize.x <= requiredSize.x && originalSize.y <= requiredSize.y) {
            newSize.x = originalSize.x;
            newSize.y = originalSize.y;
        } else {
            // get the percent for width and height
            float w = originalSize.x / (float) requiredSize.x;
            float h = originalSize.y / (float) requiredSize.y;
            // if width is mort than height.
            if (w > h) {
                newSize.x = requiredSize.x;
                newSize.y = (int) (w >= 1 ? Math.round(originalSize.y / w) : Math.round(originalSize.y * w));
            }
            // if width is less than height.
            else if (w < h) {
                newSize.x = (int) (h >= 1 ? Math.round(originalSize.x / h) : Math.round(originalSize.x * h));
                newSize.y = requiredSize.y;
            }
            // if equal
            else {
                newSize.x = requiredSize.x;
                newSize.y = requiredSize.y;
            }
        }
        return newSize;
    }
}
