package org.talend.librariesmanager.nexus.nexus3.handler;

import org.talend.core.nexus.ArtifactRepositoryBean;

public class Nexus3V1SearchHandler extends AbsNexus3SearchHandler {

    private String SEARCH_SERVICE = "service/rest/v1/search?";

    public Nexus3V1SearchHandler(ArtifactRepositoryBean serverBean) {
        super(serverBean);
    }

    protected String getSearchUrl() {
        return this.getServerUrl() + SEARCH_SERVICE;
    }

    public String getHandlerVersion() {
        return "Nexus3.V1"; //$NON-NLS-1$
    }
}
