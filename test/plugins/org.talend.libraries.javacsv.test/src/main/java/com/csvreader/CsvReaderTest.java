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
package com.csvreader;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link com.csvreader.CsvReader}
 * 
 */
public class CsvReaderTest {

    private static CsvReader reader;

    private static String CSV_File_Ref = "resources/Repeated_Characters_Ref.csv";; //$NON-NLS-1$

    @Before
    public void setUp() throws FileNotFoundException {
        reader = new CsvReader(new FileReader(CSV_File_Ref), CsvWriterTest.CURRENT_SEPARATOR);

        // this is the default escape mode, set it here only for information
        reader.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);

    }

    @After
    public void tearDown() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * Test method for {@link com.csvreader.CsvReader#getHeaders()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetHeaders() throws IOException {
        assertTrue(reader.readHeaders());
        String[] headers = reader.getHeaders();
        assertEquals(CsvWriterTest.EXPECTED_HEADERS.length, headers.length);
        for (int i = 0; i < CsvWriterTest.EXPECTED_HEADERS.length; i++) {
            assertEquals(CsvWriterTest.EXPECTED_HEADERS[i], headers[i]);
        }
    }

    /**
     * Test method for {@link com.csvreader.CsvReader#getValues()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetValues() throws IOException {
        assertTrue(reader.readHeaders());
        assertTrue(reader.readRecord());
        String[] values = reader.getValues();
        assertEquals(CsvWriterTest.EXPECTED_VALUES.length, values.length);
        for (int i = 0; i < CsvWriterTest.EXPECTED_VALUES.length; i++) {
            assertEquals(CsvWriterTest.EXPECTED_VALUES[i], values[i]);
        }
    }
}
