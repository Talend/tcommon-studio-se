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
package org.talend.presentation.onboarding.ui.runtimedata;

import java.util.Map;

/**
 * created by cmeng on Sep 28, 2015 Detailled comment
 *
 */
public class OnBoardingCommandBean {

    private String commandId;

    private Map<String, String> commandParameters;

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
        if (this.commandId != null) {
            this.commandId = this.commandId.trim();
        }
    }

    public Map<String, String> getCommandParameters() {
        return this.commandParameters;
    }

    public void setCommandParameters(Map<String, String> commandParameters) {
        this.commandParameters = commandParameters;
    }

}
