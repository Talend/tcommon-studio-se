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
package org.talend.core.hadoop.creator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.hadoop.IHadoopConnectionCreator;

/**
 * created by ycbai on 2015年6月29日 Detailled comment
 *
 */
public class HadoopConnectionCreatorManager {

    private static final String CREATOR_EXT_ID = "org.talend.core.runtime.hadoopConnectionCreator"; //$NON-NLS-1$

    private static List<IHadoopConnectionCreator> creators = null;

    public static List<IHadoopConnectionCreator> getCreators(String hadoopClusterId) {
        if (creators == null) {
            creators = new ArrayList<>();
            IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
            IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(CREATOR_EXT_ID);
            if (extensionPoint != null) {
                IExtension[] extensions = extensionPoint.getExtensions();
                for (IExtension extension : extensions) {
                    IConfigurationElement[] configurationElements = extension.getConfigurationElements();
                    for (IConfigurationElement configurationElement : configurationElements) {
                        try {
                            Object creator = configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                            if (creator instanceof IHadoopConnectionCreator) {
                                IHadoopConnectionCreator connectionCreator = (IHadoopConnectionCreator) creator;
                                creators.add(connectionCreator);
                            }
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
        for (IHadoopConnectionCreator hcCreator : creators) {
            hcCreator.init(hadoopClusterId);
        }
        return creators;
    }

}
