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
package org.talend.repository.ui.wizards.metadata.connection.files.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.PackageHelper;
import org.apache.poi.util.TempFile;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.core.model.utils.RepositoryManagerHelper;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class ExcelReader {

    private String excelPath = null;

    private Workbook workbook = null;

    private XSSFWorkbook xwb = null;

    private String[] sheetNamesForXlsx = null;

    private boolean isXlsx = false;

    private int maximumRowsToPreview = RepositoryManagerHelper.getMaximumRowsToPreview();

    private String generationMode = null;

    private static final String EVENT_MODE = "EVENT_MODE";

    private static String[] x = new String[] {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$
            "S", "T", "U", "V", "W", "X", "Y", "Z" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

    public ExcelReader() {

    }

    /**
     * Maybe take long time
     * <p>
     * DOC yexiaowei ExcelReader constructor comment.
     * 
     * @param excel
     * @throws BiffException
     * 
     * @throws IOException
     */
    public ExcelReader(String excel) throws BiffException, IOException {
        this.excelPath = excel;
        init();
    }

    /**
     * 
     * DOC wf ExcelReader constructor comment. add for bug TDI-26614
     * 
     * @param excel
     * @param isSelect
     * @param isForUserMode
     * @throws BiffException
     * @throws IOException
     */
    public ExcelReader(String excel, boolean isXlsx, String generationMode) throws BiffException, IOException {
        this.excelPath = excel;
        this.isXlsx = isXlsx;
        this.generationMode = generationMode;
        init();
    }

    private void init() throws BiffException, IOException {
        // hywang modified for excel 2007
        //        if (excelPath.endsWith(".xls")) { //$NON-NLS-1$
        // isXlsx = false;
        //        } else if (excelPath.endsWith(".xlsx")) { //$NON-NLS-1$
        // isXlsx = true;
        // }

        if (!isXlsx) {
            WorkbookSettings worksetting = new WorkbookSettings();
            //worksetting.setEncoding("ISO-8859-15"); //$NON-NLS-1$
            worksetting.setCellValidationDisabled(true);
            worksetting.setSuppressWarnings(true);
            workbook = Workbook.getWorkbook(new File(excelPath), worksetting);
        } else {
            // modify for bug 12174.
            File file = new File(excelPath);
            OPCPackage clone = null;
            try {
                FileInputStream in = new FileInputStream(file);
                OPCPackage open = OPCPackage.open(in);
                clone = PackageHelper.clone(open, createTempFile());
                open.close();

                // Package createPackage = Package.openOrCreate(file);
                // clone = PackageHelper.clone(createPackage);
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            } catch (OpenXML4JException e) {
                e.printStackTrace();
            }
            if (clone != null) {
                List<String> sheetlist = new ArrayList<String>();
                // modified for bug TDI-26614, Use XSSF and SAX (Event API) to parse excel 2007, only need small memory
                // footprint
                if (isXlsx && (EVENT_MODE).equals(generationMode)) {
                    try {
                        XSSFReader xssfReader = new XSSFReader(clone);
                        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
                        while (sheets.hasNext()) {
                            sheets.next();
                            String sheetName = sheets.getSheetName();
                            sheetlist.add(sheetName);
                        }
                    } catch (OpenXML4JException e) {
                        CommonExceptionHandler.process(e);
                    }
                } else {
                    xwb = new XSSFWorkbook(clone);
                    for (int i = 0; i < xwb.getNumberOfSheets(); i++) {
                        XSSFSheet sheet = xwb.getSheetAt(i);
                        if (sheet == null) {
                            continue;
                        }
                        String name = xwb.getSheetName(i);
                        sheetlist.add(name);
                    }
                }
                sheetNamesForXlsx = new String[sheetlist.size()];
                for (int i = 0; i < sheetlist.size(); i++) {
                    sheetNamesForXlsx[i] = sheetlist.get(i);
                }
                sheetlist.clear();
            }
        }

    }

    /**
     * <li><b>Background</b></li>: After update the poi-ooxml jar from poi-ooxml-3.9 to poi-ooxml-3.11, this api
     * "PackageHelper.createTempFile()" is removed. so need to resolve this compile problem in our codes where calling
     * this api; And I find this function is only used to create a temp file, so I just copy the original source to here <br>
     * <li><b>other information</b></li>: The following function source is copied from the source code of
     * org.apache.poi.util.PackageHelper.createTempFile() <source version: 3.10.1><br>
     * The source can be found from:
     * http://grepcode.com/file/repo1.maven.org/maven2/org.apache.poi/poi-ooxml/3.10.1/org/apache/poi/util/
     * PackageHelper.java?av=f <br>
     * The useage of "PackageHelper.createTempFile()" in our codes is added from TDI-24490
     * 
     * @return
     * @throws IOException
     */
    private File createTempFile() throws IOException {
        File file = TempFile.createTempFile("poi-ooxml-", ".tmp"); //$NON-NLS-1$ //$NON-NLS-2$
        // there is no way to pass an existing file to Package.create(file),
        // delete first, the file will be re-created in Packe.create(file)
        file.delete();
        return file;

    }

    public String[] getSheetNames() {
        if (!this.isXlsx) {
            return workbook.getSheetNames();
        }
        return sheetNamesForXlsx;
    }

    public List<String[]> readSheet(String sheetName) {
        List<String[]> res = new ArrayList<String[]>();
        int recordReadRow = 0;

        // hywang modified for excel 2007
        if (!this.isXlsx) {
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                return null;
            }

            int rows = sheet.getRows();

            for (int i = 0; i < rows; i++) {
                if (recordReadRow >= maximumRowsToPreview) {
                    break;
                }
                Cell[] cells = sheet.getRow(i);
                String[] contents = new String[cells.length];
                for (int j = 0, k = cells.length; j < k; j++) {
                    contents[j] = cells[j].getContents();
                }
                res.add(contents);
                recordReadRow++;
            }
        } else if (isXlsx && (EVENT_MODE).equals(generationMode)) {
            try {
                com.talend.excel.xssf.event.ExcelReader reader = new com.talend.excel.xssf.event.ExcelReader();
                reader.addSheetName(sheetName, false);
                reader.parse(excelPath, "UTF-8");//$NON-NLS-1$
                while (reader.hasNext()) {
                    if (recordReadRow >= maximumRowsToPreview) {
                        reader.stopRead();
                        break;
                    }
                    List<String> row = reader.next();
                    String[] rowContents = new String[row.size()];
                    for (int i = 0; i < row.size(); i++) {
                        String cellContent = row.get(i);
                        if (cellContent != null) {
                            rowContents[i] = cellContent;
                        } else {
                            rowContents[i] = "";//$NON-NLS-1$
                        }
                    }
                    res.add(rowContents);
                    recordReadRow++;
                }
            } catch (Exception e) {
                CommonExceptionHandler.process(e);
            }
        } else {
            XSSFSheet sheet = xwb.getSheet(sheetName);
            for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
                if (recordReadRow >= maximumRowsToPreview) {
                    break;
                }
                Row row = sheet.getRow(i);
                if (row != null) {
                    List<String> contents = new ArrayList<String>();
                    for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                        String cell = null;
                        if (row.getLastCellNum() != row.getPhysicalNumberOfCells()) {
                            if ((j - 1) == 0 && row.getCell(0) == null) {
                                cell = ""; //$NON-NLS-1$
                                contents.add(cell);
                            }
                            if (row.getCell(j) != null && !row.getCell(j).equals("")) { //$NON-NLS-1$
                                row.getPhysicalNumberOfCells();
                                cell = row.getCell(j).toString();
                            } else {
                                cell = ""; //$NON-NLS-1$
                            }
                        } else {
                            if (row.getCell(j) != null && !row.getCell(j).equals("")) { //$NON-NLS-1$
                                row.getPhysicalNumberOfCells();
                                cell = row.getCell(j).toString();
                            } else {
                                cell = ""; //$NON-NLS-1$
                            }
                        }
                        contents.add(cell);
                    }
                    Object[] objs = contents.toArray();
                    String[] rowContents = new String[objs.length];
                    for (int k = 0; k < rowContents.length; k++) {
                        rowContents[k] = objs[k].toString();
                    }
                    res.add(rowContents);
                    recordReadRow++;
                }
            }
        }

        return res.size() <= 0 ? null : res;

    }

    /**
     * Getter for excelPath.
     * 
     * @return the excelPath
     */
    public String getExcelPath() {
        return this.excelPath;
    }

    public static String[] getColumnsTitle(int rows) {
        if (rows <= 0) {
            return new String[0];
        } else if (rows <= 26) {
            String[] res = new String[rows];
            System.arraycopy(x, 0, res, 0, rows);
            return res;
        } else {
            return getAllColumnsTitle(rows);
        }
    }

    /**
     * DOC zli Comment method "getColumnsTitle".
     * 
     * @param s
     * @param rows
     * @return
     */
    public static String[] getColumnsTitle(int index, int rows) {
        if (index > 26 * 26 + 26 || index < 1) {
            return new String[0];
        }
        if (index == 1) {
            return getColumnsTitle(rows);
        }

        index = index - 1;
        int newRows = rows + index;

        if (rows <= 0) {
            return new String[0];
        } else if (newRows <= 26) {
            String[] res = new String[rows];
            System.arraycopy(x, index, res, 0, rows);
            return res;
        } else {
            String[] res = getAllColumnsTitle(newRows);
            String[] res2 = new String[rows];
            System.arraycopy(res, index, res2, 0, rows);
            return res2;
        }
    }

    public static String[] getAllColumnsTitle(int rows) {
        String[] res = new String[rows];
        System.arraycopy(x, 0, res, 0, 26);
        int offset = 26;
        FirstLoop: for (String first : x) {
            for (String second : x) {
                String rowName = first + second;
                res[offset] = rowName;
                offset++;
                if (offset == rows) {
                    break FirstLoop;
                }
            }
        }
        if (rows > 26 * 26 + 26) {
            SecondLoop: for (String first : x) {
                for (String second : x) {
                    for (String three : x) {
                        String rowName = first + second + three;
                        res[offset] = rowName;
                        offset++;
                        if (offset == rows) {
                            break SecondLoop;
                        }
                    }
                }
            }
        }
        return res;
    }
}
