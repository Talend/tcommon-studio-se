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
package org.talend.designer.core.convert;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * created by hcyi on May 25, 2015 Detailled comment
 *
 */
public interface IProcessConvertToAllTypeService extends IProcessConvertService {

    /**
     * Converts the current child process extends process to process.
     * 
     * @param item an item to be converted.
     * @param repViewObject an object to be converted.
     * @return
     */
    Item convertToProcess(Item item, IRepositoryViewObject repViewObject, String newJobName, String jobTypeValue);

    /**
     * Converts the current child process extends process to process.
     * 
     * @param item an item to be converted.
     * @param repViewObject an object to be converted.
     * @return
     */
    Item convertToProcessStreaming(Item item, IRepositoryViewObject repViewObject, String newJobName, String jobTypeValue,
            String frameworkValue);

    /**
     * Converts the current child process extends process to process.
     * 
     * @param item an item to be converted.
     * @param repViewObject an object to be converted.
     * @return
     */
    Item convertToProcessBatch(Item item, IRepositoryViewObject repViewObject, String newJobName, String jobTypeValue,
            String frameworkValue);
}
