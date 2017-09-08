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
import org.talend.commons.ui.swt.colorstyledtext.jedit.Type;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: CToken.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class CToken extends Token {

    protected Type type;

    protected String color;

    public CToken(Type type) {
        super(type.getType());
        this.type = type;
    }

    public CToken(String color) {
        super(color);
        this.color = color;
        this.type = null;
    }

    public String getColor() {
        if (type != null) {
            return type.getColor();
        } else {
            return color;
        }
    }

    public Type getType() {
        return type;
    }

    public Object getData() {
        return type.getContentType();
    }
}
