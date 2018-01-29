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
package org.talend.repository.viewer.content;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.internal.navigator.ContributorTrackingSet;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.internal.navigator.extensions.NavigatorContentDescriptor;
import org.eclipse.ui.internal.navigator.extensions.NavigatorContentExtension;
import org.eclipse.ui.internal.navigator.extensions.SafeDelegateTreeContentProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.INavigatorContentDescriptor;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.IPipelinedTreeContentProvider;
import org.eclipse.ui.navigator.PipelinedShapeModification;
import org.eclipse.ui.navigator.PipelinedViewerUpdate;

/**
 * this class has been create because of eclipse bug : https://bugs.eclipse.org/bugs/show_bug.cgi?id=82973 this can be
 * used by a dummy content provider definition based on a dummy property tester, in order to start the pluging and
 * register all the necessary adapters.
 * 
 * here is the content provider defintion:
 * 
 * @formatter:off <navigatorContent activeByDefault="true"
 * contentProvider="org.talend.repository.view.di.viewer.content.DummyContentProvider"
 * id="org.talend.repository.view.content.dummy.for.activation.di" name="name"> <enablement> <and> <instanceof
 * value="org.talend.repository.navigator.TalendRepositoryRoot"> </instanceof> <test forcePluginActivation="true" <!--
 * this is what make the plugin to get started and there for activator called if adapter need to be registered -->
 * property="org.talend.repository.node.alwaysFalse"> </test> </and> </enablement> </navigatorContent>
 * 
 * and here is the property tester definition, the class must be in the plugin that needs to be started and manifest
 * needs this line (Bundle-ActivationPolicy: lazy) <propertyTester class="org.talend.repository.tester.NodeTester"
 * id="org.talend.repository.view.di.activation.tester" namespace="org.talend.repository.node" properties="alwaysFalse"
 * type="org.talend.repository.navigator.TalendRepositoryRoot"> </propertyTester>
 * @formatter:on
 * 
 * 
 * 
 * */
public class EmptyContentProvider implements IPipelinedTreeContentProvider {

    private Viewer viewer;

    @Override
    public void dispose() {
        this.viewer = null;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = viewer;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        return new Object[0];

    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.ICommonContentProvider#init(org.eclipse.ui.navigator.ICommonContentExtensionSite)
     */
    @Override
    public void init(ICommonContentExtensionSite aConfig) {
        // left empty on purpose

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IMementoAware#restoreState(org.eclipse.ui.IMemento)
     */
    @Override
    public void restoreState(IMemento aMemento) {
        // left empty on purpose

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IMementoAware#saveState(org.eclipse.ui.IMemento)
     */
    @Override
    public void saveState(IMemento aMemento) {
        // left empty on purpose

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IPipelinedTreeContentProvider#getPipelinedChildren(java.lang.Object, java.util.Set)
     */
    @Override
    public void getPipelinedChildren(Object aParent, Set theCurrentChildren) {
        removeResourceVisitorForOverride(theCurrentChildren);
        theCurrentChildren.clear();
    }

    @SuppressWarnings({ "restriction", "rawtypes" })
    private void removeResourceVisitorForOverride(Set theCurrentChildren) {
        if (this.viewer instanceof CommonViewer && theCurrentChildren instanceof ContributorTrackingSet) {
            INavigatorContentService navigatorContentService = ((CommonViewer) this.viewer).getNavigatorContentService();
            if (navigatorContentService instanceof NavigatorContentService) {
                ContributorTrackingSet contributorTrackingSet = (ContributorTrackingSet) theCurrentChildren;

                // have override
                INavigatorContentDescriptor firstClassContributor = contributorTrackingSet.getFirstClassContributor();
                if (!firstClassContributor.hasOverridingExtensions()) {
                    return;
                }

                // check override
                boolean isOverride = false;
                INavigatorContentDescriptor descriptor = contributorTrackingSet.getContributor();
                NavigatorContentDescriptor overridingDescriptor;
                for (Iterator contentDescriptorsItr = firstClassContributor.getOverriddingExtensions().iterator(); contentDescriptorsItr
                        .hasNext();) {
                    overridingDescriptor = (NavigatorContentDescriptor) contentDescriptorsItr.next();
                    if (overridingDescriptor.getId().equals(descriptor.getId())) {
                        isOverride = true;
                    }
                }
                if (!isOverride) {
                    return;
                }

                // remove ResourceVisitor
                NavigatorContentExtension firstClassExtension = ((NavigatorContentService) navigatorContentService)
                        .getExtension(firstClassContributor);
                if (firstClassExtension != null) {
                    SafeDelegateTreeContentProvider internalGetContentProvider = firstClassExtension.internalGetContentProvider();
                    if (internalGetContentProvider != null) {
                        ITreeContentProvider delegateContentProvider = internalGetContentProvider.getDelegateContentProvider();
                        if (delegateContentProvider instanceof FolderListenerSingleTopContentProvider) {
                            // try to remove the ResourceVisitor of FolderListenerSingleTopContentProvider
                            delegateContentProvider.dispose();
                        }
                    }
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IPipelinedTreeContentProvider#getPipelinedElements(java.lang.Object, java.util.Set)
     */
    @Override
    public void getPipelinedElements(Object anInput, Set theCurrentElements) {
        removeResourceVisitorForOverride(theCurrentElements);
        theCurrentElements.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IPipelinedTreeContentProvider#getPipelinedParent(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public Object getPipelinedParent(Object anObject, Object aSuggestedParent) {
        return aSuggestedParent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptAdd(org.eclipse.ui.navigator.
     * PipelinedShapeModification)
     */
    @Override
    public PipelinedShapeModification interceptAdd(PipelinedShapeModification anAddModification) {
        return anAddModification;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptRemove(org.eclipse.ui.navigator.
     * PipelinedShapeModification)
     */
    @Override
    public PipelinedShapeModification interceptRemove(PipelinedShapeModification aRemoveModification) {
        return aRemoveModification;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptRefresh(org.eclipse.ui.navigator.
     * PipelinedViewerUpdate)
     */
    @Override
    public boolean interceptRefresh(PipelinedViewerUpdate aRefreshSynchronization) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.navigator.IPipelinedTreeContentProvider#interceptUpdate(org.eclipse.ui.navigator.PipelinedViewerUpdate
     * )
     */
    @Override
    public boolean interceptUpdate(PipelinedViewerUpdate anUpdateSynchronization) {
        return false;
    }

}
