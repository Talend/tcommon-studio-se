// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.io.Serializable;

public enum TraceStatusBean implements TraceBean, Serializable {

    UI_STATUS,
    ID_STATUS,
    PAUSE,
    NEXT_ROW,
    NEXT_BREAKPOINT,
    STATUS_OK,
    STATUS_WAITING;

    public boolean equals(TraceBean traceBean) {
        if (traceBean != null && traceBean instanceof TraceStatusBean) {
            if (this == ((TraceStatusBean) traceBean)) {
                return true;
            }
        }
        return false;
    }
}
