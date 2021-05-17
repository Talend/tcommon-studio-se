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
package org.talend.core.runtime;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IAdditionalInfo {

    Object getInfo(final String key);

    void putInfo(final String key, final Object value);

    void onEvent(final String event, final Object... parameters);

    default Object func(final String funcName, final Object... params) throws Exception {
        throw new UnsupportedOperationException();
    }

    void cloneAddionalInfoTo(final IAdditionalInfo targetAdditionalInfo);

}
