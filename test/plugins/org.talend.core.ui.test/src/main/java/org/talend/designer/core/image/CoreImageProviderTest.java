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
package org.talend.designer.core.image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.ui.runtime.image.ImageUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.CoreImageProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class CoreImageProviderTest {

    @Test
    public void testGetImage() {
        Image image1 = CoreImageProvider.getImage(ERepositoryObjectType.PROCESS);
        Assert.assertNotNull(image1);
        ImageData imageData1 = image1.getImageData();
        // image1.dispose();

        Image image2 = CoreImageProvider.getImage(ERepositoryObjectType.PROCESS);
        Assert.assertNotNull(image2);
        Assert.assertEquals(image2, image1); // same object from cache

        ImageData imageData2 = image2.getImageData();
        Assert.assertNotEquals(imageData2, imageData1);

    }

    @Test
    public void testGetImageWithDispose() {
        Image image1 = CoreImageProvider.getImage(ERepositoryObjectType.PROCESS);
        Assert.assertNotNull(image1);
        ImageData imageData1 = image1.getImageData();
        image1.dispose();

        Image image2 = CoreImageProvider.getImage(ERepositoryObjectType.PROCESS);
        Assert.assertNotNull(image2);
        // different object, because first one was disposed, so the second will be re-create.
        Assert.assertNotEquals(image2, image1);

        ImageData imageData2 = image2.getImageData();
        Assert.assertNotEquals(imageData2, imageData1);

    }

    @Test
    public void testData4SameImageDescriptor() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        byte[] data1 = ImageUtils.saveImageToData(imgDesc);
        byte[] data2 = ImageUtils.saveImageToData(imgDesc);
        Assert.assertNotEquals(data2, data1); // different object(Array)
    }

    @Test
    public void testData4SameNewImageDescriptor() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        Image img = imgDesc.createImage();
        Assert.assertNotNull(img);

        ImageDescriptor newImgDesc = ImageDescriptor.createFromImageData(img.getImageData());
        Assert.assertNotNull(newImgDesc);

        byte[] data1 = ImageUtils.saveImageToData(newImgDesc);
        byte[] data2 = ImageUtils.saveImageToData(newImgDesc);
        Assert.assertNotEquals(data2, data1);
    }

    @Test
    public void testData4SameImage() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        Image img = imgDesc.createImage();
        Assert.assertNotNull(img);

        ImageData imageData1 = img.getImageData();
        ImageData imageData2 = img.getImageData();
        Assert.assertNotEquals(imageData2, imageData1);

        ImageDescriptor newImgDesc1 = ImageDescriptor.createFromImageData(imageData1);
        ImageDescriptor newImgDesc2 = ImageDescriptor.createFromImageData(imageData2);
        Assert.assertNotEquals(newImgDesc2, newImgDesc1);

        byte[] data1 = ImageUtils.saveImageToData(newImgDesc1);
        byte[] data2 = ImageUtils.saveImageToData(newImgDesc2);
        Assert.assertNotEquals(data2, data1);
    }

    @Test
    public void testData4SameImageData() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        Image img = imgDesc.createImage();
        Assert.assertNotNull(img);

        ImageData imageData = img.getImageData();
        Assert.assertNotNull(imageData);

        ImageDescriptor newImgDesc1 = ImageDescriptor.createFromImageData(imageData);
        ImageDescriptor newImgDesc2 = ImageDescriptor.createFromImageData(imageData);
        Assert.assertEquals(newImgDesc2, newImgDesc1);

        byte[] data1 = ImageUtils.saveImageToData(newImgDesc1);
        byte[] data2 = ImageUtils.saveImageToData(newImgDesc2);
        Assert.assertNotEquals(data2, data1);
    }

    @Test
    public void testData4DifferentNewImageDescriptor() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        Image img1 = imgDesc.createImage();
        Image img2 = imgDesc.createImage();
        Assert.assertNotEquals(img2, img1);

        ImageData imageData1 = img1.getImageData();
        ImageData imageData2 = img2.getImageData();
        Assert.assertNotEquals(imageData2, imageData1);

        ImageDescriptor newImgDesc1 = ImageDescriptor.createFromImageData(imageData1);
        ImageDescriptor newImgDesc2 = ImageDescriptor.createFromImageData(imageData2);
        Assert.assertNotEquals(newImgDesc2, newImgDesc1);

        byte[] data1 = ImageUtils.saveImageToData(newImgDesc1);
        byte[] data2 = ImageUtils.saveImageToData(newImgDesc2);
        Assert.assertNotEquals(data2, data1);
    }

    @Test
    public void testData4DifferentImageDescriptor() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc1 = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        ImageDescriptor imgDesc2 = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertEquals(imgDesc2, imgDesc1);

        Image img1 = imgDesc1.createImage();
        Image img2 = imgDesc2.createImage();
        Assert.assertNotEquals(img2, img1);

        ImageData imageData1 = img1.getImageData();
        ImageData imageData2 = img2.getImageData();
        Assert.assertNotEquals(imageData2, imageData1);

        ImageDescriptor newImgDesc1 = ImageDescriptor.createFromImageData(imageData1);
        ImageDescriptor newImgDesc2 = ImageDescriptor.createFromImageData(imageData2);
        Assert.assertNotEquals(newImgDesc2, newImgDesc1);

        byte[] data1 = ImageUtils.saveImageToData(newImgDesc1);
        byte[] data2 = ImageUtils.saveImageToData(newImgDesc2);
        Assert.assertNotEquals(data2, data1);
    }

    @Test
    public void testImageDescriptor4SameData() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        byte[] data = ImageUtils.saveImageToData(imgDesc);
        Assert.assertNotNull(data);

        ImageDescriptor newImgDesc1 = ImageUtils.createImageFromData(data);
        ImageDescriptor newImgDesc2 = ImageUtils.createImageFromData(data);
        Assert.assertEquals(newImgDesc2, newImgDesc1); // same object from cache.
    }

    @Test
    public void testImageDescriptor4SameDataWithDispose() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        byte[] data = ImageUtils.saveImageToData(imgDesc);
        Assert.assertNotNull(data);

        ImageDescriptor newImgDesc1 = ImageUtils.createImageFromData(data);
        ImageUtils.disposeImages(data); // clean the cache.
        ImageDescriptor newImgDesc2 = ImageUtils.createImageFromData(data);
        Assert.assertNotEquals(newImgDesc2, newImgDesc1); // different object
    }

    @Test
    public void testImageDescriptor4DifferentData() {
        IImage icon = CoreImageProvider.getIcon(ERepositoryObjectType.PROCESS);
        ImageDescriptor imgDesc = ImageDescriptor.createFromFile(icon.getLocation(), icon.getPath());
        Assert.assertNotNull(imgDesc);

        byte[] data1 = ImageUtils.saveImageToData(imgDesc);
        byte[] data2 = ImageUtils.saveImageToData(imgDesc);
        Assert.assertNotEquals(data2, data1);

        ImageDescriptor newImgDesc1 = ImageUtils.createImageFromData(data1);
        ImageDescriptor newImgDesc2 = ImageUtils.createImageFromData(data2);
        Assert.assertNotEquals(newImgDesc2, newImgDesc1);

        Image img1 = newImgDesc1.createImage();
        Image img2 = newImgDesc2.createImage();
        Assert.assertNotEquals(img2, img1);
    }

}
