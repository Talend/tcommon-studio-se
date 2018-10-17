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
package org.talend.updates.runtime.storage.impl;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.updates.runtime.nexus.component.NexusComponentsTransport;
import org.talend.updates.runtime.storage.AbstractFeatureStorage;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class NexusFeatureStorage extends AbstractFeatureStorage {

    private String mvnURI;

    private String imageMvnURI;

    private ArtifactRepositoryBean serverBean;

    public NexusFeatureStorage(ArtifactRepositoryBean serverBean, String mvnURI, String imageMvnURI) {
        this.serverBean = serverBean;
        this.mvnURI = mvnURI;
        this.imageMvnURI = imageMvnURI;
    }

    @Override
    protected File downloadFeatureFile(IProgressMonitor monitor) throws Exception {
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        String reletivePath = PomUtil.getArtifactPath(getArtifact());
        if (reletivePath == null) {
            throw new Exception("Can't install");
        }

        String compFileName = new Path(reletivePath).lastSegment();
        final File target = new File(getFeatDownloadFolder(), compFileName);

        try {
            ArtifactRepositoryBean serverRepoBean = getServerBean();
            char[] passwordChars = null;
            String password = serverRepoBean.getPassword();
            if (password != null) {
                passwordChars = password.toCharArray();
            }
            NexusComponentsTransport transport = new NexusComponentsTransport(serverRepoBean.getRepositoryURL(),
                    serverRepoBean.getUserName(), passwordChars);
            transport.downloadFile(monitor, getMvnURI(), target);

            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }

            return target;
        } finally {
            // nothing to do
        }
    }

    @Override
    protected File downloadImageFile(IProgressMonitor monitor) throws Exception {
        String imageMvnURI = getImageMvnURI();
        if (StringUtils.isNotBlank(imageMvnURI)) {
            try {
                ArtifactRepositoryBean serverRepoBean = getServerBean();
                char[] passwordChars = null;
                String password = serverRepoBean.getPassword();
                if (password != null) {
                    passwordChars = password.toCharArray();
                }
                NexusComponentsTransport transport = new NexusComponentsTransport(serverRepoBean.getRepositoryURL(),
                        serverRepoBean.getUserName(), passwordChars);

                File tmpImageFile = File.createTempFile("Talend_feature_", ".png"); //$NON-NLS-1$ //$NON-NLS-2$
                transport.downloadFile(monitor, imageMvnURI, tmpImageFile);
                return tmpImageFile;
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    public MavenArtifact getArtifact() {
        return MavenUrlHelper.parseMvnUrl(mvnURI);
    }

    public String getMvnURI() {
        return this.mvnURI;
    }

    public String getImageMvnURI() {
        return this.imageMvnURI;
    }

    public ArtifactRepositoryBean getServerBean() {
        return this.serverBean;
    }
}
