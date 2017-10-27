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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: Span.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class Span extends TextSequence {

    protected String begin, end;

    protected boolean noLineBreak, noWordBreak, excludeMatch;

    public void accept(IVisitor visitor) {
        visitor.acceptSpan(this);
    }

    public String getStart() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public boolean hasDelegate() {
        return getDelegate() != null;
    }

    public boolean noLineBreak() {
        return noLineBreak;
    }

    public boolean isNoWordBreak() {
        return noWordBreak;
    }

    public boolean getExcludeMatch() {
        return excludeMatch;
    }

    public String getDelegateContentType() {
        return getDelegate() + getContentType();
    }

}
