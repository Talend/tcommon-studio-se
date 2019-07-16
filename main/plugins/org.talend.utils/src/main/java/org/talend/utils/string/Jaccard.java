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

import java.util.HashSet;
import java.util.Set;

/**
 * DOC xwen  class global comment. Detailled comment
 */
public final class Jaccard {

    private Jaccard() {

    }

    // handle Camel tokenization
    public static String tokenize(String outputEntry) {

        String str = outputEntry.replaceAll(
                String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
        str = str.replaceAll("_", "");
        str = str.replaceAll(" +", " ").toLowerCase();

        return str;
    }

    public static double JaccardCompare(String res, String res1) {
        String[] left = res.split("\\s+");
        String[] right = res1.split("\\s+");
        int leftLength = left.length;
        int rightLength = right.length;
        Set<String> unionSet = new HashSet<String>();
        boolean unionFilled = false;
        double intersection = 0;

        if (leftLength == 0 || rightLength == 0) {
            return 0d;
        }

        for (int leftIndex = 0; leftIndex < leftLength; leftIndex++) {
            unionSet.add(left[leftIndex]);
            for (int rightIndex = 0; rightIndex < rightLength; rightIndex++) {
                if (!unionFilled) {
                    unionSet.add(right[rightIndex]);
                }
                if (left[leftIndex].equals(right[rightIndex])) {
                    int wordLength = left[leftIndex].length();
                    if (wordLength > 1) {
                        double weight = Math.log(wordLength) / Math.log(2);
                        intersection = intersection + weight;
                    } else {
                        intersection++;
                    }
                }
            }
            unionFilled = true;
        }

        return intersection / Double.valueOf(unionSet.size());
    }

}
