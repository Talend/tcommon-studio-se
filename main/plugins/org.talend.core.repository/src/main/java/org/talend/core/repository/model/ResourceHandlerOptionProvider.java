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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.talend.commons.runtime.model.emf.provider.EmfResourcesFactoryReader;
import org.talend.commons.runtime.model.emf.provider.OptionProvider;
import org.talend.commons.runtime.model.emf.provider.ResourceHandler;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ResourceHandlerOptionProvider extends OptionProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.runtime.model.emf.provider.EOptionProvider#getName()
     */
    @Override
    public String getName() {
        return XMLResource.OPTION_RESOURCE_HANDLER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.runtime.model.emf.provider.EOptionProvider#getValue()
     */
    @Override
    public Object getValue() {
        return new XMLResource.ResourceHandler() {

            ResourceHandler[] handlers;

            ResourceHandler[] getResourceHandlers() {
                if (handlers == null) {
                    synchronized (ResourceHandlerOptionProvider.class) {
                        if (handlers == null) {
                            handlers = EmfResourcesFactoryReader.INSTANCE.getResourceHandlers();
                        }
                    }
                }
                return handlers;
            }

            @Override
            public void preLoad(XMLResource resource, InputStream inputStream, Map<?, ?> options) {
                for (ResourceHandler handler : getResourceHandlers()) {
                    handler.preLoad(resource, inputStream, options);
                }
            }

            @Override
            public void postLoad(XMLResource resource, InputStream inputStream, Map<?, ?> options) {
                for (ResourceHandler handler : getResourceHandlers()) {
                    handler.postLoad(resource, inputStream, options);
                }
            }

            @Override
            public void preSave(XMLResource resource, OutputStream outputStream, Map<?, ?> options) {
                for (ResourceHandler handler : getResourceHandlers()) {
                    handler.preSave(resource, outputStream, options);
                }
            }

            @Override
            public void postSave(XMLResource resource, OutputStream outputStream, Map<?, ?> options) {
                for (ResourceHandler handler : getResourceHandlers()) {
                    handler.postSave(resource, outputStream, options);
                }

            }

        };
    }
}
