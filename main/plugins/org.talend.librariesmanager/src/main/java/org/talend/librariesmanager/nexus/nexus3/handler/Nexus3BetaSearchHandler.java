package org.talend.librariesmanager.nexus.nexus3.handler;

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
import org.talend.core.nexus.ArtifactRepositoryBean;

public class Nexus3BetaSearchHandler extends AbsNexus3SearchHandler {

    private String SEARCH_SERVICE = "service/rest/beta/search?"; //$NON-NLS-1$

    public Nexus3BetaSearchHandler(ArtifactRepositoryBean serverBean) {
        super(serverBean);
    }

    protected String getSearchUrl() {
        return this.getServerUrl() + SEARCH_SERVICE;
    }

    public String getHandlerVersion() { 
        return "Nexus3.beta"; //$NON-NLS-1$
    }  
}
