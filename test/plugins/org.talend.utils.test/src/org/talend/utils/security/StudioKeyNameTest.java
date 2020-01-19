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
package org.talend.utils.security;

import static org.junit.Assert.*;

import org.junit.Test;

/*
* Created by bhe on Jan 17, 2020
*/
public class StudioKeyNameTest {

    @Test
    public void testConstructor() throws Exception {

        String[] validNames = new String[] { StudioKeyName.KEY_SYSTEM_DEFAULT, StudioKeyName.KEY_ROUTINE,
                StudioKeyName.KEY_ROUTINE_PREFIX, StudioKeyName.KEY_SYSTEM_PREFIX, StudioKeyName.KEY_MIGRATION,
                StudioKeyName.KEY_MIGRATION_TOKEN };
        for (String n : validNames) {
            StudioKeyName sn = new StudioKeyName(n);
            assertNotNull(sn);
        }

        // invalid name
        try {
            new StudioKeyName("some name");
            fail("invalid key name, should fail");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }

        // version part is not a number
        try {
            new StudioKeyName(StudioKeyName.KEY_SYSTEM_PREFIX + "a");
            fail("invalid key name, should fail");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }

        // version part is a negative number
        try {
            new StudioKeyName(StudioKeyName.KEY_SYSTEM_PREFIX + "-1");
            fail("invalid key name, should fail");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetVersionNumber() throws Exception {
        String[] validNames = new String[] { StudioKeyName.KEY_SYSTEM_DEFAULT, StudioKeyName.KEY_ROUTINE };
        for (String n : validNames) {
            StudioKeyName sn = new StudioKeyName(n);
            assertEquals(1, sn.getVersionNumber());
        }

        StudioKeyName sn1 = new StudioKeyName(StudioKeyName.KEY_MIGRATION);
        assertEquals(0, sn1.getVersionNumber());

        StudioKeyName sn2 = new StudioKeyName(StudioKeyName.KEY_MIGRATION_TOKEN);
        assertEquals(0, sn2.getVersionNumber());

        StudioKeyName sn3 = new StudioKeyName(StudioKeyName.KEY_SYSTEM_PREFIX + "11");
        assertEquals(11, sn3.getVersionNumber());
    }

    @Test
    public void testKeyName() throws Exception {
        StudioKeyName sn1 = new StudioKeyName(StudioKeyName.KEY_SYSTEM_DEFAULT);
        assertTrue(sn1.isSystemKey());
        assertFalse(sn1.isRoutineKey());
        assertFalse(sn1.isDefaultRoutineKey());
        assertEquals(StudioKeyName.KEY_SYSTEM_PREFIX, sn1.getKeyNamePrefix());

        StudioKeyName sn2 = new StudioKeyName(StudioKeyName.KEY_ROUTINE);
        assertFalse(sn2.isSystemKey());
        assertTrue(sn2.isRoutineKey());
        assertTrue(sn2.isDefaultRoutineKey());
        assertEquals(StudioKeyName.KEY_ROUTINE_PREFIX, sn2.getKeyNamePrefix());

        StudioKeyName sn3 = new StudioKeyName(StudioKeyName.KEY_MIGRATION);
        assertFalse(sn3.isSystemKey());
        assertFalse(sn3.isRoutineKey());
        assertFalse(sn3.isDefaultRoutineKey());
        assertEquals("", sn3.getKeyNamePrefix());

        StudioKeyName sn4 = new StudioKeyName(StudioKeyName.KEY_MIGRATION_TOKEN);
        assertFalse(sn4.isSystemKey());
        assertFalse(sn4.isRoutineKey());
        assertFalse(sn4.isDefaultRoutineKey());
        assertEquals("", sn4.getKeyNamePrefix());
    }
}
