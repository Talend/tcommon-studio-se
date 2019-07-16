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
package org.talend.utils.string;

import org.apache.commons.text.similarity.LevenshteinDistance;


/**
 * DOC xwen class global comment. Detailled comment an algorithm to get Levenshtein score between two strings
 */
public final class Levenshtein {


    private Levenshtein() {
    }

    public static double getLevenshteinScore(String inputStr, String outputColumnName) {
        double LevenshteinScore = 0.0;

        double maxLength = (inputStr.length() > outputColumnName.length()) ? inputStr.length() : outputColumnName.length();
        LevenshteinDistance ld = new LevenshteinDistance();
        double LevenshteinDistance = ld.apply(outputColumnName, inputStr);

        // one can overwrite to have his own version
        if (inputStr.contains(outputColumnName) || outputColumnName.contains(inputStr)) {
            LevenshteinScore = (maxLength - LevenshteinDistance + 1) / (maxLength + 1);
        } else {
            LevenshteinScore = 1 - (LevenshteinDistance / maxLength);
        }
        return LevenshteinScore;
    }

}
