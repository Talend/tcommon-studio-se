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
package org.talend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles version numbers such as 1.1.3 or 2.1.3r12345. A product version is given by three numbers: major, minor and
 * micro. The version is given "major.minor.micro".
 */
public class ProductVersion implements Comparable<ProductVersion> {

    private static final Pattern THREE_DIGIT_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+).*"); //$NON-NLS-1$

    private static final Pattern EXTENDED_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?.*"); //$NON-NLS-1$

    private static final Pattern FOUR_PART_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)\\.(.*)"); //$NON-NLS-1$

    private int major;

    private int minor;

    private int micro = 0;

    private boolean setMicro = false;

    private String extraInfo;

    private boolean setExtraInfo = false;

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
     * @param extraInfo
     */
    public ProductVersion(int major, int minor, int micro, String extraInfo) {
        this(major, minor, micro);
        this.extraInfo = extraInfo;
        this.setExtraInfo = true;
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
     * this ProductVersion constructor generate a ProductVersion with versionDate.
     * 
     * @param ProductVersion
     * @param Date
     */
    public ProductVersion(ProductVersion productVersion, Date versionDate) {
        this(productVersion.getMajor(), productVersion.getMinor(), productVersion.getMicro(),
                new SimpleDateFormat("yyyyMMdd").format(versionDate)); //$NON-NLS-1$
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
     * DOC msjian Comment method "fromString".
     * 
     * @param version the version to parse
     * @param extendedVersion true if the version could be a 2 or 3 digit version
     * @param isFourPartVersion true if the version contains 4 part like: 7.3.1.20200507 or 7.3.1.20200417_1111-patch
     * @return the product version
     */
    public static ProductVersion fromString(String version, boolean extendedVersion, boolean isFourPartVersion) {
        if (isFourPartVersion) {
            Matcher matcher4 = FOUR_PART_PATTERN.matcher(version);
            if (matcher4.find() && matcher4.groupCount() == 4) {
                int major = Integer.parseInt(matcher4.group(1));
                int minor = Integer.parseInt(matcher4.group(2));
                String microStr = matcher4.group(3);
                String extraInfo = matcher4.group(4);
                int micro = Integer.parseInt(microStr);
                return new ProductVersion(major, minor, micro, extraInfo);
            }
        }
        return fromString(version, extendedVersion);
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
        if (setExtraInfo) {
            if (other.setExtraInfo) {
                return extraInfo.compareTo(other.extraInfo);
            } else {
                return 1;
            }
        } else {
            if (other.setExtraInfo) {
                return -1;
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
        if (setExtraInfo) {
            result = prime * result + extraInfo.length();
        }
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
        if (setExtraInfo && other.setExtraInfo) {
            if (!extraInfo.equals(other.extraInfo)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(major);
        stringBuilder.append("."); //$NON-NLS-1$
        stringBuilder.append(minor);
        if (setMicro) {
            stringBuilder.append("."); //$NON-NLS-1$
            stringBuilder.append(micro);
        }
        if (setExtraInfo) {
            stringBuilder.append("."); //$NON-NLS-1$
            stringBuilder.append(extraInfo);
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

    public boolean isSetMicro() {
        return setMicro;
    }

    public String getExtraInfo() {
        return this.extraInfo;
    }

    public boolean isSetExtraInfo() {
        return setExtraInfo;
    }
}
