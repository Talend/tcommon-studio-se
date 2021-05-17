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
package org.talend.utils.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.talend.utils.security.StudioEncryption;

public class MigrationTokenUtilTest {

    private static final String PRODUCTDATE_HELPER_CLASS_NAME = "org.talend.core.tis.data.ProductDateHelper"; //$NON-NLS-1$

    /*
     * the value always rely on PRODUCTDATE_HELPER_CLASS_NAME
     */
    private static final String PRODUCT_DATEHELPER_MD5_VALUE = "4d3a8096c36da901b056889652aa94af"; //$NON-NLS-1$

    @Test
    public void testGetMigrationTokenTime() {

        // generate a token
        String token = StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.MIGRATION_TOKEN)
                .encrypt("TALEND_" + System.currentTimeMillis());
        Map<String, Date> migrationTokenTime = MigrationTokenUtil.getMigrationTokenTime(token);
        boolean containsKey = migrationTokenTime.containsKey("TALEND");
        assertTrue(containsKey);
    }

    @Test
    public void testMD5() {
        String md5 = getMD5(PRODUCTDATE_HELPER_CLASS_NAME.getBytes());
        assertEquals(md5, PRODUCT_DATEHELPER_MD5_VALUE);
    }

    private String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
