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
package org.talend.metadata.managment.ui.wizard.documentation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.LinkDocumentationItem;
import org.talend.core.model.properties.LinkType;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;

/**
 * ggu class global comment. Detailled comment
 */
public final class LinkDocumentationHelper {

    /**
     * 
     * ggu Comment method "getTempFile".
     * 
     * 
     */
    public static IFile getTempFile(String id, String extension) {
        if (id == null) {
            return null;
        }
        if (extension == null) {
            extension = ""; //$NON-NLS-1$ 
        } else {
            extension = "." + extension; //$NON-NLS-1$ 
        }
        // Save data to a temporary file
        Project project = ((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject();
        IProject fsProject;
        try {
            fsProject = ResourceUtils.getProject(project);
            IFolder tmpFolder = ResourceUtils.getFolder(fsProject, RepositoryConstants.TEMP_DIRECTORY, false);
            if (!tmpFolder.exists()) {
                ResourceUtils.createFolder(tmpFolder);
            }
            return tmpFolder.getFile(id + extension);

        } catch (PersistenceException e) {
            //
        }
        return null;
    }

    public static IFile getTempFile(final String id) {
        return getTempFile(id, null);
    }

    /**
     * 
     * ggu Comment method "getContentFormGeneralFile".
     * 
     * get content form link documentation item
     */
    public static ByteArray getLinkItemContent(final LinkDocumentationItem linkItem) {
        if (linkItem == null) {
            return null;
        }
        LinkType link = linkItem.getLink();
        if (!LinkUtils.validateLink(link)) { // not existed
            return null;
        }
        File fromFile = null;
        if (LinkUtils.isRemoteFile(link.getURI())) {
            fromFile = createContentFromRemote(link.getURI());
        } else { // general file
            fromFile = new File(link.getURI());
        }
        if (fromFile == null) {
            return null;
        }
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        try {
            byteArray.setInnerContentFromFile(fromFile);
        } catch (IOException e) {
            return null;
        }
        return byteArray;
    }

    /**
     * 
     * ggu Comment method "createContentFromRemote".
     * 
     * create content of remote file to temporary file.
     */
    public static File createContentFromRemote(final String uri) {
        if (!LinkUtils.isRemoteFile(uri)) {
            return null;
        }

        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e1) {
            return null;
        }

        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        byte[] buf = new byte[1024];
        int size = 0;
        IFile tmpFile = getTempFile("_temporary_file"); //$NON-NLS-1$
        if (tmpFile == null) {
            return null;
        }
        File file = tmpFile.getLocation().toFile();

        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.connect();
            httpConn.getInputStream();
            bis = new BufferedInputStream(httpConn.getInputStream());
            fos = new FileOutputStream(file);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    //
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                    bis = null;
                } catch (IOException e) {
                    //
                }
            }
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return file;
    }

    public static boolean continueAddDocumentation() {
        return MessageDialog.openConfirm(Display.getCurrent().getActiveShell(),
                Messages.getString("LinkDocumentationHelper.remoteErrorTitle"), //$NON-NLS-1$
                Messages.getString("LinkDocumentationHelper.remoteErrorMessages")); //$NON-NLS-1$
    }
}
