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

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Defines methods to use components. Implementation from extension point is given by ComponentsFactoryProvider<br/>
 * 
 * $Id: IComponentsFactory.java 46516 2010-08-09 11:55:19Z wchen $
 * 
 */
public interface IComponentsFactory {

    String COMPONENTS_LOCATION = "org.talend.designer.components.localprovider"; //$NON-NLS-1$

    String CAMEL_COMPONENTS_LOCATION = "org.talend.designer.camel.components.localprovider";

    String COMPONENTS_INNER_FOLDER = "components"; //$NON-NLS-1$

    String EXTERNAL_COMPONENTS_INNER_FOLDER = "ext"; //$NON-NLS-1$

    String COMPONENT_DEFINITION = "org.talend.core.component_definition"; //$NON-NLS-1$

    public void reset();

    public void resetCache();

    /**
     * mainly reload joblets or generic component
     */
    public void resetSpecificComponents();

    public int size();

    /**
     * Only use it if you want to modify(add/remove) the list! and don't forget to use synchronize block!!<br/>
     * Use readComponents() if you only want to iterate/read the list.
     */
    public Set<IComponent> getComponents();

    /**
     * Get a readonly components collection to avoid ConcurrentModificationException caused by multiple thread
     * access;<br/>
     * If want to modify(add/remove) the list, please use getComponents().
     */
    public Collection<IComponent> readComponents();

    public Map<String, Map<String, Set<IComponent>>> getComponentNameMap();

    public List<IComponent> getCustomComponents();

    /**
     * Added by Marvin Wang on Jan 11, 2013.
     * 
     * @return
     */
    IComponentsHandler getComponentsHandler();

    /**
     * 
     * Added by Marvin Wang on Jan 11, 2013.
     * 
     * @param componentsHandler
     */
    void setComponentsHandler(IComponentsHandler componentsHandler);

    /**
     * This one by default will avoid the new possible type for M/R.<br>
     * Deprecated, Shouldn't be used anymore.
     * 
     * @param name
     * @return
     * @deprecated
     */
    @Deprecated
    public IComponent get(String name);

    public IComponent get(String name, String paletteType);

    public IComponent getJobletComponent(String name, String paletteType);

    public List<String> getSkeletons();

    public String getFamilyTranslation(Object component, String text);

    public void loadUserComponentsFromComponentsProviderExtension();

    public void initializeComponents(IProgressMonitor monitor);

    public void initializeComponents(IProgressMonitor monitor, boolean duringLogon);

    public Map<String, File> getComponentsProvidersFolder();

    public List<ComponentProviderInfo> getComponentsProvidersInfo();
}
