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
package org.talend.commons.exception;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIException;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class XMILoadException extends XMIException {

    private static final long serialVersionUID = -6728490459268675754L;

    private final Resource resource;

    private final int evenType;

    public XMILoadException(String message, int evenType, Resource res) {
        super(message, res != null && res.getURI() != null ? res.getURI().toString() : null, 0, 0);
        this.evenType = evenType;
        this.resource = res;
    }

    public Resource getResource() {
        return resource;
    }

    public int getEvenType() {
        return evenType;
    }

}
