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
package org.talend.core.repository.model;

import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.talend.commons.runtime.model.emf.provider.ResourceHandler;
import org.talend.commons.runtime.model.emf.provider.ResourceOption;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.repository.item.ItemProductValuesHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProductValuesResourceHandler extends ResourceHandler {

    @Override
    public void preSave(Object resource, OutputStream outputStream, Map<?, ?> options) {
        if (resource instanceof XMLResource) {
            final Property prop = (Property) EcoreUtil.getObjectByType(((XMLResource) resource).getContents(),
                    PropertiesPackage.eINSTANCE.getProperty());
            if (prop != null) {
                /*
                 * need ignore, when:
                 * 
                 * 1) import, will do create also, and created and modified keys will be set in migration task. and set
                 * the import date in ItemProductValuesHelper. Else, if existed already nothing to do.
                 * 
                 * 2) migrate in 2 cases, when import, will do point 1. when logon, just do migration task
                 */

                if (!options.containsKey(ResourceOption.ITEM_IMPORTATION.getName())
                        && !options.containsKey(ResourceOption.DEMO_IMPORTATION.getName())
                        && !options.containsKey(ResourceOption.MIGRATION.getName())) {

                    Date saveDate = new Date();
                    if (options.containsKey(ResourceOption.CREATATION.getName())) {
                        ItemProductValuesHelper.setValuesWhenCreate(prop, saveDate);
                    }

                    // if no any keys, do migration too.
                    // currently, especially when copy/paste items, if no migration task to do
                    ItemProductValuesHelper.setValuesWhenMigrate(prop);

                    // generally, work for modification in studio
                    ItemProductValuesHelper.setValuesWhenModify(prop, saveDate);
                }

                // always remove the date when save
                prop.setCreationDate(null);
                prop.setModificationDate(null);
            }
        }
    }
}
