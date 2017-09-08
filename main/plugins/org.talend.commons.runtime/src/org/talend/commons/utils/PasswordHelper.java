// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * This class helps in encrypting and decrypting passwords.
 */
public final class PasswordHelper {

    private static final String ALGORITHM = "MD5"; //$NON-NLS-1$

    private PasswordHelper() {
    }

    public static synchronized boolean verifyPassword(byte[] encryptedPassword, byte[] encryptedPassword2)
            throws NoSuchAlgorithmException {
        return MessageDigest.isEqual(encryptedPassword, encryptedPassword2);
    }

    public static synchronized byte[] encryptPasswd(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
        return messageDigest.digest(password.getBytes());
    }

    public static synchronized String encryptPasswd64(String password) throws NoSuchAlgorithmException {
        return new String(Base64.encodeBase64(encryptPasswd(password)));
    }

    public static synchronized boolean verifyPassword64(String encryptedPassword, String encryptedPassword2)
            throws NoSuchAlgorithmException {
        return verifyPassword(Base64.decodeBase64(encryptedPassword.getBytes()), Base64.decodeBase64(encryptedPassword2
                .getBytes()));
    }
}
