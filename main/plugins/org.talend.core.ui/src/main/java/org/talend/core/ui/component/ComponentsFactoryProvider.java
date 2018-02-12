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
package org.talend.core.ui.component;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.talend.commons.exception.IllegalPluginConfigurationException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponentFileNaming;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.ui.component.settings.ComponentsSettingsHelper;
import org.talend.designer.core.IPaletteFilter;

/**
 * Provides, using extension points, implementation of many factories.
 * 
 * <ul>
 * <li>IProcessFactory</li>
 * </ul>
 * 
 * $Id: ComponentsFactoryProvider.java 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 */
public class ComponentsFactoryProvider {

    public static final String FAMILY_SEPARATOR_REGEX = ComponentUtilities.FAMILY_SEPARATOR_REGEX;

    private static IComponentsFactory factorySingleton = null;

    private static IComponentFileNaming componentFileNaming = null;

    public static synchronized IComponentsFactory getInstance() {
        if (factorySingleton == null) {
            try {
                // processSingleton =
                // ExtensionImplementationProviders.getSingleInstance(ExtensionPointFactory.COMPONENTS_PROVIDER);
                IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                        IComponentsService.class);
                factorySingleton = service.getComponentsFactory();
            } catch (IllegalPluginConfigurationException e) {
                ExceptionHandler.process(e);
            }
        }
        return factorySingleton;
    }

    public static IComponentFileNaming getFileNamingInstance() {
        if (componentFileNaming == null) {
            try {
                IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                        IComponentsService.class);
                componentFileNaming = service.getComponentFileNaming();
            } catch (IllegalPluginConfigurationException e) {
                ExceptionHandler.process(e);
            }
        }
        return componentFileNaming;
    }

    /**
     * Get the family string of specific palette entry, need to remove first '/'.
     * 
     * yzhang Comment method "getPaletteEntryFamily".
     * 
     * @param entry
     * @return
     */
    public static String getPaletteEntryFamily(PaletteEntry entry) {
        String family = ""; //$NON-NLS-1$
        if (!(entry instanceof PaletteRoot)) {
            if (entry instanceof IPaletteFilter) {
                family = ((IPaletteFilter) entry).getOriginalName();
            }
            // else {
            // family = entry.getLabel();
            //                family = getPaletteEntryFamily(entry.getParent()) + "/" + family; //$NON-NLS-1$
            // }
        }
        return family;
    }

    public static void restoreComponentVisibilityStatus() {
        ComponentsSettingsHelper.resetToDefaultHiddenComponents();
    }

}
