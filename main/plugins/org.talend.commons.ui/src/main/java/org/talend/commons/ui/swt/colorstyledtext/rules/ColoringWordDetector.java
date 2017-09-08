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
package org.talend.commons.ui.swt.colorstyledtext.rules;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ColoringWordDetector.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ColoringWordDetector implements IWordDetector {

    protected Set<Integer> charMap = new HashSet<Integer>();

    public boolean isWordStart(char c) {
        return Character.isLetter(c) || '_' == c || isTextSequencePart(c);
    }

    private boolean isTextSequencePart(char c) {
        return charMap.contains(new Integer(c));
    }

    public void addWord(String word) {
        charMap.add(new Integer(word.charAt(0)));
    }

    public boolean isWordPart(char c) {
        /*
         * added the dot so properties file would mark_following correctly when they path contained dots. For example
         * a.b.c=124
         */
        return isWordStart(c) || Character.isDigit(c) || '.' == c;
    }
}
