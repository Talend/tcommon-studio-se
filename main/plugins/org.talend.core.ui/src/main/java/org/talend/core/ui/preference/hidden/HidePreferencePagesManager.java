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
package org.talend.core.ui.preference.hidden;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class HidePreferencePagesManager {

    private static HidePreferencePagesManager instance;

    private List<HidePreferencePageProvider> providers;

    private boolean init = false;

    private HidePreferencePagesManager() {
    }

    public static HidePreferencePagesManager getInstance() {
        if (instance == null) {
            instance = new HidePreferencePagesManager();
        }
        return instance;
    }

    private void init() {
        providers = new ArrayList<HidePreferencePageProvider>();
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] configurationElements = registry
                .getConfigurationElementsFor("org.talend.core.runtime.hidePreferencePages_provider"); //$NON-NLS-1$
        for (IConfigurationElement element : configurationElements) {
            final String name = element.getAttribute("name"); //$NON-NLS-1$
            final String prefPageId = element.getAttribute("prefPageId"); //$NON-NLS-1$
            final String desc = element.getAttribute("desc"); //$NON-NLS-1$
            final String validatorClass = element.getAttribute("validator"); //$NON-NLS-1$

            if (!checkString(name)) {
                ExceptionHandler.process(new IllegalArgumentException("must have name")); //$NON-NLS-1$
                continue;
            }
            if (!checkString(prefPageId)) {
                ExceptionHandler.process(new IllegalArgumentException("Must have preference page id for extension: " + name)); //$NON-NLS-1$
                continue;
            }
            //
            HidePreferencePageProvider provider = new HidePreferencePageProvider();
            provider.setName(name);
            provider.setPrefPageId(prefPageId);
            provider.setDesc(desc);

            //
            providers.add(provider);
            try {
                if (checkString(validatorClass)) {
                    Object classinstance = element.createExecutableExtension("validator"); //$NON-NLS-1$
                    if (classinstance instanceof IHidePreferencePageValidator) {
                        IHidePreferencePageValidator validator = (IHidePreferencePageValidator) classinstance;
                        provider.setValidator(validator);
                    }
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private boolean checkString(String value) {
        return value != null && !"".equals(value.trim()); //$NON-NLS-1$
    }

    public List<HidePreferencePageProvider> getProviders() {
        if (this.providers == null || this.providers.isEmpty()) {
            init();
        }
        return this.providers;
    }

}
