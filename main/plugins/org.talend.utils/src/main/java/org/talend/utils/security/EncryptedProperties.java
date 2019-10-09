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

import java.util.Properties;

import org.talend.utils.security.StudioEncryption;


/**
 * DOC hwang class global comment. Detailled comment
 */
public class EncryptedProperties extends Properties {

    public String getProperty(String key) {
        try {
            return StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM)
                    .decrypt(super.getProperty(key));
        } catch (Exception e) {
            throw new RuntimeException("Couldn't decrypt property");
        }
    }

    public synchronized Object setProperty(String key, String value) {
        try {
            return super.setProperty(key,
                    StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).encrypt(value));
        } catch (Exception e) {
            throw new RuntimeException("Couldn't encrypt property");
        }
    }

}
