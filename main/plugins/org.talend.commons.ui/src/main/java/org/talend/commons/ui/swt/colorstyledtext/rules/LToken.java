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

import org.eclipse.jface.text.rules.Token;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: LToken.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class LToken extends Token {

    protected int length;

    protected boolean isPrevious;

    public LToken(Object data) {
        super(data);
    }

    public LToken(Object data, int length) {
        this(data);
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void isPrevious(boolean value) {
        isPrevious = value;
    }

    public boolean isPrevious() {
        return isPrevious;
    }
}
