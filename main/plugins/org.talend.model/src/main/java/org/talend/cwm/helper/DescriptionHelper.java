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
package org.talend.cwm.helper;

import org.eclipse.emf.common.util.EList;
import orgomg.cwm.foundation.businessinformation.BusinessinformationFactory;
import orgomg.cwm.foundation.businessinformation.Description;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author scorreia
 * 
 * Utility class for element Descriptions.
 * @deprecated this class has never been used. Please, make sure that it's really the class you need. If yes, then
 * remove the deprecated flag.
 */
public final class DescriptionHelper {

    private DescriptionHelper() {
    }

    /**
     * Method "getTypedDescriptionString".
     * 
     * @param type the type of the searched description
     * @param element a CWM element
     * @return the description or null
     */
    public static String getTypedDescriptionString(String type, ModelElement element) {
        Description descr = getTypedDescription(type, element);
        return (descr != null) ? descr.getBody() : null;
    }

    public static Description getTypedDescription(String type, ModelElement element) {
        assert type != null;
        EList<Description> descriptions = element.getDescription();
        for (Description description : descriptions) {
            if (type.equals(description.getType())) {
                return description;
            }
        }
        return null;
    }

    public static void setTypedDescription(String type, String description, ModelElement element) {
        assert type != null;
        Description descr = getTypedDescription(type, element);
        if (descr != null) {
            descr.setBody(description);
        } else {
            element.getDescription().add(createDescription(type, description));
        }
    }

    /**
     * Method "createDescription".
     * 
     * @param type the type of the description
     * @param body the body of the description
     * @return the created description
     */
    public static Description createDescription(String type, String body) {
        Description description = BusinessinformationFactory.eINSTANCE.createDescription();
        description.setType(type);
        description.setBody(body);
        return description;
    }
}
