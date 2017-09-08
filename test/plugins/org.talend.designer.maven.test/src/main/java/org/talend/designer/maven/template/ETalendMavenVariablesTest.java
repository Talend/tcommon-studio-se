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
package org.talend.designer.maven.template;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ETalendMavenVariablesTest {

    @Test
    public void testReplaceVariables() {
        Assert.assertNull(ETalendMavenVariables.replaceVariables(null, null, false));
        Assert.assertEquals("", ETalendMavenVariables.replaceVariables("", null, false));
        Assert.assertEquals("@abc@", ETalendMavenVariables.replaceVariables("@abc@", null, false));
        Assert.assertEquals("", ETalendMavenVariables.replaceVariables("@abc@", null, true));
        Assert.assertEquals("", ETalendMavenVariables.replaceVariables("@abc@@xyz@", null, true));

        Map<ETalendMavenVariables, String> variablesValuesMap = new HashMap<ETalendMavenVariables, String>();
        Assert.assertEquals("@abc@@xyz@", ETalendMavenVariables.replaceVariables("@abc@@xyz@", variablesValuesMap, false));
        Assert.assertEquals("", ETalendMavenVariables.replaceVariables("@abc@@xyz@", variablesValuesMap, true));

        variablesValuesMap.put(ETalendMavenVariables.ProjectName, "TEST");
        Assert.assertEquals(" TESTxyz",
                ETalendMavenVariables.replaceVariables("@abc@ @ProjectName@xyz", variablesValuesMap, true));
        Assert.assertEquals(" TESTxyz@",
                ETalendMavenVariables.replaceVariables("@abc@ @ProjectName@xyz@", variablesValuesMap, true));
        Assert.assertEquals(" TESTxyz",
                ETalendMavenVariables.replaceVariables("@abc@ @ProjectName@xyz@@", variablesValuesMap, true));
        Assert.assertEquals(" TESTxyz",
                ETalendMavenVariables.replaceVariables("@abc@ @ProjectName@xyz@ @", variablesValuesMap, true));

        // FIXME? will try to find the valid variables first to replace.
        Assert.assertEquals("@abcTESTxyz@",
                ETalendMavenVariables.replaceVariables("@abc@ProjectName@xyz@", variablesValuesMap, false));
        Assert.assertEquals("@abcTESTxyz@123",
                ETalendMavenVariables.replaceVariables("@abc@ProjectName@xyz@123", variablesValuesMap, false));
        // FIXME? first will replace valid variable, so it's "@abcTESTxyz@", then clean up will clean all
        Assert.assertEquals("", ETalendMavenVariables.replaceVariables("@abc@ProjectName@xyz@", variablesValuesMap, true));
        Assert.assertEquals("123", ETalendMavenVariables.replaceVariables("@abc@ProjectName@xyz@123", variablesValuesMap, true));

        Assert.assertEquals(" ProjectName",
                ETalendMavenVariables.replaceVariables("@abc@ ProjectName@xyz@", variablesValuesMap, true));

    }

    @Test
    public void testReplaceVariable() {
        Assert.assertNull(ETalendMavenVariables.replaceVariable(null, null, null, false));
        Assert.assertEquals("", ETalendMavenVariables.replaceVariable("", null, null, false));
        Assert.assertEquals("@abc@",
                ETalendMavenVariables.replaceVariable("@abc@", ETalendMavenVariables.ProjectName, null, false));
        Assert.assertEquals("", ETalendMavenVariables.replaceVariable("@abc@", ETalendMavenVariables.ProjectName, null, true));
        Assert.assertEquals("",
                ETalendMavenVariables.replaceVariable("@abc@@xyz@", ETalendMavenVariables.ProjectName, null, true));
        Assert.assertEquals("123",
                ETalendMavenVariables.replaceVariable("123@ProjectName@", ETalendMavenVariables.ProjectName, null, true));
        Assert.assertEquals("123@abc@",
                ETalendMavenVariables.replaceVariable("123@ProjectName@@abc@", ETalendMavenVariables.ProjectName, null, false));
        Assert.assertEquals("123",
                ETalendMavenVariables.replaceVariable("123@ProjectName@@abc@", ETalendMavenVariables.ProjectName, null, true));
        Assert.assertEquals("123",
                ETalendMavenVariables.replaceVariable("123@ProjectName@@abc@", ETalendMavenVariables.ProjectName, "", true));
        Assert.assertEquals("123TEST",
                ETalendMavenVariables.replaceVariable("123@ProjectName@@abc@", ETalendMavenVariables.ProjectName, "TEST", true));
        Assert.assertEquals("123TEST ",
                ETalendMavenVariables.replaceVariable("123@ProjectName@ @abc@", ETalendMavenVariables.ProjectName, "TEST", true));
        Assert.assertEquals("123ProjectName ", ETalendMavenVariables.replaceVariable("123@abc@ProjectName @abc@",
                ETalendMavenVariables.ProjectName, "TEST", true));
    }

    @Test
    public void testCleanupVariable() {
        Assert.assertNull(ETalendMavenVariables.cleanupVariable(null));
        Assert.assertEquals("", ETalendMavenVariables.cleanupVariable(""));
        Assert.assertEquals("abc", ETalendMavenVariables.cleanupVariable("abc"));
        Assert.assertEquals("@abc", ETalendMavenVariables.cleanupVariable("@abc"));
        Assert.assertEquals("abc@", ETalendMavenVariables.cleanupVariable("abc@"));
        Assert.assertEquals("", ETalendMavenVariables.cleanupVariable("@abc@"));
        Assert.assertEquals("a", ETalendMavenVariables.cleanupVariable("a@bc@"));
        Assert.assertEquals("123", ETalendMavenVariables.cleanupVariable("@abc@123@xyz@"));
        Assert.assertEquals("a123", ETalendMavenVariables.cleanupVariable("a@bc@123@xyz@"));
        Assert.assertEquals("a123\t", ETalendMavenVariables.cleanupVariable("a@bc@123\t@xyz@"));
        Assert.assertEquals("a123\n", ETalendMavenVariables.cleanupVariable("a@bc@123\n@xyz@"));
    }
}
