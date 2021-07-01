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

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author bhe created on Jul 1, 2021
 *
 */
public interface IComponentInstallerTask {

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
	 * Get component's group ID
	 * 
	 * @return group ID of component
	 */
	String getComponentGroupId();

	/**
	 * Set component's group ID
	 * 
	 * @param groupId
	 */
	void setComponenGroupId(String groupId);

	/**
	 * Get component's artifact ID
	 * 
	 * @return artifact ID of component
	 */
	String getComponenArtifactId();

	/**
	 * Set component's artifact ID
	 * 
	 * @param artifactId
	 */
	void setComponenArtifactId(String artifactId);

	/**
	 * Get component's version
	 * 
	 * @return component's version
	 */
	String getComponenVersion();

	/**
	 * Set component's version
	 * 
	 * @param version
	 */
	void setComponenVersion(String version);

	/**
	 * Get component's classifier
	 * 
	 * @return component's classifier
	 */
	String getComponentClassifier();

	/**
	 * Set component's classifier
	 * 
	 * @param classifier
	 */
	void setComponentClassifier(String classifier);

	/**
	 * Get component's type
	 * 
	 * @return component's type
	 */
	String getComponentType();

	/**
	 * Set component's type
	 * 
	 * @param type
	 */
	void setComponentType(String type);

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
