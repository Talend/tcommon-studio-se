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
package org.talend.core.model.components;

import java.util.HashSet;
import java.util.Set;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.service.IMRProcessService;
import org.talend.core.service.IStormProcessService;
import org.talend.designer.core.ICamelDesignerCoreService;

/**
 * This enum is used to distinguish the category of components, maybe some components belong common process like "DI",
 * another belongs to "M/R". Later if you need to category any components, just add here. Created by Marvin Wang on Jan
 * 11, 2013.
 */
public enum ComponentCategory {

    CATEGORY_4_DI("DI", ""), //$NON-NLS-1$ //$NON-NLS-2$
    CATEGORY_4_MAPREDUCE("MR", "Map/Reduce"), //$NON-NLS-1$ //$NON-NLS-2$
    CATEGORY_4_STORM("STORM", "Storm"), //$NON-NLS-1$ //$NON-NLS-2$
    CATEGORY_4_SPARK("SPARK", "Spark"), //$NON-NLS-1$ //$NON-NLS-2$
    CATEGORY_4_SPARKSTREAMING("SPARKSTREAMING", "Spark Streaming"), //$NON-NLS-1$ //$NON-NLS-2$
    CATEGORY_4_CAMEL("CAMEL", "");//$NON-NLS-1$ //$NON-NLS-2$

    private String name;// Will be stored in component file at header.

    private String desc;

    ComponentCategory(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * Sets the desc.
     *
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ComponentCategory getComponentCategoryFromItem(Item item) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
            ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    ICamelDesignerCoreService.class);
            if (camelService.isInstanceofCamelRoutes(item)) {
                return ComponentCategory.CATEGORY_4_CAMEL;
            }
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMRProcessService.class)) {
            IMRProcessService mrService = (IMRProcessService) GlobalServiceRegister.getDefault().getService(
                    IMRProcessService.class);
            if (mrService.isSpark(item)) {
                return ComponentCategory.CATEGORY_4_SPARK;
            }
            if (mrService.isMapReduceItem(item)) {
                return ComponentCategory.CATEGORY_4_MAPREDUCE;
            }
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IStormProcessService.class)) {
            IStormProcessService stormService = (IStormProcessService) GlobalServiceRegister.getDefault().getService(
                    IStormProcessService.class);
            if (stormService.isSparkStreaming(item)) {
                return ComponentCategory.CATEGORY_4_SPARKSTREAMING;
            }
            if (stormService.isStormItem(item)) {
                return ComponentCategory.CATEGORY_4_STORM;
            }
        }
        if (item instanceof ProcessItem || item instanceof JobletProcessItem) {
            return ComponentCategory.CATEGORY_4_DI;
        }
        return null;
    }

    public static ComponentCategory getComponentCategoryFromName(String name) {
        for (ComponentCategory category : ComponentCategory.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public static Set<String> getAllCategoryNames() {
        HashSet<String> categoryNames = new HashSet<>();
        ComponentCategory[] categories = values();
        for (ComponentCategory category : categories) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

}
