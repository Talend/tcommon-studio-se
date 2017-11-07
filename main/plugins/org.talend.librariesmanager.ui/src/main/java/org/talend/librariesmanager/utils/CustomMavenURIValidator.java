// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * created by wchen on Sep 25, 2017 Detailled comment
 *
 */
public class CustomMavenURIValidator {

    private static PatternMatcherInput patternMatcherInput;

    private static Perl5Matcher matcher = new Perl5Matcher();

    private static Perl5Compiler compiler = new Perl5Compiler();

    private static Pattern pattern;

    // match mvn:group-id/artifact-id/version/type/classifier
    public static final String expression1 = "(mvn:(\\w+.*/)(\\w+.*/)([0-9]+(\\.[0-9])+(-SNAPSHOT){0,1}/)(\\w+/)(\\w+))";//$NON-NLS-1$

    // match mvn:group-id/artifact-id/version/type
    public static final String expression2 = "(mvn:(\\w+.*/)(\\w+.*/)([0-9]+(\\.[0-9])+(-SNAPSHOT){0,1}/)\\w+)";//$NON-NLS-1$

    // match mvn:group-id/artifact-id/version
    public static final String expression3 = "(mvn:(\\w+.*/)(\\w+.*/)([0-9]+(\\.[0-9])+(-SNAPSHOT){0,1}))";//$NON-NLS-1$

    public static String validateCustomMvnURI(String originalText, String customText) {
        if (customText.equals(originalText)) {
            return Messages.getString("InstallModuleDialog.error.sameCustomURI");
        }
        if (!validateMvnURI(customText)) {
            return Messages.getString("InstallModuleDialog.error.customURI");
        }
        return null;
    }

    public static boolean validateMvnURI(String mvnURI) {
        if (pattern == null) {
            try {
                pattern = compiler.compile(expression1 + "|" + expression2 + "|" + expression3);
            } catch (MalformedPatternException e) {
                ExceptionHandler.process(e);
            }
        }
        patternMatcherInput = new PatternMatcherInput(mvnURI);
        matcher.setMultiline(false);
        boolean isMatch = matcher.matches(patternMatcherInput, pattern);
        return isMatch;
    }
}
