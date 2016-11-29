// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

public class IntegerUtil {

    /**
     * reuse the method in java.lang.Integer.valueOf(String i) and add the null reference process step
     * 
     * @param s
     * @return
     */
    public static Integer valueOf(String s) {
        return s == null ? null : Integer.valueOf(s);
    }

    /**
     * reuse the method in java.lang.Integer.valueOf(int i)
     * 
     * @param i
     * @return
     */
    public static Integer valueOf(int i) {
        return Integer.valueOf(i);
    }

    /**
     * add the method to avoid the java auto data type converter which can make NPE exception
     * 
     * @param i
     * @return
     */
    public static Integer valueOf(Integer i) {
        return i;
    }

    public static void main(String[] args) {
        Integer nullIntegerObject = null;
        valueOf(nullIntegerObject);

        String nullIntegerStringObject = null;
        valueOf(nullIntegerStringObject);
    }
}
