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
package org.talend.core.ui.perspective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PerspectiveExtensionReader;
import org.eclipse.ui.internal.PerspectiveTagger;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.e4.compatibility.ModeledPageLayout;
import org.eclipse.ui.internal.menus.MenuHelper;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.registry.UIExtensionTracker;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.core.ui.branding.IBrandingService;

/**
 * created by ggu on Nov 10, 2014 Detailled comment
 *
 */
@SuppressWarnings("restriction")
public class RestoreAllRegisteredPerspectivesProvider {

    /**
     * 
     */
    private static final String FIRST_TIME_LAUNCH_PREF = "first.time.launch"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(RestoreAllRegisteredPerspectivesProvider.class);

    @Inject
    private IWorkbench workbench;

    @Inject
    private EModelService fModelService;

    @Inject
    @Optional
    private MWindow fWindow;

    @Inject
    private EPartService fPartService;

    @Inject
    private MApplication fApp;

    @Inject
    @Preference
    // by default uses plugin id in the current instance scope (workspace)
    IEclipsePreferences wsPreferences;

    private IExtensionTracker fTracker;

    private MPerspectiveStack fPerspectiveStack;

    private ShowPerspectivesAtStartupRegistryReader regReader;

    protected IExtensionTracker getExtensionTracker(Display display) {
        if (fTracker == null) {
            fTracker = new UIExtensionTracker(display);
        }
        return fTracker;
    }

    protected MPerspectiveStack getMPerspectiveStack() {
        if (fPerspectiveStack != null) {
            return fPerspectiveStack;
        }
        MUIElement baseElement = fWindow;
        if (fWindow == null) {
            baseElement = fApp;
        }
        if (baseElement != null) {
            if (fPerspectiveStack == null) {
                List<MPerspectiveStack> perspStackList = fModelService.findElements(baseElement, null, MPerspectiveStack.class,
                        null);
                if (perspStackList.size() > 0) {// there must be only one perspectiveStack.
                    fPerspectiveStack = perspStackList.get(0);
                    return fPerspectiveStack;
                }
            }
            if (fWindow != null) {
                for (MWindowElement child : fWindow.getChildren()) {
                    if (child instanceof MPerspectiveStack) {
                        fPerspectiveStack = (MPerspectiveStack) child;
                        return fPerspectiveStack;
                    }
                }
            }
        }
        return null;
    }

    protected ShowPerspectivesAtStartupRegistryReader getStartupRegReader() {
        if (regReader == null) {
            regReader = new ShowPerspectivesAtStartupRegistryReader();
            regReader.init();
        }
        return regReader;
    }

    public void closeAllEditors() {
        List<MPart> elements = fModelService.findElements(fApp, "org.eclipse.e4.ui.compatibility.editor", MPart.class, null); //$NON-NLS-1$
        for (MPart element : elements) {
            element.setToBeRendered(false);
            element.getParent().getChildren().remove(element);
        }
    }

    /**
     * 
     * DOC ggu Comment method "restoreAlwaysVisiblePerspectives".
     * 
     * Try to create the perspectives which are providered via extension point.
     */
    public void restoreAlwaysVisiblePerspectives() {
        String[] showPerspectiveIds = getStartupRegReader().getShowPerspectiveIds();
        if (showPerspectiveIds == null || showPerspectiveIds.length == 0) {
            return;
        }
        // find the selected perspective to re-select it after all perspective creation
        MPerspectiveStack mPerspStack = getMPerspectiveStack();
        MPerspective initialSelectedPerspective = null;

        // because it's sorted, so use list, not set.
        List<String> showPersIdsList = new ArrayList<String>(Arrays.asList(showPerspectiveIds));
        if (mPerspStack != null) {
            for (MPerspective mp : mPerspStack.getChildren()) {
                String elementId = mp.getElementId();
                if (!showPersIdsList.contains(elementId)) { // not existed.
                    showPersIdsList.add(elementId);
                }
            }
            initialSelectedPerspective = mPerspStack.getSelectedElement();
        }

        // create the perspectives.
        for (String perspId : showPersIdsList.toArray(new String[0])) {
            restorePerspective(mPerspStack, perspId);
        }

        activateTheProperPerspective(mPerspStack, initialSelectedPerspective);
    }

