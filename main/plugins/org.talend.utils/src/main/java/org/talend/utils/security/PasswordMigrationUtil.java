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

/*
 * <p>This class is intended to be used only for migrating old items persisted by studio whose version <=7.3.1</p>
 * <p>Main purpose of this class is to help migrate password field of connection,context,job,joblet. This class is
 * referenced by {@link org.talend.repository.model.migration.UpgradePasswordEncryptionAlg4ItemMigrationTask}</p>
 */
public class PasswordMigrationUtil {

    public static String decryptPassword(String pass) throws Exception {
        String cleanPass = pass;
        if (StringUtils.isNotEmpty(pass)) {
            if (StudioEncryption.hasEncryptionSymbol(pass)) {
                cleanPass = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).decrypt(pass);
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
        if (cleanPass != null) {
            return StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).encrypt(cleanPass);
        }
        return pass;
    }

    private PasswordMigrationUtil() {
    }

}
