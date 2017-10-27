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
package org.talend.model.emf;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * @author scorreia
 * 
 * This resource generates UUIDs for each object.
 * 
 * See http://serdom.szn.pl/ser/?p=6
 */
public class CwmResource extends XMIResourceImpl {

    public static final String ENCODING = "UTF-8"; //$NON-NLS-1$

    public CwmResource() {
        super();
    }

    public CwmResource(URI uri) {
        super(uri);
    }

    @Override
    protected boolean useUUIDs() {
        return true;
    }

    // overriding to force UTF-8 encoding for Cwm resources
    @Override
    protected void init() {
        encoding = ENCODING;
        xmlVersion = "1.0";
    }

}
