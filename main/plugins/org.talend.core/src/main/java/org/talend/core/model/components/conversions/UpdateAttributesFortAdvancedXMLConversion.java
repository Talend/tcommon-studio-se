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
package org.talend.core.model.components.conversions;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC s class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2007-10-25 17:06:40 +0000 xzhang $
 */
public class UpdateAttributesFortAdvancedXMLConversion implements IComponentConversion {

    private static final String ROOT_NAME = "ROOT"; //$NON-NLS-1$

    private static final String GROUP_NAME = "GROUP"; //$NON-NLS-1$

    private static final String LOOP_NAME = "LOOP"; //$NON-NLS-1$

    private static final String PATH = "PATH"; //$NON-NLS-1$

    private static final String ATTRIBUTE = "ATTRIBUTE"; //$NON-NLS-1$

    public void transform(NodeType node) {
        ElementParameterType eleParaRoot = ComponentUtilities.getNodeProperty(node, ROOT_NAME);
        ElementParameterType eleParaGroup = ComponentUtilities.getNodeProperty(node, GROUP_NAME);
        ElementParameterType eleParaLoop = ComponentUtilities.getNodeProperty(node, LOOP_NAME);
        if (eleParaRoot == null || eleParaGroup == null || eleParaLoop == null) {
            return;
        }

        String loopRootPath = null;
        EList elist = eleParaLoop.getElementValue();
        if (elist.size() > 0) {
            ElementValueType ePath = (ElementValueType) elist.get(0);
            loopRootPath = ePath.getValue();
        }
        modifyAttribute(eleParaRoot, loopRootPath);
        modifyAttribute(eleParaGroup, loopRootPath);
        modifyAttribute(eleParaLoop, loopRootPath);
    }

    /**
     * DOC s Comment method "modifyAttribute".
     * 
     * @param eleParaGroup
     * @param loopRootPath
     * @param path
     * @return
     */
    private void modifyAttribute(ElementParameterType eleParaGroup, String loopRootPath) {
        String path = null;
        EList elist = eleParaGroup.getElementValue();
        for (int i = 0; i < elist.size(); i += 4) {
            for (Object value : elist.subList(i, i + 4)) {
                ElementValueType eValue = (ElementValueType) value;
                if (eValue.getElementRef().equals(ATTRIBUTE)) {
                    String attri = eValue.getValue();
                    if (attri.equals("false") || attri.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
                        if ((loopRootPath != null && loopRootPath.startsWith(path)) || (loopRootPath == null && i == 0)) {
                            eValue.setValue("main"); //$NON-NLS-1$
                        } else {
                            eValue.setValue("branch"); //$NON-NLS-1$
                        }
                    } else if (attri.equals("true")) { //$NON-NLS-1$
                        eValue.setValue("attri"); //$NON-NLS-1$
                    }
                } else if (eValue.getElementRef().equals(PATH)) {
                    path = eValue.getValue();
                }
            }
        }
    }
}
