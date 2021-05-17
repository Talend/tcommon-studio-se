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
package org.talend.core.ui.token;

import java.util.Properties;

import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.utils.time.PerformanceStatisticUtil;
import org.talend.commons.utils.time.PerformanceStatisticUtil.StatisticKeys;
import org.talend.commons.utils.time.PropertiesFileUtil;

import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.ProcessorIdentifier;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import us.monoid.json.JSONObject;

/**
 * DOC sbliu  class global comment. Detailled comment
 */
public class PerformanceTokenCollector extends AbstractTokenCollector {

    /* (non-Javadoc)
     * @see org.talend.core.ui.token.AbstractTokenCollector#collect()
     */
    @Override
    public JSONObject collect() throws Exception {
        checkAndWait();
        
        JSONObject tokenStudioObject = new JSONObject();
        //
        JSONObject jsonObjectHDInfo = new JSONObject();
        
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();
        ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
        ComputerSystem cs = hal.getComputerSystem();//computer system
        Baseboard baseboard = cs.getBaseboard();//motherboard
        GlobalMemory memory = hal.getMemory();
        
        jsonObjectHDInfo.put("computer vendor", cs.getManufacturer());
        jsonObjectHDInfo.put("board vendor", baseboard.getManufacturer());
        jsonObjectHDInfo.put("board version", baseboard.getVersion());
        jsonObjectHDInfo.put("processor", processorIdentifier.getName());
        jsonObjectHDInfo.put("physical memory", Math.ceil((memory.getTotal() /(1024d*1024*1024))) + "GB");
        tokenStudioObject.put("hardware", jsonObjectHDInfo);
        
        //
        JSONObject jsonObjectIOInfo = new JSONObject();
        Properties props = PropertiesFileUtil.read(PerformanceStatisticUtil.getRecordingFile(),false);
        jsonObjectIOInfo.put(StatisticKeys.STARTUP_AVERAGE.get(), props.getProperty(StatisticKeys.STARTUP_AVERAGE.get()));
        jsonObjectIOInfo.put(StatisticKeys.STARTUP_MAX.get(), props.getProperty(StatisticKeys.STARTUP_MAX.get()));
        jsonObjectIOInfo.put(StatisticKeys.IO_R_MB_SEC.get(), props.getProperty(StatisticKeys.IO_R_MB_SEC.get()));
        jsonObjectIOInfo.put(StatisticKeys.IO_R_AVERAGE_MB_SEC.get(), props.getProperty(StatisticKeys.IO_R_AVERAGE_MB_SEC.get()));
        jsonObjectIOInfo.put(StatisticKeys.IO_W_MB_SEC.get(), props.getProperty(StatisticKeys.IO_W_MB_SEC.get()));
        jsonObjectIOInfo.put(StatisticKeys.IO_W_AVERAGE_MB_SEC.get(), props.getProperty(StatisticKeys.IO_W_AVERAGE_MB_SEC.get()));
        tokenStudioObject.put("performance", jsonObjectIOInfo);
        
        return tokenStudioObject;
    }

    private void checkAndWait() {
        try {
            PerformanceStatisticUtil.waitUntilFinish();
        } catch (InterruptedException e) {
            CommonExceptionHandler.log(e.getMessage());
        }
    }

    @Override
    public void reset() {
        PerformanceStatisticUtil.reset();
    }
    
}
