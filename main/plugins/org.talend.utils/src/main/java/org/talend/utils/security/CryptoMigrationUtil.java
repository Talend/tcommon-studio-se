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

import java.util.function.Function;

import org.talend.daikon.security.CryptoHelper;

/*
 * Created by bhe on Aug 13, 2019 This class is a wrapper class for CryptoHelper and is intended to be used by Migration
 * functionality only.
 */
public class CryptoMigrationUtil {

    private CryptoMigrationUtil() {

    }

    /**
     * Create an encrypt function which depends on CryptoHelper.getDefault().encrypt
     */
    public static Function<String, String> encryptFunc() {
        return (src) -> new CryptoHelper(CryptoHelper.PASSPHRASE).encrypt(src);
    }

    /**
     * 
     * Create an decrypt function which depends on CryptoHelper.getDefault().decrypt
     * 
     */
    public static Function<String, String> decryptFunc() {
        return (src) -> new CryptoHelper(CryptoHelper.PASSPHRASE).decrypt(src);
    }

    /**
     * Encrypt input string with CryptoHelper.getDefault().encrypt
     */
    public static String encrypt(String src) {
        return new CryptoHelper(CryptoHelper.PASSPHRASE).encrypt(src);
    }

    /**
     * Decrypt input string with CryptoHelper.getDefault().decrypt
     */
    public static String decrypt(String src) {
        return new CryptoHelper(CryptoHelper.PASSPHRASE).decrypt(src);
    }

    /**
     * Encrypt input string with given password
     */
    public static String encrypt(String pwd, String src) {
        CryptoHelper ch = new CryptoHelper(pwd);
        return ch.encrypt(src);
    }

    /**
     * Decrypt input string with given password
     */
    public static String decrypt(String pwd, String src) {
        CryptoHelper ch = new CryptoHelper(pwd);
        return ch.decrypt(src);
    }
}
