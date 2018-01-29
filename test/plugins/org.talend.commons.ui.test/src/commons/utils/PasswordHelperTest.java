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
package commons.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.talend.commons.utils.PasswordHelper;

/**
 * class global comment. Detailled comment. <br/>
 * 
 */
public class PasswordHelperTest {

    @Test
    public void testVerifyPassword() {
        String passwd = "good passwd"; //$NON-NLS-1$
        String passwd2 = "false passwd"; //$NON-NLS-1$
        try {
            byte[] bs1 = PasswordHelper.encryptPasswd(passwd);
            byte[] bs2 = PasswordHelper.encryptPasswd(passwd2);
            assertTrue(PasswordHelper.verifyPassword(bs1, bs1));
            assertFalse(PasswordHelper.verifyPassword(bs1, bs2));
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }
}
