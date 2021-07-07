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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.SafeRunner;
import org.osgi.framework.FrameworkUtil;
import org.talend.core.model.utils.IComponentInstallerTask.ComponentType;
import org.talend.core.utils.RegistryReader;

/**
 * @author bhe created on Jul 1, 2021
 *
 */
public class ComponentInstallerTaskRegistryReader extends RegistryReader {

	private static final String COMPONENT_INSTALLER_TASK_EXTENSION_POINT = "component_installer_task";
	private static final String TASK_ELEMENT_NAME = "componentInstallerTask";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String ORDER_ATTRIBUTE = "order";
	private static final String GROUP_ATTRIBUTE = "g";
	private static final String ARTIFACT_ATTRIBUTE = "a";
	private static final String VERSION_ATTRIBUTE = "v";
	private static final String CLASSIFIER_ATTRIBUTE = "c";
	private static final String PACKAGE_TYPE_ATTRIBUTE = "t";
	private static final String TYPE_ATTRIBUTE = "ct";

	private List<IComponentInstallerTask> ret = new ArrayList<IComponentInstallerTask>();

	private static final ComponentInstallerTaskRegistryReader INSTANCE = new ComponentInstallerTaskRegistryReader();

	private ComponentInstallerTaskRegistryReader() {
		super(FrameworkUtil.getBundle(ComponentInstallerTaskRegistryReader.class).getSymbolicName(),
				COMPONENT_INSTALLER_TASK_EXTENSION_POINT);
	}

	/**
	 * Get instance of ComponentInstallerTaskRegistryReader
	 * 
	 * @return instance of ComponentInstallerTaskRegistryReader
	 */
	public static ComponentInstallerTaskRegistryReader getInstance() {
		return INSTANCE;
	}

	/**
	 * Get all of component installer tasks from plugins
	 * 
	 * @return List<IComponentInstallerTask>
	 */
	public List<IComponentInstallerTask> getTasks() {
		if (ret.isEmpty()) {
			readRegistry();
			Collections.sort(ret, new TaskComparator());
		}
		return ret;
	}
	
	
    /**
     * Get component installer tasks from plugins
     * @param t ComponentType
     * @return List<IComponentInstallerTask>
     */
    public List<IComponentInstallerTask> getTasks(ComponentType t) {
        List<IComponentInstallerTask> allTasks = getTasks();
        allTasks =  allTasks.stream().filter(task -> task.getComponentType() == t).collect(Collectors.toList());
        Collections.sort(allTasks, new TaskComparator());
        return allTasks;
    }

	@Override
	protected boolean readElement(IConfigurationElement element) {
		if (TASK_ELEMENT_NAME.equals(element.getName())) {
			SafeRunner.run(new RegistryReader.RegistrySafeRunnable() {

				@Override
				public void run() throws Exception {
					IComponentInstallerTask task = (IComponentInstallerTask) element
							.createExecutableExtension(CLASS_ATTRIBUTE);
					int order = 0;
					try {
						order = Integer.valueOf(element.getAttribute(ORDER_ATTRIBUTE));
					} catch (NumberFormatException e) {
						order = 0;
					}
					if (order < 0) {
						order = 0;
					}

					task.setOrder(order);
					task.setComponenGroupId(element.getAttribute(GROUP_ATTRIBUTE));
					task.setComponenArtifactId(element.getAttribute(ARTIFACT_ATTRIBUTE));
					task.setComponenVersion(element.getAttribute(VERSION_ATTRIBUTE));
					task.setComponentClassifier(element.getAttribute(CLASSIFIER_ATTRIBUTE));
					task.setComponentPackageType(element.getAttribute(PACKAGE_TYPE_ATTRIBUTE));
					task.setComponentType(element.getAttribute(TYPE_ATTRIBUTE));
					ret.add(task);
				}

			});
			return true;
		} // else return false
		return false;
	}
	
	static class TaskComparator implements Comparator<IComponentInstallerTask>{

        @Override
        public int compare(IComponentInstallerTask o1, IComponentInstallerTask o2) {
            return o1.getOrder() - o2.getOrder();
        }
	    
	}

}
