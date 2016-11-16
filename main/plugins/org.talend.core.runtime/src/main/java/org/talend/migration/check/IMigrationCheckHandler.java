// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.migration.check;

import java.util.List;

public interface IMigrationCheckHandler {

    public String getType();

    public void setType(String type);

    public String getName();

    public String setName(String name);

    public List<MigrateItemInfo> checkMigration() throws Exception;
}
