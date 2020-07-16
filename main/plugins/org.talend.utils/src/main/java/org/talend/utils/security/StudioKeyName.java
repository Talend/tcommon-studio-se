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
package org.talend.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Created by bhe on Jan 17, 2020
 */
public class StudioKeyName {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudioKeySource.class);

    public static final String KEY_SYSTEM_PREFIX = "system.encryption.key.v";

    public static final String KEY_ROUTINE_PREFIX = "routine.encryption.key.v";

    public static final String KEY_SYSTEM_DEFAULT = KEY_SYSTEM_PREFIX + "1";

    public static final String KEY_ROUTINE = KEY_ROUTINE_PREFIX + "1";

    public static final String KEY_MIGRATION_TOKEN = "migration.token.encryption.key";

    public static final String KEY_MIGRATION = "migration.encryption.key";

    private final String keyName;

    /**
     * Studio encryption key class
     * 
     * @param keyName Encryption key name
     */
    public StudioKeyName(String keyName) {
        this.keyName = keyName;
        if (!isValid()) {
            throw new IllegalArgumentException("Invalid key name: " + keyName);
        }
    }

    /**
     * Check whether the encryption key name is supported
     */
    private boolean isValid() {
        if (this.keyName == null || this.keyName.isEmpty()) {
            return false;
        }

        if (this.keyName.equals(KEY_MIGRATION_TOKEN) || this.keyName.equals(KEY_MIGRATION)) {
            return true;
        }

        // not routine or system encryption key
        if (!this.isRoutineKey() && !this.isSystemKey()) {
            return false;
        }

        return getVersionNumber() > -1 ? true : false;
    }

    /**
     * Get the version number of the encryption key, 0 will be returned if there is no version number.
     */
    public int getVersionNumber() {
        if ((this.isRoutineKey() || this.isSystemKey()) && this.keyName.length() > getKeyNamePrefix().length()) {
            int idx = this.keyName.lastIndexOf('.');
            try {
                return Integer.parseInt(this.keyName.substring(idx + 2));
            } catch (NumberFormatException e) {
                LOGGER.warn("Parse version of encryption key error, key: " + this.keyName);
                return -1;
            }
        }
        return 0;
    }

    /**
     * Get encryption key name
     */
    public String getKeyName() {
        return this.keyName;
    }

    /**
     * Check whether the encryption key is started with "routine.encryption.key.v"
     */
    public boolean isRoutineKey() {
        return this.keyName.startsWith(KEY_ROUTINE_PREFIX);
    }

    /**
     * Check whether the encryption key is started with "system.encryption.key.v"
     */
    public boolean isSystemKey() {
        return this.keyName.startsWith(KEY_SYSTEM_PREFIX);
    }

    /**
     * Check whether it is the default routine key
     */
    public boolean isDefaultRoutineKey() {
        return this.keyName.equals(KEY_ROUTINE);
    }

    /**
     * Get key name prefix if any
     */
    public String getKeyNamePrefix() {
        if (this.isSystemKey()) {
            return KEY_SYSTEM_PREFIX;
        } else if (this.isRoutineKey()) {
            return KEY_ROUTINE_PREFIX;
        }
        return "";
    }

    public int hashCode() {
        return this.keyName.hashCode();
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        if (!(that instanceof StudioKeyName)) {
            return false;
        }
        StudioKeyName thatObj = (StudioKeyName) that;
        return this.keyName.equals(thatObj.getKeyName());
    }

    public String toString() {
        return this.keyName;
    }
}
