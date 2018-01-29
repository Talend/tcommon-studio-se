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
package org.talend.commons.runtime.utils.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

/**
 * DOC xtab class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class IOUtils {

    public static long computeCRC(InputStream in) {
        long unitCRC = 0;

        BufferedInputStream bufferedInputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(in);

            // Compute Adler-32 checksum
            CheckedInputStream cis = new CheckedInputStream(bufferedInputStream, new Adler32());
            byte[] tempBuf = new byte[128];
            while (cis.read(tempBuf) >= 0) {
                // do nothing
            }
            unitCRC = cis.getChecksum().getValue();
        } catch (IOException e) {
            return -1;
        } finally {
            try {
                bufferedInputStream.close();
            } catch (Exception e) {
                // ignore me even if i'm null
            }
        }
        return unitCRC;
    }

}
