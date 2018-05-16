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
package org.talend.commons.ui.html;

import java.util.Properties;

import org.eclipse.ui.internal.intro.impl.model.loader.ModelLoaderUtil;
import org.eclipse.ui.internal.intro.impl.util.StringUtil;
import org.eclipse.ui.intro.config.IIntroAction;
import org.eclipse.ui.intro.config.IIntroURL;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * 
 * DOC wchen class global comment. Detailled comment
 */
public class DynamicHtmlURL implements IIntroURL {

    private String action = null;

    private Properties parameters = null;

    DynamicHtmlURL(String action, Properties parameters) {
        this.action = action;
        this.parameters = parameters;
    }

    public boolean execute() {
        if (action.equals(IntroURL.RUN_ACTION)) {
            return runAction(getParameter(IntroURL.KEY_PLUGIN_ID), getParameter(IntroURL.KEY_CLASS), parameters,
                    getParameter(IntroURL.KEY_STANDBY));
        }
        return false;

    }

    public String getAction() {
        return this.action;
    }

    public String getParameter(String parameterId) {
        // make sure to decode only on return, since we may need to recreate the
        // url when handling custom urls.
        String value = parameters.getProperty(parameterId);
        String decode = parameters.getProperty(IntroURL.KEY_DECODE);

        if (value != null)
            try {
                if (decode != null && decode.equalsIgnoreCase(IntroURL.VALUE_TRUE))
                    // we are told to decode the parameters of the url through
                    // the decode parameter. Assume that parameters are
                    // UTF-8 encoded.
                    return StringUtil.decode(value, "UTF-8"); //$NON-NLS-1$
                return value;
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        return value;
    }

    private boolean runAction(String pluginId, String className, Properties parameters, String standbyState) {

        Object actionObject = ModelLoaderUtil.createClassInstance(pluginId, className);
        try {
            if (actionObject instanceof IIntroAction) {
                IIntroAction introAction = (IIntroAction) actionObject;
                introAction.run(null, parameters);
            }
            return true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

}
