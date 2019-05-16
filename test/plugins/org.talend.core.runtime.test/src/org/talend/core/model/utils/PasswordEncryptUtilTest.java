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
// ============================================================================
package org.talend.core.model.utils;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.commons.utils.PasswordEncryptUtil;

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
        Assert.assertEquals("ABBKp4a4zypsW08UouALBw==", encryptPassword);

        String decryptPassword = PasswordEncryptUtil.decryptPassword(encryptPassword);
        Assert.assertEquals(rawStr, decryptPassword);
    }

    @Test
    public void testEncryptPasswordHex() throws Exception {
        Assert.assertNull(PasswordEncryptUtil.encryptPasswordHex(null));
        Assert.assertEquals("3wsOMnbk/woqdy5ZsU6GMg==", PasswordEncryptUtil.encryptPasswordHex(""));
        Assert.assertEquals("DbNaSf740zWs/Wxk9uEQVg==", PasswordEncryptUtil.encryptPasswordHex("Talend"));
        Assert.assertEquals("0VJ8+G+5+0GnM7gdwEg99A==", PasswordEncryptUtil.encryptPasswordHex("toor"));
        Assert.assertEquals("KTndRHnWm9Iej7KMqWJ1fw==", PasswordEncryptUtil.encryptPasswordHex("Talend123"));
    }

    @Test
    public void testIsPasswordType() {
        Assert.assertFalse(PasswordEncryptUtil.isPasswordType(null));
        Assert.assertFalse(PasswordEncryptUtil.isPasswordType(""));
        Assert.assertFalse(PasswordEncryptUtil.isPasswordType("TEST"));
        Assert.assertFalse(PasswordEncryptUtil.isPasswordType("1234"));

        Assert.assertTrue(PasswordEncryptUtil.isPasswordType("Password")); // seems test for perl.
        Assert.assertTrue(PasswordEncryptUtil.isPasswordType("id_Password"));
    }

    @Test
    public void testIsPasswordField() {
        Assert.assertFalse(PasswordEncryptUtil.isPasswordField(null));
        Assert.assertFalse(PasswordEncryptUtil.isPasswordField(""));
        Assert.assertFalse(PasswordEncryptUtil.isPasswordField("TEST"));
        Assert.assertFalse(PasswordEncryptUtil.isPasswordField("1234"));

        Assert.assertTrue(PasswordEncryptUtil.isPasswordField("PASSWORD"));
    }

    @Test
    public void testGetPasswordDisplay() {
        Assert.assertEquals("****", PasswordEncryptUtil.getPasswordDisplay(null));
        Assert.assertEquals("****", PasswordEncryptUtil.getPasswordDisplay(""));

        Assert.assertEquals("*", PasswordEncryptUtil.getPasswordDisplay("1"));
        Assert.assertEquals("*****", PasswordEncryptUtil.getPasswordDisplay("12345"));
        Assert.assertEquals("*******", PasswordEncryptUtil.getPasswordDisplay("ABCD123"));
    }
}
