package org.talend.librariesmanager.nexus.nexus3.handler;

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
