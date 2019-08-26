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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StudioEncryptionTest {

    private String input1 = "Talend";

    private String input2 = "123456";

    private String input3 = "Talend_123456";

    @Test
    public void testDecryptPassword() throws Exception {
        assertNotEquals(input1, StudioEncryption.encryptPassword(input1));
        assertEquals(input1, StudioEncryption.decryptPassword(StudioEncryption.encryptPassword(input1)));

        assertNotEquals(input2, StudioEncryption.encryptPassword(input2));
        assertEquals(input2, StudioEncryption.decryptPassword(StudioEncryption.encryptPassword(input2)));

        assertNotEquals(input3, StudioEncryption.encryptPassword(input3));
        assertEquals(input3, StudioEncryption.decryptPassword(StudioEncryption.encryptPassword(input3)));
    }

    @Test
    public void testDecryptPasswordUseKey() throws Exception {
        String key = "1234567890123456";

        assertNotEquals(input1, StudioEncryption.encryptPassword(input1, key));
        assertEquals(input1, StudioEncryption.decryptPassword(StudioEncryption.encryptPassword(input1, key), key));

        assertNotEquals(input2, StudioEncryption.encryptPassword(input2, key));
        assertEquals(input2, StudioEncryption.decryptPassword(StudioEncryption.encryptPassword(input2, key), key));

        assertNotEquals(input3, StudioEncryption.encryptPassword(input3, key));
        assertEquals(input3, StudioEncryption.decryptPassword(StudioEncryption.encryptPassword(input3, key), key));
    }

    @Test
    public void testAESEncrypt() throws Exception {
        assertNotEquals(input1, StudioEncryption.getStudioEncryption(null).encrypt(input1));
        assertEquals(input1,
                StudioEncryption.getStudioEncryption(null).decrypt(StudioEncryption.getStudioEncryption(null).encrypt(input1)));

        assertNotEquals(input2, StudioEncryption.getStudioEncryption(null).encrypt(input2));
        assertEquals(input2,
                StudioEncryption.getStudioEncryption(null).decrypt(StudioEncryption.getStudioEncryption(null).encrypt(input2)));

        assertNotEquals(input3, StudioEncryption.getStudioEncryption(null).encrypt(input3));
        assertEquals(input3,
                StudioEncryption.getStudioEncryption(null).decrypt(StudioEncryption.getStudioEncryption(null).encrypt(input3)));

        // ensure negative case
        assertEquals(null, StudioEncryption.getStudioEncryption(null).encrypt(null));
    }

    @Test
    public void testAESEncode() throws Exception {

        assertEquals(input1, new String(StudioEncryption.decode64(StudioEncryption.encode64(input1.getBytes()))));

        assertEquals(input2, new String(StudioEncryption.decode64(StudioEncryption.encode64(input2.getBytes("UTF-8"))), "UTF-8"));
    }

    @Test
    public void testGetStudioEncryption() throws Exception {

        assertNotNull(StudioEncryption.getStudioEncryption(null));

    }
}
