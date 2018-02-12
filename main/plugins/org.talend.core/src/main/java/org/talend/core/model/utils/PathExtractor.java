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
package org.talend.core.model.utils;

import org.talend.commons.utils.StringUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class PathExtractor {

    /**
     * DOC amaumont Comment method "extractPath".
     * @param text
     * @return 
     */
    public static String extractPath(String filePathExpression) {

        ECodeLanguage currentLanguage = LanguageManager.getCurrentLanguage();
        String extractedFilePath;
        if (currentLanguage == ECodeLanguage.PERL) {
            String extractedFilePathWithQuote = StringUtils.extractFirstDelimitedString(filePathExpression, "'"); //$NON-NLS-1$
            String extractedFilePathWithDoubleQuote = StringUtils.extractFirstDelimitedString(filePathExpression, "\""); //$NON-NLS-1$
            if (extractedFilePathWithDoubleQuote.length() > extractedFilePathWithQuote.length()) {
                extractedFilePath = extractedFilePathWithDoubleQuote;
            } else {
                extractedFilePath = extractedFilePathWithQuote;
            }
        } else if (currentLanguage == ECodeLanguage.JAVA) {
            extractedFilePath = StringUtils.extractFirstDelimitedString(filePathExpression, "\""); //$NON-NLS-1$
        } else {
            throw new IllegalStateException("Case not found"); //$NON-NLS-1$
        }

        return extractedFilePath;
    }

}
