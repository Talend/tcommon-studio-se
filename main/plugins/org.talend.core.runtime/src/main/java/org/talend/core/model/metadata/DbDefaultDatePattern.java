package org.talend.core.model.metadata;

public class DbDefaultDatePattern {
    private String defaultPattern;
    private String dbTypeName;

    public DbDefaultDatePattern() {

    }

    public String getDefaultPattern() {
        return defaultPattern;
    }

    public void setDefaultPattern(String defaultPattern) {
        this.defaultPattern = defaultPattern;
    }

    public String getDbTypeName() {
        return dbTypeName;
    }

    public void setDbTypeName(String dbTypeName) {
        this.dbTypeName = dbTypeName;
    }
}
