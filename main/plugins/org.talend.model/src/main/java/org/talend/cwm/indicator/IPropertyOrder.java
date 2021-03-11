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
package org.talend.cwm.indicator;

import java.util.List;

/**
 * Create by zshen Make DataValidation support order
 */
public interface IPropertyOrder {

    /**
     *
     * Create by zshen judge whether current exist a order
     *
     * @return whether current exist a order
     */
    boolean isWork();

    /**
     *
     * Create by zshen Setting whether curtent oder is work
     *
     *
     */
    void setWork(boolean isWork);

    /**
     *
     * create by zshen add current value into order list.
     *
     * @param
     *
     * @return true when current value be added successful else return false
     */
    boolean add(Object[] element);

    /**
     *
     * create by zshen getResult list.
     *
     * @return list take order
     */
    List<Object[]> getResult();

}
