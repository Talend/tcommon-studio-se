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
package org.talend.core.runtime;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IAdditionalInfo {

    Object getInfo(String key);

    void setInfo(String key, Object value);

    void onEvent(String event, Object... parameters);

}
