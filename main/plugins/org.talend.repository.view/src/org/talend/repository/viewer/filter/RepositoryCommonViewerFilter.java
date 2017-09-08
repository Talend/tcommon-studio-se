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
package org.talend.repository.viewer.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.internal.ui.util.StringMatcher;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.impl.JobletDocumentationItemImpl;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.repository.RepositoryViewPlugin;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * filter by status, users and label for repository element and folder
 * 
 * FIXME, later, it's better to split for each functions.
 */
public class RepositoryCommonViewerFilter extends ViewerFilter {

    /**
     * DOC ggu RepositoryViewerFilter constructor comment.
     */
    public RepositoryCommonViewerFilter() {
    }

    protected IPreferenceStore getPreferenceStore() {
        return RepositoryViewPlugin.getDefault().getPreferenceStore();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {

        if (!getPreferenceStore().getBoolean(IRepositoryPrefConstants.USE_FILTER)) {
            return true;
        }

        boolean visible = true;
        if (element instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) element;
            if (ENodeType.REPOSITORY_ELEMENT.equals(node.getType()) || ENodeType.SIMPLE_FOLDER.equals(node.getType())) {
                visible = filterByUserStatusName(node);

            }
        }

        return visible;
    }

    private boolean filterByUserStatusName(RepositoryNode node) {
        String[] statusFilter = RepositoryNodeFilterHelper.getFiltersByPreferenceKey(IRepositoryPrefConstants.FILTER_BY_STATUS);
        String[] userFilter = RepositoryNodeFilterHelper.getFiltersByPreferenceKey(IRepositoryPrefConstants.FILTER_BY_USER);
        boolean enableNameFilter = getPreferenceStore().getBoolean(IRepositoryPrefConstants.TAG_USER_DEFINED_PATTERNS_ENABLED);
        if (statusFilter == null && userFilter == null && !enableNameFilter) {
            return true;
        }

        boolean visible = true;

        if (ENodeType.SIMPLE_FOLDER.equals(node.getType())) {
            visible = isStableItem(node);
            if (visible) {
                return true;
            }
            for (IRepositoryNode childNode : node.getChildren()) {
                visible = visible || filterByUserStatusName((RepositoryNode) childNode);
                if (visible) {
                    return true;
                }
            }
            return visible;
        }

        List items = new ArrayList();
        if (statusFilter != null && statusFilter.length > 0) {
            items.addAll(Arrays.asList(statusFilter));
        }
        if (userFilter != null && userFilter.length > 0) {
            items.addAll(Arrays.asList(userFilter));
        }
        if (node.getObject() != null) {
            Property property = node.getObject().getProperty();
            String statusCode = "";
            if (property != null) {
                statusCode = property.getStatusCode();
            }
            User author = node.getObject().getAuthor();
            String user = "";
            if (author != null) {
                user = author.getLogin();
            }
            if ((items.contains(statusCode) || items.contains(user)) && !isStableItem(node)) {
                visible = false;
            } else if (items.contains(RepositoryConstants.NOT_SET_STATUS) && (statusCode == null || "".equals(statusCode))) {
                visible = false;
                if (property != null) {
                    Item item = property.getItem();
                    if (item instanceof RoutineItem && ((RoutineItem) item).isBuiltIn() || item instanceof SQLPatternItem
                            && ((SQLPatternItem) item).isSystem() || item instanceof BusinessProcessItem
                            || item instanceof JobDocumentationItem || item instanceof JobletDocumentationItemImpl) {
                        visible = true;
                    }
                }

            }

        }

        // filter by name
        String label = (String) node.getProperties(EProperties.LABEL);
        if (visible && isMatchNameFilterPattern(label)) {
            visible = true;
        } else {
            if (enableNameFilter && !isStableItem(node)) {
                visible = false;
            }
        }

        return visible;
    }

    private boolean isMatchNameFilterPattern(String label) {
        boolean enable = getPreferenceStore().getBoolean(IRepositoryPrefConstants.TAG_USER_DEFINED_PATTERNS_ENABLED);
        if (!enable) {
            return false;
        }
        if (label != null && label.length() > 0) {
            StringMatcher[] testMatchers = getMatchers();
            if (testMatchers != null) {
                for (StringMatcher testMatcher : testMatchers) {
                    if (testMatcher.match(label)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private StringMatcher[] getMatchers() {
        String userFilterPattern = getPreferenceStore().getString(IRepositoryPrefConstants.FILTER_BY_NAME);
        String[] newPatterns = null;
        if (userFilterPattern != null && !"".equals(userFilterPattern)) {
            newPatterns = RepositoryNodeFilterHelper.convertFromString(userFilterPattern, RepositoryManager.PATTERNS_SEPARATOR);
        }
        StringMatcher[] matchers = null;
        if (newPatterns != null) {
            matchers = new StringMatcher[newPatterns.length];
            for (int i = 0; i < newPatterns.length; i++) {
                matchers[i] = new StringMatcher(newPatterns[i], true, false);
            }
        }
        return matchers;
    }

    private boolean isStableItem(RepositoryNode node) {
        Object label = node.getProperties(EProperties.LABEL);
        if (ENodeType.SIMPLE_FOLDER.equals(node.getType())
                && ERepositoryObjectType.SQLPATTERNS.equals(node.getContentType())
                && (label.equals("Generic") || label.equals("UserDefined") || label.equals("MySQL") || label.equals("Netezza")
                        || label.equals("Oracle") || label.equals("ParAccel") || label.equals("Teradata"))
                || label.equals("Hive")) {
            return true;

        } else if (ENodeType.REPOSITORY_ELEMENT.equals(node.getType()) && node.getObject() != null) {
            Item item = node.getObject().getProperty().getItem();
            if (item instanceof SQLPatternItem) {
                if (((SQLPatternItem) item).isSystem()) {
                    return true;
                }
            } else if (item instanceof RoutineItem) {
                if (((RoutineItem) item).isBuiltIn()) {
                    return true;
                }
            }
        }
        return false;

    }

}
