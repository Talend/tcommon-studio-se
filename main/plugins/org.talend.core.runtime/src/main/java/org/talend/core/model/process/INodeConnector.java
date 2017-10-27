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
package org.talend.core.model.process;

import org.eclipse.swt.graphics.RGB;

/**
 * Interface for Links between Nodes in a Process. <br/>
 * 
 * $Id: INodeConnector.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface INodeConnector {

    public EConnectionType getDefaultConnectionType();

    public void setDefaultConnectionType(final EConnectionType defaultConnectionType);

    public IConnectionProperty getConnectionProperty(EConnectionType type);

    public int getMaxLinkOutput();

    public void setMaxLinkOutput(final int maxLinkOutput);

    public int getMaxLinkInput();

    public void setMaxLinkInput(final int maxLinkInput);

    public int getMinLinkInput();

    public void setMinLinkInput(int minLinkInput);

    public int getMinLinkOutput();

    public void setMinLinkOutput(int minLinkOutput);

    /**
     * Built In field allow to create new schemas directly in the right click on the component. A Built In schema will
     * always be Dynamic.
     * 
     * @return
     */
    public boolean isBuiltIn();

    public void setBuiltIn(final boolean builtIn);

    /**
     * Dynamic is set when the component can have several schemas on one connector.
     * 
     * @return
     */
    public boolean isMultiSchema();

    public void setMultiSchema(final boolean multi);

    public boolean isMergeAllowDifferentSchema();

    public void setMergeAllowDifferentSchema(final boolean mergeOption);

    public int getCurLinkNbInput();

    public void setCurLinkNbInput(final int curLinkNb);

    public int getCurLinkNbOutput();

    public void setCurLinkNbOutput(final int curLinkNb);

    public String getName();

    public void setName(String name);

    public String getLinkName();

    public void setLinkName(String linkName);

    public String getMenuName();

    public void setMenuName(String menuName);

    public String getBaseSchema();

    public void setNotShowIf(String notShowIf);

    public String getNotShowIf();

    public boolean isShow();

    public void setShow(boolean show);

    public String getShowIf();

    public void setShowIf(String showIf);

    public void setBaseSchema(String baseSchema);

    /**
     * DOC qzhang Comment method "addConnectionProperty".
     * 
     * @param flow_ref
     * @param rgb
     * @param defaultLineStyle
     */
    public void addConnectionProperty(EConnectionType flowRef, RGB rgb, Integer defaultLineStyle);
}
