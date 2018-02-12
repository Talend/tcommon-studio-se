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

// Java & B�zier
// (C) 2002 Emmanuel Turquin
//
// Math.java
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

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: Math.java 7048 2007-11-16 02:36:17Z nrousseau $
 * 
 */
public final class Math {

    public static int max(int i, int j) {
        return (i > j ? i : j);
    }

    public static int min(int i, int j) {
        return (i < j ? i : j);
    }

    public static double max(double i, double j) {
        return (i > j ? i : j);
    }

    public static double min(double i, double j) {
        return (i < j ? i : j);
    }

    public static long fact(int i, int j) {
        if (i < j || i < 0 || j < 0)
            return 1;
        long r = 1;
        while (i > j)
            r *= i--;
        return j > 0 ? r * j : r;
    }

    public static long fact(int i) {
        if (i < 0)
            return -1;
        return fact(i, 1);
    }

    public static long comb(int n, int p) {
        if (n < 0 || p < 0 || n < p)
            return 0;
        if (n == p)
            return 1;
        long num, den;
        if (p > n - p) {
            if ((num = fact(n, p + 1)) <= 0 || (den = fact(n - p)) <= 0)
                return comb(n - 1, p) + comb(n - 1, p - 1);
        } else {
            if ((num = fact(n, n - p + 1)) <= 0 || (den = fact(p)) <= 0)
                return comb(n - 1, p) + comb(n - 1, p - 1);
        }
        return num / den;
    }

    public static double pow(double n, int p) {
        return (double) java.lang.Math.pow(n, p);
    }

    public static int round(int i, int j) {
        int r;

        if (j <= 0)
            return i;
        r = i / j * j;
        if (r + j - i < i - r)
            return r + j;
        return r;
    }
}
