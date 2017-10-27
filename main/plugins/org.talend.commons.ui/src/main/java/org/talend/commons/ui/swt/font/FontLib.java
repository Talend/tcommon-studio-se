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
package org.talend.commons.ui.swt.font;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;

/**
 * DOC talend class global comment. Detailled comment
 */
public class FontLib {

    public static final String TALEND_ITALIC_FONT = "Talend.Italic.Font"; //$NON-NLS-1$

    public static final Font ITALIC_FONT = JFaceResources.getFontRegistry().getItalic(TALEND_ITALIC_FONT);

}
