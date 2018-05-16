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
package org.talend.commons.utils.data.bean;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * @param <B> Bean object
 * @param <V> Value of the bean's property
 */
public interface IGetterPropertyAccessor<B, V> {

    /**
     * 
     * You must implement this method.
     * 
     * @return
     */
    public V get(B bean);

}
