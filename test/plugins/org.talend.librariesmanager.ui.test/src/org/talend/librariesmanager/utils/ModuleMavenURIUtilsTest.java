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
package org.talend.librariesmanager.utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
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

    @Test
    public void testValidateMvnURITest() throws MalformedPatternException {
        String mvnURI = "mvn:com.amazon.redshift/redshift-jdbc42-no-awssdk/1.2.37.1061/jar";
        boolean isValid = ModuleMavenURIUtils.validateMvnURI(mvnURI);
        Assert.assertTrue(isValid);

        mvnURI = "mvn:org.slf4j/slf4j-api/1.8.0-beta1/jar";
        isValid = ModuleMavenURIUtils.validateMvnURI(mvnURI);
        Assert.assertTrue(isValid);

        String input1 = "mvn:net.sf.json-lib/json-lib/2.4/jar/jdk15";
        String input2 = "mvn:net.sf.json-lib/json-lib/2.4/jar";
        String input3 = "mvn:net.sf.json-lib/json-lib/2.4";
        // expression1: match mvn:group-id/artifact-id/version/type/classifier
        Perl5Matcher matcher = new Perl5Matcher();
        matcher.setMultiline(false);
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(ModuleMavenURIUtils.expression1);
        boolean match = matcher.matches(input1, pattern);
        Assert.assertTrue(match);

        match = matcher.matches(input2, pattern);
        Assert.assertFalse(match);

        match = matcher.matches(input3, pattern);
        Assert.assertFalse(match);

        // expression2: match mvn:group-id/artifact-id/version/type
        pattern = compiler.compile(ModuleMavenURIUtils.expression2);
        match = matcher.matches(input2, pattern);
        Assert.assertTrue(match);

        match = matcher.matches(input1, pattern);
        Assert.assertFalse(match);

        match = matcher.matches(input3, pattern);
        Assert.assertFalse(match);

        // expression3: match mvn:group-id/artifact-id/version
        pattern = compiler.compile(ModuleMavenURIUtils.expression3);
        match = matcher.matches(input3, pattern);
        Assert.assertTrue(match);

        match = matcher.matches(input1, pattern);
        Assert.assertFalse(match);

        match = matcher.matches(input2, pattern);
        Assert.assertFalse(match);
    }

}
