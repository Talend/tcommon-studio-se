// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.utils.security.StudioEncryption;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class PasswordEncryptUtilTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testEncryptPassword() throws Exception {
        String rawStr = "Talend123";
        String encryptPassword = PasswordEncryptUtil.encryptPassword(rawStr);
        assertEquals("ABBKp4a4zypsW08UouALBw==", encryptPassword);

        String decryptPassword = PasswordEncryptUtil.decryptPassword(encryptPassword);
        assertEquals(rawStr, decryptPassword);
    }

    @Test
    public void testEncryptPasswordHex() throws Exception {
        StudioEncryption se = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.ROUTINE);
        assertNull(PasswordEncryptUtil.encryptPasswordHex(null));
        assertEquals("",
                se.decrypt(PasswordEncryptUtil.encryptPasswordHex("")));
        assertEquals("Talend",
                se.decrypt(PasswordEncryptUtil.encryptPasswordHex("Talend")));
        assertEquals("toor", se.decrypt(PasswordEncryptUtil.encryptPasswordHex("toor")));
        assertEquals("Talend123", se.decrypt(PasswordEncryptUtil.encryptPasswordHex("Talend123")));
    }

    @Test
    public void testIsPasswordType() {
        assertFalse(PasswordEncryptUtil.isPasswordType(null));
        assertFalse(PasswordEncryptUtil.isPasswordType(""));
        assertFalse(PasswordEncryptUtil.isPasswordType("TEST"));
        assertFalse(PasswordEncryptUtil.isPasswordType("1234"));

        assertTrue(PasswordEncryptUtil.isPasswordType("Password")); // seems test for perl.
        assertTrue(PasswordEncryptUtil.isPasswordType("id_Password"));
    }

    @Test
    public void testIsPasswordField() {
        assertFalse(PasswordEncryptUtil.isPasswordField(null));
        assertFalse(PasswordEncryptUtil.isPasswordField(""));
        assertFalse(PasswordEncryptUtil.isPasswordField("TEST"));
        assertFalse(PasswordEncryptUtil.isPasswordField("1234"));

        assertTrue(PasswordEncryptUtil.isPasswordField("PASSWORD"));
    }

    @Test
    public void testGetPasswordDisplay() {
        assertEquals("****", PasswordEncryptUtil.getPasswordDisplay(null));
        assertEquals("****", PasswordEncryptUtil.getPasswordDisplay(""));

        assertEquals("*", PasswordEncryptUtil.getPasswordDisplay("1"));
        assertEquals("*****", PasswordEncryptUtil.getPasswordDisplay("12345"));
        assertEquals("*******", PasswordEncryptUtil.getPasswordDisplay("ABCD123"));
    }

    @Test
    public void testDecryptPassword() {
        String encryptPassword1 = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.ROUTINE).encrypt("");
        assertEquals("", decryptPassword(encryptPassword1));
        String encryptPassword2 = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.ROUTINE)
                .encrypt("Talend123");
        assertEquals("Talend123", decryptPassword(encryptPassword2));

        String decryptPassword1 = "";
        assertEquals(decryptPassword1, decryptPassword(decryptPassword1));

        String decryptPassword2 = "Talend123";
        assertEquals(decryptPassword2, decryptPassword(decryptPassword2));

        String decryptPassword3 = " ";
        assertEquals(decryptPassword3, decryptPassword(decryptPassword3));

    }

    // This method copy from routines.system.PasswordEncryptUtil, to make sure the decryptPassword work well
    private String decryptPassword(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        if (StudioEncryption.hasEncryptionSymbol(input)) {

            StudioEncryption se = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.ROUTINE);
            return se.decrypt(input);
        }

        return input;
    }
}
