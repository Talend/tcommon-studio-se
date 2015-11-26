// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package files;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.talend.utils.files.FileUtils;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class FileUtilsTest {

    /**
     * Test method for {@link org.talend.utils.files.FileUtils#checkBracketsInFile(java.lang.String)}.
     */
    @Test
    @Ignore("Test is looking expecting a file that does not exists in the source.")
    public void testCheckBracketsInFile() {
        String testFile = "test/checkbrackets.txt";
        try {
            List<ReturnCode> ok = FileUtils.checkBracketsInFile(testFile);
            for (ReturnCode returnCode : ok) {
                System.out.println(returnCode.getMessage());
            }
            assertEquals(5, ok.size());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

}
