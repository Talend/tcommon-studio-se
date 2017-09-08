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

import orgomg.cwm.foundation.businessinformation.Description;
import orgomg.cwm.foundation.businessinformation.Document;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 *Helper to simplify the SAPFunctionUnit usage <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class SAPFunctionHelper {

    /**
     * return the Document associated with ModelElement. delagating to ModelElementHelper.getFirstDocument();
     * 
     * @param me the ModelElement instance to get the document from.
     * @return the first document never null
     */
    public static Document getFirstDocument(ModelElement me) {
        return ModelElementHelper.getFirstDocument(me);
    }

    /**
     * Return the first description for the modelelement, creates one if none is found<br>
     * delegate to ModelElementHelper.getFirstDescription();
     * 
     * @return the first description of the ModelElement, never null
     */
    public static Description getFirstDescription(ModelElement modelElement) {
        return ModelElementHelper.getFirstDescription(modelElement);
    }

}
