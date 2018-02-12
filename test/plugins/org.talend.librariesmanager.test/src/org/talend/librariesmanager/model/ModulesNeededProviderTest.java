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
package org.talend.librariesmanager.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.model.utils.emf.component.ComponentFactory;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;

/**
 * created by wchen on Sep 1, 2017 Detailled comment
 *
 */
public class ModulesNeededProviderTest {

    @Test
    public void testUpdateModulesNeededForRoutine() throws Exception {
        String jarName1 = "testUpdateModulesNeededForRoutine1.jar";
        String jarName2 = "testUpdateModulesNeededForRoutine2.jar";

        String message = ModuleNeeded.UNKNOWN;

        RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
        routineItem.setProperty(PropertiesFactory.eINSTANCE.createProperty());
        routineItem.getProperty().setLabel("routineTest");
        IMPORTType importJar1 = ComponentFactory.eINSTANCE.createIMPORTType();
        importJar1.setMODULE(jarName1);
        importJar1.setNAME(routineItem.getProperty().getLabel());
        routineItem.getImports().add(importJar1);
        IMPORTType importJar2 = ComponentFactory.eINSTANCE.createIMPORTType();
        importJar2.setMODULE(jarName2);
        importJar2.setNAME(routineItem.getProperty().getLabel());
        routineItem.getImports().add(importJar2);

        // remove old modules from the two lists before test
        String label = ERepositoryObjectType.getItemType(routineItem).getLabel();
        String currentContext = label + " " + routineItem.getProperty().getLabel();
        Set<ModuleNeeded> oldModules = new HashSet<ModuleNeeded>();
        for (ModuleNeeded existingModule : ModulesNeededProvider.getModulesNeeded()) {
            if (currentContext.equals(existingModule.getContext()) || jarName1.equals(existingModule.getModuleName())
                    || jarName2.equals(existingModule.getModuleName())) {
                oldModules.add(existingModule);
            }
        }
        ModulesNeededProvider.getModulesNeeded().removeAll(oldModules);
        oldModules = new HashSet<ModuleNeeded>();
        for (ModuleNeeded existingModule : ModulesNeededProvider.getAllManagedModules()) {
            if (currentContext.equals(existingModule.getContext()) || jarName1.equals(existingModule.getModuleName())
                    || jarName2.equals(existingModule.getModuleName())) {
                oldModules.add(existingModule);
            }
        }
        ModulesNeededProvider.getAllManagedModules().removeAll(oldModules);

        ModulesNeededProvider.addUnknownModules(jarName1, null, false);
        int originalNeededSize = ModulesNeededProvider.getModulesNeeded().size();
        int originalAllSize = ModulesNeededProvider.getAllManagedModules().size();

        ModulesNeededProvider.updateModulesNeededForRoutine(routineItem);
        // add two modules to needed list
        Assert.assertEquals(ModulesNeededProvider.getModulesNeeded().size(), originalNeededSize + 2);
        // add one + change one in the all list
        Assert.assertEquals(ModulesNeededProvider.getAllManagedModules().size(), originalAllSize + 1);

        List<ModuleNeeded> module1 = ModulesNeededProvider.getModulesNeededForName(jarName1);
        List<ModuleNeeded> module2 = ModulesNeededProvider.getModulesNeededForName(jarName2);
        Assert.assertEquals(module1.get(0).getContext(), "Routines " + routineItem.getProperty().getLabel());
        Assert.assertEquals(module2.get(0).getContext(), "Routines " + routineItem.getProperty().getLabel());
    }
}
