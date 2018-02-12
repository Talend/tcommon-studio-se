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
package org.talend.core.model.repository.documentation.generation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.genhtml.IHTMLDocConstants;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.document.IDocumentationGenerator;
import org.talend.repository.documentation.ExportFileResource;

/**
 * This class has utility methods for generating job information as HTML file.
 * 
 * $Id: JobHTMLScriptsManager.java 2007-3-8,下午03:15:50 ftang $
 * 
 */
public class JobHTMLScriptsManager implements IDocumentationManager {

    private IDocumentationGenerator docGenerator = null;

    private boolean needGenerate = true;

    /**
     * JobHTMLScriptsManager constructor comment.
     * 
     * @param docGenerator
     * @param isNeedGenerate
     */
    public JobHTMLScriptsManager(IDocumentationGenerator docGenerator, boolean isNeedGenerate) {
        this.docGenerator = docGenerator;
        this.needGenerate = isNeedGenerate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.documentation.generation.IDocumentationManager#getDocGenerator()
     */
    @Override
    public IDocumentationGenerator getDocGenerator() {
        return this.docGenerator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.documentation.generation.IDocumentationManager#getDocGenerator()
     */
    @Override
    public boolean isNeedGenerate() {
        return this.needGenerate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.documentation.generation.IDocumentationManager#getDocGenerator()
     */
    public void setNeedGenerate(boolean needGenerate) {
        this.needGenerate = needGenerate;
    }

    /**
     * Gets the set of <code>ExportFileResource</code>
     * 
     * @param process
     * @return
     */
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] process) {
        for (ExportFileResource proces : process) {
            docGenerator.generateHTMLFile(proces);// added path
        }
        return Arrays.asList(process);
    }

    public List<ExportFileResource> getExportResourcesWithCss(ExportFileResource[] process, String cssFile) {
        for (ExportFileResource proces : process) {
            docGenerator.generateHTMLFile(proces, cssFile);// added path
        }
        return Arrays.asList(process);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.documentation.generation.IDocumentationManager#getExportResources(org.talend.repository
     * .documentation.ExportFileResource[], java.lang.String, java.lang.String[])
     */
    @Override
    public List<ExportFileResource> getExportResources(ExportFileResource[] exportFileResources, String targetPath,
            String... jobVersion) throws Exception {

        if (this.needGenerate) {
            for (ExportFileResource exportFileResource : exportFileResources) {
                docGenerator.generateDocumentation(exportFileResource, targetPath, jobVersion);
            }
        } else {
            for (ExportFileResource exportFileResource : exportFileResources) {
                collectGeneratedDocumentation(exportFileResource, targetPath, jobVersion);
            }
        }
        List<ExportFileResource> resourceList = new ArrayList<ExportFileResource>();
        for (ExportFileResource resource : exportFileResources) {
            resourceList.add(resource);
        }
        return resourceList;

    }

    /**
     * ftang Comment method "collectionGeneratedDocumentation".
     * 
     * @param exportFileResource
     * @param jobVersion
     * @param targetPath
     * @throws MalformedURLException
     */
    private void collectGeneratedDocumentation(ExportFileResource exportFileResource, String targetPath, String[] jobVersion)
            throws MalformedURLException {

        Item item = exportFileResource.getItem();

        String jobName = item.getProperty().getLabel();

        String jobPath = item.getProperty().getItem().getState().getPath();

        String subFolder = null;

        if (ERepositoryObjectType.GENERATED != null
                && targetPath.endsWith(new Path(ERepositoryObjectType.GENERATED.getFolder()).lastSegment().toLowerCase())) {
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
            if (itemType != null) {
                subFolder = new Path(itemType.getFolder()).lastSegment().toLowerCase();
            }
            targetPath = targetPath + IPath.SEPARATOR + subFolder;
        }
        // Used for generating/updating all jobs' documentaiton only.
        targetPath = targetPath + IPath.SEPARATOR + jobPath + IPath.SEPARATOR + jobName;

        String version = ""; //$NON-NLS-1$

        // Checks if the job's version is specified, see it on "Export documentation" Dialog:
        if (jobVersion != null && jobVersion.length == 1) {
            version = jobVersion[0];
        } else {
            version = item.getProperty().getVersion();
        }
        targetPath = targetPath + "_" + version; //$NON-NLS-1$

        // Store xml and html files:
        List<URL> resultFiles = new ArrayList<URL>(5);

        // Store all pictures' path:
        List<URL> picList = new ArrayList<URL>(5);

        File file = new File(targetPath);

        if (file.exists()) {
            File[] listFiles = file.listFiles();
            for (File file2 : listFiles) {
                if (!(file2.exists())) {
                    continue;
                }

                // i.e. xml file and html file:
                if (file2.isFile()) {

                    resultFiles.add(file2.toURL());

                } else if (file2.isDirectory() && file2.getName().equals(IHTMLDocConstants.PIC_FOLDER_NAME)) {
                    for (File tempFile : file2.listFiles()) {
                        if (tempFile.exists() && tempFile.isFile()) {
                            // Copy all picture folders:
                            picList.add(tempFile.toURL());
                        }
                    }

                }
            }

            exportFileResource.addResources(resultFiles);

            exportFileResource.addResources(IHTMLDocConstants.PIC_FOLDER_NAME, picList);
        }

    }

    /**
     * Deletes the temporary files.
     */
    public void deleteTempFiles() {
        HTMLDocUtils.deleteTempFiles();
    }

}
