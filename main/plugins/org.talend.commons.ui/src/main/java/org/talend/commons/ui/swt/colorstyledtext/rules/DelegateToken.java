// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.eclipse.jface.util.Assert;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Rule;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Type;

public class DelegateToken extends CToken {

    protected Rule delegate;

    protected String end;

    protected boolean consumed;

    public DelegateToken(Type type, Rule delegate, String end) {
        super(type);
        Assert.isNotNull(delegate);
        this.delegate = delegate;
        this.end = end;
        consumed = false;
    }

    public Object getData() {
        return delegate.getName() + super.getData();
    }

    public String getEnd() {
        return end;
    }
}
