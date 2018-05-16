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
package org.talend.core.utils;

import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.HadoopConstants;
import org.talend.core.service.IMRProcessService;
import org.talend.core.service.IStormProcessService;

/**
 * created by kongxiaohan on Sep 15, 2015 Detailled comment
 *
 */
public class JobImageUtil {

    public static IImage getWizardIcon(String jobType, String framework) {
        if (HadoopConstants.JOBTYPEBDSTREAMING.equals(jobType)) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IStormProcessService.class)) {
                IStormProcessService stormProcessService = (IStormProcessService) GlobalServiceRegister.getDefault().getService(
                        IStormProcessService.class);
                return stormProcessService.getStormWizardIcon(framework);
            }
        } else if (HadoopConstants.JOBTYPEBDBATCH.equals(jobType)) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IMRProcessService.class)) {
                IMRProcessService mrProcessService = (IMRProcessService) GlobalServiceRegister.getDefault().getService(
                        IMRProcessService.class);
                return mrProcessService.getMRWizardIcon(framework);
            }
        }
        return ECoreImage.PROCESS_WIZ;
    }

}
