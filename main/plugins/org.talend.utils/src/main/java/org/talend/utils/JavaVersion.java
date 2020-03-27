// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Created by bhe on Dec 24, 2019
 */
public class JavaVersion implements Comparable<JavaVersion> {

    private static final Logger LOGGER = Logger.getLogger(JavaVersion.class.getCanonicalName());

    private int major, minor, security;

    public JavaVersion(String v) {
        parseVersion(v);
    }

    @Override
    public int compareTo(JavaVersion o) {
        if (this.major - o.major == 0) {
            if (this.minor - o.minor == 0) {
                return this.security - o.security;
            }
            return this.minor - o.minor;
        }
        return this.major - o.major;
    }

    private void parseVersion(String v) {
        if (v == null || v.isEmpty()) {
            return;
        }
        String[] version = v.split("[\\._]");
        this.major = Integer.parseInt(version[0]);
        if (version.length > 1) {
            try {
                this.minor = Integer.parseInt(version[1]);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Minor version parse error of " + v, e);
            }
        }
        if (version.length > 2) {
            // strip non number part if any
            String securityNumber = version[version.length - 1];
            for (int i = 0; i < securityNumber.length(); i++) {
                char c = securityNumber.charAt(i);
                if (c > '9' || c < '0') {
                    securityNumber = securityNumber.substring(0, i);
                    break;
                }
            }
            try {
                this.security = Integer.parseInt(securityNumber);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Security version parse error of " + v, e);
            }
        }
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = result * prime + this.major;
        result = result * prime + this.minor;
        result = result * prime + this.security;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JavaVersion)) {
            return false;
        }
        JavaVersion that = (JavaVersion) obj;
        return this.compareTo(that) == 0 ? true : false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.major);
        sb.append(".");
        sb.append(this.minor);
        sb.append(".");
        if (this.major == 1) {
            sb.append("0_");
        }
        sb.append(this.security);
        return sb.toString();
    }
}
