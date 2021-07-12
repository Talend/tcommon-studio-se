// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.viewer.sorter;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreePathViewerSorter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.internal.navigator.CommonNavigatorMessages;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.internal.navigator.NavigatorContentServiceContentProvider;
import org.eclipse.ui.internal.navigator.NavigatorPlugin;
import org.eclipse.ui.internal.navigator.Policy;
import org.eclipse.ui.navigator.INavigatorContentDescriptor;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.INavigatorSorterService;
import org.eclipse.ui.navigator.Priority;

/**
 * Copied from org.eclipse.ui.navigator.CommonViewerSorter to override the private getLabel()
 * 
 * FIXME Need to review after every eclipse upgrade
 */

public class RepoCommonViewerSorter extends TreePathViewerSorter {

    private NavigatorContentService contentService;

    private INavigatorSorterService sorterService;

    /**
     * Create a sorter service attached to the given content service.
     *
     * @param aContentService The content service used by the viewer that will use this sorter service.
     * @since 3.3
     */
    public void setContentService(INavigatorContentService aContentService) {
        contentService = (NavigatorContentService) aContentService;
        sorterService = contentService.getSorterService();
    }

    @Override
    public int category(Object element) {
        if (contentService == null)
            return 0;

        INavigatorContentDescriptor source = getSource(element);
        return source != null ? source.getSequenceNumber() : Priority.NORMAL_PRIORITY_VALUE;
    }

    private void logMissingExtension(Object parent, Object object) {
        NavigatorPlugin.logError(0, NLS.bind(CommonNavigatorMessages.CommonViewerSorter_NoContentExtensionForObject,
                object != null ? object.toString() : "<null>", parent != null ? parent.toString() : "<null>"), null); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public int compare(Viewer viewer, TreePath parentPath, Object e1, Object e2) {
        if (contentService == null)
            return -1;
        INavigatorContentDescriptor sourceOfLvalue = getSource(e1);
        INavigatorContentDescriptor sourceOfRvalue = getSource(e2);

        Object parent;
        if (parentPath == null) {
            parent = viewer.getInput();
        } else {
            parent = parentPath.getLastSegment();
        }

        if (sourceOfLvalue == null) {
            logMissingExtension(parent, e1);
            return -1;
        }
        if (sourceOfRvalue == null) {
            logMissingExtension(parent, e2);
            return -1;
        }

        ViewerSorter sorter = null;

        // shortcut if contributed by same source
        if (sourceOfLvalue == sourceOfRvalue) {
            sorter = sorterService.findSorter(sourceOfLvalue, parent, e1, e2);
        } else {
            // findSorter returns the sorter specified at the source or if it has a higher priority a sortOnly sorter
            // that is registered for the parent
            ViewerSorter lSorter = findApplicableSorter(sourceOfLvalue, parent, e1, e2);
            ViewerSorter rSorter = findApplicableSorter(sourceOfRvalue, parent, e1, e2);
            sorter = rSorter;

            if (rSorter == null || (lSorter != null && sourceOfLvalue.getSequenceNumber() < sourceOfRvalue.getSequenceNumber())) {
                sorter = lSorter;
            }
        }

        if (sorter != null) {
            return sorter.compare(viewer, e1, e2);
        }

        int categoryDelta = category(e1) - category(e2);
        if (categoryDelta == 0) {
            // return super.compare(viewer, e1, e2);
            return doCompare(viewer, e1, e2);
        }
        return categoryDelta;
    }

    // customized compare
    public int doCompare(Viewer viewer, Object e1, Object e2) {
        int cat1 = category(e1);
        int cat2 = category(e2);

        if (cat1 != cat2) {
            return cat1 - cat2;
        }

        String name1 = getLabel(viewer, e1);
        String name2 = getLabel(viewer, e2);

        // use the comparator to compare the strings
        return getComparator().compare(name1, name2);
    }

    private String getLabel(Viewer viewer, Object e1) {
        String name1;
        if (viewer == null || !(viewer instanceof ContentViewer)) {
            name1 = e1.toString();
        } else {
            IBaseLabelProvider prov = ((ContentViewer) viewer).getLabelProvider();
            if (prov instanceof ILabelProvider) {
                ILabelProvider lprov = (ILabelProvider) prov;
                if (lprov instanceof DecoratingLabelProvider) {
                    DecoratingLabelProvider dprov = (DecoratingLabelProvider) lprov;
                    lprov = dprov.getLabelProvider();
                }
                name1 = lprov.getText(e1);
            } else {
                name1 = e1.toString();
            }
        }
        if (name1 == null) {
            name1 = "";//$NON-NLS-1$
        }
        return StringUtils.strip(name1, "> "); //$NON-NLS-1$
    }

    private ViewerSorter findApplicableSorter(INavigatorContentDescriptor descriptor, Object parent, Object e1, Object e2) {
        ViewerSorter sorter = sorterService.findSorter(descriptor, parent, e1, e2);
        if (!descriptor.isSortOnly()) { // for compatibility
            if (!(descriptor.isTriggerPoint(e1) && descriptor.isTriggerPoint(e2))) {
                return null;
            }
        }
        return sorter;
    }

    @Override
    public boolean isSorterProperty(Object element, String property) {
        // Have to get the parent path from the content provider
        NavigatorContentServiceContentProvider cp = (NavigatorContentServiceContentProvider) contentService
                .createCommonContentProvider();
        TreePath[] parentPaths = cp.getParents(element);
        for (TreePath parentPath : parentPaths) {
            if (isSorterProperty(parentPath, element, property))
                return true;
        }
        return false;
    }

    @Override
    public boolean isSorterProperty(TreePath parentPath, Object element, String property) {
        INavigatorContentDescriptor contentDesc = getSource(element);
        if (parentPath.getSegmentCount() == 0)
            return false;
        ViewerSorter sorter = sorterService.findSorter(contentDesc, parentPath.getLastSegment(), element, null);
        if (sorter != null)
            return sorter.isSorterProperty(element, property);
        return false;
    }

    private INavigatorContentDescriptor getSource(Object o) {
        // Fast path - just an optimization for the common case
        INavigatorContentDescriptor ncd = contentService.getSourceOfContribution(o);
        if (ncd != null) {
            if (Policy.DEBUG_SORT)
                System.out.println("sort: " + ncd + " object: " + o); //$NON-NLS-1$//$NON-NLS-2$
            return ncd;
        }

        Set descriptors = contentService.findDescriptorsByTriggerPoint(o, NavigatorContentService.CONSIDER_OVERRIDES);
        if (descriptors != null && descriptors.size() > 0) {
            ncd = (INavigatorContentDescriptor) descriptors.iterator().next();
            if (Policy.DEBUG_SORT)
                System.out.println("sort: " + ncd + " object: " + o); //$NON-NLS-1$//$NON-NLS-2$
            return ncd;
        }
        if (Policy.DEBUG_SORT)
            System.out.println("sort: NULL object: " + o); //$NON-NLS-1$
        return null;
    }

}
