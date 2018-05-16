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
package org.talend.core.service;

import java.util.Properties;

import org.talend.core.IService;

/**
 * created by wchen on Mar 4, 2015 Detailled comment
 *
 */
public interface IMapperIDocService extends IService {

    public void launchIDocImporter(Properties properties);

}
