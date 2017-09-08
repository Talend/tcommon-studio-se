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
package org.talend.core.database.conn.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.DatabaseConnConstants;
import org.talend.core.database.conn.EDatabaseConnVar;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;

/**
 * cli class global comment. Detailled comment
 */
class DbConnStr {

    protected EDatabaseTypeName dbType;

    protected String urlTemplate;

    protected String urlPattern = ""; //$NON-NLS-1$

    protected String defaultPort;

    protected String additionProperty;

    // for hbase only need server name and port can create a connection
    protected String server;
    
    protected String defaultServer;
    
    protected String defaultDB;

    protected EDatabaseVersion4Drivers[] dbVersions;

    DbConnStr(EDatabaseTypeName dbType, String urlTemplate, String defaultPort, String additionProperty,
            EDatabaseVersion4Drivers[] dbVersions) {
        this.dbType = dbType;
        this.urlTemplate = urlTemplate;
        this.defaultPort = defaultPort;
        this.additionProperty = additionProperty;
        this.dbVersions = dbVersions;
        this.urlPattern = calcPattern(urlTemplate);
    }
    
    DbConnStr(EDatabaseTypeName dbType, String urlTemplate, String defaultPort, String additionProperty,
            EDatabaseVersion4Drivers[] dbVersions , String defaultServer, String defaultDB) {
        this.dbType = dbType;
        this.urlTemplate = urlTemplate;
        this.defaultPort = defaultPort;
        this.additionProperty = additionProperty;
        this.dbVersions = dbVersions;
        this.urlPattern = calcPattern(urlTemplate);
        this.defaultServer = defaultServer;
        this.defaultDB = defaultDB;
    }

    DbConnStr(EDatabaseTypeName dbType, String urlTemplate, String defaultPort, String additionProperty) {
        this(dbType, urlTemplate, defaultPort, additionProperty, null);
    }

    DbConnStr(EDatabaseTypeName dbType, String urlTemplate, String defaultPort, EDatabaseVersion4Drivers[] dbVersions) {
        this(dbType, urlTemplate, defaultPort, null, dbVersions);
    }

    DbConnStr(EDatabaseTypeName dbType, String urlTemplate, EDatabaseVersion4Drivers[] dbVersions) {
        this(dbType, urlTemplate, null, null, dbVersions);
    }

    DbConnStr(EDatabaseTypeName dbType, String urlTemplate, String defaultPort) {
        this(dbType, urlTemplate, defaultPort, null, null);
    }

    DbConnStr(EDatabaseTypeName dbType, String urlTemplate) {
        this(dbType, urlTemplate, null, null, null);
    }

    /**
     * 
     * cli Comment method "calcPattern".
     */
    protected String calcPattern(final String urlTemplate) {
        String str = preQuote(urlTemplate);

        return replaceVarToPattern(str);

    }

    /**
     * 
     * cli Comment method "replaceVarToPattern".
     * 
     */
    protected String replaceVarToPattern(String url) {
        for (EDatabaseConnVar var : EDatabaseConnVar.values()) {
            String p = checkAndProcess(var.getPattern());
            if (p != null) {
                String variable = checkAndProcess(var.getVariable());
                if (variable != null) {
                    url = url.replaceAll(variable, p);
                }
            }
        }
        return url;
    }

    /**
     * cli Comment method "preQuote".
     */
    protected String preQuote(final String urlTemplate) {
        List<String> matchList = new ArrayList<String>();
        try {
            Perl5Compiler compiler = new Perl5Compiler();
            Perl5Matcher matcher = new Perl5Matcher();
            Pattern pattern = compiler.compile(DatabaseConnConstants.PATTERN_VAR);
            PatternMatcherInput pinput;

            pinput = new PatternMatcherInput(urlTemplate);
            int beginOffset = 0;
            MatchResult currentResult;
            String tmp;
            while (matcher.contains(pinput, pattern)) {
                currentResult = matcher.getMatch();
                tmp = urlTemplate.substring(beginOffset, currentResult.beginOffset(0));
                tmp = checkAndProcess(tmp);
                if (tmp != null) {
                    matchList.add(tmp);
                }
                matchList.add(currentResult.group(0));
                beginOffset = currentResult.endOffset(0);
            }

            tmp = checkAndProcess(urlTemplate.substring(beginOffset, urlTemplate.length()));
            if (tmp != null) {
                matchList.add(tmp);
            }

        } catch (MalformedPatternException e) {
            //
        }

        StringBuffer sb = new StringBuffer();
        for (String tmp : matchList) {
            sb.append(tmp);
        }
        return sb.toString();
    }

    private String checkAndProcess(String str) {
        if (str != null && !"".equals(str.trim())) { //$NON-NLS-1$
            return Perl5Compiler.quotemeta(str);
        }
        return null;
    }

    EDatabaseTypeName getDbType() {
        return this.dbType;
    }

    String getDBTypeName() {
        if (this.dbType != null) {
            return this.dbType.getDisplayName();
        }
        return null;
    }

    String getUrlTemplate(EDatabaseVersion4Drivers version) {
        return this.urlTemplate; // default, no need version
    }
    
    String getDefaultServer(EDatabaseVersion4Drivers version) {
        return this.defaultServer; // default, no need version
    }
    
    String getDefaultDB(EDatabaseVersion4Drivers version) {
        return this.defaultDB; // default, no need version
    }

    String getUrlPattern(EDatabaseVersion4Drivers version) {
        return this.urlPattern; // default, no need version
    }

    String getDefaultPort() {
        return this.defaultPort;
    }

    String getAdditionProperty() {
        return this.additionProperty;
    }

    boolean isMultiVersion() {
        return this.dbVersions != null && this.dbVersions.length > 0;
    }

    EDatabaseVersion4Drivers[] getDbVersions() {
        return this.dbVersions;
    }

    String processStr(EDatabaseVersion4Drivers version, String str) {
        return str; // default, don't process
    }
}
