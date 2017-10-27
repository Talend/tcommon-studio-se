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
package org.talend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles version numbers such as 1.1.3 or 2.1.3r12345. A product version is given by three numbers: major, minor and
 * micro. The version is given "major.minor.micro".
 */
public class ProductVersion implements Comparable<ProductVersion> {

    private static final Pattern THREE_DIGIT_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+).*");

    private static final Pattern EXTENDED_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?.*");

    private int major;

    private int minor;

    private int micro = 0;

    private boolean setMicro = false;

    /**
     * ProductVersion constructor.
     * 
     * @param major
     * @param minor
     * @param micro
     */
    public ProductVersion(int major, int minor, int micro) {
        super();
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.setMicro = true;
    }

    /**
     * ProductVersion constructor.
     * 
     * @param major
     * @param minor
     * @param micro
     */
    public ProductVersion(int major, int minor) {
        super();
        this.major = major;
        this.minor = minor;
        this.setMicro = false;
    }

    /**
     * Method "fromString".
     * 
     * @param version the version to parse
     * @param extendedVersion true if the version could be a 2 or 3 digit version
     * @return the product version
     */
    public static ProductVersion fromString(String version, boolean extendedVersion) {
        if (!extendedVersion) {
            return fromString(version);
        }
        Matcher matcher = EXTENDED_PATTERN.matcher(version);
        if (matcher.find()) {
            int major = Integer.parseInt(matcher.group(1));
            int minor = Integer.parseInt(matcher.group(2));
            String microStr = matcher.group(3);
            if (microStr != null) {
                int micro = Integer.parseInt(microStr);
                return new ProductVersion(major, minor, micro);
            } else {
                return new ProductVersion(major, minor);
            }
        }
        return null;
    }

    /**
     * Method "fromString".
     * 
     * @param version a version number in the format 1.2.3xx where xx can be anything else
     * @return the product version
     */
    public static ProductVersion fromString(String version) {
        Matcher matcher = THREE_DIGIT_PATTERN.matcher(version);
        if (matcher.matches()) {
            int major = Integer.parseInt(matcher.group(1));
            int minor = Integer.parseInt(matcher.group(2));
            String microStr = matcher.group(3);
            int micro = Integer.parseInt(microStr);
            return new ProductVersion(major, minor, micro);
        }
        return null;
    }

    public int compareTo(ProductVersion other) {
        int diff = major - other.major;
        if (diff != 0) {
            return diff;
        }
        diff = minor - other.minor;
        if (diff != 0) {
            return diff;
        }
        if (setMicro && other.setMicro) {
            diff = micro - other.micro;
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + major;
        result = prime * result + micro;
        result = prime * result + minor;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProductVersion other = (ProductVersion) obj;
        if (major != other.major) {
            return false;
        }
        if (minor != other.minor) {
            return false;
        }
        if (micro != other.micro) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(major);
        stringBuilder.append(".");
        stringBuilder.append(minor);
        if (setMicro) {
            stringBuilder.append(".");
            stringBuilder.append(micro);
        }
        return stringBuilder.toString();
    }

    public int getMajor() {
        return major;
    }

    public int getMicro() {
        return micro;
    }

    public int getMinor() {
        return minor;
    }
}
