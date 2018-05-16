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

package org.talend.utils.dates;

import java.util.Comparator;
import java.util.Date;

/**
 * 
 * DOC mzhao class global comment. Detailled comment
 * 
 * @param <T>
 */
public class DateComparator<T> implements Comparator<T> {

	public int compare(T date1, T date2) {
		Date firstDate = (Date) date1;
		Date secondDate = (Date) date2;
		return firstDate.compareTo(secondDate);
	}

}
