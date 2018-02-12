// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.libraries.ui;

import org.eclipse.swt.SWT;

/**
 * Facade to accomodate difference between RCP and RAP in SWT class <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z sgandon $
 * 
 */
public abstract class SWTFacade {

    public static boolean isRAP() {
        return "rap".equals(SWT.getPlatform());
    }

    public static final int TRAVERSE_ARROW_PREVIOUS = 1 << 5;

    public static final int TRAVERSE_ARROW_NEXT = 1 << 6;

    public static final int TRAVERSE_PAGE_PREVIOUS = 1 << 8;

    public static final int TRAVERSE_PAGE_NEXT = 1 << 9;

    public static final int SHADOW_ETCHED_OUT = 1 << 6;

    public static final int MouseWheel = 37;

    public static final int NO_BACKGROUND = 1 << 18;

    public static final int MouseMove = 5;

    public static final int HIDE_SELECTION = 1 << 15;

    public static final int EraseItem = 40;

    public static final int Paint = 9;

    public static final int EMBEDDED = 1 << 24;
}
