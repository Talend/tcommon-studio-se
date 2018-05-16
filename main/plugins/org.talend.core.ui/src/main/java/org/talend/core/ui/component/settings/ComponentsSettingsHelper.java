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
package org.talend.core.ui.component.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ComponentSetting;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * created by nrousseau on Aug 8, 2014 Detailled comment
 *
 */
public class ComponentsSettingsHelper {

    /**
     * This gives all the components and the families where they're hidden. Components are stored like:
     * componentMap.get("tMsgBox").add("hiddenFamily"); paletteTypeMap.put("DI",componentMap);
     * 
     * Components not in this list are visible.
     */
    private static Map<String, Map<String, Set<String>>> hiddenComponents;

    private static Map<String, Set<String>> displayedFamilies;

    private static void initializeHiddenComponents() {
        RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (repositoryContext == null) {
            return;
        }
        Project project = repositoryContext.getProject();
        if (project == null) {
            return;
        }
        org.talend.core.model.properties.Project emfProject = project.getEmfProject();
        if (emfProject == null) {
            return;
        }
        hiddenComponents = new HashMap<String, Map<String, Set<String>>>();
        EList list = emfProject.getComponentsSettings();
        Collection<IComponent> componentList = ComponentsFactoryProvider.getInstance().readComponents();

        for (IComponent component : componentList) {
            if (component.isTechnical() || component.getComponentType() == EComponentType.JOBLET) {
                continue;
            }
            if (!hiddenComponents.containsKey(component.getPaletteType())) {
                hiddenComponents.put(component.getPaletteType(), new HashMap<String, Set<String>>());
            }
            if (!component.isVisibleInComponentDefinition()) {
                hiddenComponents.get(component.getPaletteType()).put(component.getName(), new HashSet<String>());
                for (String family : component.getOriginalFamilyName().split(ComponentsFactoryProvider.FAMILY_SEPARATOR_REGEX)) {
                    hiddenComponents.get(component.getPaletteType()).get(component.getName()).add(family);
                }
            }
        }

        for (Object oSetting : list) {
            ComponentSetting setting = (ComponentSetting) oSetting;
            String paletteType;
            String componentName;
            if (setting.getName().contains("|")) {
                String[] splitted = setting.getName().split(ComponentsFactoryProvider.FAMILY_SEPARATOR_REGEX);
                paletteType = splitted[0];
                componentName = splitted[1];
            } else {
                // only for old version of the component settings
                // to simplify since the hidden from palette got problem before, we just consider DI only
                paletteType = ComponentCategory.CATEGORY_4_DI.getName();
                componentName = setting.getName();
            }
            if (!hiddenComponents.containsKey(paletteType)) {
                hiddenComponents.put(paletteType, new HashMap<String, Set<String>>());
            }
            if (setting.isHidden()) {
                if (!hiddenComponents.get(paletteType).containsKey(componentName)) {
                    hiddenComponents.get(paletteType).put(componentName, new HashSet<String>());
                }
                for (String family : setting.getFamily().split(ComponentsFactoryProvider.FAMILY_SEPARATOR_REGEX)) {
                    hiddenComponents.get(paletteType).get(componentName).add(family);
                }
            } else {
                if (hiddenComponents.get(paletteType).containsKey(componentName)) {
                    for (String family : setting.getFamily().split(ComponentsFactoryProvider.FAMILY_SEPARATOR_REGEX)) {
                        hiddenComponents.get(paletteType).get(componentName).remove(family);
                    }
                    if (hiddenComponents.get(paletteType).get(componentName).isEmpty()) {
                        hiddenComponents.get(paletteType).remove(componentName);
                    }
                }
            }
        }

        displayedFamilies = new HashMap<String, Set<String>>();
        for (IComponent component : componentList) {
            if (component.isTechnical() || component.getComponentType() == EComponentType.JOBLET) {
                continue;
            }
            if (!displayedFamilies.containsKey(component.getPaletteType())) {
                displayedFamilies.put(component.getPaletteType(), new HashSet<String>());
            }
            for (String family : component.getOriginalFamilyName().split(ComponentsFactoryProvider.FAMILY_SEPARATOR_REGEX)) {
                if (isComponentVisible(component, null)) {
                    displayedFamilies.get(component.getPaletteType()).add(family);
                }
            }
        }
    }

    /**
     * Migrate component settings for projects before 5.6.0.<br>
     * <br>
     * Goal is to have only in this list hidden components set manually, and hidden components by default in the
     * product, set as visible by the user.<br>
     * <br>
     * <b>ComponentSettings will be stored with:</b><br>
     * Name = PaletteType | component name<br>
     * Family = list of families hidden, separated by |.<br>
     */
    public static void migrateComponentSettings() {
    }

    public static Set<String> getDisplayedFamilies(String paletteType) {
        if (hiddenComponents == null) {
            initializeHiddenComponents();
        }
        return displayedFamilies.get(paletteType);
    }

    /**
     * Check if the component is visible in the current family. Note that a component can be visible in one family but
     * not in another one.
     * 
     * @param component
     * @param family
     * @return
     */
    public static boolean isComponentVisible(IComponent component, String family) {
        if (hiddenComponents == null) {
            initializeHiddenComponents();
        }

        if (hiddenComponents.containsKey(component.getPaletteType())) {
            if (hiddenComponents.get(component.getPaletteType()).containsKey(component.getName())) {
                if (hiddenComponents.get(component.getPaletteType()).get(component.getName()).contains(family)) {
                    // not visible if in the hidden component list;
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the component is visible in the current family. Note that a component can be visible in one family but
     * not in another one.
     * 
     * @param component
     * @param family
     * @return
     */
    public static boolean isComponentVisible(String paletteType, String componentName, String family) {
        if (hiddenComponents == null) {
            initializeHiddenComponents();
        }

        if (hiddenComponents.containsKey(paletteType)) {
            if (hiddenComponents.get(paletteType).containsKey(componentName)) {
                if (hiddenComponents.get(paletteType).get(componentName).contains(family)) {
                    // not visible if in the hidden component list;
                    return false;
                }
            }
        }
        return true;
    }

    public static void resetHiddenComponents() {
        hiddenComponents = null;
        displayedFamilies = null;
    }

    public static void resetToDefaultHiddenComponents() {
        RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (repositoryContext == null) {
            return;
        }
        Project project = repositoryContext.getProject();
        if (project == null) {
            return;
        }
        org.talend.core.model.properties.Project emfProject = project.getEmfProject();
        if (emfProject == null) {
            return;
        }
        hiddenComponents = new HashMap<String, Map<String, Set<String>>>();
        EList list = emfProject.getComponentsSettings();
        if (!list.isEmpty()) {
            list.clear();
            IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IRepositoryService.class);

            IProxyRepositoryFactory prf = service.getProxyRepositoryFactory();

            try {
                prf.saveProject(project);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        hiddenComponents = null;
        displayedFamilies = null;
    }

}