    /**
     * DOC sgandon Comment method "activateTheProperPerspective".
     * 
     * @param mPerspStack, the stack to look for the perspecive to activate
     * @param initialSelectedPerspective, the perspective that was initialy selected
     */
    private void activateTheProperPerspective(final MPerspectiveStack mPerspStack, MPerspective initialSelectedPerspective) {
        if (mPerspStack != null) {
            MPerspective perspectiveToSelect = null;
            boolean firstTimeLaunch = wsPreferences.getBoolean(FIRST_TIME_LAUNCH_PREF, true);

            // select the previously opened perspective except if it is the first time the workspace is used
            if (mPerspStack.getChildren().contains(initialSelectedPerspective) && !firstTimeLaunch) {
                perspectiveToSelect = initialSelectedPerspective;
            } else {// look for the Talend default one
                    // get branding default perspective.
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
                    IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                            IBrandingService.class);
                    String defaultPerspectiveId = service.getBrandingConfiguration().getInitialWindowPerspectiveId();

                    IPerspectiveDescriptor pd = PlatformUI.getWorkbench().getPerspectiveRegistry()
                            .findPerspectiveWithId(defaultPerspectiveId);
                    if (pd == null) {
                        defaultPerspectiveId = IBrandingConfiguration.PERSPECTIVE_DI_ID;
                    }
                    // this is not the fastest way but this is to try the find API
                    List<MPerspective> matchPerspectives = fModelService.findElements(mPerspStack, MPerspective.class,
                            EModelService.IN_ANY_PERSPECTIVE, new ElementMatcher(defaultPerspectiveId, null, (String) null));
                    if (!matchPerspectives.isEmpty()) { // get the first
                        perspectiveToSelect = matchPerspectives.get(0);
                    }// else no perspective found matching the Talend, this should only occur when no default
                     // perspective is set.
                } else {// no talend default perspective then select the first one.
                    if (!mPerspStack.getChildren().isEmpty()) {
                        perspectiveToSelect = mPerspStack.getChildren().get(0);
                    }// else there is not perspective to no need to select one.
                }
            }
            if (perspectiveToSelect != null) {
                mPerspStack.setSelectedElement(perspectiveToSelect);
            } else {// we could not find any perspective to select, this should never happend so we log it.
                log.info("Unable to Select the default perspective"); //$NON-NLS-1$
            }
            wsPreferences.putBoolean(FIRST_TIME_LAUNCH_PREF, false);
        }// no perspective stack so nothing to select
    }

    /**
     * 
     * DOC ggu Comment method "restorePerspective".
     * 
     * try to find and create the perspective. if the existed custom perspective, will use the custom one. won't create
     * original one.
     * 
     */
    protected void restorePerspective(MPerspectiveStack mPerspStack, String id) {

        if (id != null && mPerspStack != null) {
            MPerspective mPersp = null;

            IPerspectiveRegistry perspectiveRegistry = workbench.getPerspectiveRegistry();

            // find the existed.
            for (MPerspective mp : mPerspStack.getChildren()) {
                if (mp.getElementId().equals(id)) { // existed.
                    mPersp = mp;
                    break;
                } else { // try to check custom perspective. (the element id should be different with id.)
                    IPerspectiveDescriptor persp = perspectiveRegistry.findPerspectiveWithId(mp.getElementId());
                    if (persp != null && persp instanceof PerspectiveDescriptor) {
                        PerspectiveDescriptor pd = (PerspectiveDescriptor) persp;

                        // if custom perspective, the original id and id are different.
                        if (!pd.getOriginalId().equals(pd.getId()) //
                                && pd.getOriginalId().equals(id)) { // when custom, just use it.
                            mPersp = mp;
                            break;
                        }
                    }
                }

            }

            IPerspectiveDescriptor perspDesc = perspectiveRegistry.findPerspectiveWithId(id);
            if (perspDesc == null) { // the id of perspective is not loaded
                if (mPersp != null) {// existed in stack, remove it
                    mPerspStack.getChildren().remove(mPersp);
                }
                return;
            }
            // create new
            if (mPersp == null) { // copied some form method setPerspective of class WorkbenchPage
                if (fWindow == null) {
                    return;
                }
                String perspId = perspDesc.getId();

                WorkbenchPage workbenchPage = (WorkbenchPage) workbench.getActiveWorkbenchWindow().getActivePage();

                mPersp = (MPerspective) fModelService.cloneSnippet(fApp, perspId, fWindow);

                if (mPersp == null) {

                    // couldn't find the perspective, create a new one
                    mPersp = fModelService.createModelElement(MPerspective.class);

                    // tag it with the same id
                    mPersp.setElementId(perspDesc.getId());

                    // instantiate the perspective
                    IPerspectiveFactory factory = ((PerspectiveDescriptor) perspDesc).createFactory();
                    ModeledPageLayout modelLayout = new ModeledPageLayout(fWindow, fModelService, fPartService, mPersp,
                            perspDesc, workbenchPage, true);
                    factory.createInitialLayout(modelLayout);
                    PerspectiveTagger.tagPerspective(mPersp, fModelService);
                    PerspectiveExtensionReader reader = new PerspectiveExtensionReader();
                    reader.extendLayout(getExtensionTracker(workbenchPage.getWorkbenchWindow().getShell().getDisplay()),
                            perspDesc.getId(), modelLayout);
                }

                mPersp.setLabel(perspDesc.getLabel());

                ImageDescriptor imageDescriptor = perspDesc.getImageDescriptor();
                if (imageDescriptor != null) {
                    String imageURL = MenuHelper.getImageUrl(imageDescriptor);
                    mPersp.setIconURI(imageURL);
                }

                // Hide placeholders for parts that exist in the 'global' areas
                fModelService.hideLocalPlaceholders(fWindow, mPersp);

                // add it to the stack
                mPerspStack.getChildren().add(mPersp);

            } else { // existed
                // add in last. because the perspectives list have been sorted.
                mPerspStack.getChildren().remove(mPersp);
                mPerspStack.getChildren().add(mPersp);
            }

        }
    }
}
