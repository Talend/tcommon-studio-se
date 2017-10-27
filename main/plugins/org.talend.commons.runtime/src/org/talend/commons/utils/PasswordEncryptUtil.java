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

import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class PasswordEncryptUtil {

    public static String ENCRYPT_KEY = "Encrypt"; //$NON-NLS-1$

    private static String rawKey = "Talend-Key"; //$NON-NLS-1$

    private static SecretKey key = null;

    private static SecureRandom secureRandom = new SecureRandom();

    private static SecretKey getSecretKey() throws Exception {
        if (key == null) {

            byte rawKeyData[] = rawKey.getBytes();
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); //$NON-NLS-1$
            key = keyFactory.generateSecret(dks);
        }
        return key;
    }

    /**
     * 
     * DOC ggu Comment method "encryptPassword".
     * 
     * TDI-30227, should only work for old migration task.
     */
    @Deprecated
    public static String encryptPassword(String input) throws Exception {
        if (input == null) {
            return input;
        }

        SecretKey key = getSecretKey();
        Cipher c = Cipher.getInstance("DES"); //$NON-NLS-1$
        c.init(Cipher.ENCRYPT_MODE, key, secureRandom);
        byte[] cipherByte = c.doFinal(input.getBytes());
        String dec = new String(Base64.encodeBase64(cipherByte));
        return dec;
    }

    /**
     * 
     * DOC ggu Comment method "encryptPassword".
     * 
     * TDI-30227, should only work for old migration task.
     */
    @Deprecated
    public static String decryptPassword(String input) throws Exception, BadPaddingException {
        if (input == null || input.length() == 0) {
            return input;
        }
        byte[] dec = Base64.decodeBase64(input.getBytes());
        SecretKey key = getSecretKey();
        Cipher c = Cipher.getInstance("DES"); //$NON-NLS-1$
        c.init(Cipher.DECRYPT_MODE, key, secureRandom);
        byte[] clearByte = c.doFinal(dec);
        return new String(clearByte);
    }

    private static SecretKey passwordKey = null;

    private static String CHARSET = "UTF-8";

    private static SecretKey getSecretKeyUTF8() throws Exception {
        if (passwordKey == null) {
            byte rawKeyData[] = rawKey.getBytes(CHARSET);
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); //$NON-NLS-1$
            passwordKey = keyFactory.generateSecret(dks);
        }
        return passwordKey;
    }

    /**
     * Work for codegen only. and must be same as the routine
     * "routines.system.PasswordEncryptUtil.encryptPassword(input)".
     * 
     */
    public static String encryptPasswordHex(String input) throws Exception {
        if (input == null) {
            return input;
        }
        SecretKey key = getSecretKeyUTF8();
        Cipher c = Cipher.getInstance("DES"); //$NON-NLS-1$
        c.init(Cipher.ENCRYPT_MODE, key, secureRandom);
        byte[] cipherByte = c.doFinal(input.getBytes(CHARSET));
        String dec = Hex.encodeHexString(cipherByte);
        return dec;
    }

    /**
     * 
     * DOC ggu Comment method "isPasswordType".
     * 
     * should match with JavaTypesManager.PASSWORD.getId()
     * 
     * @param type
     * @return
     */
    public static boolean isPasswordType(String type) {
        return "Password".equals(type) || "id_Password".equals(type); //$NON-NLS-1$   //$NON-NLS-2$
    }

    /**
     * 
     * DOC ggu Comment method "isPasswordField".
     * 
     * should match with the EParameterFieldType.PASSWORD
     * 
     * @param field
     * @return
     */
    public static boolean isPasswordField(String field) {
        return "PASSWORD".equals(field); //$NON-NLS-1$   
    }

    /**
     * 
     * DOC ggu Comment method "getPasswordDisplay".
     * 
     * will be * to dispaly always.
     * 
     * @param value
     * @return
     */
    public static String getPasswordDisplay(String value) {
        if (value == null || value.length() == 0) {
            // always display 4 start characters.
            return "****"; //$NON-NLS-1$
        } else {
            return value.replaceAll(".", "*"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

}
