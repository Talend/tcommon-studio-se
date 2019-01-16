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
package org.talend.utils.security;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * created by ggu on Aug 20, 2014 Detailled comment
 *
 */
@SuppressWarnings("nls")
public class CryptoHelperTest {

    @Test
    public void testEncrypt4Null() {
        String encrypt = CryptoHelper.getDefault().encrypt(null);
        assertNull(encrypt);
    }

    @Test
    public void testEncrypt4EMPTY() {
        String encrypt = CryptoHelper.getDefault().encrypt("");
        assertNotNull(encrypt);
        assertEquals("yJKHKGWEAQw=", encrypt);
    }

    @Test
    public void testDecrypt4Null() {
        String encrypt = CryptoHelper.getDefault().decrypt(null);
        assertNull(encrypt);
    }

    @Test
    public void testDecrypt4EMPTY() {
        String encrypt = CryptoHelper.getDefault().decrypt("");
        assertNotNull(encrypt);
        assertEquals("", encrypt);
    }

    @Test
    public void testSpace() {
        String encrypt = CryptoHelper.getDefault().encrypt(" ");
        assertNotNull(encrypt);
        assertEquals("4n6Q8vByOrg=", encrypt);

        String decrypt = CryptoHelper.getDefault().decrypt(encrypt);
        assertNotNull(decrypt);
        assertEquals(" ", decrypt);
    }

    @Test
    public void testNumber() {
        String encrypt = CryptoHelper.getDefault().encrypt("123");
        assertNotNull(encrypt);
        assertEquals("/81ashGeQx8=", encrypt);

        String decrypt = CryptoHelper.getDefault().decrypt(encrypt);
        assertNotNull(decrypt);
        assertEquals("123", decrypt);
    }

    @Test
    public void testCharacters() {
        String encrypt = CryptoHelper.getDefault().encrypt("ABC 123");
        assertNotNull(encrypt);
        assertEquals("l/R1qjFcRGA=", encrypt);

        String decrypt = CryptoHelper.getDefault().decrypt(encrypt);
        assertNotNull(decrypt);
        assertEquals("ABC 123", decrypt);
    }

    @Test
    public void testMoreCharacters() {
        String encrypt = CryptoHelper.getDefault().encrypt("ABC 123 !@#$%%^+_ QETUVNK>?><");
        assertNotNull(encrypt);
        assertEquals("eVC4UR2126US+sO9OaetY+1lXzwYmFd8OG4BkrrOXuk=", encrypt);

        String decrypt = CryptoHelper.getDefault().decrypt(encrypt);
        assertNotNull(decrypt);
        assertEquals("ABC 123 !@#$%%^+_ QETUVNK>?><", decrypt);
    }

}
