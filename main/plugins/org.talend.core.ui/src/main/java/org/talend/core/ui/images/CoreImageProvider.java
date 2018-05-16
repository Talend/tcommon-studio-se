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
package org.talend.core.ui.images;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.ImageUtils.ICON_SIZE;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.BigDataBasicUtil;
import org.talend.core.hadoop.HadoopConstants;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.utils.string.MD5;

/**
 * amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CoreImageProvider {

    public static Image getImage(ERepositoryObjectType type) {
        return ImageProvider.getImage(getIcon(type));
    }

    public static ImageDescriptor getImageDesc(ERepositoryObjectType type) {
        return ImageProvider.getImageDesc(getIcon(type));
    }

    public static IImage getIcon(ERepositoryObjectType type) {
        return RepositoryImageProvider.getIcon(type);
    }

    public static IImage getIcon(Item item) {
        if (item == null) {
            return null;
        }
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
        if (itemType == null) {
            return null;
        }
        if (itemType == ERepositoryObjectType.PROCESS_MR || itemType == ERepositoryObjectType.PROCESS_STORM) {
            Object framework = BigDataBasicUtil.getFramework(item);
            if (HadoopConstants.FRAMEWORK_SPARK.equals(framework)) {
                return ECoreImage.PROCESS_BATCH_SPARK_ICON;
            } else if (HadoopConstants.FRAMEWORK_MAPREDUCE.equals(framework)) {
                return ECoreImage.PROCESS_BATCH_MR_ICON;
            } else if (HadoopConstants.FRAMEWORK_SPARKSTREAMING.equals(framework)) {
                return ECoreImage.PROCESS_STREAMING_SPARK_ICON;
            } else if (HadoopConstants.FRAMEWORK_STORM.equals(framework)) {
                return ECoreImage.PROCESS_STREAMING_STORM_ICON;
            }
            // the following statements are for the items exported from old studio versions since they have no framework
            // properties
            else if (itemType == ERepositoryObjectType.PROCESS_MR) {
                return ECoreImage.PROCESS_BATCH_MR_ICON;
            } else if (itemType == ERepositoryObjectType.PROCESS_STORM) {
                return ECoreImage.PROCESS_STREAMING_STORM_ICON;
            }
        }
        return getIcon(itemType);
    }

    private static Map<String, Image> componentCachedImages = new HashMap<String, Image>();

    public static Image getComponentImageFromDesc(ImageDescriptor imageDescriptor) {
        String md5Desc = MD5.getMD5(imageDescriptor.getImageData().data);
        Image image = componentCachedImages.get(md5Desc);

        if (image == null || image.isDisposed()) {
            image = imageDescriptor.createImage();
            componentCachedImages.put(md5Desc, image);
        }
        return image;
    }

    public static Image getComponentIcon(IComponent component, ICON_SIZE iconSize) {
        if (component != null && iconSize != null) {
            if (iconSize == ICON_SIZE.ICON_32
                    && GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                        IDesignerCoreService.class);
                if (service.isDummyComponent(component)) {
                    return getComponentImageFromDesc(component.getIcon32());
                }
            }

            ImageDescriptor icon = null;
            switch (iconSize) {
            case ICON_16:
                icon = component.getIcon16();
                break;
            case ICON_24:
                icon = component.getIcon24();
                break;
            case ICON_32:
            default:
                icon = component.getIcon32();
            }
            // default
            if (icon == null) {
                icon = component.getIcon32();
            }
            if (icon != null) {
                return getComponentImageFromDesc(icon);
            }
        }
        return null;
    }

    public static void removeComponentImage(String name) {
        if (name != null && !name.equals("")) { //$NON-NLS-1$
            for (IComponent component : ComponentsFactoryProvider.getInstance().readComponents()) {
                if (name.equals(component.getName())) {
                    String md5Desc16 = MD5.getMD5(component.getIcon16().getImageData().data);
                    Image image = componentCachedImages.get(md5Desc16);
                    if (image != null && !image.isDisposed()) {
                        image.dispose();
                    }
                    componentCachedImages.remove(md5Desc16);
                    String md5Desc24 = MD5.getMD5(component.getIcon24().getImageData().data);
                    image = componentCachedImages.get(md5Desc24);
                    if (image != null && !image.isDisposed()) {
                        image.dispose();
                    }
                    componentCachedImages.remove(md5Desc24);
                    String md5Desc32 = MD5.getMD5(component.getIcon32().getImageData().data);
                    image = componentCachedImages.get(md5Desc32);
                    if (image != null && !image.isDisposed()) {
                        image.dispose();
                    }
                    componentCachedImages.remove(md5Desc32);
                }
            }
        }
    }

    public static void clearComponentIconImages() {
        for (Image image : componentCachedImages.values()) {
            if (image != null && !image.isDisposed()) {
                image.dispose();
            }
        }
        componentCachedImages.clear();
    }

    public static Image getImageFromFramework(ERepositoryObjectType itemType) {
        IGenericWizardService wizardService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
        }
        if (wizardService != null && wizardService.isGenericType(itemType)) {
            return wizardService.getNodeImage(itemType.getType());
        }
        return null;
    }

}
