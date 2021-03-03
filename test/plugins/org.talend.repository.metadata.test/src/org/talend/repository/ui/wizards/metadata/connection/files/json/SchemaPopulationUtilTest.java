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
package org.talend.repository.ui.wizards.metadata.connection.files.json;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class SchemaPopulationUtilTest {

    @Test
    public void testGetSchemaTree_runtimeException() {
        // https://jira.talendforge.org/browse/TUP-25052
        try {
            SchemaPopulationUtil.getSchemaTree(getTestDataFile("resources/test/file/relationship.json"), "_badEncoding_", 10);
            assertTrue("As long as execute successfully, pass", true);
        } catch (Throwable e) {
            fail("Throwing exception will block following actions");
        }
    }

    private File getTestDataFile(String bundlePath) throws IOException {
        URL dataUrl = FileLocator.find(FrameworkUtil.getBundle(this.getClass()), new Path(bundlePath), null);
        if (dataUrl != null) {
            dataUrl = FileLocator.toFileURL(dataUrl);
        }

        File testFile = new File(dataUrl.getFile());
        if (testFile.exists()) {
            return testFile;
        }
        return null;
    }

}
