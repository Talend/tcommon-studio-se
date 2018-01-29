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
package org.talend.designer.maven.tools.creator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.designer.maven.utils.PomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class UpdatePomAndAssemblyForChildrenJobs implements IPomJobExtension {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.maven.tools.creator.IPomJobExtension#updatePom(org.eclipse.core.runtime.IProgressMonitor,
     * org.eclipse.core.resources.IFile, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void updatePom(IProgressMonitor monitor, IFile pomFile, Map<String, Object> args) {
        if (pomFile == null || args == null || args.isEmpty() || !pomFile.exists()) {
            return;
        }
        Set<String> childrenGroupIds = new HashSet<>();
        if (args.containsKey(KEY_CHILDREN_JOBS_GROUP_IDS)) {
            childrenGroupIds = (Set<String>) args.get(KEY_CHILDREN_JOBS_GROUP_IDS);
        }
        if (childrenGroupIds == null || childrenGroupIds.isEmpty()) {
            return;
        }
        // set group map for libs and binaries
        Map<String, String> childrenGroupsLibExcludesMap = new HashMap<>();
        Map<String, String> childrenGroupsBinariesIncludesMap = new HashMap<>();
        final String[] array = childrenGroupIds.toArray(new String[0]);
        for (int i = 0; i < array.length; i++) {
            String groupId = array[i];
            final String childSet = groupId + ":*";
            childrenGroupsLibExcludesMap.put("talend.jobs.excludes.set.child" + i, childSet);
            childrenGroupsBinariesIncludesMap.put("jobs.binaries.includes.set.child" + i, childSet);
        }

        try {
            updatePomProfiles(monitor, pomFile, childrenGroupsLibExcludesMap, childrenGroupsBinariesIncludesMap);

            if (args.containsKey(KEY_ASSEMBLY_FILE)) {
                IFile assemblyFile = (IFile) args.get(KEY_ASSEMBLY_FILE);
                updateAssemblyDependencySets(monitor, assemblyFile, childrenGroupsLibExcludesMap,
                        childrenGroupsBinariesIncludesMap);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void updatePomProfiles(IProgressMonitor monitor, IFile pomFile,
            final Map<String, String> childrenGroupsLibExcludesMap, final Map<String, String> childrenGroupsBinariesIncludesMap)
            throws Exception {
        Model pomModel = MavenPlugin.getMavenModelManager().readMavenModel(pomFile);
        boolean modified = false;
        for (Profile p : pomModel.getProfiles()) {
            if (p.getId().equals("include-libs")) {
                addProfileProperties(p, childrenGroupsLibExcludesMap);
                modified = true;
            } else if (p.getId().equals("include-binaries")) {
                addProfileProperties(p, childrenGroupsBinariesIncludesMap);
                modified = true;
            }
        }

        if (modified) {
            PomUtil.savePom(monitor, pomModel, pomFile);
        }
    }

    private void addProfileProperties(Profile p, final Map<String, String> props) {
        final Properties properties = p.getProperties();
        final Iterator<Entry<String, String>> entryIt = props.entrySet().iterator();
        while (entryIt.hasNext()) {
            final Entry<String, String> entry = entryIt.next();
            properties.put(entry.getKey(), entry.getValue());
        }
    }

    private void updateAssemblyDependencySets(IProgressMonitor monitor, IFile assemblyFile,
            final Map<String, String> childrenGroupsLibExcludesMap, final Map<String, String> childrenGroupsBinariesIncludesMap)
            throws Exception {
        if (assemblyFile == null || !assemblyFile.exists() || childrenGroupsLibExcludesMap.isEmpty()) {
            return;
        }

        Document document = PomUtil.loadAssemblyFile(monitor, assemblyFile);
        if (document == null) {
            return;
        }

        Node dependencySetsElem = getElement(document.getDocumentElement(), "dependencySets", 1);
        if (dependencySetsElem == null) {
            return;
        }

        boolean modified = false;

        final NodeList dependencySetNodes = dependencySetsElem.getChildNodes();
        for (int i = 0; i < dependencySetNodes.getLength(); i++) {
            Node dependencySetNode = dependencySetNodes.item(i);
            if (dependencySetNode.getNodeType() == Node.ELEMENT_NODE && dependencySetNode.getNodeName().equals("dependencySet")) {
                final Node outputDirectoryNode = getElement(dependencySetNode, "outputDirectory", 1);
                if (outputDirectoryNode == null) {
                    continue;
                }
                // exclude for libs
                if ("lib".equals(outputDirectoryNode.getTextContent())) {
                    final Node excludesNode = getElement(dependencySetNode, "excludes", 1);
                    if (excludesNode != null) {
                        boolean valid = false;
                        final NodeList childNodes = excludesNode.getChildNodes();
                        for (int index = 0; index < childNodes.getLength(); index++) {
                            Node exclude = childNodes.item(index);
                            if (exclude.getNodeType() == Node.ELEMENT_NODE && exclude.getNodeName().equals("exclude")) {
                                if ("${talend.jobs.excludes.set}".equals(exclude.getTextContent())) {
                                    valid = true;
                                    break;
                                }
                            }
                        }
                        //
                        if (valid) {
                            final Iterator<Entry<String, String>> libExcludesIt = childrenGroupsLibExcludesMap.entrySet()
                                    .iterator();
                            while (libExcludesIt.hasNext()) {
                                final Entry<String, String> libExcludeEntry = libExcludesIt.next();
                                final Element excludeElement = document.createElement("exclude");
                                excludesNode.appendChild(excludeElement);
                                excludeElement.setTextContent("${" + libExcludeEntry.getKey() + "}");
                            }
                            modified = true;
                        }
                    }
                }
                // include for children jobs
                if ("${talend.job.name}".equals(outputDirectoryNode.getTextContent())) {
                    final Node includesNode = getElement(dependencySetNode, "includes", 1);
                    if (includesNode != null) {
                        boolean valid = false;
                        final NodeList childNodes = includesNode.getChildNodes();
                        for (int index = 0; index < childNodes.getLength(); index++) {
                            Node include = childNodes.item(index);
                            if (include.getNodeType() == Node.ELEMENT_NODE && include.getNodeName().equals("include")) {
                                if ("${jobs.binaries.includes.set}".equals(include.getTextContent())) {
                                    valid = true;
                                    break;
                                }
                            }
                        }

                        if (valid) {
                            final Iterator<Entry<String, String>> binariesIncludesIt = childrenGroupsBinariesIncludesMap
                                    .entrySet().iterator();
                            while (binariesIncludesIt.hasNext()) {
                                final Entry<String, String> binariesIncludeEntry = binariesIncludesIt.next();
                                final Element includeElement = document.createElement("include");
                                includesNode.appendChild(includeElement);
                                includeElement.setTextContent("${" + binariesIncludeEntry.getKey() + "}");
                            }
                            modified = true;
                        }
                    }
                }
            }
        }

        if (modified) {
            PomUtil.saveAssemblyFile(monitor, assemblyFile, document);
        }
    }

    private Node getElement(Node parent, String elemName, int level) {
        NodeList childrenNodeList = parent.getChildNodes();
        for (int i = 0; i < childrenNodeList.getLength(); i++) {
            Node child = childrenNodeList.item(i);
            if (child != null && child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return child;
                }
            }
            if (level > 1) {
                Node element = getElement(child, elemName, --level);
                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }
}
