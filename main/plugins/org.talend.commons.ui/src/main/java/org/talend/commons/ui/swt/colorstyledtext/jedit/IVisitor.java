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
 * $Id: IVisitor.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public interface IVisitor {

    void acceptSpan(Span span);

    void acceptTextSequence(TextSequence text);

    void acceptEolSpan(EOLSpan eolSpan);

    void acceptMark(Mark mark);
}
