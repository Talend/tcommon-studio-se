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
package org.talend.core.model.properties.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Status;


/**
 * DOC tguiu  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public final class StatusHelper {

    private static final String STATUS_DELIMITER = ";"; //$NON-NLS-1$
    private static final char CODE_LABEL_DELIMITER = ' ';

    public static String writeString(List<String> items) {
        int size = items.size();
        StringBuffer buf = new StringBuffer(size * 50);
        for (int i = 0; i < size; i++) {
            buf.append(items.get(i));
            if (i != size - 1)
                buf.append(STATUS_DELIMITER);
        }
        return buf.toString();
    }
    
    public static List<String> readString(String stringList) {
        if (stringList == null || "".equals(stringList)) //$NON-NLS-1$
            return EMPTY_STRING_LIST;
        check(stringList);
        ArrayList<String> result = new ArrayList<String>(50);
        for (String tmp : stringList.split(STATUS_DELIMITER)) {
            result.add(tmp);
        }
        return result;
    }

    public static List<Status> parse(String stringList) {
        if (stringList == null || "".equals(stringList)) //$NON-NLS-1$
            return EMPTY_STATUS_LIST;
        final List<String> strings = readString(stringList);
        List<Status> result = new ArrayList<Status>();
        for (String statusStr: strings) {
            final Status status = PropertiesFactory.eINSTANCE.createStatus();
            status.setCode(getCode(statusStr));
            status.setLabel(getLabel(statusStr));
            result.add(status);
        }
        return Collections.unmodifiableList(result);
    }
    
    public static String flat(List<Status> list) {
        final List<String> strings = new ArrayList<String>(list.size());
        for (Status status: list) {
            strings.add(getString(status.getCode(), status.getLabel()));
        }
        return writeString(strings);
    }
    
    public static final String getCode(String value) {
        check(value);
        int index = value.indexOf(CODE_LABEL_DELIMITER);
        return index < 0 ? "PB" : value.substring(0, index); //$NON-NLS-1$
    }
    
    public static final String getLabel(String value) {
        check(value);
        int index = value.indexOf(CODE_LABEL_DELIMITER);
        return index < 0 ? "PB" : value.substring(index + 1); //$NON-NLS-1$
    }
    
    public static final String getString(String code, String label) {
        check(code);
        check(label);
        return code + CODE_LABEL_DELIMITER + label;
    }
    
    private static void check(String str) {
        if (str == null || str.equals("")) //$NON-NLS-1$
            throw new IllegalArgumentException();
    }
    
    private StatusHelper() {
    }

    private static final List<Status> EMPTY_STATUS_LIST = Collections.unmodifiableList(new ArrayList<Status>());
    private static final List<String> EMPTY_STRING_LIST = Collections.unmodifiableList(new ArrayList<String>());
}
