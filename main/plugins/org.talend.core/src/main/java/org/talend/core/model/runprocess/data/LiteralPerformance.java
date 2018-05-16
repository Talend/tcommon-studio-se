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
package org.talend.core.model.runprocess.data;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.process.EConnectionType;

/**
 * DOC zwei class global comment. Detailled comment
 */
public class LiteralPerformance extends CommonPerformance {

    public LiteralPerformance(EConnectionType eConnectionType) {
        super(eConnectionType);
    }

    private static final String COLOR_OK = "#229922"; //$NON-NLS-1$

    private static final String COLOR_ERROR = "#AA3322"; //$NON-NLS-1$

    private static final String COLOR_TRUE = "#229922"; //$NON-NLS-1$

    private static final String COLOR_FALSE = "#AA3322"; //$NON-NLS-1$

    private String label;

    @Override
    public String getLabel(String msg) {
        if (StringUtils.isEmpty(msg)) {
            // handle by super class
            return super.getLabel(msg);
        }

        String[] part = msg.split("\\|"); //$NON-NLS-1$
        if (part != null && part.length == 3) {
            // update label
            // String oldLabel = label;
            label = createHtmlText(part[1]);
            // firePropertyChange(LABEL_PROP, oldLabel, label);

        }
        return this.label;
    }

    public String createHtmlText(String literal) {
        StringBuilder html = new StringBuilder();
        String pattern = "<font style='font-size:11px' color='%1$s'>%2$s</font><br>"; //$NON-NLS-1$
        html.append(String.format(pattern, getColorStatus(literal), literal));
        return html.toString();
    }

    private String getColorStatus(String status) {
        if ("true".equalsIgnoreCase(status)) { //$NON-NLS-1$
            return COLOR_TRUE;
        } else if ("false".equalsIgnoreCase(status)) { //$NON-NLS-1$
            return COLOR_FALSE;
        } else if ("ok".equalsIgnoreCase(status)) { //$NON-NLS-1$
            return COLOR_OK;
        } else if ("error".equalsIgnoreCase(status)) { //$NON-NLS-1$
            return COLOR_ERROR;
        }

        return COLOR_FALSE;
    }
}
