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
package org.talend.commons.runtime.model.emf;

import java.nio.charset.StandardCharsets;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.talend.commons.runtime.model.emf.provider.OptionProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class UTF8EncodingOptionProvider extends OptionProvider {

    @Override
    public String getName() {
        return XMLResource.OPTION_ENCODING;
    }

    @Override
    public Object getValue() {
        return StandardCharsets.UTF_8.name();
    }

}
