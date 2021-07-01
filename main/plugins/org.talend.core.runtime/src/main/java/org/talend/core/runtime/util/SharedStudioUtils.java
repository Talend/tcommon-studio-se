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
package org.talend.core.runtime.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.IUpdateService;


public class SharedStudioUtils {

    public static final String PROP_DEVMODE = "osgi.dev"; //$NON-NLS-1$
    
    public static boolean isSharedStudioMode() {
    	if (isDevEnvironment()) {
    		return false;
    	}
        File configFolder = new File (Platform.getConfigurationLocation().getURL().getFile());
        File studioFolder = new File (Platform.getInstallLocation().getURL().getFile());
        if (configFolder != null && studioFolder != null && configFolder.getParentFile() != null
                && configFolder.getParentFile().getAbsolutePath().equals(studioFolder.getAbsolutePath())) {
            return false;
        }
        return true;
    }
    
    private static boolean isDevEnvironment() {
    	if (CoreRuntimePlugin.getInstance().getBundle().getBundleContext().getProperty(PROP_DEVMODE) != null) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isNeedCleanOnSharedMode() {
        if (SharedStudioUtils.isSharedStudioMode()) {
            boolean isNeedClean = installedPatch();
            String studioArtifactsFileSha256Hex = getStudioArtifactsFileSha256Hex();
            if (updateArtifactsFileSha256Hex(studioArtifactsFileSha256Hex)) {
                isNeedClean = true;
            }
            return isNeedClean;
        }
        return false;
    }
    
    public static boolean updateArtifactsFileSha256Hex(String studioArtifactsFileSha256Hex) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUpdateService.class)) {
            IUpdateService updateService = GlobalServiceRegister.getDefault().getService(IUpdateService.class);
            try {
                return updateService.updateArtifactsFileSha256Hex(new NullProgressMonitor(), studioArtifactsFileSha256Hex);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }
    public static boolean installedPatch() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUpdateService.class)) {
            IUpdateService updateService = GlobalServiceRegister.getDefault().getService(IUpdateService.class);
            try {
                return updateService.syncSharedStudioLibraryInPatch(new NullProgressMonitor());
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }
    
	public static File getSharedStudioComponentsParentFolder() {
		File configFolder = new File(Platform.getConfigurationLocation().getURL().getFile());
		return configFolder;
	}
	
	public static File getSharedStudioComponentsExtFolder() {
		File componentFolder = SharedStudioUtils.getSharedStudioComponentsParentFolder();
		IPath path = new Path(IComponentsFactory.COMPONENTS_INNER_FOLDER);
        path = path.append(IComponentsFactory.EXTERNAL_COMPONENTS_INNER_FOLDER);
        File extchangeFolder = new File (componentFolder, path.toOSString());
		return extchangeFolder;
	}
	
	public static IPath getTempFolderPath() {
		if (SharedStudioUtils.isSharedStudioMode()) {
			Path wsPath = new Path(Platform.getInstanceLocation().getURL().getPath());
			return wsPath.append("temp");
		} else {
			return new Path(System.getProperty("user.dir")).append("temp");
		}
	}
	
    public static String getStudioArtifactsFileSha256Hex() {
        File studioArtifactsFile = new File(Platform.getInstallLocation().getURL().getPath(), "artifacts.xml");//$NON-NLS-1$
        if (studioArtifactsFile.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(studioArtifactsFile);
                return sha256Hex(inputStream);
            } catch (FileNotFoundException e) {
                ExceptionHandler.process(e);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ex) {
                        ExceptionHandler.process(ex);
                    }
                }
            }
        }
        return "";
    }

    private static String sha256Hex(final InputStream inputStream) throws IOException {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(inputStream);
    }
}
