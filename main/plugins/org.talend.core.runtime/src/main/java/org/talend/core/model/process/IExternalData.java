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
package org.talend.core.model.process;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.talend.core.model.process.node.IExternalMapEntry;
import org.talend.core.model.process.node.IExternalMapTable;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public interface IExternalData extends Serializable, Cloneable {

    /**
     * 
     * cli ExternalDataType class global comment. Detailled comment
     */
    public enum ExternalDataType {
        OUTPUT,
        INPUT,
        VAR,
        //
        ;
    }

    public IExternalData clone() throws CloneNotSupportedException;

    /**
     * 
     * cli Comment method "getExpressionColumns".
     * 
     * find the column name which include this expression.
     */
    public Map<IExternalMapTable, List<IExternalMapEntry>> getExpressionColumns(String expression, ExternalDataType... types);

    public List<String> getJoinedTableNames(String mainTable);

    public List<? extends IExternalMapTable> getOutputTables();

    public List<? extends IExternalMapTable> getInputTables();

    public List<? extends IExternalMapTable> getVarsTables();
}
