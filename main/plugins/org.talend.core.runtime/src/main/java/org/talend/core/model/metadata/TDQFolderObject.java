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
package org.talend.core.model.metadata;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;

public class TDQFolderObject extends RepositoryViewObject {

    private final IRepositoryViewObject viewObject;

    private ETDQFolderType folderType;

    private ERepositoryObjectType type;

    public ERepositoryObjectType getType() {
        return this.type;
    }

    public enum ETDQFolderType {
        COLUMN_FOLDER("Columns"),
        TABLE_FOLDER("Tables"),
        VIEW_FOLDER("Views"),
        ANA_ELEMENT_FOLDER("Analysis"),
        ANA_CONTAINER_FOLDER("AnaContainer"),
        INDICATOR_FOLDER("Indicators"),
        REP_GENERATED_FOLDER("RepGenerated");

        private String key;

        private String alias;

        ETDQFolderType(String key) {
            this(key, key);
        }

        ETDQFolderType(String key, String alias) {
            this.key = key;
            this.alias = alias;
        }

        public String toString() {
            return getAlias();
        }

        public String getKey() {
            return this.key;
        }

        public String getAlias() {
            return this.alias;
        }

        public static ETDQFolderType getTypeFromKey(String key) {
            for (ETDQFolderType type : ETDQFolderType.values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }
            return null;
        }
    }

    public void setType(ERepositoryObjectType type) {
        this.type = type;
    }

    public TDQFolderObject(IRepositoryViewObject viewObject, ERepositoryObjectType type) {
        super(viewObject.getProperty());
        this.viewObject = viewObject;
        this.type = type;
    }
    public ETDQFolderType getFolderType() {
        return this.folderType;
    }

    public void setFolderType(ETDQFolderType type) {
        this.folderType = type;
    }

    public IRepositoryViewObject getViewObject() {
        return this.viewObject;
    }

    public String getId() {
        return this.getFolderType().getKey();
    }

    public String getLabel() {
        return this.getFolderType().getKey();
    }
}
