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
package org.talend.core.repository.utils;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.IProcess2;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.model.ContextReferenceBean;

/**
 * @author Marvin Wang
 * @version 1.0 jdk1.6
 * @date Sep 14, 2012
 */
public class RepositoryReferenceBeanUtils {

    public static boolean hasReferenceBean(List<ContextReferenceBean> list, String beanName, String beanVersion) {
        boolean has = false;

        if (list == null) {
            list = new ArrayList<ContextReferenceBean>();
            has = false;
        } else {
            for (ContextReferenceBean loopBean : list) {
                String loopBeanName = loopBean.getRelateName();
                String loopBeanVersion = loopBean.getRelateVersion();
                if (loopBeanName != null && !"".equals(loopBeanName) && loopBeanName.equals(beanName) && loopBeanVersion != null
                        && !"".equals(loopBeanVersion) && loopBeanVersion.equals(beanVersion)) {
                    has = true;
                    break;
                }
            }
        }

        return has;
    }
    
    public static boolean isOpenedProcess(IProcess2 process){
    	List<IProcess2> openedProcesses = RepositoryManagerHelper.getOpenedProcess();
        boolean isOpenedProcess = false;
        if (openedProcesses != null && openedProcesses.size() > 0) {
            for (IProcess2 tempPro : openedProcesses) {
                if (process.getId().equals(tempPro.getId())) {
                    isOpenedProcess = true;
                    break;
                }
            }
        }
    	return isOpenedProcess;
    }
}
