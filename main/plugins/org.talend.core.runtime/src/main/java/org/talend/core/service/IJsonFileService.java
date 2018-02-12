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

import org.talend.core.IService;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;

/**
 * created by wchen on 2013-6-5 Detailled comment
 * 
 */
public interface IJsonFileService extends IService {

    public boolean changeFilePathFromRepository(Object jsonConneciton, IElementParameter filePathParm, IElement elem, Object value);

}
