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
package org.talend.utils.jobconductor;

import java.util.Date;

import org.talend.utils.IdGenerator;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public final class TriggerNameHelper {

    private static final String P_TASK = "_task";

    public static final String PREFIX_TALEND_RECOVER_TRIGGER = "RecoverTrigger" + P_TASK;

    public static final String PREFIX_QUARTZ_RECOVER_TRIGGER = "recover_";

    public static final String PREFIX_INSTANT_RUN_TRIGGER = "InstantRunTrigger";

    public static final String PREFIX_FILE_TRIGGER = "FileTrigger";

    private static final String PREFIX_TIME_TRIGGER = "";

    private static TriggerNameHelper instance;

    public static TriggerNameHelper getInstance() {
        if (instance == null) {
            instance = new TriggerNameHelper();
        }
        return instance;
    }

    private TriggerNameHelper() {
        super();
    }

    /**
     * 
     * DOC amaumont Comment method "parseQuartzTriggerId".
     * 
     * @param triggerName
     * @return the idQuartzTrigger if the triggerName contains a valid idQuartzTrigger, return null for case where
     * idQuartzTrigger can't be retrieved
     */
    public Integer parseQuartzTriggerId(String triggerName) {

        String[] split = triggerName.split("\\.");
        triggerName = split[split.length - 1];

        if (isTalendRecoverTriggerName(triggerName)) {
            // throw new IllegalArgumentException("InstantRunTrigger " + triggerName
            // + " does not contain a talend trigger id!");
            String idStr = triggerName.substring(PREFIX_TALEND_RECOVER_TRIGGER.length());
            return Integer.parseInt(idStr);
        } else if (isInstantRunTriggerName(triggerName)) {
            // throw new IllegalArgumentException("InstantRunTrigger " + triggerName
            // + " does not contain a talend trigger id!");
            return null;
        } else if (isFileTriggerName(triggerName)) {
            String idStr = triggerName.substring(PREFIX_FILE_TRIGGER.length());
            return Integer.parseInt(idStr);
        }
        try {
            return Integer.parseInt(triggerName);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(triggerName + " is not a valid trigger name");
        }
    }

    public int parseQuartzJobNameId(String quartzJobName) {

        try {
            String[] split = quartzJobName.split("\\.");
            quartzJobName = split[split.length - 1];

            if (quartzJobName.startsWith(TRIGGER_TYPE.getFileTriggerParentPrefix())) {
                String idStr = quartzJobName.substring(TRIGGER_TYPE.getFileTriggerParentPrefix().length());
                return Integer.parseInt(idStr);
            } else {
                return Integer.parseInt(quartzJobName);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(quartzJobName + " is not a valid job name");
        }
    }

    public boolean isInstantRunTriggerName(String triggerName) {
        return triggerName.matches(".*" + PREFIX_INSTANT_RUN_TRIGGER + ".+");
    }

    public String buildRecoverTriggerName(int idTask) {
        return PREFIX_TALEND_RECOVER_TRIGGER + idTask;
    }

    public String buildInstantRunTriggerName(int idTask) {
        return (new StringBuilder(PREFIX_INSTANT_RUN_TRIGGER)).append(idTask).append("_").append((new Date()).getTime())
                .append("_")
                + IdGenerator.getAsciiRandomString(5);
    }

    public String buildQuartzTriggerName(TRIGGER_TYPE triggerType, int talendTriggerId) {
        String quartzTriggerName = null;
        if (triggerType == TRIGGER_TYPE.FILE_TRIGGER) {
            quartzTriggerName = PREFIX_FILE_TRIGGER + talendTriggerId;
        } else {
            quartzTriggerName = String.valueOf(talendTriggerId);
        }
        return quartzTriggerName;
    }

    public boolean isTalendRecoverTriggerName(String triggerName) {
        return triggerName.matches(".*" + PREFIX_TALEND_RECOVER_TRIGGER + ".+");
    }

    public boolean isQuartzRecoverTriggerName(String triggerName) {
        return triggerName.matches(PREFIX_QUARTZ_RECOVER_TRIGGER + ".+");
    }

    public boolean isRecoverTriggerName(String triggerName) {
        return isTalendRecoverTriggerName(triggerName) || isQuartzRecoverTriggerName(triggerName);
    }

    public boolean isFileTriggerName(String triggerName) {
        return triggerName.matches(".*" + PREFIX_FILE_TRIGGER + ".+");
    }

    public boolean isTimeTriggerName(String triggerName) {
        return triggerName.matches("\\d+");
    }

    public boolean isTalendTriggerDependent(String quartzTriggerName) {
        return isTimeTriggerName(quartzTriggerName) || isFileTriggerName(quartzTriggerName);
    }

}
