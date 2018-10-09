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
package org.talend.commons.ui.runtime;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface ColorConstants {

    static final Color WHITE_COLOR = new Color(null, 255, 255, 255);

    static final Color GREY_COLOR = new Color(null, 215, 215, 215);

    static final Color YELLOW_GREEN_COLOR = new Color(null, 138, 188, 0);// 143, 163, 35

    static final Color YELLOW_COLOR = new Color(null, 255, 173, 37);// 254, 182, 84

    static final Color RED_COLOR = new Color(null, new RGB(240, 0, 0));// 255

    static final Color VERTICAL_SEPERATOR_LINE_COLOR = new Color(null, 162, 179, 195);
}
