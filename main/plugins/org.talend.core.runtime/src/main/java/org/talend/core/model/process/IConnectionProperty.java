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

import org.eclipse.swt.graphics.RGB;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 */
public interface IConnectionProperty {

    public RGB getRGB();

    public void setRGB(RGB rgb);

    public Integer getLineStyle();

    public void setLineStyle(Integer lineStyle);

    public String getLinkedComponent();

    public void setLinkedComponent(String linkedComponent);
}
