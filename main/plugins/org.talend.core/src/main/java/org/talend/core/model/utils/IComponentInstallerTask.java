// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author bhe created on Jul 1, 2021
 *
 */
public interface IComponentInstallerTask {

    int COMPONENT_TYPE_TCOMPV0 = 1;

    int COMPONENT_TYPE_TCOMPV1 = 2;

    /**
     * Order of the task, smaller means higher priority
     * 
     * @return Order of the task
     */
    int getOrder();

    /**
     * Set order of the task
     * 
     * @param order
     */
    void setOrder(int order);

    /**
     * Get all component gavs
     * 
     * @return Set<ComponentGAV>
     */
    Set<ComponentGAV> getComponentGAV();

    /**
     * @param componentType 1 - tcompv0, 2 - tcompv1
     * @return Set<ComponentGAV>
     */
    Set<ComponentGAV> getComponentGAV(int componentType);

    /**
     * Add component gav
     * 
     * @param gav
     */
    void addComponentGAV(ComponentGAV gav);

    /**
     * Whether it is necessary to install the component
     * 
     * @return
     */
    boolean needInstall();

    /**
     * Install the component
     * 
     * @param monitor
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    boolean install(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

}
