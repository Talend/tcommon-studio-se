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
package org.talend.presentation.onboarding.handlers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.interfaces.IOnBoardingJsonI18n;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingRegistedResource;
import org.talend.presentation.onboarding.utils.OnBoardingUtils;

/**
 * created by cmeng on Sep 21, 2015 Detailled comment
 *
 */
public class OnBoardingExtentionHandler {

    public static final String RESOURCE_EXTENTION_POINT_ID = "org.talend.presentation.onboarding.resource"; //$NON-NLS-1$

    public static final String RESOURCE_EXTENTION_ATTR_FILE_PATH = "filePath"; //$NON-NLS-1$

    public static final String RESOURCE_EXTENTION_ATTR_I18N_CLASS = "i18nClass"; //$NON-NLS-1$

    public static boolean hasOnBoardingResourceRegisted() {
        IConfigurationElement[] resourceElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
                RESOURCE_EXTENTION_POINT_ID);
        if (resourceElements == null || resourceElements.length <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public static OnBoardingRegistedResource getOnBoardingResourceLastRegisted() {
        List<OnBoardingRegistedResource> result = getOnBoardingResourcesRegisted();
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(result.size() - 1);
    }

    public static List<OnBoardingRegistedResource> getOnBoardingResourcesRegisted() {
        List<OnBoardingRegistedResource> result = new ArrayList<OnBoardingRegistedResource>();

        IConfigurationElement[] resourceElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
                RESOURCE_EXTENTION_POINT_ID);

        if (resourceElements == null || resourceElements.length <= 0) {
            return result;
        }

        for (IConfigurationElement element : resourceElements) {
            try {
                OnBoardingRegistedResource resource = new OnBoardingRegistedResource();
                String urlPath = element.getAttribute(RESOURCE_EXTENTION_ATTR_FILE_PATH);
                Bundle bundle = Platform.getBundle(element.getContributor().getName());
                URL url = OnBoardingUtils.getResourceLocalURL(bundle, urlPath);
                if (url == null) {
                    continue;
                }
                resource.setUrl(url);
                result.add(resource);

                String i18nClass = element.getAttribute(RESOURCE_EXTENTION_ATTR_I18N_CLASS);
                if (i18nClass != null && !i18nClass.isEmpty()) {
                    Object i18nObj = element.createExecutableExtension(RESOURCE_EXTENTION_ATTR_I18N_CLASS);
                    if (i18nObj instanceof IOnBoardingJsonI18n) {
                        resource.setI18n((IOnBoardingJsonI18n) i18nObj);
                    }
                }
            } catch (Exception e) {
                OnBoardingExceptionHandler.process(e);
            }
        }

        return result;
    }
}
