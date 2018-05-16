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
package org.talend.updates.runtime.engine.component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.internal.p2.core.helpers.ServiceHelper;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.updates.runtime.engine.factory.NewComponentsInstallFactory;
import org.talend.updates.runtime.i18n.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RemoteComponentsTransport {

    class RemoteComponent {

        public String name, version, file, description, type;

    }

    private final File workFolder;

    private List<String> failedList = new ArrayList<String>();

    public RemoteComponentsTransport(File workFolder) {
        super();
        this.workFolder = workFolder;
    }

    public List<String> getFailedDownloadComponents() {
        return failedList;
    }

    public void download(IProgressMonitor monitor, URI base) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        final List<RemoteComponent> componentsList = retrieveComponents(monitor, base);
        if (componentsList == null || componentsList.isEmpty()) {
            throw new FileNotFoundException(Messages.getString("RemoteComponentsTransport.noAnyComponents")); //$NON-NLS-1$
        }
        for (RemoteComponent comp : componentsList) {
            try {
                File downloadedFile = downloadFile(monitor, base, comp.file);

                if (downloadedFile == null || !downloadedFile.exists()) { // download failure
                    throw new FileNotFoundException(Messages.getString("RemoteComponentsTransport.downloadComponentFailure", //$NON-NLS-1$
                            comp.file, base.toString()));
                }
            } catch (IOException e) {
                failedList.add(comp.file);
                ExceptionHandler.process(e);
            }
        }
    }

    Transport getTransport() {
        final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        IProvisioningAgent agent = (IProvisioningAgent) ServiceHelper.getService(bundleContext, IProvisioningAgent.SERVICE_NAME);
        if (agent == null) {
            throw new NullPointerException("Can't find the agent service."); //$NON-NLS-1$
        }
        Transport transport = (Transport) agent.getService(Transport.SERVICE_NAME);
        if (transport == null) {
            throw new NullPointerException("Can't find the transport service."); //$NON-NLS-1$
        }
        return transport;
    }

    List<RemoteComponent> retrieveComponents(IProgressMonitor monitor, URI base) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        List<RemoteComponent> compsList = new ArrayList<RemoteComponent>();

        File compIndexFile = downloadFile(monitor, base, NewComponentsInstallFactory.FILE_COMPONENTS_INDEX);
        if (compIndexFile != null && compIndexFile.exists()) {
            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(compIndexFile);
            NodeList list = document.getElementsByTagName("component"); //$NON-NLS-1$
            for (int i = 0; i < list.getLength(); i++) {
                Node comp = list.item(i);
                if (comp.getNodeType() == Node.ELEMENT_NODE) {
                    RemoteComponent rc = new RemoteComponent();
                    rc.name = getXmlNodeAtrr(comp, "name"); //$NON-NLS-1$
                    rc.version = getXmlNodeAtrr(comp, "version"); //$NON-NLS-1$
                    rc.type = getXmlNodeAtrr(comp, "type"); //$NON-NLS-1$
                    rc.file = getXmlNodeAtrr(comp, "file"); //$NON-NLS-1$

                    final NodeList childNodes = comp.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        final Node child = childNodes.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            if (child.getNodeName().equals("description")) { //$NON-NLS-1$
                                rc.description = child.getNodeValue();
                            }
                        }
                    }

                    compsList.add(rc);
                }
            }

        } else {
            throw new FileNotFoundException(Messages.getString("RemoteComponentsTransport.downloadComponentIndexFailure"));
        }
        return compsList;
    }

    String getXmlNodeAtrr(Node node, String attrName) {
        final NamedNodeMap attributes = node.getAttributes();
        Node attr = attributes.getNamedItem(attrName);
        if (attr != null) {
            return attr.getNodeValue();
        }
        return "";
    }

    File downloadFile(IProgressMonitor monitor, URI base, String name) throws IOException {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (base == null || name == null || name.trim().isEmpty()) {
            return null;
        }
        boolean hasError = false;
        File localFile = new File(workFolder, name);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(localFile));
            String uriString = base.toString();
            URI fileUri = URI.create(uriString + (uriString.endsWith("/") ? "" : "/") + name);
            IStatus status = getTransport().download(fileUri, bos, monitor);

            if (!status.isOK()) {
                hasError = true;
                final Throwable exception = status.getException();
                if (exception instanceof IOException) {
                    throw (IOException) exception;
                } else {
                    throw new IOException(exception);
                }
            }
            monitor.worked(20);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                // after close io, if has error, need delete the empty contents of file.
                if (hasError && localFile.exists()) {
                    localFile.delete();
                }
            } catch (IOException e) {
                //
            }
        }
        return localFile;
    }
}
