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
package org.talend.updates.runtime.engine.component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.utils.PomUtil;

public class ComponentsNexusTransport {

    private static final int BUFFER_SIZE = 8192;

    private String nexusURL, nexusUser;

    private char[] nexusPass;

    public ComponentsNexusTransport(String nexusURL, String nexusUser, char[] nexusPass) {
        this.nexusURL = nexusURL;
        this.nexusUser = nexusUser;
        this.nexusPass = nexusPass;
    }

    protected String getNexusURL() {
        return nexusURL;
    }

    protected String getNexusUser() {
        return nexusUser;
    }

    protected String getNexusPassStr() {
        if (nexusPass == null) {
            return null;
        }
        return new String(nexusPass);

    }

    public ComponentsNexusTransport(String nexusUrl) {
        this(nexusUrl, null, null);
    }

    public boolean isAvailable() {
        if (getNexusURL() == null || getNexusURL().isEmpty()) {
            return false;
        }
        return NexusServerUtils.checkConnectionStatus(getNexusURL(), getNexusUser(), getNexusPassStr());
    }

    public void downloadFile(IProgressMonitor monitor, String mvnURI, File target) throws Exception {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnURI);
        downloadFile(monitor, artifact, target);
    }

    private void setAuthenticator() {
        if (StringUtils.isNotEmpty(getNexusUser())) {
            Authenticator.setDefault(new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    final String nexusPassStr = getNexusPassStr();
                    return new PasswordAuthentication(getNexusUser(), nexusPassStr != null ? nexusPassStr.toCharArray()
                            : new char[0]);
                }

            });
        }
    }

    public void downloadFile(IProgressMonitor monitor, MavenArtifact artifact, File target) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (artifact == null) {
            return;
        }
        final String reletivePath = PomUtil.getArtifactPath(artifact);
        if (reletivePath == null) {
            return;
        }

        final Authenticator defaultAuthenticator = NetworkUtil.getDefaultAuthenticator();

        boolean success = false;

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        HttpURLConnection httpURLConnection = null;
        try {
            setAuthenticator();

            httpURLConnection = NexusServerUtils.getHttpURLConnection(getNexusURL(), reletivePath, getNexusUser(),
                    getNexusPassStr());

            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }

            InputStream inputStream = httpURLConnection.getInputStream();
            bis = new BufferedInputStream(inputStream);
            bos = new BufferedOutputStream(new FileOutputStream(target));

            byte[] buf = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = bis.read(buf)) != -1) {
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                bos.write(buf, 0, bytesRead);
            }
            bos.flush();
            success = true;
        } finally {
            //
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }

            //
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }

            //
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            // delete the failed file
            if (!success && target.exists() && target.isFile()) {
                target.delete();
            }

            // set back
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    public Document downloadXMLDocument(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (artifact == null) {
            return null;
        }
        final String reletivePath = PomUtil.getArtifactPath(artifact);
        if (reletivePath == null) {
            return null;
        }

        final Authenticator defaultAuthenticator = NetworkUtil.getDefaultAuthenticator();

        BufferedInputStream bis = null;
        HttpURLConnection httpURLConnection = null;
        try {
            setAuthenticator();

            httpURLConnection = NexusServerUtils.getHttpURLConnection(getNexusURL(), reletivePath, getNexusUser(),
                    getNexusPassStr());

            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }

            InputStream inputStream = httpURLConnection.getInputStream();
            bis = new BufferedInputStream(inputStream);
            SAXReader saxReader = new SAXReader();

            Document document = saxReader.read(bis);
            return document;
        } finally {
            //
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }

            //
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            // set back
            Authenticator.setDefault(defaultAuthenticator);
        }

    }

}
