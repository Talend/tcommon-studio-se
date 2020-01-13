package org.talend.utils.security;

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
// ============================================================================import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.junit.Test;

public class StudioEncryptionTest {

    private String input1 = "Talend";

    private String input2 = "123456";

    private StudioEncryption se = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.ROUTINE);

    @Test
    public void testDecryptPassword() throws Exception {
        String encrypted = se.encrypt(input1);
        // should match
        assertTrue(StudioEncryption.hasEncryptionSymbol(encrypted));
        assertNotEquals(input1, encrypted);
        assertEquals(input1, se.decrypt(encrypted));
    }

    @Test
    public void testAESEncrypt() throws Exception {

        // always encrypt data by highest version of system key
        String encrypted = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).encrypt(input1);
        assertNotEquals(input1, encrypted);
        // should match
        assertTrue(StudioEncryption.hasEncryptionSymbol(encrypted));
        assertTrue(encrypted.startsWith("enc:" + StudioEncryption.EncryptionKeyName.SYSTEM.toString()));
        assertEquals(input1,
                StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).decrypt(encrypted));

        encrypted = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN).encrypt(input2);
        assertNotEquals(input2, encrypted);

        // should match
        assertTrue(StudioEncryption.hasEncryptionSymbol(encrypted));
        assertTrue(encrypted.startsWith("enc:" + StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN.toString()));
        assertEquals(input2,
                StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN).decrypt(encrypted));

        assertEquals(null, StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).encrypt(null));
        assertEquals(encrypted,
                StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).encrypt(encrypted));
        assertEquals(input1, StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).decrypt(input1));
    }

    @Test
    public void testGetStudioEncryption() throws Exception {

        assertNotNull(StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM));

    }

    @Test
    public void testGenerateEncryptionKeys() throws Exception {
        File keyFile = File.createTempFile("StudioEncryptionTest", "testGenerateEncryptionKeys");

        // empty file, nothing to generate
        boolean gen = StudioEncryption.generateEncryptionKeys(keyFile);
        assertFalse(gen);

        // add two encryption keys, values exist, so nothing to generate
        Properties p = new Properties();
        p.put(StudioKeySource.KEY_SYSTEM_PREFIX + "1", "ObIr3Je6QcJuxJEwErWaFWIxBzEjxIlBrtCPilSByJI\\=");
        p.put(StudioKeySource.KEY_ROUTINE_PREFIX + "1", "ObIr3Je6QcJuxJEwErWaFWIxBzEjxIlBrtCPilSByJI\\=");
        try (FileOutputStream fo = new FileOutputStream(keyFile)) {
            p.store(fo, "");
        }

        gen = StudioEncryption.generateEncryptionKeys(keyFile);
        assertFalse(gen);

        // add two empty encryption keys, need to generate two keys
        p.put(StudioKeySource.KEY_SYSTEM_PREFIX + "2", "");
        p.put(StudioKeySource.KEY_ROUTINE_PREFIX + "2", "");
        try (FileOutputStream fo = new FileOutputStream(keyFile)) {
            p.store(fo, "");
        }

        gen = StudioEncryption.generateEncryptionKeys(keyFile);
        assertTrue(gen);

        p.clear();
        try (FileInputStream fi = new FileInputStream(keyFile)) {
            p.load(fi);
        }

        assertTrue(p.getProperty(StudioKeySource.KEY_SYSTEM_PREFIX + "2").length() > 0);
        assertTrue(p.getProperty(StudioKeySource.KEY_ROUTINE_PREFIX + "2").length() > 0);

        assertEquals(p.getProperty(StudioKeySource.KEY_SYSTEM_PREFIX + "1"), "ObIr3Je6QcJuxJEwErWaFWIxBzEjxIlBrtCPilSByJI\\=");
        assertEquals(p.getProperty(StudioKeySource.KEY_ROUTINE_PREFIX + "1"), "ObIr3Je6QcJuxJEwErWaFWIxBzEjxIlBrtCPilSByJI\\=");

        keyFile.delete();
    }
}
