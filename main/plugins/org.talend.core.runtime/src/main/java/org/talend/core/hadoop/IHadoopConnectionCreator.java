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
package org.talend.core.hadoop;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.talend.core.model.properties.ConnectionItem;

/**
 * created by ycbai on 2015年6月29日 Detailled comment
 *
 */
public interface IHadoopConnectionCreator {

    public void init(String hadoopClusterId);

    public ConnectionItem create(Map<String, Map<String, String>> initParams) throws CoreException;

    public String getTypeName();

}
