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

// Java & B�zier
// (C) 2002 Emmanuel Turquin
//
// Point2DList.java
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.talend.commons.ui.runtime.geometry;

import java.util.ListIterator;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: Point2DList.java 7048 2007-11-16 02:36:17Z nrousseau $
 * 
 */
public class Point2DList extends PointList {

    // contains
    public int contains(double x, double y) {
        if (current != null && ((Point2D) current).contains(x, y))
            return list.indexOf(current);
        Point2D point;
        for (ListIterator li = list.listIterator(list.size()); li.hasPrevious();) {
            point = (Point2D) li.previous();
            if (point.contains(x, y))
                return li.nextIndex();
        }
        return -1;
    }

}
