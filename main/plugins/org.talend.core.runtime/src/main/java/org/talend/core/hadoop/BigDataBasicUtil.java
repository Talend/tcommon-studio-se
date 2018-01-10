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
package org.talend.core.hadoop;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EMap;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.hd.IDynamicDistributionManager;

/**
 * created by cmeng on Jul 20, 2015 Detailled comment
 *
 */
public class BigDataBasicUtil {

    /**
     * DON'T use cache here!
     */
    // private static Collection<String> dynamicDistributionPaths;

    public static Object getFramework(Item item) {
        if (item == null) {
            return null;
        }
        Property property = item.getProperty();
        if (property != null) {
            EMap additionalProperties = property.getAdditionalProperties();
            if (additionalProperties != null) {
                return additionalProperties.get(HadoopConstants.FRAMEWORK);
            }
        }
        return null;
    }

    public static void reloadAllDynamicDistributions(IProgressMonitor monitor) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            IHadoopDistributionService hdService = (IHadoopDistributionService) GlobalServiceRegister.getDefault()
                    .getService(IHadoopDistributionService.class);
            if (hdService != null) {
                IDynamicDistributionManager ddManager = hdService.getDynamicDistributionManager();
                if (ddManager != null && ddManager.isLoaded()) {
                    try {
                        ddManager.reloadAllDynamicDistributions(monitor);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }

    public static Collection<String> getDynamicDistributionPaths() {
        // if (dynamicDistributionPaths == null || dynamicDistributionPaths.isEmpty()) {
        Collection<String> dynamicDistributionPaths = new HashSet<>();
        IDynamicDistributionManager ddManager = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            IHadoopDistributionService hdService = (IHadoopDistributionService) GlobalServiceRegister.getDefault()
                    .getService(IHadoopDistributionService.class);
            if (hdService != null) {
                ddManager = hdService.getDynamicDistributionManager();
                if (ddManager != null) {
                    String dynamicDistrPath = ddManager.getUserStoragePath();
                    dynamicDistributionPaths.add(dynamicDistrPath);
                    Collection<String> preferencePaths = ddManager.getPreferencePaths();
                    dynamicDistributionPaths.addAll(preferencePaths);
                }
            }

        }
        // }
        return dynamicDistributionPaths;
    }

    public static boolean isInDynamicDistributionPath(String projectTechName, String path) {
        Collection<String> distrPaths = getDynamicDistributionPaths();
        if (path != null && distrPaths != null && !distrPaths.isEmpty()) {
            for (String distrPath : distrPaths) {
                if (path.startsWith(projectTechName + "/" + distrPath)) { //$NON-NLS-1$
                    return true;
                }
            }
        }
        return false;
    }
}
