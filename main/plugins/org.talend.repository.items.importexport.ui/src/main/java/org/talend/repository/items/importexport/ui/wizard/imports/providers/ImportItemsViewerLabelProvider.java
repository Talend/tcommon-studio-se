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
package org.talend.repository.items.importexport.ui.wizard.imports.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryNodeProviderRegistryReader;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.repository.items.importexport.wizard.models.FolderImportNode;
import org.talend.repository.items.importexport.wizard.models.ImportNode;
import org.talend.repository.items.importexport.wizard.models.ItemImportNode;
import org.talend.repository.items.importexport.wizard.models.ProjectImportNode;
import org.talend.repository.items.importexport.wizard.models.StandardJobImportNode;
import org.talend.repository.items.importexport.wizard.models.TypeImportNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImportItemsViewerLabelProvider extends LabelProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        return getImageBasedOn(element);
    }

    public static Image getImageBasedOn(Object element) {
        if (element instanceof ImportNode) {
            if (element instanceof ProjectImportNode) {
                return ImageProvider.getImage(ECoreImage.PROJECT_ICON);
            } else if (element instanceof ItemImportNode) {
                Item item = ((ItemImportNode) element).getItemRecord().getItem();
                final ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
                if (itemType == ERepositoryObjectType.PROCESS_MR || itemType == ERepositoryObjectType.PROCESS_STORM) {
                    return ImageProvider.getImage(CoreImageProvider.getIcon(item));
                }
                return getImageBasedOn(itemType);
            } else if (element instanceof TypeImportNode) {
                ERepositoryObjectType repObjectType = ((TypeImportNode) element).getType();
                // also see RepoViewCommonNavigator.java
                if (repObjectType == ERepositoryObjectType.PROCESS_STORM) {
                    return ImageProvider.getImage(ECoreImage.PROCESS_STREAMING_GENERIC_CATEGORY_OPEN_ICON);
                } else if (repObjectType == ERepositoryObjectType.PROCESS_MR) {
                    return ImageProvider.getImage(ECoreImage.PROCESS_BATCH_GENERIC_CATEGORY_OPEN_ICON);
                } else {
                    return getImageBasedOn(repObjectType);
                }
            } else if (element instanceof StandardJobImportNode) {
                ERepositoryObjectType repObjectType = ((StandardJobImportNode) element).getType();
                if (repObjectType == ERepositoryObjectType.PROCESS) {
                    return ImageProvider.getImage(ECoreImage.PROCESS_STANDARD_GENERIC_CATEGORY_OPEN_ICON);
                }
                return getImageBasedOn(repObjectType);
            } else if (element instanceof FolderImportNode) {
                return CoreImageProvider.getImage(ERepositoryObjectType.FOLDER);
            }
        } else if (element instanceof ERepositoryObjectType) {
            ERepositoryObjectType itemType = (ERepositoryObjectType) element;
            if (itemType != null) {
                IGenericDBService dbService = null;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
                    dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                            IGenericDBService.class);
                }
                if(dbService != null && dbService.getExtraTypes().contains(itemType)){
                    itemType = dbService.getExtraDBType(itemType);
                }
                
                Image image = null;
                IGenericWizardService wizardService = null;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
                    wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(
                            IGenericWizardService.class);
                }
                if (wizardService != null && wizardService.isGenericType(itemType)) {
                    image = wizardService.getNodeImage(itemType.getType());
                } else {
                    image = RepositoryNodeProviderRegistryReader.getInstance().getImage(itemType);
                }
                if (image == null) {
                    image = CoreImageProvider.getImage(itemType);
                }
                return image;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        if (element instanceof ImportNode) {
            return ((ImportNode) element).getDisplayLabel();
        }
        return null;
    }

}
