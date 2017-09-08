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
package org.talend.core.model.process;

import org.eclipse.swt.graphics.RGB;
import org.talend.core.runtime.i18n.Messages;

/**
 * Different types of connections in Talend. <br/>
 * 
 * $Id: EConnectionType.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public enum EConnectionType implements IConnectionCategory, IDraw2dGraphics {

    FLOW_MAIN(0, "FLOW", //$NON-NLS-1$
              EConnectionCategory.MAIN,
              MAIN | DATA | FLOW | CUSTOM_NAME | UNIQUE_NAME,
              "Main", Messages.getString("EConnectionType.mainMenu"), //$NON-NLS-1$ //$NON-NLS-2$
              new Integer(LINE_SOLID),
              new RGB(230, 100, 0)),

    RUN_AFTER(2, "AFTER", //$NON-NLS-1$
              EConnectionCategory.OTHER,
              EXECUTION_ORDER | DEPENDENCY,
              "RunAfter", Messages.getString("EConnectionType.runAfterMenu"), //$NON-NLS-1$ //$NON-NLS-2$ 
              new Integer(LINE_SOLID),
              new RGB(100, 100, 100)),

    RUN_IF(6, "RUN_IF", //$NON-NLS-1$
           EConnectionCategory.OTHER,
           CONDITION | DEPENDENCY | CUSTOM_NAME | UNIQUE_NAME,
           "If", Messages.getString("EConnectionType.runIfMenu"), //$NON-NLS-1$ //$NON-NLS-2$
           new Integer(LINE_DASHDOTDOT),
           new RGB(180, 100, 30)),

    ON_SUBJOB_OK(1, "SUBJOB_OK", //$NON-NLS-1$
                 EConnectionCategory.OTHER,
                 EXECUTION_ORDER | DEPENDENCY | UNIQUE_NAME,
                 "OnSubjobOk", Messages.getString("EConnectionType.onSubjobOkMenu"), //$NON-NLS-1$ //$NON-NLS-2$
                 new Integer(LINE_SOLID),
                 new RGB(0, 100, 0)),

    ON_SUBJOB_ERROR(4, "SUBJOB_ERROR", //$NON-NLS-1$
                    EConnectionCategory.OTHER,
                    EXECUTION_ORDER | DEPENDENCY | UNIQUE_NAME,
                    "OnSubjobError", Messages.getString("EConnectionType.onSubjobErrorMenu"), //$NON-NLS-1$ //$NON-NLS-2$
                    new Integer(LINE_SOLID),
                    new RGB(128, 0, 0)),

    ON_COMPONENT_OK(3, "COMPONENT_OK", //$NON-NLS-1$
                    EConnectionCategory.OTHER,
                    CONDITION | DEPENDENCY | UNIQUE_NAME,
                    "OnComponentOk", Messages.getString("EConnectionType.onComponentOkMenu"), //$NON-NLS-1$ //$NON-NLS-2$
                    new Integer(LINE_SOLID),
                    new RGB(0, 150, 0)),

    ON_COMPONENT_ERROR(5, "COMPONENT_ERROR", //$NON-NLS-1$
                       EConnectionCategory.OTHER,
                       CONDITION | DEPENDENCY | UNIQUE_NAME,
                       "OnComponentError", Messages.getString("EConnectionType.onComponentErrorMenu"), //$NON-NLS-1$ //$NON-NLS-2$
                       new Integer(LINE_SOLID),
                       new RGB(200, 0, 0)),

    ON_ROWS_END(11, "ROWS_END", //$NON-NLS-1$
                EConnectionCategory.MAIN,
                MAIN | USE_ITERATE,
                "OnRowsEnd", Messages.getString("EConnectionType.onRowsEndMenu"), //$NON-NLS-1$ //$NON-NLS-2$
                new Integer(LINE_SOLID),
                new RGB(0, 150, 0)),

    ITERATE(7, "ITERATE", //$NON-NLS-1$
            EConnectionCategory.MAIN,
            MAIN | USE_ITERATE | UNIQUE_NAME,
            "Iterate", Messages.getString("EConnectionType.iteratorMenu"), //$NON-NLS-1$ //$NON-NLS-2$
            new Integer(LINE_SOLID),
            new RGB(100, 230, 0)),

    FLOW_REF(8, "LOOKUP", //$NON-NLS-1$
             EConnectionCategory.OTHER,
             DATA | FLOW | CUSTOM_NAME | USE_HASH | UNIQUE_NAME,
             "Lookup", Messages.getString("EConnectionType.lookupMenu"), //$NON-NLS-1$ //$NON-NLS-2$
             new Integer(LINE_DOT),
             new RGB(230, 100, 0)),

    TABLE(9, "TABLE", //$NON-NLS-1$
          EConnectionCategory.MAIN,
          MAIN | DATA | CUSTOM_NAME | IConnectionCategory.TABLE,
          "Table", Messages.getString("EConnectionType.tableMenu"), //$NON-NLS-1$ //$NON-NLS-2$
          new Integer(LINE_SOLID),
          new RGB(0, 150, 100)),

    FLOW_MERGE(10, "MERGE", //$NON-NLS-1$
               EConnectionCategory.MAIN,
               MAIN | DATA | FLOW | CUSTOM_NAME | UNIQUE_NAME | MERGE,
               "Merge", Messages.getString("EConnectionType.mergeMenu"), //$NON-NLS-1$ //$NON-NLS-2$
               new Integer(LINE_DASHDOT),
               new RGB(230, 100, 0)),

    SYNCHRONIZE(12, "SYNCHRONIZE", //$NON-NLS-1$
                EConnectionCategory.MAIN,
                EXECUTION_ORDER | DEPENDENCY,
                "Synchronize", Messages.getString("EConnectionType.synchronize"), //$NON-NLS-1$ //$NON-NLS-2$
                new Integer(LINE_DASH),
                new RGB(0, 50, 150)),
    PARALLELIZE(13, "PARALLELIZE", //$NON-NLS-1$
                EConnectionCategory.MAIN,
                EXECUTION_ORDER | DEPENDENCY,
                "Parallelize", Messages.getString("EConnectionType.parallelize"), //$NON-NLS-1$ //$NON-NLS-2$
                new Integer(LINE_DASH),
                new RGB(0, 50, 150)),
    ROUTE(14, "ROUTE", //$NON-NLS-1$
          EConnectionCategory.MAIN,
          CAMEL | UNIQUE_NAME | CUSTOM_NAME,
          "Route", Messages.getString("EConnectionType.route"), //$NON-NLS-1$ //$NON-NLS-2$
          new Integer(LINE_SOLID),
          new RGB(230, 100, 0)),
    ROUTE_WHEN(15, "ROUTE_WHEN", //$NON-NLS-1$
               EConnectionCategory.MAIN,
               CONDITION | CAMEL | UNIQUE_NAME | CUSTOM_NAME,
               "When", Messages.getString("EConnectionType.when"), //$NON-NLS-1$ //$NON-NLS-2$
               new Integer(LINE_SOLID),
               new RGB(230, 100, 0)),
    ROUTE_OTHER(16, "ROUTE_OTHER", //$NON-NLS-1$
                EConnectionCategory.MAIN,
                CAMEL | UNIQUE_NAME | CUSTOM_NAME,
                "Otherwise", Messages.getString("EConnectionType.otherwise"), //$NON-NLS-1$ //$NON-NLS-2$
                new Integer(LINE_SOLID),
                new RGB(230, 100, 0)),
    ROUTE_TRY(17, "ROUTE_TRY", //$NON-NLS-1$
              EConnectionCategory.MAIN,
              CAMEL | UNIQUE_NAME | CUSTOM_NAME,
              "Try", "Try", //$NON-NLS-1$ //$NON-NLS-2$
              new Integer(LINE_SOLID),
              new RGB(0, 100, 0)),
    ROUTE_CATCH(18, "ROUTE_CATCH", //$NON-NLS-1$
                EConnectionCategory.MAIN,
                CONDITION | CAMEL | UNIQUE_NAME | CUSTOM_NAME,
                "Catch", "Catch", //$NON-NLS-1$ //$NON-NLS-2$
                new Integer(LINE_SOLID),
                new RGB(128, 0, 0)),
    ROUTE_FINALLY(19, "ROUTE_FINALLY", //$NON-NLS-1$
                  EConnectionCategory.MAIN,
                  CAMEL | UNIQUE_NAME | CUSTOM_NAME,
                  "Finally", "Finally", //$NON-NLS-1$ //$NON-NLS-2$
                  new Integer(LINE_SOLID),
                  new RGB(0, 0, 160)),
    ROUTE_ENDBLOCK(20, "ROUTE_ENDBLOCK", //$NON-NLS-1$
                   EConnectionCategory.MAIN,
                   CAMEL | UNIQUE_NAME | CUSTOM_NAME,
                   "Route", "Route", //$NON-NLS-1$ //$NON-NLS-2$
                   new Integer(LINE_SOLID),
                   new RGB(230, 100, 0)),

    STARTS(21, "STARTS", EConnectionCategory.OTHER, CONDITION | DEPENDENCY | UNIQUE_NAME, "Starts", Messages//$NON-NLS-1$ //$NON-NLS-2$
            .getString("EConnectionType.startsMenu"), new Integer(LINE_SOLID), new RGB(0, 0, 150)), //$NON-NLS-1$ 

    REJECT(0, "REJECT", //$NON-NLS-1$
           EConnectionCategory.MAIN,
           MAIN | DATA | FLOW | CUSTOM_NAME | UNIQUE_NAME,
           "Reject", Messages.getString("EConnectionType.RejectMenu"), //$NON-NLS-1$ //$NON-NLS-2$
           new Integer(LINE_SOLID),
           new RGB(230, 100, 0)),

    TABLE_REF(22, "TABLE_REF", //$NON-NLS-1$
              // EConnectionCategory.OTHER,
              EConnectionCategory.MAIN,
              MAIN | DATA | CUSTOM_NAME | IConnectionCategory.TABLE,
              "Table_ref", Messages.getString("EConnectionType.tableRefMenu"), //$NON-NLS-1$ //$NON-NLS-2$
              new Integer(LINE_DOT),
              new RGB(0, 150, 100));

    private String name;

    private int id;

    private EConnectionCategory category;

    private int connectionCategory;

    private String defaultMenuName;

    private String defaultLinkName;

    /**
     * @see org.eclipse.draw2d.Graphics
     */
    private Integer defaultLineStyle;

    private RGB rgb;

    EConnectionType(int id, String name, EConnectionCategory category, int connectionCategory, String linkName, String menuName,
            Integer lineStyle, RGB rgb) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.connectionCategory = connectionCategory;
        this.defaultLinkName = linkName;
        this.defaultMenuName = menuName;
        this.defaultLineStyle = lineStyle;
        this.rgb = rgb;
    }

    public static EConnectionType getTypeFromId(int id) {
        EConnectionType[] listConnectionType = EConnectionType.values();
        for (EConnectionType element : listConnectionType) {
            if ((element.getId()) == id) {
                return element;
            }
        }
        // Default Value
        return EConnectionType.FLOW_MAIN;
    }

    public static EConnectionType getTypeFromName(String name) {
        EConnectionType[] listConnectionType = EConnectionType.values();
        for (EConnectionType element : listConnectionType) {
            if (element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    /**
     * Getter for category.
     * 
     * @return the category
     * @deprecated
     */
    @Deprecated
    public EConnectionCategory getCategory() {
        return this.category;
    }

    public boolean hasConnectionCategory(int category) {
        return (connectionCategory & category) != 0;
    }

    /**
     * Getter for defaultColor.
     * 
     * @return the defaultColor
     */
    public RGB getRGB() {
        return rgb;
    }

    /**
     * Getter for defaultLineStyle.
     * 
     * @return the defaultLineStyle
     */
    public Integer getDefaultLineStyle() {
        return defaultLineStyle;
    }

    /**
     * Getter for defaultLinkName.
     * 
     * @return the defaultLinkName
     */
    public String getDefaultLinkName() {
        return defaultLinkName;
    }

    /**
     * Getter for defaultMenuName.
     * 
     * @return the defaultMenuName
     */
    public String getDefaultMenuName() {
        return defaultMenuName;
    }
}
