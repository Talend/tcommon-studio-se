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
package org.talend.core;

import org.eclipse.swt.widgets.Control;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.Item;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface ITDQPatternService extends ITDQService {

    static final String PATTERN_NAME = "PATTERN_NAME";

    static final String PATTERN_REGEX = "PATTERN_REGEX";

    static final String PATTERN_ID = "PATTERN_ID";
    /**
     * This method is used in tis to get the patterns value in TDQ.
     * 
     * for the return result, it's a three dimensionality string array. the first is represented for all patterns. the
     * second is represented for expressions for each pattern. the third is one array included three parts:
     * String[][][0] = pattern name String[][][1] = expression body String[][][2] = expression language String[][][3] =
     * pattern path
     * 
     * @return patterns defined in TDQ as String arrays.
     */
    public String[][][] retrieveTDQPatterns();

    /**
     * This method is to override the patter list in a pattern matching component.
     * 
     * @param dbTypeParam
     * @param patternParam
     */
    public void overridePatternList(IElementParameter dbTypeParam, IElementParameter patternParam);

    public boolean selectPattern(IElementParameter typeParam, IElement elem);

 //   public boolean updateJobForPattern(IElement node, Item item);
    
    public String getRegex(IElement node, Item item);

    public String getPatternDisplayName(Item item);

    public boolean isPattern(Object parameter);
    
    public boolean isSameName(Item pattern, String patternName);
    
    public String[] selectPattern(Control cellEditorWindow,IElementParameter dbType);
    
    public boolean isSinglePatternNode(IElement node);
    
    public boolean isMultiPatternNode(IElement node);
}
