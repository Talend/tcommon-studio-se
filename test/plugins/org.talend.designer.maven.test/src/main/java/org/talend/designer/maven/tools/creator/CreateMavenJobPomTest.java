// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.tools.creator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.junit.Test;
import org.mockito.Mockito;
import org.talend.designer.runprocess.IProcessor;

/*
 * Created by bhe on May 9, 2020
 */
public class CreateMavenJobPomTest {

    @Test
    public void testNormalizeSpaces() throws Exception {
        String inputSh = "#!/bin/sh\n" + "cd `dirname $0`\n" + "ROOT_PATH=`pwd`\n"
                + "java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib  -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.12.1.jar:$ROOT_PATH/../lib/log4j-api-2.12.1.jar:$ROOT_PATH/../lib/log4j-core-2.12.1.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/crypto-utils.jar:$ROOT_PATH/../lib/talend_file_enhanced-1.1.jar:$ROOT_PATH/../lib/slf4j-api-1.7.25.jar:$ROOT_PATH/../lib/dom4j-2.1.1.jar:$ROOT_PATH/nojvmparam_0_1.jar: local_project.nojvmparam_0_1.noJVMparam --context=Default \"$@\"\n";
        String expectSh = "#!/bin/sh\n" + "cd `dirname $0`\n" + "ROOT_PATH=`pwd`\n"
                + "java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.12.1.jar:$ROOT_PATH/../lib/log4j-api-2.12.1.jar:$ROOT_PATH/../lib/log4j-core-2.12.1.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/crypto-utils.jar:$ROOT_PATH/../lib/talend_file_enhanced-1.1.jar:$ROOT_PATH/../lib/slf4j-api-1.7.25.jar:$ROOT_PATH/../lib/dom4j-2.1.1.jar:$ROOT_PATH/nojvmparam_0_1.jar: local_project.nojvmparam_0_1.noJVMparam --context=Default \"$@\"\n";

        String inputBat = "%~d0\n" + "cd %~dp0\n"
                + "java -Dtalend.component.manager.m2.repository=\"%cd%/../lib\"  -cp .;../lib/routines.jar;../lib/log4j-slf4j-impl-2.12.1.jar;../lib/log4j-api-2.12.1.jar;../lib/log4j-core-2.12.1.jar;../lib/antlr-runtime-3.5.2.jar;../lib/org.talend.dataquality.parser.jar;../lib/crypto-utils.jar;../lib/talend_file_enhanced-1.1.jar;../lib/slf4j-api-1.7.25.jar;../lib/dom4j-2.1.1.jar;nojvmparam_0_1.jar; local_project.nojvmparam_0_1.noJVMparam --context=Default %*\n";
        String expectBat = "%~d0\n" + "cd %~dp0\n"
                + "java -Dtalend.component.manager.m2.repository=\"%cd%/../lib\" -cp .;../lib/routines.jar;../lib/log4j-slf4j-impl-2.12.1.jar;../lib/log4j-api-2.12.1.jar;../lib/log4j-core-2.12.1.jar;../lib/antlr-runtime-3.5.2.jar;../lib/org.talend.dataquality.parser.jar;../lib/crypto-utils.jar;../lib/talend_file_enhanced-1.1.jar;../lib/slf4j-api-1.7.25.jar;../lib/dom4j-2.1.1.jar;nojvmparam_0_1.jar; local_project.nojvmparam_0_1.noJVMparam --context=Default %*\n";

        String actualSh = CreateMavenJobPom.normalizeSpaces(inputSh);
        assertEquals(expectSh, actualSh);

        String actualBat = CreateMavenJobPom.normalizeSpaces(inputBat);
        assertEquals(expectBat, actualBat);

    }

