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
package org.talend.presentation.onboarding.ui.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.handlers.OnBoardingExtentionHandler;
import org.talend.presentation.onboarding.i18n.Messages;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingRegistedResource;

/**
 * created by cmeng on Sep 15, 2015 Detailled comment
 *
 */
public class OnBoardingResourceManager {

    public static final String DEFAULT_DOC_ID = "org.talend.presentation.onboarding.docid.default"; //$NON-NLS-1$

    private Map<String, OnBoardingRegistedResource> registedResourceMap = new HashMap<String, OnBoardingRegistedResource>();

    private static OnBoardingResourceManager defaultOnBoardingResourceManager;

    public static OnBoardingResourceManager getDefaultResourceManager() {
        if (defaultOnBoardingResourceManager == null) {
            List<OnBoardingRegistedResource> resources = OnBoardingExtentionHandler.getOnBoardingResourcesRegisted();
            if (resources == null || resources.isEmpty()) {
                defaultOnBoardingResourceManager = null;
            } else {
                defaultOnBoardingResourceManager = new OnBoardingResourceManager();
                for (OnBoardingRegistedResource resource : resources) {
                    defaultOnBoardingResourceManager.addOnBoardingRegistedResource(resource);
                }
            }
        }
        return defaultOnBoardingResourceManager;
    }

    public void addOnBoardingRegistedResource(OnBoardingRegistedResource resource) {
        if (resource == null) {
            return;
        }
        String docId = resource.getJsonDoc().getId();
        if (registedResourceMap.get(docId) != null) {
            OnBoardingExceptionHandler.warn(Messages.getString(
                    "OnBoardingResourceManager.addOnBoardingRegistedResource.docIdExists", docId)); //$NON-NLS-1$
        }
        registedResourceMap.put(docId, resource);
    }

    public OnBoardingRegistedResource getOnBoardingRegistedResource(String docId) {
        if (docId == null) {
            return null;
        }
        return registedResourceMap.get(docId);
    }

    public Collection<OnBoardingRegistedResource> getAllRegistedResources() {
        return registedResourceMap.values();
    }
}
