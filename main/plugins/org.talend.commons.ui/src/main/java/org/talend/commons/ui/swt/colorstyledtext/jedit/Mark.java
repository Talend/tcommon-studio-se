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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: Mark.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class Mark extends Span {

    protected boolean isPrevious;

    public boolean isMarkPrevious() {
        return isPrevious;
    }

    public boolean isMarkFollowing() {
        return !isPrevious;
    }

    public void accept(IVisitor visitor) {
        visitor.acceptMark(this);
    }

    public String getType() {
        return super.getType() + "@" + text.length(); //$NON-NLS-1$
    }

}
