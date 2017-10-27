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
package org.talend.core.repository.ui.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.runtime.model.repository.ECDCStatus;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.ImageUtils;
import org.talend.commons.ui.runtime.image.ImageUtils.ICON_SIZE;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SAPBWTable;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.model.repository.RepositoryNodeProviderRegistryReader;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.ui.ICDCProviderService;
import org.talend.core.ui.IReferencedProjectService;
import org.talend.core.ui.ISparkJobletProviderService;
import org.talend.core.ui.ISparkStreamingJobletProviderService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.ui.images.RepositoryImageProvider;
import org.talend.cwm.helper.SAPBWTableHelper;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.utils.string.MD5;

/**
 * Label provider for the repository view. <code>DEBUG</code> boolean field specify if details (such as objects ids)
 * must appears on display or not.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryLabelProvider extends LabelProvider implements IColorProvider, IFontProvider {

    private static final Color STABLE_SECONDARY_ENTRY_COLOR = new Color(null, 100, 100, 100);

    private static final Color STABLE_PRIMARY_ENTRY_COLOR = new Color(null, 0, 0, 0);

    protected static final Color INACTIVE_ENTRY_COLOR = new Color(null, 200, 200, 200);

    private static final Color LOCKED_ENTRY = new Color(null, 200, 0, 0);

    private static final Color MERGED_REFERENCED_ITEMS_COLOR = new Color(null, 120, 120, 120);

    private IRepositoryView view;

    public RepositoryLabelProvider(IRepositoryView view) {
        super();
        this.view = view;
    }

    protected IRepositoryView getView() {
        return view;
    }

    public String getText(IRepositoryViewObject object) {
        StringBuffer string = new StringBuffer();
        string.append(object.getLabel());
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        boolean allowVerchange = brandingService.getBrandingConfiguration().isAllowChengeVersion();
        if (!(object instanceof Folder)) {
            if (allowVerchange) {
                string.append(" " + object.getVersion()); //$NON-NLS-1$
            }

            boolean isJunit = false;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
                ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                        .getDefault().getService(ITestContainerProviderService.class);
                if (testContainerService != null) {
                    isJunit = testContainerService.isTestContainerType(object.getRepositoryObjectType());
                }
            }

            // nodes in the recycle bin
            if (object.isDeleted() && !isJunit) {
                String oldPath = object.getPath();
                if (oldPath != null && !"".equals(oldPath)) {
                    string.append(" (" + oldPath + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        } else {
            String oldPath = object.getPath();
            if (object.isDeleted() && oldPath != null && !"".equals(oldPath)) {
                string.append(" (" + oldPath + ")"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return string.toString();
    }

    public String getText(Property property) {
        StringBuffer string = new StringBuffer();
        string.append(property.getLabel());
        // PTODO SML [FOLDERS++] temp code
        if (ERepositoryObjectType.getItemType(property.getItem()) != ERepositoryObjectType.FOLDER) {
            string.append(" " + property.getVersion()); //$NON-NLS-1$
        }
        // nodes in the recycle bin
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.getStatus(property.getItem()) == ERepositoryStatus.DELETED) {
            String oldPath = property.getItem().getState().getPath();
            string.append(" (" + oldPath + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return string.toString();
    }

    @Override
    public String getText(Object obj) {
        if (obj instanceof IRepositoryViewObject) {
            return getText((IRepositoryViewObject) obj);
        }
        if (obj instanceof Property) {
            return getText((Property) obj);
        }
        RepositoryNode node = (RepositoryNode) obj;

        if (node.getType() == ENodeType.REPOSITORY_ELEMENT || node.getType() == ENodeType.SIMPLE_FOLDER) {
            IRepositoryViewObject object = node.getObject();
            String label = ""; //$NON-NLS-1$
            object.getProperty();
            if (object.isModified()) {
                label = "> "; //$NON-NLS-1$
            }
            org.talend.core.model.properties.Project mainProject = ProjectManager.getInstance().getCurrentProject()
                    .getEmfProject();
            String projectLabel = object.getProjectLabel();

            ERepositoryObjectType repositoryObjectType = object.getRepositoryObjectType();
            if (repositoryObjectType == ERepositoryObjectType.METADATA_CON_QUERY
                    || repositoryObjectType == ERepositoryObjectType.SNIPPETS
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_SYNONYM
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_TABLE
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_VIEW
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_CDC
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_CDC
                    || repositoryObjectType == ERepositoryObjectType.METADATA_SAP_IDOC
                    || repositoryObjectType == ERepositoryObjectType.METADATA_SAP_FUNCTION
                    || repositoryObjectType == ERepositoryObjectType.METADATA_SALESFORCE_MODULE
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_COLUMN) {
                label = label + object.getLabel();

                if (object instanceof MetadataTableRepositoryObject) {
                    MetadataTable table = ((MetadataTableRepositoryObject) object).getTable();
                    if (table instanceof SAPBWTable) {
                        SAPBWTable bwTable = (SAPBWTable) table;
                        if (SAPBWTableHelper.TYPE_INFOOBJECT.equals(bwTable.getModelType())) {
                            String innerType = bwTable.getInnerIOType();
                            if (innerType != null && !innerType.equals(SAPBWTableHelper.IO_INNERTYPE_BASIC)) {
                                label = label + " (" + innerType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                            }
                        }
                    }
                }

                if (!mainProject.getLabel().equals(projectLabel) && PluginChecker.isRefProjectLoaded()) {

                    IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault()
                            .getService(IReferencedProjectService.class);
                    if (service != null && service.isMergeRefProject()) {
                        label = label + " (@" + projectLabel + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                    }

                }
                return label;
            }
            if (repositoryObjectType == ERepositoryObjectType.JOB_DOC || repositoryObjectType == ERepositoryObjectType.JOBLET_DOC
                    || repositoryObjectType == ERepositoryObjectType.valueOf(ERepositoryObjectType.class, "ROUTE_DOC")) {
                if (object.isModified()) {
                    label = "> "; //$NON-NLS-1$
                }
            }
            label = label + getText(object);
            if (!mainProject.getLabel().equals(projectLabel) && PluginChecker.isRefProjectLoaded()) {
                IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault().getService(
                        IReferencedProjectService.class);
                if (service != null && service.isMergeRefProject()) {
                    label = label + " (@" + projectLabel + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                }

            }

            return label;
        } else {
            String label = node.getLabel();
            return label;
        }
    }

    public Image getImage(IRepositoryViewObject object) {
        // Item item = property.getItem();
        ERepositoryObjectType itemType = object.getRepositoryObjectType();
        // must get the property here before if/else ,otherwise can not get the correct document(link) icon.
        Image img = null;
        if (object instanceof RepositoryViewObject && ((RepositoryViewObject) object).getCustomImage() != null) {
            img = ((RepositoryViewObject) object).getCustomImage();
        } else {
            // MOD sizhaoliu 2011-10-14
            // TDQ-3356 enable different icons of extension points under a same repository node
            boolean isExtensionPoint = false;
            for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                isExtensionPoint = handler.isRepObjType(itemType);
                if (isExtensionPoint) {
                    IImage icon = handler.getIcon(itemType);
                    if (handler.hasDynamicIcon()) {
                        Property property = object.getProperty();
                        if (property == null) {
                            // means item has been deleted or is not available for display now, so just return null
                            return null;
                        }
                        Item item = property.getItem();
                        icon = handler.getIcon(item);
                    }
                    if (icon != null) {
                        img = ImageProvider.getImage(icon);
                        break;
                    }
                }
            }
            if (!isExtensionPoint || img == null) {
                img = RepositoryNodeProviderRegistryReader.getInstance().getImage(itemType);
                if (img == null) {
                    img = getImageFromFramework(itemType);
                    if (img == null) {
                        IImage icon = RepositoryImageProvider.getIcon(itemType);
                        if (icon != null) {
                            img = ImageProvider.getImage(icon);
                        }
                    }
                }
            }
        }

        ERepositoryStatus repositoryStatus = object.getRepositoryStatus();

        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext rc = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);

        if (rc.isEditableAsReadOnly()) {
            if (repositoryStatus == ERepositoryStatus.LOCK_BY_USER) {
                repositoryStatus = ERepositoryStatus.DEFAULT;
            }
        }

        Image image = OverlayImageProvider.getImageWithStatus(img, repositoryStatus);

        ERepositoryStatus informationStatus = object.getInformationStatus();

        return OverlayImageProvider.getImageWithStatus(image, informationStatus);
    }

    protected Image getImageFromFramework(ERepositoryObjectType itemType) {
        IGenericWizardService wizardService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
        }
        if (wizardService != null && wizardService.isGenericType(itemType)) {
            return wizardService.getNodeImage(itemType.getType());
        }
        return null;
    }

    public static Image getDefaultJobletImage() {
        return ImageProvider.getImage(ECoreImage.JOBLET_COMPONENT_ICON);
    }
    
    public static Image getDefaultJobletImage(Item item) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ISparkJobletProviderService.class)) {
            ISparkJobletProviderService sparkJobletService = (ISparkJobletProviderService) GlobalServiceRegister
                    .getDefault().getService(ISparkJobletProviderService.class);
            if (sparkJobletService != null && sparkJobletService.isSparkJobletItem(item)) {
                return  ImageProvider.getImage(ECoreImage.SPARK_JOBLET_COMPONENT_ICON);
            }
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ISparkStreamingJobletProviderService.class)) {
            ISparkStreamingJobletProviderService sparkStreamingJobletService = (ISparkStreamingJobletProviderService) GlobalServiceRegister
                    .getDefault().getService(ISparkStreamingJobletProviderService.class);
            if (sparkStreamingJobletService != null && sparkStreamingJobletService.isSparkStreamingJobletItem(item)) {
                return  ImageProvider.getImage(ECoreImage.SPARK_STREAMING_JOBLET_COMPONENT_ICON);
            }
        }
        return ImageProvider.getImage(ECoreImage.JOBLET_COMPONENT_ICON);
    }

    /**
     * DOC bqian Comment method "getJobletCustomIcon".
     * 
     * @param property
     * @return
     */
    public static Image getJobletCustomIcon(Property property) {
        JobletProcessItem item = (JobletProcessItem) property.getItem();
        Image image = null;
        if (item.getIcon() == null || item.getIcon().getInnerContent() == null || item.getIcon().getInnerContent().length == 0) {
            image = getDefaultJobletImage(item);
        } else {

            ImageDescriptor imageDesc = ImageUtils.createImageFromData(item.getIcon().getInnerContent());
            imageDesc = ImageUtils.scale(imageDesc, ICON_SIZE.ICON_32);
            String md5Desc = MD5.getMD5(item.getIcon().getInnerContent());
            image = cachedImages.get(md5Desc);

            if (image == null || image.isDisposed()) {
                image = imageDesc.createImage();
                cachedImages.put(md5Desc, image);
            } else {
                // image = imageDesc.createImage();
            }
        }
        return image;
    }

    private static Map<String, Image> cachedImages = new HashMap<String, Image>();

    @Override
    public Image getImage(Object obj) {
        if (obj instanceof IRepositoryViewObject) {
            return getImage((IRepositoryViewObject) obj);
        }

        RepositoryNode node = (RepositoryNode) obj;

        IImage nodeIcon = node.getIcon();

        switch (node.getType()) {
        case STABLE_SYSTEM_FOLDER:
        case SYSTEM_FOLDER:
            ERepositoryObjectType contentType = node.getContentType();
            if (nodeIcon == null || EImage.DEFAULT_IMAGE.equals(nodeIcon)) {
                Image image = getImageFromFramework(contentType);
                if (image != null) {
                    return image;
                }
            }
            if (nodeIcon != null) {
                return ImageProvider.getImage(nodeIcon);
            }
            if (contentType != null) {
                Image image = RepositoryNodeProviderRegistryReader.getInstance().getImage(contentType);
                if (image != null) {
                    return image;
                }
            }
        case SIMPLE_FOLDER:
            ECoreImage image = null;
            if (getView() != null) {
                image = (getView().getExpandedState(obj) ? ECoreImage.FOLDER_OPEN_ICON : ECoreImage.FOLDER_CLOSE_ICON);
            } else {
                image = ECoreImage.FOLDER_OPEN_ICON;
            }
            return ImageProvider.getImage(image);
        default:
            if (node.getObject() == null) {
                return ImageProvider.getImage(nodeIcon);
            }

            ERepositoryObjectType repositoryObjectType = node.getObject().getRepositoryObjectType();
            if (repositoryObjectType == ERepositoryObjectType.METADATA_CON_QUERY
                    || repositoryObjectType == ERepositoryObjectType.SNIPPETS
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_SYNONYM
                    || repositoryObjectType == ERepositoryObjectType.METADATA_CON_VIEW
                    || repositoryObjectType == ERepositoryObjectType.JOB_DOC
                    || repositoryObjectType == ERepositoryObjectType.JOBLET_DOC) {
                return ImageProvider.getImage(nodeIcon);
            } else if (repositoryObjectType == ERepositoryObjectType.METADATA_CON_TABLE) {
                Image tableImage = ImageProvider.getImage(nodeIcon);
                Item item = node.getObject().getProperty().getItem();
                if (item != null && item instanceof DatabaseConnectionItem) {
                    if (PluginChecker.isCDCPluginLoaded()) {
                        ICDCProviderService service = (ICDCProviderService) GlobalServiceRegister.getDefault().getService(
                                ICDCProviderService.class);
                        if (service != null) {
                            String cdcLinkId = service.getCDCConnectionLinkId((DatabaseConnectionItem) item);
                            if (cdcLinkId != null) { // cdc connection exist.
                                if (node.getObject() instanceof MetadataTableRepositoryObject) {
                                    MetadataTable table = ((MetadataTableRepositoryObject) node.getObject()).getTable();
                                    String tableType = table.getTableType();
                                    if (tableType != null && "TABLE".equals(tableType)) { //$NON-NLS-1$
                                        ECDCStatus status = ECDCStatus.NONE;
                                        if (table.isActivatedCDC()) {
                                            status = ECDCStatus.ACTIVATED;
                                        } else if (table.isAttachedCDC()) {
                                            status = ECDCStatus.ADDED;
                                        }
                                        return OverlayImageProvider.getImageWithCDCStatus(tableImage, status).createImage();
                                    }
                                }
                            }
                        }
                    }
                }
                return tableImage;
            } else if (repositoryObjectType == ERepositoryObjectType.METADATA_CON_CDC) {
                IImage icon = RepositoryImageProvider.getIcon(repositoryObjectType);
                if (icon != null) {
                    return ImageProvider.getImage(icon);
                }
            }
            return getImage(node.getObject());
        }
    }

    @Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
    public Color getForeground(Object element) {
        RepositoryNode node = (RepositoryNode) element;
        switch (node.getType()) {
        case REFERENCED_PROJECT:
            return STABLE_PRIMARY_ENTRY_COLOR;
        case STABLE_SYSTEM_FOLDER:
            if (node.getLabel().equals(ERepositoryObjectType.SNIPPETS.toString())) {
                return INACTIVE_ENTRY_COLOR;
            }
            if (node.getContentType() == ERepositoryObjectType.METADATA) {
                return STABLE_PRIMARY_ENTRY_COLOR;
            }
        case SYSTEM_FOLDER:
            if (node.getContentType() == ERepositoryObjectType.BUSINESS_PROCESS) {
                return STABLE_PRIMARY_ENTRY_COLOR;
            }
            if (node.getContentType() == ERepositoryObjectType.PROCESS) {
                return STABLE_PRIMARY_ENTRY_COLOR;
            }
            return STABLE_SECONDARY_ENTRY_COLOR;
        default:
            ERepositoryStatus repositoryStatus = node.getObject().getRepositoryStatus();
            if (repositoryStatus == ERepositoryStatus.LOCK_BY_OTHER) {
                return LOCKED_ENTRY;
            } else {
                if (PluginChecker.isRefProjectLoaded()) {
                    IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault()
                            .getService(IReferencedProjectService.class);
                    if (service != null && service.isMergeRefProject()) {
                        IRepositoryViewObject object = node.getObject();
                        if (object != null) {
                            org.talend.core.model.properties.Project mainProject = ProjectManager.getInstance()
                                    .getCurrentProject().getEmfProject();
                            String projectLabel = object.getProjectLabel();
                            if (!mainProject.getLabel().equals(projectLabel)) {
                                return MERGED_REFERENCED_ITEMS_COLOR;
                            }
                        }
                    }
                }
                return null;
            }
        }
    }

    @Override
    public Font getFont(Object element) {
        RepositoryNode node = (RepositoryNode) element;
        switch (node.getType()) {
        case STABLE_SYSTEM_FOLDER:
            if (node.getLabel().equals(ERepositoryObjectType.SNIPPETS.toString())) {
                return JFaceResources.getFontRegistry().defaultFont();
            }
        case SYSTEM_FOLDER:
            return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
        default:
            return JFaceResources.getFontRegistry().defaultFont();
        }
    }

}
