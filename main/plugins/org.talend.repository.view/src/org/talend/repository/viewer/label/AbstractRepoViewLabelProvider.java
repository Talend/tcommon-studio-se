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
package org.talend.repository.viewer.label;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.commons.ui.utils.image.ColorUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryNodeProviderRegistryReader;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.IReferencedProjectService;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.ui.images.RepositoryImageProvider;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractRepoViewLabelProvider extends LabelProvider implements ICommonLabelProvider, IFontProvider,
        IColorProvider {

    private static final String SPACE = " "; //$NON-NLS-1$

    private static final RGB COLOR_LOCKED = new RGB(200, 0, 0);

    @Override
    public String getDescription(Object anElement) {
        return null;
    }

    @Override
    public void restoreState(IMemento aMemento) {

    }

    @Override
    public void saveState(IMemento aMemento) {

    }

    @Override
    public void init(ICommonContentExtensionSite aConfig) {

    }

    @Override
    public Color getForeground(Object element) {
        RepositoryNode node = (RepositoryNode) element;
        if (node != null) {
            IRepositoryViewObject object = node.getObject();
            if (object != null) {
                ERepositoryStatus repositoryStatus = object.getRepositoryStatus();
                if (repositoryStatus == ERepositoryStatus.LOCK_BY_OTHER) {
                    return ColorUtils.getCacheColor(COLOR_LOCKED);
                }
            }
        }
        return null;
    }

    @Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
    public Font getFont(Object element) {
        if (element instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) element;
            switch (node.getType()) {
            case SYSTEM_FOLDER:
                return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
            case STABLE_SYSTEM_FOLDER:
            default:
            }
        }
        return JFaceResources.getFontRegistry().defaultFont();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
     */
    @Override
    public Image getImage(Object element) {
        if (element instanceof IRepositoryViewObject) {
            return getElementImage((IRepositoryViewObject) element);
        } else if (element instanceof RepositoryNode) {
            return getElementImage((RepositoryNode) element);
        }
        return super.getImage(element);
    }

    protected Image getElementImage(IRepositoryViewObject object) {
        Image img = null;

        final ERepositoryObjectType itemType = object.getRepositoryObjectType();
        object.getProperty(); // this will force an update of the item.

        if (itemType != null) {
            img = RepositoryNodeProviderRegistryReader.getInstance().getImage(itemType);
            if (img == null) {
                IImage icon = getIcon(itemType);
                if (icon != null) {
                    img = ImageProvider.getImage(icon);
                }
            }
            if (img != null) {
                final ERepositoryStatus repositoryStatus = object.getRepositoryStatus();
                if (repositoryStatus != null) {
                    img = OverlayImageProvider.getImageWithStatus(img, repositoryStatus);

                    final ERepositoryStatus informationStatus = object.getInformationStatus();
                    if (informationStatus != null) {
                        img = OverlayImageProvider.getImageWithStatus(img, informationStatus);
                    }
                }
            }
        }
        return img;
    }

    protected Image getElementImage(RepositoryNode node) {
        IImage icon = null;
        switch (node.getType()) {
        case STABLE_SYSTEM_FOLDER:
        case SYSTEM_FOLDER:
            icon = node.getIcon();
            if (icon == null) {
                ERepositoryObjectType contentType = node.getContentType();
                if (contentType != null) {
                    Image image = RepositoryNodeProviderRegistryReader.getInstance().getImage(contentType);
                    if (image != null) {
                        return image;
                    }
                }
                icon = getIcon(node.getContentType());
            }
            break;
        case SIMPLE_FOLDER:
            IRepositoryView view = RepositoryManagerHelper.findRepositoryView();
            if (view != null) {
                icon = (view.getExpandedState(node) ? ECoreImage.FOLDER_OPEN_ICON : ECoreImage.FOLDER_CLOSE_ICON);
            } else {
                icon = ECoreImage.FOLDER_OPEN_ICON;
            }
            break;
        default:
            IRepositoryViewObject object = node.getObject();
            if (object != null) {
                return getElementImage(object);
            }
            icon = getIcon(node.getContentType());
            break;
        }
        if (icon != null) {
            return ImageProvider.getImage(icon);
        }

        return null;
    }

    protected IImage getIcon(ERepositoryObjectType itemType) {
        if (itemType != null) {
            return RepositoryImageProvider.getIcon(itemType);
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
        if (element instanceof IRepositoryViewObject) {
            return getElementText((IRepositoryViewObject) element);
        } else if (element instanceof Property) {
            return getElementText((Property) element);
        } else if (element instanceof RepositoryNode) {
            return getElementText((RepositoryNode) element);
        }
        return super.getText(element);
    }

    protected String getElementText(IRepositoryViewObject object) {
        boolean isDeleted = object.isDeleted();
        boolean isModified = object.isModified();
        boolean isFolder = (object instanceof Folder);
        boolean inRef = !ProjectManager.getInstance().getCurrentProject().getLabel().equals(object.getProjectLabel());

        return getText(object.getLabel(), object.getVersion(), object.getPath(), object.getProjectLabel(), isDeleted, isModified,
                isFolder, inRef);
    }

    protected String getElementText(Property property) {
        boolean isDeleted = false;
        boolean isModified = false;
        boolean inRef = false;
        boolean isFolder = false;

        // nodes in the recycle bin
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.getStatus(property.getItem()) == ERepositoryStatus.DELETED) {
            isDeleted = true;
        }
        // PTODO SML [FOLDERS++] temp code
        if (ERepositoryObjectType.getItemType(property.getItem()) == ERepositoryObjectType.FOLDER) {
            isFolder = true;
        }
        //
        isModified = factory.isModified(property);
        inRef = !ProjectManager.getInstance().isInCurrentMainProject(property);

        return getText(property.getLabel(), property.getVersion(), property.getItem().getState().getPath(), null, isDeleted,
                isModified, isFolder, inRef);
    }

    protected String getElementText(RepositoryNode node) {
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT || node.getType() == ENodeType.SIMPLE_FOLDER) {
            boolean isDeleted = false;
            boolean isModified = false;
            boolean inRef = false;
            boolean isFolder = false;
            IRepositoryViewObject object = node.getObject();

            isModified = object.isModified();
            isDeleted = object.isDeleted();

            org.talend.core.model.properties.Project mainProject = ProjectManager.getInstance().getCurrentProject()
                    .getEmfProject();
            inRef = !mainProject.getLabel().equals(object.getProjectLabel());
            if (node.getType() == ENodeType.SIMPLE_FOLDER) {
                isFolder = true;
            }
            return getText(object.getLabel(), object.getVersion(), object.getPath(), object.getProjectLabel(), isDeleted,
                    isModified, isFolder, inRef);
        } else {
            return getText(node.getLabel(), null, null, null, false, false, false, false);
        }
    }

    protected final boolean isAllowChengeVersion() {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        return brandingService.getBrandingConfiguration().isAllowChengeVersion();
    }

    protected final boolean isRefMerged() {
        if (PluginChecker.isRefProjectLoaded()) {
            IReferencedProjectService service = (IReferencedProjectService) GlobalServiceRegister.getDefault().getService(
                    IReferencedProjectService.class);
            if (service != null && service.isMergeRefProject()) {
                return true;
            }
        }
        return false;
    }

    protected String getText(String label, String version, String path, String projectLabel, boolean isDeleted,
            boolean isModified, boolean isFolder, boolean inRef) {
        Assert.isNotNull(label);

        StringBuffer text = new StringBuffer(50);

        if (isModified) {
            text.append('>');
            text.append(SPACE);
        }

        text.append(label);

        if (isAllowChengeVersion() && !isFolder && version != null) {
            text.append(SPACE);
            text.append(version);
        }
        if (isRefMerged() && inRef && projectLabel != null) {
            text.append(SPACE);
            text.append('(');
            text.append('@');
            text.append(projectLabel);
            text.append(')');
        }
        if (!isFolder && isDeleted && path != null && path.length() > 0) {
            text.append(SPACE);
            text.append('(');
            text.append(path);
            text.append(')');
        }
        return text.toString();
    }
}
