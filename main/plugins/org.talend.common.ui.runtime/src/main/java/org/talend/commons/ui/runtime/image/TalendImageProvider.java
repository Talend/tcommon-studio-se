package org.talend.commons.ui.runtime.image;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;

public class TalendImageProvider implements ImageDataProvider {

    private ImageData imageData;

    public TalendImageProvider(ImageData imageData) {
        this.imageData = imageData;
    }

    @Override
    public ImageData getImageData(int zoom) {
        if (zoom == 100) {
            return imageData;
        }
        return null;
    }

}
