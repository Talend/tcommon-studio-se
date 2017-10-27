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



/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public enum TRIGGER_TYPE {

    SIMPLE_TRIGGER("SimpleTrigger", true, getEmptyString()),
    CRON_TRIGGER("CronTrigger", true, getEmptyString()),
    CRON_UI_TRIGGER("CronUITrigger", true, getEmptyString()),
    FILE_TRIGGER("FileTrigger", false, getFileTriggerPrefix());

    private static final String EMPTY_STRING = "";

    private static final String TIME_TRIGGER_PREFIX = EMPTY_STRING;

    private static final String FILE_TRIGGER_PREFIX = TriggerNameHelper.PREFIX_FILE_TRIGGER;

    private static final String FILE_TRIGGER_PARENT_PREFIX = "FileTriggerJob";

    private String triggerType;

    private boolean timeTrigger;

    private String prefix;

    TRIGGER_TYPE(String mappingForward, boolean timeTrigger) {
        this.triggerType = mappingForward;
        this.timeTrigger = timeTrigger;
    }

    TRIGGER_TYPE(String mappingForward, boolean timeTrigger, String prefix) {
        this(mappingForward, timeTrigger);
        this.prefix = prefix;
    }

    /**
     * DOC amaumont Comment method "getMappingForward".
     * 
     * @return
     */
    public String getTriggerTypeStr() {
        return triggerType;
    }

    public static TRIGGER_TYPE get(String triggerTypeStr) {
        TRIGGER_TYPE[] values = values();
        for (int i = 0; i < values.length; i++) {
            TRIGGER_TYPE triggerTypeCurrent = values[i];
            if (triggerTypeCurrent.triggerType.equals(triggerTypeStr)) {
                return triggerTypeCurrent;
            }
        }
        return null;
    }

    /**
     * Getter for timeTrigger.
     * 
     * @return the timeTrigger
     */
    public boolean isTimeTrigger() {
        return this.timeTrigger;
    }

    /**
     * Getter for FileTrigger.
     * 
     * @return the FileTrigger
     */
    public boolean isFileTrigger() {
        return !this.timeTrigger;
    }

    public String getPrefix() {
        return prefix;
    }

    private static String getEmptyString() {
        return EMPTY_STRING;
    }

    public static String getFileTriggerPrefix() {
        return FILE_TRIGGER_PREFIX;
    }

    public static String getFileTriggerParentPrefix() {
        return FILE_TRIGGER_PARENT_PREFIX;
    }

    public static String getTimeTriggerPrefix() {
        return TIME_TRIGGER_PREFIX;
    }

}
