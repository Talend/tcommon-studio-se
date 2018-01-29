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
package org.talend.commons.utils.resource;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * created by scorreia on Jul 3, 2012 Detailled comment
 * 
 */
public class ResourceUtilTest {

    /**
     * Test method for {@link org.talend.commons.utils.resource.ResourceUtil#convertResourceToFile(java.net.URL)}.
     */
    @Ignore("This test should be implemented later")
    @Test
    public void testConvertResourceToFile() {
        fail("Not yet implemented"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.resource.ResourceUtil#getFileFromResource(java.lang.Class, java.lang.String)}.
     * 
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test
    public void testGetFileFromResource() throws IOException, URISyntaxException {
        String filename = "/data/TextFile.txt"; //$NON-NLS-1$
        File file = ResourceUtil.getFileFromResource(this.getClass(), filename);
        Assert.assertNotNull(file);
    }

}
