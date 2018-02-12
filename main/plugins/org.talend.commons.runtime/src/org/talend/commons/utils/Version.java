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
package org.talend.commons.utils;

import java.util.StringTokenizer;

import org.talend.commons.i18n.internal.Messages;

/**
 * 
 * @author smallet
 * 
 */
public class Version implements Comparable<Version> {

    public static final String LAST_VERSION_NAME = "lastVersion"; //$NON-NLS-1$

    public static final Version LAST_VERSION = new Version(-1, -1);

    private static final String LEVEL_SEPARATOR = "."; //$NON-NLS-1$

    private int major = 0;

    private int minor = 1;

    /**
     * Version initialized constructor.
     * 
     * @param major
     * @param minor
     */
    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public Version(String version) {
        this(version, false);
    }

    public Version(String version, boolean allowedLastVersion) {
        if (allowedLastVersion && LAST_VERSION_NAME.equals(version)) {
            this.major = LAST_VERSION.getMajor();
            this.minor = LAST_VERSION.getMinor();
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(version, LEVEL_SEPARATOR);
            try {
                this.major = Integer.parseInt(stringTokenizer.nextToken());
                this.minor = Integer.parseInt(stringTokenizer.nextToken());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(Messages.getString("VersionUtils.Version.Error2", version), e); //$NON-NLS-1$
            }
        }
    }

    public void upMajor() {
        this.major++;
        this.minor = 0;
    }

    public void upMinor() {
        this.minor++;
    }

    /**
     * Getter for major.
     * 
     * @return the major
     */
    public int getMajor() {
        return this.major;
    }

    /**
     * Getter for minor.
     * 
     * @return the minor
     */
    public int getMinor() {
        return this.minor;
    }

    @Override
    public String toString() {
        return major + LEVEL_SEPARATOR + minor;
    }

    public int compareTo(Version arg0) {
        if (this.equals(arg0)) {
            return 0;
        }

        if (this.getMajor() == arg0.getMajor()) {
            Integer m1 = this.getMinor();
            Integer m2 = arg0.getMinor();
            return m1.compareTo(m2);
        } else {
            Integer m1 = this.getMajor();
            Integer m2 = arg0.getMajor();
            return m1.compareTo(m2);
        }
    }

}
