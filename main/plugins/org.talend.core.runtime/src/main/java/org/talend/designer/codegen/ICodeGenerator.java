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
package org.talend.designer.codegen;

import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.INode;
import org.talend.core.model.temp.ECodePart;

/**
 * CodeGenerator Interface
 * 
 * $Id: CodeGenerator.java 636 2006-11-21 03:34:52 +0000 (星期二, 21 十一月 2006) ftang $
 * 
 */
public interface ICodeGenerator {

    /**
     * Generate the code for the process given to the constructor.
     * 
     * @return
     * @throws CodeGeneratorException
     */
    public String generateProcessCode() throws SystemException;

    /**
     * Parse Process, and generate Code for Context Variables.
     */
    public String generateContextCode(IContext designerContext) throws SystemException;

    /**
     * Generate Part Code for a given Component.
     * 
     * @param config
     * @param node
     * @return
     * @throws JETException
     * @throws CoreException
     */
    public String generateComponentCode(INode node, ECodePart part) throws SystemException;

    /**
     * Generate Part Code for a given node, with optional nodeConfigurer.
     * 
     * @param node
     * @param nodeConfigurer allows to configure thr process node, can be null
     * @return generated code
     */
    public String generateComponentCodeWithRows(String nodeName, IAloneProcessNodeConfigurer nodeConfigurer);

    public void setContextName(String contextName);

}
