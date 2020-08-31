// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons;

import java.io.IOException;

import org.apache.log4j.Layout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.IPath;

/**
 * PluginFileAppender
 * This class is a custom Log4J appender that sends Log4J events to 
 * the Eclipse plug-in state location. It extends the RollingFileAppender class.
 * @author Manoel Marques
 */
public class PerformanceFileAppender extends RollingFileAppender {

	private IPath stateLocation;
	private boolean activateOptionsPending; 
	
	/**
	 * Creates a new PluginFileAppender.
	 */
	public PerformanceFileAppender() {
		super();
	}
	
	/**
	 * Creates a new PluginFileAppender.
	 * @param layout layout instance.
	 * @param stateLocation IPath containing the plug-in state location 
	 */	
	public PerformanceFileAppender(Layout layout,IPath stateLocation) {
		this();
		setLayout(layout);
		setStateLocation(stateLocation);	
	}
	
	/**
	 * Creates a new PluginFileAppender.
	 * @param layout layout instance.
	 * @param stateLocation IPath containing the plug-in state location 
	 * @param file file name
	 * @param append true if file is to be appended
	 */	
	public PerformanceFileAppender(Layout layout,IPath stateLocation, String file, boolean append)
			throws IOException {
		this();
		setLayout(layout);
		setStateLocation(stateLocation);
		setFile(file);
		setAppend(append);
		activateOptions();
	}

	/**
	 * Creates a new PluginFileAppender.
	 * @param layout layout instance.
	 * @param stateLocation IPath containing the plug-in state location 
	 * @param file file name
	 */	
	public PerformanceFileAppender(Layout layout,IPath stateLocation, String file) throws IOException {
		super();
		setLayout(layout);
		setStateLocation(stateLocation);
		setFile(file);
		activateOptions();
	}
	
	/**
	 * Sets the state location. If activateOptions call is pending, translate the file name
	 * and call activateOptions 
	 * @param stateLocation IPath containing the plug-in state location 
	 */	
	void setStateLocation(IPath stateLocation) {
		if (this.activateOptionsPending) {
			this.activateOptionsPending = false;
			setFile(getFile());
			activateOptions();
		}
	}
	
	/**
	 * Finishes instance initialization. If state location was not set, set activate as 
	 * pending and does nothing.
	 */	
	public void activateOptions() {
		if (this.stateLocation == null) { 
			this.activateOptionsPending = true;
			return;
		}	
		
		// base class will call setFile, don't translate the name
		// because it was already translated
		super.activateOptions(); 
	}
	
}