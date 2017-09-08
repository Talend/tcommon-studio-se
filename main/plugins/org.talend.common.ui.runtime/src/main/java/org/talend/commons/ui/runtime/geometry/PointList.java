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
// PointList.java
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

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: PointList.java 7048 2007-11-16 02:36:17Z nrousseau $
 * 
 */
public abstract class PointList {

    java.util.List list;

    boolean visible = true;

    Point2D current;

    // constructor
    public PointList() {
        list = new ArrayList();
    }

    // listIterator
    public ListIterator listIterator() {
        return list.listIterator();
    }

    // add
    public void add(Point2D p) {
        list.add(p);
        setCurrent(p);
    }

    public void add(int i, Point2D p) {
        list.add(i, p);
        setCurrent(p);
    }

    // remove
    public int remove(Point2D p) {
        boolean cr = false;
        int i = getCurrentIndex();

        if (p == current)
            cr = true;
        list.remove(p);
        if (size() == 0) {
            current = null;
            return 1;
        }
        if (cr)
            setCurrent(Math.max(i - 1, 0));
        return 0;
    }

    public int remove(int i) {
        boolean cr = false;

        if (get(i) == current)
            cr = true;
        list.remove(i);
        if (size() == 0) {
            current = null;
            return 1;
        }
        if (cr)
            setCurrent(Math.max(i - 1, 0));
        return 0;
    }

    // clear
    public void clear() {
        current = null;
        list.clear();
    }

    // get
    public Point2D get(int i) {
        return (Point2D) list.get(i);
    }

    // size
    public int size() {
        return list.size();
    }

    // getCurrent
    public Point2D getCurrent() {
        return current;
    }

    // getCurrentIndex
    public int getCurrentIndex() {
        if (current == null)
            return -1;
        return list.indexOf(current);
    }

    // setCurrent
    public void setCurrent(Point2D p) {
        current = p;
    }

    public void setCurrent(int i) {
        current = (Point2D) list.get(i);
    }

    // isVisible
    public boolean isVisible() {
        return visible;
    }

    // setVisible
    public void setVisible(boolean b) {
        Point2D point;

        for (ListIterator li = list.listIterator(); li.hasNext();) {
            point = (Point2D) li.next();
            point.setVisible(b);
        }
        visible = b;
    }

}
