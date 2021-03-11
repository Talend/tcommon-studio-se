// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.repository.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.talend.commons.utils.Version;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * @author hwang
 *
 */
public class RepositoryNodeSortUtil {

	public List<IRepositoryViewObject> getSortVersion(List<IRepositoryViewObject> versions) {
    	List<IRepositoryViewObject> temp = new ArrayList<IRepositoryViewObject>();
        temp.addAll(versions);
        
        Collections.sort(temp, new Comparator<IRepositoryViewObject>() {

            @Override
            public int compare(IRepositoryViewObject o1, IRepositoryViewObject o2) {
                String version1 = o1.getVersion();
                String version2 = o2.getVersion();
                if(version1 != null && version2 != null) {
                	return new Version(version1).compareTo(new Version(version2));
                }
                return 0;
            }
        });
        return temp;
    }
}
