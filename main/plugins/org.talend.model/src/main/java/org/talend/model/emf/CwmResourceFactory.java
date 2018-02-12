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
package org.talend.model.emf;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.talend.commons.runtime.model.emf.TalendXMIResource;

/**
 * @author scorreia
 * 
 * This factory only create CwmResource. It can be used in plugin.xml file in order to automatically map the editor to a
 * file name extension.
 * 
 * see http://serdom.szn.pl/ser/?p=6
 */
public class CwmResourceFactory extends XMIResourceFactoryImpl {

    public CwmResourceFactory() {
    }

    @Override
    public Resource createResource(URI uri) {
        String business = "businessProcess";
        String process = "process";
        String joblet = "joblets";
        // PTODO, maybe will bring bugs, like mr job,route, maybe jobscript
        if (validUrl(uri, business) || validUrl(uri, process) || validUrl(uri, joblet)) {
            return new TalendXMIResource(uri);
        } else {
            return new CwmResource(uri);
        }
    }

    private boolean validUrl(URI uri, String str) {
        if (uri.toString().contains("/" + str + "/") && uri.segmentCount() > 2 && uri.segments()[2].equals(str)) {
            return true;
        }
        return false;
    }

}
