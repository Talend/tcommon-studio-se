// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.hadoop;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class HadoopConfJarBean {

    private boolean overrideCustomConf;

    private boolean isContextMode;

    /**
     * value saved in connection, it may be a context
     */
    private String overrideCustomConfPath;

    /**
     * original value of the path, if connection is in context mode, then this value if the default value of the context
     */
    private String originalOverrideCustomConfPath;

    private String customConfJarName;

    public HadoopConfJarBean(boolean isContextMode, boolean overrideCustomConf, String overrideCustomConfPath,
            String originalOverrideCustomConfPath, String customConfJarName) {
        this.isContextMode = isContextMode;
        this.overrideCustomConf = overrideCustomConf;
        this.overrideCustomConfPath = overrideCustomConfPath;
        this.originalOverrideCustomConfPath = originalOverrideCustomConfPath;
        this.customConfJarName = customConfJarName;
    }

    public boolean isContextMode() {
        return isContextMode;
    }

    public void setContextMode(boolean isContextMode) {
        this.isContextMode = isContextMode;
    }

    public boolean isOverrideCustomConf() {
        return overrideCustomConf;
    }

    public void setOverrideCustomConf(boolean overrideCustomConf) {
        this.overrideCustomConf = overrideCustomConf;
    }

    public String getOriginalOverrideCustomConfPath() {
        return originalOverrideCustomConfPath;
    }

    public void setOriginalOverrideCustomConfPath(String originalOverrideCustomConfPath) {
        this.originalOverrideCustomConfPath = originalOverrideCustomConfPath;
    }

    public String getOverrideCustomConfPath() {
        return overrideCustomConfPath;
    }

    public void setOverrideCustomConfPath(String overrideCustomConfPath) {
        this.overrideCustomConfPath = overrideCustomConfPath;
    }

    public String getCustomConfJarName() {
        return customConfJarName;
    }

    public void setCustomConfJarName(String customConfJarName) {
        this.customConfJarName = customConfJarName;
    }

    @Override
    public String toString() {
        return "HadoopConfJarBean [overrideCustomConf=" + overrideCustomConf + ", isContextMode=" + isContextMode
                + ", overrideCustomConfPath=" + overrideCustomConfPath + ", originalOverrideCustomConfPath="
                + originalOverrideCustomConfPath + ", customConfJarName=" + customConfJarName + "]";
    }

}
