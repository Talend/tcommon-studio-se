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
package org.talend.librariesmanager.utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.junit.Assert;
import org.junit.Test;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * created by wchen on Sep 8, 2017 Detailled comment
 *
 */
public class ModuleMavenURIUtilsTest {

    @Test
    public void testValidateCustomMvnURI() throws MalformedPatternException {
        String errorMessage1 = Messages.getString("InstallModuleDialog.error.sameCustomURI");
        String errorMessage2 = Messages.getString("InstallModuleDialog.error.customURI");

        String originalURI = "mvn:org.talend.libraries/test/6.0.0";
        String customURI = "mvn:org.talend.libraries/test/6.0.0";
        String result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertEquals(result, errorMessage1);

        customURI = "mvn:org.talend.libraries/test/6.0";
        result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertNull(result);

        customURI = "mvn:org.talend.libraries/test/6.0.0/exe";
        result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertNull(result);

        customURI = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT";
        result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertNull(result);

        customURI = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar";
        result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertNull(result);

        customURI = "mvn:org.talend.libraries/";
        result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertEquals(result, errorMessage2);

        customURI = "mvn:org.talend.libraries/test";
        result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertEquals(result, errorMessage2);

        customURI = "mvn:org.talend.libraries/test/6";
        result = ModuleMavenURIUtils.validateCustomMvnURI(originalURI, customURI);
        Assert.assertEquals(result, errorMessage2);

    }

}
