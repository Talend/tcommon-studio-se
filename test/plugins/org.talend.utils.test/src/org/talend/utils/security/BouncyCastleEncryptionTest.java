package org.talend.utils.security;

import static org.junit.Assert.assertNotEquals;

import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import junit.framework.TestCase;

public class BouncyCastleEncryptionTest extends TestCase {

    public void testEncyptAndDecrypt() throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        String sampeText = "this is secret";

        String encrypted = BouncyCastleEncryption.getInstance().encrypt(sampeText);
        
        assertNotEquals(sampeText, encrypted);

        String decrypted = BouncyCastleEncryption.getInstance().decrypt(encrypted);

        assertEquals(sampeText, decrypted);

    }

}
