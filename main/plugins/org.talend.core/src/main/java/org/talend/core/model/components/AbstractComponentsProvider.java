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
package org.talend.core.model.components;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.talend.core.GlobalServiceRegister;
import org.talend.designer.core.ILocalProviderService;

/***/
/**
 * DOC nrousseau class global comment. Detailled comment
 */
public abstract class AbstractComponentsProvider {

    private static Logger logger = Logger.getLogger(AbstractComponentsProvider.class);

    private String id;

    private String folderName;

    private String contributer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getComponentsLocation() {
        return new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER).toString();
    }

    public void preComponentsLoad() throws IOException {
        // File installationFolder = getInstallationFolder();
        //
        // FilesUtils.createFoldersIfNotExists(installationFolder.getAbsolutePath(), false);
        //
        // File externalComponentsLocation = getExternalComponentsLocation();
        // if (externalComponentsLocation != null) {
        // if (externalComponentsLocation.exists()) {
        // try {
        // FileFilter ff = new FileFilter() {
        //
        // public boolean accept(File pathname) {
        // if (pathname.getName().equals(".svn")) {
        // return false;
        // }
        // return true;
        // }
        //
        // };
        // FilesUtils.copyFolder(externalComponentsLocation, installationFolder, false, ff, null, true, true);
        // } catch (IOException e) {
        // ExceptionHandler.process(e);
        // }
        // } else {
        // logger.warn(Messages
        //                        .getString("AbstractComponentsProvider.folderNotExist", externalComponentsLocation.toString())); //$NON-NLS-1$
        // }
        // }
    }

    /**
     * This method should return the original path where should be stored all the components in the provider.<br>
     * Means <bundle>/components.
     * 
     * @return
     */
    protected abstract File getExternalComponentsLocation();

    /**
     * By default this one is the same as the getExternalComponentsLocation.<br>
     * But some components need to install components finally in another folder. Like the user components, these
     * components will be stored in the main provider instead.
     * 
     * @return
     * @throws IOException
     */
    public File getInstallationFolder() throws IOException {
        return getExternalComponentsLocation();
    }

    public String getFamilyTranslation(String text) {
        return null;
    }

    /**
     * DOC guanglong.du Comment method "getResourceBundle".
     * 
     * @return
     */
    public ResourceBundle getResourceBundle(String label) {
        ILocalProviderService service = (ILocalProviderService) GlobalServiceRegister.getDefault().getService(
                ILocalProviderService.class);
        return service.getResourceBundle(label);
    }

    /**
     * Always false, except for components who are stored in the main provider (user or ecosystem components)
     * 
     * @return
     */
    public boolean isUseLocalProvider() {
        return false;
    }

    public String getContributer() {
        return contributer;
    }

    public void setContributer(String contributer) {
        this.contributer = contributer;
    }

    public boolean isCustom() {
        return false;
    }

    public String getComponentsBundle() {
        return null;
    }

}
