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

import org.eclipse.jface.text.rules.IToken;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: SingleLineRule.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class SingleLineRule extends CasedPatternRule {

    public SingleLineRule(String startSequence, String endSequence, IToken token, char escapeCharacter) {
        this(startSequence, endSequence, token, escapeCharacter, false);
    }

    public SingleLineRule(String startSequence, String endSequence, IToken token, char escapeCharacter,
            boolean ignoreCase) {
        super(startSequence, endSequence, token, escapeCharacter, true, ignoreCase);
    }

}
