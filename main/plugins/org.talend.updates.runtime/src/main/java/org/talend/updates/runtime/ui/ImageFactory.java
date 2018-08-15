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
package org.talend.updates.runtime.ui;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.threading.TalendCustomThreadPoolExecutor;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class ImageFactory {

    private static ImageFactory instance;

    private static Object lock = new Object();

    private List<Image> loadedFeatureImages;

    private TalendCustomThreadPoolExecutor threadPoolExecutor;

    private Object threadPoolExecutorLock = new Object();

    private ImageFactory() {
        loadedFeatureImages = Collections.synchronizedList(new LinkedList<>());
    }

    private TalendCustomThreadPoolExecutor createThreadPoolExecutor() {
        return new TalendCustomThreadPoolExecutor(60);
    }

    public static ImageFactory getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (lock) {
            if (instance == null) {
                instance = new ImageFactory();
            }
        }
        return instance;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor != null) {
            return threadPoolExecutor;
        }
        synchronized (threadPoolExecutorLock) {
            if (threadPoolExecutor == null) {
                threadPoolExecutor = createThreadPoolExecutor();
            }
        }
        return threadPoolExecutor;
    }

    public void clearThreadPool() {
        if (threadPoolExecutor != null) {
            synchronized (threadPoolExecutorLock) {
                if (threadPoolExecutor != null) {
                    TalendCustomThreadPoolExecutor tmp = threadPoolExecutor;
                    threadPoolExecutor = null;
                    tmp.clearThreads();
                }
            }
        }
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
