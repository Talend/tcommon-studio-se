package org.talend.utils.security;

// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import org.junit.Test;

public class AESEncryptionTest {

    private String input1 = "Talend";

    private String input2 = "123456";

    private String input3 = "Talend_123456";

    @Test
    public void testDecryptPassword() throws Exception {
        assertNotEquals(input1, AESEncryption.encryptPassword(input1));
        assertEquals(input1, AESEncryption.decryptPassword(AESEncryption.encryptPassword(input1)));

        assertNotEquals(input2, AESEncryption.encryptPassword(input2));
        assertEquals(input2, AESEncryption.decryptPassword(AESEncryption.encryptPassword(input2)));

        assertNotEquals(input3, AESEncryption.encryptPassword(input3));
        assertEquals(input3, AESEncryption.decryptPassword(AESEncryption.encryptPassword(input3)));
    }

    @Test
    public void testDecryptPasswordUseKey() throws Exception {
        String key = "1234567890123456";

        assertNotEquals(input1, AESEncryption.encryptPassword(input1, key));
        assertEquals(input1, AESEncryption.decryptPassword(AESEncryption.encryptPassword(input1, key), key));

        assertNotEquals(input2, AESEncryption.encryptPassword(input2, key));
        assertEquals(input2, AESEncryption.decryptPassword(AESEncryption.encryptPassword(input2, key), key));

        assertNotEquals(input3, AESEncryption.encryptPassword(input3, key));
        assertEquals(input3, AESEncryption.decryptPassword(AESEncryption.encryptPassword(input3, key), key));
    }
}
