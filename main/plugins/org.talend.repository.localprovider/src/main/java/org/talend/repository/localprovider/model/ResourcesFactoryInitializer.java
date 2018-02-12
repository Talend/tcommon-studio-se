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
package org.talend.repository.localprovider.model;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * 
 * class ResourcesFactoryInitializer. Initialize *ResourcesFactory instances required to load Talend items.
 */
public class ResourcesFactoryInitializer {

    /**
     * 
     * Method "initResourceFactories". <br/>
     * Initialize *ResourcesFactory instances required to load Talend *.properties files.
     */
    public static void initResourceFactories() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("properties", new PropertiesProjectResourcesFactory());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("project", new PropertiesProjectResourcesFactory());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("item", new XMIResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("screenshot", new XMIResourceFactoryImpl());
    }

}
