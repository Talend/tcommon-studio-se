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
package org.talend.core.language;

import java.util.List;

import org.talend.core.model.process.Problem;
import org.talend.designer.codegen.IAloneProcessNodeConfigurer;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: ICodeProblemsChecker.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface ICodeProblemsChecker {

    public abstract List<Problem> checkProblemsForExpression(String code);

    /**
     * 
     * Force code generation, check all problems and load problems in cache.
     * 
     * @param externalData TODO
     * @return
     */
    public abstract List<Problem> checkProblems(IAloneProcessNodeConfigurer nodeConfigurer);

    /**
     * 
     * Force code generation, check problems which matches with given key and load all problems in cache.
     * 
     * @param key
     * @param externalData
     * @return
     */
    public abstract List<Problem> checkProblemsFromKey(String key, IAloneProcessNodeConfigurer nodeConfigurer);

    /**
     * 
     * get problemList for red error mark.
     * 
     * @param key
     * @return
     */
    public abstract List<Problem> checkProblemsForErrorMark(String key, IAloneProcessNodeConfigurer nodeConfigurer);

    /**
     * 
     * Use cache of problems and get problems.
     * 
     * @param key
     * @return
     */
    public abstract List<Problem> getProblemsFromKey(String key);

}
