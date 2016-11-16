package org.talend.repository.items.importexport.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ImportExportUiActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.talend.repository.items.importexport.ui"; //$NON-NLS-1$

	// The shared instance
	private static ImportExportUiActivator plugin;
	
	/**
	 * The constructor
	 */
	public ImportExportUiActivator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ImportExportUiActivator getDefault() {
		return plugin;
	}

}
