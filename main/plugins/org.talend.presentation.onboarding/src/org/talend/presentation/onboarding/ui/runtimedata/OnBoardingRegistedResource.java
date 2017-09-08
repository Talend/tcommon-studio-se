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
package org.talend.presentation.onboarding.ui.runtimedata;

import java.net.URL;

import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.interfaces.IOnBoardingJsonI18n;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * created by cmeng on Sep 21, 2015 Detailled comment
 *
 */
public class OnBoardingRegistedResource {

    private URL url;

    private IOnBoardingJsonI18n i18n;

    private OnBoardingJsonDoc jsonDoc = null;

    public void reloadJsonDoc() {
        TypeReference<OnBoardingJsonDoc> typeReference = new TypeReference<OnBoardingJsonDoc>() {
            // no need to overwrite
        };
        try {
            jsonDoc = new ObjectMapper().readValue(url, typeReference);
        } catch (Throwable e) {
            OnBoardingExceptionHandler.process(e);
        }
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public IOnBoardingJsonI18n getI18n() {
        return this.i18n;
    }

    public void setI18n(IOnBoardingJsonI18n i18n) {
        this.i18n = i18n;
    }

    public OnBoardingJsonDoc getJsonDoc() {
        if (this.jsonDoc == null) {
            reloadJsonDoc();
        }
        return this.jsonDoc;
    }

    public void setJsonDoc(OnBoardingJsonDoc jsonDoc) {
        this.jsonDoc = jsonDoc;
    }

}
