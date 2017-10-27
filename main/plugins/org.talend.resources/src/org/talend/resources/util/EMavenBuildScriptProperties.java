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
package org.talend.resources.util;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 * This is a mapping enum for variable and maven properties.
 */
public enum EMavenBuildScriptProperties {
    ItemGroupName,
    ItemProjectName,
    ItemName,
    ItemVersion,
    //
    ItemExportedJarName,
    //
    BundleConfigPrivatePackage,
    BundleConfigExportPackage,
    BundleConfigExportService,
    BundleConfigImportPackage,
    BundleConfigBundleClasspath,
    //
    ;

    EMavenBuildScriptProperties() {
    }

    public String getVarName() {
        return this.name();
    }

    public String getVarScript() {
        return '@' + this.getVarName() + '@';
    }

}
