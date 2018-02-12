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
package org.talend.core.model.process;

import java.util.List;

import org.eclipse.swt.graphics.Image;

/**
 * created by hwang on Nov 17, 2015 Detailled comment
 *
 */
public interface IContainerEntry {

    public String getLabel();

    public boolean hasChildren();

    public List getChildren();

    public Image getImage();
}
