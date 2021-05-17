// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.utils;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.URL;

import org.junit.Test;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.properties.RoutinesJarType;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.RoutineUtils;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.repository.ProjectManager;

public class RoutineUtilsTest {

    @Test
    public void testChangeInnerCodePackage() throws Exception {
        Property jarProperty = PropertiesFactory.eINSTANCE.createProperty();
        String jarLabel = "RoutineUtilsTest_testChangeInnerCodePackage_routinejar1";
        try {
            {
                RoutinesJarItem jarItem = PropertiesFactory.eINSTANCE.createRoutinesJarItem();
                String id = ProxyRepositoryFactory.getInstance().getNextId();
                RoutinesJarType jarType = PropertiesFactory.eINSTANCE.createRoutinesJarType();
                jarItem.setRoutinesJarType(jarType);
                ItemState state = PropertiesFactory.eINSTANCE.createItemState();
                state.setPath("");
                jarItem.setState(state);
                jarProperty.setId(id);
                jarProperty.setLabel(jarLabel);
                jarProperty.setVersion("1.0");
                jarProperty.setItem(jarItem);
                CodesJarResourceCache.addToCache(jarProperty);
            }

            Property property = PropertiesFactory.eINSTANCE.createProperty();
            RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
            property.setItem(routineItem);
            ItemState state = PropertiesFactory.eINSTANCE.createItemState();
            state.setPath("/" + jarLabel);
            routineItem.setState(state);
            RoutinesUtil.setInnerCodes(property, ERepositoryObjectType.ROUTINESJAR);
            URL url = ILibrariesService.get().getRoutineTemplate();
            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            InputStream stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            String content = new String(innerContent);
            String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
            String packageStr = "package org.example." + projectTechName.toLowerCase() + ".routinesjar." + jarLabel.toLowerCase()
                    + ";";
            String tmpPackageStr = "package org.example.test1.routinesjar." + jarLabel.toLowerCase() + ";";
            content = content.replace(packageStr, tmpPackageStr);
            byteArray.setInnerContent(content.getBytes());
            routineItem.setContent(byteArray);

            RoutineUtils.changeInnerCodePackage(routineItem, true);
            String result = new String(routineItem.getContent().getInnerContent());
            assertTrue(result.contains(packageStr));
        } finally {
            CodesJarResourceCache.removeCache(jarProperty);
        }
    }

}
