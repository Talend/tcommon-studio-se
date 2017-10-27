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
package org.talend.core.model.metadata;

import java.util.List;

import org.talend.core.IService;
import org.talend.core.model.metadata.types.AutoConversionType;

/**
 * created by hcyi on Aug 22, 2016 Detailled comment
 *
 */
public interface IAutoConvertTypesService extends IService {

    public List<AutoConversionType> getAllAutoConversionTypes();
}
