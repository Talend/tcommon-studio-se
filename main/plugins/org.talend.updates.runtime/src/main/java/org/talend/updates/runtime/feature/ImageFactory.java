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
package org.talend.updates.runtime.feature;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class ImageFactory {

    private static ImageFactory instance;

    private List<Image> loadedFeatureImages;

    private ImageFactory() {
        loadedFeatureImages = Collections.synchronizedList(new LinkedList<>());
    }

    public static ImageFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (ImageFactory.class) {
            if (instance == null) {
                instance = new ImageFactory();
            }
        }
        return instance;
    }

    /**
     * <font color="red"><b>NOTE: </b></font> these created images will be disposed after dialog closed
     */
    public Image createFeatureImage(File imgFile) throws Exception {
        Image image = null;
        if (imgFile != null && imgFile.exists()) {
            ImageDescriptor imgDescriptor = ImageDescriptor.createFromURL(imgFile.toURI().toURL());
            if (imgDescriptor != null) {
                image = imgDescriptor.createImage();
                registFeatureImage(image);
            }
        }
        return image;
    }

    public void registFeatureImage(Image image) {
        loadedFeatureImages.add(image);
    }

    public void disposeFeatureImages() {
        for (Image image : loadedFeatureImages) {
            try {
                if (!image.isDisposed()) {
                    image.dispose();
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        loadedFeatureImages.clear();
    }

}
