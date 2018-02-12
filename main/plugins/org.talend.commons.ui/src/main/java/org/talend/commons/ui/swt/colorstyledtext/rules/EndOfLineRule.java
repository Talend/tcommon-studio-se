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
package org.talend.commons.ui.swt.colorstyledtext.rules;

import org.eclipse.jface.text.rules.IToken;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: EndOfLineRule.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class EndOfLineRule extends SingleLineRule {

    public EndOfLineRule(String startSequence, IToken token, boolean ignoreCase) {
        this(startSequence, token, (char) 0, ignoreCase);
    }

    public EndOfLineRule(String startSequence, IToken token, char escapeCharacter, boolean ignoreCase) {
        super(startSequence, null, token, escapeCharacter, ignoreCase);
    }

}
