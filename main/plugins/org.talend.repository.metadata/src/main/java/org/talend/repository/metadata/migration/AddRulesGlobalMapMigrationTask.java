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
package org.talend.repository.metadata.migration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jxl.Workbook;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.migration.AbstractItemMigrationTask;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * DOC gldu class global comment. Detailled comment
 */
public class AddRulesGlobalMapMigrationTask extends AbstractItemMigrationTask {

    public List<ERepositoryObjectType> getTypes() {
        List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
        toReturn.add(ERepositoryObjectType.METADATA_FILE_RULES);
        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        // TODO Auto-generated method stub
        try {
            addNewColumn(item);
            return ExecutionResult.SUCCESS_NO_ALERT;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return ExecutionResult.FAILURE;
        }
    }

    /**
     * DOC gldu Comment method "addNewColumn".
     * 
     * @param item
     */
    private void addNewColumn(Item item) {
        // TODO Auto-generated method stub
        Workbook wb;
        if (!((RulesItem) item).getExtension().equals(".xls")) {
            StringBuffer buffer = new StringBuffer();
            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(((RulesItem) item).getContent().getInnerContent());
            InputStreamReader input = new InputStreamReader(tInputStringStream);
            BufferedReader sb = new BufferedReader(input);
            String line = null;
            String splite = "";
            int i = 0;
            boolean modify = true;
            try {
                while ((line = sb.readLine()) != null) {
                    i++;
                    if (i == 5) {
                        if (line.trim().equals("import java.util.HashMap")) {
                            modify = false;
                        } else {
                            line = line + ("\nimport java.util.HashMap\r\n" + "import java.lang.Integer ");
                        }
                    }
                    if (modify) {
                        if (line.trim().equals("global ArrayList list")) {
                            line = line + ("\nglobal HashMap globalMap" + "\n");
                        } else if (line.trim().indexOf("rule") != -1 && line.indexOf("\"") != -1) {
                            String[] pp = line.split("\"");
                            splite = pp[1];
                        } else if (line.trim().equals("then")) {
                            line = line
                                    + ("\n        Integer count = (Integer)globalMap.get(\"" + splite + "_count\");\r\n"
                                            + "        if(count==null){\r\n" + "           count=1;\r\n" + "        }else{\r\n"
                                            + "        ++count;\r\n" + "        }\r\n" + "        globalMap.put(\"" + splite + "_count\",count);");
                        } else if (line.trim().equals("list.add(output);")) {
                            line = line + "\n        retract(input);";
                        }
                    }
                    buffer.append(line + "\n");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            byteArray.setInnerContent(buffer.toString().getBytes());
            ((RulesItem) item).setContent(byteArray);
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.save(item, true);
            } catch (PersistenceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IProjectMigrationTask#getOrder()
     */
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2010, 7, 05, 17, 41, 10);
        return gc.getTime();
    }

}
