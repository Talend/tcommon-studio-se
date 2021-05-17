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
package org.talend.updates.runtime.storage.impl;

import java.io.File;
import java.nio.file.Files;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.updates.runtime.storage.AbstractFeatureStorage;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public class NexusComponentStorage extends AbstractFeatureStorage {

    private Function<MavenArtifact, File> downloader;

    private String mvnURI;

    private String imageMvnURI;

    public NexusComponentStorage(Function<MavenArtifact, File> downloader, String mvnURI, String imageMvnURI) {
        this.downloader = downloader;
        this.mvnURI = mvnURI;
        this.imageMvnURI = imageMvnURI;
    }

    @Override
    protected File downloadFeatureFile(IProgressMonitor monitor) throws Exception {
        return downloadFile(MavenUrlHelper.parseMvnUrl(getMvnURI()));
    }

    @Override
    protected File downloadImageFile(IProgressMonitor monitor) throws Exception {
        return downloadFile(MavenUrlHelper.parseMvnUrl(getImageMvnURI()));
    }

    private File downloadFile(MavenArtifact ma) throws Exception {
        File finalFile = null;
        File file = this.downloader.apply(ma);
        if (file != null) {
            finalFile = new File(getFeatDownloadFolder(), file.getName());
            Files.copy(file.toPath(), finalFile.toPath());
        }
        return finalFile;
    }

    public String getMvnURI() {
        return this.mvnURI;
    }

    public String getImageMvnURI() {
        return this.imageMvnURI;
    }

}
