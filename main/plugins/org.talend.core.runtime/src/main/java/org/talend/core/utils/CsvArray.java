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
package org.talend.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Detailled comment <br/>
 * 
 * @author ftang, 07-25-2007
 * 
 */
public class CsvArray {

    private static final String ENCODING = "ISO-8859-15"; //$NON-NLS-1$

    private List<String[]> rows;

    public CsvArray createFrom(File file) throws IOException {
        return createFrom(new java.io.FileInputStream(file), ENCODING);
    }

    public CsvArray createFrom(InputStream is) throws IOException {
        return createFrom(is, ENCODING);
    }

    public CsvArray createFrom(File file, final String encoding) throws IOException {
        return createFrom(new java.io.FileInputStream(file), encoding);
    }

    public CsvArray createFrom(InputStream is, final String encoding) throws IOException {
        CsvArray array = new CsvArray();

        com.talend.csv.CSVReader reader = new com.talend.csv.CSVReader(new BufferedReader(new InputStreamReader(is,
                encoding == null ? ENCODING : encoding)), ';');

        reader.setQuoteChar('\"');

        reader.setEscapeChar('\\');
        while (reader.readNext()) {
            array.add(reader.getValues());
        }

        return array;
    }

    public void add(String[] row) {
        rows.add(row);
    }

    public List<String[]> getRows() {
        return rows;
    }

    /**
     * Constructs a new XmlArray.
     */
    public CsvArray() {
        super();
        rows = new ArrayList<String[]>();
    }

    // public static void main(String[] args) throws IOException {
    // CsvArray c = new CsvArray();
    // c = c.createFrom(new File("e:/testOraout.csv"));
    // System.out.println(c.getRows().size());
    // for (String[] string : c.getRows()) {
    // for (String string2 : string) {
    // System.out.print(string2 + " ");
    // }
    // System.out.println();
    //
    // }
    //
    // }
}
