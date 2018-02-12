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
package org.talend.designer.maven.ui.setting.repository;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.maven.ui.setting.repository.node.RepositoryPreferenceNode;
import org.talend.designer.maven.ui.setting.repository.tester.IRepositorySettingTester;
import org.talend.designer.maven.ui.utils.DesignerMavenUiHelper;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class RepositoryMavenSetting {

    private String name, description;

    private int order;

    private IRepositorySettingTester tester;

    public RepositoryMavenSetting() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public IRepositorySettingTester getTester() {
        return tester;
    }

    public void setTester(IRepositorySettingTester tester) {
        this.tester = tester;
    }

    /**
     * 
     * If existed same id of node, will use it directly. Else will create one folder node.
     */
    public IPreferenceNode createAndFindMavenFolderNode(final IPreferenceNode parentNode, final ILabelProvider labelProvider,
            final RepositoryNode node) {
        if (parentNode == null || labelProvider == null || node == null) {
            return null;
        }
        ERepositoryObjectType contentType = node.getContentType();
        // mabye it's not good for some sub nodes, for example, if for routines, supported type must containd
        // CODE.
        List<ERepositoryObjectType> supportedTypes = Arrays.asList(RepositoryMavenSettingManager.REGISTRY.getSupportTypes());
        if (!supportedTypes.contains(contentType)) {
            return null;
        }
        String id = buildFolderNodeId(parentNode, labelProvider, node);

        IPreferenceNode foundNode = parentNode.findSubNode(id);
        if (foundNode == null || foundNode != null && !(foundNode instanceof RepositoryPreferenceNode)) { // not found,
            IPreferenceNode mavenFolderNode = createFolderNode(id, labelProvider, node);
            if (mavenFolderNode != null) {
                // must be front, when add other nodes. make sure the build id is right.
                parentNode.add(mavenFolderNode);
                return mavenFolderNode;
            }
        } else { // found, just use existed one.
            return foundNode;
        }

        return null;
    }

    /**
     * Unify the folder id to create.
     */
    protected String buildFolderNodeId(final IPreferenceNode parentNode, final ILabelProvider labelProvider,
            final RepositoryNode node) {
        ERepositoryObjectType contentType = node.getContentType();
        String id = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(parentNode.getId(), contentType.getType());
        if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            String label = labelProvider.getText(node);
            String parentId = parentNode.getId();
            if (parentId != null && parentId.length() > 0) {
                id = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(parentId, label);
            } else {
                id = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(contentType.getType(), label);
            }
        }
        return id;
    }

    /**
     * 
     * Create folder node.
     */
    protected IPreferenceNode createFolderNode(final String id, final ILabelProvider labelProvider, final RepositoryNode node) {
        ERepositoryObjectType contentType = node.getContentType();
        ImageDescriptor imageDesc = null;
        Image image = labelProvider.getImage(node);
        if (image != null) {
            imageDesc = ImageDescriptor.createFromImageData(image.getImageData());
        }
        String label = labelProvider.getText(node);
        // label= node.getLabel(); //there is on bug for this label, so use provider directly.

        RepositoryPreferenceNode mavenFolderNode = null;
        if (!contentType.isResouce() || node.getType() == ENodeType.SYSTEM_FOLDER || node.getType() == ENodeType.SIMPLE_FOLDER) {
            mavenFolderNode = new RepositoryPreferenceNode(id, label, imageDesc, node);

        }
        return mavenFolderNode;
    }

    /**
     * 
     * Check the node to enable add the maven scripts nodes or not.
     */
    public boolean needMavenScripts(RepositoryNode node) {
        if (node != null) {
            ERepositoryObjectType contentType = node.getContentType();
            // must be resource item, for root node and simple folder nodes
            if (contentType != null && contentType.isResourceItem()
                    && (node.getType() == ENodeType.SYSTEM_FOLDER || node.getType() == ENodeType.SIMPLE_FOLDER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * Create the maven scripts children nodes
     */
    public abstract void createMavenScriptsChildren(final IPreferenceNode parentNode, final RepositoryNode node);
}
