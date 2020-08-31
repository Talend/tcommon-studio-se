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

import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.RootLogger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;

/**
 * PluginLogManager
 * This class encapsulates a Log4J Hierarchy and centralizes all Logger access.
 * @author Manoel Marques
 */
public class PerformanceLogManager {

	private Hierarchy hierarchy;
	
	private class PluginEventListener implements HierarchyEventListener {
		
		/**
		 * Called when a new appender is added for a particular level.
		 * Internally it checks if the appender is one of our custom ones
		 * and sets its custom properties. 
		 * @param category level
		 * @param appender appender added for this level
		 */
		public void addAppenderEvent(Category cat, Appender appender) {
			if (appender instanceof PerformanceFileAppender) {
				((PerformanceFileAppender)appender).activateOptions();
			}
		}
		
		/**
		 * Called when a appender is removed from for a particular level.
		 * Does nothing.
		 * @param category level
		 * @param appender appender added for this level
		 */
		public void removeAppenderEvent(Category cat, Appender appender) {
		}
	}
	
	/**
	 * Creates a new PluginLogManager. Saves the plug-in log and state location.
	 * Creates a new Hierarchy and add a new PluginEventListener to it.
	 * Configure the hierarchy with the properties passed.
	 * Add this object to the lits of acctive plug-in log managers. 
	 * @param plugin the plug-in object
	 * @param properties log configuration properties
	 */
	public PerformanceLogManager(Plugin plugin,Properties properties) {
	    properties.put("log4j.rootCategory", ", A1");
	    properties.put("log4j.appender.A1", RollingFileAppender.class.getName());
	    IPath performanceLogPath = Platform.getLogFileLocation().removeLastSegments(1).append("performance.log");
	    properties.put("log4j.appender.A1.File", performanceLogPath.toOSString());
	    properties.put("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
	    properties.put("log4j.appender.A1.layout.ConversionPattern", "%d %-5p %c %x - %m%n");
	    
		this.hierarchy = new Hierarchy(new RootLogger(Level.INFO));
		this.hierarchy.addHierarchyEventListener(new PluginEventListener());
		new PropertyConfigurator().doConfigure(properties,this.hierarchy);	
	}
	
	/**
	 * Checks if this PluginLogManager is disabled for this level.
	 * @param level level value
	 * @return boolean true if it is disabled
	 */
	public boolean isDisabled(int level) {
		return this.hierarchy.isDisabled(level);
	}
	
	/**
	 * Enable logging for logging requests with level l or higher.
	 * By default all levels are enabled.
	 * @param level level object
	 */
	public void setThreshold(Level level) {
		this.hierarchy.setThreshold(level);
	}
	
	/**
	 * The string version of setThreshold(Level level)
	 * @param level level string
	 */
	public void setThreshold(String level) {
		this.hierarchy.setThreshold(level);
	}

	/**
	 * Get the repository-wide threshold.
	 * @return Level
	 */
	public Level getThreshold() {
		return this.hierarchy.getThreshold();
	}

	/**
	 * Returns a new logger instance named as the first parameter
	 * using the default factory. If a logger of that name already exists,
	 * then it will be returned. Otherwise, a new logger will be instantiated 
	 * and then linked with its existing ancestors as well as children.
	 * @param name logger name
	 * @return Logger
	 */
	public Logger getLogger(String name) {
		return this.hierarchy.getLogger(name);
	}
	
	/**
	 * The same as getLogger(String name) but using a factory instance instead of
	 * a default factory.
	 * @param name logger name
	 * @param factory factory instance 
	 * @return Logger
	 */
	public Logger getLogger(String name, LoggerFactory factory) {
		return this.hierarchy.getLogger(name,factory);
	}

	/**
	 * Returns the root of this hierarchy.
	 * @return Logger
	 */
	public Logger getRootLogger() {
		return this.hierarchy.getRootLogger();
	}

	/**
	 * Checks if this logger exists.
	 * @return Logger
	 */
	public Logger exists(String name) {
		return this.hierarchy.exists(name);
	}
	
	/**
	 * Removes appenders and disposes the logger hierarchy
	 *
	 */
	public void shutdown() {
		internalShutdown();
	}
	
	/**
	 * Used by LoggingPlugin to shutdown without removing it from the LoggingPlugin list
	 *
	 */
	void internalShutdown() {
		this.hierarchy.shutdown();
	}
	
	/**
	 * Returns all the loggers in this manager.
	 * @return Enumeration logger enumeration
	 */
	public Enumeration getCurrentLoggers() {
		return this.hierarchy.getCurrentLoggers();
	}

	/**
	 * Resets configuration values to its defaults.
	 * 
	 */
	public void resetConfiguration() {
		this.hierarchy.resetConfiguration();
	}
}