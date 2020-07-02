// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import org.talend.core.model.process.INode;

/**
 * Interface of TDQCryptoFileService
 */
public interface ITDQCryptoFileService extends IService {

    /**
     *  Get Original value if it is context variable
     */
    public String getOriginalValue(INode node, String input);

    /**
     * Check whether all the parameter is validation
     */
    public boolean checkParameterValidation(String cryptoFilePath, String passwordM1);

    /**
     * Get the name of encrypty method
     */
    public String getCryptoMethod();

    /**
     * Generate Encrypty file
     */
    public void generateCryptoFile(String passwordM1, String cryptoMethod, String cryptoFilePath);

}
