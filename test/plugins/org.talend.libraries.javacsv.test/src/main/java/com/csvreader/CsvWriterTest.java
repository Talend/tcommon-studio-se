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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link com.csvreader.CsvWriter}
 * 
 */
public class CsvWriterTest {

    static final String[] EXPECTED_HEADERS = { "Label", "Purpose", "Description", "Author", "Relative_Path", "All_DB_Regexp", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            "DB2_Regexp", "MySQL_Regexp", "Oracle_Regexp", "PostgreSQL_Regexp", "SQL_Server_Regexp", "Sybase_Regexp", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            "Ingres_Regexp", "Informix_Regexp", "MDM_Informix_Regexp", "SQLite3_Regexp", "Teradata_Regexp", "Java_Regexp", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            "Category", "Access", "AS400", "CLASS_NAME_TEXT", "JAR_FILE_PATH" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    /**
     * The 3rd field contains New Line Symbol and quotes. In the CSV file, the New Line Symbols refers to new lines, and
     * the quotes is actually doubled as below:
     * <p/>
     * will find data such <br/>
     * as ""aaa"", ""abbbb"", ""bbbc"", ""affffd"".
     * 
     * The regex field contains "\1". Unlike Java, they should not be doubled in the CSV file:
     * <p/>
     * "'^.*([a-zA-Z])\1\1.*$'"
     * 
     */
    static final String[] EXPECTED_VALUES = {
            "Repeated_Characters", "find data with the same characters repeated at least 3 times", //$NON-NLS-1$ //$NON-NLS-2$
            "will find data such\n" + " as \"aaa\", \"abbbb\", \"bbbc\", \"affffd\".", "s@t.c", "", "", "", "", "", "", "", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
            "", "", "", "", "", "", "'^.*([a-zA-Z])\\1\\1.*$'", "", "", "", "", "" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

    static final char CURRENT_SEPARATOR = '\t';

    private static final String CSV_File_Write = "resources/Repeated_Characters_Write.csv"; //$NON-NLS-1$

    private static final boolean USE_TEXT_QUAL = true;

    private static final char TEXT_QUAL = '"';

    private static CsvReader reader;

    private static CsvWriter writer;

    @Before
    public void setUp() throws Exception {

        writer = new CsvWriter(new FileWriter(CSV_File_Write), CURRENT_SEPARATOR);
        writer.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);
        writer.setTextQualifier(TEXT_QUAL);
        writer.setForceQualifier(USE_TEXT_QUAL);
    }

    /**
     * Test method for {@link com.csvreader.CsvWriter#writeRecord(java.lang.String[])}.
     * 
     * @throws IOException
     */
    @Test
    public void testWriteRecordStringArray() throws IOException {

        // write to file
        writer.writeRecord(EXPECTED_HEADERS);
        writer.writeRecord(EXPECTED_VALUES);
        writer.endRecord();
        writer.close();

        // read from file
        reader = new CsvReader(new FileReader(CSV_File_Write), CURRENT_SEPARATOR);
        reader.setEscapeMode(CsvReader.ESCAPE_MODE_DOUBLED);

        // compare headers
        assertTrue(reader.readHeaders());
        String[] headers = reader.getHeaders();
        assertEquals(EXPECTED_HEADERS.length, headers.length);
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            assertEquals(EXPECTED_HEADERS[i], headers[i]);
        }

        // compare values
        assertTrue(reader.readRecord());
        String[] values = reader.getValues();
        assertEquals(EXPECTED_VALUES.length, values.length);
        for (int i = 0; i < EXPECTED_VALUES.length; i++) {
            assertEquals(EXPECTED_VALUES[i], values[i]);
        }

        reader.close();
    }

}
