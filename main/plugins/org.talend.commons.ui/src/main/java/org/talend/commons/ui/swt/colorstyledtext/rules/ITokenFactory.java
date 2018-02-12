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
import org.talend.commons.ui.swt.colorstyledtext.jedit.Type;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ITokenFactory.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public interface ITokenFactory {

    IToken makeToken(Type type);
}
