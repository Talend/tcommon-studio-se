// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.runtime.conf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * created by ycbai on Jul 31, 2014 Detailled comment
 * 
 * <p>
 * This class is used to resolve the default configuration file to get the default value. The configuration file must be
 * the json format. You can use {@link #getDefaultConfValue(String... args)} method to get the default value. Subclasses
 * should give their special configuration file to make it work.
 * </p>
 *
 */
public abstract class DefaultConfsManager {

    private Logger logger = Logger.getLogger(getClass());

    private String confFilePath;

    private Object confObj;

    protected DefaultConfsManager(String confFilePath) {
        this.confFilePath = confFilePath;
    }

    /**
     * DOC ycbai Comment method "getDefaultConfValue".
     * 
     * @param args are the arguments which used to find the default config value.
     * @return the default configuration value.
     */
    public String getDefaultConfValue(String... args) {
        Object val = getValue(args);
        if (val == null) {
            return null;
        }
        return String.valueOf(val);
    }

    private Object getValue(String... args) {
        Object value = null;
        if (args == null) {
            return null;
        }
        try {
            if (confObj == null) {
                initConfObj();
            }
            value = getValue(confObj, args);
        } catch (Exception e) {
            logger.warn("Fail to get the default value from \"" + confFilePath + "\". Please check it.", e); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return value;
    }

    private static Object getValue(Object object, String... args) {
        if (args.length > 0) {
            String arg = args[0];
            Object obj = ((JSONObject) object).get(arg);
            String[] newArgs = (String[]) ArrayUtils.remove(args, 0);
            return getValue(obj, newArgs);
        } else {
            return object;
        }
    }

    private void initConfObj() throws IOException, ParseException {
        Bundle b = FrameworkUtil.getBundle(getClass());
        URL url = FileLocator.toFileURL(FileLocator.find(b, new Path(confFilePath), null));
        if (url != null) {
            FileInputStream in = null;
            BufferedReader reader = null;
            try {
                in = new FileInputStream(url.getPath());
                reader = new BufferedReader(new InputStreamReader(in));
                JSONParser parser = new JSONParser();
                Object fileObj = parser.parse(reader);
                confObj = ((JSONObject) fileObj).get(getRootElement());
            } finally {
                if (in != null) {
                    in.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    protected abstract String getRootElement();

}
