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
package org.talend.platform.logging;

import java.text.MessageFormat;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * Define the log stategy of application. <br/>
 * 
 * $Id: TalendPlatformLogAppender.java 6924 2007-11-12 13:16:18Z plegall $
 * 
 */
public class TalendPlatformLogAppender extends AppenderSkeleton {

    private String symbolicName;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
     */
    protected void append(LoggingEvent event) {

        // don't go any further if event is not severe enough.
        if (!isAsSevereAsThreshold(event.getLevel())) {
            return;
        }

        ILog log = getBundleILog();
        if (log == null) {
            return;
        }

        // if throwable information is available, extract it.
        Throwable t = null;
        if (event.getThrowableInformation() != null && layout.ignoresThrowable()) {
            t = event.getThrowableInformation().getThrowable();
        }

        // build an Eclipse Status record, map severity and code from Event.
        Status s = new Status(getSeverity(event), getSymbolicName(), getCode(event), layout.format(event), t);

        log.log(s);
    }

    /**
     * map LoggingEvent's level to Status severity
     * 
     * @param ev
     * @return
     */
    private int getSeverity(LoggingEvent ev) {

        Level level = ev.getLevel();
        if (level == Level.FATAL || level == Level.ERROR)
            return IStatus.ERROR;
        else if (level == Level.WARN)
            return IStatus.WARNING;
        else if (level == Level.INFO)
            return IStatus.INFO;
        else
            // debug, trace and custom levels
            return IStatus.OK;
    }

    /**
     * Returns the pluginId under which the messages will be logged.
     * 
     * @return the symbolicName
     */
    public String getSymbolicName() {
        return this.symbolicName;
    }

    /**
     * Sets the symbolicName.
     * 
     * @param symbolicName the symbolicName to set
     */
    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    /**
     * map LoggingEvent to Status code.
     * 
     * @param ev
     * @return
     */
    private int getCode(LoggingEvent ev) {
        return 0;
    }

    private ILog getBundleILog() {
        // get the bundle for a plug-in
        Bundle b = Platform.getBundle(getSymbolicName());
        if (b == null) {
            String m = MessageFormat.format("Plugin: {0} not found in {1}.", new Object[] { getSymbolicName(), //$NON-NLS-1$
                    this.name });
            this.errorHandler.error(m);
            return null;
        }

        return Platform.getLog(b);

    }

    public void close() {
        // nothing to close
        this.closed = true;
    }

    public boolean requiresLayout() {
        return true;
    }

}
