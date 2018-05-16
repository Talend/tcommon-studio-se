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
package org.talend.core.repository.model;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.repository.constants.FileConstants;

/**
 * 
 */
public class PropertiesProjectResourceImpl extends XMIResourceImpl {

    private static final String REF = "projectReference"; //$NON-NLS-1$

    public PropertiesProjectResourceImpl(URI uri) {
        super(uri);
    }

    @Override
    protected boolean useUUIDs() {
        return true;
    }

    @Override
    protected XMLSave createXMLSave() {
        return new XMISaveImpl(createXMLHelper()) {

            @Override
            public void traverse(List<? extends EObject> contents) {
                for (EObject object : contents) {
                    unloadProjectReferences(object);
                }
                super.traverse(contents);
            }
        };
    }

    private void unloadProjectReferences(EObject object) {
        if (!object.eClass().equals(PropertiesPackage.eINSTANCE.getProjectReference())) {
            return;
        }

        // "unload" projectreference.referencedproject
        // we replace it with a proxy which has the good uri to serialize
        // and to avoid using the id of the project in the other file
        // because the id could change
        ProjectReference projectReference = (ProjectReference) object;
        InternalEObject proxy = (InternalEObject) PropertiesFactory.eINSTANCE.createProject();
        String pathName = "/" + projectReference.getReferencedProject().getTechnicalLabel() + "/" //$NON-NLS-1$ //$NON-NLS-2$
                + FileConstants.LOCAL_PROJECT_FILENAME;
        URI uri = URI.createPlatformResourceURI(pathName, false).appendFragment(REF);
        proxy.eSetProxyURI(uri);
        projectReference.setReferencedProject((org.talend.core.model.properties.Project) proxy);
    }

    @Override
    public EObject getEObject(String uriFragment) {
        if (uriFragment.startsWith(REF)) {
            // if there was a special serialization for the project reference
            // then return the good object
            return (EObject) EcoreUtil.getObjectByType(contents, PropertiesPackage.eINSTANCE.getProject());
        }
        return super.getEObject(uriFragment);
    }
}
