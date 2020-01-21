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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.Test;

/*
 * Created by bhe on Nov 4, 2019
 */
public class StudioKeySourceTest {

    @Test
    public void testLoadAllKeys() {
        /*************************************
         * load system default keys
         *************************************/
        Map<StudioKeyName, String> p = StudioKeySource.loadAllKeys();
        assertNotNull(p);
        assertTrue(p.keySet().size() > 1);
        p.keySet().forEach((k) -> {
            assertTrue("default keys: " + k,
                    k.getKeyName().startsWith(StudioKeyName.KEY_SYSTEM_PREFIX)
                            || k.getKeyName().equals(StudioEncryption.EncryptionKeyName.MIGRATION.toString())
                            || k.getKeyName().equals(StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN.toString())
                            || k.getKeyName().equals(StudioEncryption.EncryptionKeyName.ROUTINE.toString()));
        });
    }

    @Test
    public void testGetKeyName() throws Exception {
        Map<StudioKeyName, String> p = generateKeys();

        /*************************************
         * key for encryption
         *************************************/
        StudioKeySource ks = StudioKeySource.key(p, StudioKeyName.KEY_SYSTEM_PREFIX, true);
        assertEquals("highest version of system encryption key name not equal", StudioKeyName.KEY_SYSTEM_PREFIX + 3,
                ks.getKeyName().getKeyName());

        assertNotNull(ks.getKey());

        // input default system key name
        ks = StudioKeySource.key(p, StudioEncryption.EncryptionKeyName.SYSTEM.toString(), true);
        assertEquals("highest version of system encryption key name not equal", StudioKeyName.KEY_SYSTEM_PREFIX + 3,
                ks.getKeyName().getKeyName());

        assertNotNull(ks.getKey());

        // input default migration token key name
        ks = StudioKeySource.key(p, StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN.toString(), true);
        assertEquals("migration token key name not equal", StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN.toString(),
                ks.getKeyName().getKeyName());

        assertNotNull(ks.getKey());

        // input default routine key name
        ks = StudioKeySource.key(p, StudioEncryption.EncryptionKeyName.ROUTINE.toString(), true);
        assertEquals("routine key name not equal", StudioEncryption.EncryptionKeyName.ROUTINE.toString(),
                ks.getKeyName().getKeyName());

        assertNotNull(ks.getKey());

        /*************************************
         * key for decryption
         *************************************/
        for (int i = 1; i < 4; i++) {
            // input system key name
            ks = StudioKeySource.key(p, StudioKeyName.KEY_SYSTEM_PREFIX + i, false);
            assertEquals("system decryption key name not equal", StudioKeyName.KEY_SYSTEM_PREFIX + i,
                    ks.getKeyName().getKeyName());

            assertNotNull(ks.getKey());
        }

        // input default migration token key name
        ks = StudioKeySource.key(p, StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN.toString(), false);
        assertEquals("migration token key name not equal", StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN.toString(),
                ks.getKeyName().getKeyName());

        assertNotNull(ks.getKey());

        // input default routine key name
        ks = StudioKeySource.key(p, StudioEncryption.EncryptionKeyName.ROUTINE.toString(), false);
        assertEquals("routine key name not equal", StudioEncryption.EncryptionKeyName.ROUTINE.toString(),
                ks.getKeyName().getKeyName());

        assertNotNull(ks.getKey());
    }

    @Test
    public void testMigrationEncryptionKey() throws Exception {
        Map<StudioKeyName, String> p = StudioKeySource.loadAllKeys();
        StudioKeySource ks = StudioKeySource.key(p, StudioEncryption.EncryptionKeyName.MIGRATION.toString(), true);
        assertEquals("migration key name not equal", StudioEncryption.EncryptionKeyName.MIGRATION.toString(),
                ks.getKeyName().getKeyName());
        assertNotNull(ks.getKey());
        assertEquals("Talend-Key", new String(ks.getKey(), StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadDefaultKeys() throws Exception {
        Properties p = StudioKeySource.loadDefaultKeys();
        assertNotNull(p);
        assertFalse(p.getProperty(StudioKeyName.KEY_SYSTEM_DEFAULT).isEmpty());
        assertFalse(p.getProperty(StudioKeyName.KEY_ROUTINE).isEmpty());
        assertFalse(p.getProperty(StudioKeyName.KEY_MIGRATION_TOKEN).isEmpty());
        assertFalse(p.getProperty(StudioKeyName.KEY_MIGRATION).isEmpty());
    }

    public static Map<StudioKeyName, String> generateKeys() throws NoSuchAlgorithmException {
        Properties p = new Properties();

        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);

        SecretKey key = kg.generateKey();
        // create routine key
        p.put(StudioEncryption.EncryptionKeyName.ROUTINE.toString(), Base64.getEncoder().encodeToString(key.getEncoded()));

        key = kg.generateKey();
        // create migration token key
        p.put(StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN.toString(),
                Base64.getEncoder().encodeToString(key.getEncoded()));

        // create system encryption key
        for (int i = 1; i < 4; i++) {
            key = kg.generateKey();
            // create migration token key
            p.put(StudioKeyName.KEY_SYSTEM_PREFIX + i, Base64.getEncoder().encodeToString(key.getEncoded()));
        }

        Map<StudioKeyName, String> retMap = new HashMap<StudioKeyName, String>();
        // construct key name
        p.forEach((k, v) -> {
            retMap.put(new StudioKeyName(String.valueOf(k)), String.valueOf(v));
        });

        return retMap;
    }
}
