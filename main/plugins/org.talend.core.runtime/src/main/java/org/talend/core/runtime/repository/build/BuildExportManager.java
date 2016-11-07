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
package org.talend.core.runtime.repository.build;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.repository.documentation.ExportFileResource;

/**
 * DOC ggu class global comment. Detailed comment
 */
public final class BuildExportManager {

    private static final BuildExportManager instance = new BuildExportManager();

    private static final BuildExportRegistryReader reader = new BuildExportRegistryReader();

	private EXPORT_TYPE exportType = EXPORT_TYPE.STANDARD;

    private BuildExportManager() {
        super();
    }

    public static BuildExportManager getInstance() {
        return instance;
    }

    IBuildExportDependenciesProvider[] getDependenciesProviders() {
        return reader.getDependenciesProviders();
    }

    public void exportDependencies(ExportFileResource resource, Item item) {
        IBuildExportDependenciesProvider[] dependenciesProviders = getDependenciesProviders();
        if (dependenciesProviders != null) {
            for (IBuildExportDependenciesProvider provider : dependenciesProviders) {
                provider.exportDependencies(resource, item);
            }
        }
    }

	/**
	 * Calling this method will set export type to OSGI. The purpose is that any implementation of "Build Export Provider" schema, will
	 * be able to know when the context is OSGI by calling BuildExportManager.getInstance().getCurrentExportType().
	 * @see #getCurrentExportType()
	 * @param resource
	 * @param item
	 */
	public void exportOSGIDependencies(ExportFileResource resource, ProcessItem item) {
		this.exportType = EXPORT_TYPE.OSGI;
		try {
			exportDependencies(resource, item);
		} finally {
			this.exportType = EXPORT_TYPE.STANDARD;
		}
	}
	
	/**
	 * Provide the context of the job export (i.e. OSGI or STANDARD export)
	 * @see #exportOSGIDependencies(ExportFileResource, ProcessItem)
	 * @return context of the job export
	 */
	public EXPORT_TYPE getCurrentExportType() {
		return exportType;
	}
	
	public static enum EXPORT_TYPE {
		OSGI,STANDARD;
	}
}
