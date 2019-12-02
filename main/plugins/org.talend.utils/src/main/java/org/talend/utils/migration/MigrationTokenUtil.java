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
package org.talend.utils.migration;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.talend.utils.security.StudioEncryption;

public final class MigrationTokenUtil {

    private MigrationTokenUtil() {
    	// complete
    }
    
    /**
     * Return a map containing the decrypted migration token time
     * @param value the encrypted migration token time
     * @return a map containing the decrypted migration token time
     */
    public static Map<String, Date> getMigrationTokenTime(String value) {
        if (!(value == null || value.isEmpty())) {
            try {
                String dec = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN).decrypt(value);
                if (!(dec == null || dec.isEmpty())) {
                    String[] split = dec.split("_");

                    if (split.length == 2) {
                        String name = split[0];
                        long time = Long.parseLong(split[1]);
                        Map<String, Date> map = new HashMap<String, Date>();
                        map.put(name, new Date(time));
                        return map;
                    }
                }
            } catch (Throwable e) {
                return Collections.emptyMap();
            }
        }

        return Collections.emptyMap();
    }

}
