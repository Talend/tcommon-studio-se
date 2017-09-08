// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.migration;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * Similar to
 * {@link org.talend.camel.designer.migration.AbstractRouteItemComponentMigrationTask}
 * , As component node abstract level migration task class, and also provide
 * some useful methods, such as #
 * {@link AbstractJobItemComponentMigrationTask#findComponentNodesRegex(ProcessType, String)}
 * \n, #
 * {@link AbstractJobItemComponentMigrationTask#findElementByName(List, String, Class)}
 * .
 * 
 * @author GaoZone
 */
public abstract class AbstractJobItemComponentMigrationTask extends AbstractJobMigrationTask {

	/** FACTORY use to save item if changed in migration task. */
	protected static final ProxyRepositoryFactory REPO_FACTORY = ProxyRepositoryFactory.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.talend.core.model.migration.AbstractItemMigrationTask#execute(org
	 * .talend.core.model.properties.Item)
	 */
	@Override
	final public ExecutionResult execute(Item item) {
		ProcessType processType = getProcessType(item);
		if (processType == null) {
			return ExecutionResult.NOTHING_TO_DO;
		}
		List<NodeType> nodes = findComponentNodesRegex(processType, getComponentNameRegex());
		if (nodes.isEmpty()) {
			return ExecutionResult.NOTHING_TO_DO;
		}
		try {
			boolean needSave = false;
			for (NodeType node : nodes) {
				needSave |= execute(node);
			}
			if (needSave) {
				saveItem(item);
				return ExecutionResult.SUCCESS_NO_ALERT;
			} else {
				return ExecutionResult.NOTHING_TO_DO;
			}
		} catch (Exception e) {
			ExceptionHandler.process(e);
			return ExecutionResult.FAILURE;
		}
	}

	private void saveItem(Item item) throws PersistenceException {
		REPO_FACTORY.save(item, true);
	}

	/**
	 * Find component nodes equals given name.
	 * 
	 * @param item
	 *            the item
	 * @param component
	 *            the component
	 * @return the list
	 */
	protected List<NodeType> findComponentNodes(ProcessType process, String component) {
		return findComponentNodes(process, component, false);
	}

	/**
	 * Find component nodes match given regex.
	 * 
	 * @param item
	 *            the item
	 * @param componentRegex
	 *            the component regex
	 * @return the list
	 */
	protected List<NodeType> findComponentNodesRegex(ProcessType process, String componentRegex) {
		return findComponentNodes(process, componentRegex, true);
	}

	private List<NodeType> findComponentNodes(ProcessType process, String search, boolean isRegex) {
		if (search == null) {
			throw new RuntimeException("Can't search component node by \"null\" in " + this.getClass());
		}
		List<NodeType> returnList = new ArrayList<NodeType>();
		for (Object o : process.getNode()) {
			if (o instanceof NodeType) {
				NodeType currentNode = (NodeType) o;
				String componentName = currentNode.getComponentName();
				if (isRegex) {
					if (componentName.matches(search)) {
						returnList.add(currentNode);
					}
				} else {
					if (componentName.equals(search)) {
						returnList.add(currentNode);
					}
				}
			}
		}
		return returnList;
	}

	/**
	 * Find element by name.
	 * 
	 * @param <T>
	 *            the generic type, same as <b>elementType</b>.
	 * @param elements
	 *            the elements in List.
	 * @param name
	 *            the name use for searching.
	 * @param elementType
	 *            the return element type. Should be
	 *            {@code ElementParameterType.class} or
	 *            {@code ElementValueType.class}
	 * @return contented element, or {@code null} if not found.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T findElementByName(List<?> elements, String name, Class<?> elementType) {
		if (elements == null || name == null || elementType == null) {
			return null;
		}
		for (Object ele : elements) {
			if (!elementType.isInstance(ele)) {
				continue;
			}
			String elementName = null;
			if (elementType == ElementParameterType.class) {
				ElementParameterType elementParameterType = (ElementParameterType) ele;
				elementName = elementParameterType.getName();
				// return (T) findParameterByName(elements, name);
			} else if (elementType == ElementValueType.class) {
				ElementValueType elementValueType = (ElementValueType) ele;
				elementName = elementValueType.getElementRef();
			}
			if (name.equals(elementName)) {
				return (T) ele;
			}
		}
		return null;
	}

	/**
	 * Execute migration task on node level.
	 * 
	 * @param node
	 *            the node need to do migration task.
	 * @return true - if need save, or false - no changes on this node.
	 */
	protected abstract boolean execute(NodeType node);

	/**
	 * the regex patten to filter component name.
	 * 
	 * @return the component name regex
	 */
	protected abstract String getComponentNameRegex();
}
