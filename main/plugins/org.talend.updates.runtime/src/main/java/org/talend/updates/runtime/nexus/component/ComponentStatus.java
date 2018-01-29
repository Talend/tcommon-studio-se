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
package org.talend.updates.runtime.nexus.component;

import java.util.ArrayList;
import java.util.List;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentStatus {

    /*
     * not existed any components.
     */
    private final static int NEW = 1 << 1;

    /*
     * existed same one
     */
    private final static int EXIST = 1 << 2;

    /*
     * it's latest version
     */
    private final static int LATEST_VERSION = 1 << 3;

    /*
     * it's lower version
     */
    private final static int LOWER_VERSION = 1 << 4;

    /*
     * not existed at all, must be new and latest version.
     */
    public final static int NEW_LATEST = NEW | LATEST_VERSION;

    /*
     * not existed at all, but just lower version.
     */
    public final static int NEW_LOWER = NEW | LOWER_VERSION;

    /*
     * existed one and be latest version too.
     */
    public final static int EXIST_LATEST = EXIST | LATEST_VERSION;

    /*
     * existed one, but not latest version.
     */
    public final static int EXIST_LOWER = EXIST | LOWER_VERSION;

    /*
     * unknown
     */
    public final static ComponentStatus NONE = new ComponentStatus();

    private final ComponentIndexBean indexBean;

    private final int statusCode;

    private final List<ComponentIndexBean> exitedComponents = new ArrayList<ComponentIndexBean>();

    private ComponentStatus() {
        this(null, 0);
    }

    public ComponentStatus(int status) {
        this(null, status);
    }

    public ComponentStatus(ComponentIndexBean indexBean, int status) {
        this.indexBean = indexBean;
        this.statusCode = status;
    }

    /**
     * Getter for indexBean.
     * 
     * @return the indexBean
     */
    public ComponentIndexBean getIndexBean() {
        return indexBean;
    }

    /**
     * Getter for statusCode.
     * 
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Getter for exitedComponents.
     * 
     * @return the exitedComponents
     */
    public List<ComponentIndexBean> getExitedComponents() {
        return exitedComponents;
    }

}
