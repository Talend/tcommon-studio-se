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

import java.util.Set;

import org.talend.core.IService;
import org.talend.core.hadoop.version.custom.ECustomVersionType;

/**
 * created by ycbai on 2013-3-14 Detailled comment
 * 
 */
public interface IHadoopService extends IService {

    /**
     * DOC ycbai Comment method "getHadoopJars".
     * 
     * @param distribution
     * @param version
     * @return jars which this hadoop distribution(and version) needed.
     */
    public Set<String> getHadoopLibraries(String distribution, String version, boolean showDownloadIfNotExist);

    public Set<String> getHadoopLibrariesByType(ECustomVersionType type, String distribution, String version);

    /**
     * DOC Talend Comment method "getMissingLibraries".
     * 
     * @param distribution
     * @param version
     * @return
     */
    public Set<String> getMissingLibraries(String distribution, String version);

}
