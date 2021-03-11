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
package org.talend.commons.utils.time;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.RootLogger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

public class PerformanceLogManager {

	private Hierarchy hierarchy;
	
	public PerformanceLogManager() {
	    Properties properties = new Properties();
	    properties.put("log4j.rootCategory", ", A1");
	    properties.put("log4j.appender.A1", RollingFileAppender.class.getName());
	    IPath performanceLogPath = Platform.getLogFileLocation().removeLastSegments(1).append("performance.log");
	    properties.put("log4j.appender.A1.File", performanceLogPath.toOSString());
	    properties.put("log4j.appender.A1.MaxBackupIndex", "10");// same as .log's max backup log file count
	    properties.put("log4j.appender.A1.MaxFileSize", "1000000");//1000*1000 byte, same as .log's max file size
	    properties.put("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
	    properties.put("log4j.appender.A1.layout.ConversionPattern", "%d %-5p %c %x - %m%n");
	    
		this.hierarchy = new Hierarchy(new RootLogger(Level.INFO));
		new PropertyConfigurator().doConfigure(properties,hierarchy);	
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

	public Logger getRootLogger() {
		return this.hierarchy.getRootLogger();
	}

	public Logger exists(String name) {
		return this.hierarchy.exists(name);
	}
	
	public void shutdown() {
	    this.hierarchy.shutdown();
	}
	
	/**
	 * Returns all the loggers in this manager.
	 * @return Enumeration logger enumeration
	 */
	public Enumeration getCurrentLoggers() {
		return this.hierarchy.getCurrentLoggers();
	}

	public void resetConfiguration() {
		this.hierarchy.resetConfiguration();
	}
}