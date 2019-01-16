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

/**
 * for the sake of not refectoring all the classes before the release I have just inherited the daikon class. This
 * should be cleanup later.
 * 
 * A class that helps to encrypt and decrypt strings.
 */
public class CryptoHelper extends org.talend.daikon.security.CryptoHelper {

    /**
     * constructor with a passPhrase
     * 
     * @param passPhrase
     */
    public CryptoHelper(String passPhrase) {
        super(passPhrase);
    }

    /*
     * FIXME, TDI-31303
     * 
     * Sometimes, even right encryption value, can't be be decrypted. And throw one exception:
     * "javax.crypto.BadPaddingException: Given final block not properly padded", then return null.
     * 
     * Maybe, caused by the same problem with AES (TDI-31032).
     * 
     * Finally, try to use new instance for each one.
     */
    // public static final CryptoHelper DEFAULT = new CryptoHelper(PASSPHRASE);

    public static final CryptoHelper getDefault() {
        return new CryptoHelper(PASSPHRASE);
    }
}
