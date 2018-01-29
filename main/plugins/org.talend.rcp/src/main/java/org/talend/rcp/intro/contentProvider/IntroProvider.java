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
package org.talend.rcp.intro.contentProvider;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.w3c.dom.Element;

/**
 * DOC wchen class global comment. Detailled comment
 */
public abstract class IntroProvider implements IIntroXHTMLContentProvider {

    protected String branding = System.getProperty("talend.license.branding");

    protected boolean isItemShow(String type) {
        if ("ANALYSIS".equals(type)) {
            IPerspectiveDescriptor pd = PlatformUI.getWorkbench().getPerspectiveRegistry()
                    .findPerspectiveWithId(IBrandingConfiguration.PERSPECTIVE_DQ_ID);
            if (pd != null) {
                return true;
            } else {
                return false;
            }
        } else if ("SERVICES".equals(type) || "ROUTE".equals(type)) {
            if (ERepositoryObjectType.valueOf(ERepositoryObjectType.class, type) != null) {
                return true;
            } else {
                return false;
            }
        } else if ("MDM.DataModel".equals(type)) {
            return ERepositoryObjectType.valueOf(ERepositoryObjectType.class, type) != null;
        }
        return false;
    }

    protected void setTDStyle(Element td) {
        String defaultBranding = System.getProperty("talend.branding.type");
        String color = null;
        if ("DI".equals(defaultBranding)) {
            color = "#c9282d";
        } else if ("DQ".equals(defaultBranding)) {
            color = "#ffa11b";
        } else if ("MDM".equals(defaultBranding)) {
            color = "#00a8e1";
        }
        if (color == null) {
            // other brandings use the gloable css
            td.setAttribute("class", "separator");
        } else {
            String style = "border-left: 2px solid " + color + ";padding-left:5px;margin-left:15px;";
            td.setAttribute("style", style);
        }

    }
}
