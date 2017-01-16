// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

    public void resetSpecificComponents(); // mainly to reload only joblets

    public int size();

    public Set<IComponent> getComponents();

    public Set<IComponent> getStdComponents();

    public void addStdComponents(Collection<IComponent> components);

    public void removeStdComponents(Collection<IComponent> components);

    public Set<IComponent> getComponentLets();

    public boolean containsComponentlet(IComponent component);

    public void addComponentlets(Collection<IComponent> components);

    public void removeComponentlets(Collection<IComponent> components);

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

    public List<String> getSkeletons();

    public String getFamilyTranslation(Object component, String text);

    public void loadUserComponentsFromComponentsProviderExtension();

    public void initializeComponents(IProgressMonitor monitor);

    public void initializeComponents(IProgressMonitor monitor, boolean duringLogon);

    public Map<String, File> getComponentsProvidersFolder();

    public List<ComponentProviderInfo> getComponentsProvidersInfo();
}
