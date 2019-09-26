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
package org.talend.utils.security;

import org.apache.commons.lang.StringUtils;
import org.talend.daikon.security.CryptoHelper;
import org.talend.utils.security.StudioEncryption;

public class PasswordMigrationUtil {

    private final static StudioEncryption SE = StudioEncryption.getStudioEncryption(StudioEncryption.EnryptionKeyName.SYSTEM);

    public static String decryptPassword(String pass) throws Exception {
        String cleanPass = pass;
        if (StringUtils.isNotEmpty(pass)) {
            if (StudioEncryption.hasEncryptionSymbol(pass)) {
                cleanPass = SE.decrypt(pass);
            } else {
                try {
                    cleanPass = new CryptoHelper(CryptoHelper.PASSPHRASE).decrypt(pass);
                } catch (Exception e) {
                    // Ignore here
                }
            }
        }
        return cleanPass;
    }

    public static String encryptPasswordIfNeeded(String pass) throws Exception {
        String cleanPass = decryptPassword(pass);
        return SE.encrypt(cleanPass);
    }

    private PasswordMigrationUtil() {
    }

}
