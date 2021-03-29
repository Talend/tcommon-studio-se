package org.talend.designer.maven.tools.creator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CreateMavenJobPomTest {
    
    @Test
    public void testNormalizeSpaces() throws Exception {
        String inputSh = "#!/bin/sh\n" + "cd `dirname $0`\n" + "ROOT_PATH=`pwd`\n"
                + "java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib  -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.12.1.jar:$ROOT_PATH/../lib/log4j-api-2.12.1.jar:$ROOT_PATH/../lib/log4j-core-2.12.1.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/crypto-utils.jar:$ROOT_PATH/../lib/talend_file_enhanced_20070724.jar:$ROOT_PATH/../lib/slf4j-api-1.7.25.jar:$ROOT_PATH/../lib/dom4j-2.1.1.jar:$ROOT_PATH/nojvmparam_0_1.jar: local_project.nojvmparam_0_1.noJVMparam --context=Default \"$@\"\n";
        String expectSh = "#!/bin/sh\n" + "cd `dirname $0`\n" + "ROOT_PATH=`pwd`\n"
                + "java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.12.1.jar:$ROOT_PATH/../lib/log4j-api-2.12.1.jar:$ROOT_PATH/../lib/log4j-core-2.12.1.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/crypto-utils.jar:$ROOT_PATH/../lib/talend_file_enhanced_20070724.jar:$ROOT_PATH/../lib/slf4j-api-1.7.25.jar:$ROOT_PATH/../lib/dom4j-2.1.1.jar:$ROOT_PATH/nojvmparam_0_1.jar: local_project.nojvmparam_0_1.noJVMparam --context=Default \"$@\"\n";

        String inputBat = "%~d0\n" + "cd %~dp0\n"
                + "java -Dtalend.component.manager.m2.repository=\"%cd%/../lib\"  -cp .;../lib/routines.jar;../lib/log4j-slf4j-impl-2.12.1.jar;../lib/log4j-api-2.12.1.jar;../lib/log4j-core-2.12.1.jar;../lib/antlr-runtime-3.5.2.jar;../lib/org.talend.dataquality.parser.jar;../lib/crypto-utils.jar;../lib/talend_file_enhanced_20070724.jar;../lib/slf4j-api-1.7.25.jar;../lib/dom4j-2.1.1.jar;nojvmparam_0_1.jar; local_project.nojvmparam_0_1.noJVMparam --context=Default %*\n";
        String expectBat = "%~d0\n" + "cd %~dp0\n"
                + "java -Dtalend.component.manager.m2.repository=\"%cd%/../lib\" -cp .;../lib/routines.jar;../lib/log4j-slf4j-impl-2.12.1.jar;../lib/log4j-api-2.12.1.jar;../lib/log4j-core-2.12.1.jar;../lib/antlr-runtime-3.5.2.jar;../lib/org.talend.dataquality.parser.jar;../lib/crypto-utils.jar;../lib/talend_file_enhanced_20070724.jar;../lib/slf4j-api-1.7.25.jar;../lib/dom4j-2.1.1.jar;nojvmparam_0_1.jar; local_project.nojvmparam_0_1.noJVMparam --context=Default %*\n";

        String actualSh = CreateMavenJobPom.normalizeSpaces(inputSh);
        assertEquals(expectSh, actualSh);

        String actualBat = CreateMavenJobPom.normalizeSpaces(inputBat);
        assertEquals(expectBat, actualBat);

    }
    
    //https://jira.talendforge.org/browse/TUP-24422
    @Test
    public void testCarriageReturn() throws Exception {
        String inputSh = "#!/bin/sh\r\n" + "cd `dirname $0`\r\n" + "ROOT_PATH=`pwd`\r\n"
                + "java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib  -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.12.1.jar:$ROOT_PATH/../lib/log4j-api-2.12.1.jar:$ROOT_PATH/../lib/log4j-core-2.12.1.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/crypto-utils.jar:$ROOT_PATH/../lib/talend_file_enhanced_20070724.jar:$ROOT_PATH/../lib/slf4j-api-1.7.25.jar:$ROOT_PATH/../lib/dom4j-2.1.1.jar:$ROOT_PATH/nojvmparam_0_1.jar: local_project.nojvmparam_0_1.noJVMparam --context=Default \"$@\"\n";
        String expectSh = "#!/bin/sh\n" + "cd `dirname $0`\n" + "ROOT_PATH=`pwd`\n"
                + "java -Dtalend.component.manager.m2.repository=$ROOT_PATH/../lib -cp .:$ROOT_PATH:$ROOT_PATH/../lib/routines.jar:$ROOT_PATH/../lib/log4j-slf4j-impl-2.12.1.jar:$ROOT_PATH/../lib/log4j-api-2.12.1.jar:$ROOT_PATH/../lib/log4j-core-2.12.1.jar:$ROOT_PATH/../lib/antlr-runtime-3.5.2.jar:$ROOT_PATH/../lib/org.talend.dataquality.parser.jar:$ROOT_PATH/../lib/crypto-utils.jar:$ROOT_PATH/../lib/talend_file_enhanced_20070724.jar:$ROOT_PATH/../lib/slf4j-api-1.7.25.jar:$ROOT_PATH/../lib/dom4j-2.1.1.jar:$ROOT_PATH/nojvmparam_0_1.jar: local_project.nojvmparam_0_1.noJVMparam --context=Default \"$@\"\n";

        String inputBat = "%~d0\r\n" + "cd %~dp0\r\n"
                + "java -Dtalend.component.manager.m2.repository=\"%cd%/../lib\"  -cp .;../lib/routines.jar;../lib/log4j-slf4j-impl-2.12.1.jar;../lib/log4j-api-2.12.1.jar;../lib/log4j-core-2.12.1.jar;../lib/antlr-runtime-3.5.2.jar;../lib/org.talend.dataquality.parser.jar;../lib/crypto-utils.jar;../lib/talend_file_enhanced_20070724.jar;../lib/slf4j-api-1.7.25.jar;../lib/dom4j-2.1.1.jar;nojvmparam_0_1.jar; local_project.nojvmparam_0_1.noJVMparam --context=Default %*\n";
        String expectBat = "%~d0\n" + "cd %~dp0\n"
                + "java -Dtalend.component.manager.m2.repository=\"%cd%/../lib\" -cp .;../lib/routines.jar;../lib/log4j-slf4j-impl-2.12.1.jar;../lib/log4j-api-2.12.1.jar;../lib/log4j-core-2.12.1.jar;../lib/antlr-runtime-3.5.2.jar;../lib/org.talend.dataquality.parser.jar;../lib/crypto-utils.jar;../lib/talend_file_enhanced_20070724.jar;../lib/slf4j-api-1.7.25.jar;../lib/dom4j-2.1.1.jar;nojvmparam_0_1.jar; local_project.nojvmparam_0_1.noJVMparam --context=Default %*\n";

        String actualSh = CreateMavenJobPom.normalizeSpaces(inputSh);
        assertEquals(expectSh, actualSh);

        String actualBat = CreateMavenJobPom.normalizeSpaces(inputBat);
        assertEquals(expectBat, actualBat);

    }

}
