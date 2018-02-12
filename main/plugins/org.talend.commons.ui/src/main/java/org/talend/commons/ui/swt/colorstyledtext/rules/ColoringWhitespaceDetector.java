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

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ColoringWhitespaceDetector.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ColoringWhitespaceDetector implements IWhitespaceDetector {

    public boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
    }

}
