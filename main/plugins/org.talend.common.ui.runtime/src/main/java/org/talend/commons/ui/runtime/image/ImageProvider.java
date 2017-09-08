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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.MultiKeyMap;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: ImageProvider.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ImageProvider {

    private static Map<ImageDescriptor, Image> cachedImages = new HashMap<ImageDescriptor, Image>();

    public static Image getImage(ImageDescriptor imageDescriptor) {
        Image image = null;
        image = cachedImages.get(imageDescriptor);
        if (image == null || image.isDisposed()) {
            image = imageDescriptor.createImage();
            cachedImages.put(imageDescriptor, image);
        }
        return image;
    }

    private static MultiKeyMap cacheDescriptors = new MultiKeyMap();

    private static MultiKeyMap cacheImages = new MultiKeyMap();

    public static Image getImage(IImage image) {
        Image toReturn = (Image) cacheImages.get(image.getLocation(), image.getPath());
        if (toReturn == null || toReturn.isDisposed()) {
            ImageDescriptor desc = getImageDesc(image);
            toReturn = getImage(desc);
            cacheImages.put(image.getLocation(), image.getPath(), toReturn);
        }
        return toReturn;
    }

    public static ImageDescriptor getImageDesc(IImage image) {
        ImageDescriptor toReturn = (ImageDescriptor) cacheDescriptors.get(image.getLocation(), image.getPath());
        if (toReturn == null) {
            toReturn = ImageDescriptor.createFromFile(image.getLocation(), image.getPath());
            cacheDescriptors.put(image.getLocation(), image.getPath(), toReturn);
        }
        return toReturn;
    }

    public static String getImageCache() {
        return cacheImages.keySet().toString();
    }

    public static String getImageDescCache() {
        return cacheDescriptors.keySet().toString();
    }

}