    @Test
    public void testIsLatestVersionOrLowerVersionInChildJob() {
        CreateMavenJobPom createMavenJobPom = new CreateMavenJobPom(Mockito.mock(IProcessor.class), null);

        String httpcomponents = "org.apache.httpcomponents";
        String httpclient = "httpclient";
        String httpcore = "httpcore";
        Dependency depdencyClient4505 = toDepdency(new String[] { httpcomponents, httpclient, "4.5.5" });
        Dependency depdencyClient4510 = toDepdency(new String[] { httpcomponents, httpclient, "4.5.10"});
        Dependency depdencyClient4512 = toDepdency(new String[]{ httpcomponents, httpclient, "4.5.12"});
        Dependency depdencyCore4403 = toDepdency(new String[]{ httpcomponents, httpcore, "4.4.3"});
        Dependency depdencyCore4409 = toDepdency(new String[]{ httpcomponents, httpcore, "4.4.9"});
        Dependency depdencyCore4413 = toDepdency(new String[]{ httpcomponents, httpcore, "4.4.13"});

        // only one parent job,has no duplicated dependencies
        Map<String, Set<Dependency>> childJobDependencies = new HashMap<String, Set<Dependency>>(2);
        List<Dependency> depList = Arrays.asList(depdencyClient4505,depdencyCore4409);
        Set<Dependency> parentJobDependencies = new HashSet<Dependency>(depList);
        
        Map<String, Set<Dependency>> duplicateLibs = new HashMap<String, Set<Dependency>>();
        fillDuplicateLibs(createMavenJobPom, duplicateLibs, parentJobDependencies);

        // only one job, no children, has not duplicated dependencies
        for(Dependency dep:parentJobDependencies) {
            assertTrue(createMavenJobPom
                    .isLatestVersionOrLowerVersionInChildJob(parentJobDependencies, childJobDependencies, duplicateLibs, dep));
        }

        // only one job, no children, has duplicated dependencies
        depList = Arrays.asList(depdencyClient4505,depdencyClient4512,depdencyCore4409,depdencyCore4413);
        parentJobDependencies = new HashSet<Dependency>(depList);
        fillDuplicateLibs(createMavenJobPom, duplicateLibs, parentJobDependencies);
        for(Dependency dep:parentJobDependencies) {
            if("4.5.5".equals(dep.getVersion()) || "4.4.9".equals(dep.getVersion())) {
                assertFalse(createMavenJobPom
                    .isLatestVersionOrLowerVersionInChildJob(parentJobDependencies, childJobDependencies, duplicateLibs, dep));
            } else {
                assertTrue(createMavenJobPom
                        .isLatestVersionOrLowerVersionInChildJob(parentJobDependencies, childJobDependencies, duplicateLibs, dep));
            }
        }
        
     // parent job, with children, has duplicated dependencies
        duplicateLibs.clear();
        depList = Arrays.asList(depdencyClient4512, depdencyCore4413);
        parentJobDependencies = new HashSet<Dependency>(depList);
        fillDuplicateLibs(createMavenJobPom, duplicateLibs, Arrays.asList(depdencyClient4505, depdencyClient4510,
                depdencyClient4512, depdencyCore4403, depdencyCore4409, depdencyCore4413));

        childJobDependencies.put("childrenstr1", new HashSet<Dependency>(Arrays.asList(depdencyClient4505,depdencyClient4510,depdencyCore4403)));
        childJobDependencies.put("childrenstr2", new HashSet<Dependency>(Arrays.asList(depdencyClient4505,depdencyCore4403,depdencyCore4409)));
        for(Dependency dep:Arrays.asList(depdencyClient4512,depdencyCore4413,depdencyClient4505, depdencyClient4510,depdencyCore4403,depdencyCore4409)) {
            assertTrue(createMavenJobPom
                    .isLatestVersionOrLowerVersionInChildJob(parentJobDependencies, childJobDependencies, duplicateLibs, dep));
        }
    }

    private void fillDuplicateLibs(CreateMavenJobPom createMavenJobPom, Map<String, Set<Dependency>> duplicateLibs,
            Collection<Dependency> fromJobDependencies) {
        for (Dependency dep : fromJobDependencies) {
            String coordinate = createMavenJobPom.getCoordinate(dep.getGroupId(), dep.getArtifactId(), dep.getType(), null,
                    dep.getClassifier());
            Set<Dependency> dependencys = duplicateLibs.get(coordinate);
            if (dependencys == null) {
                dependencys = new HashSet<Dependency>();
                duplicateLibs.put(coordinate, dependencys);
            }
            dependencys.add(dep);
        }
    }

    private Dependency toDepdency(String[] deparr) {
        Dependency dep = new Dependency();
        dep.setGroupId(deparr[0]);
        dep.setArtifactId(deparr[1]);
        dep.setVersion(deparr[2]);
        if (deparr.length == 4) {
            dep.setClassifier(deparr[3]);
        }
        return dep;
    }
}
